import java.io.File
import java.util.Collections.max
import java.util.Collections.min

fun readInput() : Game {
    val input = File("input.txt")
        .readLines()

    val posRegex = """Player (\d+) starting position: (\d+)""".toRegex()

    val (_, player1Pos) = posRegex.find(input[0])?.destructured!!
    val (_, player2Pos) = posRegex.find(input[1])?.destructured!!

    return Game(player1Pos.toInt(), player2Pos.toInt())
}

class Game(player1Pos : Int, player2Pos : Int) {

    companion object {

        const val DIE_MAX_VAL = 100
        const val MIN_SCORE_TO_WIN = 1000
        const val MAX_POS = 10
        const val ROLLS_PER_TURN = 3

    }

    private val playersPos = mutableListOf(player1Pos, player2Pos)
    private var dieCurVal = 1
    private var playerToMove = 0
    private val scores = mutableListOf(0, 0)
    private var turns = 0
    private var dieRolls = 0

    fun playTurn() {
        playersPos[playerToMove] = makeMove(playerToMove)
        scores[playerToMove] += playersPos[playerToMove]

        changePlayerToMove()
        turns += 1
    }

    private fun makeMove(player : Int) : Int {
        var diceSum = 0
        for (i in 1..ROLLS_PER_TURN) {
            diceSum += dieCurVal
            updateDieVal()
        }
        
        val pos = (playersPos[player] + diceSum) % MAX_POS
        return if (pos == 0) MAX_POS else pos
    }

    private fun updateDieVal() {
        dieRolls += 1
        dieCurVal = (dieCurVal + 1) % DIE_MAX_VAL

        if (dieCurVal == 0) dieCurVal = DIE_MAX_VAL
    }

    private fun changePlayerToMove() {
        playerToMove = 1 - playerToMove
    }

    fun isFinished() : Boolean {
        return max(scores) >= MIN_SCORE_TO_WIN
    }

    fun calcScore() : Int {
        return min(scores) * dieRolls;
    }

}

fun solve(game : Game) : Int {
    while(!game.isFinished()) {
        game.playTurn()
    }
    return game.calcScore()
}

fun main() {
    val game = readInput()
    val ans = solve(game)

    println(ans)
}
