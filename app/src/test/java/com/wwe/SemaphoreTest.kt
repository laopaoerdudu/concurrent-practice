package com.wwe

import com.wwe.semaphore.Task
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

/** 通过执行结果可以看出，同时最多只有 3 个线程可以访问我们的慢服务。 */
class SemaphoreTest {

    @Test
    fun test() {
        val service = Executors.newFixedThreadPool(50)
        (0 until 1000).forEach { _ ->
            service.submit(Task(Semaphore(3)))
        }
        service.shutdown()
    }
}