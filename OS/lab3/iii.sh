#!/bin/bash

task="~/lab3/i.sh"
echo "*/5 * * * 3 $task" | crontab