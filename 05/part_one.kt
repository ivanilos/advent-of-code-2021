import java.io.File
import kotlin.math.*


fun readInput() : List<Line> {
    val inputLines = File("input.txt")
            .readLines()
            .map { it.split("\n") }

    val intRegex = """\d+""".toRegex()
    val lines = inputLines.map{ inputLine -> Line(intRegex.findAll(inputLine[0])
            .map{ it.value.toInt() }
            .toList()) }
    return lines
}

data class Pt(val x : Int, val y : Int)

class Line(points : List<Int>) {

    private val a : Pt
    private val b : Pt

    init {
        a = Pt(points[0], points[1])
        b = Pt(points[2], points[3])
    }

    fun isVertical() : Boolean {
        return a.x == b.x
    }

    fun isHorizontal() : Boolean {
        return a.y == b.y
    }

    fun touchPointsIfAxisAligned() : HashSet<Pt> {
        val result = HashSet<Pt>()

        if (isVertical() || isHorizontal()) {
            val deltaX = (b.x - a.x).sign
            val deltaY = (b.y - a.y).sign

            var curX = a.x
            var curY = a.y

            while(!(curX == b.x && curY == b.y)) {
                result.add(Pt(curX, curY))
                curX += deltaX
                curY += deltaY
            }
            result.add(Pt(b.x, b.y))
        }
        return result
    }
}

fun updatePointsFreq(pointsFreq : HashMap<Pt, Int>, line : Line) {
    val pointsInLine = line.touchPointsIfAxisAligned()

    for (point in pointsInLine) {
        val curPointFreq = pointsFreq.getOrDefault(point, 0)
        pointsFreq[point] = curPointFreq + 1
    }
}

fun solve(lines : List<Line>) : Int {
    val pointsFreq = hashMapOf<Pt, Int>()

    for (line in lines) {
        if (line.isHorizontal() || line.isVertical()) {
            updatePointsFreq(pointsFreq, line)
        }
    }

    return pointsFreq.filter{ it.value > 1 }.size
}

fun main() {
    val lines = readInput()
    val ans = solve(lines)

    println(ans)
}
