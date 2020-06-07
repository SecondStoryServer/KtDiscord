package me.syari.discord.entity.api

interface Guild {
    fun getTextChannel(id: Long): TextChannel?
}