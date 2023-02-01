package com.mpdbailey.nabu


data class SynonymIndex(val word : String,
                        val indices : List<String>){
    operator fun plus(other : SynonymIndex) : SynonymIndex {
        val combine = (this.indices + other.indices).distinct()
        return SynonymIndex(word, combine)
    }
}

data class SynonymSet(val index : String,
                      val words : List<String>,
                      val associatedIndices : List<String>,
                      val partOfSpeech: PartOfSpeech = PartOfSpeech.NOUN,
                      val definitions : String = "")

enum class PartOfSpeech {
    NOUN, VERB, ADJECTIVE, ADVERB;
    companion object {
        fun from(letter: String) : PartOfSpeech = when (letter) {
            "n" -> NOUN
            "a", "s" -> ADJECTIVE
            "v" -> VERB
            "r" -> ADVERB
            else -> NOUN
        }
    }
}