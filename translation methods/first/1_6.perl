#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    print if /\b\d+\b/;
}