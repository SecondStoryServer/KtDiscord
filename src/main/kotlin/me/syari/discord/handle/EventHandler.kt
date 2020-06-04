package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.KtDiscord.LOGGER

object EventHandler {
    private val handlers = mapOf(
        "GUILD_CREATE" to GuildCreateHandler, "MESSAGE_CREATE" to MessageCreateHandler
    )

    fun handleEvent(eventType: String, data: JsonObject) {
        try {
            handlers[eventType]?.handle(data)
        } catch (ex: Exception) {
            LOGGER.error("Failed to handle the event! (type: $eventType, json: $data)", ex)
        }
    }
}