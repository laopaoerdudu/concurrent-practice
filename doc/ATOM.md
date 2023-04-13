### 什么是原子类？原子类有什么作用？

原子性意味着 “一组操作要么全都操作成功，要么全都失败，不能只操作成功其中的一部分”。

而 java.util.concurrent.atomic 下的类，就是具有原子性的类，可以原子性地执行添加、递增、递减等操作。

原子类的作用和锁有类似之处，是为了保证并发情况下线程安全。不过原子类相比于锁，有一定的优势：

- 粒度更细：原子变量可以把竞争范围缩小到变量级别，通常情况下，锁的粒度都要大于原子变量的粒度。

- 效率更高：除了高度竞争的情况之外，使用原子类的效率通常会比使用同步互斥锁的效率更高，因为原子类底层利用了 CAS 操作，不会阻塞线程。

AtomicInteger，它是对于 int 类型的封装，并且提供了原子性的访问和更新。
也就是说，我们如果需要一个整型的变量，并且这个变量会被运用在并发场景之下，我们可以不用基本类型 int，也不使用包装类型 Integer，
而是直接使用 AtomicInteger，这样一来就自动具备了原子能力，使用起来非常方便。

AtomicInteger 比普通的变量更加耗费资源。

```
// 如果输入的数值等于预期值，则以原子方式将该值更新为输入值（update）
boolean compareAndSet(int expect, int update) 
```

AtomicReference 类的作用和 AtomicInteger 并没有本质区别， AtomicInteger 可以让一个整数保证原子性，而 AtomicReference 可以让一个对象保证原子性。
这样一来，AtomicReference 的能力明显比 AtomicInteger 强，因为一个对象里可以包含很多属性。

### Unsafe 类

Unsafe 类主要是用于和操作系统打交道的，因为大部分的 Java 代码自身无法直接操作内存，所以在必要的时候，
可以利用 Unsafe 类来和操作系统进行交互，CAS 正是利用到了 Unsafe 类。

Unsafe 的 getAndAddInt 方法是通过循环 + CAS 的方式来实现的，
在此过程中，它会通过 compareAndSwapInt 方法来尝试更新 value 的值，这是一个死循环。它实现的原理是利用自旋去不停地尝试，直到成功为止。
































































