package com.wwe.Volatile

import java.util.concurrent.atomic.AtomicInteger

/** volatile 不适用：a++ */
class VolatileTask : Runnable {

    @Volatile
    var a: Int = 0

    val realA = AtomicInteger()

    override fun run() {
        (0 until 1000).forEach { _ ->
            a++
            realA.incrementAndGet()
        }
    }
}