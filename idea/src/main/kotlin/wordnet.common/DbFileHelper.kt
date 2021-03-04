package wordnet.common

import java.nio.file.Paths

fun getAllIndexFiles() : List<String> {
    return listOf(
        getDbFilePath(DbType.Index,DbPartOfSpeech.Adj),
        getDbFilePath(DbType.Index,DbPartOfSpeech.Adv),
        getDbFilePath(DbType.Index,DbPartOfSpeech.Noun),
        getDbFilePath(DbType.Index,DbPartOfSpeech.Verb),
    )
}

fun getAllDataFiles() : List<String> {
    return listOf(
        getDbFilePath(DbType.Data,DbPartOfSpeech.Adj),
        getDbFilePath(DbType.Data,DbPartOfSpeech.Adv),
        getDbFilePath(DbType.Data,DbPartOfSpeech.Noun),
        getDbFilePath(DbType.Data,DbPartOfSpeech.Verb),
    )
}

fun getDbFilePath(db : DbType, pos : DbPartOfSpeech) : String {
    val filename = "/Users/markbailey/work/MPDBTech/wordlist/wordlists/WordNet/dict3.1/${db.filename}.${pos.fileExtension}"
    return Paths.get(filename).toAbsolutePath().toString()
}