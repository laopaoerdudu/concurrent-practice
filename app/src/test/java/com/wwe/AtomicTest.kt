package com.wwe

import com.wwe.atomic.AtomicTask
import com.wwe.atomic.SynchronizedTask
import com.wwe.atomic.Task
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
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

    @Test
    fun test1() {
        // GIVEN
        val atomicInteger = AtomicInteger()
        val runnable = AtomicTask(atomicInteger)
        val thread1 = Thread(runnable)
        val thread2 = Thread(runnable)

        // WHEN
        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()

        // THEN
        assert(2000 == atomicInteger.get())
    }

    @Test
    fun test2() {
        // GIVEN
        var value = 0
        val runnable = SynchronizedTask(value)
        val thread1 = Thread(runnable)
        val thread2 = Thread(runnable)

        // WHEN
        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()

        // THEN
        println("value: $value")
    }
}