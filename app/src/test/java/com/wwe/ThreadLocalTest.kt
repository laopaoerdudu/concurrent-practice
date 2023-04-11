package com.wwe

import com.wwe.thread_local.ThreadSafeFormatter
import org.junit.Test
import java.sql.Date
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ThreadLocalTest {

    @Test
    fun test() {
        for (i in 0 until 1000) {
            threadPool.submit {
                println(this@ThreadLocalTest.date(i))
            }
        }
        threadPool.shutdown()
    }

    private fun date(seconds: Int): String {
        val dateFormat = ThreadSafeFormatter.dateFormatThreadLocal.get()
        return dateFormat.format(Date((1000 * seconds).toLong()))
    }

    companion object {
        @JvmStatic
        val threadPool: ExecutorService = Executors.newFixedThreadPool(16)
    }
}