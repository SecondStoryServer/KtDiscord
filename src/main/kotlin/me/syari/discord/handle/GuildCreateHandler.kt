package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.entity.api.Guild
import me.syari.discord.util.json.JsonUtil.getOrNull

object GuildCreateHandler: GatewayHandler {
    override fun handle(json: JsonObject) {
        if (json.getOrNull("unavailable")?.asBoolean == true) {
            return
        }

        val id = json["id"].asLong

        Guild.putOrUpdate(id, json)
    }
}