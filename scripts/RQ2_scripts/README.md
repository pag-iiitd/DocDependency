# Pre-requisites:       
1.  Python version 3.8.3 (or above)       
2.  Java(TM) SE Runtime Environment (build 1.8.0_144 and above)       
       
# Setting up environments       
## Setting up python environment       
Run the following commands to install the given python packages       
1.  `pip install progress` (Progress Library: Helps with visual representation of logs processed)       
2.  `pip install gitpython` (GitPython: Python library to use GitAPIs)       
       
## Setting up Java Environment:       
The project jars have been provided in 'project_jars' folder       
       
## Setting up the extraction environment:       
1.  Create a new folder (In complete README.txt this directory is refered as project folder)       
2.  Copy the folder named project_jars, getting_line_numbers.py and filter_database.py into project folder.       
3.  Clone the git repo into the project folder using the command       
    `>   git clone <link to the github project>`        
    For example to clone netty project use command       
    `>   git clone https://github.com/netty/netty/`       
    This step will create a folder with project_name (netty in above case)       
       
# Running extraction scripts:       
## Getting the line numbers which changed between commits (using getting_line_numbers.py):       
1.  Open a terminal with the curent directory as the project folder       
2.  Run the python script using command       
    `>   python3 getting_line_numbers.py <project_name>`       
3.  This script will create a file named **"<project_name>_last_2_years.json"**. This file contains all the files changed in a given commit with the line numbers changed in each file between the commits.        
       
## Getting functions changed between two commits (using ExtractCodeDocModularity):       
1.  Execute the compiled ExtractCodeDocModularity.class by including the jars in the classpath, as in the command below. Pass the two arguments: the absolute path to <project_folder>. Eg is given below and the project's name       
    `> java -cp ".;./path/to/javaparser-core-3.14.3.jar;./path/to/org.json.simple-0.4.jar" ExtractCodeDocModularity`       
    For example: below project_jars folder is located in the project folder itself.
    OR       
    Execute the ExtractCodeDocModularity.jar using the command below:       
    `> java -jar .\ExtractCodeDocMoularity.jar`       
2.  Program will take three inputs project name, location of the output of getting_line_numbers.py and location of .git file of the corresponding project. For example:        
    `Enter the project name: gson`                     
    `Enter the location of directory where output of getting_line_numbers.py is present: C:/Users/Avyakt/Desktop/project_folder/`             
    `Enter the path where .git file for the repo is saved: C:/Users/Avyakt/Desktop/project_folder/gson/.git`        
3.  It will create a new JSON file with name **"<project_name>_changedFunctions.json"**. This file contains all the functions which evolved between two commits with the component which evolved in the commit (code/doc).        
       
## Getting functions where only functions were changed (using filter_database.py):       
1.  Open a terminal in the project folder        
    >(Assuming filter_database.py and "<project_name>_changedFunctions.json" exist in this directory)       
3.  Run the python script using command       
    `> python3 filter_database.py <project_name>`       
4.  This script will create a file named **"<project_name>_changedFunctionsFiltered.json"**. This file contains all the functions for which only documentation was changed in the commit.