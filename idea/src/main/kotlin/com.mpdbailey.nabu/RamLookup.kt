package com.mpdbailey.nabu

class RamLookup(private val indices : List<SynonymIndex>, private val synonyms : List<SynonymSet>) {

    private fun lookup(synonymIndex: SynonymIndex) : List<SynonymSet>{
        //direct indexed sets
        val direct = synonymIndex.indices
            .map{i -> synonyms.first {s-> i == s.index }}

        //associated sets
        val associated = direct
            .flatMap{ it.associatedIndices}
            .map{i -> synonyms.first {s-> i == s.index }}
        return direct + associated
    }

    fun search(word : String) : List<String> {
       val index =  indices.first { word == it.word }
        return lookup(index)
            .flatMap { it.words }
            .distinct()
    }

}