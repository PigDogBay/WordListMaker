package com.mpdbailey.nabu

import com.mpdbailey.scowl.bannedWords

class Compressor(indices : List<SynonymIndex>, synonymSets : List<SynonymSet>) {

    private val compressedIndex = CompressedIndex()
    private val regexIllegal = Regex("[./0-9]")
    private val replaceBrackets = Regex("\\([a-z]+\\)")
    /*
        Synonyms contain A-Z 0-9 () ' . - /
        A-Z is lowercased
        Remove strings containing 0-9 /
        Removed brackets and what is inside
        Remove chars ' and .
        Remove any strings containing wordnet

     */
    private val regexSynonymIllegal = Regex("[//0-9]")
    private val regexSynonymRemoveChars = Regex("['.]")

    init {
        compressedIndex.createMap(synonymSets.map{it.index})
    }

    private fun cleanIndexWord(word : String) = word
        .replace("\'","")
        .replace('_',' ')
        .replace('-',' ')

    private fun compressMultipleIndices(indices : List<SynonymIndex>) : SynonymIndex {
        val all = indices
            .flatMap { it.indices }
            .distinct()
        return SynonymIndex(indices[0].word, all)
    }

    private fun processSynSetWords(words : List<String>) = words
        .map { it.replace(replaceBrackets,"") }
        .map{it.toLowerCase()}
        .filter { !it.contains(regexSynonymIllegal) }
        .filter { !it.contains("wordnet") }
        .map { it.replace(regexSynonymRemoveChars,"") }
        .filter{ !bannedWords.contains(it)}
        .filter { it.isNotEmpty() }
        .distinct()

    val compressedIndices =
        indices
            .asSequence()
            .filter { it.word.length in 2..28 }
            .filter {!it.word.contains(regexIllegal)}
            .filter {!(it.word.contains('\'') && it.word.length<7)}
            .map { SynonymIndex(
                cleanIndexWord(it.word)
                ,compressedIndex.compress(it.indices)) }
            .filter{ !bannedWords.contains(it.word)}
            //multiple entries for can exist for the same phrase, eg step-in or 'step in'
            .groupBy { it.word }
            .map { compressMultipleIndices(it.value) }
            .toList()

    val compressedSynonymSets = synonymSets.map{ SynonymSet(
            compressedIndex.compress(it.index),
            processSynSetWords(it.words),
            compressedIndex.compress(it.associatedIndices)) }

}