package me.syari.discord.entity

interface Mentionable {
    val asMentionRegex: String

    val asMentionDisplay: String

    fun String.replaceMention() = replace(asMentionRegex, asMentionDisplay)
}