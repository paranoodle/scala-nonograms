# Nonograms game in Scala

This is a Scala project done at HEIG-VD, Switzerland. It is a stand-alone game about [Nonograms](https://en.wikipedia.org/wiki/Nonogram) using the Scage library with maven.

It was done in Spring 2016 by [Eleonore d'Agostino](https://github.com/paranoodle) & [Valentin Minder](https://github.com/ValentinMinder).

Please find the more details about this project in the [`REPORT`](REPORT.md) file.

## Scage Library

We used the scage library to make our project run. Scage is a framework to write simple 2D opengl games, written in Scala. [Find out more about Scage](https://github.com/dunnololda/scage/#introduction).

## Run instructions
### How to run the ZIP release (all OS): easy

1. Download the latest release from the [releases page](https://github.com/paranoodle/scala-nonograms/releases). Alternatively, try our Dropbox releases: from [here for UNIX/MacOSX/Linux](https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/nonograms-0.1-linux.zip) or from [here for Windows](https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/nonograms-0.1-windows.zip).
1. Unzip it
1. Run it with the given script
	
	- on Windows with the `run.bat` file
	- on UNIX with the `run.sh` file. From the terminal, you may need to change execution rights with `chmod u+x run.sh` and then run with `sh ./run.sh`.

### How to run the JNLP release with Java Web Start (all OS): harder

See futher down. It is really complicated.

## Build Instructions

First of all, move one level down to the project level with: `cd nonograms`. The file `local-build.properties` must exists, even if empty. If it is not there, do: `touch local-build.properties`.

### How to build a ZIP release with maven

Type: `mvn clean install` or `mvn clean package` or `mvn clean package -Pbuild` (they all have the same 

To build but skip the tests, type: `mvn clean package -Dmaven.test.skip`

The target for linux/unix is now available under `target/nonograms-linux.zip`. It is a zip containing a runnable script and all necessary librairies to be runnable stand-alone.

- To speed up the process when developping, use our `quickrun.sh` which does the compilation (without clean & test) and directly runs the application.
- To compile for another OS (eg. Windows), open the `build.properties` file, go to the key `os.type = linux` and change it to `windows`.
- To compile for another language (eg. French), open the `src/main/resources/maven.properties` file, go to the key `xml.lang = en` and change it to `fr`. 
- You are free to translate the game in any another language. Translate the strings ressources in `src/main/resources/resources/strings/nonograms_strings_XX.xml` where `XX` is the language abbreviation you use in the above `maven.properties`

To troobleshoot or more details follow [the original indications from the Scage library](https://github.com/dunnololda/scage/#for-maven-users) or [the indications in the original readme file](nonograms/readme).

### How to build a JNLP release with maven

First, you need a valid signing certificate in the top directory. It can be a self-signed certificate, but it will create issues to run the jnlp file (see below). Add theses lines to your `local-build.properties` file.

```
# replace the lines below with the correct data and add this file to .gitignore or 
# git update-index --assume-unchanged local-build.properties
keystore = XYZFileName
keystore.alias = XYZ
keystore.pass = XYZ
key.pass = XYZ
```

Then, you need hosting to host your web application. You need to put the url it in your `pom.xml` file, under the `<url>` tag of the project.

```
<name>Nonograms</name>
<description>Simple Nonograms Project Stub</description>
<url>https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/jnlp</url><
```

We used a public dropbox, namely 

`https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/jnlp/nonograms-run.jnlp`

Finally, type `mvn clean package -Pwebstart`. It will sign ALL the files including the existing librairies, the process is quite long.

The files are available in `target/jnlp`. Move the WHOLE directory to your hosting site. 

Distribute the jnlp destriptor: `target/jnlp/nonograms-run.jnlp`. Running it with java will download the web application from your hosting.

To troobleshoot or more details follow [the indications in the original readme file](nonograms/readme).

### How to test with maven

Type: `mvn clean test`. All 18 tests must be successful.

### How to test & build with IntelliJ IDEA

Some people prefer to work with IDE, although it is not necessary for this project. You will need Maven the very first time, though.

The very first time, you need to create all necessary file and dependancies with maven. Type `mvn clean compile`

In the IDE, try to run the main UI class (**`LauncherMainMenu.scala`** in `src/main/scala/heigvd/nonograms/views/`). This should fail with an error like `Exception in thread "main" java.lang.UnsatisfiedLinkError: no lwjgl in java.library.path`. But it created a run configuration in the top right corner. Then, edit the config launch of the last run and add under `VM Options:`

`-Djava.library.path=target/natives -DLWJGL_DISABLE_XRANDR=true -Dfile.encoding=UTF-8`

To troobleshoot or more details follow [the original indications from the Scage library](https://github.com/dunnololda/scage/#intellij-idea).

### How to test and check coverage with InteliJ IDEA

First, you need the project to work in IntelliJ from previous point.

Go to the `src/main/scala/heigvd/nonograms/models` package. Right-click and select **Run with coverage**. You should see an new view with the line and method coverage for each model class.

## Run Instructions (continued)

### How to run the JNLP release with Java Web Start (all OS): harder

1. **Download the latest JNLP file** from [the releases page](https://github.com/paranoodle/scala-nonograms/releases) or from [here](https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/jnlp/nonograms-run.jnlp). Different browsers cause different issues:

	- **Chrome users**: by-pass the browser warning *"This type of file can harm your computer."*, and click **Keep** 

		![](img/warn01.png)

	- **Firefox** users have the possibility to direct launch with Java Web Start, but its recommended to rather **Save File**, as some more security settings have to be changed
	
		![](img/warn02.png)
	
	- **Safari or other browsers users** should have no problems.

1. **Double-click on the downloaded JNLP file to launch it.**. If you run **Windows or Linux**, you should have no problems and you can go to next step. If you run **Mac OS X**, most users will have several problems (if they have the default highest level of security):

	- First, this screen will appear. It is because the application is self-signed, and therefore not recognized as legitimate.
	
		![](img/warn03.png)
	
	- From Spotlight (`Cmd + Space`), go to `System Preferences`  and then navigate to `Security & Privacy > General` and click **Open Anyway**. You should be asked for your computer admin password.

		![](img/warn04.png)
	
	- Run the `jlnp` program again. When you see this, click **Open**
	
		![](img/warn05.png)

1. **All users** will get a warning (*running Java 8u20 and above*): ***Application Blocked by Java Security***. It is because the application is self-signed, and therefore not recognized as legitimate. Also *Java 8u20* removed the *medium security level*  and now enforces application to be correctly signed or to enter manually an exception.

	![](img/warn08.png)

1. **Change Java Security Exception List** from the **Java Control Panel**

	- **UNIX users**: type `javaws -viewer` in Terminal (and close the front window, the interesting one is behind)

	- **MacOS X users**: go to `System Preferences > Java` or see [here](http://java.com/en/download/help/mac_controlpanel.xml)

	- **Windows users**: go to `Windows Start menu > Programs > Java program > Configure Java` and it will launch the Java Control Panel, or see [here](http://java.com/en/download/help/win_controlpanel.xml)
	
		![](img/warn09.png)
	
	From the **Java Control Panel**, go to `Security > Exception Site List > Edit Site List > Add` and add the following URL. It is a direct https url, so no other programs may forge it or use .
	
	```
	https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/jnlp/nonograms-run.jnlp
	```
	
	![](img/warn10.png)
	
	[More information about JCP Security](http://java.com/en/download/help/jcp_security.xml)

1. **Re-run the application**. You will get a **Security Warning** like this one. Again, it is because the application is self-signed, and therefore not recognized as legitimate. Tick **I accept the risk and want to run this application** and click **Run**.

	![](img/warn11.png)
	
	If you are paranoid or concerned about security:
	
	- Check that the name is correctly `heigvd.nonograms.LauncherMainMenu`. 
	- Hover location and check it is 
	`https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/jnlp/nonograms-run.jnlp`
	- Click on **More Information** then **View Certificate Details**
	- Check the certificate, it should correspond to the following signatures and screenshot.
		- MD5: `F6:4F:C5:24:E7:ED:9A:79:1C:BD:25:6E:E0:EF:31:87`
		- SHA-1: `84:B1:AA:86:33:F6:B0:62:26:02:5E:CB:86:92:7F:19:1C:52:7B:36`
	
			![](img/warn12.png)
	
1. **Success, the app is now live!**

**If you are paranoid or concerned about security, when you don't need the app anymore, go to your Java Control Panel**
 	
- remove the exception from `Security > Exception Site List > Edit Site List > Remove`
- restore security prompts `Security > Restore Security Prompts`
- clear the cache from `General > Temporary Internet Files` and then:
	- `Settings > Delete Files > Cached & Installed Applications and Applets`
	- `View > Applications > Nonograms > right-click > delete`