package com.cormontia.adventOfCode.year2023

import utils.parseListOfLongs
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day09Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay09.txt""")
            .readLines()
            .filter { it.isNotBlank() }

        val inputLists = input.map { parseListOfLongs(it) }

        val result1 = solvePart1(inputLists)
        println("Sum of the next sequence values is: $result1")
        val result2 = solvePart2(inputLists) // NOT: -2
        println("Sum of the preceding sequence values is: $result2")
    }

    private fun solvePart1(inputLists: List<List<Long>>): Long {
        var sum = 0L
        for (list in inputLists) {
            sum += determineNextValue(list)
        }
        return sum
    }

    private fun solvePart2(inputLists: List<List<Long>>): Long {
        var sum = 0L
        for (list in inputLists.reversed()) {
            sum += determinePrevValue(list)
        }
        return sum
    }

    private fun determineNextValue(list: List<Long>): Long {
        val lastElements = mutableListOf<Long>()
        var differences = list
        do{
            lastElements.add(differences.last())
            differences = differences.zipWithNext().map { it.second - it.first }
        } while (differences.any { it != 0L })

        var next = 0L
        for (elt in lastElements.reversed()) {
            next += elt
        }
        return next
    }

    private fun determinePrevValue(list: List<Long>): Long {
        val firstElements = mutableListOf<Long>()
        var differences = list
        do{
            firstElements.add(differences.first())
            differences = differences.zipWithNext().map { it.second - it.first }
        } while (differences.any { it != 0L })

        var pred = 0L
        for (elt in firstElements.reversed()) {
            pred = elt - pred
        }
        return pred
    }
}