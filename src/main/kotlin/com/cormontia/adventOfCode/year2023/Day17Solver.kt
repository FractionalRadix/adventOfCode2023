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
            .mapValues { Node(it.value.toInt()) }
            .toMutableMap()

        val maxRows = map.keys.maxBy { it.row }.row
        val maxCols = map.keys.maxBy { it.col }.col
        val destinationNode = Coor(maxRows, maxCols)

        var currentWalkers = mutableListOf<Walker>()
        currentWalkers.add(Walker(Coor(0L, 0L), Direction.LEFT, 0, map[Coor(0L, 0L)]?.heatLoss!!))
        currentWalkers.add(Walker(Coor(0L, 0L), Direction.DOWN, 0, map[Coor(0L, 0L)]?.heatLoss!!))
        do {
            val nextWalkers = mutableListOf<Walker>()
            for (walker in currentWalkers) {
                val heatLoss = walker.cumulativeHeatLoss
                val currentBest = map[walker.coor]?.shortestDistance
                if (currentBest == null || heatLoss < currentBest) {
                    map[walker.coor]!!.shortestDistance = walker.cumulativeHeatLoss
                }

                val spawnedWalkers = findNextIteration(map, walker)
                nextWalkers.addAll(spawnedWalkers)
            }

            println("Next generation size: ${nextWalkers.size}")

            if (currentWalkers.any { it.coor == destinationNode }) {
                break;
            }

            currentWalkers = nextWalkers

        } while ( true )

        println("Shortest distance: ${map[destinationNode]!!.shortestDistance}")


        /*
        for (line in input) {
            println(line)
        }
         */
    }

    private fun findNextIteration(map: Map<Coor, Node>, walker: Walker /* coor: Coor, direction: Direction, stepsTaken: Int */): List<Walker> {
        val result = mutableListOf<Walker>()
        val coor = walker.coor
        val direction = walker.direction
        val stepsTaken = walker.stepsTaken
        val mapKeys = map.keys
        val cumulativeLoss = walker.cumulativeHeatLoss

        val above = Coor(coor.row - 1, coor.col)
        if (mapKeys.contains(above)) {
            val heatLoss = map[above]?.heatLoss ?: 0
            if (direction == Direction.UP && stepsTaken < 3) {
                result.add(Walker(above, direction, stepsTaken + 1, cumulativeLoss + heatLoss))
            } else if (direction != Direction.DOWN) {
                result.add(Walker(above, direction, 0, cumulativeLoss + heatLoss))
            }
        }

        val below = Coor(coor.row + 1, coor.col)
        if (mapKeys.contains(below)) {
            val heatLoss = map[above]?.heatLoss?: 0
            if (direction == Direction.DOWN && stepsTaken < 3) {
                result.add(Walker(below, direction, stepsTaken + 1, cumulativeLoss + heatLoss))
            } else if (direction != Direction.UP) {
                result.add(Walker(below, direction, 0, cumulativeLoss + heatLoss))
            }
        }

        val left = Coor(coor.row, coor.col - 1)
        if (mapKeys.contains(left)) {
            val heatLoss = map[above]?.heatLoss?: 0
            if (direction == Direction.LEFT && stepsTaken < 3) {
                result.add(Walker(left, direction, stepsTaken + 1, cumulativeLoss + heatLoss))
            } else if (direction != Direction.RIGHT) {
                result.add(Walker(left, direction, 0, cumulativeLoss + heatLoss))
            }
        }

        val right = Coor(coor.row, coor.col + 1)
        if (mapKeys.contains(right)) {
            val heatLoss = map[above]?.heatLoss?: 0
            if (direction == Direction.RIGHT && stepsTaken < 3) {
                result.add(Walker(right, direction, stepsTaken + 1, cumulativeLoss + heatLoss))
            } else if (direction != Direction.LEFT) {
                result.add(Walker(right, direction, 0, cumulativeLoss + heatLoss))
            }
        }

        return result
    }

    private fun findNeighbours(mapKeys: Set<Coor>, coor: Coor, direction: Direction, stepsTaken: Int): List<Coor> {
        val result = mutableListOf<Coor>()

        val above = Coor(coor.row - 1, coor.col)
        if (mapKeys.contains(above)) {
            if (direction == Direction.UP && stepsTaken < 3) {
                result.add(above)
            } else if (direction != Direction.DOWN) {
                result.add(above)
            }
        }

        val below = Coor(coor.row + 1, coor.col)
        if (mapKeys.contains(below)) {
            if (direction == Direction.DOWN && stepsTaken < 3) {
                result.add(below)
            } else if (direction != Direction.UP) {
                result.add(below)
            }
        }

        val left = Coor(coor.row, coor.col - 1)
        if (mapKeys.contains(left)) {
            if (direction == Direction.LEFT && stepsTaken < 3) {
                result.add(left)
            } else if (direction != Direction.RIGHT) {
                result.add(left)
            }
        }

        val right = Coor(coor.row, coor.col + 1)
        if (mapKeys.contains(right)) {
            if (direction == Direction.RIGHT && stepsTaken < 3) {
                result.add(right)
            } else if (direction != Direction.LEFT) {
                result.add(right)
            }
        }

        return result
    }

    // "stepsTaken" is the number of steps taken in the given direction: 0, 1, 2, or 3.
    class Walker(val coor: Coor, val direction: Direction, val stepsTaken: Int, val cumulativeHeatLoss: Int)

    class Node(val heatLoss: Int) {
        //TODO?~ Keep track of how many steps forward were available at the time, too?
        var shortestDistance: Int? = null
    }
}