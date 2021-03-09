package com.mpdbailey.scowl

import com.mpdbailey.nabu.Combine
import com.mpdbailey.utils.RomanNumerals

class WordNet {
    private val regexIllegal = Regex("[_'\\-.!/0-9]")
    private val regexPhrases = Regex("[\\s\\-]")
    private val regexNonAZ = Regex("[^a-zA-Z]")
    private val minPhraseLength = 5
    private val maxPhraseLength = 37
    private val romanNumerals = RomanNumerals()
        .toRoman(1..2000)
        .map { it.toLowerCase() }

    fun singleWords() : List<String>{
        return Combine()
            .indices()
            .asSequence()
            .map{it.word}
            .filter {!it.contains(regexIllegal)}
            .filter{it.length>2}
            .minus(bannedWords)
            .minus(romanNumerals)
            .sortedWith(comparator.thenBy { it })
            .distinct()
            .toList()
    }

    fun phrases() : List<String>{
        return Combine()
            .indices()
            .asSequence()
            .map{it.word}
            .map { it.replace('_', ' ') }
            .map{it.replace("st.", "st")}
            .filter { it.length in minPhraseLength..maxPhraseLength }
            .sortedWith(comparator.thenBy { it })
            .distinct()
            .toList()
    }
}