import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import me.syari.discord.BOT_TOKEN
import me.syari.discord.KtDiscord.httpClient

suspend fun main(){
    val response = httpClient.get<String>("https://discord.com/api/v6/gateway/bot"){
        header(HttpHeaders.Authorization, "Bot $BOT_TOKEN")
    }
    println(response)
}