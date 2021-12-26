#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    s/(\w)(\1)+/$1/g;
    print
}