# Backstage - Detecting Behavior Anomalies in Graphical User Interfaces

## Prerequiesites:
1. Java 8
2. Maven


## Building:
```bash
$ mvn initialize
$ mvn package
```
## Usage:
The script below only works on Linux and MacOS systems. If you want to run it on Windows, just examine the `runApp.sh` file and run `apktool` and `backstage.jar` manually.
```bash
./runApp.sh PATH_TO_APK/myApp.apk
```

## Results
The tool produces:
1. `appSerialized.txt` file in `output/<name_of_apk>` folder with the UI model
2. `<name_of_apk>_forward_apiResults_1.xml` with the mappting of callbacks to APIs

Those files are needed to obtain the mapping between UI elements and APIs later on. 

Scripts for processing data as well complete scripts with mutation techniques will be published soon.
