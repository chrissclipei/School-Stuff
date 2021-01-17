#!/usr/bin/perl
use strict;
use warnings;
use Data::Dumper;

my $filename = "Makefile";
my $target;
my @targets = ();
my @inputs = ();
my @cmd = ();
my %commands = ();

open my $file, "<$filename" or warn "$0: $filename: $!\n";
while (my $row = <$file>) {
   chomp $row;
   if ($row !~ /^\s*$/){
      if ($row !~ /#.*?/){
         if ($row !~ /\t/){ 
            $row =~ s/^\s+|\s+$//g;
            push @inputs, $row;
            $row =~ /(.*):(.*)/;
            $target = $1;
            $target =~ s/^\s+|\s+$//g;
            push @targets, $target;
         }
         if ($row =~ /\t/){ 
            $row =~ s/^\s+|\s+$//g;
            if (index($row, '@') != -1){
               
            }
            my $command = $row;
            push @cmd, $row;
            push @{$commands{$target}}, $command;
         }
      }
   }
}

sub parse_dep ($) {
   my ($line) = @_;
   return undef unless $line =~ m/^(\S+)\s*:\s*(.*?)\s*$/;
   my ($temptarget, $dependency) = ($1, $2);
   my @dependencies = split m/\s+/, $dependency;
   return $temptarget, \@dependencies;
}

my %hashdep;
for my $input (@inputs) {
   my ($temptarget, $deps) = parse_dep $input;
   print "$0: syntax error: $input\n" and next unless defined $temptarget; 
   $hashdep{$temptarget} = $deps;
}
 
make ($targets[0]);

sub make {
   my $target = shift;
   print "$target\n";
   print Dumper(\%hashdep);
   print Dumper(\@{$hashdep{$target}});
   my @prereq;
   if (defined($hashdep{$target})){
      @prereq = $hashdep{$target};
   #   print Dumper(\@prereq);
   }
}

#print Dumper(\@inputs);
#print Dumper(\%hashdep);
#print Dumper(\@targets);
#print Dumper(\%commands);
for my $comm ( keys %commands){
   for my $i ( 0 .. $#{$commands{$comm}}) {
      system($commands{$comm}[$i]);
   }
   print "\n";
}

