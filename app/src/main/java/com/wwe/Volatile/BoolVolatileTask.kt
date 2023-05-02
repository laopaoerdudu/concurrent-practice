package com.wwe.Volatile

import java.util.concurrent.atomic.AtomicInteger

class BoolVolatileTask : Runnable {

    @Volatile
    var a: Boolean = false

    val realA = AtomicInteger()

    override fun run() {
        (0 until 1000).forEach { _ ->
            a = true
            realA.incrementAndGet()
        }
    }
}