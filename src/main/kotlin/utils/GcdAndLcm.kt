package utils

// Source: https://stackoverflow.com/a/4202114/812149
// Converted to Kotlin by IntelliJ.

fun gcd(a0: Long, b0: Long): Long {
    var a = a0
    var b = b0
    while (b > 0) {
        val temp = b
        b = a % b // % is remainder
        a = temp
    }
    return a
}

fun gcd(input: LongArray): Long {
    var result = input[0]
    for (i in 1..< input.size) {
        result = gcd(result, input[i])
    }
    return result
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

fun lcm(input: LongArray): Long {
    var result = input[0]
    for (i in 1..< input.size) {
        result = lcm(result, input[i])
    }
    return result
}
