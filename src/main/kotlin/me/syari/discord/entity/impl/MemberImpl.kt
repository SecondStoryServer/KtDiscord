package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.Updatable
import me.syari.discord.entity.api.Member
import me.syari.discord.entity.api.User
import me.syari.discord.util.json.JsonUtil.asStringOrNull
import me.syari.discord.util.json.JsonUtil.getOrNull

class MemberImpl(json: JsonObject, user: User): Member, Updatable {
    override val name = user.name
    override var nickName: String? = null
    override val isBot = user.isBot

    init {
        update(json)
    }

    override fun update(json: JsonObject) {
        json.getOrNull("nick")?.let { nickName = it.asStringOrNull }
    }
}