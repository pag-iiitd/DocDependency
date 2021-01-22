import git
import json
import random
import sys
from timeit import default_timer as timer

# pip install progress
from progress.bar import Bar 		# used for tracking the progress

'''
	Function to get the parent log_ids of
	the given log
'''
def get_parent(repo, log_id):
	parent_id = repo.log(['--pretty=format:%p', '-n', '1', log_id])
	return parent_id

'''
	To get all the insertions and deletions
	made in a particular file between two 
	commits.
	INPUT PARAMETERS:
	commit1: [String]
	commit2: [String]
	filename: [String]

	RETURN VALUE: [tuple]
	insertions [List]: Inserted Line Numbers in filename from commit1
	deletions [List]: Deleted Line Numbers in filename from commit2
'''
def get_line_differences (commit1, commit2, filename):
	# Git difference to get the changes
	diff_file = repo.diff('--no-prefix', '-U10000', commit1, commit2, filename)
	diff_file = diff_file.split('\n')[5:]

	# Initially both the line numbers are one 
	insertion_line_nums = 1
	deletion_line_nums = 1

	insertions = []
	deletions = []

	# Iterating through the diff file
	for line_number in range (len(diff_file)):
		# Checking if there was an insertion, deletion or nothing
		if (diff_file[line_number][0] == '-'):
			insertions.append(insertion_line_nums)
			insertion_line_nums += 1
		elif (diff_file[line_number][0] == '+'):
			deletions.append(deletion_line_nums)
			deletion_line_nums += 1
		else:
			insertion_line_nums += 1
			deletion_line_nums += 1

	return insertions, deletions

if __name__ == '__main__':

	start = timer()
	# Path to git repo
	path = sys.argv[1]
	repo = git.Git(path)

	# Getting Logs for 2 years
	log_ids = repo.log(['--since', '"JUN 16 2018"', '--until', '"JUN 16 2020"', '--pretty=format:%h', '--no-merges']).split("\n")

	bar = Bar('Processed Commits', max = len(log_ids))

	# Empty dictionary to store the commit line numbers
	commit_line_numbers = {}

	# Iterating over the commit history
	for log_id in log_ids:
		# Getting parent of a given log
		parent = get_parent(repo, log_id)

		# Get files changed in the given commit
		files_altered = repo.show('--pretty=format:""',  '--name-only', log_id).split("\n")

		for file in files_altered:
			# skipping all the test files
			if (file.lower().find('test') > -1 or file.lower().find('.java') < 0):
				continue

			# Iterating over all the parents
			commit1 = log_id
			commit2 = parent
			filename = file

			key = f'{commit1}_{commit2}_{filename}'
			# trying to get insertions and deletions for the given filename
			try:
				insertions, deletions = get_line_differences(commit1, commit2, filename)
				commit_line_numbers[key] = insertions, deletions
				pass
			# Exception occurs if a file was deleted in the current commit or if it is not present in previous commit
			except Exception as e:
				pass
			else:
				pass
			finally:
				pass
		bar.next()
	bar.finish()

	# Creating a json object
	json_object = json.dumps(commit_line_numbers, indent=4)
	# Writing to sample.json 
	with open(f"{path}_last_2_years.json", "w") as outfile: 
	    outfile.write(json_object)
	end = timer()
	print(f"Total Time taken = {end - start}")