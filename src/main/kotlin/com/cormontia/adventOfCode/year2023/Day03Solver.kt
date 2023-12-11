package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.buildGridMap
import utils.findNumbersInString
import utils.surroundingCoordinates
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day03Solver {
    fun solve() {
        val firstInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay03.txt""")
            .readLines()
            .filter { str -> str.isNotEmpty() }

        val solutionPart1 = solveDay03Part1(firstInputAsStrings)
        println("The sum of part numbers is $solutionPart1 .")

        val solutionPart2 = solveDay03Part2(firstInputAsStrings)
        println("The sum of gear ratios is $solutionPart2 .")
    }

    fun solveDay03Part2(inputStrings: List<String>): Int {

        data class Component(
            val value: Int,
            val startOfNumber: Coor,
            val endOfNumber: Coor,
            val environment: List<Coor>
        )
        val componentsNearGear = mutableListOf<Component>()

        val map = buildGridMap(inputStrings)

        fun containsStar(environment: List<Coor>) =
            environment
                .filter { key -> map.containsKey(key) }
                .any { key -> map[key] == '*' }

        for (rowNr in 0L..< inputStrings.size) {
            val row = inputStrings[rowNr.toInt()]
            val numbers = findNumbersInString(row)
            numbers.forEach {
                val startOfNumber = Coor(rowNr, it.first)
                val endOfNumber = Coor(rowNr, it.second)
                val environment = surroundingCoordinates(startOfNumber, endOfNumber)
                if (containsStar(environment)) {
                    val number = row
                        .substring(IntRange(it.first.toInt(), it.second.toInt()))
                        .toInt()
                    componentsNearGear.add(Component(number, startOfNumber, endOfNumber, environment))
                }
            }
        }

        // For every gear (star symbol), determine if it is in exactly TWO environments.
        var sum = 0
        map.forEach {
            if (it.value == '*') {
                val components = componentsNearGear
                    .filter { comp -> comp.environment.contains(it.key) }
                if (components.size == 2) {
                    val gearRatio = components[0].value * components[1].value
                    sum += gearRatio
                }
            }
        }

        return sum
    }

    private fun solveDay03Part1(
        inputStrings: List<String>,
        //map: MutableMap<com.cormontia.adventOfCode.year2023.utils.Coor, Char>,
    ): Int {
        val map = buildGridMap(inputStrings)
        var sum = 0
        for (rowNr in inputStrings.indices) {
            val row = inputStrings[rowNr]
            val numbers = findNumbersInString(row)

            numbers.forEach { it ->
                val startOfNumber = Coor(rowNr.toLong(), it.first)
                val endOfNumber = Coor(rowNr.toLong(), it.second)
                val neighbours = surroundingCoordinates(startOfNumber, endOfNumber)
                val touched = neighbours
                    .filter { coor -> map.containsKey(coor) }
                    .any { coor -> map[coor] != '.' }
                if (touched) {
                    val number = row.substring(IntRange(it.first.toInt(), it.second.toInt()))
                    val result = number.toInt()
                    sum += result
                }
            }
        }
        return sum
    }

}