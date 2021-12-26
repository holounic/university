#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    print if /cat.*cat/;
}