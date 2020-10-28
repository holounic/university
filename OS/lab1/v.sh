#!/bin/bash

touch info.log
file="/var/log/syslog"

while IFS= read -r line
do
  if [ "$line" == *"INFO"* ]
  then
    echo "$line" >> "info.log"
  fi
done < "$file"
< "info.log"