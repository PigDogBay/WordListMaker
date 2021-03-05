package com.mpdbailey.scowl

import com.mpdbailey.utils.removeAccents

class Ukacd(private val filename : String) {
    private val regexIllegal = Regex("[\\s'\\-.!]")
    private val regexPhrases = Regex("[\\s\\-]")
    private val regexAccents = Regex("[À-ÖØ-öø-ÿ]")
    private val regexNonAZ = Regex("[^a-zA-Z]")

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

    fun phrases() : List<Pair<String,String>>{
        return loadWordList(filename)
            .asSequence()
            .filter {it.contains(regexPhrases)}
            .map { it.removeAccents() }
            .sortedWith(comparator.thenBy { it })
            .distinct()
            .map{Pair(compressString(it).toLowerCase(),it)}
            .toList()
    }
}