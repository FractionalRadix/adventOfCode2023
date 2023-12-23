package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.buildGridMap
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day23Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay23.txt""")
            .readLines()

        val grid = Grid(buildGridMap(input))

        val solution1 = solvePart1(grid)
        println("The longest path has $solution1 steps.")
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