package com.wwe

import org.junit.Test
import java.util.concurrent.locks.ReentrantReadWriteLock

class ReadLockJumpQueueTest {

    @Test
    fun test() {
        Thread({
            read()
        }, "Thread-2").start()
        Thread({
            read()
        }, "Thread-4").start()

        Thread({
            write()
        }, "Thread-3").start() // ReentrantReadWriteLock 的实现选择了“不允许插队”的策略，这就大大减小了发生“饥饿”的概率。
        Thread({
            read()
        }, "Thread-5").start()

        Thread.sleep(10_000)
    }

    private fun read() {
        readLock.lock()

        try {
            println("${Thread.currentThread().name} got read lock, reading...")
            Thread.sleep(2_000)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        } finally {
            readLock.unlock()
            println("${Thread.currentThread().name} unlock @read")
        }
    }

    private fun write() {
        writeLock.lock()

        try {
            println("${Thread.currentThread().name} got write lock, writing...")
            Thread.sleep(2_000)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        } finally {
            writeLock.unlock()
            println("${Thread.currentThread().name} unlock @write")
        }
    }

    companion object {

        @JvmStatic
        private val reentrantReadWriteLock = ReentrantReadWriteLock()

        @JvmStatic
        private val readLock: ReentrantReadWriteLock.ReadLock = reentrantReadWriteLock.readLock()

        @JvmStatic
        private val writeLock: ReentrantReadWriteLock.WriteLock = reentrantReadWriteLock.writeLock()
    }
}