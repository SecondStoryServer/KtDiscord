package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.KtDiscord.LOGGER

object MessageCreateHandler: GatewayHandler {
    override fun handle(data: JsonObject) {
        LOGGER.debug("MessageCreateHandler $data")
    }
}