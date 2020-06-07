package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.entity.impl.MemberImpl
import me.syari.discord.entity.impl.UserImpl
import me.syari.discord.util.json.JsonUtil.getArrayOrNull

object MessageCreateHandler: GatewayHandler {
    override fun handle(data: JsonObject) {
        LOGGER.debug("MessageCreateHandler $data")
        val authorObject = data["author"].asJsonObject
        val author = UserImpl(authorObject)
        val memberObject = data["member"].asJsonObject
        val member = MemberImpl(memberObject, author)
        val content = data["content"].asString
        val mentionObjects = data.getArrayOrNull("mentions")
        LOGGER.debug(mentionObjects.toString())
    }
}