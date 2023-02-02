package com.mpdbailey.nabu

import wordnet.common.*
import java.io.BufferedReader
import java.io.File
import java.io.RandomAccessFile

class Validation {

    fun validateAll(){
        validate(DbPartOfSpeech.Adv)
        validate(DbPartOfSpeech.Adj)
        validate(DbPartOfSpeech.Verb)
        validate(DbPartOfSpeech.Noun)
    }

    fun validate(dbPartOfSpeech: DbPartOfSpeech){
        val dbFileHelper = DbFileHelper()
        val indices = dbFileHelper.loadIndices(dbPartOfSpeech)
        val definitions = dbFileHelper.loadDefinitions(dbPartOfSpeech)
        val posPass = checkPartOfSpeech(dbPartOfSpeech, indices)
        val defPosPass = checkDefPartOfSpeech(dbPartOfSpeech, definitions)
        val indexPass = checkIndices(indices,definitions)
        println("$dbPartOfSpeech ${indices.count()}, $posPass, $defPosPass, $indexPass")
    }

    private fun checkPartOfSpeech(dbPartOfSpeech: DbPartOfSpeech, indices : List<Index>) : Boolean{
        val letter = dbPartOfSpeech.letter
        val fails = indices.filter { it.partOfSpeech != letter }
        return fails.count() == 0
    }

    private fun checkDefPartOfSpeech(dbPartOfSpeech: DbPartOfSpeech, definitions: List<Definition>) : Boolean{
        val letter = dbPartOfSpeech.letter
        return definitions
            .filter {dbPartOfSpeech == DbPartOfSpeech.Adj && it.partOfSpeech != "s"}
            .filter { it.partOfSpeech != letter }
            .count() == 0
    }

    private fun checkIndices(indices: List<Index>, definitions : List<Definition>) : Boolean = indices
            .flatMap { it.synSetsOffsets }
            .filter { synOffset -> !definitions.any { it.position == synOffset  } }
            .count() == 0

}