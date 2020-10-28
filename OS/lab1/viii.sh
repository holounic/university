#!/bin/bash

source="/etc/passwd"
awk -F: '{print $1 " " $3}' $source | sort -n -k2