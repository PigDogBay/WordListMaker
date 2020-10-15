#!/bin/bash
#Install the french word list using
#sudo apt install aspell-fr
#
#The dictionary can be dumped out and all the declensions/conjugations expanded to create the base word list
#The conjugations/declensions are spearated by spaces so these need to be converted to newlines
#
#French uses hyphens a lot, eg hospitalo-universitaires, hospitalo does not exist as a single word but universitaires does
#tr '-' '\n' Splits hyphenated words up into separate words 
#
#GUTenberg (Aspell mistake?) is getting stripped out as an abbreviation. 
#
#Next I remove the acronyms, the regex checks if second or last letter is capitalized and if so removes it
#This will remove most acronyms including SuSE VMware CoE and leave words like McDonald untouched.
#- sed "s/match pattern/substitute string/g"
#- \<\w[A-Z]\w*\>
#-- \< start of word
#-- \w any word character
#-- [A-Z] any capital letter
#-- \w* any word character 0 or more times
#-- \> end of word
#- \<\w\+[A-Z]\>  = start of word + any letters + capital letter + end of word
#- /^$/d = this deletes the empty lines left by the removal of the acronym
#
# Now that the acronyms have been removed I can convert to lower case
# gawk tolower() converts accented letters to lower case as well as A-Z
#
# iconv converts accented chars to normal a-z chars
#
#French words are often repeated with prefixes d' l' qu' - these can all be removed
# sed "s/^.*'.*$//g" \    ^ start of line, .* any char 0 or more times, $ end of line
#
# sed delete function to remove words of 2 letters or less
#
# Sort the words by length and then alphabetically
#- gawk is used to print the length and word
#- sort field 1 numerically and reverse, field 2 alphabetically
#- cut uses space as a delimiter and selects only the second field to the end of the line
#- uniq removes repeated adjacent lines
#
# The two letter word lists is a based on the Aspell-fr list but each word has been checked by Google translate and Collins
# https://www.collinsdictionary.com/dictionary/french-english/nu
#
echo "Creating wordlist-fr.txt"
aspell -d fr dump master \
| aspell -l fr expand \
| tr ' ' '\n' \
| tr '-' '\n' \
| tr 'GUTenberg' 'gutenberg' \
| sed "s/\<\w[A-Z]\w*\>//g;s/\<\w\+[A-Z]\>//g;/^$/d" \
| gawk '{print tolower($0)}' \
| iconv -f utf8 -t ascii//TRANSLIT \
| sed "s/^.*'.*$//g" \
| sed "/^.\{,2\}\$/d" \
| gawk '{print length, $0}' \
| sort -k1,1nr -k2,2 \
| cut -d " " -f2- \
| uniq \
> wordlist-fr.txt
#append two letter words
echo $'ah\nai\nan\nas\nau\nbu\nca\nce\nci\nde\ndo\ndu\neh\nen\nes\net\neu\nfi\nha\nhe\nif\nil\nin\nje\nla\nle\nli\nlu\nma\nme\nmi\nmu\nne\nni\nnu\noh\non\nor\nos\nou\npu\nre\nri\nsa\nse\nsi\nsu\nta\nte\ntu\nun\nus\nva\nvu' >> wordlist-fr.txt
echo "Number of words "
wc -w wordlist-fr.txt