import java.io.File
import java.lang.Exception

fun readInput() : List<Display> {
    return File("input.txt")
        .readLines()
        .map { Display(it) }
}

class Display(inputLine : String) {

    companion object {
        val UNIQUE_OUTPUT_SIZES = listOf(2, 3, 4, 7)
        val INPUT_TO_OUTPUT_MAP = mapOf("abcefg" to 0,
            "cf" to 1,
            "acdeg" to 2,
            "acdfg" to 3,
            "bcdf" to 4,
            "abdfg" to 5,
            "abdefg" to 6,
            "acf" to 7,
            "abcdefg" to 8,
            "abcdfg" to 9)
    }

    var input = listOf<String>()
    var output = listOf<String>()
    var digitSegmentUnionSize = mutableMapOf<Int, MutableList<Int>>()

    init {
        val (leftSide, rightSide) = inputLine.split('|')
        input = leftSide.split(" ").filter { it.isNotEmpty() }.map { it.toCharArray().sorted().joinToString("") }
        output = rightSide.split(" ").filter { it.isNotEmpty() }.map { it.toCharArray().sorted().joinToString("") }

        for ((curSegments, curDigit) in INPUT_TO_OUTPUT_MAP) {
            val segmentsUnionSize = mutableListOf<Int>()
            for ((otherSegments, _) in INPUT_TO_OUTPUT_MAP) {
                val segmentsUsed = curSegments + otherSegments
                segmentsUsed.toCharArray().sort().toString()
                val distinctUsed = segmentsUsed.toList().distinct().size
                segmentsUnionSize.add(distinctUsed)
            }
            segmentsUnionSize.sort()
            digitSegmentUnionSize[curDigit] = segmentsUnionSize
        }
    }
}

fun findDigitMatch(display: Display, curSegmentUnionSize : List<Int>) : Int {
    for ((digit, knownSegmentUnionSize) in display.digitSegmentUnionSize) {
        if (knownSegmentUnionSize == curSegmentUnionSize) {
            return digit
        }
    }
    throw Exception("Could not find corresponding digit")
}

fun calcSignalToDigitMap(display: Display) : Map<String, Int> {
    val result = mutableMapOf<String, Int>()

    for (curSegments in display.input) {
        val segmentsUnionSize = mutableListOf<Int>()

        for (otherSegments in display.input) {
            val segmentsUsed = curSegments + otherSegments
            segmentsUsed.toCharArray().sort().toString()
            val distinctUsed = segmentsUsed.toList().distinct().size
            segmentsUnionSize.add(distinctUsed)
        }

        segmentsUnionSize.sort()
        result[curSegments] = findDigitMatch(display, segmentsUnionSize)
    }
    return result.toMap()
}

fun calcOutputValue(signalToDigitMap : Map<String, Int>, display : Display) : Int {
    var result = 0
    for (output in display.output) {
        result = 10 * result + signalToDigitMap[output]!!
    }
    return result
}

fun solve(displays : List<Display>) : Int {
    var result = 0

    for (display in displays) {
        val signalToDigitMap = calcSignalToDigitMap(display)
        val outputValue = calcOutputValue(signalToDigitMap, display)
        result += outputValue
    }
    return result
}

fun main() {
    val displays = readInput()
    val ans = solve(displays)

    println(ans)
}
