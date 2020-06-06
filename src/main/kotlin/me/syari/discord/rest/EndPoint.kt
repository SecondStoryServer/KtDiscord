package me.syari.discord.rest

import io.ktor.http.HttpMethod

sealed class EndPoint(val method: HttpMethod, val path: String) {
    object GetGatewayBot: EndPoint(HttpMethod.Get, "/gateway/bot")
    class CreateMessage(channel_id: Long): EndPoint(HttpMethod.Post, "/channels/$channel_id/messages")
}