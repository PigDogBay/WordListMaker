#!/bin/bash
#Install the Portugese word list using
#sudo apt install aspell-pt
#
#The dictionary can be dumped out and all the declensions/conjugations expanded to create the base word list
#The conjugations/declensions are spearated by spaces so these need to be converted to newlines
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
# sed delete function to remove words of 2 letters or less and words with 11 letters or more
#
# Sort the words by length and then alphabetically
#- gawk is used to print the length and word
#- sort field 1 numerically and reverse, field 2 alphabetically
#- cut uses space as a delimiter and selects only the second field to the end of the line
#- uniq removes repeated adjacent lines
#
# The two letter word lists is a based on the Aspell-pt list but each word has been checked by Google translate and Collins
# https://www.collinsdictionary.com/dictionary/portuguese-english/ola
#
echo "Creating wordlist-pt.txt"
aspell -d pt_PT dump master \
| aspell -l pt_PT expand \
| tr ' ' '\n' \
| tr '-' '\n' \
| sed "/\<\(\|xi\|xii\|xiii\|xiv\|xv\|xvi\|xvii\|xviii\|xix\|xx\|xxi\|xxii\|xxiii\|xxiv\|xxv\|xxvi\|xxvii\|xxviii\|xxix\|xxx\|xis\|vii\|viii\|iii\)\>/d" \
| sed "/\<\w[A-Z]\w*\>/d;/\<\w\+[A-Z]\>/d" \
| gawk '{print tolower($0)}' \
| iconv -f utf8 -t ascii//TRANSLIT \
| sed "/^.\{,2\}\$/d;/^.\{11,\}/d" \
| gawk '{print length, $0}' \
| sort -k1,1nr -k2,2 \
| cut -d " " -f2- \
| uniq \
> wordlist-pt.txt
#append two letter words
echo $'ah\nai\nao\nar\nas\nca\ncu\nda\nde\ndo\nem\neu\nfa\nha\nia\nis\nja\nla\nlo\nma\nna\nne\nno\nnu\noh\nos\nou\npa\npe\npo\nra\nre\nri\nse\nta\nte\nti\ntu\num\nva\nvi\nxa' >> wordlist-pt.txt
echo "Number of words "
wc -w wordlist-pt.txt
