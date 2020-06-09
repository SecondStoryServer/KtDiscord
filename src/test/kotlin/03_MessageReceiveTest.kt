import me.syari.discord.KtDiscord
import me.syari.discord.KtDiscord.LOGGER

suspend fun main() {
    KtDiscord.login(BOT_TOKEN) { message ->
        LOGGER.debug(message.toString())
    }
    LOGGER.debug("return")
}