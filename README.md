# Nonograms game in Scala

This is a Scala project done at HEIG-VD, Switzerland. It is a stand-alone game about [Nonograms](https://en.wikipedia.org/wiki/Nonogram) using the Scage library with maven.

It was done in Spring 2016 by [Eleonore d'Agostino](https://github.com/paranoodle) & [Valentin Minder](https://github.com/ValentinMinder).

Please find the more details about this project in the [`REPORT`](REPORT.md) file.

## Scage Library

We used the scage library to make our project run. Scage is a framework to write simple 2D opengl games, written in Scala. [Find out more about Scage](https://github.com/dunnololda/scage/#introduction).

## Run & Build Instructions

### How to test with maven

Type: `mvn clean test`. All 18 tests must be successful.

### How to build with maven

Type: `mvn clean install`

Or, to build and skip the tests, type: `mvn clean package -Dmaven.test.skip`

The target for linux/unix is available under `target/nonograms-##.zip`. Change the `build.properties` file to generate a zip for Windows.

It contains a runnable script and all necessary librairies to be runnable stand-alone.

To troobleshoot follow [these indications](https://github.com/dunnololda/scage/#for-maven-users).

### How to test & build with IntelliJ IDEA

The very first time, you need to create all necessary file and dependancies with maven. Type `mvn clean compile`

In the IDE, try to run the main class (NonogramsOffline.scala). Then, edit the config launch of the last run and add under `VM Options:`

`-Djava.library.path=target/natives -DLWJGL_DISABLE_XRANDR=true -Dfile.encoding=UTF-8`

Alternatively, follow the instructions [here](https://github.com/dunnololda/scage/#intellij-idea).

### How to run the ZIP release (all OS): easy

1. Download the latest release
 - from the [releases page](https://github.com/paranoodle/scala-nonograms/releases) 
 - from [here for UNIX/MacOSX/Linux](https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/nonograms-0.1-linux.zip) 
 - from [here for Windows](https://dl.dropboxusercontent.com/u/46998912/heig-nonograms-EdAVM/nonograms-0.1-windows.zip)
1. Unzip it
1. Run it with the given script
	
	- on Windows with the `run.bat` file
	- on UNIX with the `run.sh` file. From the terminal, you may need to change execution rights with `chmod u+x run.sh` and then run with `sh ./run.sh`.

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