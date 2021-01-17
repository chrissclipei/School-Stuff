#!/usr/bin/perl

use strict;
use warnings;
use Getopt::Std;
use Data::Dumper;

my $filename = "Makefile";
my $target;
my @targets = ();
my @inputs = ();
my @cmd = ();
my %commands = ();
my %targetStatus = ();
my %opts;

getopts ("d", \%opts);
if ($opts{'d'}){
   print "Debugging\n";
}

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
            $targetStatus{$target} = 0;
         }
         if ($row =~ /\t/){
            $row =~ s/^\s+|\s+$//g;
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

#print Dumper(\%hashdep);
#print Dumper(\%commands);


if (exists $ARGV[0]){
   foreach my $i (0 .. $#targets){
      if ($ARGV[0] eq $targets[$i]){
         make($targets[$i]);
      }
   }
} else {
   make($targets[0]);
}

sub handlecommands {
   my $target = shift;
   foreach my $command (@{$commands{$target}}){
      my $noExit = index($command, '-');
      if($command !~ /@/){
         print("$command\n");
      }
      if($command =~ /@\s*(.*)/){
         $command = $1;
      } 
      system($command);
      my $term_signal = $? & 0x7F;
      my $core_dumped = $? & 0x80;
      my $exit_status = ($? >> 8) & 0xFF;
  
      if ($noExit < 0){
         if ($term_signal != "0") {
           print "termination signal $term_signal: %strsignal[$term_signal]";
           exit 1;
         }
      }
   }
   $targetStatus{$target} = 1;
   return;
}

sub isTarget{
   my $inQuestion = shift;
   for my $tValue (@targets) {
     if ($tValue eq $inQuestion) {
       return 1;
     }
   }
   return 0;
}

sub isFile{
   my $inQuestion = shift;
   return 1 if -e $inQuestion;
   return 0;
}
sub make {
   my $target = shift;
   if ($targetStatus{$target}) { return; }
   $targetStatus{$target} = 1;

   if (isFile($target)){
      foreach my $preReq (@{$hashdep{$target}}){
         if (isTarget($preReq)) {
             make($preReq);
         } elsif (isFile($preReq)) {
            if (-C $target < -C $preReq){
               handlecommands($target);
            } else {
               next;
            }
         } else {
             print "bad makefile buddy!";
         }
      }
   } else {
      foreach my $preReq (@{$hashdep{$target}}){
         if (isTarget($preReq)) {
            make($preReq);
         } else {
            handlecommands($target);
         }
      }
      handlecommands($target);
   }
}

#my $indtar = shift @targets;
#print "$indtar\n";
#print Dumper(\@targets);
