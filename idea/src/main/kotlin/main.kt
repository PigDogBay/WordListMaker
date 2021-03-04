import com.mpdbailey.scowl.createScowlWordList
import com.mpdbailey.scowl.saveWordList
import com.mpdbailey.scowl.validate

const val OUT_FILENAME = "../../out/idea.txt"
const val NABU_FILENAME = "/Users/markbailey/work/MPDBTech/wordlist/out/nabu.db"

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


fun main(args: Array<String>) {
    createScowl()
//    createNabuDb()
}
