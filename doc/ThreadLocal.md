### ThreadLocal 和 synchronized 是什么关系？

- ThreadLocal 是通过让每个线程独享自己的副本，避免了资源的竞争。

- synchronized 主要用于临界资源的分配，在同一时刻限制最多只有一个线程能访问该资源。

相比于 ThreadLocal 而言，synchronized 的效率会更低一些，但是花费的内存也更少。

一个 Thread 里面只有一个 ThreadLocalMap ，而在一个 ThreadLocalMap 里面却可以有很多的 ThreadLocal，每一个 ThreadLocal 都对应一个 value。
（value 这就是我们希望 ThreadLocal 存储的内容，例如 user 对象等。）

```
/**
 * 我们可以把 Entry 理解为一个 map，其键值对为：
 *   键，当前的 ThreadLocal；
 *   值，实际需要存储的变量，比如 user 用户对象或者 simpleDateFormat 对象等。 
 */
static class ThreadLocalMap {

    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;


        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }
   private Entry[] table;
//...
}
```

可以看到，这个 Entry 是 extends WeakReference。
弱引用的特点是，如果这个对象只被弱引用关联，而没有任何强引用关联，那么这个对象就可以被回收，所以弱引用不会阻止 GC。
因此，这个弱引用的机制就避免了 ThreadLocal 的内存泄露问题。 这就是为什么 Entry 的 key 要使用弱引用的原因。

### 什么是内存泄漏？

当某一个对象不再有用的时候，占用的内存却不能被回收，这就叫作内存泄漏。
如果对象没有用，但一直不能被回收，这样的垃圾对象如果积累的越来越多，则会导致我们可用的内存越来越少，最后发生内存不够用的 OOM 错误。

在使用完了 ThreadLocal 之后，我们应该手动去调用它的 remove 方法，目的是防止内存泄漏的发生。

>这里的 remove 方法可以把 key 所对应的 value 给清理掉，这样一来，value 就可以被 GC 回收了。

```
public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());
    if (m != null)
        m.remove(this);
}
```



---

我们知道 HashMap 在面对 hash 冲突的时候，采用的是拉链法。
它会先把对象 hash 到一个对应的格子中，如果有冲突就用链表的形式往下链，如图（hash_conflict.png）

但是 ThreadLocalMap 解决 hash 冲突的方式是不一样的，它采用的是线性探测法。
如果发生冲突，并不会用链表的形式往下链，而是会继续寻找下一个空的格子。这是 ThreadLocalMap 和 HashMap 在处理冲突时不一样的点。




































