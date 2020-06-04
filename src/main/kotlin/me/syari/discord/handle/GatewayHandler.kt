package me.syari.discord.handle

import com.google.gson.JsonObject

interface GatewayHandler {
    fun handle(data: JsonObject)
}