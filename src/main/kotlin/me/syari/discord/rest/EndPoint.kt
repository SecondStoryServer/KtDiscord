package me.syari.discord.rest

import io.ktor.http.HttpMethod

enum class EndPoint(val method: HttpMethod, val path: String) {
    GET_GATEWAY_BOT(HttpMethod.Get, "/gateway/bot")
}