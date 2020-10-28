#!/bin/bash

script="./iv_proc.sh"
chmod +x $script

{
    $script &
    $script &
    $script & 
} ;
proc_pid=`ps -ao pid -C $script --no-header | head -1` ;
sudo cpulimit --pid $proc_pid --limit 5 --background
