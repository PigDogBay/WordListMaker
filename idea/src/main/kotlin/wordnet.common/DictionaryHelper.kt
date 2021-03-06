package wordnet.common

import java.io.RandomAccessFile
import kotlin.collections.ArrayList

fun readRecord(offset : Int, dbFileName : String) : String {
    RandomAccessFile(dbFileName,"r").use { fs ->
        fs.seek(offset.toLong())
        return fs.readLine()
    }
}

/**
 * Uses a binary search to find the file position of the line containing the keyword
 */
fun fastSearch(keyword : String, dbFileName : String) : Int {
    var diff = 42L
    RandomAccessFile(dbFileName,"r").use { fs ->
        var top = 0L
        var bottom = fs.length()
        var mid = (bottom - top)/2L
        do {
            fs.seek(mid-1)
            if (mid!=1L) {
                //Go to start of next line
                fs.readLine()
            }
            val currentPosition = fs.filePointer
            val line = fs.readLine() ?: break
            val key = line.trim().split(' ')[0]
            val comparison = keyword.compareTo(key)
            when {
                comparison > 0 -> {
                    top = mid
                    diff = (bottom-top)/2L
                    mid = top + diff
                }
                comparison < 0 -> {
                    bottom = mid
                    diff = (bottom-top)/2L
                    mid = top + diff
                }
                else -> {
                    return currentPosition.toInt()
                }
            }
        } while (diff!=0L)
    }
    return 0
}

fun getSearchSense(def : Definition, whichWord : Int) : Int {
    //Search sense is ignored in .Net, so not implemented here
    return 0
}
val comparator = compareByDescending<String> {it.length }
fun searchAll(word : String) : List<String>{
    return searchSynonyms(word,DbPartOfSpeech.Adj)
        .union(searchSynonyms(word,DbPartOfSpeech.Noun))
        .union(searchSynonyms(word,DbPartOfSpeech.Verb))
        .union(searchSynonyms(word,DbPartOfSpeech.Adv))
        .distinct()
        .sortedWith(comparator.thenBy { it })
}


fun searchSynonyms(word : String, dbPartOfSpeech: DbPartOfSpeech) : List<String> {
    val indexFile = getDbFilePath(DbType.Index,dbPartOfSpeech)
    val dataFile = getDbFilePath(DbType.Data,dbPartOfSpeech)
    val words = ArrayList<String>()
    val offset = fastSearch(word,indexFile)
    if (offset == 0) return words
    val indexRecord = readRecord(offset,indexFile)
    val index = Index(indexRecord,offset)
    index.synSetsOffsets.forEach {  synOffset ->
        val record = readRecord(synOffset,dataFile)
        val definition = Definition(word,record)
        words.addAll(definition.words)
        words.addAll(getAssociatedWords(definition))
    }
    return words.distinct()
}

fun getAssociatedWords(definition: Definition) : List<String> {
    val words = ArrayList<String>()
    for (i in 0 until definition.ptrCount) {
        val filename = getDbFilePath(DbType.Data, definition.ptrPartOfSpeech[i])
        val record = readRecord(definition.ptrOffsets[i], filename)
        val assDef = Definition("", record)
        words.addAll(assDef.words)
    }
    return words
}