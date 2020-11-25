using System;
using System.Linq;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace WordListMaker
{
    class Program
    {
        const string ROOT_DIR = "../scowl/wordlist/scowl/final/";
        static void Main(string[] args)
        {
            Program p = new Program();
            //Console.WriteLine("Cleaning up aspell words");
            //p.createGerman();
            p.createAppWordList();
            //p.createSowpods();
            //p.createTwl();
            //p.createFrench();
            //p.createSpanish();
        }

        /*
        First create German word list, using
        aspell -d de dump master | aspell -l de expand | tr ' ' '\n' > aspell-de.txt
        The two letters word have been checked on Google German-English translator
        */
        void createGerman() {
            String[] twoWords = {"ab","an","am","da","du","eh","er","es","ex","im","in","ja","je","ob","oh","pi","qm","so","um","wo","zu"};
            var expandedAspell = loadUtf8("../wordlists/aspell-de.txt");
            var processed = expandedAspell
                .Where(s => s.Length>1 && Char.IsLower(s.ElementAt(1))) //Strip abbreviations
                .Where(s => Char.IsLower(s.Last())) //Strip abbreviations, eg CoE
                .Select(s => s.ToLower())
                .Select(s => convertGermanAccents(s)) //äöüß to ae, oe, ue, ss
                .Select(s => convertAccentedWord(s)) //Convert accented words to standard latin alphabet
                .Where(s => s.Length>2 && s.Length<13)             //Remove all 1 and 2 letter words and any words over 12 letters 
                .Union(twoWords)
                .Distinct()
                .OrderByDescending(s => s.Length)
                .ThenBy(s => s);
            
            checkWords(processed);
            save("../wordlist-net-de.txt", processed);
            System.Console.WriteLine("Word Count -- {0}",processed.Count());
        }
        void createFrench() {
            String[] twoWords = {"ah","ai","an","as","au","bu","ca","ce","ci","de","do","du","eh","en","es","et","eu","fi","ha","he","if","il","in","je","la","le","li","lu","ma","me","mi","mu","ne","ni","nu","oh","on","or","os","ou","pu","re","ri","sa","se","si","su","ta","te","tu","un","us","va","vu"};
            var expandedAspell = loadUtf8("../wordlists/aspell-fr.txt");
            var processed = expandedAspell
               .Where(s => s.Length>1 && Char.IsLower(s.ElementAt(1))) //Strip abbreviations
               .Where(s => Char.IsLower(s.Last())) //Strip abbreviations, eg CoE
               .Select(s => {                 //split up hyphenated words
                   if (s.Contains("-"))
                       return s.Split('-');
                   else
                       return new String[]{s};
               })
               .SelectMany(s => s)
               .Where(s=> !s.Contains('\''))  //Strip out words with apostrophes
               .Select(s => s.ToLower())
               .Select(s => convertAccentedWord(s)) //Convert accented words to standard latin alphabet
                .Where(s => s.Length>2 && s.Length<13)             //Remove all 1 and 2 letter words and any words over 12 letters 
               .Union(twoWords)
               .Distinct()
               .OrderByDescending(s => s.Length)
               .ThenBy(s => s);
            
            checkWords(processed);
            save("../wordlist-net-fr.txt", processed);
            System.Console.WriteLine("Word Count -- {0}",processed.Count());
        }
        void createSpanish() {
            String[] twoWords = {"ah","al","as","ay","ca","da","de","di","ea","eh","el","en","es","fe","fu","ha","he","ir","ja","je","ji","jo","la","le","lo","me","mi","na","ni","no","nu","oh","os","se","si","su","te","ti","tu","uf","un","va","ve","vi","ya","yo"};
            var expandedAspell = loadUtf8("../wordlists/aspell-es.txt");
            var processed = expandedAspell
               .Where(s => s.Length>1 && Char.IsLower(s.ElementAt(1))) //Strip abbreviations
               .Where(s => Char.IsLower(s.Last())) //Strip abbreviations, eg CoE
               .Select(s => s.ToLower())
               .Select(s => convertAccentedWord(s)) //Convert accented words to standard latin alphabet
                .Where(s => s.Length>2 && s.Length<11)             //Remove all 1 and 2 letter words and any words over 12 letters 
               .Union(twoWords)
               .Distinct()
               .OrderByDescending(s => s.Length)
               .ThenBy(s => s);
            
            checkWords(processed);
            save("../wordlist-net-es.txt", processed);
            System.Console.WriteLine("Word Count -- {0}",processed.Count());
        }


        void createAppWordList(){
            var sowpods = load("../wordlists/sowpods.txt");
            var scowl = loadWords(getProWordListNames());
            var combined = scowl.Union(sowpods);
            var processed = process(combined);
            checkWords(processed);
            save("../words.txt", processed);
            System.Console.WriteLine("Word Count -- {0}",processed.Count());
        }
        void createSowpods(){
            var sowpods = load("../wordlists/sowpods.txt");
            var processed = sowpods
                .Select(s => s.ToLower())
                .Except(ExtraWords.bannedWords)
                .Distinct()
                .OrderByDescending(s => s.Length)
                .ThenBy(s => s);
            checkWords(processed);
            save ("../sowpods.txt", processed);
            System.Console.WriteLine("SOWPODS Word Count -- {0}",processed.Count());
        }
        void createTwl(){
            var sowpods = load("../wordlists/twl06.txt");
            var processed = sowpods
                .Select(s => s.ToLower())
                .Except(ExtraWords.bannedWords)
                .Distinct()
                .OrderByDescending(s => s.Length)
                .ThenBy(s => s);
            checkWords(processed);
            save ("../twl.txt", processed);
            System.Console.WriteLine("TWL06 Word Count -- {0}",processed.Count());
        }
        void checkWords(IEnumerable<string> words){
            foreach (var w in words){
                if (isNotLetter(w)){
                    Console.WriteLine("Error found {0}",w);
                }
            }
        }


        void listForeignWords(){
            var words = loadWords(getProWordListNames())
                .Where(s => containsAccent(s));
            print(words);
            save("../foreign.txt",words);
            System.Console.WriteLine("Word Count {0}",words.Count());
        }

        //'ABCDEFGHIKLMNOPQRSTVWZabcdefghijklmnopqrstuvwxyzÅÖÜàáâäåçèéêëíîïñóôöøùúûü
        void showAllChars(){
            var words = loadWords(getProWordListNames())
                .Where(s => containsAccent(s));
            StringBuilder sb = new StringBuilder();
            foreach (String w in words){
                sb.Append(w);
            }
            var chars = sb.ToString().Distinct().OrderBy(c => c);
            foreach (char c in chars){
                Console.Write("{0}",c);
            }
            Console.WriteLine("\nNumber of chars is{0}",chars.Count());
            
        }
        void compareToOldLists(){
            var stdList = load("../standard.txt");
            var proList = load("../pro.txt");
            var newList = load("../words.txt");
            var missingFromNew = 
            stdList
                .Union(proList)
                .Except(newList);
            save("../missingFromNew.txt", missingFromNew);
            System.Console.WriteLine("Word Count {0}",missingFromNew.Count());
        }
        void compareNewToOldLists(){
            var stdList = load("../standard.txt");
            var proList = load("../pro.txt");
            var newList = load("../words.txt");
            var missingFromOld = 
            newList
                .Except(proList)
                .Except(stdList);
            save("../missingFromOld.txt", missingFromOld);
            System.Console.WriteLine("Word Count {0}",missingFromOld.Count());
        }

        void findWordsContaining(String word){
            var matches = process(loadWords(getProWordListNames()))
            .Where(s => s.Contains(word));
            print(matches);
            System.Console.WriteLine("Word Count {0}",matches.Count());
        }

        IEnumerable<String> process(IEnumerable<String> words){
            return words
                .Where(s => !s.Contains('\''))
                .Where(s => s.Length>2)             //Remove all 1 and 2 letter words 
                .Where(s => Char.IsLower(s.ElementAt(1))) //Strip abbreviations
                .Where(s => Char.IsLower(s.Last())) //Strip abbreviations, eg CoE
                .Union(ExtraWords.abbreviations) //add some abbreviations back
                .Union(ExtraWords.feedback) 
                .Union(ExtraWords.neoligism)
                .Union(ExtraWords.misc)
                .Union(ExtraWords.twoLetterWords)
                .Select(s => s.ToLower())
                .Except(ExtraWords.bannedWords)
                .Select(s => convertAccentedWord(s)) //Convert accented words to standard latin alphabet
                .Distinct()
                .OrderByDescending(s => s.Length)
                .ThenBy(s => s);
        }
        public void save(String filename, IEnumerable<String> words)
        {
            //Android default charset is UTF-8
            var utf8WithoutBom = new UTF8Encoding(false);
            using (StreamWriter writer = new StreamWriter(filename, false, utf8WithoutBom))
            {
                writer.NewLine = "\n";
                foreach(var word in words)
                {
                    writer.WriteLine(word);
                }
            }
        }
        void print(IEnumerable<String> names){
            foreach (var name in names){
                Console.WriteLine(name);
            }
        }

        List<String> loadWords(IEnumerable<String> wordLists) {
            List<String> words = new List<String>();
            foreach (var filename in wordLists){
                words.AddRange(load(ROOT_DIR+filename));
            }
            return words;
        }
        IEnumerable<String> getStdWordListNames(){
            return WordListNames.filenames
                .Where(s=> !s.EndsWith(".95"))
                .Where(s=> !s.EndsWith(".80"))
                .Where(s=> !s.Contains("abbreviations"))
                .Where(s=> !s.Contains("special"))
                .Where(s=> !s.Contains("contractions"));
        }
        IEnumerable<String> getProWordListNames(){
            return WordListNames.filenames
                .Where(s=> !s.EndsWith(".95"))
                .Where(s=> !s.Contains("abbreviations"))
                .Where(s=> !s.Contains("special"))
                .Where(s=> !s.Contains("contractions"));
        }
       
        //Word lists stored as ISO-8859-1, if you load as UTF-8 you get � for accented chars
        List<String> load(String filename)
        {
            List<String> words = new List<string>();

            //Accented words encoded using iso-8859-1
            using (StreamReader sr = new StreamReader(filename, Encoding.GetEncoding("iso-8859-1")))
            {
                while (sr.Peek() > 0)
                {
                    words.Add(sr.ReadLine());
                }
            }
            return words;
        }
        List<String> loadUtf8(String filename)
        {
            List<String> words = new List<string>();
            using (StreamReader sr = new StreamReader(filename, Encoding.UTF8))
            {
                while (sr.Peek() > 0)
                {
                    words.Add(sr.ReadLine());
                }
            }
            return words;
        }

        String convertAccentedWord(String word){
            return containsAccent(word) ? removeAccents(word) : word;
        }

        bool containsAccent(String word){
            return word.Any(c => c > 'z');
        }

        bool isNotLetter(String word){
            return word.Any(c => c<'a' || c>'z');
        }

        //These are the accents found in the word list
        //Note that German ä ö ü should be ae oe ue
        //
        //àáâäå -> a
        //ç     -> c
        //èéêë  -> e
        //íîï   -> i
        //ñ     -> n
        //óôöø  -> o
        //ùúûü  -> u
        String removeAccents(String word){
            return word
            .Replace('à','a')
            .Replace('á','a')
            .Replace('â','a')
            .Replace('ä','a')
            .Replace('å','a')
            .Replace('ç','c')
            .Replace('è','e')
            .Replace('é','e')
            .Replace('ê','e')
            .Replace('ë','e')
            .Replace('í','i')
            .Replace('î','i')
            .Replace('ï','i')
            .Replace('ñ','n')
            .Replace('ó','o')
            .Replace('ô','o')
            .Replace('ö','o')
            .Replace('ø','o')
            .Replace('ù','u')
            .Replace('ú','u')
            .Replace('û','u')
            .Replace('ü','u');
        }
        //äöüß to ae, oe, ue, ss
        String convertGermanAccents(String word){
            return word
            .Replace("ß","ss")
            .Replace("ä","ae")
            .Replace("ö","oe")
            .Replace("ü","ue");
        }

    }
}
