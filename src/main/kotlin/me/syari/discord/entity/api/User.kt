package me.syari.discord.entity.api

interface User {
    val name: String

    val id: Long

    val isBot: Boolean
}