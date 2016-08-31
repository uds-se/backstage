# Backstage - Detecting Behavior Anomalies in Graphical User Interfaces

## UI analysis phase

### Prerequiesites:
1. Java 8
2. Maven


### Building:
```bash
$ mvn initialize
$ mvn package
```
### Usage:
The script below only works on Linux and MacOS systems. If you want to run it on Windows, just examine the `runApp.sh` file and run `apktool` and `backstage.jar` manually.
```bash
./runApp.sh PATH_TO_APK/myApp.apk
```

### Results
The tool produces:
* `appSerialized.txt` file in `output/<name_of_apk>` folder with the UI model
* `<name_of_apk>_forward_apiResults_1.xml` with the mappting of callbacks to APIs

Those files are needed to obtain the mapping between UI elements and APIs later on. 

## Detecting outliers phase 

### Prerequiesites:
1. R
2. Python 3
3. pip3 
4. Linux or MacOS

### Before you start
Before running the scripts you need to follow the steps below:
#### Open `R` console:
```R
install.packages("logging", dependencies=TRUE)
install.packages("stringr", dependencies=TRUE)
install.packages("argparse", dependencies=TRUE)
slam_link="https://cran.r-project.org/src/contrib/Archive/slam/slam_0.1-37.tar.gz"
install.packages(slam_link, repos = NULL, type="source")
install.packages("skmeans", dependencies=TRUE)
install.packages("cluster", dependencies=TRUE)
install.packages("clue", dependencies=TRUE)
install.packages("doParallel", dependencies=TRUE)
```
####  Make sure you have `Python v3` and `pip v3` installed and: 
```bash
pip3 install argparse
pip3 install numpy
pip3 install pandas
pip3 install webcolors
pip3 install gensim
pip3 install nltk
pip3 install spacy
python3 -m spacy.en.download
```
Finally, open `python3` console:
```python
import nltk
nltk.download()
```
and download the following packages:
* wordnet
* stopwords
* words

### Run an analysis
```bash
./launch.sh
```
