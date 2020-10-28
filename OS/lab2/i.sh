#!/bin/bash
usr="azureuser"
ps ao pid,command -u $usr | awk '{n++} {print $1":"$2} END {print n-1}' \
> "res.txt"