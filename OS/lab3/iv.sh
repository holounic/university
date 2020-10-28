#!/bin/bash

script="iv_proc.sh"
chmod +x $script

{
    "./$script" &
    "./$script" &
    "./$script" & 
} ;
proc_pid=`pgrep $script -o` ;
sudo cpulimit --pid $proc_pid --limit 5 --background
