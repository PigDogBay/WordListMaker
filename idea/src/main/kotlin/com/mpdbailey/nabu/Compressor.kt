package com.mpdbailey.nabu

class Compressor(indices : List<SynonymIndex>, synonymSets : List<SynonymSet>) {

    private val compressedIndex = CompressedIndex()

    init {
        compressedIndex.createMap(synonymSets.map{it.index})
    }

    val compressedIndices =
        indices.map { SynonymIndex(it.word,compressedIndex.compress(it.indices)) }

    val compressedSynonymSets = synonymSets.map{ SynonymSet(
            compressedIndex.compress(it.index),
            it.words,
            compressedIndex.compress(it.associatedIndices)) }

}