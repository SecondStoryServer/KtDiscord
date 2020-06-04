package me.syari.discord.exception

open class DiscordException(override val message: String?, val code: Int? = null): Exception()