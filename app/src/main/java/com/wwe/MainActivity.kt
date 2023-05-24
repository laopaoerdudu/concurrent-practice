package com.wwe

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val asyncExecuteHandler: Handler by lazy {
        val worker = HandlerThread("asyncExecuteWorker")
        worker.start()
        return@lazy Handler(worker.looper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * 判断下当前线程，如果是主线程则调度到一个专门负责创建线程的线程进行工作。
     */
//    fun execute(runnable: Runnable, priority: Int) {
//        if (Looper.getMainLooper().thread == Thread.currentThread() && asyncExecute) {
//            //异步执行
//            asyncExecuteHandler.post {
//                mExecutor.execute(runnable, priority)
//            }
//        } else {
//            mExecutor.execute(runnable, priority)
//        }
//    }
}