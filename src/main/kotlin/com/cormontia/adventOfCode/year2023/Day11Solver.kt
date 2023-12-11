package com.cormontia.adventOfCode.year2023

import com.cormontia.adventOfCode.year2023.utils.Coor
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day11Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay11_sample1.txt""")
            .readLines()
            .filter { it.isNotBlank() }

        val originalNumRows = input.size
        val originalNumCols = input[0].length

        val originalMap = mutableMapOf<Coor, Char>()
        for (rowIdx in input.indices) {
            val line = input[rowIdx]
            for (colIdx in line.indices) {
                val coor = Coor(rowIdx, colIdx)
                originalMap[coor] = line[colIdx]
            }
        }

        val expandedRowsMap = mutableMapOf<Coor, Char>()
        var expRowIdx = 0
        for (originalRowIdx in 0 .. originalNumRows) {
            val originalRow = originalMap.filter { it.key.row == originalRowIdx }
            if (originalRow.values.all { it == '.' }) {
                for (colIdx in 0 .. originalNumCols) {
                    expandedRowsMap[Coor(expRowIdx, colIdx)] = '.'
                    expandedRowsMap[Coor(expRowIdx + 1, colIdx)] = '.'
                    expRowIdx += 2
                }
            } else {
                // Copy current line.
                for (colIdx in 0 .. originalNumCols) {
                    expandedRowsMap[Coor(expRowIdx, colIdx)] = originalRow[Coor(originalRowIdx, colIdx)]!!
                }
                expRowIdx++
            }
        }

        
    }
}