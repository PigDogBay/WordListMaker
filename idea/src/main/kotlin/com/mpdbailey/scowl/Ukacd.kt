package com.mpdbailey.scowl

import com.mpdbailey.utils.removeAccents

class Ukacd(private val filename : String) {
    private val regexIllegal = Regex("[\\s'\\-.!]")
    private val regexPhrases = Regex("[\\s\\-]")
    private val regexBannedWords = Regex("^(?!fuck|nigger|ape shit|piss)")
    private val regexAccents = Regex("[À-ÖØ-öø-ÿ]")
    private val regexNonAZ = Regex("[^a-zA-Z]")
    private val minPhraseLength = 5
    private val maxPhraseLength = 37

    fun singleWords() : List<String>{
        return loadWordList(filename)
            .asSequence()
            .filter {!it.contains(regexIllegal)}
            .filter{it.length>2}
            .map { it.removeAccents() }
            .sortedWith(comparator.thenBy { it })
            .distinct()
            .toList()
    }

    private fun compressString(str : String) : String{
        return str.replace(",","")
            .replace(",","")
            .replace("?","")
            .replace("!","")
            .replace("'","")
            .replace(".","")
            .replace(";","")
            .replace(":","")
            .replace(" ","")
            .replace("-","")
    }

    fun phrases() : List<String>{
        return loadWordList(filename)
            .asSequence()
            .filter {it.contains(regexPhrases)}
            .filter { it.length in minPhraseLength..maxPhraseLength }
            .filter{it.contains(regexBannedWords)}
            .map { it.removeAccents() }
            .sortedWith(comparator.thenBy { it })
            .distinct()
            .toList()
    }
}