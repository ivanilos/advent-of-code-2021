import java.io.File

fun readInput() : Image {
    val input = File("input.txt")
        .readText()
        .split("""\r\n\r\n""".toRegex())

    val enhancementAlgo = input[0]
    val pixels = input[1].split("\r\n").filter { it.isNotEmpty() }

    return Image(enhancementAlgo, pixels)
}

const val ALGO_STEPS = 2

class Image(enhancementAlgo : String, pixels : List<String>) {

    companion object {

        const val LIGHT_PIXEL = '#'

    }

    private var enhancementAlgo: String
    private var pixels : List<String>
    private var interestingPixels = mutableMapOf<Pair<Int, Int>, Char>()
    private var rows = 0
    private var cols = 0
    private var pixelsOutsideInterest = '.'
    private var stepsApplied = 0

    init {
        this.enhancementAlgo = enhancementAlgo
        this.pixels = pixels
        rows = pixels.size
        cols = pixels[0].length

        for (i in -ALGO_STEPS..(rows + ALGO_STEPS)) {
            for (j in -ALGO_STEPS..(cols + ALGO_STEPS)) {
                val pos = Pair(i, j)

                if (isIn(pos)) {
                    interestingPixels[pos] = pixels[i][j]
                } else {
                    interestingPixels[pos] = pixelsOutsideInterest
                }
            }
        }
    }

    fun applyAlgoStep() {
        stepsApplied++

        val newInterestingPixels = mutableMapOf<Pair<Int, Int>, Char>()

        for (pos in interestingPixels.keys) {
            val pixelValue = calcValue(pos)
            newInterestingPixels[pos] = enhancementAlgo[pixelValue]
        }
        
        interestingPixels = newInterestingPixels
        pixelsOutsideInterest = if (pixelsOutsideInterest == LIGHT_PIXEL) enhancementAlgo.last() else enhancementAlgo.first()
    }

    private fun calcValue(pos : Pair<Int, Int>) : Int {
        var result = 0
        for (deltaX in -1..1) {
            for (deltaY in -1..1) {
                val posChecked = Pair(pos.first + deltaX, pos.second + deltaY)

                if (posChecked in interestingPixels) {
                    result = 2 * result + (if (interestingPixels[posChecked] == LIGHT_PIXEL) 1 else 0)
                } else {
                    result = 2 * result + (if (pixelsOutsideInterest == LIGHT_PIXEL) 1 else 0)
                }
            }
        }
        return result
    }

    fun countLightPixels() : Int {
        if (pixelsOutsideInterest == LIGHT_PIXEL) throw Exception("Infinite pixels are light")

        var result = 0
        for (pixel in interestingPixels.values) {
            result += if (pixel == LIGHT_PIXEL) 1 else 0
        }
        return result
    }

    private fun isIn(pos : Pair<Int, Int>) : Boolean {
        return pos.first in 0 until rows && pos.second in 0 until cols
    }
}

fun solve(image : Image) : Int {
    for (step in 1..ALGO_STEPS) {
        image.applyAlgoStep()
    }
    return image.countLightPixels()
}

fun main() {
    val image = readInput()
    val ans = solve(image)

    println(ans)
}
