package com.mpdbailey.scowl

import com.mpdbailey.utils.ResourceLoader
import com.mpdbailey.utils.removeAccents
import java.io.File

const val SCOWL_DIR = "../../scowl/wordlist/scowl/final/"
const val SOWPODS = "../../wordlists/sowpods.txt"
const val BANNED_WORDS = "/bannedwords.txt"
const val EXTRA_WORDS = "/extrawords.txt"

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
        .union(loadWordList(SOWPODS))                           //Add words only found in SOWPODS
        .asSequence()
        .filter { !it.contains('\'' )}
        .filter{it.length>2}
        .filter{ it[1].isLowerCase()}                           //Remove abbreviations
        .filter { it.last().isLowerCase() }                     //Remove abbreviations
        .map{it.toLowerCase()}
        .map{it.removeAccents()}
        .minus(bannedWords)
        .toList()
        .union(extraWords)
        .sortedWith(comparator.thenBy { it })
        .distinct()
        .toList()
}

fun loadWordList(filename : String) : List<String> = File(filename).readLines(Charsets.ISO_8859_1)

