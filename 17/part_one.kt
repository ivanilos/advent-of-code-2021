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


    fun calcMaxYWithHit() : Int {
        var result = 0
        for (initialVx in 1..maxiX) {
            for (initialVy in 0..10 * (maxiY - miniY)) { // heuristic initialVy values
                result = maxOf(result, maxHeightIfHit(initialVx, initialVy))
            }
        }
        return result
    }

    private fun maxHeightIfHit(initialVx : Int, initialVy : Int) : Int {
        var vx = initialVx
        var vy = initialVy
        var curX = 0
        var curY = 0
        var result = 0
        var throwMaxY = 0

        while(curY >= miniY) {
            curX += vx
            curY += vy
            throwMaxY = maxOf(throwMaxY, curY)

            vx = maxOf(0, vx - 1)
            vy -= 1

            if (isHit(curX, curY)) {
                result = maxOf(result, throwMaxY)
            }
        }
        return result
    }

    private fun isHit(curX : Int, curY : Int) : Boolean {
        return curX in miniX..maxiX && curY in miniY..maxiY
    }

}

fun solve(target : Target) : Int {
    return target.calcMaxYWithHit()
}

fun main() {
    val target = readInput()
    val ans = solve(target)

    println(ans)
}
