#!/bin/bash

string=""
sum=""
while [ "$string" != "q" ] 
do
	read string
	sum="$sum$string"
done

echo "$sum"