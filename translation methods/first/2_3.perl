#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    s/\ba+\b/argh/i;
    print;
}