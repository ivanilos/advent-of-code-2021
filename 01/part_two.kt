import java.io.File

fun readInput() : List<Int> {
    return File("input.txt")
            .readLines()
            .map(String::toInt)
}

fun sumOfLastThreeElements(list : List<Int>, index : Int) : Int {
    return list.slice(index - 2..index).sum()
}

fun solve(depths: List<Int>) : Int {
    var result = 0

    for (i in 3..depths.size - 1) {
        val prev = sumOfLastThreeElements(depths, i - 1)
        val cur = sumOfLastThreeElements(depths, i)

        result += if (cur > prev) 1 else 0       
    }
    return result
}

fun main() {
    val depths = readInput()
    val ans = solve(depths)

    println(ans)
}
