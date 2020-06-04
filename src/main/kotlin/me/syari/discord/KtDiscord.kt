package me.syari.discord

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

object KtDiscord {
    internal val httpClient = HttpClient(OkHttp)
}