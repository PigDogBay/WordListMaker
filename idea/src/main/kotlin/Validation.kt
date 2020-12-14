import java.io.File

/**
 * Only a-z chars allowed in a word
 */
fun containsIllegalChar(word : String) : Boolean = word.any{it < 'a' || it > 'z'}


fun validate(filename : String) : Int {
    //Check only contain a-z
    val invalidWords = File(filename)
        .readLines()
        .filter { containsIllegalChar(it) }
    invalidWords.forEach { println(it) }

    //Do not contain Roman numerals
    val roman = RomanNumerals()
    val numerals = (1..3000)
        .map { roman.toRoman(it).toLowerCase() }
    val invalidRomans = File(filename)
        .readLines()
        .intersect(numerals)
    invalidRomans.forEach{println(it)}

    return invalidWords.count() + invalidRomans.count()
}