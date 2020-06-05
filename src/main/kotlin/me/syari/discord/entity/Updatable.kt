package me.syari.discord.entity

import com.google.gson.JsonObject

interface Updatable {
    fun update(json: JsonObject)
}