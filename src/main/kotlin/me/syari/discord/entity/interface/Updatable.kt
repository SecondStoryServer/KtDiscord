package me.syari.discord.entity.`interface`

import com.google.gson.JsonObject

interface Updatable {
    fun update(json: JsonObject)
}