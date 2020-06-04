package me.syari.discord.rest

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.withLock
import me.syari.discord.KtDiscord.GITHUB_URL
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.KtDiscord.bot_token
import me.syari.discord.exception.DiscordException
import me.syari.discord.exception.MissingPermissionsException
import me.syari.discord.exception.NotFoundException
import me.syari.discord.exception.RateLimitedException

object RestClient {
    private val HTTP_CLIENT = HttpClient(OkHttp)
    private const val DISCORD_API_URL = "https://discord.com/api/v6"
    private val GSON = Gson()

    suspend fun request(endPoint: EndPoint, data: JsonObject? = null, rateLimitRetries: Int = 50): JsonElement {
        RateLimiter.getMutex(endPoint).withLock {
            repeat(rateLimitRetries) {
                RateLimiter.wait(endPoint)

                try {
                    var url = DISCORD_API_URL + endPoint.path
                    val response = HTTP_CLIENT.request<HttpResponse> {
                        method = endPoint.method
                        header(HttpHeaders.Accept, "application/json")
                        header(HttpHeaders.Authorization, "Bot $bot_token")
                        header(HttpHeaders.UserAgent, "DiscordBot ($GITHUB_URL)")
                        if (method == HttpMethod.Get) {
                            if (data != null) {
                                val parameter = data.entrySet().joinToString("&") { "${it.key}=${it.value}" }
                                url += "?$parameter"
                            }
                        } else {
                            body = TextContent(data?.toString() ?: "{}", ContentType.Application.Json)
                        }
                        url(url)
                    }
                    val contentType = response.headers["Content-Type"]
                    val body = response.readText()
                    val json = if (contentType?.equals("application/json", true) == true) {
                        GSON.fromJson(body, JsonElement::class.java)
                    } else {
                        null
                    }
                    LOGGER.debug("Response: ${response.status.value}, Body: $json")

                    // Update rate limits
                    val rateLimit = response.headers["X-RateLimit-Limit"]?.toInt()
                    val rateLimitRemaining = response.headers["X-RateLimit-Remaining"]?.toInt()
                    val rateLimitEnds = response.headers["X-RateLimit-Reset"]?.toLong()

                    if (response.status.value !in 200..299) {
                        // When failed to request
                        RateLimiter.incrementRateLimitRemaining(endPoint)
                    }

                    if (rateLimit != null && rateLimitRemaining != null && rateLimitEnds != null) {
                        RateLimiter.setRateLimit(endPoint, rateLimit)
                        RateLimiter.setRateLimitRemaining(endPoint, rateLimitRemaining)
                        RateLimiter.setRateLimitEnds(endPoint, rateLimitEnds * 1000)
                        LOGGER.debug("RateLimit: $rateLimit, Remaining: $rateLimitRemaining, Ends: $rateLimitEnds")
                    }

                    // Handle rate limits (429 Too Many Requests)
                    if (response.status.value == 429) {
                        if (json == null) {
                            // When get rate limited without body
                            throw RateLimitedException()
                        }

                        val delay = json.asJsonObject["retry_after"].asLong

                        if (json.asJsonObject["global"].asBoolean) {
                            RateLimiter.setGlobalRateLimitEnds(delay)
                        } else {
                            RateLimiter.setRateLimitEnds(endPoint, System.currentTimeMillis() + delay)
                            RateLimiter.setRateLimitRemaining(endPoint, 0)
                        }

                        return@repeat
                    }

                    if (response.status.value in 500..599) {
                        throw Exception("Discord API returned internal server error (code: ${response.status.value})") // Retry
                    }

                    if (response.status.value == 403) {
                        throw MissingPermissionsException("Request: $url, Response: $body")
                    }

                    if (response.status.value == 404) {
                        throw NotFoundException(true)
                    }

                    if (response.status.value !in 200..299) {
                        throw DiscordException(
                            "Discord API returned status code ${response.status.value} with body ${json?.toString()}",
                            response.status.value
                        )
                    }

                    return json ?: JsonObject()
                } catch (ex: DiscordException) {
                    throw ex
                } catch (ex: RateLimitedException) {
                    throw ex
                } catch (ex: NotFoundException) {
                    throw ex
                } catch (ex: MissingPermissionsException) {
                    throw ex
                } catch (ex: Exception) {
                    LOGGER.warn("An unexpected error has occurred! we will retry in a second", ex)
                    delay(1000)
                }
            }

            throw RateLimitedException()
        }
    }
}