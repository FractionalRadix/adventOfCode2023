package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.Direction
import utils.buildGridMap
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day17Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay17_sample1.txt""")
            .readLines()

        val map = buildGridMap(input)
            .mapValues { Node(it.value) }

        val maxRows = map.keys.maxBy { it.row }.row
        val maxCols = map.keys.maxBy { it.col }.col
        val destinationNode = Coor(maxRows, maxCols)

        val currentNodes = mutableListOf<Walker>()
        currentNodes.add(Walker(Coor(0L, 0L), Direction.LEFT, 0))
        currentNodes.add(Walker(Coor(0L, 0L), Direction.DOWN, 0))
        do {
          for (walker in currentNodes) {
              val nodes = neighbours(map.keys, walker.coor, walker.direction)
          }

        } while ( something )



        /*
        for (line in input) {
            println(line)
        }
         */
    }

    private fun findNeighbours(mapKeys: Set<Coor>, coor: Coor, direction: Direction, stepsTaken: Int): List<Coor> {
        val result = mutableListOf<Coor>()

        val above = Coor(coor.row - 1, coor.col)
        val below = Coor(coor.row + 1, coor.col)
        val left = Coor(coor.row, coor.col - 1)
        val right = Coor(coor.row, coor.col + 1)

        if (mapKeys.contains(above) && direction != Direction.DOWN && stepsTaken < 3) {
            result.add(above)
        }

        //TODO!+
    }

    // "stepsTaken" is the number of steps taken in the given direction: 0, 1, 2, or 3.
    class Walker(val coor: Coor, val direction: Direction, val stepsTaken: Int)

    class Node(val ch: Char) {
        //TODO?~ Keep track of how many steps forward were available at the time, too?
        var shortestDistance: Int? = null
    }
}