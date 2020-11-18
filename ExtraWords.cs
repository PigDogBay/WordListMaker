using System;
using System.Linq;
using System.Collections.Generic;
using System.Text;

namespace WordListMaker
{

    public class ExtraWords {

        public static List<String> misc = new List<String>(){
                "a", "i", "o",
                //From the old list, but missing in the new
                "deliminators","overclocking","bogotifying","deliminator","overclocked","bloatwares","bogometers","bogosities","bogotified","bogotifies",
                "overclocks","superusers","bloatware","bogometer","glitching","overclock","superuser","bogosity","bogotify","copyleft",
                "glitched","grepping","segfault","tarballs","modding","nagware","tarball","eccles","foobar","modded","app","apps","bro","bros",
                "palaeethnologist","palaeethnologic","paleethnologist","paleethnologic","ubergeek"
        };
        //TWL + CSW words from
        //https://en.wikibooks.org/wiki/Scrabble/Two_Letter_Words
        public static List<String> twoLetterWords = new List<String>(){
                //TWL
                "aa","ab","ad","ae","ag","ah","ai","al","am","an","ar","as","at","aw","ax","ay",
                "ba","be","bi","bo","by",
                "da","de","do",
                "ed","ef","eh","el","em","en","er","es","et","ew","ex",
                "fa","fe",
                "gi","go",
                "ha","he","hi","hm","ho",
                "id","if","in","is","it",
                "jo",
                "ka","ki",
                "la","li","lo",
                "ma","me","mi","mm","mo","mu","my",
                "na","ne","no","nu",
                "od","oe","of","oh","oi","ok","om","on","op","or","os","ow","ox","oy",
                "pa","pe","pi","po",
                "qi",
                "re",
                "sh","si","so",
                "ta","te","ti","to",
                "uh","um","un","up","us","ut",
                "we","wo",
                "xi","xu",
                "ya","ye","yo",
                "za",
                //CSW
                "ch","di","ea","ee","fy","gu","io","ja","ko","ky","ny","ob","oo","ou","st","ug","ur","yu","zo"
        };
        public static List<String> abbreviations = new List<String>(){
                "ecmascript","gnu","ikea","nascar","norad","nvidia","sap","sars","sqlite","suse","ebay","emusic","ios","ipad","iphone","ipod","itunes","mudder","mudders","mudding","muddings","slapped","slapping","ufological","ufologist","ufologists","var","vars","waspier","waspiest","waspiness","waspinesses","waspish","waspishness","waspishnesses","ok","ebook","ebooks"
        };
        public static List<String> feedback = new List<String>(){
                 "rulings", "ecarte", "sashimi", "leangle", "neddy", "actioned", "nidor", "manspreading", "covfefe", "trenchcoat", "actioned", "hutia", "roedean", "onesie",
                 "twerk","twerking","twerks", "umbrellabird", "weei", "smokie", "zol", "piert", "goodfella", "goodfellas", "snowpea", "doner", "lusophone", "therm", "monetarise", 
                 "asgardia","asgardian", "cryptocurrency", "cryptocurrencies", "levidrome", "affordance", "affordances", "modularization", "interoperate", "interoperates", 
                 "compositionality","bicorporal", "ammanford", "quatford", "sleaford", "yarford", "valleyford", "ilford","bananaquit","jalfrezi","unpave"
        };
        public static List<String> neoligism = new List<String>(){
                "onesie", "covfefe", "fatberg"
        };
        public static List<String> bannedWords = new List<String>(){
                "cunt","cunts",
                "motherfuckers","motherfucking","motherfucker","fistfucking","mindfucking","fistfucked","mindfucked","fistfucks","fuckfaces","fuckheads",
                "mindfucks","fistfuck","mindfuck","headfuck","headfucks","fuckwit","fuckwits","starfucker","starfucking","starfuck","starfuckers","starfuckings",
                "fuckface","fuckhead","fuckings","fuckoffs","fuckers","fucking","fuckoff","fuckups","fucked","fucker","fuckup","fucks","fuck",
                "nigger","niggers","niggerhead","niggerheads","niggerdom","niggerdoms","niggered","niggering","niggerish","niggerism","niggerisms","niggerling","niggerlings","niggery",                
                "abo","abos","coon","coons","kraut","krauts","wop","wops","coonass","coonasses","wog","wogs","woggish","paki","pakis",
                "golliwogg","golliwoggs","golliwog","golliwogs","gollywog","gollywogs","gollywogg","gollywoggs","killawog","killawogs",
                "bullshit","bullshits","bullshitted","bullshitting","shit","shits","shittier","shittiest","shitting","shitty","bullshitter","bullshitter","chickenshit",
                "chickenshits","horseshit","shitfaced","shithead","shitheads","shitload","shitted","horseshits","shitwork","apeshit","dipshit","dipshits","gobshite",
                "gobshites","ratshit","shiting","shitkicker","shitkickers","shitless","shitlist","shitlists","shitloads","shittiness","shite","shites",
                "shited","shithole","shitholes","shittily","bullshittings","bullshitters","dumbshits","dumbshit",
                "wank","wanked","wanker","wankers","wanking","wanks","wankiest","wanksta","wankstas","wankier","wanky",
                "cocksucker","cocksuckers",
                "piss","pissed","pisser","pisses","pissing","pisshead","pissheads","pissers","pissant","pissants","pisspot","pisspots",
                "dickhead","dickheads",
                "arsehole","arseholes","asshole","assholes","arsed","arses","arsey","twat","twats","shittinesses"
        };
        
    }
}