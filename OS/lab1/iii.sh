#!/bin/bash

echo "nano vi links exit"
option=4
read option

if [ $option -gt 4 ]
then 
	echo "ты дурак"
fi

case $option in
1 )
nano
;;
2 )
vi
;;
3 )
links
;;
esac