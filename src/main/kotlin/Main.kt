import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main(args: Array<String>) {
    solveDay01()
}

fun solveDay01() {
    val firstInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay01_sample1.txt""").readLines()

    val values1 = firstInputAsStrings
        .filter { str -> str.isNotEmpty() }
        .sumOf { str -> calibrationValue(str) }

    println("Answer for the first part: $values1.")

    val secondInputAsStrings = Path("""src/main/resources/inputFiles/AoCDay01_sample2.txt""").readLines()
    val values2 = secondInputAsStrings
        .filter { str -> str.isNotEmpty() }
        .map { str -> replaceWordsWithDigits(str) }
        .sumOf { str -> calibrationValue(str) }

    println("Answer for the second part: $values2.")
}

fun calibrationValue(str: String): Int {
    val firstDigit = str
        .first { ch -> ch.isDigit() }
        .digitToInt()
    val lastDigit = str
        .last { ch -> ch.isDigit() }
        .digitToInt()
    println("$firstDigit $lastDigit")
    return ( 10 * firstDigit + lastDigit )
}

//TODO!-
fun replaceWordsWithDigits(str: String) = str
        .replace("one", "1")
        .replace("two", "2")
        .replace("three", "3")
        .replace("four", "4")
        .replace("five", "5")
        .replace("six", "6")
        .replace("seven", "7")
        .replace("eight", "8")
        .replace("nine", "9")
