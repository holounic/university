#!/bin/bash

output="new_output.txt"
awk -F'[ =]' '
BEGIN {
    ppid="0"
    children=0
    sum=0
    }
{
    if ($5 == ppid) {
        ++children
        sum+=$8
    } else {
        print "Average_Sleeping_Children_of_ParentID=" ppid  " is " sum / children
        ppid=$5
        childer=1
    }
    print($0)
}
END {
    print "Average_Sleeping_Children_of_ParentID=" ppid  " is " sum / children 
}
' 'output.txt' > $output