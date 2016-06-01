#!/bin/sh
# clean, test, build (jnlp) and post it to release

out="/Users/val/Dropbox/Public/heig-nonograms-EdAVM/"

mvn clean package -Pwebstart

mkdir "$out"
cp -r target/jnlp "$out"

## JNLP release accessible under https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/jnlp/nonograms-run.jnlp
