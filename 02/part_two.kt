import java.io.File

fun readInput() : List<List<String>> {
    return File("input.txt")
            .readLines()
            .map { it.split(" ") }          
}

fun solve(moves: List<List<String>>) : Int {
    val directions = mapOf("forward" to Triple(1, 1, 0), 
                            "down" to Triple(0, 0, 1), 
                            "up" to Triple(0, 0, -1))

    var posX = 0
    var posY = 0
    var aim = 0
    for (move in moves) {
        val direction = directions[move[0]]!!
        val steps = move[1].toInt()

        posX += direction.first * steps
        posY += direction.second * aim * steps
        aim += direction.third * steps
    }
    return posX * posY
}

fun main() {
    val moves = readInput()
    val ans = solve(moves)

    println(ans)
}
