package me.syari.discord.entity.`interface`

interface Mentionable {
    val asMentionRegex: Regex

    val asMentionDisplay: String

    fun String.replaceMention() = replace(asMentionRegex, asMentionDisplay)
}