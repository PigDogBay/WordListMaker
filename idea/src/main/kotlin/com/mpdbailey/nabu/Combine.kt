package com.mpdbailey.nabu

import wordnet.common.DbPartOfSpeech

class Combine {

    private fun load(dbPartOfSpeech: DbPartOfSpeech) : Sequence<SynonymIndex> {
        val adapter = Adapter()
        return Validation().loadIndices(dbPartOfSpeech)
            .map{adapter.convert(it)}
            .asSequence()
    }

    private fun loadDefinitions(dbPartOfSpeech: DbPartOfSpeech) : Sequence<SynonymSet> {
        val adapter = Adapter()
        return Validation().loadDefinitions(dbPartOfSpeech)
            .map{adapter.convert(it)}
            .asSequence()
    }

    fun displayIndices(word : String, partOfSpeech: DbPartOfSpeech){
        val synIndex = load(partOfSpeech)
            .first{ it.word == word}
        println("$word, $partOfSpeech, count = ${synIndex.indices.count()}\n${synIndex.indices.joinToString()}")
    }

    fun indices() : List<SynonymIndex>{
        val adj = load(DbPartOfSpeech.Adj)
        val adv = load(DbPartOfSpeech.Adv)
        val verb = load(DbPartOfSpeech.Verb)
        val noun = load(DbPartOfSpeech.Noun)

        return (adj + adv + noun + verb)
            .groupBy { it.word }
            .map{ g -> g.value.reduce { acc, next -> acc + next }}
    }


    fun synonyms() : List<SynonymSet> {
        val adj = loadDefinitions(DbPartOfSpeech.Adj)
        val adv = loadDefinitions(DbPartOfSpeech.Adv)
        val verb = loadDefinitions(DbPartOfSpeech.Verb)
        val noun = loadDefinitions(DbPartOfSpeech.Noun)
        return (noun+adj+verb+adv).toList()
    }
}