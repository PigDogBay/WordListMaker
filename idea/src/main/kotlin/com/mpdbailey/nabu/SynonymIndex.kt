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
                      val associatedIndices : List<String>)
