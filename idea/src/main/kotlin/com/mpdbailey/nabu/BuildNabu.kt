package com.mpdbailey.nabu

import com.mpdbailey.utils.ResourceLoader

/**
 * Builds the Nabu database and stores it as a file
 *
 * The code is a facade for the following process:
 *
 * 1) Load the 4 types of files (adjectives, adverbs, nouns and verbs) for the index and definition data
 * 2) Parse each line of data into Index/Definition objects
 * 3) Combine the 4 types into one output list for both Index and Definition
 * 4) Convert the raw data types, Index & Definition, into SynonymIndex and SynonymSet
 * 5) Fix up the index offsets based on the file type
 * 6) Compress the offsets from 8 chars to 3 chars
 * 7) Tidy up the index and synonymSet lists and remove any offensive entries
 * 8) Create the Nabu database table and store as a file
 * 9) Insert all the indices and synonym set data into the DB
 * 10) Add extra definitions from resources/ExtraDefinitions.json, see DbAdditions.kt
 */
class BuildNabu {

    /**
     * Build the Nabu database and store it at the specified filename
     */
    fun build(dbFileName : String){
        //Combine the noun, verb, adjective and adverb files
        //TO create unified list of indices and synonymSets
        val combine = Combine()
        val compressor = Compressor(combine.indices(),combine.synonyms())
        //Compress the definition offsets to 3 chars
        val compIndices = compressor.compressedIndices
        val compSets = compressor.compressedSynonymSets
        //Build the database
        val database = Database(dbFileName)
        database.create()
        database.insert(compIndices)
        database.insertSynonyms(compSets)
        database.insertExceptions(Morphology().loadAllExceptions())

        addExtraDefinitions(dbFileName,compressor.compressedIndex)
    }

    /**
     * Extra definitions are defined in resources/ExtraDefinitions.json
     */
    private fun addExtraDefinitions(dbFileName: String, compressedIndex: CompressedIndex){
        val additional = DbAdditions(dbFileName)
        val data = ResourceLoader().loadDefinitionGson("/ExtraDefinitions.json")
        data.forEach {
            //Set unique id for the definition
            it.synIndex = compressedIndex.next()
            additional.add(it)
        }
    }
}