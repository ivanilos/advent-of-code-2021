import java.io.File

const val BIT_SIZE = 4
const val INPUT_RADIX = 16

fun readInput() : BITS {
    val input = File("input.txt")
        .readText()
        .replace("\r\n", "")


    val bitsArray = mutableListOf<String>()
    for (ch in input) {
        val decimal = Integer.parseInt(ch.toString(), INPUT_RADIX)
        val binary = Integer.toBinaryString(decimal).padStart(BIT_SIZE, '0')
        bitsArray.add(binary)
    }
    return BITS(bitsArray.joinToString(separator = ""))
}

class BITS(data : String) {

    companion object {

        const val VERSION_SIZE = 3
        const val TYPE_ID_SIZE = 3
        const val GROUP_PREFIX_SIZE = 1
        const val GROUP_SIZE = 4
        const val LENGTH_TYPE_ID_SIZE = 1

        const val LITERAL_VALUE_TYPE_ID = 4

        const val LAST_GROUP_PREFIX = 0

        const val LENGTH_TYPE_ID_FOR_LENGTH = 0
        const val LENGTH_TYPE_ID_FOR_QUANTITY = 1

        const val SUB_PACKETS_NUM_SIZE = 11
        const val SUB_PACKETS_BITS_SIZE = 15

    }

    private val data : String
    private val packet : Packet
    private var nextPos : Int

    init {
        this.data = data
        this.nextPos = 0
        this.packet = parse()
    }

    private fun parse() : Packet {
        val curPacket = Packet()

        readVersion(curPacket)
        readTypeId(curPacket)

        if (curPacket.typeId == LITERAL_VALUE_TYPE_ID) {
            readLiteralValue(curPacket)
        } else {
            readSubPackets(curPacket)
        }
        return curPacket
    }

    private fun readVersion(curPacket: Packet) {
        val version = data.substring(nextPos, nextPos + VERSION_SIZE).toInt(2)
        curPacket.version = version

        nextPos += VERSION_SIZE
    }

    private fun readTypeId(curPacket : Packet) {
        val typeId = data.substring(nextPos, nextPos + VERSION_SIZE).toInt(2)
        curPacket.typeId = typeId

        nextPos += TYPE_ID_SIZE
    }

    private fun readLiteralValue(curPacket : Packet) {
        var isLastGroup = false
        val valueList = mutableListOf<String>()
        while(!isLastGroup) {
            isLastGroup = data.substring(nextPos, nextPos + GROUP_PREFIX_SIZE).toInt(2) == LAST_GROUP_PREFIX
            nextPos += GROUP_PREFIX_SIZE

            val groupValue = data.substring(nextPos, nextPos + GROUP_SIZE)
            nextPos += GROUP_SIZE
            valueList.add(groupValue)
        }

        curPacket.value = valueList.joinToString("").toLong(2)
    }

    private fun readSubPackets(curPacket : Packet) {
        val lengthTypeId = data.substring(nextPos, nextPos + LENGTH_TYPE_ID_SIZE).toInt(2)
        nextPos += LENGTH_TYPE_ID_SIZE

        if (lengthTypeId == LENGTH_TYPE_ID_FOR_LENGTH) {
            val subPacketsSize = data.substring(nextPos, nextPos + SUB_PACKETS_BITS_SIZE).toInt(2)
            nextPos += SUB_PACKETS_BITS_SIZE

            val originalNextPos = nextPos
            while (nextPos - originalNextPos < subPacketsSize) {
                curPacket.subPackets.add(parse())
            }
        } else if (lengthTypeId == LENGTH_TYPE_ID_FOR_QUANTITY) {
            val subPacketsQuantity = data.substring(nextPos, nextPos + SUB_PACKETS_NUM_SIZE).toInt(2)
            nextPos += SUB_PACKETS_NUM_SIZE

            for (i in 1..subPacketsQuantity) {
                curPacket.subPackets.add(parse())
            }
        }
    }

    fun eval() : Long {
        return packet.eval()
    }

}

class Packet {

    companion object {

        const val TYPE_ID_SUM = 0
        const val TYPE_ID_PRODUCT = 1
        const val TYPE_ID_MIN = 2
        const val TYPE_ID_MAX = 3
        const val TYPE_ID_LITERAL = 4
        const val TYPE_ID_GREATER = 5
        const val TYPE_ID_LESS = 6
        const val TYPE_ID_EQUAL = 7

    }

    var version = 0
    var typeId = 0
    var value = 0L
    val subPackets = mutableListOf<Packet>()

    fun eval() : Long {

        when (typeId) {
            TYPE_ID_SUM -> return subPackets.sumOf { it.eval() }
            TYPE_ID_PRODUCT -> return subPackets.map { it.eval() }.reduce { acc, value -> acc * value }
            TYPE_ID_MIN -> return subPackets.minOf { it.eval() }
            TYPE_ID_MAX -> return subPackets.maxOf { it.eval() }
            TYPE_ID_LITERAL -> return value
            TYPE_ID_GREATER -> return if (subPackets[0].eval() > subPackets[1].eval()) 1 else 0
            TYPE_ID_LESS -> return if (subPackets[0].eval() < subPackets[1].eval()) 1 else 0
            TYPE_ID_EQUAL -> return if (subPackets[0].eval() == subPackets[1].eval()) 1 else 0
        }
        throw Exception("Invalid typeId")
    }

}

fun solve(bits : BITS) : Long {
    return bits.eval()
}

fun main() {
    val bits = readInput()
    val ans = solve(bits)

    println(ans)
}
