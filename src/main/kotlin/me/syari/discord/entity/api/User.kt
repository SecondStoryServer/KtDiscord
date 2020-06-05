package me.syari.discord.entity.api

import me.syari.discord.entity.Updatable

interface User: Updatable {
    val name: String

    val isBot: Boolean
}