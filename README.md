# scala-nonograms

A scala project at HEIG-VD, Switzerland.

A stand-alone game about [nonograms](https://en.wikipedia.org/wiki/Nonogram) using the scage library with maven.

Authors:

- [Eleonore d'Agostino](https://github.com/paranoodle)
- [Valentin Minder](https://github.com/ValentinMinder)

## Scage Library

We used the scage library to make our project run. Scage is a framework to write simple 2D opengl games, written in Scala. [Find out more about Scage](https://github.com/dunnololda/scage/#introduction).

## Run & Build Instructions

### How to test with maven

Type: `mvn clean test`. All tests must be successful.

### How to build with maven

Type: `mvn clean install`

Or, to build and skip the tests, type: `mvn clean package -Dmaven.test.skip`

The target is available under `target/nonograms-##.zip`

It contains a runnable script and all necessary librairies to be runnable stand-alone.

To troobleshoot follow [these indications](https://github.com/dunnololda/scage/#for-maven-users).

### How to test & build with IntelijIDEA

The very first time, you need to create all necessary file and dependancies with maven. Type `mvn clean compile`

In the IDE, try to run the main class (NonogramsOffline.scala). Then, edit the config launch of the last run and add under `VM Options:`

`-Djava.library.path=target/natives -DLWJGL_DISABLE_XRANDR=true -Dfile.encoding=UTF-8`

Alternatively, follow the instructions [here](https://github.com/dunnololda/scage/#intellij-idea).

### How to run the release (UNIX)

- pick the latest [release](https://github.com/paranoodle/scala-nonograms/releases) and download it
- unzip it
- on UNIX systems (MacOSX/Linux): run the `run.sh` file, by typing `sh ./run.sh` in your terminal.