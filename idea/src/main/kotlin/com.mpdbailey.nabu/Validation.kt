package com.mpdbailey.nabu

import wordnet.common.*
import java.io.File
import java.io.RandomAccessFile

class Validation {
    private val copyrightLineCount = 29

    fun validateAll(){
        validate(DbPartOfSpeech.Adv)
        validate(DbPartOfSpeech.Adj)
        validate(DbPartOfSpeech.Verb)
        validate(DbPartOfSpeech.Noun)
    }

    fun validate(dbPartOfSpeech: DbPartOfSpeech){
        val indices = loadIndices(dbPartOfSpeech)
        val definitions = loadDefinitions(dbPartOfSpeech)
        val posPass = checkPartOfSpeech(dbPartOfSpeech, indices)
        val defPosPass = checkDefPartOfSpeech(dbPartOfSpeech, definitions)
        val indexPass = checkIndices(indices,definitions)
        println("$dbPartOfSpeech ${indices.count()}, $posPass, $defPosPass, $indexPass")
    }

    fun loadIndices(dbPartOfSpeech: DbPartOfSpeech) : List<Index> =
        File(getDbFilePath(DbType.Index,dbPartOfSpeech))
            .readLines(Charsets.UTF_8)
            .drop(copyrightLineCount)
            .map { Index(it,0) }

    fun loadDefinitions(dbPartOfSpeech: DbPartOfSpeech) : List<Definition> =
        File(getDbFilePath(DbType.Data,dbPartOfSpeech))
            .readLines(Charsets.UTF_8)
            .drop(copyrightLineCount)
            .map { Definition("",it) }

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

    fun checkOffsets(dbPartOfSpeech: DbPartOfSpeech) : Int {
        val filename = getDbFilePath(DbType.Data, dbPartOfSpeech)
        var count = 0
        RandomAccessFile(filename,"r").use { fs ->
            for (i in 1..copyrightLineCount){
                fs.readLine()
            }
            do {
                val offset = fs.filePointer.toInt()
                val line = fs.readLine() ?: break
                val tokens = line.split(' ')
                count++
                if (tokens[0].toInt()!= offset) break
            } while (true)
        }
        return count
    }

}