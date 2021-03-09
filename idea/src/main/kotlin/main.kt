import com.mpdbailey.scowl.*
import com.mpdbailey.utils.saveWordList
import com.mpdbailey.utils.removePunctuation

const val OUT_FILENAME = "../../out/idea.txt"
const val PHRASES_FILENAME = "../../out/phrases.txt"
const val WN_PHRASES_FILENAME = "../../out/wnphrases.txt"
const val WORDNET_FILENAME = "../../out/wordnet.txt"
const val NABU_FILENAME = "/Users/markbailey/work/MPDBTech/wordlist/out/nabu.db"
const val UKACD17_FILENAME = "../../wordlists/UKACD/UKACD17.TXT"

fun createNabuDb(){
    println("Creating Nabu database")
    val database = com.mpdbailey.nabu.Database(NABU_FILENAME)
    val combine = com.mpdbailey.nabu.Combine()
    println("Creating Db")
    database.create()
    println("Inserting Indices")
    database.insert(combine.indices())
    println("Inserting Definitions")
    database.insertSynonyms(combine.synonyms())
}

fun createScowl(){
    val words = createScowlWordList()
    println("Count: ${words.count()}")
    words.saveWordList(OUT_FILENAME)
    println("Validating - find any illegal words:")
    val badWordCount = validate(OUT_FILENAME)
    println("Found $badWordCount illegal words")
}

fun createPhrases(){
    val wordNet = WordNet().phrases()
    val ukacd = Ukacd(UKACD17_FILENAME).phrases()
    val combined = (wordNet + ukacd)
        .distinctBy {it.removePunctuation()  }
        .sortedWith(comparator.thenBy { it })
    println("Phrases count ${combined.count()}")
    combined.saveWordList(PHRASES_FILENAME)
}


fun main(args: Array<String>) {
//    createScowl()
//    createNabuDb()
    createPhrases()
}
