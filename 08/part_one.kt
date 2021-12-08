import java.io.File

fun readInput() : List<Display> {
    return File("input.txt")
            .readLines()
            .map { Display(it) }
}

class Display(inputLine : String) {

    companion object {
        val UNIQUE_OUTPUT_SIZES = listOf(2, 3, 4, 7)
    }

    var input = listOf<String>()
    var output = listOf<String>()

    init {
        val (leftSide, rightSide) = inputLine.split('|')
        input = leftSide.split(" ").filter { it.isNotEmpty() }
        output = rightSide.split(" ").filter { it.isNotEmpty() }
    }
}

fun solve(displays : List<Display>) : Int {
    var result = 0

    for (display in displays) {
        result += display.output.filter{ it.length in Display.UNIQUE_OUTPUT_SIZES }.size
    }
    return result
}

fun main() {
    val displays = readInput()
    val ans = solve(displays)

    println(ans)
}
