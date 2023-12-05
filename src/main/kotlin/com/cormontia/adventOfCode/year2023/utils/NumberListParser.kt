package com.cormontia.adventOfCode.year2023.utils

fun parseListOfInts(str: String): List<Int> {
    val numbers = str.split(" ")
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

fun parseListOfLongs(str: String): List<Long> {
    val numbers = str.split(" ")
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

class NumberListParser {
}