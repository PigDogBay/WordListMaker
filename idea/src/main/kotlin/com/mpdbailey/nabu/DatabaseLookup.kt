package com.mpdbailey.nabu

class DatabaseLookup(filename : String) {
    private val database = Database(filename)
    private fun lookup(ids : List<String>) : List<SynonymSet>{
        val direct = ids.mapNotNull {database.querySynonymSet(it)}
        val associated = direct
            .flatMap { it.associatedIndices }
            .filter { it.isNotEmpty() }
            .mapNotNull {database.querySynonymSet(it)}
        return direct+associated
    }

    fun search(word : String) : List<String> {
        val id = database.query(word)
        return lookup(id)
            .flatMap { it.words }
            .distinct()
    }

    fun display(definition: SynonymSet) : String {
        val builder = StringBuilder()
        builder.append("("+definition.partOfSpeech+")")
        builder.append(definition.words.joinToString(separator = ", "))
        builder.append("\n")
        builder.append(definition.definitions.replace("; ","\n"))
        builder.append("\n")
        return builder.toString()
    }

    fun getDefinitions(word : String) {
        //List of string indices that point to definition IDs
        val indices = database.query(word)
        //Get definitions for each index and group by part of speech
        val groupedSets = indices.mapNotNull {database.querySynonymSet(it)}
            .groupBy { it.partOfSpeech }
        //Sort keys (noun, verb, adj, adv) then sort definitions by the order that the id appears in indices
        groupedSets.keys
            .sortedBy { it.sortOrder }
            .forEach { pos ->
            println("$word ($pos)")
            println("-------------------------")
            groupedSets[pos]?.sortedBy { indices.indexOf(it.index)}?.forEach { def ->
                println(display(def))
            }
        }
    }
}