package com.cormontia.adventOfCode.year2023

import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day02Solver {
    fun solve() {
        val firstInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay02.txt""")
            .readLines()
            .filter { str -> str.isNotEmpty() }
        val games = parseDay02(firstInputAsStrings)

        // Solve the first part.
        var sumPart1 = 0
        games.forEach {
                game -> if (game.isPossible(maxRed = 12, maxGreen = 13, maxBlue = 14)) {
            sumPart1 += game.gameNr
        }
        }
        println("The sum of possible games is: $sumPart1")

        // Solve the second part.
        var sumPart2 = 0
        games.forEach {
                game -> sumPart2 += game.power()
        }
        println("The sum of powers of the minimal sets of cubes is: $sumPart2")
    }

    fun parseDay02(inputLines: List<String>): List<Game> {
        val games = mutableListOf<Game>()
        for (i in 1 .. inputLines.size) {
            val game = Game(i)
            val curStr = inputLines[i - 1]
            var stripped = curStr.dropWhile { ch -> ch != ':' }
            stripped = stripped.substring(1)
            val draws = stripped.split(';')
            for (draw in draws) {
                game.addDraw(draw)
            }
            games.add(game)
        }
        return games
    }


    class Game(val gameNr: Int) {
        data class Draw(val red: Int, val green: Int, val blue: Int)

        private val draws = mutableListOf<Draw>()

        fun addDraw(draw: String) {
            val parsedDraw = parseDraw(draw)
            draws.add(parsedDraw)
        }

        private fun parseDraw(draw: String): Draw {
            val cubes = draw
                .split(',')
            var red = 0
            var green = 0
            var blue = 0
            // All the parsing that's left to do, is to determine the amount and the color...
            for (cube in cubes) {
                val parsed = cube.trim().split(' ')
                val nrOfCubes = parsed[0].toInt()
                val color = parsed[1]

                if (color == "red") {
                    red = nrOfCubes
                }

                if (color == "green") {
                    green = nrOfCubes
                }

                if (color == "blue") {
                    blue = nrOfCubes
                }
            }
            return Draw(red, green, blue)
        }

        fun isPossible(maxRed: Int, maxGreen: Int, maxBlue: Int): Boolean {
            val redPossible = draws.none { draw -> draw.red > maxRed }
            val greenPossible = draws.none { draw -> draw.green > maxGreen }
            val bluePossible = draws.none { draw -> draw.blue > maxBlue }
            return (redPossible && greenPossible && bluePossible)
        }

        fun power(): Int {
            val minimumRedRequired = draws.maxOfOrNull { draw -> draw.red } ?: 0
            val minimumGreenRequired = draws.maxOfOrNull { draw -> draw.green } ?: 0
            val minimumBlueRequired = draws.maxOfOrNull { draw -> draw.blue } ?: 0

            return minimumRedRequired * minimumGreenRequired * minimumBlueRequired
        }

        fun printGame() {
            println()
            print("Game $gameNr: ")
            draws.forEach {
                    d -> print("${d.red} red, ${d.green} green, ${d.blue} blue;")
            }
            println()
        }
    }

}