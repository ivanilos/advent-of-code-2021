import java.io.File

fun readInput() : Target {
    val input = File("input.txt")
        .readText()

    val xRegex = """x=(-?\d+)..(-?\d+)""".toRegex()
    val yRegex = """y=(-?\d+)..(-?\d+)""".toRegex()

    val (miniX, maxiX) = xRegex.find(input)?.destructured!!
    val (miniY, maxiY) = yRegex.find(input)?.destructured!!

    return Target(miniX.toInt(), maxiX.toInt(), miniY.toInt(), maxiY.toInt())
}

class Target(private val miniX: Int, private val maxiX : Int,
             private val miniY : Int, private val maxiY : Int) {


    fun calcVelocitiesWithHit() : List<Pair<Int, Int>> {
        var result = mutableListOf<Pair<Int, Int>>()
        for (initialVx in 1..maxiX) {
            val windowY = 10 * (maxiY - miniY)
            for (initialVy in -windowY..windowY) { // heuristic initialVy values
                if (hitAfterSomeTime(initialVx, initialVy)) {
                    result.add(Pair(initialVx, initialVy))
                }
            }
        }
        return result.toList()
    }

    private fun hitAfterSomeTime(initialVx : Int, initialVy : Int) : Boolean {
        var vx = initialVx
        var vy = initialVy
        var curX = 0
        var curY = 0

        while(curY >= miniY) {
            curX += vx
            curY += vy

            vx = maxOf(0, vx - 1)
            vy -= 1

            if (isHit(curX, curY)) {
                return true
            }
        }
        return false
    }

    private fun isHit(curX : Int, curY : Int) : Boolean {
        return curX in miniX..maxiX && curY in miniY..maxiY
    }

}

fun solve(target : Target) : Int {
    return target.calcVelocitiesWithHit().size
}

fun main() {
    val target = readInput()
    val ans = solve(target)

    println(ans)
}
