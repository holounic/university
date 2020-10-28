#!/bin/bash

date=`date  +%Y_%m_%d_%H_%M_%S`
report="report"
mkdir test &&
{ 
    echo "catalog test was created successfully" > $report ;
    touch test/$date
} ;
ping -c1 www.net_nikogo.ru || echo $date "oopsy" >> $report
