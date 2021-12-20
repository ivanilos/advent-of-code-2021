import java.io.File

fun readInput() : List<SnailFishNumber> {
    val input = File("input.txt")
        .readLines()

    return input.map { SnailFishNumber(it) }
}

class SnailFishNumber() {

    companion object {

        const val LEFT_DELIMITER = '['
        const val RIGHT_DELIMITER = ']'
        const val SEPARATOR = ','
        const val DEPTH_TO_EXPLODE = 4
        const val LITERAL_TO_SPLIT = 10
        const val LEFT_MAGNITUDE_MULTIPLIER = 3
        const val RIGHT_MAGNITUDE_MULTIPLIER = 2

    }

    private var left : SnailFishNumber? = null
    private var right : SnailFishNumber? = null
    private var parent : SnailFishNumber? = null
    private var literalValue = 0
    private var isLiteralValue = false

    constructor(str : String) : this() {
        if (str.length == 1) {
            literalValue = str.toInt()
            isLiteralValue = true
        } else {
            val (leftNumber, rightNumber) = parseNumber(str)
            left = leftNumber
            right = rightNumber

            leftNumber.parent = this
            rightNumber.parent = this
            isLiteralValue = false
        }
    }

    constructor(left : SnailFishNumber, right : SnailFishNumber) : this() {
        this.left = left
        this.right = right
        this.parent = left.parent
        left.parent = this
        right.parent = this
        this.isLiteralValue = false
    }

    constructor(value : Int, par : SnailFishNumber?) : this() {
        this.isLiteralValue = true
        this.literalValue = value
        this.parent = par
    }

    operator fun plus(number : SnailFishNumber) : SnailFishNumber {
        val result = SnailFishNumber(this, number)
        result.reduce()

        return result
    }

    fun magnitude() : Int {
        if (isLiteralValue) return literalValue

        val leftMagnitude = LEFT_MAGNITUDE_MULTIPLIER * left?.magnitude()!!
        val rightMagnitude = RIGHT_MAGNITUDE_MULTIPLIER * right?.magnitude()!!

        return leftMagnitude + rightMagnitude
    }

    fun printNumber() {
        if (isLiteralValue) {
            print(literalValue)
        } else {
            print(LEFT_DELIMITER)
            left?.printNumber()
            print(SEPARATOR)
            right?.printNumber()
            print(RIGHT_DELIMITER)
        }
    }

    private fun parseNumber(str : String) : Pair<SnailFishNumber, SnailFishNumber> {
        var nesting = 0
        for ((idx, ch) in str.withIndex()) {
            if (ch == LEFT_DELIMITER) {
                nesting++
            } else if (ch == RIGHT_DELIMITER) {
                nesting--
            } else if (ch == SEPARATOR && nesting == 1) {
                val leftNumber = SnailFishNumber(str.slice(1 until idx))
                val rightNumber = SnailFishNumber(str.slice(idx + 1 until str.length - 1))
                return Pair(leftNumber, rightNumber)
            }
        }
        throw Exception("Error parsing number $str")
    }

    private fun reduce() {
        while(explode() || split()) { continue }
    }

    private fun explode(depth: Int = 0) : Boolean {
        if (isLiteralValue) return false

        if (depth == DEPTH_TO_EXPLODE) {
            val closestLiteralToLeft = findClosestLiteralToLeft()
            val closestLiteralToRight = findClosestLiteralToRight()

            if (closestLiteralToLeft != null) closestLiteralToLeft.literalValue += left!!.literalValue;
            if (closestLiteralToRight != null) closestLiteralToRight.literalValue += right!!.literalValue

            left = null
            right = null
            isLiteralValue = true
            literalValue = 0

            return true;
        } else {
            if (left != null && left!!.explode(depth + 1)) { return true; }
            else if (right != null && right!!.explode(depth + 1)) { return true; }
        }
        return false
    }

    private fun split() : Boolean {
        if (isLiteralValue && literalValue >=  LITERAL_TO_SPLIT) {
            isLiteralValue = false
            left = SnailFishNumber(literalValue / 2, this)
            right = SnailFishNumber((literalValue + 1) / 2, this)

            return true
        } else {
            if (left != null && left!!.split()) { return true }
            if (right != null && right!!.split()) { return true }
        }
        return false
    }

    private fun findClosestLiteralToLeft() : SnailFishNumber? {
        var par = parent
        var cur = this

        while(par != null && par.left == cur) {
            cur = par
            par = par.parent
        }

        if (par != null) {
            cur = par.left!!
            while (!cur.isLiteralValue) {
                cur = cur.right!!
            }
        } else {
            return null
        }
        return cur;
    }

    private fun findClosestLiteralToRight() : SnailFishNumber? {
        var par = parent
        var cur = this

        while(par != null && par.right == cur) {
            cur = par
            par = par.parent
        }

        if (par != null) {
            cur = par.right!!
            while (!cur.isLiteralValue) {
                cur = cur.left!!
            }
        } else {
            return null
        }
        return cur;
    }

}

fun solve(snailFishNumbers : List<SnailFishNumber>) : Int {
    val result = snailFishNumbers.reduce{ acc, num -> acc + num }
    return result.magnitude()
}

fun main() {
    val snailFishNumbers = readInput()
    val ans = solve(snailFishNumbers)

    println(ans)
}
