import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.getenv
import kotlin.system.measureNanoTime

actual inline fun measureNanos(block: () -> Unit) = measureNanoTime(block)
fun main(args: Array<String>) {
    // Only single-threaded coroutine support for now
    // See https://github.com/Kotlin/kotlinx.coroutines/issues/462
    runBlocking {
        val repeat = args[0].toInt()
        val day = args[1].toInt()
        run(repeat, day, "Native")
    }
}

actual fun readInput(name: String): String {
    val returnBuffer = StringBuilder()
    val inputDir = getenv("AOC_INPUT_DIR")?.toKString() ?: ""
    val file = fopen("""$inputDir\$name""", "r") ?: throw IllegalArgumentException("Cannot open input file $name")

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            var line = fgets(buffer, readBufferLength, file)?.toKString()
            while (line != null) {
                returnBuffer.append(line)
                line = fgets(buffer, readBufferLength, file)?.toKString()
            }
        }
    } finally {
        fclose(file)
    }

    return returnBuffer.toString()
}