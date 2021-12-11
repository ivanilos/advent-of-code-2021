import java.io.File

fun readInput() : Octopuses {
    val inputLines = File("input.txt")
        .readLines()
        .map { line -> line.split("")
            .filter { it.isNotEmpty() }
            .map{ it.toInt() }}

    return Octopuses(inputLines.map { it.toMutableList() }.toMutableList())
}

class Octopuses(inputLines : MutableList<MutableList<Int>>) {

    companion object {
        const val ENERGY_TO_FLASH = 10
    }

    var energy = inputLines
    var daysPassed = 0
    var flashes = 0
    private val rows = energy.size
    private val cols = energy[0].size

    fun passDay() {
        daysPassed++
        updateEnergy()
    }

    fun allFlashedOnSingleDay() : Boolean {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (energy[row][col] != 0) return false
            }
        }
        return flashes > 0
    }

    private fun updateEnergy() {
        increaseEnergyLevel()
        startChainFlashes()
        resetFlashedEnergy()
    }

    private fun increaseEnergyLevel() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                energy[row][col]++
            }
        }
    }

    private fun startChainFlashes() {
        val queue = ArrayDeque<Pair<Int, Int>>()
        val flashedThisDay = mutableSetOf<Pair<Int, Int>>()

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (energy[row][col] >= ENERGY_TO_FLASH) {
                    flashedThisDay.add(Pair(row, col))
                    queue.add(Pair(row, col))
                }
            }
        }

        while (queue.isNotEmpty()) {
            val (row, col) = queue.first()
            queue.removeFirst()

            val neighbors = getNeighbors(row, col)
            for (neighbor in neighbors) {
                energy[neighbor.first][neighbor.second]++
                if (energy[neighbor.first][neighbor.second] >= ENERGY_TO_FLASH &&
                    neighbor !in flashedThisDay) {
                    flashedThisDay.add(neighbor)
                    queue.add(neighbor)
                }
            }
        }
    }

    private fun getNeighbors(row : Int, col : Int) : MutableList<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        for (i in -1..1) {
            for (j in -1.. 1) {
                if (isIn(row + i, col + j)) {
                    result.add(Pair(row + i, col + j))
                }
            }
        }
        return result
    }

    private fun isIn(row : Int, col : Int) : Boolean {
        return row in 0 until rows && col in 0 until cols
    }

    private fun resetFlashedEnergy() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (energy[row][col] >= ENERGY_TO_FLASH) {
                    flashes++
                    energy[row][col] = 0
                }
            }
        }
    }
}

fun solve(octopuses : Octopuses) : Int {
    while (!octopuses.allFlashedOnSingleDay()) {
        octopuses.passDay()
    }
    return octopuses.daysPassed
}

fun main() {
    val octopuses = readInput()
    val ans = solve(octopuses)

    println(ans)
}
