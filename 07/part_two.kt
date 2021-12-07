import java.io.File
import kotlin.math.*

fun readInput() : List<Int> {
    return File("input.txt")
            .readLines()
            .flatMap { it.split(",") }
            .map { it.toInt() }
}

fun movementCost(positions : List<Int>, finalPos : Int) : Int {
    var result = 0
    for (position in positions) {
        val dist = abs(position - finalPos)
        result += ((1 + dist) * dist) / 2
    }
    return result
}

/*
The optimal real valued position is at max 0.5 away from the mean
so it is sufficient to calc for floor(mean) and ceil(mean)
when the mean is not an integer

https://www.reddit.com/r/adventofcode/comments/rawxad/2021_day_7_part_2_i_wrote_a_paper_on_todays/
*/
fun solve(positions : List<Int>) : Int {
    val meanFloor = positions.sum() / positions.size

    return min(movementCost(positions, meanFloor),
                    movementCost(positions, meanFloor + 1))
}

fun main() {
    val positions = readInput()
    val ans = solve(positions)

    println(ans)
}
