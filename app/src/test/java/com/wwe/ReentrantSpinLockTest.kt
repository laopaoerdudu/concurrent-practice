package com.wwe

import org.junit.Test
import java.util.concurrent.atomic.AtomicReference

/** 实现一个可重入的自旋锁 */
class ReentrantSpinLockTest {
    private val owner: AtomicReference<Thread> = AtomicReference()
    private var count = 0 // 重入次数

    private val spinLock = this@ReentrantSpinLockTest

    private val runnable = Runnable {
        spinLock.lock()
        try {
            println("${Thread.currentThread().name} 获取到了自旋锁")
            Thread.sleep(2_000)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        } finally {
            spinLock.unlock()
            println("${Thread.currentThread().name} 释放了了自旋锁")
        }
    }

    @Test
    fun test() {
        Thread(runnable).start()
        Thread(runnable).start()

        Thread.sleep(5_000)
    }

    private fun lock() {
        val curTd = Thread.currentThread()
        if (curTd == owner.get()) {
            ++count
            return
        }

        // 自旋获取锁，自旋期间，CPU 依然在不停运转
        while (!owner.compareAndSet(null, curTd)) {
            println("自旋...")
        }
    }

    private fun unlock() {
        val curTd = Thread.currentThread()
        // 只有持有锁的线程才能解锁
        if (curTd == owner.get()) {
            if (count > 0) {
                --count
            } else {
                owner.set(null)
            }
        }
    }
}