#!/bin/bash

echo "Creating SCOWL V2 source word list"


cd ../../SCOWLv2/wordlist

./scowl --db scowl.db word-list 85 A,B,C,D 1 --deaccent --wo-poses abbr --wo-pos-categories special,nonword,wordpart \
| awk '{ print length, $0 }' \
| sort -k1,1nr -k2,2 \
| cut -d' ' -f2- > scowlV2.txt
