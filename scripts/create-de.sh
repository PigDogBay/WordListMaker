#!/bin/bash
#Install the german word list using
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
# Convert äöüß to ae, oe, ue, ss using sed's substition function
#
# iconv converts accented chars to normal a-z chars
#
# sed delete function to remove words of 2 letters or less and words with 13 letters or more
#
# Sort the words by length and then alphabetically
#- gawk is used to print the length and word
#- sort field 1 numerically and reverse, field 2 alphabetically
#- cut uses space as a delimiter and selects only the second field to the end of the line
#- uniq removes repeated adjacent lines
#
# sed "/\<gst\>/d" Removes gst from the word list, this is an abbreviation (Gst)
#
# The two letter word lists is a based on the Aspell-de list but each word has been checked by Google translate
	
echo "Creating wordlist-de.txt"
aspell -d de dump master \
| aspell -l de expand \
| tr ' ' '\n' \
| sed "s/\<\w[A-Z]\w*\>//g;s/\<\w\+[A-Z]\>//g;/^$/d" \
| gawk '{print tolower($0)}' \
| sed "s/ä/ae/g; s/ö/oe/g; s/ü/ue/g; s/ß/ss/g" \
| iconv -f utf8 -t ascii//TRANSLIT \
| sed "/^.\{,2\}\$/d;/^.\{13,\}/d" \
| gawk '{print length, $0}' \
| sort -k1,1nr -k2,2 \
| cut -d " " -f2- \
| uniq \
| sed "/\<gst\>/d" \
> wordlist-de.txt
#append two letter words
echo $'ab\nam\nan\nda\ndu\neh\nei\ner\nes\nex\nim\nin\nja\nje\nob\noh\npi\nqm\nso\num\nwo\nzu' >> wordlist-de.txt
echo "Number of words "
wc -w wordlist-de.txt
