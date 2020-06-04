package me.syari.discord.exception

class DiscordException(override val message: String?, val code: Int? = null): Exception()