package com.wwe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.concurrent.locks.ReentrantReadWriteLock

class ReadWriteLockTest {

//    @Rule
//    @JvmField
//    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun test() {
        Thread {
            read()
        }.start()
        Thread {
            read()
        }.start()

        Thread {
            write()
        }.start()
        Thread {
            write()
        }.start()

        Thread.sleep(3_000)
    }

    private fun read() {
        readLock.lock()

        try {
            println("${Thread.currentThread().name} got read lock, reading...")
            Thread.sleep(500)
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
            Thread.sleep(500)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        } finally {
            writeLock.unlock()
            println("${Thread.currentThread().name} unlock @write")
        }
    }

    companion object {

        @JvmStatic
        private val reentrantReadWriteLock = ReentrantReadWriteLock(false)

        @JvmStatic
        private val readLock: ReentrantReadWriteLock.ReadLock = reentrantReadWriteLock.readLock()

        @JvmStatic
        private val writeLock: ReentrantReadWriteLock.WriteLock = reentrantReadWriteLock.writeLock()
    }
}