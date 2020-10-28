#!/bin/bash

pid=`cat .pid`
while true;
do
    read line
    case $line in 
        +)
            kill -USR1 $pid
            ;;
        [*])
            kill -USR2 $pid
            ;;
        "TERM")
            kill -SIGTERM $pid
            exit
            ;;
        *)
            :
            ;;
    esac
done
