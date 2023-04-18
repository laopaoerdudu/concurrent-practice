package com.wwe.semaphore

import java.util.concurrent.Semaphore

class Task(private val semaphore: Semaphore) : Runnable {

    override fun run() {
        try {
            semaphore.acquire()
            println("@${Thread.currentThread().name} 拿到了许可证。")
            Thread.sleep(2000)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
        println("@${Thread.currentThread().name} 释放了许可证。")
        semaphore.release()
    }
}