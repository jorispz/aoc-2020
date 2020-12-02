import day02.p02
import day1.p01
import kotlin.math.sqrt


suspend fun run(repeat: Int, day: Int, platform: String) {
    println("Running day $day $repeat times on platform $platform")
    val puzzles = listOf(p01, p02)
    val times = (1..repeat).map {
        measureNanos {
            val p = puzzles[day - 1]
            p()
        }
    }
    println("Done")

    val average = times.average()
    val std = sqrt(times.map { it.toDouble() - average }.map { it * it }.sum() / times.size)

    println("\nAverage: ${average / 1e3} +- ${std / 1e3}")
    println("Times: ${times.map { (it / 1e3).toInt() }.joinToString()} us")
    println("\nAverage: ${average / 1e6} +- ${std / 1e6}")
    println("Times: ${times.map { (it / 1e6).toInt() }.joinToString()} ms")
}


expect inline fun measureNanos(block: () -> Unit): Long
