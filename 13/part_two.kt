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

    fun fold() {
        for (instruction in instructions) {
            val updatedDots = mutableSetOf<Pair<Int, Int>>()
            for (dot in dots) {
                if (instruction.first == 'x') {
                    val newX = if (dot.first < instruction.second) dot.first else 2 * instruction.second - dot.first
                    val newY = dot.second

                    updatedDots.add(Pair(newX, newY))
                } else {
                    val newX = dot.first
                    val newY = if (dot.second < instruction.second) dot.second else 2 * instruction.second - dot.second


                    updatedDots.add(Pair(newX, newY))
                }
            }
            dots = updatedDots
        }
    }

    fun print() {
        val maxDotX = dots.maxOf { it.first }
        val maxDotY = dots.maxOf { it.second }

        for (y in 0..maxDotY) {
            for (x in 0..maxDotX) {
                if (Pair(x, y) in dots) {
                    print("@")
                } else {
                    print(" ")
                }
            }
            println()
        }
    }
}

fun solve(paper: Paper) {
    paper.fold()
    paper.print() // REUPUPKR
}

fun main() {
    val paper = readInput()
    solve(paper)
}
