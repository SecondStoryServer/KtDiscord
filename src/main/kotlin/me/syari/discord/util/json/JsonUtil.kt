package me.syari.discord.util.json

object JsonUtil {
    fun json(builder: JsonBuilder.() -> Unit) = JsonBuilder().apply(builder).get()

    fun jsonArray(builder: JsonArrayBuilder.() -> Unit) = JsonArrayBuilder().apply(builder).get()
}