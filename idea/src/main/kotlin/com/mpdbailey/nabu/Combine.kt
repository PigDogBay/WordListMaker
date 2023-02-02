package com.mpdbailey.nabu

import wordnet.common.DbFileHelper
import wordnet.common.DbPartOfSpeech

/**
 * Loads the index and data files (see resources/wordnet/)
 * The files are split into 4 parts, adjectives, adverbs, verbs and nouns
 * These files are combined into 1 list
 */
class Combine {

    private fun load(dbPartOfSpeech: DbPartOfSpeech) : Sequence<SynonymIndex> {
        val adapter = Adapter()
        return DbFileHelper().loadIndices(dbPartOfSpeech)
            .map{adapter.convert(it)}
            .asSequence()
    }

    private fun loadDefinitions(dbPartOfSpeech: DbPartOfSpeech) : Sequence<SynonymSet> {
        val adapter = Adapter()
        return DbFileHelper().loadDefinitions(dbPartOfSpeech)
            .map{adapter.convert(it)}
            .asSequence()
    }

    fun displayIndices(word : String, partOfSpeech: DbPartOfSpeech){
        val synIndex = load(partOfSpeech)
            .first{ it.word == word}
        println("$word, $partOfSpeech, count = ${synIndex.indices.count()}\n${synIndex.indices.joinToString()}")
    }

    /**
     * Load the all the index files and combine them into one list
     */
    fun indices() : List<SynonymIndex>{
        val adj = load(DbPartOfSpeech.Adj)
        val adv = load(DbPartOfSpeech.Adv)
        val verb = load(DbPartOfSpeech.Verb)
        val noun = load(DbPartOfSpeech.Noun)

        return (adj + adv + noun + verb)
            .groupBy { it.word }
            .map{ g -> g.value.reduce { acc, next -> acc + next }}
    }


    /**
     * Load the all the data files and combine them into one list
     */
    fun synonyms() : List<SynonymSet> {
        val adj = loadDefinitions(DbPartOfSpeech.Adj)
        val adv = loadDefinitions(DbPartOfSpeech.Adv)
        val verb = loadDefinitions(DbPartOfSpeech.Verb)
        val noun = loadDefinitions(DbPartOfSpeech.Noun)
        return (noun+adj+verb+adv).toList()
    }
}