package com.wwe.atomic

import java.util.concurrent.atomic.AtomicInteger

class AtomicTask(private val atomicInteger: AtomicInteger) : Runnable {

    override fun run() {
        (0 until 1000).forEach { _ ->
            atomicInteger.incrementAndGet()
        }
    }
}