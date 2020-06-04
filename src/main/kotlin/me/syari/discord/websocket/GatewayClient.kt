package me.syari.discord.websocket

import me.syari.discord.KtDiscord.LOGGER
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object GatewayClient {
    private lateinit var websocket: WebSocket

    fun connect(gatewayURL: String) {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().url(gatewayURL).build()
        websocket = client.newWebSocket(request, Listener)
    }

    object Listener: WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            LOGGER.debug("onOpen")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            LOGGER.debug("onMessage String")
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            LOGGER.debug("onMessage Byte")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            LOGGER.debug("onClosing")
        }

        override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
            LOGGER.debug("onFailure")
        }
    }
}