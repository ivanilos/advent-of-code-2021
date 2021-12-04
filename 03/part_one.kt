import java.io.File

fun readInput() : List<String> {
    return File("input.txt")
            .readLines()
}

fun getRates(report : List<String>) : Pair<Int, Int> {
    var lineFreq = Array(report[0].length) { Array(2) {0} }

    for (line in report) {
        for ((idx, char) in line.withIndex()) {
            lineFreq[idx][char - '0']++;
        }
    }
    return calcRatesFrom(lineFreq)
}

fun calcRatesFrom(lineFreq : Array<Array<Int>>) : Pair<Int, Int> {
    var gammaRate = 0
    var epsRate = 0

    for (column in lineFreq.indices) {
        gammaRate *= 2
        epsRate *= 2
        if (lineFreq[column][1] > lineFreq[column][0]) {
            gammaRate++
        } else {
            epsRate++
        }
    }
    return Pair(gammaRate, epsRate)
}

fun solve(gammaRate : Int, epsRate : Int) : Int {
    return gammaRate * epsRate;
}

fun main() {
    val report = readInput()
    val (gammaRate, epsRate) = getRates(report)
    val ans = solve(gammaRate, epsRate)

    println(ans)
}