package com.mpdbailey.nabu

data class ExceptionItem(val partOfSpeech: PartOfSpeech, val inflected : String, val baseForms : List<String>)
data class Detachment(val partOfSpeech: PartOfSpeech, val suffix : String, val ending : String)


class Morphology {

    /*
    Based on rules in Word Net morph.c
     */
    private val rulesOfDetachment = listOf(
        Detachment(PartOfSpeech.NOUN,"s",""),
        Detachment(PartOfSpeech.NOUN,"ses","s"),
        Detachment(PartOfSpeech.NOUN,"xes","x"),
        Detachment(PartOfSpeech.NOUN,"zes","z"),
        Detachment(PartOfSpeech.NOUN,"ches","ch"),
        Detachment(PartOfSpeech.NOUN,"shes","sh"),
        Detachment(PartOfSpeech.NOUN,"men","man"),
        Detachment(PartOfSpeech.NOUN,"ies","y"),
//Already done above
//        Detachment(PartOfSpeech.VERB,"s",""),
//        Detachment(PartOfSpeech.VERB,"ies","y"),
        Detachment(PartOfSpeech.VERB,"es","e"),
        Detachment(PartOfSpeech.VERB,"es",""),
        Detachment(PartOfSpeech.VERB,"ed","e"),
        Detachment(PartOfSpeech.VERB,"ed",""),
        Detachment(PartOfSpeech.VERB,"ing","e"),
        Detachment(PartOfSpeech.VERB,"ing",""),
        Detachment(PartOfSpeech.ADJECTIVE,"er",""),
        Detachment(PartOfSpeech.ADJECTIVE,"est",""),
        Detachment(PartOfSpeech.ADJECTIVE,"er","e"),
        Detachment(PartOfSpeech.ADJECTIVE,"est","e"))

    private fun findBase(inflected : String, rule : Detachment) : String?{
        //Special rule for ful
        //Remove ful, check detachment rules, if a match append ful back on the word
        //eg: boxesful -> boxful
        if (inflected.endsWith("ful")) {
            val noFul = inflected.substring(0,inflected.length-3)
            val baseNofUl = applyRule(noFul, rule)
            if (baseNofUl!=null){
                return baseNofUl + "ful"
            }
            return null
        }
        return applyRule(inflected,rule)
    }

    private fun applyRule(inflected: String, rule : Detachment) : String? {
        if (inflected.length<3){
            return null
        }
        if (inflected.endsWith(rule.suffix)) {
            //replace suffix with the new ending
            val baseLen = inflected.length - rule.suffix.length
            if (baseLen == 0){
                return null
            }
            return inflected.substring(0, baseLen) + rule.ending
        }
        return null
    }

    fun getWordBases(inflected : String) : List<String> = rulesOfDetachment
        .mapNotNull { findBase(inflected, it) }
        .distinct()

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