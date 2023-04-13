package com.wwe

import com.wwe.atomic.Task
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

class AtomicTest {

    @Test //(expected = InterruptedException::class)
    fun test() {
        val counter = AtomicLong(0)
        val threadPool = Executors.newFixedThreadPool(16)

        // 虽然是多线程并发访问，但是 AtomicLong 依然可以保证 incrementAndGet 操作的原子性，所以不会发生线程安全问题。
        for (i in 0 until 100) {
            threadPool.submit(Task(counter))
        }
        Thread.sleep(200)
        println(counter.get())
        threadPool.shutdown()
    }
}