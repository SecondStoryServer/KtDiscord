package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.KtDiscord

object GuildCreateHandler: GatewayHandler {
    override fun handle(data: JsonObject) {
        KtDiscord.LOGGER.debug("GuildCreateHandler $data")
    }
}