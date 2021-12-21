import java.io.File

fun readInput() : Game {
    val input = File("input.txt")
        .readLines()

    val posRegex = """Player (\d+) starting position: (\d+)""".toRegex()

    val (_, player1Pos) = posRegex.find(input[0])?.destructured!!
    val (_, player2Pos) = posRegex.find(input[1])?.destructured!!

    return Game(player1Pos.toInt(), player2Pos.toInt())
}

@JvmName("plusLongLong")
operator fun Pair<Long, Long>.plus(rhs : Pair<Long, Long>): Pair<Long, Long> {
    return Pair(first + rhs.first, second + rhs.second)
}

operator fun Pair<Int, Int>.plus(rhs : Pair<Int, Int>): Pair<Int, Int> {
    return Pair(first + rhs.first, second + rhs.second)
}

data class GameState(val playersPos : Pair<Int, Int>, val scores : Pair<Int, Int>, val playerToMove : Int)

class Game(player1Pos : Int, player2Pos : Int) {

    companion object {

        const val DIE_MAX_VAL = 3
        const val MIN_SCORE_TO_WIN = 21
        const val MAX_POS = 10
        const val ROLLS_PER_TURN = 3
        const val PLAYER_1 = 0
        const val FIRST_PLAYER_TO_MOVE = PLAYER_1

    }

    private val playersPos = mutableListOf(player1Pos, player2Pos)
    private val allPossibleRolls = mutableListOf<Int>()

    init {
        calcPossibleDiceSum(0, ROLLS_PER_TURN)
    }

    fun calcUniverses() : Pair<Long, Long> {
        val dp = mutableMapOf<GameState, Pair<Long, Long>>()
        val initialGameState = GameState(Pair(playersPos[0], playersPos[1]), Pair(0, 0), FIRST_PLAYER_TO_MOVE)

        return solve(initialGameState, dp)
    }

    private fun calcPossibleDiceSum(curSum : Int, remainingRolls : Int) {
        if (remainingRolls == 0) {
            allPossibleRolls.add(curSum)
        } else {
            for (value in 1..DIE_MAX_VAL) {
                calcPossibleDiceSum(curSum + value, remainingRolls - 1)
            }
        }
    }

    private fun solve(gameState : GameState, dp : MutableMap<GameState, Pair<Long, Long>>) : Pair<Long, Long> {
        if (gameState.scores.first >= MIN_SCORE_TO_WIN) return Pair(1, 0)
        if (gameState.scores.second >= MIN_SCORE_TO_WIN) return Pair(0, 1)
        if (gameState in dp) return dp[gameState]!!

        var result = Pair(0L, 0L)
        for (roll in allPossibleRolls) {
            val newPlayersPos = calcPlayersPos(gameState.playersPos, gameState.playerToMove, roll)
            val newScore = calcScore(newPlayersPos, gameState.playerToMove, gameState.scores)
            val newPlayerToMove = nextPlayerToMove(gameState.playerToMove)

            val newGameState = GameState(newPlayersPos, newScore, newPlayerToMove)
            result += solve(newGameState, dp)
        }

        dp[gameState] = result
        return result
    }

    private fun calcPlayersPos(playersPos : Pair<Int, Int>, playerToMove : Int, diceSum : Int) : Pair<Int, Int> {
        var result = if (playerToMove == PLAYER_1) {
            Pair((playersPos.first + diceSum) % MAX_POS, playersPos.second)
        } else {
            Pair(playersPos.first, (playersPos.second + diceSum) % MAX_POS)
        }

        if (result.first == 0) {
            result = Pair(MAX_POS, result.second)
        } else if (result.second == 0) {
            result = Pair(result.first, MAX_POS)
        }
        return result
    }

    private fun calcScore(playersPos : Pair<Int, Int>, playerToMove : Int, scores: Pair<Int, Int>) : Pair<Int, Int> {
        return if (playerToMove == PLAYER_1) {
            scores + Pair(playersPos.first, 0)
        } else {
            scores + Pair(0, playersPos.second)
        }
    }

    private fun nextPlayerToMove(playerToMove: Int) : Int {
        return 1 - playerToMove
    }

}

fun solve(game : Game) : Long {
    val (player1Wins, player2Wins) = game.calcUniverses()
    return maxOf(player1Wins, player2Wins)
}

fun main() {
    val game = readInput()
    val ans = solve(game)

    println(ans)
}
