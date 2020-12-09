#!/bin/bash
#TODO - need to remove taboo words
#
#Install the English word list using
#sudo apt-get install aspell-de
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
# Remove words with apostrophes
#
# iconv converts accented chars to normal a-z chars
#
# sed delete function to remove words of 2 letters or less
#
# Sort the words by length and then alphabetically
#- gawk is used to print the length and word
#- sort field 1 numerically and reverse, field 2 alphabetically
#- cut uses space as a delimiter and selects only the second field to the end of the line
#- uniq removes repeated adjacent lines
#
# The two letter word lists is a based on the Aspell-de list but each word has been checked by Google translate
echo "Creating wordlist-en.txt"
aspell -d en dump master \
| aspell -l en expand \
| tr ' ' '\n' \
| sed "s/\<\w[A-Z]\w*\>//g;s/\<\w\+[A-Z]\>//g;/^$/d" \
| gawk '{print tolower($0)}' \
| iconv -f utf8 -t ascii//TRANSLIT \
| sed "/'/d" \
| sed "/^.\{,2\}\$/d" \
| gawk '{print length, $0}' \
| sort -k1,1nr -k2,2 \
| cut -d " " -f2- \
| uniq \
> wordlist-en.txt
#append two letter words (need ordering)
echo $'aa\nab\nad\nae\nag\nah\nai\nal\nam\nan\nar\nas\nat\naw\nax\nay\nba\nbe\nbi\nbo\nby\nda\nde\ndo\ned\nef\neh\nel\nem\nen\ner\nes\net\new\nex\nfa\nfe\ngi\ngo\nha\nhe\nhi\nhm\nho\nid\nif\nin\nis\nit\njo\nka\nki\nla\nli\nlo\nma\nme\nmi\nmm\nmo\nmu\nmy\nna\nne\nno\nnu\nod\noe\nof\noh\noi\nok\nom\non\nop\nor\nos\now\nox\noy\npa\npe\npi\npo\nqi\nre\nsh\nsi\nso\nta\nte\nti\nto\nuh\num\nun\nup\nus\nut\nwe\nwo\nxi\nxu\nya\nye\nyo\nza\nch\ndi\nea\nee\nfy\ngu\nio\nja\nko\nky\nny\nob\noo\nou\nst\nug\nur\nyu\nzo' >> wordlist-en.txt

