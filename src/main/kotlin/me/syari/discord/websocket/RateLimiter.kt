package me.syari.discord.websocket

object RateLimiter {
    private const val PERIOD = 60 * 1000
    private const val RATE = 100

    private val times = mutableListOf<Long>()

    fun isLimited(): Boolean {
        synchronized(times) {
            val currentTime = System.currentTimeMillis()
            times.removeIf { it < currentTime - PERIOD }
            return RATE <= times.size
        }
    }

    fun increment() {
        synchronized(times) {
            times.add(System.currentTimeMillis())
        }
    }
}