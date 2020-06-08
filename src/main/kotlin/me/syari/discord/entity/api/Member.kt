package me.syari.discord.entity.api

import me.syari.discord.entity.Mentionable

interface Member: User, Mentionable {
    val nickName: String?

    val displayName
        get() = nickName ?: name

    override val asMentionDisplay: String
        get() = "@$displayName"

    override val asMentionRegex: Regex
        get() = "<@!?$id>".toRegex()
}