#!/bin/bash
foldername=$1
logfile=$2
basefolder=$3
mkdir -p $basefolder$foldername
mkdir -p $basefolder$foldername/en/images
mkdir -p $basefolder$foldername/en/certificate
mkdir -p $basefolder$foldername/en/css
touch $basefolder$foldername/brand.en.properties
chown nobody $basefolder$foldername -R
chgrp nogroup $basefolder$foldername -R
cp -r $basefolder$foldername /home/vu360/brands/$foldername
