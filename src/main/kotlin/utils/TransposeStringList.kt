package utils

/**
 * Given a list of strings, that are all equally long, return the transpose list of strings.
 * @param square A list of equally long strings.
 * @return A list of strings, where each string corresponds to a column in the input.
 */
fun transposeStringList(square: List<String>): List<String> {
    val result = mutableListOf<String>()
    val len = square.maxBy { it.length }.length

    for (i in 0 ..< len) {
        var str = ""
        //TODO?~ There has to be a more functional way of saying this...
        for (j in square.indices) {
            val currentLine = square[j]
            str += currentLine[i]
        }
        result.add(str)
    }

    return result
}
