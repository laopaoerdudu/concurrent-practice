package com.wwe

import com.wwe.Volatile.BoolVolatileTask
import junit.framework.TestCase.assertEquals
import org.junit.Test

class BoolVolatileTaskTest {

    @Test
    fun test() {
        // GIVEN
        val task = BoolVolatileTask()
        val t1 = Thread(task)
        val t2 = Thread(task)

        // WHEN
        t1.start()
        t2.start()
        t1.join()
        t2.join()

        // THEN
        assert(task.a)
        assertEquals(2000, task.realA.get())
    }
}