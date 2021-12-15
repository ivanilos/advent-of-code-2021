import java.io.File
import java.util.PriorityQueue

fun readInput() : Grid {
    val input = File("input.txt")
        .readText()
        .split("\r\n")
        .filter { it.isNotEmpty() }
        .map { row -> row.map { it.digitToInt() }}

    return Grid(input)
}

class Grid(mat : List<List<Int>>) {

    companion object {
        val deltaX = listOf(1, 0, -1, 0)
        val deltaY = listOf(0, 1, 0, -1)
        const val INF = 1e9.toInt()
        const val DIRECTIONS = 4
    }

    private val mat : List<List<Int>>
    private val rows : Int
    private val cols : Int

    init {
        this.mat = mat
        rows = mat.size
        cols = mat[0].size
    }

    fun minCostToEnd() : Int {
        return dijkstra(0, 0, rows - 1, cols - 1)
    }

    private fun dijkstra(startX : Int, startY : Int, endX : Int, endY : Int) : Int {
        val distance = Array(rows) { IntArray(cols) { INF }}
        distance[0][0] = 0

        val pq = PriorityQueue()
        { o1: Triple<Int, Int, Int>, o2: Triple<Int, Int, Int> ->
            if (o1.first < o2.first) -1 else 1
        }
        pq.add(Triple(0, startX, startY))

        while(pq.isNotEmpty()) {
            val (cost, x, y) = pq.peek()
            pq.remove()

            if (cost > distance[x][y]) continue

            for (dir in 0 until DIRECTIONS) {
                val nx = x + deltaX[dir]
                val ny = y + deltaY[dir]
                
                if (isIn(nx, ny)) {
                    if (distance[nx][ny] > distance[x][y] + mat[nx][ny]) {
                        distance[nx][ny] = distance[x][y] + mat[nx][ny]
                        pq.add(Triple(distance[nx][ny], nx, ny))
                    }
                }
            }
        }
        return distance[endX][endY]
    }

    private fun isIn(x : Int, y : Int) : Boolean {
        return x in 0 until rows && y in 0 until cols
    }
}

fun solve(grid : Grid) : Int {
    return grid.minCostToEnd()
}

fun main() {
    val grid = readInput()
    val ans = solve(grid)

    println(ans)
}
