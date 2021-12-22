import java.io.File
import kotlin.math.*

fun readInput() : Reactor {
    val input = File("input.txt")
        .readLines()
        .map { it.split(",") }

    val opRegex = """(\w+)""".toRegex()
    val cubeRegex = """(-?\d+)..(-?\d+)""".toRegex()

    val steps = mutableListOf<Step>()
    input.forEach {
        val op = opRegex.find(it[0])?.groupValues?.get(0).toString()
        val (xMin, xMax) = cubeRegex.find(it[0])?.destructured!!
        val (yMin, yMax) = cubeRegex.find(it[1])?.destructured!!
        val (zMin, zMax) = cubeRegex.find(it[2])?.destructured!!

        val cube = Cuboid(xMin.toInt(), xMax.toInt(), yMin.toInt(), yMax.toInt(), zMin.toInt(), zMax.toInt())
        steps.add(Step(op, cube))
    }
    return Reactor(steps)
}

data class Step(val op : String, val cuboid : Cuboid)

data class Cuboid (var xMin : Int, var xMax : Int,
                   var yMin : Int, var yMax : Int,
                   var zMin : Int, var zMax : Int) {

    fun difference(otherCuboid : Cuboid) : List<Cuboid> {
        val result = mutableListOf<Cuboid>()

        if (!intersect(otherCuboid)) return mutableListOf(this)

        result += calcDifferenceInX(otherCuboid)
        result += calcDifferenceInY(otherCuboid)
        result += calcDifferenceInZ(otherCuboid)

        return result.toList()
    }

    private fun intersect(otherCuboid: Cuboid) : Boolean {
        return min(xMax, otherCuboid.xMax) >= max(xMin, otherCuboid.xMin) &&
                min(yMax, otherCuboid.yMax) >= max(yMin, otherCuboid.yMin) &&
                min(zMax, otherCuboid.zMax) >= max(zMin, otherCuboid.zMin)
    }

    private fun calcDifferenceInX(otherCuboid: Cuboid) : MutableList<Cuboid> {
        val result = mutableListOf<Cuboid>()
        if (otherCuboid.xMin in xMin + 1..xMax) {
            result.add(Cuboid(xMin, otherCuboid.xMin - 1, yMin, yMax, zMin, zMax))
            xMin = otherCuboid.xMin
        }

        if (otherCuboid.xMax in xMin..xMax - 1) {
            result.add(Cuboid(otherCuboid.xMax + 1, xMax, yMin, yMax, zMin, zMax))
            xMax = otherCuboid.xMax
        }
        return result
    }

    private fun calcDifferenceInY(otherCuboid: Cuboid) : MutableList<Cuboid> {
        val result = mutableListOf<Cuboid>()
        if (otherCuboid.yMin in yMin + 1..yMax) {
            result.add(Cuboid(xMin, xMax, yMin, otherCuboid.yMin - 1, zMin, zMax))
            yMin = otherCuboid.yMin
        }

        if (otherCuboid.yMax in yMin..yMax - 1) {
            result.add(Cuboid(xMin, xMax, otherCuboid.yMax + 1, yMax, zMin, zMax))
            yMax = otherCuboid.yMax
        }
        return result
    }

    private fun calcDifferenceInZ(otherCuboid: Cuboid) : MutableList<Cuboid> {
        val result = mutableListOf<Cuboid>()

        if (otherCuboid.zMin in zMin + 1..zMax) {
            result.add(Cuboid(xMin, xMax, yMin, yMax, zMin, otherCuboid.zMin - 1))
            zMin = otherCuboid.zMin
        }

        if (otherCuboid.zMax in zMin..zMax - 1) {
            result.add(Cuboid(xMin, xMax, yMin, yMax, otherCuboid.zMax + 1, zMax))
            zMax = otherCuboid.zMax
        }
        return result
    }

    fun volume() : Long {
        return (xMax - xMin + 1L) *
                (yMax - yMin + 1L) *
                (zMax - zMin + 1L)
    }

}

class Reactor(private val steps : List<Step>) {

    fun onCubesAfterSteps() : Long {
        var onCuboids = mutableSetOf<Cuboid>()

        for (step in steps) {
            val newOnCuboids = calcNewOnCuboids(step.cuboid, onCuboids)

            if (step.op == "on") {
                newOnCuboids.add(step.cuboid)
            }
            onCuboids = newOnCuboids
        }
        return calcVolume(onCuboids.toList())
    }

    private fun calcNewOnCuboids(stepCuboid: Cuboid, onCuboids : MutableSet<Cuboid>) : MutableSet<Cuboid> {
        val result = mutableSetOf<Cuboid>()
        for (cuboid in onCuboids) {
            val diffList = cuboid.difference(stepCuboid)
            for (diff in diffList) {
                result.add(diff)
            }
        }
        return result
    }

    private fun calcVolume(cuboids : List<Cuboid>) : Long {
        var result = 0L
        for (cuboid in cuboids) {
            result += cuboid.volume()
        }
        return result
    }

}

fun solve(reactor : Reactor) : Long {
    return reactor.onCubesAfterSteps()
}

fun main() {
    val reactor = readInput()
    val ans = solve(reactor)

    println(ans)
}
