#!/bin/bash

trash_folder=".trash"
trash_log=".trash.log"

[ -f "$1" ] ||
{
    echo "ты дурак, нет файла $1"
    exit 1
}

[ -d $trash_folder ] ||
{
    mkdir $trash_folder
}

cnt=`ls $trash_folder | wc -l`
((cnt++))

[ -f $trash_log ] || touch $trash_log

ln "$1" $trash_folder/$cnt && 
rm "$1" &&
echo `realpath "$1"` $cnt >> $trash_log