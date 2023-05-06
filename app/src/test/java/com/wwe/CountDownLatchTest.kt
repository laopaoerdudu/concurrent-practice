package com.wwe

import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class CountDownLatchTest {

    /** 一个线程等待其他多个线程都执行完毕，再继续自己的工作。 */
    @Test
    fun test1() {
        val latch = CountDownLatch(5)
        val service = Executors.newFixedThreadPool(5)
        (0 until 5).forEach { i ->
            service.submit {
                try {
                    Thread.sleep((Math.random() * 10_00).toLong())
                    println("$i 号运动员完成比赛。")
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                } finally {
                    // 调用 countDown 方法来把计数减 1。
                    latch.countDown()
                }
            }
        }
        println("主线程开始等待。")
        latch.await() // 主线程在其他线程都准备完毕之后再继续执行
        println("比赛结束。")
    }

    /** 多个线程等待某一个线程的信号，同时开始执行 */
    @Test
    fun test2() {
        val countDownLatch = CountDownLatch(1)
        val service = Executors.newFixedThreadPool(5)
        (0 until 5).forEach { i ->
            service.submit {
                println("$i 号运动员准备就绪")
                try {
                    countDownLatch.await()
                    println("$i 号运动员刚跑完")
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                }
            }
        }
        Thread.sleep(5_000)
        println("发令枪响，比赛开始！")
        countDownLatch.countDown()
    }
}