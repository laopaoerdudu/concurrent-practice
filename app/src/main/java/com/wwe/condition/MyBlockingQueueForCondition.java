package com.wwe.condition;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/** 用 Condition 实现简易版阻塞队列 */
public class MyBlockingQueueForCondition {
    private Queue queue;
    private int max = 16;
    private ReentrantLock lock = new ReentrantLock();
    private Condition emptyCondition = lock.newCondition();
    private Condition fullCondition = lock.newCondition();

    public MyBlockingQueueForCondition(int size) {
        this.max = size;
        queue = new LinkedList();
    }

    public void put(Object data) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == max) {
                // 调用 await 的时候必须持有锁，否则会抛出异常
                // await 方法完成会自动释放持有的 Lock 锁
                fullCondition.await();
            }
            queue.add(data);
            emptyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == 0) {
                emptyCondition.await();
            }
            Object data = queue.remove();
            fullCondition.signalAll();
            return data;
        } finally {
            lock.unlock();
        }
    }
}
