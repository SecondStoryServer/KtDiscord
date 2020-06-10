import me.syari.discord.KtDiscord

fun main() {
    KtDiscord.loginAsync(BOT_TOKEN) { message ->
        KtDiscord.LOGGER.debug(message.toString())
    }
    while (true) {
    }
}