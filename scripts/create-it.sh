#!/bin/bash
#Due to conjugations / declensions there are many variations for each Italian word
#To reduce the size of the list to a reasonable level, the max word length is 9 letters (Wc of  252727)
#10 letters gives a word count of 455809
#
#Install the Italian word list using
#sudo apt install aspell-it
#
#The dictionary can be dumped out and all the declensions/conjugations expanded to create the base word list
#The conjugations/declensions are spearated by spaces so these need to be converted to newlines
#
#Remove Roman numerals
#Before splitting the lines up I can remove huge lines of Roman numerals
#sed "/[a-z\s]*iii[a-z\s]*/d"   - \s space, this looks for iii in a line
#
#Split lines up into  1 word per line
#tr ' ' '\n'
#
#Remove words with apostrtophes, look like compounded words
#sed "/^.*'.*$/d"   ^ start of line, .* any character 1 or more times, $ end of line
#
#Next I remove the acronyms, the regex checks if second or last letter is capitalized and if so removes it
#This will remove most acronyms including SuSE VMware CoE and leave words like McDonald untouched.
#- sed "/match pattern/d" - delete mataching pattern
#- \<\w[A-Z]\w*\>
#-- \< start of word
#-- \w any word character
#-- [A-Z] any capital letter
#-- \w* any word character 0 or more times
#-- \> end of word
#- \<\w\+[A-Z]\>  = start of word + any letters + capital letter + end of word
#
# Now that the acronyms have been removed I can convert to lower case
# gawk tolower() converts accented letters to lower case as well as A-Z
#
# iconv converts accented chars to normal a-z chars
#
# sed delete function to remove words of 2 letters or less and words with 11 letters or more
#
# Sort the words by length and then alphabetically
#- gawk is used to print the length and word
#- sort field 1 numerically and reverse, field 2 alphabetically
#- cut uses space as a delimiter and selects only the second field to the end of the line
#- uniq removes repeated adjacent lines
#
# The two letter word lists is a based on the Aspell-es list but each word has been checked by Google translate and Collins
# https://www.collinsdictionary.com/dictionary/italian-english/ciao
#

echo "Creating wordlist-it.txt"
aspell -d it dump master \
| aspell -l it expand \
| sed "/[a-z\s]*iii[a-z\s]*/d" \
| sed "/[a-z\s]*lxc[a-z\s]*/d" \
| sed "/[a-z\s]*xx[a-z\s]*/d" \
| tr ' ' '\n' \
| sed "/^.*'.*$/d" \
| sed "/^usa$/d;/^www$/d" \
| sed "/\<\w[A-Z]\w*\>/d;/\<\w\+[A-Z]\>/d" \
| gawk '{print tolower($0)}' \
| iconv -f utf8 -t ascii//TRANSLIT \
| sed "/^.\{,2\}\$/d;/^.\{10,\}/d" \
| gawk '{print length, $0}' \
| sort -k1,1nr -k2,2 \
| cut -d " " -f2- \
| uniq \
> wordlist-it.txt
#append two letter words
echo $'ad\nah\nal\nbe\nce\nci\nda\ndi\neh\nex\nfa\nfu\nha\nho\nil\nin\nio\nla\nle\nli\nlo\nma\nme\nne\nno\noh\nok\npo\nre\nsa\nse\nsi\nso\nsu\nte\nti\ntu\nun\nva\nve' >> wordlist-it.txt
echo "Number of words "
wc -w wordlist-it.txt
