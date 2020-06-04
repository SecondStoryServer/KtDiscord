package me.syari.discord.rest

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import me.syari.discord.KtDiscord.GITHUB_URL
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.KtDiscord.bot_token

object RestClient {
    private val HTTP_CLIENT = HttpClient(OkHttp)
    private const val DISCORD_API_URL = "https://discord.com/api/v6"

    suspend fun request(endPoint: EndPoint) {
        val response = HTTP_CLIENT.request<HttpResponse> {
            method = endPoint.method
            url(DISCORD_API_URL + endPoint.path)
            header(HttpHeaders.Authorization, "Bot $bot_token")
            header(HttpHeaders.UserAgent, "DiscordBot ($GITHUB_URL)")
        }
        LOGGER.debug(response.toString())
    }
}