package wordnet.common

const val DONT_KNOW = 0
const val DIRECT_ANT = 1 /* direct antonyms (cluster head) */
const val INDIRECT_ANT = 2 /* indrect antonyms (similar) */
const val PERTAINY = 3 /* no antonyms or similars (pertainyms) */
const val POS_NOUN = 1
const val POS_VERB = 2
const val POS_ADJ = 3
const val POS_ADV = 4
const val KEY_LEN = 1024
const val LINE_LEN = 1024 * 25

const val ANTPTR = 1 /* ! */
const val HYPERPTR = 2 /* @ */
const val HYPOPTR = 3 /* ~ */
const val ENTAILPTR = 4 /* * */
const val SIMPTR = 5 /* & */
const val ISMEMBERPTR = 6 /* #m */
const val ISSTUFFPTR = 7 /* #s */
const val ISPARTPTR = 8 /* #p */
const val HASMEMBERPTR = 9 /* %m */
const val HASSTUFFPTR = 10 /* %s */
const val HASPARTPTR = 11 /* %p */
const val MERONYM = 12 /* % (not valid in lexicographer file) */
const val HOLONYM = 13 /* # (not valid in lexicographer file) */
const val CAUSETO = 14 /* > */
const val PPLPTR = 15 /* < */
const val SEEALSOPTR = 16 /* ^ */
const val PERTPTR = 17 /* \ */
const val ATTRIBUTE = 18 /* = */
const val VERBGROUP = 19 /* $ */
const val DERIVATION = 20 /* + */
const val CLASSIFICATION = 21 /* ; */
const val CLASS = 22 /* - */
const val TOKENIZER = " |\n|\r"

fun getPartOfSpeech(code : String) : Int = when (code) {
    "n" -> POS_NOUN
    "a" -> POS_ADJ
    "v" -> POS_VERB
    "r" -> POS_ADV
    else -> -1
}

//TODO check AdjSat value in the C code
enum class DbPartOfSpeech(val value : Int) {
    All(0),Noun(1),Verb(2),Adj(3),Adv(4),Satellite(5),AdjSat(5);

    val fileExtension : String
        get() = when (this){
            Noun -> "noun"
            Adj -> "adj"
            Verb -> "verb"
            Adv -> "adv"
            else -> ""
        }

    val letter : String
        get() = when (this){
            Noun -> "n"
            Adj -> "a"
            Verb -> "v"
            Adv -> "r"
            else -> "x"
        }

    companion object {
        fun from(code: String): DbPartOfSpeech = when (code) {
                "n" -> Noun
                "a" -> Adj
                "v" -> Verb
                "s" -> Satellite
                "r" -> Adv
                else -> All
        }

        fun isAdjective(code : String) = code == "a" || code == "s"
        fun isVerb(code : String) = code == "v"
    }
}

enum class DbType(val value : Int) {
    Index(1),Data(2);

    val filename : String
        get() = when (this) {
            Index -> "index"
            Data -> "data"
        }
}

class Constants {
    companion object {
        //TODO remove use TOKENIZER above
        val tokenizer = charArrayOf(' ', '\n', '\r')
        val pointerTypes = arrayOf(
            "",                /* 0 not used */
            "!",            /* 1 ANTPTR */
            "@",            /* 2 HYPERPTR */
            "~",            /* 3 HYPOPTR */
            "*",            /* 4 ENTAILPTR */
            "&",            /* 5 SIMPTR */
            "#m",            /* 6 ISMEMBERPTR */
            "#s",            /* 7 ISSTUFFPTR */
            "#p",            /* 8 ISPARTPTR */
            "%m",            /* 9 HASMEMBERPTR */
            "%s",            /* 10 HASSTUFFPTR */
            "%p",            /* 11 HASPARTPTR */
            "%",            /* 12 MERONYM */
            "#",            /* 13 HOLONYM */
            ">",            /* 14 CAUSETO */
            "<",            /* 15 PPLPTR */
            "^",            /* 16 SEEALSO */
            "\\",            /* 17 PERTPTR */
            "=",            /* 18 ATTRIBUTE */
            "$",            /* 19 VERBGROUP */
            "+",            /* 20 NOMINALIZATIONS */
            ";",            /* 21 CLASSIFICATION */
            "-",            /* 22 CLASS */
            /* additional searches, but not pointers.  */
            "",                /* SYNS */
            "",                /* FREQ */
            "+",            /* FRAMES */
            "",                /* COORDS */
            "",                /* RELATIVES */
            "",                /* HMERONYM */
            "",                /* HHOLONYM */
            "",                /* WNGREP */
            "",                /* OVERVIEW */
            ";c",            /* CLASSIF_CATEGORY */
            ";u",            /* CLASSIF_USAGE */
            ";r",            /* CLASSIF_REGIONAL */
            "-c",            /* CLASS_CATEGORY */
            "-u",            /* CLASS_USAGE */
            "-r",            /* CLASS_REGIONAL */
            "@i",            /* INSTANCE */
            "~i")            /* INSTANCES */
    }
}