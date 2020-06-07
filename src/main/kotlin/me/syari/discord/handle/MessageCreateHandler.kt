package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.entity.impl.MemberImpl
import me.syari.discord.entity.impl.UserImpl

object MessageCreateHandler: GatewayHandler {
    override fun handle(data: JsonObject) {
        LOGGER.debug("MessageCreateHandler $data")
        val authorObject = data["author"].asJsonObject
        val author = UserImpl(authorObject)
        val memberObject = data["member"].asJsonObject
        val member = MemberImpl(memberObject, author)
        val content = data["content"].asString
        LOGGER.debug(author.toString())
        LOGGER.debug(member.toString())
        LOGGER.debug(content)
    }
}