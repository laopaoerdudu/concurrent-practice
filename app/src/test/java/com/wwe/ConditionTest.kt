package com.wwe

import org.junit.Test
import java.util.concurrent.locks.ReentrantLock

/** signalAll() 会唤醒所有正在等待的线程，而 signal() 只会唤醒一个线程。 */
class ConditionTest {
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()

    @Test
    fun test() {
        Thread {
            try {
                b()
            } catch (ex: InterruptedException) {
                ex.printStackTrace()
            }
        }.start()
        a()
        println("@test Done")
    }

    private fun a() {
        lock.lock()
        try {
            println("@a ${Thread.currentThread().name} 开始 await。")
            condition.await()
            println("@a ${Thread.currentThread().name}: 条件满足了，开始执行后续的任务。")
        } finally {
            lock.unlock()
            println("@a unlock")
        }
    }

    /**
     * 调用了 signal 之后，还需要等待子线程完全退出这个锁，即执行 unlock 之后，
     * 这个主线程才有可能去获取到这把锁，并且当获取锁成功之后才能继续执行后面的任务。
     * 刚被唤醒的时候主线程还没有拿到锁，是没有办法继续往下执行的。
     */
    private fun b() {
        lock.lock()
        try {
            Thread.sleep(2_000)
            println("@b ${Thread.currentThread().name}: 唤醒其它的线程。")
            condition.signal()
        } finally {
            lock.unlock()
            println("@b unlock")
        }
    }
}