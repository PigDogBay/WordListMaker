#!/bin/bash
echo "Creating wordlist-de.txt"
aspell -d de dump master \
| aspell -l de expand \
| tr ' ' '\n' \
| sed "s/\<\w[A-Z]\w*\>//g;s/\<\w\+[A-Z]\>//g;/^$/d" \
| gawk '{print tolower($0)}' \
| sed "s/ä/ae/g; s/ö/oe/g; s/ü/ue/g; s/ß/ss/g" \
| iconv -f utf8 -t ascii//TRANSLIT \
| sed "/^.\{,2\}\$/d" \
| gawk '{print length, $0}' \
| sort -k1,1nr -k2,2 \
| cut -d " " -f2- \
| uniq \
> wordlist-de.txt
#append two letter words
echo $'ab\nam\nan\nda\ndu\neh\ner\nes\nex\nim\nin\nja\nje\nob\noh\npi\nqm\nso\num\nwo\nzu' >> wordlist-de.txt
