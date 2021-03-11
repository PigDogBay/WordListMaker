package com.mpdbailey.scowl

import com.mpdbailey.nabu.Combine
import com.mpdbailey.utils.RomanNumerals
import com.mpdbailey.utils.removePunctuation

class WordNet {
    private val regexIllegal = Regex("[_'\\-.!/0-9]")
    private val regexPhraseIllegal = Regex("[/0-9]")
    private val regexPhrases = Regex("[\\s\\-_]")
    private val regexNonAZ = Regex("[^a-zA-Z]")
    private val minPhraseLength = 6
    private val maxPhraseLength = 42
    private val regexBannedWords = Regex("^(?!\\w*fuck|\\w+shit|\\w+-shit|piss-up|pissed|arse.around|arse.about|bollocks|cock.sucking)")
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
            .filter {it.word.contains(regexPhrases)}
            .filter {!it.word.contains(regexPhraseIllegal)}
            .filter {it.word.length in minPhraseLength..maxPhraseLength }
            .filter{it.word.contains(regexBannedWords)}
            .map{it.word}
            .map { it.replace('_', ' ') }
            .map{it.replace("st.", "st")}
            .filter { !it.contains('.') }
            .map { it.removePunctuation() }
            .sortedWith(comparator.thenBy { it })
            .distinct()
            .toList()
    }
}