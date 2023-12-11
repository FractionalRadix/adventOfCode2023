package utils

// Kotlin doesn't seem to have a built-in pow function for integers, only for Doubles...
fun pow(base: Int, power: Int): Int {
    var result = 1
    for (i in 1 .. power) {
        result *= base
    }
    return result
}