# SENIOR ROADMAP — Java Core Project

> Roadmap phân tích **lượng kiến thức còn thiếu** ở mỗi module hiện tại để đưa toàn bộ project lên level **senior**, kèm danh sách feature Java hiện đại và ecosystem cần làm chủ.
>
> Mục lục:
>
> - [A. Executive summary — đánh giá hiện trạng](#a-executive-summary)
> - [B. Gap analysis theo module 2-9](#b-gap-analysis-theo-module)
> - [C. Modern Java features (cross-cutting)](#c-modern-java-features-cross-cutting)
> - [D. Ecosystem cần biết ở level senior](#d-ecosystem-c%E1%BA%A7n-bi%E1%BA%BFt-%E1%BB%9F-level-senior)
> - [E. Suggested module mới (optional)](#e-suggested-module-m%E1%BB%9Bi-optional)
> - [F. Bảng kiểm senior level](#f-b%E1%BA%A3ng-ki%E1%BB%83m-senior-level)

---

## A. Executive summary

| Module | Tên hiện tại | Hiện trạng | Mức coverage |
|--------|--------------|----------------|--------------|
| 1 | Concepts | **Senior** — 13 docs JVM internals | senior |
| 2 | Core (basic syntax) | **Senior** — đã thêm 8 demo modern syntax (J10-J21) | senior |
| 3 | OOP | Intermediate — có inheritance, polymorphism, interface, encapsulation, abstraction | intermediate |
| 4 | Exception | Beginner — chỉ try/catch/finally cơ bản + log | beginner |
| 5 | Collections | Intermediate-Senior — có ConcurrentHashMap, LRU, performance comparison | intermediate-senior |
| 6 | I/O | Beginner — chỉ `java.io` cơ bản | beginner |
| 7 | GUI (Swing) | Beginner — Swing đã legacy | legacy |
| 8 | Java 8 | Intermediate — lambda, stream, method ref, default method | intermediate |
| 9 | Multithreading | Intermediate — sync, sort, priority, thread ops | intermediate |
| 11 | **Concurrency Advanced** | **Senior** — 12 chương `j.u.c`, virtual threads, structured concurrency | senior |

**Đã hoàn tất**: bước 1-2 từ "Suggested next steps" — bổ sung 8 demo modern syntax cho module 2 + tách `module_11_concurrency_advanced` (12 chương).

**Còn lại để đạt senior toàn diện**: module 3, 4, 6, 8, 9 cần expand + ecosystem (build, test, log, design patterns).

> Module 1: [`src/module_1_concepts/`](src/module_1_concepts/introduction.md) (14 file).
> Module 11: [`src/module_11_concurrency_advanced/`](src/module_11_concurrency_advanced/README.md) (1 README + 12 chương).

---

## B. Gap analysis theo module

### Module 2 — Core (`src/module_2_core/`)

**Đã có**: data type, control flow, OOP basics, constructors, overloading, toString, array of objects, pass by value vs reference.

**Còn thiếu (ưu tiên senior)**:

- [ ] **Modern syntax**:
  - `var` (Java 10) — local variable type inference, scope rule khi nào nên / không nên dùng.
  - **Text blocks** `"""..."""` (Java 15) — incidental whitespace, escape `\s`, `\<newline>`.
  - **Records** (Java 16) — immutable DTO, canonical/compact constructor, deconstruct với pattern matching.
  - **Sealed classes** (Java 17) — `sealed`, `permits`, `non-sealed`, hierarchy đóng + exhaustive switch.
  - **Pattern matching** for `instanceof` (J16) và `switch` (J21) — record patterns, guard `when`.
  - **Switch expressions** (J14) — `->` form, multi-label, yield.
  - **Enhanced `for-each`** với `Map.entrySet()`.
- [ ] **Object class methods** — `equals`, `hashCode`, `toString`, `clone`, `finalize` (deprecated):
  - Contract `equals`/`hashCode` (Effective Java item 10-11) — symmetric, transitive, consistent.
  - `clone` problem — copy constructor / static factory tốt hơn (item 13).
- [ ] **`Comparable<T>`** & natural ordering (item 14).
- [ ] **Immutability**: how to design immutable class (item 17), defensive copy.
- [ ] **`enum`** mở rộng:
  - Method per constant (strategy pattern).
  - `EnumSet`, `EnumMap`.
  - `values()`, `valueOf()` cost.
- [ ] **Nested types** chuyên sâu — chuyển sang module 3 hoặc bổ sung tại đây.
- [ ] **Primitive vs wrapper** — autoboxing cost, cache `Integer.valueOf` (-128..127), so sánh `==` Long.
- [ ] **`final`** vs immutable — không phải synonym.
- [ ] **`Optional<T>`** giới thiệu (chi tiết ở module 8).

**Suggested files** (thêm vào module 2) — **đã hoàn tất**:

- [x] `_45_var_keyword/` — demo `var` đúng/sai cách.
- [x] `_46_text_blocks/` — JSON/SQL/HTML literal.
- [x] `_47_records/` — DTO + pattern matching.
- [x] `_48_sealed/` — type hierarchy + switch exhaustive.
- [x] `_49_switch_pattern/` — `switch` expression + pattern matching for switch.
- [x] `_50_equals_hashcode_contract/` — bug examples + Objects.hash, Records auto.
- [x] `_51_enum_advanced/` — strategy enum, EnumMap/EnumSet.
- [x] `_52_immutable_design/` — defensive copy, factory.

---

### Module 3 — OOP (`src/module_3_oop/`)

**Đã có**: static, inheritance (single/hierarchical/multilevel/multiple via interface), method override, super, abstraction, access modifier, encapsulation, copy, interface, polymorphism (static & dynamic).

**Còn thiếu**:

- [ ] **`SOLID` principles** — 5 nguyên tắc + ví dụ vi phạm/tuân thủ.
- [ ] **Composition over inheritance** (Effective Java item 18).
- [ ] **Liskov Substitution principle** — ví dụ `Square extends Rectangle` sai.
- [ ] **Nested types đầy đủ**:
  - `static` nested class.
  - **Inner class** (non-static) — implicit reference outer (leak source!).
  - **Local class** trong method.
  - **Anonymous class** vs lambda — khi nào nên dùng cái nào.
- [ ] **`instanceof` pattern matching** (J16) thay cho cast.
- [ ] **`Object` contract** — đặc biệt cho `equals`/`hashCode` khi inheritance.
- [ ] **Design patterns** (GoF — chọn 8-10 pattern quan trọng):
  - Creational: Singleton (DCL, Holder, Enum), Builder, Factory Method, Abstract Factory.
  - Structural: Adapter, Decorator, Proxy, Facade, Composite.
  - Behavioral: Strategy, Observer, Template Method, Chain of Responsibility, State, Command, Iterator (đã ở Collection).
- [ ] **Effective Java idioms** chọn lọc:
  - Item 1: Static factory thay constructor.
  - Item 2: Builder cho nhiều tham số.
  - Item 3: Singleton qua enum.
  - Item 17: Minimize mutability.
  - Item 18: Composition over inheritance.
  - Item 19: Design for inheritance hoặc cấm.
- [ ] **Marker interface vs annotation** (`Serializable`, `Cloneable`).
- [ ] **`abstract class` vs `interface`** (J8+ default method, J9 private method).

**Suggested files**:

- `solid/` — 5 sub folder cho từng nguyên tắc.
- `nested_types/` — static nested, inner, local, anonymous.
- `instanceof_pattern/` — J16+ pattern matching.
- `design_patterns/` — sub folder per pattern.
- `composition_vs_inheritance/`.
- `effective_java/` — top 20 items quan trọng.

---

### Module 4 — Exception (`src/module_4_exception/`)

**Đã có**: try-catch cơ bản với InputMismatchException, ArithmeticException; Debug; SLF4J log.

**Còn thiếu (rất nhiều)**:

- [ ] **Hierarchy `Throwable`**: `Throwable` → `Error` (OOM, StackOverflow, AssertionError) vs `Exception` → `RuntimeException` (unchecked) vs others (checked).
- [ ] **Checked vs unchecked best practice** (Effective Java item 70-77):
  - Checked cho recoverable, unchecked cho programming error.
  - Tranh luận hiện đại: nhiều framework (Spring) chọn unchecked-only.
- [ ] **Custom exception** — extends `RuntimeException` / `Exception`, constructor chuẩn 4 form.
- [ ] **`try-with-resources`** + `AutoCloseable` — close đúng, suppress exception.
- [ ] **Exception chaining** — `initCause`, constructor `(message, cause)`, `getCause`.
- [ ] **Suppressed exceptions** — `Throwable.addSuppressed`, `getSuppressed` (J7+ — sinh ra do try-with-resources).
- [ ] **Multi-catch** `catch (IOException | SQLException e)` (J7+).
- [ ] **Rethrow more precise** với `final` parameter (J7+).
- [ ] **Helpful NullPointerException** (J14, JEP 358) — `-XX:+ShowCodeDetailsInExceptionMessages`.
- [ ] **`Optional<T>`** thay null return — không thay null parameter.
- [ ] **Best practice**:
  - Đừng catch `Exception`/`Throwable` chung chung.
  - Đừng nuốt exception (`catch ... { }`).
  - Đừng dùng exception cho control flow.
  - Đừng `throw` trong `finally`.
- [ ] **Logging exception đúng**: `log.error("...", e)` (truyền `Throwable` cuối cùng) — không phải `log.error("..." + e)`.
- [ ] **MDC (Mapped Diagnostic Context)** — tracing context.
- [ ] **Async exception**: `CompletableFuture.exceptionally`, `handle`, `whenComplete`.

**Suggested files**:

- `throwable_hierarchy/`.
- `custom_exception/`.
- `try_with_resources/`.
- `exception_chaining/`.
- `multi_catch/`.
- `optional_vs_null/` (cross link module 8).
- `best_practices/`.
- `npe_helpful/`.

---

### Module 5 — Collections (`src/module_5_collections/`)

**Đã có (khá tốt)**: List (ArrayList, LinkedList, Vector), Set (HashSet, LinkedHashSet, TreeSet), Map (HashMap, Hashtable, LinkedHashMap, TreeMap, ConcurrentHashMap, LRUCache), Queue (Queue, Deque, ArrayDeque, PriorityQueue), Performance comparison.

**Còn thiếu**:

- [ ] **`Iterator` & `Iterable`** contract — `hasNext`, `next`, `remove`. Custom iterable.
- [ ] **Fail-fast vs fail-safe** iterator — `ConcurrentModificationException`. Snapshot/weakly consistent.
- [ ] **`Comparable` vs `Comparator`** — natural ordering, `Comparator.comparing`, `thenComparing`, `reversed`, `nullsFirst`.
- [ ] **Immutable factories** (J9+) — `List.of`, `Set.of`, `Map.of`, `Map.ofEntries`. Khác `Collections.unmodifiable*`.
- [ ] **`Collections.unmodifiableXxx`** — view không phải copy.
- [ ] **Specialized maps**:
  - `WeakHashMap` — key bị GC → entry tự xoá. Use case: cache by class.
  - `IdentityHashMap` — so sánh `==`, không `equals`.
  - `EnumMap` — array-backed, fast cho enum key.
  - `EnumSet` — bitset.
- [ ] **Concurrent collections**:
  - `CopyOnWriteArrayList`, `CopyOnWriteArraySet`.
  - `BlockingQueue` (`ArrayBlockingQueue`, `LinkedBlockingQueue`, `SynchronousQueue`, `PriorityBlockingQueue`, `DelayQueue`).
  - `ConcurrentLinkedQueue` (lock-free).
  - `ConcurrentSkipListMap`/`Set`.
- [ ] **`HashMap` internals**:
  - Hash function (J8 spread).
  - Bucket → linked list → red-black tree (treeify threshold = 8, untreeify = 6).
  - Resize, load factor 0.75.
  - Initial capacity tuning.
  - **Vì sao hashCode tệ → O(N) lookup**.
- [ ] **`ConcurrentHashMap` internals** Java 7 (segment lock) vs Java 8 (CAS bin + synchronized node).
- [ ] **Big-O comparison** bảng đầy đủ:
  - ArrayList vs LinkedList (`get`, `add`, `remove`).
  - HashMap vs TreeMap vs LinkedHashMap.
  - HashSet vs TreeSet.
- [ ] **Sequenced Collections** (J21, JEP 431): `SequencedCollection`, `SequencedSet`, `SequencedMap` — `getFirst`, `getLast`, `reversed`.
- [ ] **`Collections` utility**: `frequency`, `disjoint`, `binarySearch`, `nCopies`, `emptyList()`, `singleton`.
- [ ] **`Collectors`** (giao thoa module 8) — đầy đủ: `groupingBy`, `partitioningBy`, `toMap` với merge, `mapping`, `flatMapping`, `teeing` (J12+), `filtering`.

**Suggested files**:

- `iterator/` — custom Iterable.
- `comparable_comparator/`.
- `immutable_factories/`.
- `weak_identity_enum_map/`.
- `concurrent_collections/`.
- `hashmap_internals/` — demo treeify, resize.
- `bigo_comparison/` — benchmark JMH.
- `sequenced_collections/`.

---

### Module 6 — I/O (`src/module_6_io/`)

**Đã có**: File class, FileReader, FileWriter, audio.

**Còn thiếu**:

- [ ] **`java.nio` Channel/Buffer/Selector**:
  - `FileChannel` (read/write/transferTo zero-copy).
  - `ByteBuffer` (heap vs direct), `mark`, `position`, `limit`, `flip`.
  - `Selector` cho non-blocking IO multiplexing.
- [ ] **`NIO.2` (`java.nio.file`, J7+)**:
  - `Path`, `Paths`, `Files` — `Files.lines`, `Files.walk`, `Files.copy`, `Files.move`, `Files.write`.
  - `WatchService` — directory event monitoring.
  - `FileSystem`, `FileStore`.
- [ ] **Try-with-resources** cho stream.
- [ ] **Buffered stream** vs unbuffered — `BufferedReader`, `BufferedInputStream`, `BufferedWriter`.
- [ ] **Character encoding**: `Charset`, `UTF-8` (default từ J18, JEP 400), `InputStreamReader` với charset.
- [ ] **`Reader`/`Writer` (text) vs `InputStream`/`OutputStream` (binary)** — khi nào dùng cái nào.
- [ ] **Serialization**:
  - `Serializable` interface — `serialVersionUID`.
  - `transient` field.
  - `Externalizable` — manual control.
  - **Security risks** — Java deserialization vulnerabilities (CVE-2015-7501 Apache Commons Collections).
  - JEP 415 (J17): Context-Specific Deserialization Filters.
  - **Khuyến nghị thay thế**: JSON (Jackson, Gson), Protobuf, Avro.
- [ ] **`PrintWriter` / `PrintStream`** — `System.out` formatter.
- [ ] **`Console`** — `System.console()` (null khi redirect).
- [ ] **`Scanner` vs `BufferedReader`** performance.
- [ ] **Memory-mapped file** (`MappedByteBuffer`) cho large file.
- [ ] **Async I/O** (`AsynchronousFileChannel`, `AsynchronousSocketChannel`, J7+).
- [ ] **`HttpClient`** (J11, JEP 321) — modern HTTP client built-in, sync + async + WebSocket.

**Suggested files**:

- `nio_channel_buffer/`.
- `nio2_path_files/`.
- `watch_service/`.
- `serialization/` — gồm cả security demo.
- `charset_encoding/`.
- `memory_mapped/`.
- `http_client/` — Java 11 `HttpClient` thay `HttpURLConnection`.
- `file_io_comparison/` — benchmark IO vs NIO.

---

### Module 7 — GUI (`src/module_7_gui/`)

**Hiện trạng**: Swing — đã legacy. Không phải kiến thức senior cần thiết.

**Khuyến nghị**:

1. **Đánh dấu module này là `optional`** trong README (giữ làm reference cho ai muốn).
2. **Hoặc** thay bằng:
   - **`JavaFX`** (modern, theming, FXML, properties binding) — nếu vẫn cần desktop.
   - **Console UI** — Spring Shell, JLine.
3. Xoá hẳn nếu không dùng — quá thời gian học.

→ Tài liệu senior thường **không yêu cầu** kiến thức GUI. Nên đầu tư thời gian này vào **web** (Spring Boot) hoặc **concurrency** (xem module 9, 11).

---

### Module 8 — Java 8 (`src/module_8_java_8/`)

**Đã có**: Lambda, Method Reference, Default Method, Stream API.

**Còn thiếu**:

- [ ] **`Optional<T>`**:
  - `of`, `ofNullable`, `empty`, `orElse`, `orElseGet`, `orElseThrow`, `map`, `flatMap`, `filter`, `ifPresent`, `ifPresentOrElse` (J9), `or` (J9), `stream` (J9).
  - **Khi nên / không nên dùng** — return type only, không phải parameter, field, hay collection element.
- [ ] **Functional interfaces đầy đủ** (`java.util.function`):
  - `Predicate<T>`, `BiPredicate`.
  - `Function<T, R>`, `BiFunction`, `UnaryOperator`, `BinaryOperator`.
  - `Consumer<T>`, `BiConsumer`.
  - `Supplier<T>`.
  - Primitive specialization: `IntPredicate`, `IntFunction`, `ToIntFunction`, `IntToLongFunction`, ...
  - **Vì sao** primitive specialization tránh autoboxing — ảnh hưởng performance trong stream lớn.
- [ ] **Primitive streams**: `IntStream`, `LongStream`, `DoubleStream` — `range`, `rangeClosed`, `summaryStatistics`, `boxed`, `mapToObj`.
- [ ] **`Collectors` deep**:
  - `toMap` với 3 form (key, value, mergeFunction).
  - `groupingBy` với downstream (`counting`, `summingInt`, `mapping`, `filtering`, `flatMapping`, `reducing`).
  - `partitioningBy`.
  - `teeing` (J12+).
  - `toUnmodifiableList/Set/Map` (J10+).
- [ ] **`reduce`** 3 form — identity + accumulator + combiner.
- [ ] **`Stream` mở rộng**:
  - `peek` (debug only — không có side effect).
  - `takeWhile`, `dropWhile` (J9).
  - `iterate(seed, hasNext, next)` (J9 — finite).
  - `generate`.
  - `flatMap`.
  - `mapMulti` (J16+) — nhanh hơn flatMap.
  - **Stream gatherers** (J22 preview, JEP 461) — generic intermediate ops.
- [ ] **Parallel streams** — `parallelStream()`, `Spliterator`, `ForkJoinPool.commonPool`. Khi nên / không nên parallel.
- [ ] **`java.time` API** (JSR-310):
  - `LocalDate`, `LocalTime`, `LocalDateTime`, `ZonedDateTime`, `OffsetDateTime`, `Instant`.
  - `Duration` vs `Period`.
  - `DateTimeFormatter`.
  - `ZoneId`, `ZoneOffset`.
  - **Migrate from `Date`/`Calendar`**.
- [ ] **`CompletableFuture`** (giao thoa module 9):
  - `supplyAsync`, `runAsync`.
  - `thenApply`, `thenAccept`, `thenRun`, `thenCompose` (flatMap), `thenCombine`.
  - `exceptionally`, `handle`, `whenComplete`.
  - `allOf`, `anyOf`.
  - **Custom executor** — không nên dùng `commonPool` cho I/O.
- [ ] **`Stream.collect(Supplier, BiConsumer, BiConsumer)`** mutable reduction tự build.

**Suggested files**:

- `optional/` — đúng/sai pattern.
- `functional_interfaces/`.
- `primitive_streams/`.
- `collectors_advanced/`.
- `parallel_stream/`.
- `java_time/`.
- `completable_future/`.
- `stream_gatherers/` (J22 preview).

---

### Module 9 — Multithreading (`src/module_9_multithreading/`)

**Đã có**: Priority, sync (race condition, synchronized, sumThread), sort thread (bubble, merge, quick), thread operations.

**Còn thiếu (rất nhiều — đây là module quan trọng nhất ở senior interview)**:

- [ ] **`Thread` lifecycle states**: `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, `TERMINATED`.
- [ ] **`Runnable` vs `Callable`** — `Callable` trả giá trị + throw checked.
- [ ] **`Future<V>`**:
  - `get`, `get(timeout)`, `cancel`, `isDone`.
- [ ] **`CompletableFuture`** (chuyên sâu — xem module 8).
- [ ] **Executors framework** (`java.util.concurrent`):
  - `Executors.newFixedThreadPool`, `newCachedThreadPool`, `newSingleThreadExecutor`, `newScheduledThreadPool`.
  - `ThreadPoolExecutor` chi tiết: `corePoolSize`, `maximumPoolSize`, `keepAliveTime`, `workQueue`, `RejectedExecutionHandler`.
  - **Antipattern**: dùng `newCachedThreadPool` không bound, `newFixedThreadPool` với queue unbound (`LinkedBlockingQueue`) — nguyên nhân OOM.
  - `ScheduledThreadPoolExecutor`.
- [ ] **`ForkJoinPool`** + work-stealing — `RecursiveTask`, `RecursiveAction`. Là pool đứng sau `parallelStream` & `CompletableFuture.async`.
- [ ] **Locks** (`java.util.concurrent.locks`):
  - `ReentrantLock` — fairness, `tryLock(timeout)`, `lockInterruptibly`, multiple `Condition`.
  - `ReadWriteLock` / `ReentrantReadWriteLock` — read-heavy workload.
  - `StampedLock` (J8+) — optimistic read.
  - `LockSupport.park`/`unpark`.
- [ ] **Atomic classes**:
  - `AtomicInteger`, `AtomicLong`, `AtomicReference`, `AtomicBoolean`.
  - `AtomicIntegerArray`, `AtomicLongArray`.
  - `AtomicReferenceFieldUpdater`.
  - `LongAdder`, `LongAccumulator`, `DoubleAdder` (J8+) — striped, low-contention.
  - **CAS** (compare-and-swap) — ABA problem, `AtomicStampedReference`.
- [ ] **`volatile`** — cross-link với module 1 JMM.
- [ ] **`synchronized` deep**:
  - Object monitor (mark word, biased locking đã removed J18).
  - Lightweight locking → heavyweight.
  - `synchronized` block vs method.
- [ ] **Sync utilities** (`java.util.concurrent`):
  - `CountDownLatch` — đợi N event.
  - `CyclicBarrier` — đồng bộ N thread tại điểm.
  - `Semaphore` — permit-based (rate limit).
  - `Phaser` — flexible barrier với register/deregister.
  - `Exchanger` — 2 thread trao đổi data.
- [ ] **`BlockingQueue`**:
  - `ArrayBlockingQueue` — bounded, FIFO.
  - `LinkedBlockingQueue` — bounded/unbounded.
  - `PriorityBlockingQueue`.
  - `SynchronousQueue` — handoff không lưu.
  - `DelayQueue`.
  - `LinkedTransferQueue`.
  - **Producer-Consumer pattern** với BlockingQueue.
- [ ] **`ConcurrentHashMap` internals** (cross-link module 5).
- [ ] **`ThreadLocal`** + leak khi dùng pool thread, `InheritableThreadLocal`.
- [ ] **`Scoped Values`** (J21 preview, J25 standard) — replacement an toàn cho `ThreadLocal` với virtual threads.
- [ ] **Deadlock**, **livelock**, **starvation** — cách phát hiện (`jstack`), cách tránh (lock ordering, timeout).
- [ ] **`Virtual Threads`** (J21, JEP 444 — Project Loom):
  - Mounting/unmounting, carrier thread.
  - Khi nào dùng — I/O bound.
  - Pinning issue với `synchronized` (sẽ fix ở J24+ JEP 491).
  - `Executors.newVirtualThreadPerTaskExecutor()`.
  - `Thread.startVirtualThread`.
  - Pattern: 1 thread per request.
- [ ] **Structured concurrency** (J21 preview, J25 standard, JEP 453):
  - `StructuredTaskScope.ShutdownOnFailure` / `ShutdownOnSuccess`.
  - Tree of subtasks — cancellation propagation.
- [ ] **`CompletableFuture` patterns**:
  - Async pipeline.
  - Combining multiple futures.
  - Timeout + retry.
- [ ] **JCSP, Reactor (Project Reactor), RxJava** — giới thiệu.

**Suggested files** (lớn — có thể tách thành module riêng):

- `thread_lifecycle/`.
- `runnable_vs_callable/`.
- `executors/` — gồm `ThreadPoolExecutor` config đúng.
- `fork_join_pool/`.
- `locks/` — Reentrant, ReadWrite, Stamped.
- `atomic/`.
- `sync_utilities/` — CountDownLatch, Semaphore, ...
- `blocking_queue_producer_consumer/`.
- `deadlock_demo/`.
- `virtual_threads/`.
- `structured_concurrency/`.
- `completable_future_patterns/`.

> **Khuyến nghị**: tách thành **module mới `module_11_concurrency_advanced`** vì lượng kiến thức lớn. → **đã thực hiện**, xem [`src/module_11_concurrency_advanced/`](src/module_11_concurrency_advanced/README.md).

---

## C. Modern Java features (cross-cutting)

Các tính năng từ Java 9+ chưa được phân bổ vào module nào — nên gom vào `module_10_modern_java`:

| Feature | Phiên bản | JEP | Module hiện tại |
|---------|-----------|-----|----|
| Module system (`Project Jigsaw`) | J9 | 261 | chưa có |
| `var` | J10 | 286 | module 2 |
| Text blocks | J15 | 378 | module 2 |
| Records | J16 | 395 | module 2/3 |
| Pattern matching `instanceof` | J16 | 394 | module 3 |
| Sealed classes | J17 | 409 | module 3 |
| Pattern matching `switch` | J21 | 441 | module 2/3 |
| Record patterns | J21 | 440 | module 2/3 |
| Sequenced Collections | J21 | 431 | module 5 |
| Generational ZGC | J21 | 439 | module 1 (đã có) |
| Virtual threads | J21 | 444 | module 9/11 |
| Structured concurrency | J21→J25 | 453 | module 9/11 |
| Scoped Values | J21→J25 | 446 | module 9/11 |
| `HttpClient` | J11 | 321 | module 6 |
| Class-File API | J24 | 457 | module 1 / advanced |
| Foreign Function & Memory API | J22 | 454 | advanced |
| Stream gatherers | J24 | 461 | module 8 |

---

## D. Ecosystem cần biết ở level senior

Senior Java không chỉ là ngôn ngữ — còn phải nắm chắc ecosystem:

### D.1. Build tools

- **`Maven`** — POM, lifecycle, dependency scope, plugin (`compiler`, `surefire`, `failsafe`, `shade`, `jar`, `assembly`).
- **`Gradle`** — Groovy/Kotlin DSL, task graph, version catalog, build cache, configuration cache.
- **Multi-module build**, BOM (Bill of Materials), dependency convergence.
- **Reproducible build**, supply-chain security (`maven-enforcer`, `dependency-check`).

### D.2. Testing

- **`JUnit 5` (Jupiter)** — `@Test`, `@ParameterizedTest`, `@RepeatedTest`, `@TestFactory`, `@Nested`, lifecycle.
- **`Mockito`** — mock, spy, verify, argument captor, mock static (J5+).
- **`AssertJ`** — fluent assertion (vs Hamcrest).
- **`Testcontainers`** — Postgres/Kafka/Redis containerized cho integration test.
- **Test pyramid** — unit > integration > E2E.
- **Mutation testing** (`PIT`).
- **Test doubles**: stub vs mock vs fake vs spy.

### D.3. Logging

- **`SLF4J`** — facade — đã có trong module 4.
- **`Logback`** vs **`Log4j2`** — appenders, async, rolling.
- **Structured logging** (JSON) — Datadog/Splunk/ELK.
- **MDC** (Mapped Diagnostic Context) cho tracing.
- **Performance**: parameterized message `log.info("user={}", id)` không phải string concat.
- **Log4Shell** (CVE-2021-44228) — bài học bảo mật.

### D.4. Code quality

- **`SpotBugs`** (kế thừa FindBugs) — bug detection.
- **`Checkstyle`** — style consistency.
- **`PMD`** — code smells.
- **`SonarQube`** / `SonarCloud` — quality gate.
- **`Error Prone`** (Google) — `javac` plugin với rule mạnh.
- **`Spotless`** — auto-format (Google Java Format).
- **Coverage**: JaCoCo, Cobertura.

### D.5. Sách bắt buộc

- **`Effective Java`** (3rd ed.) — Joshua Bloch. **Phải đọc**.
- **`Java Concurrency in Practice`** — Brian Goetz et al.
- **`Optimizing Java`** — Ben Evans, James Gough, Chris Newland.
- **`Java Performance`** (2nd ed.) — Scott Oaks.
- **`Modern Java in Action`** — Raoul-Gabriel Urma et al. (lambda/stream).

### D.6. Core libraries

- **`Lombok`** — `@Data`, `@Builder`, `@Slf4j`. Pros + cons.
- **`Guava`** (Google) — Cache, Multimap, Preconditions, util.
- **`Apache Commons`** — Lang, Collections, IO.
- **`Jackson`** — JSON serialization (mọi Spring app dùng).
- **`Caffeine`** — high-performance cache.

### D.7. Spring & Microservices (nếu mục tiêu là backend)

- **`Spring Framework`** core — IoC, DI, AOP.
- **`Spring Boot`** — auto-config, actuator, profile.
- **`Spring Data`**, **`Spring Security`**, **`Spring Cloud`**.
- **`Hibernate`/`JPA`** — entity lifecycle, lazy loading, N+1, fetch strategies.
- **REST / gRPC**, OpenAPI.
- **Kafka**, **RabbitMQ**.
- **Observability**: OpenTelemetry, Prometheus, Grafana.

---

## E. Suggested module mới (optional)

Đề xuất tách kiến thức thiếu thành các module mới để giữ cấu trúc rõ ràng:

| Module | Mô tả | Chứa |
|--------|------|------|
| `module_10_modern_java` | Modern Java features Java 9-21+ | var, text blocks, records, sealed, pattern matching, switch expressions, sequenced collections, virtual threads cơ bản |
| `module_11_concurrency_advanced` | Concurrency nâng cao | Executors, CompletableFuture, locks, atomic, sync utils, BlockingQueue, virtual threads, structured concurrency |
| `module_12_jvm_internals_lab` | Lab thực hành JVM | `javap` exercise, GC tuning lab, JFR profiling, heap dump analysis |
| `module_13_design_patterns` | GoF patterns trong Java | Singleton (DCL, Holder, Enum), Builder, Factory, Strategy, Observer, Decorator, Template Method, Chain of Responsibility, ... |
| `module_14_testing` | Testing toolchain | JUnit 5, Mockito, AssertJ, Testcontainers, parameterized, mutation |
| `module_15_build_ecosystem` | Build & ecosystem | Maven, Gradle, SLF4J, Lombok, Jackson, Guava, Caffeine |

---

## F. Bảng kiểm senior level

Checklist tự đánh giá. Mỗi mục **bạn có thể giải thích trong 5 phút trên giấy** mới tính là biết.

### F.1. JVM & Memory

- [ ] Phân biệt `JDK` / `JRE` / `JVM`. Vì sao Java 9+ bỏ JRE độc lập?
- [ ] 7 vùng memory của JVM. Vùng nào shared, per-thread.
- [ ] `Metaspace` khác `PermGen` ở đâu.
- [ ] Class loading 5 phase. `ClassNotFoundException` vs `NoClassDefFoundError`.
- [ ] Parent delegation. Khi nào framework đảo (Tomcat, OSGi).
- [ ] Compressed Oops — vì sao heap nên < 32 GB.
- [ ] String pool, intern, compact strings.

### F.2. JIT & Bytecode

- [ ] Tiered compilation: C1 vs C2.
- [ ] Inlining, escape analysis, scalar replacement, OSR.
- [ ] Đọc bytecode bằng `javap`. Method descriptor.
- [ ] `invokevirtual` vs `invokespecial` vs `invokeinterface` vs `invokedynamic`.
- [ ] Lambda Java 8 → bytecode `invokedynamic` + `LambdaMetafactory`.
- [ ] Generics erasure + bridge method.

### F.3. GC

- [ ] Reachability từ GC roots — không phải reference counting.
- [ ] Generational hypothesis. Eden / Survivor / Old.
- [ ] G1 vs ZGC vs Shenandoah — trade-off.
- [ ] Tuning `-Xms/-Xmx`, `MaxGCPauseMillis`, `MaxRAMPercentage`.
- [ ] Reference types: Strong, Soft, Weak, Phantom.
- [ ] Vì sao `finalize` deprecated. `Cleaner` thay thế.

### F.4. Concurrency

- [ ] JMM: atomicity, visibility, ordering. `happens-before`.
- [ ] `volatile` / `synchronized` / `final` semantics.
- [ ] Double-checked locking đúng & sai. Holder idiom.
- [ ] CAS, ABA problem.
- [ ] `ReentrantLock` vs `synchronized` — khi nào chọn.
- [ ] `ConcurrentHashMap` Java 7 vs 8 internals.
- [ ] `ThreadPoolExecutor` config. Antipattern `newCachedThreadPool`.
- [ ] Virtual threads — pinning issue.
- [ ] Structured concurrency (J21+).
- [ ] Deadlock — phát hiện qua `jstack`.

### F.5. Collections

- [ ] HashMap internals: hash, bucket, treeify (8), load factor (0.75).
- [ ] Fail-fast vs fail-safe iterator.
- [ ] `List.of` vs `Collections.unmodifiableList`.
- [ ] Big-O: `ArrayList` vs `LinkedList` vs `HashMap` vs `TreeMap`.
- [ ] `EnumMap`/`EnumSet` performance.
- [ ] `Comparable<? super T>` PECS.

### F.6. Modern Java

- [ ] `var` — khi nào dùng / không dùng.
- [ ] `Records` — when not to use.
- [ ] `Sealed classes` — exhaustive switch.
- [ ] Pattern matching for `instanceof` & `switch`.
- [ ] Sequenced Collections.
- [ ] Optional — không dùng làm field/parameter.
- [ ] `java.time` migrate từ `Date`/`Calendar`.

### F.7. Performance & Profiling

- [ ] `jcmd`: heap dump, JFR, thread dump, NMT.
- [ ] MAT: leak suspects, dominator tree, path to GC roots.
- [ ] `async-profiler` flame graph.
- [ ] JMH — vì sao cần warm-up.
- [ ] 5 patterns memory leak (static cache, ThreadLocal, listener, classloader, direct buffer).
- [ ] Production JVM flag tối thiểu (`HeapDumpOnOutOfMemoryError`, GC log, JFR).

### F.8. Ecosystem

- [ ] Maven lifecycle + scope (`compile`/`provided`/`runtime`/`test`).
- [ ] Gradle dependency configuration.
- [ ] JUnit 5 + Mockito + AssertJ.
- [ ] Spring Boot auto-config nguyên lý.
- [ ] Hibernate N+1 problem.
- [ ] Logback / Log4j2 cấu hình production.
- [ ] OpenTelemetry / observability.

> Tự đánh giá: **mọi mục đánh dấu** → đủ điều kiện apply senior backend Java. **80%** → mid-senior. **60%** → strong mid.

---

## Tổng kết

1. **Bộ kiến thức module 1** đã hoàn thành — đây là phần khó nhất, nền tảng JVM internals.
2. ✅ **Module 2** đã expand với 8 demo modern syntax (J10-J21).
3. ✅ **Module 11 — Concurrency Advanced** đã tách riêng với 12 chương + concept.md đầy đủ.
4. **Module 3, 4, 6, 8, 9** mỗi module còn thiếu kiến thức senior — tham khảo gap analysis ở phần B.
5. **Modern Java** (Java 9-21) đã được phủ phần lớn ở module 1, 2, 11. Còn `module_10_modern_java` cho J9 module system, J11 HttpClient, scoped values.
6. **Ecosystem** (Maven, JUnit, Spring, Hibernate, Lombok, observability) **không** thuộc Java core nhưng **bắt buộc** ở vị trí senior.
7. **Tài liệu kèm code** — mỗi module nên có file `concept.md` ngắn giải thích **vì sao**, không chỉ **làm sao**.

### Suggested next steps (ưu tiên giảm dần)

1. **Module 5** — bổ sung `Sequenced Collections` (J21), immutable factories (`List.of`/`Set.of`/`Map.of`), `Collections.unmodifiable*` vs `List.copyOf`, fail-fast vs fail-safe iterator.
2. **Module 8** — expand modern stream API: `Collectors.teeing` (J12), `Stream.toList` (J16), `Stream.mapMulti` (J16), `Optional.or`/`ifPresentOrElse`, gatherers (J22+).
3. **Module 4** — exception nâng cao: try-with-resources, `Throwable.getSuppressed`, custom exception hierarchy, exception translation, sentry/logging best practice.
4. **Module 6** — NIO.2 (`Path`, `Files`, `WatchService`), `HttpClient` (J11), serialization alternatives (Jackson, Protobuf), memory-mapped file.
5. **Module 3** — design patterns thật sự (Strategy, Observer, Decorator, Adapter, Chain), SOLID examples, double-dispatch, Bridge.
6. **Module mới `module_10_modern_java`** — gom J9 module system, J11 HttpClient, J16 packaging tool (`jpackage`), JFR API mới.
7. **Đánh dấu module 7 (Swing) là optional** hoặc remove — không phải knowledge senior.
