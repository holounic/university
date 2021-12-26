#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    print if /\b(\S+)\1\b/
}
