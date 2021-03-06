package com.mpdbailey.scowl

import com.mpdbailey.utils.RomanNumerals
import java.io.File

const val SCOWL_FILENAME = "../../out/words.txt"
const val PORTUGUESE_FILENAME = "../../out/wordlist-pt.txt"
const val FRENCH_FILENAME = "../../out/wordlist-fr.txt"
const val SPANISH_FILENAME = "../../out/wordlist-es.txt"
const val GERMAN_FILENAME = "../../out/wordlist-de.txt"
const val ITALIAN_FILENAME = "../../out/wordlist-it.txt"


fun main(args: Array<String>) {
    validatePrinter("SCOWL", SCOWL_FILENAME)
    validatePrinter("German",GERMAN_FILENAME )
    validatePrinter("Spanish", SPANISH_FILENAME)
    validatePrinter("French", FRENCH_FILENAME)
    validatePrinter("Italian", ITALIAN_FILENAME)
    validatePrinter("Portuguese", PORTUGUESE_FILENAME)
}

private fun validatePrinter(displayName : String, filename: String){
    println("   ***   $displayName   ***")
    val errors = validate(filename)
    println("$displayName error count $errors")
    println("-----------------------")
}


/**
 * Only a-z chars allowed in a word
 */
fun containsIllegalChar(word : String) : Boolean = word.any{it < 'a' || it > 'z'}

private val legalRomanNumeralWords = listOf("div","dix","mix","di","li","mi","mm","xi","i")

fun validate(filename : String) : Int {
    val regexIllegalChars = Regex("[^a-z\\s\\-]")
    //Check only contain a-z,- and space
    val invalidWords = File(filename)
        .readLines()
        .filter { it.contains(regexIllegalChars) }
    invalidWords.forEach { println(it) }

    //Do not contain Roman numerals
    val roman = RomanNumerals()
    val numerals = (1..3000)
        .map { roman.toRoman(it).toLowerCase() }
    val invalidRomans = File(filename)
        .readLines()
        .intersect(numerals)
        .minus(legalRomanNumeralWords)
    invalidRomans.forEach{println(it)}

    return invalidWords.count() + invalidRomans.count()
}