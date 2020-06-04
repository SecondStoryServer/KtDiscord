package me.syari.discord.websocket

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
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
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.Inflater
import java.util.zip.InflaterOutputStream

@KtorExperimentalAPI
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

    private fun handleMessage(websocket: WebSocket, text: String) {
        LOGGER.debug(text)
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

        private val buffer = mutableListOf<ByteArray>()
        private val inflater = Inflater()
        private val ZLIB_SUFFIX = hex("0000ffff")

        private fun ByteArray.takeLastAsByteArray(n: Int): ByteArray {
            return ByteArray(n).also { result ->
                for (i in 0 until n) {
                    result[i] = this[size - n + i]
                }
            }
        }

        private fun Collection<ByteArray>.concat(): ByteArray {
            val length = sumBy { it.size }
            return ByteArray(length).also { output ->
                var pos = 0
                forEach {
                    System.arraycopy(it, 0, output, pos, it.size)
                    pos += it.size
                }
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            val byteArray = bytes.toByteArray()

            // Add the received data to buffer
            buffer.add(byteArray)

            // Check for zlib suffix
            if (byteArray.size < 4 || !byteArray.takeLastAsByteArray(4).contentEquals(ZLIB_SUFFIX)) {
                return
            }

            // Decompress the buffered data
            val text = ByteArrayOutputStream().use { output ->
                try {
                    InflaterOutputStream(output, inflater).use {
                        it.write(buffer.concat())
                    }
                    output.toString("UTF-8")
                } catch (e: IOException) {
                    LOGGER.error("Error while decompressing payload", e)
                    return
                } finally {
                    buffer.clear()
                }
            }

            // Handle the message
            handleMessage(websocket, text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            LOGGER.debug("onClosing")
        }

        override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
            LOGGER.debug("onFailure")
        }
    }
}