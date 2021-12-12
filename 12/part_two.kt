import java.io.File

fun readInput() : Graph {
    val edges = File("input.txt")
        .readLines()
        .map { it.split("-") }

    return Graph(edges)
}

class Graph(edges : List<List<String>>) {

    companion object {
        const val START = "start"
        const val END = "end"
    }

    private var start = 0
    private var end = 0
    private var adjList = mutableMapOf<Int, MutableList<Int>>()
    private var nodes = 0
    private var smallCaves = setOf<Int>()

    init {
        val nodeMap = mapNodesToInt(edges)
        smallCaves = determineSmallCaves(nodeMap)
        addEdges(edges, nodeMap)

        start = nodeMap[START]!!
        end = nodeMap[END]!!
    }

    fun calculatePaths() : Int {
        val timesVisitedSmallCaves = mutableMapOf(start to 2)

        return DFS(start, timesVisitedSmallCaves, false)
    }

    private fun DFS(node : Int, timesVisitedSmallCaves : MutableMap<Int, Int>, smallVisitedTwice : Boolean) : Int {
        if (node == end) return 1

        var result = 0
        for (adjNode in adjList[node]!!) {
            if (isSmallCave(adjNode)) {
                val timesVisited = timesVisitedSmallCaves.getOrDefault(adjNode, 0)

                if (timesVisited == 0 || timesVisited == 1 && !smallVisitedTwice) {
                    timesVisitedSmallCaves[adjNode] = timesVisited + 1
                    result += DFS(adjNode, timesVisitedSmallCaves, smallVisitedTwice || timesVisited == 1)
                    timesVisitedSmallCaves[adjNode] = timesVisited
                }
            } else if (!isSmallCave(adjNode)) {
                result += DFS(adjNode, timesVisitedSmallCaves, smallVisitedTwice)
            }
        }
        return result
    }

    private fun isSmallCave(cave : Int) : Boolean {
        return cave in smallCaves
    }

    private fun mapNodesToInt(edges : List<List<String>>) : MutableMap<String, Int> {
        val result = mutableMapOf<String, Int>()
        for ((a, b) in edges) {
            if (a !in result) result[a] = nodes++
            if (b !in result) result[b] = nodes++
        }
        return result
    }

    private fun determineSmallCaves(nodeMap : MutableMap<String, Int>) : Set<Int> {
        val result = mutableSetOf<Int>()

        for ((key, value) in nodeMap) {
            if (isLowerCase(key)) {
                result.add(value)
            }
        }
        return result
    }

    private fun addEdges(edges: List<List<String>>, nodeMap : MutableMap<String, Int>) {
        for (edge in edges) {
            val a = nodeMap[edge[0]]!!
            val b = nodeMap[edge[1]]!!

            addEdge(a, b)
        }
    }

    private fun addEdge(a : Int, b : Int) {
        val adjA = adjList.getOrDefault(a, mutableListOf())
        adjA.add(b)
        adjList[a] = adjA

        val adjB = adjList.getOrDefault(b, mutableListOf())
        adjB.add(a)
        adjList[b] = adjB
    }
}

fun isLowerCase(str : String) : Boolean {
    for (ch in str) {
        if (!ch.isLowerCase()) return false
    }
    return true
}

fun solve(graph : Graph) : Int {
    return graph.calculatePaths()
}

fun main() {
    val graph = readInput()
    val ans = solve(graph)

    println(ans)
}
