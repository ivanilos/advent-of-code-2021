import java.io.File

fun readInput() : List<String> {
    return File("input.txt")
            .readLines()
}

fun getRates(report : List<String>) : Pair<Int, Int> {
    val oxygenGenRating = calcOxygenRating(report)
    val carbonDioxideScrubRating = calcCarbonDioxideScrubRating(report)

    return Pair(oxygenGenRating, carbonDioxideScrubRating)
}

fun calcOxygenRating(report : List<String>) : Int {
    val columns = report[0].length
    var reportCopy = report.toList()

    var column = 0
    while (column < columns && reportCopy.size > 1) {
        val mostFrequentBit = calcMostFrequentBit(reportCopy, column)
        reportCopy = getUpdatedReport(reportCopy, column, mostFrequentBit)
        column++
    }

    return reportCopy[0].toInt(2)
}

fun calcCarbonDioxideScrubRating(report : List<String>) : Int {
    val columns = report[0].length
    var reportCopy = report.toList()

    var column = 0
    while (column < columns && reportCopy.size > 1) {
        val leastFrequentBit = calcLeastFrequentBit(reportCopy, column)
        reportCopy = getUpdatedReport(reportCopy, column, leastFrequentBit)
        column++
    }
    
    return reportCopy[0].toInt(2)
}

fun calcMostFrequentBit(report : List<String>, column : Int) : Char {
    val freq = IntArray(2)

    for (line in report) {
        freq[line[column] - '0']++
    }
    return if (freq[1] >= freq[0]) '1' else '0'
}

fun calcLeastFrequentBit(report : List<String>, column : Int) : Char {
    val freq = IntArray(2)

    for (line in report) {
        freq[line[column] - '0']++
    }
    return if (freq[1] < freq[0]) '1' else '0'
}

fun getUpdatedReport(report : List<String>, column : Int, matchingBit : Char) : List<String> {
    val updatedReport = mutableListOf<String>()

    for (line in report) {
        if (line[column] == matchingBit) {
            updatedReport.add(line)
        }
    }
    return updatedReport
}

fun solve(oxygenGenRating : Int, carbonDioxideScrubRating : Int) : Int {
    return oxygenGenRating * carbonDioxideScrubRating;
}

fun main() {
    val report = readInput()
    val (oxygenGenRating, carbonDioxideScrubRating) = getRates(report)
    val ans = solve(oxygenGenRating, carbonDioxideScrubRating)

    println(ans)
}