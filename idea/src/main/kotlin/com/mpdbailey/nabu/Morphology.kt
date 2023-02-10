package com.mpdbailey.nabu

data class ExceptionItem(val partOfSpeech: PartOfSpeech, val inflected : String, val baseForms : List<String>)

class Morphology {

    fun loadAllExceptions() : List<ExceptionItem> =
        PartOfSpeech.values().flatMap { loadExceptionList(it) }

    private fun getExceptionListName(pos : PartOfSpeech) : String  = when(pos) {
            PartOfSpeech.NOUN -> "/wordnet/noun.exc"
            PartOfSpeech.VERB -> "/wordnet/verb.exc"
            PartOfSpeech.ADJECTIVE -> "/wordnet/adj.exc"
            PartOfSpeech.ADVERB -> "/wordnet/adv.exc"}

    private fun loadExceptionList(partOfSpeech: PartOfSpeech) : List<ExceptionItem> =
        this.javaClass.getResourceAsStream(getExceptionListName(partOfSpeech))
            .bufferedReader(Charsets.UTF_8)
            .readLines()
            .map { parseException(partOfSpeech, it) }

    private fun parseException(pos : PartOfSpeech, line : String) : ExceptionItem {
        val tokens = line
            .split(' ')
            .map {it.replace('-',' ')}
            .map {it.replace('_',' ')}
            .map {it.replace(".","")}
            .map {it.replace("'","")}
        return ExceptionItem(pos,tokens[0],tokens.drop(1))
    }
}