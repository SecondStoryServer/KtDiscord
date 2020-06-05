package me.syari.discord.util.json

import com.google.gson.JsonElement
import com.google.gson.JsonObject

object JsonUtil {
    fun json(builder: JsonBuilder.() -> Unit) = JsonBuilder().apply(builder).get()

    fun jsonArray(builder: JsonArrayBuilder.() -> Unit) = JsonArrayBuilder().apply(builder).get()

    fun JsonObject.getOrNull(memberName: String): JsonElement? {
        return if (has(memberName)) get(memberName) else null
    }

    fun JsonObject.getObjectOrNull(memberName: String): JsonObject? {
        if (!has(memberName)) return null
        val member = get(memberName)
        return if (member.isJsonObject) member.asJsonObject else null
    }

    val JsonElement.asStringOrNull
        get() = if (isJsonPrimitive && asJsonPrimitive.isString) asString else null
}