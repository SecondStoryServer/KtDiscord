import me.syari.discord.KtDiscord
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.entity.api.TextChannel

suspend fun main() {
    KtDiscord.login(BOT_TOKEN) {}
    TextChannel.get(TEST_TEXT_CHANNEL)?.send("Hello!!")
    LOGGER.debug("return")
}