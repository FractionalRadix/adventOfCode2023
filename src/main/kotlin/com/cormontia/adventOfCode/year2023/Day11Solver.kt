package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.buildGridMap
import utils.transposeStringList
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

class Day11Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay11.txt""")
            .readLines()
            .filter { it.isNotBlank() }

        val answer1 = solvePart1(input)
        println("The first sum of distances is $answer1")

        val answer2 = solvePart2(input)
        println("The second sum of distances is $answer2")
    }

    private fun solvePart2(input: List<String>): Long {
        val baseGrid = buildGridMap(input)
        val baseRowIndices = baseGrid.map { it.key.row }.toSet()
        val baseColIndices = baseGrid.map { it.key.col }.toSet()

        val galaxyGrid = baseGrid.filter { it.value != '.' }
        val rowIndices = galaxyGrid.map { it.key.row }.toSet()
        val colIndices = galaxyGrid.map { it.key.col }.toSet()

        val emptyRowIndices = baseRowIndices - rowIndices
        val emptyColIndices = baseColIndices - colIndices

        println(emptyRowIndices)
        println(emptyColIndices)

        val increment = 1000000

        val newGrid = mutableListOf<Coor>()
        for (galaxy in galaxyGrid) {
            val rowIdx = galaxy.key.row
            val colIdx = galaxy.key.col
            // Find all the empty rows/cols BEFORE this galaxy, multiply that amount by "increment", and add that.
            val earlierEmptyRows = emptyRowIndices.filter { it < rowIdx }.size
            val earlierEmptyCols = emptyColIndices.filter { it < colIdx }.size
            val newCoor = Coor(rowIdx + (increment - 1) * earlierEmptyRows, colIdx + (increment - 1) * earlierEmptyCols)
            newGrid.add(newCoor)
        }
        println(newGrid)

        val distances = distanceBetweenPairs(newGrid)
        return distances.sum()

    }

    private fun solvePart1(input: List<String>): Long {
        // Double the rows
        val doubledRows = doubleRows(input)
        // Transpose, then double the columns.
        val transposedDoubledRows = transposeStringList(doubledRows)
        val doubledColumns = doubleRows(transposedDoubledRows)
        // Transpose back. Now you have the "expanded universe".
        val expandedUniverse = transposeStringList(doubledColumns)

        val grid = buildGridMap(expandedUniverse)

        val galaxies = grid.filter { it.value == '#' }.map { it.key }
        val distances = distanceBetweenPairs(galaxies)

        return distances.sum()
    }

    private fun distanceBetweenPairs(galaxies: List<Coor>): List<Long> {
        val galaxyPairs = mutableListOf<Pair<Coor, Coor>>()
        for (i in galaxies.indices) {
            for (j in i + 1..<galaxies.size) {
                galaxyPairs.add(Pair(galaxies[i], galaxies[j]))
            }
        }

        val distances = galaxyPairs.map {
            val rowDist = abs(it.first.row - it.second.row)
            val colDist = abs(it.first.col - it.second.col)
            rowDist + colDist
        }
        return distances
    }

    /**
     * Given a list of strings, copy it - but if any string consists ONLY of dots ('.'), copy it twice.
     * @param input A list of strings.
     * @return A copy of the input, where every string that consists entirely of dots is doubled.
     */
    private fun doubleRows(input: List<String>): MutableList<String> {
        val doubledRows = mutableListOf<String>()
        for (row in input) {
            doubledRows.add(row)
            if (row.all { it == '.' }) {
                doubledRows.add(row)
            }
        }
        return doubledRows
    }
}