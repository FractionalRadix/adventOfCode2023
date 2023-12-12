package utils

fun parseListOfIntegers(str: String, separator: String = " "): List<Int> {
    val numbers = str.split(separator)
    val numbersList = mutableListOf<Int>()
    numbers.forEach {
            numString ->
        if (numString.isNotEmpty() && numString.isNotBlank()) {
            val number = numString.toInt()
            numbersList.add(number)
        }
    }
    return numbersList
}

fun parseListOfLongs(str: String, separator: String = " "): List<Long> {
    val numbers = str.split(separator)
    val numbersList = mutableListOf<Long>()
    numbers.forEach {
            numString ->
        if (numString.isNotEmpty() && numString.isNotBlank()) {
            val number = numString.toLong()
            numbersList.add(number)
        }
    }
    return numbersList
}
