package com.cormontia.adventOfCode.year2023.utils

/**
 * Given a rectangular area in a grid, give the coordinates that <em>directly surround<em> that area.
 * @param topLeft Top left coordinate of the rectangle. In other words, the point with the lowest row- and column-indices.
 * @param bottomRight Bottom right coordinate of the rectangle. In other words, the point with highest row- and column-indices.
 * @return A list of all the points that touch the rectangle (i.e. are directly near it) but are not part of it.
 */
fun surroundingCoordinates(topLeft: Coor, bottomRight: Coor): List<Coor> {
    val result = mutableListOf<Coor>()

    val top = topLeft.row
    val aboveTop = top - 1
    val beforeLeft = topLeft.col - 1
    val afterRight = bottomRight.col + 1
    val bottom = bottomRight.row
    val belowBottom = bottom + 1

    // The rows above and below the rectangle.
    for (colIdx in beforeLeft .. afterRight ) {
        result.add(Coor(aboveTop, colIdx))
        result.add(Coor(belowBottom, colIdx))
    }

    // The columns to the left and right.
    // Note that the corner points have already been added, when we added the rows.
    for(rowIdx in top .. bottom ) {
        result.add(Coor(rowIdx, beforeLeft))
        result.add(Coor(rowIdx, afterRight))
    }

    return result
}