#!/usr/bin/perl
use strict;
use warnings;

while (<>) {
    print if /^((\S+(.*\S+)*)|\S*)$/
}
