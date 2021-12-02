import java.io.File

fun readInput() : List<Int> {
    return File("input.txt")
            .readLines()
            .map(String::toInt)
}

fun solve(depths: List<Int>) : Int {
    var result = 0

    for (i in 1..depths.size - 1) {
        val prev = depths[i - 1]
        val cur = depths[i]

        result += if (cur > prev) 1 else 0       
    }
    return result
}

fun main() {
    val depths = readInput()
    val ans = solve(depths)

    println(ans)
}
