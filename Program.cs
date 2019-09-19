using System;
using System.Linq;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace WordListMaker
{
    class Program
    {
        /*
            3 Sept 2018
            Pro Word Count -- 284923
            Std Word Count -- 132615

            Uses pre-built SCOWL from source forge, couldn't build github src code on Ubuntu 18.04 LTS or on the Mac
         */
        const string ROOT_DIR = "../scowl-src/scowl/final/";
        static void Main(string[] args)
        {
            Program p = new Program();
            p.createAppWordList();
            // p.createWordList("../pro.txt", p.getProWordListNames());
            // p.createWordList("../std.txt", p.getStdWordListNames());
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

        void createWordList(String name, IEnumerable<String> wordlists){
            var words = process(loadWords(wordlists));
            checkWords(words);
            save(name,words);
            System.Console.WriteLine("Word Count -- {0}",words.Count());
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

    }
}
