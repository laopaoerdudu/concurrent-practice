package com.wwe.atomic

class SynchronizedTask(private var value: Int) : Runnable {
    override fun run() {
        (0 until 1000).forEach { _ ->
            synchronized(this) {
                ++value
            }
        }
    }
}