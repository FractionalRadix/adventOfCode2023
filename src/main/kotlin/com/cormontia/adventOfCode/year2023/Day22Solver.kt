package com.cormontia.adventOfCode.year2023

import utils.parseListOfIntegers
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day22Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay22_sample1.txt""")
            .readLines()

        val bricks = input.map {
            val parts = it.split("~")
            val part1 = parseListOfIntegers(parts[0], ",")
            val cube1 = Cube(part1[0], part1[1], part1[2])
            val part2 = parseListOfIntegers(parts[1], ",")
            val cube2 = Cube(part2[0], part2[1], part2[2])
            Brick(cube1, cube2)
        }

        for (brick in bricks) {
            brick.print()
        }
    }

    data class Cube(val x: Int, val y:Int, val z:Int) {
        fun print() {
            print("($x,$y,$z)")
        }
    }

    data class Brick(val start: Cube, val end: Cube) {
        fun print() {
            println()
            start.print()
            print("~")
            end.print()
        }
    }

}