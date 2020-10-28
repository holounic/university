#!/bin/bash

echo $$ > .pid
n=1
mode="+"

usr1()
{
    mode="+"
}
usr2()
{
    mode="*"
}
sigterm()
{
    mode="term"
}

trap 'usr1' USR1
trap 'usr2' USR2
trap 'sigterm' SIGTERM

while true;
do
    case $mode in
        "term")
            echo "Stopped by SIGTERM"
            exit
            ;;
        "+")
            n=$(($n + 2))
            ;;
        "*")
            n=$(($n * 2))
            ;;
        *) 
            :
            ;;
    esac
    if [ "$mode" != "waiting" ]
    then
        echo $n
        mode="waiting"
    fi
    sleep 1s
done