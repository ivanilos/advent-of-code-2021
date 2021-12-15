import java.io.File

const val LAST_STEP = 40

fun readInput() : Polymer {
    val input = File("input.txt")
        .readText()
        .split("\r\n\r\n")

    val original = input[0]
    val rules = mutableMapOf<String, String>()

    input[1].split("\n").filter{ it.isNotEmpty() }.forEach {
        val regex = "(\\w+) -> (\\w+)".toRegex()

        val (key, value) = regex.find(it)?.destructured!!
        rules[key] = value
    }

    return Polymer(original, rules)
}

class Polymer(original : String, rules : Map<String, String>) {

    private val original : String
    private val rules : Map<String, String>
    private val pairFreq = mutableMapOf<String, Long>()
    private var steps = 0

    init {
        this.original = original
        this.rules = rules

        for (i in 1 until original.length) {
            val pair = original.substring(i - 1, i + 1)
            pairFreq[pair] = pairFreq.getOrDefault(pair, 0) + 1
        }

    }

    fun applyStep() {
        steps++

        val newPairs = mutableMapOf<String, Long>()

        for ((key, value) in pairFreq) {
            if (key in rules) {
                val leftPair = key[0] + rules[key]!!
                val rightPair = rules[key]!! + key[1]

                newPairs[leftPair] = newPairs.getOrDefault(leftPair, 0) + value
                newPairs[rightPair] = newPairs.getOrDefault(rightPair, 0) + value

                pairFreq[key] = 0
            }
        }

        for ((key, value) in newPairs) {
            pairFreq[key] = pairFreq.getOrDefault(key, 0) + value
        }
    }

    fun calc(): Long {
        val elemFreq = mutableMapOf<Char, Long>()

        elemFreq[original.last()] = 1
        for ((key, value) in pairFreq) {
            elemFreq[key[0]] = elemFreq.getOrDefault(key[0], 0) + value
        }

        val maxi = elemFreq.maxOf { it.value }
        val mini = elemFreq.minOf { it.value }
        return maxi - mini
    }
}

fun solve(polymer : Polymer): Long {
    for (i in 1..LAST_STEP) {
        polymer.applyStep()
    }
    return polymer.calc()
}

fun main() {
    val polymer = readInput()
    val ans = solve(polymer)

    println(ans)
}
