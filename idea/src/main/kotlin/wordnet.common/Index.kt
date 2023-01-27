package wordnet.common

/**
 * Parses an index structure from the specified file at the specified offset
 *
 * @param idxOffset The offset in the file at which the index exists</param>
 * @param record The full path to the database file to open</param>
 * @return A populated index structure in successful; otherwise an empty index structure</returns>
 *
 * Example
 *
 *  idxOffset: 148117 (file: Index.adj)
 *  record: "close a 15 4 ! & = + 15 7 00448955 00453543 00446553 00310743 00022256 00893915 00560964 02519542 02240484 02007394 01865308 01450828 01439442 01116967 00502916  "
 *
 *    close : Word
 *    a : Part of Speech, a = adjective
 *    15 : Sense Count
 *    4 : Pointers Used Count
 *    ! & = + : Pointer types (ANTPTR, SIMPTR, ATTRIBUTE, NORMALIZATIONS)
 *    15 : Offset count
 *    7 : Tagged Senses Count
 *    00448955 00453543 00446553 00310743 00022256 00893915 00560964 02519542 02240484 02007394 01865308 01450828 01439442 01116967 00502916 : Syn Set Offsets
 *
 *
 *    Note that the ordering of the Offsets represents the 'Sense', so 00448955 = 1, 00502916 = 15
 *    When displaying definitions, they are ordered by sense.
 */
class Index(record: String, val idxOffset: Int) {
    val word : String
    val partOfSpeech : String
    val senseCount : Int
    val offsetCount : Int
    val taggedSensesCount : Int
    val synSetsOffsets = ArrayList<Int>()
    val pointersUsedCount : Int
    val pointersUsed = ArrayList<Int>()

    init {
        val tokens = record
            .trim()
            .split(' ')
            .iterator()
        word = tokens.next()
        partOfSpeech = tokens.next()
        senseCount = tokens.next().toInt()
        pointersUsedCount = tokens.next().toInt()
        for (i in 1..pointersUsedCount){
            pointersUsed.add(Constants.pointerTypes.indexOf(tokens.next()))
        }
        offsetCount = tokens.next().toInt()
        taggedSensesCount = tokens.next().toInt()
        for (i in 1..offsetCount){
            synSetsOffsets.add(tokens.next().toInt())
        }
    }
}