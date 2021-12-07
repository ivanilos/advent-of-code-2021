import java.io.File
import kotlin.math.abs

fun readInput() : List<Int> {
    return File("input.txt")
            .readLines()
            .flatMap { it.split(",") }
            .map { it.toInt() }
}

fun solve(positions : MutableList<Int>) : Int {
    positions.sort()

    val finalPos = positions.sum() / positions.size

    var result = 0
    for (position in positions) {
        val dist = abs(position - finalPos)
        result += ((1 + dist) * dist) / 2
    }
    return result
}

fun main() {
    val positions = readInput()
    val ans = solve(positions.toMutableList())

    println(ans)
}
