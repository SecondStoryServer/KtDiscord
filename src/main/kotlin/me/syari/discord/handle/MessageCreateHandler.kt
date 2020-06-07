package me.syari.discord.handle

import com.google.gson.JsonObject
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.entity.api.Member
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
        val mentionMembers = getMentionMembers(data)
        LOGGER.debug(mentionMembers.toString())
    }

    private fun getMentionMembers(parent: JsonObject): List<Member> {
        val array = parent.getArrayOrNull("mentions")
        return array?.map {
            val data = it.asJsonObject
            val user = UserImpl(data)
            val memberObject = data["member"].asJsonObject
            MemberImpl(memberObject, user)
        } ?: emptyList()
    }
}