package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.buildGridMap
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day23Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay23.txt""")
            .readLines()

        val grid = Grid(buildGridMap(input))

        //val solution1 = solvePart1(grid)
        //println("The longest path has $solution1 steps.")

        val solution2 = solvePart2(grid)
        println("The longest path without the 'steep slope' rule has $solution2 steps.")
    }

    fun solvePart2(grid: Grid): Int {
        // The recursive solution caused a stack overflow.
        // Let's try an iterative solution, where we keep track of the stack ourselves.

        data class State(val pos: Coor, val visited: List<Coor>)
        val stack = Stack<State>()

        // Determine the starting point.
        val firstRow = grid.grid.filter { it.key.row == grid.minRow }
        val firstColIdx = firstRow.filter { it.value == '.' }.minBy { it.key.col }.key.col
        val firstPos = Coor(grid.minRow, firstColIdx)

        // Determine the ending point.
        val lastRow = grid.grid.filter { it.key.row == grid.maxRow }
        val lastColIdx = lastRow.filter { it.value == '.' }.maxBy { it.key.col }.key.col
        val lastPos = Coor(grid.maxRow, lastColIdx)

        val goal = lastPos // No need to put this in the State class...
        val pathLengths = mutableListOf<Int>() // again, no need to put this in the State class...

        val state0 = State(pos = firstPos, visited = emptyList())
        stack.push(state0)
        while (stack.isNotEmpty()) {
            //println("Stack size: ${stack.size}")
            val state = stack.pop()

            val neighbours = grid.neighbours(state.pos)
            val nextSteps = neighbours
                .filter { it.value != '#' } // Only the tiles that you can move through.
                .filter { it.key !in state.visited } // Only tiles you haven't visited yet.

            //val pathLengths = mutableListOf<Int>()

            val newList = mutableListOf<Coor>()
            newList.addAll(state.visited)
            newList.add(state.pos)
            for (elt in nextSteps) {
                if (elt.key == goal) {
                    println("Found! Length of path: ${newList.size}")
                    pathLengths.add(newList.size)
                } else {
                    //val results = stepPart2(grid, elt.key, newList, goal)
                    val nextState = State(elt.key, newList)
                    //pathLengths.addAll(results)
                    stack.push(nextState)
                }
            }
        }

        return pathLengths.max()
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

        Throwable().printStackTrace()

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
                println("Found! Length of path: ${newList.size}")
                pathLengths.add(newList.size)
            } else {
                val results = step(grid, elt.key, newList, goal)
                pathLengths.addAll(results)
            }
        }

        return pathLengths
    }
}

//TODO?~ Move to utils package?
class Grid(val grid: Map<Coor, Char>) {
    val minRow = grid.keys.minBy { it.row }.row
    val maxRow = grid.keys.maxBy { it.row }.row
    val minCol = grid.keys.minBy { it.col }.col
    val maxCol = grid.keys.maxBy { it.col }.col

    /**
     * Given a position on the grid, find the points left, right, above, and below that position.
     * If any of these is outside the grid, ignore them.
     * For example, if the given position is the top left corner, there would not be positions above or to the left.
     * So in that case the result would be just the position below and the position to the right.
     * @param position A position on that grid.
     * @return The coordinates of the positions horizontally and vertically adjacent to the given position.
     */
    fun neighbours(position: Coor): Map<Coor, Char> {
        val coordinates = mutableListOf<Coor>()

        if (position.row > minRow) {
            coordinates.add(Coor(position.row - 1, position.col))
        }
        if (position.row < maxRow) {
            coordinates.add(Coor(position.row + 1, position.col))
        }
        if (position.col > minCol) {
            coordinates.add(Coor(position.row, position.col - 1))
        }
        if (position.col < maxCol) {
            coordinates.add(Coor(position.row, position.col + 1))
        }

        return grid.filter { it.key in coordinates }
    }

    fun print() {
        for (row in minRow .. maxRow) {
            println()
            for (col in minCol .. maxCol) {
                print(grid[Coor(row,col)])
            }
        }
    }

}