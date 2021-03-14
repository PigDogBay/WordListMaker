package com.mpdbailey.nabu

import wordnet.common.Definition
import wordnet.common.Index
import java.lang.IllegalArgumentException

class Adapter {

    fun convert(index: Index) : SynonymIndex {
        val associatedIndices = index.synSetsOffsets
            .map { convertPosition(it,index.partOfSpeech) }
        return SynonymIndex(index.word,associatedIndices)
    }

    fun convert(definition: Definition) : SynonymSet {
        val associatedIndices = definition.associatedIndices
            .map{ convertPosition(it.offset,it.partOfSpeech.letter) }
        return SynonymSet(convertPosition(definition.position,definition.partOfSpeech),definition.words, associatedIndices)
    }

    /*
        Max position (data.noun last line) is 0xE9D86E
     */
    private fun convertPosition(position : Int, pos : String) : String {
        if (position > 0x00ffffff){
            throw IllegalArgumentException("File position is too large")
        }
        val mask = when (pos){
            "a" -> 0x01000000
            "s" -> 0x01000000
            "r" -> 0x02000000
            "v" -> 0x04000000
            "n" -> 0x08000000
            else -> 0x0
        }
        return position.or(mask).toString(radix = 16)
    }
}