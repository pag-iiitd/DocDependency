import json
import sys
# Enter the filename you want to process
file = sys.argv[1]
filename = f'{file}_changedFunctions.json'

with open(filename) as f:
	init_database = json.load(f)

# Print total number of examples in dataset
print(len(init_database))

filtered_database = {}
count = 0

# Calculating the total number of examples with
# documentation changes
for i in init_database:
	if init_database[i]["Doc"] == True and init_database[i]["Code"] == False:
		count += 1
		contents = i.split("_")
		new_key = contents[0]
		for con in range(2, len(contents)):
			new_key += "_" + contents[con]
		filtered_database[new_key] = init_database[i]

print('files to be processed:', count)

D = {}
for i in filtered_database:
	commit_id = i.split('_')[0]
	if commit_id in D:
		D[commit_id] += 1
	else:
		D[commit_id] = 1
print('commits to be processed:', len(D))

# print('-----')
# for i in D:
# 	print(i)
# print('-----')

count1 = 0
for i in D:
	count1 += D[i]
print(count1)

# Creating a JSON Dump in pretty format
init_database = json.dumps(init_database, sort_keys=True, indent=4)
filtered_database = json.dumps(filtered_database, sort_keys=True, indent=4)

# Writing Back to the JSON File
with open(filename, "w") as outfile: 
    outfile.write(init_database)

# Writing Back the filtered database
with open(f'{file}_changedFunctionsFiltered.json', "w") as outfile: 
    outfile.write(filtered_database)

