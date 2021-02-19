# System Environment        
These scripts have been executed on a Windows 10 OS       
       
# Pre-requisites   
1.  Python version 3.8.3 (or above)       
2.  Java(TM) SE Runtime Environment (build 1.8.0_144 and above)       
       
This readme file contains steps to produce files, containing method details, analysed per project (the Github repo.) for RQ 1: Are the documentations written independently       
       
For convenience all these files have been provided along, project-wise, in the 'data' folder. Running all these steps per project will likely take less than 1 minute.  
       
# Steps       
0.  Launch the git bash command prompt (https://www.stanleyulili.com/git/how-to-install-git-bash-on-windows/) and execute the below described commands     
       
1.  Change your directory to the path where you would like to clone the project repository, e.g. Desktop      
`> cd <absolute_path_to_working_folder>`       
2.  clone the project repository. Sample to clone guava project:        
`> git clone https://github.com/google/guava.git`       
3.  Change the directory to the cloned repository. For eg.        
`> cd guava`       
4.  Store the paths to all .java files in paths.txt       
`> find . -type f -name "*.java" >paths.txt`       
5.  Change the directory to the folder containing this README file and execute getRQ1_commits.py:       
`> cd <path to getRQ1_commits.py>`       
`> python -u getRQ1_commits.py  <Absolute path to the paths.txt> <project-name as appears in the github repo.>`    
Sample:        
`python -u getRQ1_commits.py "D:\Study\guava\paths.txt" "guava"`
**NOTE:** If you would like to repeat the step 5. on a particular project, please make sure that you delete the folder **<project-name_rq1>** (example: guava_rq1) from the project repository folder (example: guava)

       
# Output
The script will create a folder **<project-name_rq1>** (example: guava_rq1) inside the project repository folder and store a list of files contaning method details(signature, javadoc, source-code) used in analyzing RQ1.


