#!/bin/bash

file="report"
at now + 2 minutes -f i.sh
tail -n 0 -f $file &
