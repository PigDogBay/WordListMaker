import com.mpdbailey.nabu.Combine
import com.mpdbailey.nabu.Compressor
import com.mpdbailey.nabu.DatabaseLookup
import com.mpdbailey.scowl.*
import com.mpdbailey.utils.ResourceLoader
import com.mpdbailey.utils.saveWordList
import com.mpdbailey.utils.removeWordSeparators

const val OUT_FILENAME = "../../out/words.txt"
const val SMALL_FILENAME = "../../out/small.txt"
const val PHRASES_FILENAME = "../../out/phrases.txt"
const val NABU_FILENAME = "/Users/markbailey/work/MPDBTech/wordlist/out/nabu.db"
const val UKACD17_FILENAME = "../../wordlists/UKACD/UKACD17.TXT"
const val EXTRA_PHRASES = "/extraphrases.txt"

fun createNabuDb(){
    println("Creating Nabu database")
    val combine = Combine()
    val compressor = Compressor(combine.indices(),combine.synonyms())
    println("Compressing indices")
    val compIndices = compressor.compressedIndices
    println("Compressing sets")
    val compSets = compressor.compressedSynonymSets
    val database = com.mpdbailey.nabu.Database(NABU_FILENAME)
    println("Creating Db")
    database.create()
    println("Inserting Indices")
    database.insert(compIndices)
    println("Inserting Definitions")
    database.insertSynonyms(compSets)
}

fun createScowl(){
    val words = createScowlWordList()
    println("Count: ${words.count()}")
    words.saveWordList(OUT_FILENAME)
    println("Validating - find any illegal words:")
    val badWordCount = validate(OUT_FILENAME)
    println("Found $badWordCount illegal words")
}

fun createSmall(){
    val words = createSmallWordList()
    println("Count: ${words.count()}")
    words.saveWordList(SMALL_FILENAME)
    println("Validating - find any illegal words:")
    val badWordCount = validate(SMALL_FILENAME)
    println("Found $badWordCount illegal words")
}

fun createPhrases(){
    val wordNet = WordNet().phrases()
    val ukacd = Ukacd(UKACD17_FILENAME).phrases()
    val extraPhrases = ResourceLoader().load(EXTRA_PHRASES)

    val phrases = (wordNet + ukacd + extraPhrases)
        .map { it.toLowerCase() }
        .sortedWith(comparator.thenBy { it })
        .distinctBy {it.removeWordSeparators()}
    phrases.saveWordList(PHRASES_FILENAME)

    println("Invalid words found:")
    validate(PHRASES_FILENAME)
    println("\nSummary\n-------")
    println("WordNet Count: ${wordNet.count()}")
    println("UKACD Count: ${ukacd.count()}")
    println("Phrases Count: ${phrases.count()}")
}

fun dbLookup(query : String){
    val dbl = DatabaseLookup(NABU_FILENAME)
    val r = dbl.search(query).sortedBy { it.length }
    r.forEach { println("$it (${it.count()})") }
    println("Count ${r.count()}")
}

fun main(args: Array<String>) {
    //createNabuDb()
//    dbLookup("second hand")
//    createScowl()
//    createSmall()
    createPhrases()
}
