package me.syari.discord.entity.impl

import com.google.gson.JsonObject
import me.syari.discord.entity.Updatable
import me.syari.discord.entity.api.Server

class ServerImpl: Server, Updatable {
    private val roles = mutableMapOf<Long, String>()

    override fun update(json: JsonObject) {
        updateRole(json)
    }

    private fun updateRole(json: JsonObject) {
        val roleObjects = json["roles"].asJsonArray.map { it.asJsonObject }
        roleObjects.forEach {
            val roleName = it["name"].asString
            val roleId = it["id"].asLong
            roles[roleId] = roleName
        }
    }
}