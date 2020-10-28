#!/bin/bash

echo > tmp
IFS=$'\n'
for line in `ps ao pid,command | tail -n+2`
do
  	pid=`awk '{print $1}' <<< "$line"`
if [ -e /proc/$pid/io ] 
then
    echo $line | awk 'BEGIN { ORS="" } { $1 = $1"*"; print $0 }'
    cat /proc/$pid/io | awk '{ if ($1 == "read_bytes:") print "*"$2 }' 
fi
done >> tmp
sleep 1m
for line in `cat tmp | tail -n+2` 
do
  	pid=`echo $line | awk 'BEGIN { FS="*" } { print $1 }'`
  	upd=`cat /proc/$pid/io | awk ' { if ($1 == "read_bytes:") print $2 }'`
    echo $line | awk -v updated=$upd 'BEGIN { OFS="*"; FS="*" } { $NF=updated-$NF; print $0 }'
done | sort -t* -nrk 3 | head -n 3 | tr '*' ':'
rm tmp