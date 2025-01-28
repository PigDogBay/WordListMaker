package wordnet.common

import java.util.Dictionary

/**
 * Tools to create new definition and index strings for adding to the index and data files
 */
class Additions {
    var position = 15400000
    var partOfSpeech = DbPartOfSpeech.Noun
    var words = mutableListOf<String>()
    var associates = mutableListOf<AssociatedIndex>()
    var definitions = "def 1; def2"

    //Not used
    private val fileNumber = "00"
    private val lexId = "0"
    private val ptrType = "@"
    private val lex = "0000"

    fun generate() : String {
        return "%08d ".format(position) +
                "$fileNumber ${partOfSpeech.letter} "+
                "%02x ".format(words.size) +
                generateWordString() +
                "%03d ".format(associates.size) +
                generateAssociated() + " | " +
                definitions
    }

    fun add(associatedWord : String, partOfSpeech: DbPartOfSpeech){
        val indexWord = associatedWord.lowercase().replace(' ','_')
        val helper = DbFileHelper()
        val indices = helper.loadIndices(partOfSpeech)
        val index = indices.first { it.word == indexWord }
        associates.add(AssociatedIndex(index.synSetsOffsets.first(),partOfSpeech))
    }

    private fun generateWordString() : String {
        return words.fold("") { acc, s -> "$acc${s.replace(' ', '_')} $lexId " }
    }

    private fun generate(assoc : AssociatedIndex) : String {
        return "$ptrType "+"%08d".format(assoc.offset)+" ${assoc.partOfSpeech.letter} $lex"
    }

    private fun generateAssociated() : String {
        return associates
            .map { generate(it) }
            .reduce{acc, s -> "$acc $s" }
    }
}