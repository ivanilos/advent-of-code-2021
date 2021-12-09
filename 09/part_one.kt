import java.io.File

fun readInput() : HeightMap {
    val inputLines = File("input.txt")
        .readLines()
        .map { line -> line.split("")
            .filter { it.isNotEmpty() }
            .map{ it.toInt() }}

    return HeightMap(inputLines)
}

class HeightMap(inputLines : List<List<Int>>) {

    companion object {
        val NEIGHBORS_DELTA = listOf(Pair(0, 1),
                                        Pair(1, 0),
                                        Pair(0, -1),
                                        Pair(-1, 0))
    }

    val rows = inputLines.size
    val cols = inputLines[0].size
    val heights = inputLines

    fun getNeighbors(x : Int, y : Int) : List<Int> {
        val result = mutableListOf<Int>()
        for (delta in NEIGHBORS_DELTA) {
            val neighborX = x + delta.first
            val neighborY = y + delta.second

            if (isIn(neighborX, neighborY)) {
                result.add(heights[neighborX][neighborY])
            }
        }
        return result.toList()
    }

    fun isIn(x : Int, y : Int) : Boolean {
        return x in 0 until rows && y in 0 until cols
    }

    fun isLowPoint(x : Int, y : Int) : Boolean {
        val height = heights[x][y]
        val neighbors = getNeighbors(x, y)
        return neighbors.none { it <= height }
    }
}

fun solve(heightMap : HeightMap) : Int {
    var result = 0
    
    for (i in 0 until heightMap.rows) {
        for (j in 0 until heightMap.cols) {
            if (heightMap.isLowPoint(i, j)) {
                result += heightMap.heights[i][j] + 1
            }
        }
    }
    return result
}

fun main() {
    val heightMap = readInput()
    val ans = solve(heightMap)

    println(ans)
}
