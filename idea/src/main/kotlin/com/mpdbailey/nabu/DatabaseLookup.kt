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
        val index = database.query(word)
        val groupedSets = index.mapNotNull {database.querySynonymSet(it)}
            .groupBy { it.partOfSpeech }
        groupedSets.keys.forEach { pos ->
            println("$word ($pos)")
            println("-------------------------")
            groupedSets[pos]?.sortedBy { index.indexOf(it.index)}?.forEach { def ->
                println(display(def))
            }
        }
    }
}