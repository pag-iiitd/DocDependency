import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.Scanner;

import org.json.simple.*;
import org.json.simple.parser.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ExtractCodeDocModularity {
	// Public Arrays to maintain insertions and deletions
	ArrayList<Long> insertions = new ArrayList<Long>();
	ArrayList<Long> deletions = new ArrayList<Long>();
	
	static Scanner input = new Scanner(System.in); 
	
	static String gitdir;
	// JSON Object to store the loaded json file
	JSONObject jsonObject;
	
	// Total number of changed functions
	int functionsChanged = 0;
	
	// Constructor corresponding to the object
	public ExtractCodeDocModularity(String source) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		this.jsonObject = (JSONObject) parser.parse(new FileReader(source));
	}
	
	/*
	 * Function to fill in insertions and deletions given
	 * the json_object and the String Key
	 */
	public void fillInsertionsDeletions (String key) {
		insertions.clear();
		deletions.clear();
		
		JSONArray json_insertions = (JSONArray) ((JSONArray) jsonObject.get(key)).get(0);
	    JSONArray json_deletions = (JSONArray) ((JSONArray) jsonObject.get(key)).get(1);
		
	    // Filling up insertions and deletions corresponding
	    // to the key in the JSON Object
	    for (int i = 0; i < json_insertions.size(); i++){ 
	    	insertions.add((Long) json_insertions.get(i));
	    }
	    
	    for (int i = 0; i < json_deletions.size(); i++){ 
	    	deletions.add((Long) json_deletions.get(i));
	    }
	    return;
	}
	
	/*
	 * Returns the source code corresponding to the
	 * Commit Hash and filename. Makes use of the exec
	 * command to return the following things.
	 */
	public String getSourceCode (String commitHash, String filename) throws IOException {
		// Enter the path of github library you want to process
//		String gitdir = "C:/Users/Avyakt/Desktop/project_folder/elasticsearch/.git";
		String command = "git --git-dir=" + gitdir + " show " + commitHash + ":" + filename;
		ProcessBuilder builder = new ProcessBuilder();
		
		// Replace cmd with your command line client
		builder.command("cmd.exe", "/c", command);
		builder.redirectErrorStream(false);
		Process process = builder.start();
		InputStream is = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		// Iterating over the returned code to process everything
		String line = "";
		String sourceCode = "";
		while ((line = reader.readLine()) != null) {
		   sourceCode += line + "\n";
		}
		
		// Returning the source code
		return sourceCode;
	}
	
	
	public HashMap<String, ArrayList<Boolean>> getChanges(String SourceCode, ArrayList<Long> changes) 
	{	
		// ArrayList to maintain whether the code or documentation was changed
		HashMap<String, ArrayList<Boolean>> changedMethods = new HashMap<>();
		
		// No need to iterate in case changes are not made
		// Decrease the computational complexity
		if (changes.size() == 0) 
			return changedMethods;
		
		// Compilation unit
		CompilationUnit cu = StaticJavaParser.parse(SourceCode);
		
		// To iterate over changes 
		int change_index = 0;
		int change_end = changes.size();
		
		// Iterating over the complete code
		for (TypeDeclaration typeDec : cu.getTypes()) {
//			List<> members = typeDec.getMembers()
	        List<BodyDeclaration> members = typeDec.getMembers();
	        
	        if (members != null) {
	            for (BodyDeclaration member : members) {
	            	
	            	try {
	            		// Signature for the method/interface/class
	            		String signature = "";
	            		
	            		// Starting and ending part of code and documentation
	                    int startCode = -1;
	                    int endCode = -1;
	                    int startDoc = -1;
	                    int endDoc = -1;
	                    
	                    // Booleans to check if there was a change in doc or code
	                    boolean changeInDoc = false;
	                    boolean changeInCode = false;
	                    
	            		// If reference is a method declaration identify if the method was changed
	            		if (member.isMethodDeclaration()) {
	            			MethodDeclaration field = (MethodDeclaration) member;
		                    signature = field.getDeclarationAsString(true, false, true);
		                    
		                    
		                    // If function body is present find it's start and end
		                    if (field.getBody().isPresent()) {
		                    	startCode = field.getBody().get().getBegin().get().line;
		                    	endCode = field.getBody().get().getEnd().get().line;
		                    }
		                    
		                    // If documentation is present find it's start and end
		                    if (field.getComment().isPresent()) {
		                    	startDoc = field.getComment().get().getBegin().get().line;
		                    	endDoc = field.getComment().get().getEnd().get().line;
		                    }
		            	}
	            		
	            		// Check for an update in the constructor
	            		if (member.isConstructorDeclaration()) {
	            			ConstructorDeclaration field = (ConstructorDeclaration) member;
		                    signature = field.getDeclarationAsString(true, false, true);
		                    
		                    // If constructor body is present find it's start and end
		                    if (field.getBody() != null) {
		                    	startCode = field.getBody().getBegin().get().line;
		                    	endCode = field.getBody().getEnd().get().line;
		                    }
		                    
		                    // If documentation is present find it's start and end
		                    if (field.getComment().isPresent()) {
		                    	startDoc = field.getComment().get().getBegin().get().line;
		                    	endDoc = field.getComment().get().getEnd().get().line;
		                    }
		            	}
	            		
	            		
	            		// Iterating over the array of changes since parser is working chronologically
	            		while (change_index < change_end && changes.get(change_index) <= (long) endDoc) {
	            			if (changes.get(change_index) <= (long) endDoc && changes.get(change_index) >= (long) startDoc)
                    			changeInDoc = true;
	            			change_index++;
	            		}
	            		
	            		while (change_index != 0 && changes.get(change_index - 1) >= (long) startDoc && startDoc != -1) {
	                    	change_index--;
	                    }
	            		
	                    while (change_index < change_end && changes.get(change_index) <= (long) endCode) {
	                    	if (changes.get(change_index) <= (long) endCode && changes.get(change_index) >= (long) startCode)
	                    		changeInCode = true;
                    		if (changes.get(change_index) <= (long) endDoc && changes.get(change_index) >= (long) startDoc)
                    			changeInDoc = true;
                    		change_index ++;
	                    }
	                    
	                    // Resetting the changes to begin_index because there might be some methods inside the interface/class
	                    while (change_index != 0 && changes.get(change_index - 1) >= (long) startCode && startCode != -1) {
	                    	change_index--;
	                    }
	                    
	                    // If there was a change in either the doc or code add to the hashmap
	                    if (changeInDoc || changeInCode) {
	                    	ArrayList<Boolean> changesMade = new ArrayList<>();
	                    	changesMade.add(changeInDoc);
	                    	changesMade.add(changeInCode);
	                    	changedMethods.put(signature, changesMade);
	                    }
	            	}
	            	catch (Exception e) {
	            		System.out.println("There was an exception");
	            	}
	            	// If we reach end of the changes array then break the parser loop
	            	if (change_index >= change_end) {
	            		break;
	            	}
	            }
	        }
	    }
		// Return all the changed methods
		return changedMethods;
	}
	
	/*
	 * Given a string key of the format commit1_commit2_filename
	 * the following function returns the hashmap of all the method 
	 * declarations changed in the code.
	 */
	public HashMap<String, ArrayList<Boolean>> processKey (String key) {
		HashMap<String, ArrayList<Boolean>> changedMethods = new HashMap<>();
		
		// Make sure a java file is being processed
		if (!key.contains(".java")) 
			return changedMethods;
		
		// Setting the insertions and deletions corresponding
		// to the key in JSONObject
		fillInsertionsDeletions(key);
		
		// indices of two underscores
		int index1 = -1;
		int index2 = -1;
		
		// Setting indices of two underscores for getting commits
		// and the filenames
		for (int i = 0; i < key.length(); i++) {
			if (key.charAt(i) == '_' && index1 == -1)
				index1 = i;
			else if (key.charAt(i) == '_' && index2 == -1){
				index2 = i;
				break;
			}
		}
		
		// Parsing Commit Hashes and Filename from the keys
		String afterCommitHash = key.substring(0, index1);
		String beforeCommitHash = key.substring(index1 + 1, index2); 
		String filename = key.substring(index2 + 1);

		// Mainting changes made befor and after commit in the file
		HashMap<String, ArrayList<Boolean>> afterCommitChanges = new HashMap<>();
		HashMap<String, ArrayList<Boolean>> beforeCommitChanges = new HashMap<>();
		
		// Getting source code and insertions after commit
		if (insertions.size() > 0) {
			String afterCommitSource = "";
			try {
				afterCommitSource  = getSourceCode(afterCommitHash, filename);
			}
			catch (Exception e) {
				System.out.println("Loading Error: " + key);
			}
			try {
				afterCommitChanges = getChanges(afterCommitSource, insertions);
			}
			catch (Exception e) {
				System.out.println("Unprocessed Key: " + key);
			}
		}
		
		// Getting source code and deletions before commit
		if (deletions.size() > 0 ) {
			String beforeCommitSource = "";
			try {
				beforeCommitSource = getSourceCode(beforeCommitHash, filename);
			}
			catch (Exception e) {
				System.out.println("Loading Error: " + key);
			}

			try {
				beforeCommitChanges = getChanges(beforeCommitSource, deletions);
			}
			catch (Exception e) {
				System.out.println("Unprocessed Key: " + key);
			}
		}
		
		// Merging the changes made before and after commits
		for (Entry<String, ArrayList<Boolean>> entry: afterCommitChanges.entrySet()) {
			String method_key = entry.getKey();
			ArrayList<Boolean> method_value = entry.getValue();
			
			if (beforeCommitChanges.containsKey(method_key)) {
				method_value.set(0, method_value.get(0) | beforeCommitChanges.get(method_key).get(0));
				method_value.set(1, method_value.get(1) | beforeCommitChanges.get(method_key).get(1));
			}
			method_key = afterCommitHash + "_" + beforeCommitHash + "_" +  filename + "_" + method_key;
			changedMethods.put(method_key, method_value);
			functionsChanged++;
		}
		
		for (Entry<String, ArrayList<Boolean>> entry: afterCommitChanges.entrySet()) {
			String method_key = entry.getKey();
			ArrayList<Boolean> method_value = entry.getValue(); 
			if (afterCommitChanges.containsKey(method_key)) {
				continue;
			}
			method_key = afterCommitHash + "_" + beforeCommitHash + "_" +  filename + "_" + method_key;
			changedMethods.put(method_key, method_value);
			functionsChanged++;
		}
		
		// Returning a hashmap of all the changed methods
		return changedMethods;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException { 
		String project_name, location, source, destination;
		System.out.println("All the paths can be either absolute or relative");
		System.out.print("Enter the project name: ");
		project_name = input.nextLine(); // guava
		
		System.out.print("Enter the location of directory where output of getting_line_numbers.py is present: ");
		location = input.nextLine(); // Example: C:/Users/Avyakt/Desktop/project_folder/
		
		source = location + project_name + "_last_2_years.json";
		destination = location + project_name + "_changedFunctions.json";
		
		System.out.print("Enter the path where .git file for the repo is saved: ");
		gitdir = input.nextLine(); // Example: C:/Users/Avyakt/Desktop/project_folder/guava/.git
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("Project Name: " + project_name);
		System.out.println("Source File: " + source);
		System.out.println("Destination File: " + destination);
		System.out.println("Path to the .git file for the project is: " + gitdir);
		// Class instance
		
		ExtractCodeDocModularity ext = new ExtractCodeDocModularity(source);
		
		// Files Processed and total files
		int files_processed = 0;
		int total_files = ext.jsonObject.size();
		
		// A hashmap to maintain all the changed Methods
		HashMap<String, ArrayList<Boolean>> changedMethodsFromJson = new HashMap<>();
		Iterator<?> keys = ext.jsonObject.keySet().iterator();
		
		while(keys.hasNext()) {
			// Converting the key to string 
			if (files_processed % 20 == 0)
				System.out.println("Progress: " + files_processed + "/" + total_files + ", Changed Functions: " + ext.functionsChanged);
			String key = (String)keys.next();
			changedMethodsFromJson.putAll(ext.processKey(key));
			files_processed++;
		}
		System.out.println("Progress: " + files_processed + "/" + total_files + ", Changed Functions: " + ext.functionsChanged);
		
		JSONObject json = new JSONObject();
		
		for (Entry<String, ArrayList<Boolean>> entry: changedMethodsFromJson.entrySet()) {
			JSONObject temp = new JSONObject();
			temp.put("Code", entry.getValue().get(1));
			temp.put("Doc", entry.getValue().get(0));
			json.put(entry.getKey(), temp);
		}
		
		try {
	         FileWriter file = new FileWriter(destination);
	         file.write(json.toJSONString());
	         file.close();
		} catch (IOException e) {
	         e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println(project_name + ", Time Taken = " + (((double)(endTime - startTime)))/1000);
		System.out.println("Code Terminated");
	}
}
