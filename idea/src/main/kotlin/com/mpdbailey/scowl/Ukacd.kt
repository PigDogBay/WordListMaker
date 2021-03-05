package com.mpdbailey.scowl

import com.mpdbailey.utils.removeAccents

class Ukacd(private val filename : String) {
    private val regexIllegal = Regex("[\\s'\\-.!]")

    fun createWordList() : List<String>{
        return loadWordList(filename)
            .asSequence()
            .filter {!it.contains(regexIllegal)}
            .filter{it.length>2}
            .map { it.removeAccents() }
            .sortedWith(comparator.thenBy { it })
            .distinct()
            .toList()
    }
}