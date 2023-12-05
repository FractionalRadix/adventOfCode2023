package com.cormontia.adventOfCode.year2023.utils

/**
 * Given a string, find the (positive natural) numbers in the string, including their start- and end-positions.
 * @param str A String that may contain one or more positive natural numbers.
 * @return A list of start- and end-indices, where each pair corresponds to a number inside the string.
 */
fun findNumbersInString(str: String): List<Pair<Int,Int>> {
    val numberSubstrings = mutableListOf<Pair<Int,Int>>()

    var parsingNumber = false
    var startPos: Int? = 0
    var i = 0
    while (i < str.length) {
        val ch = str[i]
        if (ch.isDigit() && !parsingNumber) {
            parsingNumber = true
            startPos = i
        }
        if (!ch.isDigit()) {
            if (parsingNumber) {
                val endPos = i - 1
                numberSubstrings.add(Pair(startPos!!, endPos))
                startPos = null
            }
            parsingNumber = false
        }
        i++
    }
    if (parsingNumber) {
        val endPos = str.length - 1
        numberSubstrings.add(Pair(startPos!!, endPos))
    }

    return numberSubstrings
}