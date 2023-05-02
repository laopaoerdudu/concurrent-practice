package com.wwe

import com.wwe.Volatile.VolatileTask
import org.junit.Test

/** 我们仅靠 volatile 是不能保证 a++ 的线程安全的。*/
class VolatileTaskTest {

    @Test
    fun test() {
        // GIVEN
        val task = VolatileTask()
        val t1 = Thread(task)
        val t2 = Thread(task)

        // WHEN
        t1.start()
        t2.start()
        t1.join()
        t2.join()

        // THEN
        println("a -> ${task.a}") // 1997
        println("realA -> ${task.realA.get()}") // 2000
    }
}