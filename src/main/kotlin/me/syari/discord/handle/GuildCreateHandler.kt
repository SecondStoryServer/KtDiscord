package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.entity.impl.ServerImpl
import me.syari.discord.util.json.JsonUtil.getOrNull

object GuildCreateHandler: GatewayHandler {
    override fun handle(data: JsonObject) {
        if (data.getOrNull("unavailable")?.asBoolean == true) {
            return
        }

        val id = data["id"].asLong

        ServerImpl.putOrUpdate(id, data)
    }
}