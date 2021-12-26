#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    s/\b(\d)(\d*)0\b/$1$2/g;
    print;
}