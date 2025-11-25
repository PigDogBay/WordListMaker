package com.mpdbailey.scowl

import com.mpdbailey.utils.ResourceLoader
import com.mpdbailey.utils.removeAccents
import java.io.File
import java.util.*

const val SCOWL_V1_60 = "/source/scowlV1-60.txt"
const val SCOWL_V1_90 = "/source/scowlV1-90.txt"
const val SOWPODS = "/source/sowpods.txt"
const val BANNED_WORDS = "/bannedwords.txt"
const val EXTRA_WORDS = "/extrawords.txt"
const val SCOWL_V2 = "/source/scowlV2.txt"

val comparator = compareByDescending<String> {it.length }
val bannedWords = ResourceLoader().load(BANNED_WORDS)
val extraWords = ResourceLoader().load(EXTRA_WORDS)



fun createScowlWordList() : List<String> {
        return ResourceLoader()
            .load(SCOWL_V1_90)
            .union(ResourceLoader().load(SOWPODS))
            .union(createScowlV2WordList()) //Add the new words found in SCOWL v2
            .toList()
            .cleanList()
}

fun createSmallWordList() : List<String> {
    return ResourceLoader()
            .load(SCOWL_V1_60)
            .cleanList()
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
        .union(extraWords.map { it.lowercase(Locale.US) })
        .sortedWith(comparator.thenBy { it })
        .distinct()
}

/**
 * SCOWL v2 needs to become the main word list generator
 * For now its extra words are added to createScowlWordList()
 * (This needs to be flipped, add SCOWL v1 extra words to this list)
 *
 * Note words below 6 letters are excluded, any suitable ones have been added manually
 * These smaller words are mostly computing terms and abbreviations
 */
fun createScowlV2WordList() : List<String> {
    return ResourceLoader().load(SCOWL_V2)
        .filter { !it.contains('\'' )}
        .filter{it.length>5}                                    //Words below 6 contain a mostly abbreviations, computing terms, I've added correct ones manually
        .filter{ it[1].isLowerCase()}                           //Remove abbreviations
        .filter { it.last().isLowerCase() }                     //Remove abbreviations
        .map{ it.lowercase(Locale.US) }
        .map{it.removeAccents()}
        .minus(bannedWords.toSet())
        .union(extraWords.map { it.lowercase(Locale.US) })
        .sortedWith(comparator.thenBy { it })
        .distinct()
}

fun loadWordList(filename : String) : List<String> = File(filename).readLines(Charsets.ISO_8859_1)

