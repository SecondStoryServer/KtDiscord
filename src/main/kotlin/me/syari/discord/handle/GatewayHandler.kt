package me.syari.discord.handle

import com.google.gson.JsonObject

internal interface GatewayHandler {
    fun handle(json: JsonObject)
}