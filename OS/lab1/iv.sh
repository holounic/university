#!/bin/bash

if [ $PWD == $HOME ]
then
	echo $HOME
	exit 0
fi
echo $PWD
echo "ты дурак"
exit 1