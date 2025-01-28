package wordnet.common

import wordNetLookup

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

    fun add(associatedWord : String, partOfSpeech: DbPartOfSpeech, assInd : Int = 0){
        val indexWord = associatedWord.lowercase().replace(' ','_')
        val helper = DbFileHelper()
        val indices = helper.loadIndices(partOfSpeech)
        val index = indices.first { it.word == indexWord }
        associates.add(AssociatedIndex(index.synSetsOffsets[assInd],partOfSpeech))
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

/*
Generates an extra definition for bail
copy the generated string to the end of index.noun
15400000 00 n 05 bail 0 wicket 0 stump 0 cricket_equipment 0 cricket 0 004 @ 04590155 n 0000 @ 03164306 n 0000 @ 03136912 n 0000 @ 00477400 n 0000 | One of the two wooden crosspieces that rest on top of the stumps to form a wicket

Update bail in index.noun:
    Add the syn offset
    Update both the sense count and offset count

    Changes marked with []
    bail n [3] 3 @ + ; [3] 0 13371991 05912039 [15400000]

 */
fun additionBail(){
    //Use this function to check which index you require for the matching meaning
    wordNetLookup("cricket")

    val addition = Additions()
    addition.words.addAll(arrayOf("bail","wicket","stump","cricket equipment","cricket"))
    addition.position = 15400000
    addition.partOfSpeech = DbPartOfSpeech.Noun
    addition.definitions = "One of the two wooden crosspieces that rest on top of the stumps to form a wicket"
    addition.add("wicket",DbPartOfSpeech.Noun,0)
    addition.add("stump",DbPartOfSpeech.Noun,3)
    addition.add("cricket equipment",DbPartOfSpeech.Noun,0)
    addition.add("cricket",DbPartOfSpeech.Noun,1)

    val raw = addition.generate()
    println(raw)
    val definition = Definition("bail",raw)
    displayDefinition(definition)

}