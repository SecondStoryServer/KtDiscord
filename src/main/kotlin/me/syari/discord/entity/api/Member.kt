package me.syari.discord.entity.api

interface Member: User {
    val nickName: String?

    val displayName
        get() = nickName ?: name
}