import java.io.File

const val LAST_DAY = 80

fun readInput() : List<Int> {
    return File("input.txt")
            .readLines()
            .flatMap { it.split(",") }
            .map { it.toInt() }
}

class LanternFishPopulation(timers : List<Int>)  {
    companion object {
        const val MAX_TIMER = 8
        const val TIMER_AFTER_0 = 6
    }

    private val timersFreq = MutableList<Int>(MAX_TIMER + 1) { 0 }
    private var daysPassed = 0

    init {
        for (timer in timers) {
            timersFreq[timer]++
        }
    }

    fun passDay() {
        daysPassed++
        val freqWithTimerZero = timersFreq[0]
        for (i in 1..MAX_TIMER) {
            timersFreq[i - 1] = timersFreq[i]
        }
        timersFreq[MAX_TIMER] = freqWithTimerZero

        timersFreq[TIMER_AFTER_0] += freqWithTimerZero
    }

    fun totalPopulation() : Int {
        return timersFreq.sum()
    }
}

fun solve(timers : List<Int>) : Int {
    val lanternFishPopulation = LanternFishPopulation(timers)

    for (day in 1..LAST_DAY) {
        lanternFishPopulation.passDay()
    }
    return lanternFishPopulation.totalPopulation()
}

fun main() {
    val timers = readInput()
    val ans = solve(timers)

    println(ans)
}
