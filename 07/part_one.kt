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

    val finalPos = positions[positions.size / 2]

    var result = 0
    for (position in positions) {
        result += abs(position - finalPos)
    }
    return result
}

fun main() {
    val positions = readInput()
    val ans = solve(positions.toMutableList())

    println(ans)
}
