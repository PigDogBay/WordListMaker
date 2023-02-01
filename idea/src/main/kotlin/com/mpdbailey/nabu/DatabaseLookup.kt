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

    fun getDefinitions(word : String) {
        val index = database.query(word)
        val sets = index.mapNotNull {database.querySynonymSet(it)}
        sets.forEach {println(it.definitions) }
    }
}