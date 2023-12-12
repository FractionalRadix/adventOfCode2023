package com.cormontia.adventOfCode.year2023

import utils.parseListOfIntegers
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day06Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay06.txt""")
            .readLines()
            .filter { it.isNotBlank() }

        // Part 1.
        val timesString = input[0].dropWhile { it != ':' }.drop(1)
        println("Times are: $timesString")
        val distancesString = input[1].dropWhile { it != ':' }.drop(1)
        println("Distances are: $distancesString")

        val times = parseListOfIntegers(timesString)
        println(times)
        val distances = parseListOfIntegers(distancesString)
        println(distances)

        val product = solvePart1(times, distances)
        println("The product of the number of winning options is $product.")

        // Part 2.
        val timeString = input[0].dropWhile { it != ':' }.drop(1).filter { !it.isWhitespace() }
        val distanceString = input[1].dropWhile { it != ':' }.drop(1).filter { !it.isWhitespace() }
        println("Single time: $timeString")
        println("Single distance: $distanceString")
        val time = timeString.toLong()
        println("Time as long: $time")
        val distance = distanceString.toLong()
        println("Distance as long: $distance")

        val answer = countOptions(time, distance)
        println("Nr of options: $answer")
    }

    private fun solvePart1(times: List<Int>, distances: List<Int>): Int {
        var product = 1
        for (i in times.indices) {
            val time = times[i]
            val targetDistance = distances[i]
            val nrOfOptions = countOptions(time, targetDistance)
            println("Nr of options: $nrOfOptions")
            product *= nrOfOptions
        }
        return product
    }

    private fun countOptions(time: Int, targetDistance: Int): Int {
        var count = 0
        for (i in 0 .. time) {
            val speed = i
            val remainingTime = time - i
            val distance = speed * remainingTime
            if (distance > targetDistance) {
                count++
            }
        }
        return count
    }

    private fun countOptions(time: Long, targetDistance: Long): Long {
        var count = 0L
        for (i in 0 .. time) {
            val speed = i
            val remainingTime = time - i
            val distance = speed * remainingTime
            if (distance > targetDistance) {
                count++
            }
        }
        return count
    }

}