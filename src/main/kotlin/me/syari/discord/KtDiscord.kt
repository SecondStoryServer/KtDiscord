package me.syari.discord

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object KtDiscord {
    const val NAME = "KtDiscord"
    const val VERSION = "1.0-dev"
    const val GITHUB_URL = "https://github.com/sya-ri/KtDiscord"

    val LOGGER: Logger = LoggerFactory.getLogger(NAME)

    init {
        LOGGER.info("$NAME v$VERSION ($GITHUB_URL)")
    }

    var bot_token = ""
}