import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main(args: Array<String>) {
    solveDay01()
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
