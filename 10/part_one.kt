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
        val SCORE_TABLE = mapOf(')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137)
    }

    fun calcScore(line : String) : Int {
        val stack = mutableListOf<Char>()

        for (ch in line) {
            if (ch in OPEN_CHARS) {
                stack.add(ch)
            } else {
                if (stack.isNotEmpty() && MATCH[stack.last()] == ch) {
                    stack.removeLast()
                } else {
                    return SCORE_TABLE[ch]!!
                }
            }
        }
        return 0
    }
}

fun solve(lines : List<String>) : Int {
    var result = 0

    val syntaxChecker = SyntaxChecker()
    for (line in lines) {
        result += syntaxChecker.calcScore(line)
    }
    return result
}

fun main() {
    val lines = readInput()
    val ans = solve(lines)

    println(ans)
}
