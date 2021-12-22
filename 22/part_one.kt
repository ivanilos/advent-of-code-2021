import java.io.File

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

data class Cuboid (val xMin : Int, val xMax : Int,
                   val yMin : Int, val yMax : Int,
                   val zMin : Int, val zMax : Int)

data class Cube (val x : Int, val y : Int, val z : Int)

class Reactor(private val steps : List<Step>) {

    companion object {

        const val MIN_VALID_COORDINATE = -50
        const val MAX_VALID_COORDINATE = 50

    }

    fun onCubesAfterSteps() : Int {
        val onCubes = mutableSetOf<Cube>()

        for (step in steps) {
            if (!isValidCuboid(step.cuboid)) continue

            for (x in step.cuboid.xMin..step.cuboid.xMax) {
                for (y in step.cuboid.yMin..step.cuboid.yMax) {
                    for (z in step.cuboid.zMin..step.cuboid.zMax) {
                        val cube = Cube(x, y, z)
                        if (step.op == "on") {
                            onCubes.add(cube)
                        } else {
                            onCubes.remove(cube)
                        }
                    }
                }
            }
        }
        return onCubes.size
    }

    private fun isValidCuboid(cuboid : Cuboid) : Boolean {
        return cuboid.xMin in MIN_VALID_COORDINATE..MAX_VALID_COORDINATE &&
                cuboid.xMax in MIN_VALID_COORDINATE..MAX_VALID_COORDINATE &&
                cuboid.yMin in MIN_VALID_COORDINATE..MAX_VALID_COORDINATE &&
                cuboid.yMax in MIN_VALID_COORDINATE..MAX_VALID_COORDINATE &&
                cuboid.zMin in MIN_VALID_COORDINATE..MAX_VALID_COORDINATE &&
                cuboid.zMin in MIN_VALID_COORDINATE..MAX_VALID_COORDINATE
    }
    
}

fun solve(reactor : Reactor) : Int {
    return reactor.onCubesAfterSteps()
}

fun main() {
    val reactor = readInput()
    val ans = solve(reactor)

    println(ans)
}
