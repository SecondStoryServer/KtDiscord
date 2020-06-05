package me.syari.discord.entity.`interface`

interface Mentionable {
    val asMentionRegex: String

    val asMentionDisplay: String

    fun String.replaceMention() = replace(asMentionRegex, asMentionDisplay)
}