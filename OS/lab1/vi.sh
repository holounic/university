#!/bin/bash

source="/var/log/syslog"
dest="full.log"
touch $dest

replaced=`sed 's/INFO/Information:/g; s/WARNING/Warning:/g' $source`

echo $replaced | grep "Warning:" >> $dest
echo $replaces | grep "Information:" > $dest 