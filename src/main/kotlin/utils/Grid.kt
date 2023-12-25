package utils

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

        if (position.row < maxRow) {
            coordinates.add(Coor(position.row + 1, position.col))
        }
        if (position.col < maxCol) {
            coordinates.add(Coor(position.row, position.col + 1))
        }
        if (position.row > minRow) {
            coordinates.add(Coor(position.row - 1, position.col))
        }
        if (position.col > minCol) {
            coordinates.add(Coor(position.row, position.col - 1))
        }

        return grid.filter { it.key in coordinates }
    }

    fun print() {
        for (row in minRow .. maxRow) {
            println()
            for (col in minCol .. maxCol) {
                print(grid[Coor(row, col)])
            }
        }
    }

}