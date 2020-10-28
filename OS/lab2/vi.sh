#!/bin/bash

max_mem=0
max_pid=0
top=`top -n1 -bo +%MEM | sed 1,7d | head -1` 
for cur_pid in `ps -axo pid --no-headers`
 do
    cur_mem=$((`awk '{print $1}' /proc/$cur_pid/statm` + 0))
    if [ $max_mem -lt $cur_mem ]
    then
        max_mem=$cur_mem
        max_pid=$cur_pid
    fi
done
awk '{print "Top result: " $1}' <<< $top