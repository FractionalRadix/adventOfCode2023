package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.Grid
import utils.buildGridMap
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.max

class Day23Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay23_sample1.txt""")
            .readLines()

        val grid = Grid(buildGridMap(input))

        val solution1 = solvePart1(grid)
        println("The longest path has $solution1 steps.")

        val solution2 = solvePart2(grid)
        println("The longest path without the 'steep slope' rule has $solution2 steps.")
    }

    fun solvePart2(grid: Grid): Int {
        // The recursive solution caused a stack overflow.
        // Let's try an iterative solution, where we keep track of the stack ourselves.

        // Determine the starting point.
        val firstRow = grid.grid.filter { it.key.row == grid.minRow }
        val firstColIdx = firstRow.filter { it.value == '.' }.minBy { it.key.col }.key.col
        val firstPos = Coor(grid.minRow, firstColIdx)

        // Determine the ending point.
        val lastRow = grid.grid.filter { it.key.row == grid.maxRow }
        val lastColIdx = lastRow.filter { it.value == '.' }.maxBy { it.key.col }.key.col
        val lastPos = Coor(grid.maxRow, lastColIdx)

        val paths = findPaths(grid, firstPos, lastPos)
        return paths.maxOf { it.tiles.size }
    }

    data class Path(val tiles: List<Coor>)

    /**
     * Given a grid, find all paths between the given first and the last position on that grid.
     * @param grid A grid, representing a maze, where '#' represents a wall.
     * @param firstPos Starting position on the grid.
     * @param lastPos Ending position on the grid.
     * @return A list containing all (and only) the paths between the starting and ending position.
     */
    private fun findPaths(grid: Grid, firstPos: Coor, lastPos: Coor): MutableList<Path> {
        val paths = mutableListOf<Path>()

        data class State(val pos: Coor, val visited: List<Coor>)

        val stack = Stack<State>()

        val goal = lastPos // No need to put this in the State class...

        var highestYet = 0

        val state0 = State(pos = firstPos, visited = emptyList())
        stack.push(state0)
        while (stack.isNotEmpty()) {
            val state = stack.pop()

            val neighbours = grid.neighbours(state.pos)
            val nextSteps = neighbours
                .filter { it.value != '#' } // Only the tiles that you can move through.
                .filter { it.key !in state.visited } // Only tiles you haven't visited yet.

            val newList = mutableListOf<Coor>()
            newList.addAll(state.visited)
            newList.add(state.pos)
            for (elt in nextSteps) {
                if (elt.key == goal) {
                    val foundPath = Path(newList)
                    highestYet = max(foundPath.tiles.size, highestYet)
                    println()
                    println("Found a path! Length: ${foundPath.tiles.size} (highest so far: $highestYet).")
                    paths.add(Path(newList))
                } else {
                    val nextState = State(elt.key, newList)
                    stack.push(nextState)
                }
            }
        }
        return paths
    }

    private fun findLeastOpenRow(grid: Grid): Set<Coor> {
        val openingsPerRow = mutableMapOf<Long, Int>()
        for (rowIdx in grid.minRow + 1..<grid.maxRow) {
            val row = grid.grid.filter { it.key.row == rowIdx }
            val nrOfOpenings = row.count { it.value != '#' }
            openingsPerRow[rowIdx] = nrOfOpenings
        }
        //println(openingsPerRow)
        val indexOfLeastOpenRow = openingsPerRow.minBy { it.value }.key
        val leastOpenRow = grid.grid
            .filter { it.key.row == indexOfLeastOpenRow }
            .filter { it.value != '#' }
            .keys
        return leastOpenRow
    }

    //TODO!~ This one causes a stack overflow on the puzzle input.
    fun solvePart2_OLD(grid: Grid): Int {
        // Inefficient solution: depth-first search with backtracking.

        // Determine the starting point.
        val firstRow = grid.grid.filter { it.key.row == grid.minRow }
        val firstColIdx = firstRow.filter { it.value == '.' }.minBy { it.key.col }.key.col
        val firstPos = Coor(grid.minRow, firstColIdx)

        // Determine the ending point.
        val lastRow = grid.grid.filter { it.key.row == grid.maxRow }
        val lastColIdx = lastRow.filter { it.value == '.' }.maxBy { it.key.col }.key.col
        val lastPos = Coor(grid.maxRow, lastColIdx)

        val lengths = stepPart2(grid, firstPos, emptyList(), lastPos)
        return lengths.max()
    }

    private fun stepPart2(grid: Grid, pos: Coor, visited: List<Coor>, goal: Coor): List<Int> {
        val neighbours = grid.neighbours(pos)

        val nextSteps = neighbours
            .filter { it.value != '#' } // Only the tiles that you can move through.
            .filter { it.key !in visited } // Only tiles you haven't visited yet.

        val pathLengths = mutableListOf<Int>()

        val newList = mutableListOf<Coor>()
        newList.addAll(visited)
        newList.add(pos)
        for (elt in nextSteps) {
            if (elt.key == goal) {
                println("Found! Length of path: ${newList.size}")
                pathLengths.add(newList.size)
            } else {
                val results = stepPart2(grid, elt.key, newList, goal)
                pathLengths.addAll(results)
            }
        }

        return pathLengths
    }

    fun solvePart1(grid: Grid): Int {
        // Inefficient solution: depth-first search with backtracking.

        // Determine the starting point.
        val firstRow = grid.grid.filter { it.key.row == grid.minRow }
        val firstColIdx = firstRow.filter { it.value == '.' }.minBy { it.key.col }.key.col
        val firstPos = Coor(grid.minRow, firstColIdx)

        // Determine the ending point.
        val lastRow = grid.grid.filter { it.key.row == grid.maxRow }
        val lastColIdx = lastRow.filter { it.value == '.' }.maxBy { it.key.col }.key.col
        val lastPos = Coor(grid.maxRow, lastColIdx)

        val lengths = step(grid, firstPos, emptyList(), lastPos)
        return lengths.max()
    }

    private fun step(grid: Grid, pos: Coor, visited: List<Coor>, goal: Coor): List<Int> {

        val neighbours: Map<Coor, Char> = when (grid.grid[pos]) {
            '.' -> grid.neighbours(pos)
            '^' -> grid.grid.filter { it.key == Coor(pos.row - 1, pos.col) }
            '>' -> grid.grid.filter { it.key == Coor(pos.row, pos.col + 1) }
            'v' -> grid.grid.filter { it.key == Coor(pos.row + 1, pos.col) }
            '<' -> grid.grid.filter { it.key == Coor(pos.row, pos.col - 1) }
            else -> {
                println("Error!! at position $pos . (Value: ${grid.grid[pos]}")
                emptyMap()
            }
        }

        val nextSteps = neighbours
            .filter { it.value != '#' } // Only the tiles that you can move through.
            .filter { it.key !in visited } // Only tiles you haven't visited yet.

        val pathLengths = mutableListOf<Int>()

        val newList = mutableListOf<Coor>()
        newList.addAll(visited)
        newList.add(pos)
        for (elt in nextSteps) {
            if (elt.key == goal) {
                pathLengths.add(newList.size)
            } else {
                val results = step(grid, elt.key, newList, goal)
                pathLengths.addAll(results)
            }
        }

        return pathLengths
    }
}