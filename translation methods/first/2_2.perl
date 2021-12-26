#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    s/\bhuman\b/computer/g;
    print;
}