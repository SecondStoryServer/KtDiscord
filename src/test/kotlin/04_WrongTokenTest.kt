import me.syari.discord.KtDiscord

suspend fun main() {
    KtDiscord.login("wrong") {}
    KtDiscord.LOGGER.debug("return")
}