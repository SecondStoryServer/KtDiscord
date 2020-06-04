package me.syari.discord.websocket

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import me.syari.discord.ConnectStatus
import me.syari.discord.KtDiscord
import me.syari.discord.KtDiscord.API_VERSION
import me.syari.discord.KtDiscord.LOGGER
import me.syari.discord.KtDiscord.gatewayIntents
import me.syari.discord.KtDiscord.maxShards
import me.syari.discord.KtDiscord.shard
import me.syari.discord.KtDiscord.token
import me.syari.discord.util.json.JsonUtil.json
import me.syari.discord.util.json.JsonUtil.jsonArray
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object GatewayClient {
    private lateinit var websocket: WebSocket

    @Volatile
    private var sessionId: String? = null

    fun connect(gatewayURL: String) {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().url("$gatewayURL/?v=$API_VERSION&encoding=json&compress=zlib-stream").build()
        websocket = client.newWebSocket(request, Listener)
    }

    private fun authenticate() {
        send(Opcode.IDENTIFY, json {
            "token" to token

            if (gatewayIntents.isNotEmpty()) {
                var value = 0
                gatewayIntents.forEach {
                    value = value or it.flag
                }
                LOGGER.trace("Intent flag: $value")
                "intents" to value
            }

            "properties" to json {
                "\$os" to "who knows"
                "\$browser" to "who knows"
                "\$device" to "who knows"
            }

            "shard" to jsonArray {
                +JsonPrimitive(shard)
                +JsonPrimitive(maxShards)
            }

            //            activity?.let {
            //                "presence" to it
            //            }
        })
    }

    private fun resume() {

    }

    private fun send(opCode: Opcode, data: JsonObject) {
        val json = json {
            "op" to opCode.code
            "d" to data
        }.toString()

        LOGGER.trace("Sent: $json")
        websocket.send(json)
        RateLimiter.increment()
    }

    object Listener: WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            LOGGER.info("Connected to the gateway")
            KtDiscord.status = ConnectStatus.CONNECTED

            if (sessionId == null) {
                LOGGER.info("Authenticating...")
                authenticate()
            } else {
                LOGGER.info("Resuming the session...")
                resume()
            }
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