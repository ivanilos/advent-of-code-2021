import java.io.File

fun readInput() : Paper {
    val lines = File("input.txt")
        .readLines()

    val dots = mutableListOf<Pair<Int, Int>>()
    val instructions = mutableListOf<Pair<Char, Int>>()

    parseInput(lines, dots, instructions)
    return Paper(dots, instructions)
}

fun parseInput(inputLines : List<String>,
               dots : MutableList<Pair<Int, Int>>,
               instructions: MutableList<Pair<Char, Int>>) {

    var readingInstructions = false
    for (line in inputLines) {
        if (line.isEmpty()) {
            readingInstructions = true
        } else if (readingInstructions) {
            instructions.add(parseInstruction(line))
        } else {
            dots.add(parseDot(line))
        }
    }
}

fun parseDot(line : String) : Pair<Int, Int> {
    val (x, y) = line.split(",").map { it.toInt() }
    return Pair(x, y)
}

fun parseInstruction(line : String) : Pair<Char, Int> {
    val regex = """fold along ([x|y])=(\d+)""".toRegex()
    val (axis, pos) = regex.find(line)?.destructured!!
    return Pair(axis[0], pos.toInt())
}

class Paper(dots : MutableList<Pair<Int, Int>>, instructions : MutableList<Pair<Char, Int>>) {

    var dots : MutableSet<Pair<Int, Int>>
    private var instructions : List<Pair<Char,Int>>

    init {
        this.dots = dots.toMutableSet()
        this.instructions = instructions
    }

    fun foldOnce() {
        val updatedDots = mutableSetOf<Pair<Int, Int>>()
        for (dot in dots) {
            if (instructions[0].first == 'x') {
                val newX = if (dot.first < instructions[0].second) dot.first else 2 * instructions[0].second - dot.first
                val newY = dot.second

                updatedDots.add(Pair(newX, newY))
            } else {
                val newX = dot.first
                val newY = if (dot.second < instructions[0].second) dot.second else 2 * instructions[0].second - dot.second


                updatedDots.add(Pair(newX, newY))
            }
        }
        dots = updatedDots
    }

}

fun solve(paper: Paper): Int {
    paper.foldOnce()

    return paper.dots.size
}

fun main() {
    val paper = readInput()
    val ans = solve(paper)

    println(ans)
}
