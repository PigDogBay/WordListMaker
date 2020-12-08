#!/bin/bash

echo "German"
aspell -d de dump master | aspell -l de expand > raw-de.txt
wc raw-de.txt
echo "Spanish"
aspell -d es dump master | aspell -l es expand > raw-es.txt
wc raw-es.txt
echo "French"
aspell -d fr dump master | aspell -l fr expand > raw-fr.txt
wc raw-fr.txt
echo "Italian"
aspell -d it dump master | aspell -l it expand > raw-it.txt
wc raw-it.txt
echo "Portuguese"
aspell -d pt_PT dump master | aspell -l pt_PT expand > raw-pt.txt
wc raw-pt.txt
