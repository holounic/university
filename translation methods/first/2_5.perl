#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    s/(\w)(\w)(\w*)(\W)/$2$1$3$4/g;
    print
}