package com.wwe.thread_local

import java.text.SimpleDateFormat

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