package me.syari.discord.entity.api

import me.syari.discord.entity.Mentionable

internal data class Role(val name: String, val id: Long): Mentionable {
    override val asMentionDisplay: String
        get() = "@$name"

    override val asMentionRegex: Regex
        get() = "<@&$id>".toRegex()
}