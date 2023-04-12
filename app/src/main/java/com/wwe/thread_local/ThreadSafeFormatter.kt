package com.wwe.thread_local

import java.text.SimpleDateFormat

/**
 * ThreadLocal 解决线程安全问题的时候，相比于使用“锁”而言，换了一个思路，把资源变成了各线程独享的资源，非常巧妙地避免了同步操作。
 * 具体而言，它可以在 initialValue 中 new 出自己线程独享的资源。
 */
class ThreadSafeFormatter {

    companion object {

        @JvmStatic
        val dateFormatThreadLocal = object : ThreadLocal<SimpleDateFormat>() {
            override fun initialValue(): SimpleDateFormat? {
                return SimpleDateFormat("mm:ss")
            }
        }
    }
}