package me.syari.discord.util.json

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject

class JsonBuilder {
    private val jsonObject = JsonObject()

    infix fun String.to(value: Number?) {
        jsonObject.addProperty(this, value)
    }

    infix fun String.to(value: String?) {
        jsonObject.addProperty(this, value)
    }

    infix fun String.to(value: Boolean?) {
        jsonObject.addProperty(this, value)
    }

    infix fun String.to(value: JsonElement?) {
        jsonObject.add(this, value)
    }

    fun String.toNull() {
        jsonObject.add(this, JsonNull.INSTANCE)
    }

    fun get() = jsonObject
}