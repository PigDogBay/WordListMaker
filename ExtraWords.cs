using System;
using System.Linq;
using System.Collections.Generic;
using System.Text;

namespace WordListMaker
{

    public class ExtraWords {

        //TODO Scrabble two letter words only
        //TODO Foreign accented words that have been removed
        //TODO Supercali....
        public static List<String> misc = new List<String>(){
                "a", "i", "o",
                //From the old list, but missing in the new
                "deliminators","overclocking","bogotifying","deliminator","overclocked","bloatwares","bogometers","bogosities","bogotified","bogotifies","overclocks","superusers","bloatware","bogometer","glitching","overclock","superuser","bogosity","bogotify","copyleft","glitched","grepping","segfault","tarballs","modding","nagware","tarball","eccles","foobar","modded","app","apps","bro","bros","palaeethnologist","palaeethnologic","paleethnologist","paleethnologic"
        };
        //TWL + CSW words from
        //https://en.wikibooks.org/wiki/Scrabble/Two_Letter_Words
        public static List<String> twoLetterWords = new List<String>(){
                //TWL
                "aa","ab","ad","ae","ag","ah","ai","al","am","an","ar","as","at","aw","ax","ay","ba","be","bi","bo","by","da","de","do","ed","ef","eh","el","em","en","er","es","et","ex","fa","fe","ga","gi","go","gu","ha","he","hi","hm","ho","id","if","in","is","it","jo","ji","ka","ki","la","li","lo","ma","me","mi","mm","mo","mu","my","na","ne","no","nu","od","oe","of","oh","oi","om","on","op","or","os","ow","ox","oy","pa","pe","pi","po","qi","re","ra","sh","si","so","ta","te","ti","to","uh","um","un","up","us","ut","we","wo","xi","xu","ya","ye","yo","za",
                //CSW
                "ch","di","ea","ee","fy","gu","io","ja","ko","ky","ny","ob","oo","ou","ov","st","ug","ur","yu","zo"
        };
        public static List<String> abbreviations = new List<String>(){
                "ecmascript","gnu","ikea","nascar","norad","nvidia","sap","sars","sqlite","suse","ebay","emusic","ios","ipad","iphone","ipod","itunes","mudder","mudders","mudding","muddings","slapped","slapping","ufological","ufologist","ufologists","var","vars","waspier","waspiest","waspiness","waspinesses","waspish","waspishness","waspishnesses","ok","ebook","ebooks"
        };
        public static List<String> feedback = new List<String>(){
                 "rulings", "ecarte", "sashimi", "leangle", "neddy", "actioned", "nidor", "manspreading", "covfefe", "trenchcoat", "actioned", "hutia", "roedean", "onesie",
                 "twerk","twerking","twerks", "umbrellabird"
        };
        public static List<String> neoligism = new List<String>(){
                "onesie", "covfefe", "fatberg"
        };
        public static List<String> bannedWords = new List<String>(){
                "cunt","cunts",
                "motherfuckers","motherfucking","motherfucker","fistfucking","mindfucking","fistfucked","mindfucked","fistfucks","fuckfaces","fuckheads","mindfucks","fistfuck","fuckface","fuckhead","fuckings","fuckoffs",
                "mindfuck","fuckers","fucking","fuckoff","fuckups","fucked","fucker","fuckup","fucks","fuck",
                "nigger","niggers","niggerhead","niggerdom","niggered","niggering","niggerish","niggerism","niggerisms","niggerling","niggerlings","niggery",                
                "abo","abos","coon","coons","kraut","krauts","wop","wops","coonass","coonasses","wog","wogs","woggish",
                "bullshit","bullshits","bullshitted","bullshitting","shit","shits","shittier","shittiest","shitting","shitty","bullshitter","bullshitter","chickenshit","chickenshits","horseshit","shitfaced","shithead","shitheads","shitload","shitted","horseshits","shitwork","apeshit","dipshit","dipshits","gobshite","gobshites","ratshit","shiting","shitkicker","shitkickers","shitless","shitlist","shitlists","shitloads","shittiness","shite","shites",
                "wank","wanked","wanker","wankers","wanking","wanks",
                "cocksucker","cocksuckers",
                "piss","pissed","pisser","pisses","pissing",
                "dickhead","dickheads",
                "arsehole","arseholes","asshole","assholes"
        };
        
    }
}