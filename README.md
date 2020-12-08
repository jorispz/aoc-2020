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
| GraalVM          | 21&nbsp;±&nbsp;20 | ` 99, 30, 25, 41, 20, 31, 16, 10, 15, 10, 15, 11, 11, 11, 12, 12, 12, 11, 11, 13` |
| Node JS          | 14.5&nbsp;±&nbsp;14.8       | ` 70, 35, 31, 12, 9, 8, 9, 8, 7, 11, 9, 9, 8, 8, 8, 9, 8, 7, 7, 8` |
| Native           | 147&nbsp;±&nbsp;9.7         | `158, 143, 132, 141, 143, 142, 141, 152, 171, 143, 142, 146, 144, 144, 137, 149, 150, 137, 150, 170` | 

# Day 2

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| GraalVM          | 9.1&nbsp;±&nbsp;14.7   | `71, 16, 11, 7, 7, 6, 5, 5, 5, 8, 4, 5, 2, 3, 3, 4, 2, 2, 4, 4` |
| Node JS          | 17.4&nbsp;±&nbsp;18.6  | `90, 43, 25, 20, 12, 10, 8, 8, 9, 8, 9, 8, 8, 8, 19, 11, 11, 11, 9, 11` |
| Native           | 39.1&nbsp;±&nbsp;7.5   | `36, 36, 37, 34, 34, 36, 34, 45, 41, 68, 34, 42, 36, 35, 42, 34, 38, 35, 41, 34` | 

# Day 3

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| GraalVM          | 5.7&nbsp;±&nbsp;11.1   | `53, 7, 4, 4, 2, 5, 5, 4, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1` |
| Node JS          | 13.1&nbsp;±&nbsp;17.0  | `73, 41, 34, 11, 7, 9, 6, 10, 6, 7, 6, 5, 6, 8, 5, 3, 3, 4, 4, 4` |
| Native           | 25.2&nbsp;±&nbsp;6.4   | `20, 20, 29, 31, 22, 17, 36, 17, 30, 26, 20, 18, 31, 22, 33, 27, 25, 18, 36, 17` | 

# Day 4

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| GraalVM          | 11.3&nbsp;±&nbsp;14,5   | `70, 21, 16, 17, 10, 10, 8, 11, 6, 6, 5, 5, 4, 4, 6, 5, 3, 4, 3, 2` |
| Node JS          | 14.8&nbsp;±&nbsp;13.5   | `65, 26, 35, 17, 8, 8, 9, 7, 7, 8, 8, 8, 12, 8, 8, 12, 10, 12, 10, 8` |
| Native           | 56.2&nbsp;±&nbsp;6.0    | `53, 69, 51, 51, 47, 60, 59, 47, 56, 49, 52, 56, 53, 57, 62, 65, 56, 48, 64, 57` | 

# Day 5

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| GraalVM          | 3.9&nbsp;±&nbsp;6.0   | `29, 5, 2, 2, 2, 2, 4, 3, 2, 2, 2, 1, 1, 1, 2, 1, 1, 1, 5, 1` |
| Node JS          | 8.8&nbsp;±&nbsp;8.1   | `35, 24, 13, 17, 8, 9, 4, 4, 4, 5, 3, 5, 4, 5, 5, 3, 4, 3, 4, 5` |
| Native           | 18.9&nbsp;±&nbsp;2.6  | `18, 18, 16, 19, 18, 18, 27, 18, 18, 16, 18, 16, 19, 17, 22, 16, 18, 17, 22, 17` | 

# Day 6

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| GraalVM          | 7.4&nbsp;±&nbsp;10.7   | `52, 10, 11, 12, 7, 5, 8, 6, 5, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2` |
| Node JS          | 19.4&nbsp;±&nbsp;21. 0 | `108, 29, 20, 22, 13, 11, 11, 11, 14, 13, 15, 16, 13, 12, 11, 15, 9, 12, 10, 14` |
| Native           | 58.5&nbsp;±&nbsp;5.0   | `64, 61, 63, 55, 58, 56, 65, 54, 56, 58, 72, 62, 54, 52, 54, 55, 59, 52, 52, 59` | 

# Day 7

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| GraalVM          | 84&nbsp;±&nbsp;71      | `390, 83, 68, 66, 62, 63, 61, 60, 73, 85, 71, 77, 62, 63, 73, 65, 62, 61, 60, 60` |
| Node JS          | 1679&nbsp;±&nbsp;142   | `1723, 1726, 1628, 1657, 1517, 1843, 1748, 1988, 2061, 1620, 1614, 1730, 1646, 1606, 1686, 1577, 1567, 1555, 1506, 1568` |
| Native           | 4721&nbsp;±&nbsp;383   | `4417, 4235, 4648, 4426, 4581, 5086, 4806, 5037, 4304, 5537, 4693, 5095, 4679, 5624, 4955, 4457, 4256, 4552, 4506, 4508` | 

# Day 8

| Platform         | Average (ms)           | Measurements (ms) |
| -----------------| ----------------------:|------------------:|
| GraalVM          | 8.7&nbsp;±&nbsp;9.5    | `47, 15, 16, 7, 5, 5, 3, 4, 5, 6, 5, 5, 7, 5, 5, 5, 5, 6, 6, 4` |
| Node JS          | 23.3&nbsp;±&nbsp;14.1  | `81, 37, 20, 22, 24, 17, 20, 17, 18, 23, 15, 18, 17, 18, 15, 16, 16, 20, 19, 23` |
| Native           | 124&nbsp;±&nbsp;17     | `106, 134, 141, 112, 136, 105, 139, 106, 134, 107, 153, 108, 128, 135, 117, 119, 154, 90, 118, 124` | 
