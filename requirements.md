# Requirements
This page includes useful information on the supported Operating Systems as well as the software requirements to run all the scripts used in data extraction.

## Operating System
### Microsoft Windows
Microsoft windows was used to develop and run all the scripts used in the process of data extraction and filtering and hence we strongly recommend to run all the files on windows operating system itself.

## Software requirements
### Pre-requisites:
1.  Python `version 3.8.3 (or above)`
2.  Java(TM) SE Runtime Environment `(build 1.8.0_144 and above)`
3.	Git for Windows

### Setting up the Python Environment
Run the following commands to install the required python packages
1.  `pip install progress` ([Progress Library](https://pypi.org/project/progress/): Helps with visual representation of logs processed)
2.  `pip install gitpython` ([GitPython](https://gitpython.readthedocs.io/en/stable/): Python library to use GitAPIs)

### Java Requirements
Fetch the additional JAR Files required to run the java programs. These files can be found in the folder `DocDependency/scripts/project_jars/`