package com.mpdbailey.scowl

import com.mpdbailey.utils.ResourceLoader
import com.mpdbailey.utils.removeAccents
import java.io.File
import java.util.Locale

const val SCOWL_DIR = "../../scowl/wordlist/scowl/final/"
const val SOWPODS = "/source/sowpods.txt"
const val BANNED_WORDS = "/bannedwords.txt"
const val EXTRA_WORDS = "/extrawords.txt"
const val SCOWL_V2 = "/source/scowlV2.txt"

val comparator = compareByDescending<String> {it.length }
val bannedWords = ResourceLoader().load(BANNED_WORDS)
val extraWords = ResourceLoader().load(EXTRA_WORDS)

fun createScowlWordList() : List<String> {
    return File(SCOWL_DIR).listFiles()                          //List all word files in the SCOWL output folder
        .filter { it.extension!="95" }                          //Remove certain files
        .filter { !it.name.contains("abbreviations") }
        .filter { !it.name.contains("special") }
        .filter { !it.name.contains("contractions") }
        .flatMap { it.readLines(Charsets.ISO_8859_1) }          //SCOWL lists stored as ISO_8859_1
        .union(ResourceLoader().load(SOWPODS))
        .union(createScowlV2WordList()) //Add the new words found in SCOWL v2
        .asSequence()
        .filter { !it.contains('\'' )}
        .filter{it.length>2}
        .filter{ it[1].isLowerCase()}                           //Remove abbreviations
        .filter { it.last().isLowerCase() }                     //Remove abbreviations
        .map{ it.lowercase(Locale.US) }
        .map{it.removeAccents()}
        .minus(bannedWords)
        .toList()
        .union(extraWords.map { it.toLowerCase() })
        .sortedWith(comparator.thenBy { it })
        .distinct()
        .toList()
}

fun createSmallWordList() : List<String> {
    return File(SCOWL_DIR).listFiles()                          //List all word files in the SCOWL output folder
        .filter { it.extension!="95" }                          //Remove rare words
        .filter { it.extension!="80" }
        .filter { it.extension!="70" }
        .filter { !it.name.contains("abbreviations") }
        .filter { !it.name.contains("special") }
        .filter { !it.name.contains("contractions") }
        .flatMap { it.readLines(Charsets.ISO_8859_1) }          //SCOWL lists stored as ISO_8859_1
        .asSequence()
        .filter { !it.contains('\'' )}
        .filter{it.length>2}
        .filter{ it[1].isLowerCase()}                           //Remove abbreviations
        .filter { it.last().isLowerCase() }                     //Remove abbreviations
        .map{ it.lowercase(Locale.US) }
        .map{it.removeAccents()}
        .minus(bannedWords)
        .toList()
        .union(extraWords.map { it.toLowerCase() })
        .sortedWith(comparator.thenBy { it })
        .distinct()
        .toList()
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
        .asSequence()
        .filter { !it.contains('\'' )}
        .filter{it.length>5}                                    //Words below 6 contain a mostly abbreviations, computing terms, I've added correct ones manually
        .filter{ it[1].isLowerCase()}                           //Remove abbreviations
        .filter { it.last().isLowerCase() }                     //Remove abbreviations
        .map{ it.lowercase(Locale.US) }
        .map{it.removeAccents()}
        .minus(bannedWords)
        .toList()
        .union(extraWords.map { it.toLowerCase() })
        .sortedWith(comparator.thenBy { it })
        .distinct()
        .toList()
}


fun loadWordList(filename : String) : List<String> = File(filename).readLines(Charsets.ISO_8859_1)

