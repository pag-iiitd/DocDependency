# Requirements
The requirements to run all the scripts can be found in [REQUIREMENTS.md](https://github.com/pag-iiitd/DocDependency/blob/master/REQUIREMENTS.md)

# Data Extraction
We followed a two-phase data extraction methodology to analyze the documentations in 11 open-source GitHub repositories or projects (listed in Table 1 of the paper). 
1. Phase 1 prepares data for RQ 1 by randomly extracting functions from each project. The script used for data extraction and steps required to run the scripts can be found in [RQ1_scripts](https://github.com/pag-iiitd/DocDependency/tree/master/scripts/RQ1_scripts).
2. Phase 2 prepares data for RQ 2,3,4,5 by extracting commit logs where only documentation changes were made to a method. The scripts and instructions to run these scripts can be found in [RQ2_scripts](https://github.com/pag-iiitd/DocDependency/tree/master/scripts/RQ2_scripts).   

As an output, we obtained a dataset of 
1. 50 functions from each project
2. all the commits from a project from a 2-year span (June 15, 2018 to June 15, 2020) containing changes in a method-documentation and extracted their associated parameters such as method signature, line numbers changed, etc.

# Data Annotation
The obtained dataset was manually analyzed for various features discussed in the paper. The annotated dataset has been shared along in [Dataset_Observations.xlsx](https://github.com/pag-iiitd/DocDependency/blob/master/Dataset_Observations.xlsx). By the means of Artifact Evaluation Track, we want to make all the scripts and the dataset AVAILABLE and REUSABLE for further research. 

# Authors
Please find in [AUTHORS.md](https://github.com/pag-iiitd/DocDependency/blob/master/AUTHORS.md).