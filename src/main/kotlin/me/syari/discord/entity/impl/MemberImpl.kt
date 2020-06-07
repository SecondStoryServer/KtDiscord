package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.api.Member
import me.syari.discord.entity.api.User
import me.syari.discord.util.json.JsonUtil.asStringOrNull
import me.syari.discord.util.json.JsonUtil.getOrNull

class MemberImpl(json: JsonObject, user: User): Member {
    override val name = user.name
    override var nickName = json.getOrNull("nick")?.asStringOrNull
    override val id = user.id
    override val isBot = user.isBot

    override fun toString(): String {
        return "Member(name: $name, nick: $nickName, id: $id, isBot: $isBot)"
    }
}