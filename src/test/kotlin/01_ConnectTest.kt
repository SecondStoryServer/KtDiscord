import me.syari.discord.BOT_TOKEN
import me.syari.discord.KtDiscord
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.rest.EndPoint
import me.syari.discord.rest.RestClient

suspend fun main() {
    KtDiscord.bot_token = BOT_TOKEN
    RestClient.request(EndPoint.GET_GATEWAY_BOT)
    LOGGER.debug("return")
}