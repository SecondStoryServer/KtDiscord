package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.api.Role

class RoleImpl(json: JsonObject): Role {
    override var name: String = json["name"].asString
    override val id = json["id"].asLong
}