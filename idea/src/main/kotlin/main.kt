import com.mpdbailey.nabu.BuildNabu
import com.mpdbailey.nabu.DatabaseLookup
import com.mpdbailey.scowl.*
import com.mpdbailey.scowl.comparator
import com.mpdbailey.utils.ResourceLoader
import com.mpdbailey.utils.saveWordList
import com.mpdbailey.utils.removeWordSeparators
import wordnet.common.*
import java.io.File

const val OUT_FILENAME = "../../out/words.txt"
const val OUT_FILENAME_V2 = "../../out/words2.txt"
const val SMALL_FILENAME = "../../out/small.txt"
const val PHRASES_FILENAME = "../../out/phrases.txt"
const val NABU_FILENAME = "/Users/markbailey/work/MPDBTech/wordlist/out/nabu.db"
const val UKACD17 = "/source/ukacd17.txt"
const val SCHOLAR_FILENAME = "../../out/scholar.txt"
const val EXTRA_PHRASES = "/extraphrases.txt"
const val ACTORS = "/actors.txt"
const val POLITICIANS = "/politicians.txt"

fun createScowl(){
    println("Creating SCOWL Original large word list")
    val words = createScowlWordList()
    println("Count: ${words.count()}")
    words.saveWordList(OUT_FILENAME)
    println("Validating - find any illegal words:")
    val badWordCount = validate(OUT_FILENAME)
    println("Found $badWordCount illegal words")
}

/*
Create word list using
./scowl --db scowl.db word-list 85 A,B,C,D 1 --deaccent --wo-poses abbr --wo-pos-categories special,nonword,wordpart > scowlV2.txt


To subtract the word lists, (words2 - words)
NR = number of records read, resets to 1 when starts a new file
FNR = total number of records read
So NR equals FNR when reading words.txt
{exclude[$0]; next} loads all the words into hashtable called exclude
When it starts reading words2.txt
NR!=FNR, so !{$0 in exclude} will exclude any word found in the hashtable

awk 'NR==FNR {exclude[$0]; next} !($0 in exclude)' words.txt words2.txt >sub.txt

 */
fun createScowlV2(){
    println("Creating SCOWL v2 large word list")
    val words = createScowlV2WordList()
    println("Count: ${words.count()}")
    words.saveWordList(OUT_FILENAME_V2)
    println("Validating - find any illegal words:")
    val badWordCount = validate(OUT_FILENAME_V2)
    println("Found $badWordCount illegal words")
}

fun createScholar(){
    val words = createScowlWordList()
        .filter { it.count()<11 }
    println("Count: ${words.count()}")
    words.saveWordList(SCHOLAR_FILENAME)
    println("Validating - find any illegal words:")
    val badWordCount = validate(SCHOLAR_FILENAME)
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
    val ukacd = Ukacd("").phrases(ResourceLoader().load(UKACD17))
    val actors = Ukacd("").phrases(ResourceLoader().load(ACTORS))
    val politicians = Ukacd("").phrases(ResourceLoader().load(POLITICIANS))
    val extraPhrases = ResourceLoader().load(EXTRA_PHRASES)

    val phrases = (wordNet + ukacd + extraPhrases + actors + politicians)
        .map { it.lowercase() }
        //Sort hyphens and spaces as being the same otherwise 'close-range' comes after 'close shave'
        .sortedWith(comparator.thenBy { it.replace("-"," ") })
        .distinctBy {it.removeWordSeparators()}
    phrases.saveWordList(PHRASES_FILENAME)

    println("Invalid words found:")
    validate(PHRASES_FILENAME)
    println("\nSummary\n-------")
    println("WordNet Count: ${wordNet.count()}")
    println("Actors Count: ${actors.count()}")
    println("UKACD Count: ${ukacd.count()}")
    println("Phrases Count: ${phrases.count()}")
}

fun dbLookup(query : String){
    println("Looking up $query")
    val dbl = DatabaseLookup(NABU_FILENAME)
    dbl.getDefinitions(query)
//    val r = dbl.search(query).sortedBy { it.length }
//    r.forEach { println("$it (${it.count()})") }
//    println("Count ${r.count()}")
}

fun wordNetLookup(word : String){
    val grouped = getDefinition(word)
        .flatMap { it.value }
        .groupBy {  mbDisplayPartOfSpeech(it.partOfSpeech) }
    grouped.keys.forEach { pos ->
        println("$word ($pos)")
        println("-------------------------")
        grouped[pos]?.sortedBy { it.sense }?.forEach {def ->
            println("%08d".format(def.position))
            println(displayDefinition(def))
        }
    }
}

fun nabuStatus(){
    val file = File(NABU_FILENAME)

    val dbl = DatabaseLookup(NABU_FILENAME)
    println("Nabu Status")
    println("File size: ${file.length()} bytes, ${file.length()/(1024*1024)}Mb")
    println("Table Counts: ${dbl.indicesCount} indices, ${dbl.definitionsCount} definitions, ${dbl.exceptionsCount} exceptions")

 //   dbl.getDefinitions("grift")
}


fun main(args: Array<String>) {
//    createScholar()
//    BuildNabu().build(NABU_FILENAME)
//    nabuStatus()
//    createScowl()
//    createSmall()
//    createPhrases()

    createScowlV2()

}
