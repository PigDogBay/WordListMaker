import java.io.File

const val SCOWL_DIR = "../../scowl/wordlist/scowl/final/"
const val SOWPODS = "../../wordlists/sowpods.txt"
const val BANNED_WORDS = "bannedwords.txt"
const val EXTRA_WORDS = "extrawords.txt"

val comparator = compareByDescending<String> {it.length }
val bannedWords = ResourceLoader().load(BANNED_WORDS)
val extraWords = ResourceLoader().load(EXTRA_WORDS)

fun createScowlWordList() : List<String> {
    return File(SCOWL_DIR).listFiles()                          //List all word files in the SCOWL output folder
        .filter { it.extension!="95" }                          //Remove certain files
        .filter { !it.name.contains("abbreviations") }
        .filter { !it.name.contains("special") }
        .filter { !it.name.contains("contractions") }
        .flatMap { it.readLines(Charsets.ISO_8859_1) }          //SCOWL lists stored as ISO_8859_1
        .union(loadWordList(SOWPODS))                           //Add words only found in SOWPODS
        .asSequence()
        .filter { !it.contains('\'' )}
        .filter{it.length>2}
        .filter{ it[1].isLowerCase()}                           //Remove abbreviations
        .filter { it.last().isLowerCase() }                     //Remove abbreviations
        .map{it.toLowerCase()}
        .map{it.removeAccents()}
        .minus(bannedWords)
        .toList()
        .union(extraWords)
        .sortedWith(comparator.thenBy { it })
        .distinct()
        .toList()
}

/**
 * Using normalizer didn't work
 * https://stackoverflow.com/questions/51731574/removing-accents-and-diacritics-in-kotlin
 */
fun String.removeAccents() : String = this
    .replace('à','a')
    .replace('á','a')
    .replace('â','a')
    .replace('ä','a')
    .replace('å','a')
    .replace('ç','c')
    .replace('è','e')
    .replace('é','e')
    .replace('ê','e')
    .replace('ë','e')
    .replace('í','i')
    .replace('î','i')
    .replace('ï','i')
    .replace('ñ','n')
    .replace('ó','o')
    .replace('ô','o')
    .replace('ö','o')
    .replace('ø','o')
    .replace('ù','u')
    .replace('ú','u')
    .replace('û','u')
    .replace('ü','u')

fun loadWordList(filename : String) : List<String> = File(filename).readLines(Charsets.ISO_8859_1)

fun List<String>.saveWordList(filename: String) {
    File(filename).printWriter().use { out ->
        forEach { out.println(it) }
    }
}
