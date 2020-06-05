package me.syari.discord.entity.api

import com.google.gson.JsonObject

data class Role(val name: String, val id: Long) {
    constructor(json: JsonObject): this(json["name"].asString, json["id"].asLong)
}