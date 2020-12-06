import kotlin.math.roundToLong

external val process: dynamic

actual inline fun measureNanos(block: () -> Unit): Long {
    val start = process.hrtime()
    block()
    val end = process.hrtime(start)
    val nanos = (end[0] * 1e9 + end[1]) as Double
    return nanos.roundToLong()
}

suspend fun main() {
    val arguments = (process["argv"] as Array<String>).drop(2)
    val repeat = arguments[0].toInt()
    val day = arguments[1].toInt()
    run(repeat, day, "JS")
}

external fun require(name: String): dynamic

val fs = require("fs")
val path = require("path");

actual fun readInput(name: String): String {
    val path = path.join(process.env.AOC_INPUT_DIR, name)
    return fs.readFileSync(path, "utf8") as String
}