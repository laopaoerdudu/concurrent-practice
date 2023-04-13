package com.wwe.atomic

import java.util.concurrent.atomic.AtomicLong

class Task(private val counter: AtomicLong) : Runnable {

    override fun run() {
        counter.incrementAndGet()
    }
}