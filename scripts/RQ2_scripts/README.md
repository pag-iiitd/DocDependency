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
1.  Create a new folder at any location, e.g., Desktop (In complete README.md this folder is referred as 'project_folder')       
2.  Copy the folder named project_jars and all the files in RQ2_scripts into the 'project_folder'.
3.  Open the _Windows Command line interpreter (cmd.exe)_ and change directory to 'project_folder'.        
    `>   cd <path_to_project_folder>`        
4.  Clone the git repo into the 'project_folder' using the command       
    `>   git clone <link to the github project>`        
    For example to clone gson project use command       
    `>   git clone https://github.com/google/gson`       
    This step will create a folder with project_name (gson in above case)       

# Running extraction scripts:       

## Getting the line numbers which changed between commits (using getting_line_numbers.py):       
1.  Open a command prompt with the current directory as the 'project_folder'       
2.  Run the python script using command       
    `>   python getting_line_numbers.py <project_name>`       
    For example for gson project use command           
    `>   python getting_line_numbers.py gson`        
3.  This script will create a file named **<project_name>_last_2_years.json**. This file contains all the files changed in a given commit with the line numbers changed in each file between the commits.

## Getting functions changed between two commits (using ExtractCodeDocModularity):       
1.  Open a command prompt with the current directory as the 'project_folder'       
2.  Execute the compiled ExtractCodeDocModularity.class by including the jars in the classpath, as in the command below.       
    `> java -cp ".;./path/to/project_jars/javaparser-core-3.14.3.jar;./path/to/project_jars/org.json.simple-0.4.jar" ExtractCodeDocModularity`         
    For example: below project_jars folder is located in the 'project_folder' itself.       
    `> java -cp ".;./project_jars/javaparser-core-3.14.3.jar;./project_jars/org.json.simple-0.4.jar" ExtractCodeDocModularity`       
    OR       
    Execute the ExtractCodeDocModularity.jar using the command below:       
    `> java -jar .\ExtractCodeDocMoularity.jar`       
3.  Program will take three inputs project name, location of the output of getting_line_numbers.py and location of .git file of the corresponding project. For example:        
    `Enter the project name: gson`                     
    `Enter the location of directory where output of getting_line_numbers.py is present: C:/Users/Avyakt/Desktop/project_folder/`             
    `Enter the path where .git file for the repo is saved: C:/Users/Avyakt/Desktop/project_folder/gson/.git`        
4.  It will create a new JSON file with name **<project_name>_changedFunctions.json**. This file contains all the functions which evolved between two commits with the component which evolved in the commit (code/doc).        

## Getting functions where only documentation was changed (using filter_database.py):       
1.  Open a command prompt in the 'project_folder'        
    >(Assuming filter_database.py and "<project_name>_changedFunctions.json" exist in this directory)       
2.  Run the python script using command       
    `> python filter_database.py <project_name>`  
    For example for gson project use command           
    `> python filter_database.py gson`    
3.  This script will create a file named **<project_name>_changedFunctionsFiltered.json**. This file contains all the functions for which only documentation was changed in the commit.       

## Examples        
 1. [Glide](https://github.com/pag-iiitd/DocDependency/tree/master/scripts/RQ2_exmples/glide_examples)
 2. [Gson](https://github.com/pag-iiitd/DocDependency/tree/master/scripts/RQ2_exmples/gson_examples)

# Time taken by each project (in seconds)
|library  |getting_line_numbers|javaparser | total
|--|--|--|--
|elasticsearch|4449.49|5241.134|9690.624
|spring-boot|1919.916|1162.482|3082.398
|RxJava|437.056|1505.028|1942.084
|spring-framework|1905.409|2912.984|4818.393
|guava|192.824|197.454|390.278
|retrofit|49.057|49.563|98.62
|dubbo|681.384|809.488|1490.872
|MPAndroidChart|33.933|21.745|55.678
|glide|176.184|290.787|466.971
|netty|316.088|421.234|737.322
|gson|7.5899|11.041|18.6309
