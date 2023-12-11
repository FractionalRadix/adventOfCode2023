package com.cormontia.adventOfCode.year2023

import com.cormontia.adventOfCode.year2023.utils.parseListOfLongs
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day09Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay09_sample1.txt""")
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

    fun determineNextValue(list: List<Long>): Long {
        var differences = list
        var sum = 0L
        do{
            differences = differences.zipWithNext().map { it.second - it.first }
        } while (differences.any { it != 0L })

        TODO()
    }
}