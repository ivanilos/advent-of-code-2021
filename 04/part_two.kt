import java.io.File

private const val SIZE = 5

fun readInput() : Pair<List<Int>, List<Board>> {
    val lines = File("input.txt")
            .readLines()
            .map { it.split("\n") }
            .filter{ it[0] != "" }

    val numbers = lines[0][0].split(",").map{ it.toInt() }
    val boards = mutableListOf<Board>()

    for (i in 1 until lines.size step SIZE) {
        boards.add(Board(lines.subList(i, i + SIZE)))
    }
    return Pair(numbers, boards)
}

class Board(inputBoard: List<List<String>>) {

    private val numbersPos = HashMap<Int, Pair<Int, Int>>()
    private val filledInRows = IntArray(SIZE){0}
    private val filledInCols = IntArray(SIZE){0}
    private val crossed = HashSet<Int>()
    private var boardSum = 0
    private var crossedSum = 0
    private var lastCrossedNumber = 0

    init {
        for ((rowIdx, row) in inputBoard.withIndex()) {
            val values = row[0].split(" ")
                    .filter { it.isNotEmpty() }
                    .map{ it.toInt() }
            for ((colIdx, num) in values.withIndex()) {
                numbersPos[num] = Pair(rowIdx, colIdx)
                boardSum += num
            }
        }
    }

    fun cross(num : Int) {
        if (num in numbersPos) {
            lastCrossedNumber = num
            crossed.add(num)
            crossedSum += num
            val (rowIdx, colIdx) = numbersPos[num]!!
            filledInRows[rowIdx]++
            filledInCols[colIdx]++
        }
    }

    fun hasBingo() : Boolean {
        for (i in 0 until SIZE) {
            if (filledInRows[i] >= SIZE || filledInCols[i] >= SIZE) {
                return true
            }
        }
        return false
    }

    fun getScore(): Int {
        return (boardSum - crossedSum) * lastCrossedNumber
    }
}

fun lastWinnerBoardScore(numbers : List<Int>, boards: List<Board>) : Int {
    val winnerBoardsPoints = mutableListOf<Int>()
    val winnerBoardsIdx = HashSet<Int>()

    for (num in numbers) {
        for ((idx, board) in boards.withIndex()) {
            board.cross(num)
            if (idx !in winnerBoardsIdx && board.hasBingo()) {
                winnerBoardsPoints.add(board.getScore())
                winnerBoardsIdx.add(idx)
            }
        }
    }
    if (winnerBoardsPoints.size == 0) {
        throw Exception("No board won bingo")
    }
    return winnerBoardsPoints.last()
}

fun solve(numbers : List<Int>, boards : List<Board>) : Int {
    return lastWinnerBoardScore(numbers, boards)
}

fun main() {
    val (numbers, boards) = readInput()
    val ans = solve(numbers, boards)

    println(ans)
}
