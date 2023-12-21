package com.cormontia.adventOfCode.year2023

import utils.Coor
import utils.buildGridMap
import kotlin.io.path.Path
import kotlin.io.path.readLines

class Day21Solver {
    fun solve() {
        val input = Path("""src/main/resources/inputFiles/AoCDay21_sample1.txt""")
            .readLines()
        val map = buildGridMap(input)

        val destinations = solvePart1(map)
        println("Nr of destinations: $destinations")
    }

    fun solvePart1(map: MutableMap<Coor, Char>): Int {
        var newMap = map
        for (i in 1 .. 6) {
            newMap = iterate(newMap)

            printMap(newMap)
            val count = newMap.count { it.value == 'O' || it.value == 'S' }
            println(count)
        }
        return map.count { it.value == 'O' || it.value == 'S' }
    }

    private fun iterate(map: MutableMap<Coor, Char>): MutableMap<Coor, Char> {
        val result = mutableMapOf<Coor, Char>()

        val minRow = map.keys.minBy { it.row }.row
        val maxRow = map.keys.maxBy { it.row }.row
        val minCol = map.keys.minBy { it.col }.col
        val maxCol = map.keys.maxBy { it.col }.col

        for (row in minRow .. maxRow) {
            for (col in minCol..maxCol) {
                val coor = Coor(row, col)
                if (map.containsKey(coor)) {
                    val next = map[coor]
                    if (next == 'O')
                        result[coor] = '.'
                    else
                        result[coor] = next!!
                }
            }
        }

        for (row in minRow .. maxRow) {
            for (col in minCol .. maxCol) {
                //println("($row, $col)")
                val cur = map[Coor(row, col)]

                if (cur == 'S' || cur =='O') {
                    updateNeighbour(row - 1, col, result)
                    updateNeighbour(row + 1, col, result)
                    updateNeighbour(row, col - 1, result)
                    updateNeighbour(row, col + 1, result)
                }
            }
        }

        return result
    }

    private fun updateNeighbour(row: Long, col: Long, map: MutableMap<Coor, Char>) {
        val neighbour = Coor(row, col)
        //if (map[neighbour] != null && map[neighbour] != '#' && map[neighbour] != 'S') {
        if (map[neighbour] == '.') {
            //println("Updating $neighbour")
            map[neighbour] = 'O'
        }
    }

    private fun printMap(map: Map<Coor, Char>) {
        val minRow = map.keys.minBy { it.row }.row
        val maxRow = map.keys.maxBy { it.row }.row
        val minCol = map.keys.minBy { it.col }.col
        val maxCol = map.keys.maxBy { it.col }.col

        for (row in minRow .. maxRow) {
            println()
            for (col in minCol .. maxCol ) {
                if (map.containsKey(Coor(row, col))) {
                    print(map[Coor(row,col)])
                } else {
                    print(' ')
                }
            }
        }
        println()
    }

}