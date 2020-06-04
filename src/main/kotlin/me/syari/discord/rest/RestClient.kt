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
import me.syari.discord.KtDiscord.GITHUB_URL
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.KtDiscord.bot_token

object RestClient {
    private val HTTP_CLIENT = HttpClient(OkHttp)
    private const val DISCORD_API_URL = "https://discord.com/api/v6"
    private val GSON = Gson()

    suspend fun request(endPoint: EndPoint, data: JsonObject? = null) {
        val response = HTTP_CLIENT.request<HttpResponse> {
            val url = DISCORD_API_URL + endPoint.path
            method = endPoint.method
            header(HttpHeaders.Accept, "application/json")
            header(HttpHeaders.Authorization, "Bot $bot_token")
            header(HttpHeaders.UserAgent, "DiscordBot ($GITHUB_URL)")
            url(url)
            if (method == HttpMethod.Get) {
                if (data != null) {
                    val parameter = data.entrySet().joinToString("&") { "${it.key}=${it.value}" }
                    url("$url?$parameter")
                }
            } else {
                body = TextContent(data?.toString() ?: "{}", ContentType.Application.Json)
            }
        }
        val contentType = response.headers["Content-Type"]
        val body = response.readText()
        val json = if (contentType?.equals("application/json", true) == true) {
            GSON.fromJson(body, JsonElement::class.java)
        } else {
            null
        }
        LOGGER.debug("Response: ${response.status.value}, Body: $json")
    }
}