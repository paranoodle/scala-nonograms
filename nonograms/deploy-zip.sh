#!/bin/sh
# clean, test, build (zip) and post it to release

# change OS here
os=linux
v=nonograms-1.0
out="/Users/val/Dropbox/Public/heig-nonograms-EdAVM/"
target=target/"$v"-"$os"

mvn clean package -Pbuild

mkdir "$out"
cp "$target".zip "$out"

## JNLP release accessible under https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM