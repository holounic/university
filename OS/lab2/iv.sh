#!/bin/bash

output="output.txt"
res=""
for pid in `ps -ax -o pid --no-headers`
do
    ART=""
    ppid=`grep -s "^PPid:" /proc/$pid/status | awk '{print $2}'`
    sum_exec_runtime=`grep -s "^se.sum_exec_runtime" /proc/$pid/sched | awk '{print $3}'`
    nr_switches=`grep -s "^nr_switches" /proc/$pid/sched | awk '{print $3}'`
    if [ ! -z "$nr_switches" ] && [ "$nr_switches" -ne 0 ]
    then
        ART=`echo "$sum_exec_runtime/$nr_switches" | bc -l`
        res="$res$pid $ppid $ART"$'\n'
    fi
done
sort -n --key=2 <<< "${res%?}" | awk '{print "ProcessID=" $1 " : " "Parent_ProcessID=" $2 " : " "ART=" $3 }' > $output