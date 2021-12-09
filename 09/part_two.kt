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
        const val INVALID_HEIGHT_FOR_BASIN = 9
        val NEIGHBORS_DELTA = listOf(Pair(0, 1),
            Pair(1, 0),
            Pair(0, -1),
            Pair(-1, 0))
    }

    val rows = inputLines.size
    val cols = inputLines[0].size
    val heights = inputLines

    fun getNeighbors(x : Int, y : Int) : List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        for (delta in NEIGHBORS_DELTA) {
            val neighborX = x + delta.first
            val neighborY = y + delta.second

            if (isIn(neighborX, neighborY)) {
                result.add(Pair(neighborX, neighborY))
            }
        }
        return result.toList()
    }

    fun isIn(x : Int, y : Int) : Boolean {
        return x in 0 until rows && y in 0 until cols
    }

    fun isLowPoint(x : Int, y : Int) : Boolean {
        val height = heights[x][y]
        val neighborsHeights = getNeighbors(x, y)
        return neighborsHeights.none { heights[it.first][it.second] <= height }
    }

    fun getBasins() : MutableList<List<Pair<Int, Int>>> {
        val result = mutableListOf<List<Pair<Int, Int>>>()

        val processed = mutableSetOf<Pair<Int, Int>>()
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (isLowPoint(i, j)) {
                    val basinPoints = mutableListOf<Pair<Int, Int>>()
                    DFS(i, j, processed, basinPoints)
                    result.add(basinPoints)
                }
            }
        }
        return result
    }

    fun DFS(x : Int, y : Int, processed : MutableSet<Pair<Int, Int>>, basinPoints : MutableList<Pair<Int, Int>>) {
        processed.add(Pair(x, y))
        basinPoints.add(Pair(x, y))
        val sameBasinNeighbors = getNeighbors(x, y)
            .filter { heights[it.first][it.second] != INVALID_HEIGHT_FOR_BASIN }
        
        for (sameBasinNeighbor in sameBasinNeighbors) {
            if (sameBasinNeighbor !in processed) {
                DFS(sameBasinNeighbor.first, sameBasinNeighbor.second, processed, basinPoints)
            }
        }
    }
}

fun solve(heightMap : HeightMap) : Int {
    val basins = heightMap.getBasins()
    basins.sortBy { -it.size }

    return basins.take(3).map { it.size }.reduce { a, b -> a * b }
}

fun main() {
    val heightMap = readInput()
    val ans = solve(heightMap)

    println(ans)
}
