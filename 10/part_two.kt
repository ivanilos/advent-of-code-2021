import java.io.File

fun readInput() : List<String> {
    return File("input.txt")
        .readLines()
}

class SyntaxChecker {

    companion object {
        val OPEN_CHARS = listOf('(', '[', '{', '<')
        val MATCH = mapOf('(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>')
        val SCORE_TABLE = mapOf(')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4)
    }

    fun calcScore(line : String) : Long {
        val stack = mutableListOf<Char>()

        if (isIncomplete(line, stack)) {
            return completionScore(stack.toList())
        } else {
            return 0
        }
    }

    private fun isIncomplete(line : String, stack : MutableList<Char>) : Boolean {
        for (ch in line) {
            if (ch in OPEN_CHARS) {
                stack.add(ch)
            } else {
                if (stack.isNotEmpty() && MATCH[stack.last()] == ch) {
                    stack.removeLast()
                } else {
                    return false
                }
            }
        }
        return true
    }

    private fun completionScore(unmatched : List<Char>) : Long {
        var result = 0L
        for (ch in unmatched.reversed()) {
            result *= 5
            result += SCORE_TABLE[MATCH[ch]!!]!!
        }
        return result
    }
}

fun solve(lines : List<String>) : Long {
    val completionScores = mutableListOf<Long>()

    val syntaxChecker = SyntaxChecker()
    for (line in lines) {
        completionScores.add(syntaxChecker.calcScore(line))
    }

    val sortedScores = completionScores.filter { it > 0 }.sortedBy { it }
    return sortedScores[sortedScores.size / 2]
}

fun main() {
    val lines = readInput()
    val ans = solve(lines)

    println(ans)
}
