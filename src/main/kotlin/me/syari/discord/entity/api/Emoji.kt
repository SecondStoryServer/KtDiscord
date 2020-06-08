package me.syari.discord.entity.api

data class Emoji(val name: String, val id: Long, val isAnimated: Boolean) {
    companion object {
        internal const val REGEX = "<a?:([a-zA-Z0-9_]+):([0-9]+)>"
    }
}