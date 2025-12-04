package com.mpdbailey.scowl

import com.mpdbailey.utils.ResourceLoader
import com.mpdbailey.utils.removeAccents
import java.io.File
import java.util.*

const val SCOWL_V1_60 = "/source/scowlV1-60.txt"
const val SCOWL_V1_90 = "/source/scowlV1-90.txt"
const val SOWPODS = "/source/sowpods.txt"
const val BANNED_WORDS = "/bannedwords.txt"
const val BAD_WORDS = "/bad.txt"
const val EXTRA_WORDS = "/extrawords.txt"
const val SCOWL_V2 = "/source/scowlV2.txt"
const val SCOWL_60_EXTRA_WORDS = "/scowl60Extras.txt"

val comparator = compareByDescending<String> {it.length }
val bannedWords = ResourceLoader().load(BANNED_WORDS)
val badWords = ResourceLoader().load(BAD_WORDS)
val extraWords = ResourceLoader().load(EXTRA_WORDS)



fun createScowlWordList() : List<String> {
        return ResourceLoader()
            .load(SCOWL_V1_90)
            .union(ResourceLoader().load(SOWPODS))
            .union(ResourceLoader().load(SCOWL_V2)) //Add the new words found in SCOWL v2
            .toList()
            .cleanList()
}

fun createSmallWordList() : List<String> {
    return ResourceLoader()
            .load(SCOWL_V1_60)
            .toList()
            .cleanList()
}

fun createGameWordList() : List<String> {
    return ResourceLoader()
        .load(SCOWL_V1_60)
        .filter { !it.contains('\'' )}
        .filter{it.length>2}
        .filter{ it.first().isLowerCase()}                       //Remove Proper nouns
        .filter{ it[1].isLowerCase()}                           //Remove abbreviations
        .filter { it.last().isLowerCase() }                     //Remove abbreviations
        .map{ it.lowercase(Locale.US) }
        .map{it.removeAccents()}
        .minus(bannedWords.toSet())
        .minus(badWords.toSet())
        .union(ResourceLoader().load(SCOWL_60_EXTRA_WORDS))
        .sortedWith(comparator.thenBy { it })
        .distinct()
}

fun List<String>.cleanList() : List<String>{
    return this
        .filter { !it.contains('\'' )}
        .filter{it.length>2}
        .filter{ it[1].isLowerCase()}                           //Remove abbreviations
        .filter { it.last().isLowerCase() }                     //Remove abbreviations
        .map{ it.lowercase(Locale.US) }
        .map{it.removeAccents()}
        .minus(bannedWords.toSet())
        .minus(badWords.toSet())
        .union(extraWords.map { it.lowercase(Locale.US) })
        .sortedWith(comparator.thenBy { it })
        .distinct()
}

fun loadWordList(filename : String) : List<String> = File(filename).readLines(Charsets.ISO_8859_1)

