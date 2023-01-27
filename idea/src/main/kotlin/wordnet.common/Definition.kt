package wordnet.common

data class AssociatedIndex(
    val offset : Int,
    val partOfSpeech: DbPartOfSpeech)

class Definition {
    //Byte index in the file
    val position : Int
    val definitionType: Int
    val fileNumber : Int
    val partOfSpeech : String
    val wordCount : Int
    val words = ArrayList<String>()
    val ptrCount : Int
    val ptrTypes = ArrayList<Int>()
    val ptrOffsets = ArrayList<Int>()
    val ptrPartOfSpeech = ArrayList<DbPartOfSpeech>()
    val ptrToFields = ArrayList<Int>()
    val ptrFromFields = ArrayList<Int>()
    val frameIds = ArrayList<Int>()
    val frameToFields = ArrayList<Int>()
    val definitionText : String
    var nextDefinition : Definition? = null
    var nextForm : Definition? = null
    var ptrList : Definition? = null
    val associatedIndices = ArrayList<AssociatedIndex>()

    /*
    Each line in the file represents 1 definition entry:
        "00448955 00 a
        01 close 1
        020 = 05091408 n 0000 + 05092779 n 0102 ! 00447582 a 0101 & 00449506 a 0000 & 00449787 a 0000 & 00449943 a 0000 & 00450164 a 0000 & 00450348 a 0000 & 00450678 a 0000 & 00450811 a 0000 & 00450958 a 0000 & 00451113 a 0000 & 00451366 a 0000 & 00451559 a 0000 & 00451696 a 0000 & 00451803 a 0000 & 00451923 a 0000 & 00452181 a 0000 & 00452303 a 0000 & 00452436 a 0000
        | at or within a short distance in space or time or having elements near each other; \"close to noon\"; \"how close are we to town?\"; \"a close formation of ships\"  "

    00448955: Byte Index of the start of the line, the index files use these offsets
    00: File number is a pointer to a file in the folder dbfiles. E.g. 00 points to dbfiles/adj.all, 36 to verb.creation, 43 to verb.weather
    a: Part of speech, a = adjective
    01 close 1: Number of words
    close 1: List of words, word(close) LexId(1)
    020: Associated word count
    = 05091408 n 0000: List of: Type (= ATTRIBUTE), File Offset(05091408), File Code (n data.noun), Lex To/From pointers (0000)
    |: Skipped
    Definition text
     */
    constructor(word : String, record : String) {
        val tokens = record
            .trim()
            .split(' ')
            .iterator()
        position = tokens.next().toInt()
        fileNumber = tokens.next().toInt()
        partOfSpeech = tokens.next()
        var tmpDefType = if (DbPartOfSpeech.from(partOfSpeech) == DbPartOfSpeech.Satellite) INDIRECT_ANT else DONT_KNOW
        wordCount = tokens.next().toInt(radix = 16)
        for (i in 1..wordCount){
            words.add(tokens.next().replace('_',' '))
            //skip lexId
            tokens.next()
        }

        ptrCount = tokens.next().toInt()
        var foundPert = false
        for (i in 1..ptrCount){
            val ptrIndex = Constants.pointerTypes.indexOf(tokens.next())
            ptrTypes.add(ptrIndex)
            val offset = tokens.next().toInt()
            val pos = DbPartOfSpeech.from(tokens.next())
            associatedIndices.add(AssociatedIndex(offset,pos))
            ptrOffsets.add(offset)
            ptrPartOfSpeech.add(pos)
            val lex = tokens.next()
            ptrFromFields.add(lex.substring(0,2).toInt(radix = 16))
            //Potential bug in C# code, but I fixed it here
            ptrToFields.add(lex.substring(2,4).toInt(radix = 16))
            if ((DbPartOfSpeech.isAdjective(partOfSpeech)) && (tmpDefType == DONT_KNOW)){
                if (ptrIndex == ANTPTR){
                    tmpDefType = DIRECT_ANT
                } else if ( ptrIndex == PERTPTR) {
                    foundPert = true
                }
            }
        }
        if ((DbPartOfSpeech.isAdjective(partOfSpeech)) && (tmpDefType == DONT_KNOW) && foundPert){
            tmpDefType = PERTAINY
        }
        definitionType = tmpDefType

        if (DbPartOfSpeech.isVerb(partOfSpeech)){
            val verbFrameCount = tokens.next().toInt()
            for (i in 1..verbFrameCount){
                tokens.next()
                frameIds.add(tokens.next().toInt())
                frameToFields.add(tokens.next().toInt(radix = 16))
            }
        }
        tokens.next()

        var builder = ""
        tokens.forEachRemaining{builder+= "$it " }
        definitionText = builder.trim()
    }
}