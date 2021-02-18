"""inputs a txt file containings all file paths to .java files, except test files.
Output: a txt file with sno. containing method path, method sig, doc, source code, 
a random doc line commits affecting a random line of a doc; all separated by delimiter
1) read file from input txt and pick random 50 lines and store their paths
2) For 50 times, generate number 1 or 2 (this may result >50 overall but can discard
	some later) and call javaparser code that extracts that number of methods randomly
	2a) Javaparser: Take 3 inputs: path of the file, sno for file naming, Randomly picked
		input number of methods. If a method has no doc, then skip such method
	2b) Store method's details: pathname, signature, doc, source, random line from doc 
		whose commits to analyse in <sno>.txt in a folder inside repo by the name <projectname>_rq1
3) Read all <sno>.txt created  and extract random doc line. Call git log -S <line> command to get 
	commit history of that line and add all those commit ids to the txt file
"""
import os
import sys
import json
from random import randint

#assume current path is up to the point where getRQ1_commits.py is- in scripts folder
current_path=os.getcwd()#inside script folder
path_sep=os.path.sep
#input sample: python getRQ1_commits.py  "D:\Study\guava\paths.txt" "guava"
repo_paths_fname=sys.argv[1] #path to text file containing paths in a repo
project_name=sys.argv[2] #e.g. guava
repo_path= repo_paths_fname[:repo_paths_fname.rfind(path_sep)] #path to repository folder (eg. to guava)
jars_path=current_path[:current_path.rfind(path_sep)]+path_sep+"project_jars"

print('Reading java file paths..')
with open(repo_paths_fname,'r') as f:
	data=f.readlines()
	size=len(data)
	path_lines=set() # to contain 50 file paths to java sources
	set_size=0
while(set_size<=200): # replace upper limit file number slightly greater than number of files needed
	path_num = randint(0, size)
	if path_num in path_lines:
		continue
	path_lines.add(data[path_num].strip())
	set_size=set_size+1
output_folder=repo_path+path_sep+project_name+"_rq1"
if not os.path.exists(output_folder):
	os.mkdir(output_folder)
ctr=1 #to maintain serial number for creating txt files for method info
print('Parsing java files to extract method information')
for p in path_lines:
	#call javaparser code with 2 inputs path of the file, sno for file naming
	path_to_file=repo_path+path_sep+p[2:] #to remove ./
	num_of_methods=1#randint(1,3)
	for i in range(num_of_methods):
		#print(path_to_file, "diff ",output_folder+path_sep+str(ctr))
		os.system("java -cp bin;"+jars_path+path_sep+"*;. ExtractMethodInfo \""+path_to_file+"\" \""+output_folder+path_sep+str(ctr)+"\"")
		ctr=ctr+1
os.chdir(repo_path)
for filename in os.listdir(output_folder): # returns txt files in relative path to dir
	#step 3
	with open(output_folder+path_sep+filename,'a+') as f:
		f.write('\n')
	with open(output_folder+path_sep+filename,'r') as f:
		method_info=json.load(f) #convert to dict form by loading in json format
		#keys: pathname, signature, doc, source, randomdocline 
		
		#change dir to github repo
		os.system("git log -S \""+method_info["randomdocline"]+"\" --pretty=format:'%h' >> "+output_folder+path_sep+filename) # append the commit ids
		
print('Finished method information extraction.')
