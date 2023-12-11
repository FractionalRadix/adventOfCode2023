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

        val result1 = solve_part1(inputLists)
        println("Sum of the next sequence values is: $result1")
    }

    fun solve_part1(inputLists: List<List<Long>>): Long {
        var sum = 0L
        for (list in inputLists) {
            sum += determineNextValue(list)
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
}