# Advent of Code 2019 in Multi-Platform Kotlin

## Intro
In 2020, I will be participating in the [Advent of Code](https://adventofcode.com) using [Kotlin multi-platform](https://kotlinlang.org/docs/reference/multiplatform.html) code only. This means I can run my solutions on the JVM, on Node JS and natively on Windows. I did the same in [2019](https://github.com/jorispz/aoc-2019) and [2018](https://github.com/jorispz/aoc-2018). 
Each year the Kotlin platform has evolved, so it's interesting to see how the relative performance of these build targets evolve. Just to spice things up, this year I'll be using GraalVM to run the JVM versions, and perhaps also the Node implementation of GraalVM if I find the time.


I will be measuring the performance of each platform by measuring the elapsed time calculating the solution `N` times:
```kotlin
val times = (1..repeat).map {
    measureNanos {
        Puzzles.run(day, part)
    }
}
```
where `repeat` is the number of desired repetitions, and `day` and `part` are used to select the proper challenge.

Note that the `measureNanos` function is the first example of functionality that isn't available in common code, since measuring time is platform-specific. The function is implemented using Kotlin's `expext/actual` mechanism.

`Runner.kt` defines its expectation of a function `measureNanos` for each supported platform with the signature:
```kotlin
expect inline fun measureNanos(block: () -> Unit): Long
```

Each platform provides an actual implementation in files called `Actuals.kt`. On the JVM and native, we can use `measureNanoTime` as provided by the Kotlin standard library on these platforms:

JVM and Native:
```kotlin
actual inline fun measureNanos(block: () -> Unit) = measureNanoTime(block)
```

On Node, we need to write our own implementation:
```kotlin
actual inline fun measureNanos(block: () -> Unit): Long {
    val start = process.hrtime()
    block()
    val end = process.hrtime(start)
    val nanos = (end[0] * 1e9 + end[1]) as Double
    return nanos.roundToLong()
}
```

Usually, I try to finish a puzzle as quickly as possible, then spend some time to cleanup and optimise the code. If and when these optimizations alter the performance, I'll update the measurements below to reflect the new solution.

# Important
There is a very explicit warning in the [FAQ of Kotlin/Native](https://github.com/JetBrains/kotlin-native/blob/master/RELEASE_NOTES.md#performance) that says the current version of Kotlin/Native is not suited for performance analysis, as it has not been optimised in any way for performance and benchmarking. 

Apart from that, all measurements were taken on my laptop, under non-controlled circumstances using non-optimized code and tools. So, **if you use the results below for anything important, you're insane...**

# Day 1
Today I had a lot of trouble getting everything compiling and running. First, somehow my IntelliJ run configuration for the JVM had somehow been corrupted which took me ages to discover, and then there was an issue with the native runner.

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| JVM (OpenJDK 11) | 21&nbsp;±&nbsp;20 | ` 99,  30,  25,  41,  20,  31,  16,  10,  15,  10,  15,  11,  11,  11,  12,  12,  12,  11,  11,  13` |
| Node JS          | 14.5&nbsp;±&nbsp;14.8       | ` 70, 35, 31, 12, 9, 8, 9, 8, 7, 11, 9, 9, 8, 8, 8, 9, 8, 7, 7, 8` |
| Native           | 147&nbsp;±&nbsp;9.7         | `158, 143, 132, 141, 143, 142, 141, 152, 171, 143, 142, 146, 144, 144, 137, 149, 150, 137, 150, 170` | 

# Day 2

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| JVM (OpenJDK 11) | 9.1&nbsp;±&nbsp;14.7   | `71, 16, 11, 7, 7, 6, 5, 5, 5, 8, 4, 5, 2, 3, 3, 4, 2, 2, 4, 4` |
| Node JS          | 17.4&nbsp;±&nbsp;18.6  | `90, 43, 25, 20, 12, 10, 8, 8, 9, 8, 9, 8, 8, 8, 19, 11, 11, 11, 9, 11` |
| Native           | 39.1&nbsp;±&nbsp;7.5   | `36, 36, 37, 34, 34, 36, 34, 45, 41, 68, 34, 42, 36, 35, 42, 34, 38, 35, 41, 34` | 

# Day 3

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| JVM (OpenJDK 11) | 5.7&nbsp;±&nbsp;11.1   | `53, 7, 4, 4, 2, 5, 5, 4, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1` |
| Node JS          | 13.1&nbsp;±&nbsp;17.0  | `73, 41, 34, 11, 7, 9, 6, 10, 6, 7, 6, 5, 6, 8, 5, 3, 3, 4, 4, 4` |
| Native           | 25.2&nbsp;±&nbsp;6.4   | `20, 20, 29, 31, 22, 17, 36, 17, 30, 26, 20, 18, 31, 22, 33, 27, 25, 18, 36, 17` | 


  