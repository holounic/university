#!/bin/bash

dest="emails.lst"
dir="/etc"
touch $dest

grep '[[:alnum:]_]+@[[:alnum:]]+\.[[:alnum:]]+' -r --binary-files=text $dir -h >> $dest