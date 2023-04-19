package com.wwe.wait_notify

import java.util.*

class MyBlockingQueueForWaitNotify(
    private val maxSize: Int,
    private val lock: Object = Object(),
    private val storage: LinkedList<Any> = LinkedList<Any>()
) {

    @Synchronized
    @Throws(InterruptedException::class)
    fun put(data: Any) {
        while (storage.size == maxSize) {
            lock.wait()
        }
        storage.add(data)
        lock.notifyAll()
    }

    @Synchronized
    @Throws(InterruptedException::class)
    fun take(): Any {
        while (storage.size == 0) {
            // 调用 wait 的时候必须持有锁，否则会抛出异常
            // wait 方法完成会自动释放持有的锁
            lock.wait()
        }
        val data = storage.remove()
        lock.notifyAll()
        return data
    }
}