import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main(args: Array<String>) {
    //solveDay01()
    solveDay02() // 2268, 63542
}


fun solveDay02() {
    val firstInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay02.txt""")
        .readLines()
        .filter { str -> str.isNotEmpty() }
    val games = parseDay02(firstInputAsStrings)

    // Solve the first part.
    var sumPart1 = 0
    games.forEach {
        game -> if (game.isPossible(maxRed = 12, maxGreen = 13, maxBlue = 14)) {
               sumPart1 += game.gameNr
        }
    }
    println("The sum of possible games is: $sumPart1")

    // Solve the second part.
    var sumPart2 = 0
    games.forEach {
        game -> sumPart2 += game.power()
    }
    println("The sum of powers of the minimal sets of cubes is: $sumPart2")
}

fun parseDay02(inputLines: List<String>): List<Game> {
    val games = mutableListOf<Game>()
    for (i in 1 .. inputLines.size) {
        val game = Game(i)
        val curStr = inputLines[i - 1]
        var stripped = curStr.dropWhile { ch -> ch != ':' }
        stripped = stripped.substring(1)
        val draws = stripped.split(';')
        for (draw in draws) {
            game.addDraw(draw)
        }
        games.add(game)
    }
    return games
}


class Game(val gameNr: Int) {
    data class Draw(val red: Int, val green: Int, val blue: Int)

    private val draws = mutableListOf<Draw>()

    fun addDraw(draw: String) {
        val parsedDraw = parseDraw(draw)
        draws.add(parsedDraw)
    }

    private fun parseDraw(draw: String): Draw {
        val cubes = draw
            .split(',')
        var red = 0
        var green = 0
        var blue = 0
        // All the parsing that's left to do, is to determine the amount and the color...
        for (cube in cubes) {
            val parsed = cube.trim().split(' ')
            val nrOfCubes = parsed[0].toInt()
            val color = parsed[1]

            if (color == "red") {
                red = nrOfCubes
            }

            if (color == "green") {
                green = nrOfCubes
            }

            if (color == "blue") {
                blue = nrOfCubes
            }
        }
        return Draw(red, green, blue)
    }

    fun isPossible(maxRed: Int, maxGreen: Int, maxBlue: Int): Boolean {
        val redPossible = draws.none { draw -> draw.red > maxRed }
        val greenPossible = draws.none { draw -> draw.green > maxGreen }
        val bluePossible = draws.none { draw -> draw.blue > maxBlue }
        return (redPossible && greenPossible && bluePossible)
    }

    fun power(): Int {
        val minimumRedRequired = draws.maxOfOrNull { draw -> draw.red } ?: 0
        val minimumGreenRequired = draws.maxOfOrNull { draw -> draw.green } ?: 0
        val minimumBlueRequired = draws.maxOfOrNull { draw -> draw.blue } ?: 0

        return minimumRedRequired * minimumGreenRequired * minimumBlueRequired
    }

    fun printGame() {
        println()
        print("Game $gameNr: ")
        draws.forEach {
            d -> print("${d.red} red, ${d.green} green, ${d.blue} blue;")
        }
        println()
    }
}




fun solveDay02_part1() {
    val firstInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay02.txt""")
        .readLines()
        .filter { str -> str.isNotEmpty() }

    var sum = 0
    val maxRed = 12
    val maxGreen = 13
    val maxBlue = 14
    for (i in 1 .. firstInputAsStrings.size) {
        var possible = true
        val curStr = firstInputAsStrings[i - 1]
        var stripped = curStr.dropWhile { ch -> ch != ':' }
        stripped = stripped.substring(1)
        //println(stripped)
        val draws = stripped.split(';')
        //draws.forEach { draw -> println(draw) }
        for (draw in draws) {
            val cubes = draw
                .split(',')
            //cubes.forEach { cube -> println(cube) }
            // All the parsing that's left to do, is to determine the amount and the color...
            for (cube in cubes) {
                val parsed = cube.trim().split(' ')
                val nrOfCubes = parsed[0].toInt()
                val color = parsed[1]
                //println("nr of cubes: $nrOfCubes color: $color")
                if (color == "red" && nrOfCubes > maxRed)
                    possible = false
                if (color == "green" && nrOfCubes > maxGreen)
                    possible = false
                if (color == "blue" && nrOfCubes > maxBlue)
                    possible = false
            }
        }

        if (possible) {
            println("game $i is possible")
            sum += i
        }
    }
    println("Sum of possible games: $sum")
}

fun solveDay01() {
    val firstInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay01.txt""").readLines()
    val values1 = firstInputAsStrings
        .filter { str -> str.isNotEmpty() }
        .sumOf { str -> calibrationValue(str) }
    println("Answer for the first part: $values1.") // 55447

    val secondInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay01.txt""").readLines()
    val firstDigits = secondInputAsStrings
        .filter { str -> str.isNotEmpty() }
        .map { str -> firstWordOrDigit(str) }
    val secondDigits = secondInputAsStrings
        .filter { str -> str.isNotEmpty() }
        .map { str -> lastWordOrDigit(str) }
    val values2 = firstDigits
        .zip(secondDigits) { a,b -> 10 * a + b }
        .sum()
    println("Answer for the second part: $values2") // 54706
}

fun calibrationValue(str: String): Int {
    val firstDigit = str
        .first { ch -> ch.isDigit() }
        .digitToInt()
    val lastDigit = str
        .last { ch -> ch.isDigit() }
        .digitToInt()
    return ( 10 * firstDigit + lastDigit )
}

/** Get the first digit from a string, whether it is spelled out or just given as a digit.
 * Naive but functional solution.
 * @param str A String that contains at least one digit, either as a digit or spelled out in letters.
 * @return The value of the first digit in the input.
 */
fun firstWordOrDigit(str: String): Int {
    if (str.startsWith("zero") or str.startsWith("0"))
        return 0

    if (str.startsWith("one") or str.startsWith("1"))
        return 1
    if (str.startsWith("two") or str.startsWith("2"))
        return 2
    if (str.startsWith("three") or str.startsWith("3"))
        return 3

    if (str.startsWith("four") or str.startsWith("4"))
        return 4
    if (str.startsWith("five") or str.startsWith("5"))
        return 5
    if (str.startsWith("six") or str.startsWith("6"))
        return 6

    if (str.startsWith("seven") or str.startsWith("7"))
        return 7
    if (str.startsWith("eight") or str.startsWith("8"))
        return 8
    if (str.startsWith("nine") or str.startsWith("9"))
        return 9

    return firstWordOrDigit(str.substring(1))
}

/** Get the last digit from a string, whether it is spelled out or just given as a digit.
 * Naive but functional solution.
 * @param str A String that contains at least one digit, either as a digit or spelled out in letters.
 * @return The value of the last digit in the input.
 */
fun lastWordOrDigit(str: String): Int {
    if (str.endsWith("zero") or str.endsWith("0"))
        return 0

    if (str.endsWith("one") or str.endsWith("1"))
        return 1
    if (str.endsWith("two") or str.endsWith("2"))
        return 2
    if (str.endsWith("three") or str.endsWith("3"))
        return 3

    if (str.endsWith("four") or str.endsWith("4"))
        return 4
    if (str.endsWith("five") or str.endsWith("5"))
        return 5
    if (str.endsWith("six") or str.endsWith("6"))
        return 6

    if (str.endsWith("seven") or str.endsWith("7"))
        return 7
    if (str.endsWith("eight") or str.endsWith("8"))
        return 8
    if (str.endsWith("nine") or str.endsWith("9"))
        return 9

    return lastWordOrDigit(str.substring(0, str.length - 1))
}
