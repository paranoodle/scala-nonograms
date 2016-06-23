#!/bin/sh
# build (without clean&test), unzip and launch

os=linux
v=nonograms-1.0
target=target/"$v"-"$os"

mvn package -Pbuild -Dmaven.test.skip
rm -r "$target"
mkdir "$target"
unzip -qq "$target".zip -d "$target"

cd "$target"
./run.sh
cd ../..