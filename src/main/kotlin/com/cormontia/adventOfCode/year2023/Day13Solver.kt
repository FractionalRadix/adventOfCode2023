package com.cormontia.adventOfCode.year2023

import utils.transposeStringList
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

class Day13Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay13.txt""")
            .readLines()

        val maps = mutableListOf<Map>()
        var current = mutableListOf<String>()
        for (line in input) {
            if (line.isBlank()) {
                val map = Map(current)
                maps.add(map)
                current = mutableListOf<String>()
            } else {
                current.add(line)
            }
        }
        val map = Map(current)
        maps.add(map)

        //solvePart1(maps)

        solvePart2(maps) // 21186 is TOO LOW. // 31399 is TOO HIGH.
    }

    private fun solvePart2(maps: MutableList<Map>) {

        var horizontalSum = 0
        var verticalSum = 0
        for (map in maps) {

            println()
            println("NEW MAP.")

            val horiz = findSmudgeLine(map.list)
            horizontalSum += horiz.sum()

            val vertic = findSmudgeLine(transposeStringList(map.list))
            verticalSum += vertic.sum()

            if (horiz.size > 1) {
                println("Erratic (H)? $horiz")
                map.print()
            }
            if (vertic.size > 1) {
                println("Erratic (V)? $vertic")
                map.print()
            }
        }

        println()
        println("Horizontal sum: $horizontalSum")
        println("Vertical sum: $verticalSum")
        println("Result: ${100 * horizontalSum + verticalSum}")
    }

    /**
     * Given two strings of equal length, count in how many places they differ.
     * @param str1 A string.
     * @param str2 A string whose length is equal to str1.
     * @return The number of places in which the first input string differs from the second one.
     */
    private fun differences(str1: String, str2: String): Int {
        var differences = 0
        for (i in str1.indices) {
            if (str1[i] != str2[i]) {
                differences++
            }
        }
        return differences
    }


    fun findSmudgeLine(list: List<String>): List<Int> {

        // First, find all lines that differ in exactly one spot.
        var pairs = mutableListOf<Pair<Int,Int>>()
        for (i in list.indices) {
            for (j in i + 1 ..< list.size) {
                val numDiff = differences(list[i], list[j])
                if (numDiff == 1) {
                    pairs.add(Pair(i,j))
                }
            }
        }

        // For every pair, determine where the mirroring axis would be: exactly between them.
        // First, the difference between them should be an odd number.
        pairs = pairs.filter { isOdd(Math.abs(it.first - it.second)) }.toMutableList()

        println("Candidate pairs: $pairs.")

        // For these candidates, find the axis.
        // Then check if all OTHER lines mirror properly around that axis.
        val validAxes = mutableListOf<Int>()
        for (pair in pairs) {
            val axis = (pair.first + pair.second + 1)/2
            val axisValid = isValidAxis(axis, list, pair)
            if (axisValid) {
                println("Valid! $axis")
                validAxes.add(axis)
            }
        }

        println("Axes: $validAxes" )
        return (validAxes)
    }

    private fun isValidAxis(
        axis: Int,
        list: List<String>,
        pair: Pair<Int, Int>
    ): Boolean {
        var axisValid = true
        var d = 1
        while (axis - d >= 0 && axis - 1 + d < list.size) {
            val idx1 = axis - d
            val idx2 = axis - 1 + d
            if (idx1 == Math.min(pair.first, pair.second)) { // Skip the line that differs by one.
                d++
                continue
            }
            val diff = differences(list[idx1], list[idx2])
            if (diff != 0) {
                axisValid = false
            }
            d++
        }
        return axisValid
    }

    fun isOdd(n: Int): Boolean = (n % 2) == 1

    private fun solvePart1(maps: MutableList<Map>) {
        var horizontalSum = 0
        var verticalSum = 0
        for (m in maps) {
            println()
            m.print()
            println("Horizontal:")
            val horizontalValues = m.findHorizontalMirror(m.list)
            horizontalSum += horizontalValues.sum()
            println("Vertical:")
            val verticalValues = m.findVerticalMirror(m.list)
            verticalSum += verticalValues.sum()
        }
        println("Horizontal sum: $horizontalSum")
        println("Vertical sum: $verticalSum")
        println("Result: ${100 * horizontalSum + verticalSum}")
    }

    class Map(val list: MutableList<String>) {
        fun print() {
            println("MAP:")
            for (l in list) {
                println(l)
            }
        }

        fun findHorizontalMirror(list: List<String>): List<Int> {
            val result = mutableListOf<Int>()
            for (i in list.indices.drop(1)) {
                if (list[i] == list[i - 1]) {
                    if (confirmHorizontalMirroring(list,i-1, i)) {
                      println("Found: $i ${i+1}") // Note the off-by-one correction.
                        result.add(i)
                    }
                }
            }
            if (result.size > 1) {
                println("Warning! Multiple mirroring axes!")
            }
            return result
        }

        fun confirmHorizontalMirroring(list: List<String>, i: Int, j: Int): Boolean {
            var d = 1
            while (i - d >= 0 && j + d < list.size) {
                val strI = list[i - d]
                val strJ = list[j + d]
                if (strI != strJ) {
                    return false
                }
                d++
            }
            return true
        }

        fun findVerticalMirror(list: List<String>): List<Int> {
            val transposed = transposeStringList(list)
            return findHorizontalMirror(transposed)
        }
    }
}