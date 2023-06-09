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

### AtomicInteger 在高并发下性能不好，如何解决？为什么？

在并发的场景下，如果我们需要实现计数器，可以利用 AtomicInteger，这样一来，就可以避免加锁和复杂的代码逻辑，
有了它们之后，我们只需要执行对应的封装好的方法，例如对这两个变量进行原子的增操作或原子的减操作，就可以满足大部分业务场景的需求。

而 AtomicInteger 还具有 compareAndSet 等高级方法，可以应对除了加减之外的更复杂的需要 CAS 的场景。

在低竞争的情况下，AtomicLong 和 LongAdder 这两个类具有相似的特征，吞吐量也是相似的，因为竞争不高。
但是在竞争激烈的情况下，LongAdder 的预期吞吐量要高得多，经过试验，LongAdder 的吞吐量大约是 AtomicLong 的十倍，
不过凡事总要付出代价，LongAdder 在保证高效的同时，也需要消耗更多的空间。

### 原子类和 volatile 有什么异同？

在变量的前面加上 volatile 关键字修饰， 有了这个关键字之后，线程 1 的更改会被 flush 到共享内存中，
然后又会被 refresh 到线程 2 的本地内存中，这样线程 2 就能感受到这个变化了，
所以 volatile 这个关键字最主要是用来解决可见性问题的，可以一定程度上保证线程安全。

**现在让我们回顾一下很熟悉的多线程同时进行 value++ 的场景**

即使我们在这里使用 volatile 关键字修饰 value 也是不能保证线程安全的，因为这里的问题不单单是可见性问题，还包含原子性问题。

我们有多种办法可以解决这里的问题：

第 1 种是使用 synchronized 关键字，如图1所示；

>这样一来，两个线程就不能同时去更改 value 的数值，保证了 value++ 语句的原子性，并且 synchronized 同样保证了可见性，
也就是说，当第 1 个线程修改了 value 值之后，第 2 个线程可以立刻看见本次修改的结果。

解决这个问题的第 2 个方法，就是使用我们的原子类，如图2所示：

>用一个 AtomicInteger，然后每个线程都调用它的 incrementAndGet 方法。
在利用了原子变量之后就无需加锁，我们可以使用它的 incrementAndGet 方法，这个操作底层由 CPU 指令保证原子性，
所以即便是多个线程同时运行，也不会发生线程安全问题。

### 划重点

通常情况下，volatile 可以用来修饰 boolean 类型的标记位，因为对于标记位来讲，
直接的赋值操作本身就是具备原子性的，再加上 volatile 保证了可见性，那么就是线程安全的了。

而对于会被多个线程同时操作的计数器 Counter 的场景，这种场景的一个典型特点就是，它不仅仅是一个简单的赋值操作，而是需要先读取当前的值，
然后在此基础上进行一定的修改，再把它给赋值回去。这样一来，我们的 volatile 就不足以保证这种情况的线程安全了。我们需要使用原子类来保证线程安全。

### AQ

1，可以 volatile 和 AtomicInteger 组合使用吗？

>讲师回复： 不需要，单独使用 AtomicInteger 已经可以保证原子性了。

2， 直接使用 synchronized 加锁，不适用 volatile 修饰 value，对 value 进行自增或自减操作，同样可以保证最终 value 值的准确性吧？！

>讲师回复： 可以的，synchronized 可以保证。

### AtomicInteger 和 synchronized 的异同点？

第一点，我们来看一下它们背后原理的不同。

>synchronized 背后的 monitor 锁，也就是 synchronized 原理，同步方法和同步代码块的背后原理会有少许差异，
但总体思想是一致的：在执行同步代码之前，需要首先获取到 monitor 锁，执行完毕后，再释放锁。
原子类，它保证线程安全的原理是利用了 CAS 操作。

第二点不同是使用范围的不同。

>对于原子类而言，它的使用范围是比较局限的。因为一个原子类仅仅是一个对象，不够灵活。
而 synchronized 的使用范围要广泛得多。
比如说 synchronized 既可以修饰一个方法，又可以修饰一段代码。
所以仅有少量的场景，例如计数器等场景，我们可以使用原子类。而在其他更多的场景下，如果原子类不适用，那么我们就可以考虑用 synchronized 来解决这个问题。

第三个区别是粒度的区别。

>原子变量的粒度是比较小的，它可以把竞争范围缩小到变量级别。
通常情况下，synchronized 锁的粒度都要大于原子变量的粒度。如果我们只把一行代码用 synchronized 给保护起来的话，有一点杀鸡焉用牛刀的感觉。

第四点是它们性能的区别，同时也是悲观锁和乐观锁的区别。

>因为 synchronized 是一种典型的悲观锁，而原子类恰恰相反，它利用的是乐观锁。
所以，我们在比较 synchronized 和 AtomicInteger 的时候，其实也就相当于比较了悲观锁和乐观锁的区别。
悲观锁开销是一次性的获取锁的开销，可能涉及线程状态转换，而乐观锁的开销是一次次的尝试获取锁。

### 悲观锁和乐观锁的区别

从性能上来考虑的话，悲观锁的操作相对来讲是比较重量级的。
因为 synchronized 在竞争激烈的情况下，会让拿不到锁的线程阻塞，而原子类是永远不会让线程阻塞的。
不过，虽然 synchronized 会让线程阻塞，但是这并不代表它的性能就比原子类差。

因为悲观锁的开销是固定的，也是一劳永逸的。随着时间的增加，这种开销并不会线性增长。

而乐观锁虽然在短期内的开销不大，但是随着时间的增加，它的开销也是逐步上涨的。

所以从性能的角度考虑，它们没有一个孰优孰劣的关系，而是要区分具体的使用场景。
在竞争非常激烈的情况下，推荐使用 synchronized；而在竞争不激烈的情况下，使用原子类会得到更好的效果。

值得注意的是，synchronized 的性能随着 JDK 的升级，也得到了不断的优化。
synchronized 会从无锁升级到偏向锁，再升级到轻量级锁，最后才会升级到让线程阻塞的重量级锁。
因此synchronized 在竞争不激烈的情况下，性能也是不错的，不需要“谈虎色变”。































































