#!/usr/bin/perl
use strict;
use warnings;

sub println {print @_, "\n"}

my @text = <STDIN>;
my $n_lines = @text;
my @res = ();
my $i = 0;
my $j = $n_lines - 1;

my $k = 0;

while ($k < $n_lines) {
    my $cleaned = $text[$k] =~ s/<[^>]*>//gr;
    $text[$k] = $cleaned;
    $k++;
}

while ($i < $n_lines) {
    $_ = $text[$i];
    $i++ if /^\s*$/;
    last if /\S/;
}

while ($j > $i) {
    $_ = $text[$j];
    $j-- if /^\s*$/;
    last if /\S/;
}

my $cnt = 0;

while ($i <= $j) {
    $_ = $text[$i];
    s/(^\s*)|(\s*$)//g;
    s/(\s)(\1)*/$1/g;
    my $x = $_;
    $cnt++ if /^$/;
    push @res, "" if not /^$/ and $cnt > 0;
    push @res, $x if not /^$/;
    $cnt = 0 if not /^$/;
    $i++;
}

foreach (@res) {
    println $_;
}