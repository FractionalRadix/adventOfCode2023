package com.cormontia.adventOfCode.year2023.utils

/**
 * Turn an array of strings into a mapping: (row,column) -> character
 * @param strings A list of strings
 * @return A mapping from (row, column) coordinates to characters.
 */
fun buildGridMap(strings: List<String>): MutableMap<Coor, Char> {
    val map = mutableMapOf<Coor, Char>()
    for (rowIdx in strings.indices) {
        val row = strings[rowIdx]
        for (colIdx in row.indices) {
            val char = row[colIdx]
            map[Coor(rowIdx, colIdx)] = char
        }
    }
    return map
}