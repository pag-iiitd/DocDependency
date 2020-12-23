
import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

public class ExtractMethodInfo extends GenericVisitorAdapter{
	static int pickmethod=-1;
	//keys: pathname, signature, doc, source, randomdocline 
    public static void main(String[] args) throws IOException {
        
    	String filepath=args[0];//absolute file path from repo
    	String output_to=args[1]+".txt";
    	//System.out.println(filepath);
    	//new DirExplorer((level, path, file) -> path.contentEquals(filepath), (level, path, file) -> {
        JSONObject sampleObject=new JSONObject();
        CompilationUnit classcode = StaticJavaParser.parse(new File(filepath),StandardCharsets.ISO_8859_1);
            Random rm=new Random();
            
            new VoidVisitorAdapter<Object>() {
			    @Override
			    public void visit(JavadocComment comment, Object arg) {
			        super.visit(comment, arg);
			        //System.out.println(comment);
			        if(pickmethod==1){return;}//only analyse one method
			         if (comment.getCommentedNode().isPresent()) {
			           pickmethod=rm.nextInt(2);//whether to log this method or not
			           if(pickmethod==1){
				        	String[] info=describe(comment.getCommentedNode().get());
				        	if(info!=null){
				        	try { 
				                sampleObject.put("pathname",filepath);
				                sampleObject.put("signature",info[0]);
				                sampleObject.put("doc",info[1]);
				                sampleObject.put("source",info[2]);
				                sampleObject.put("randomdocline",info[3]);
				                //System.out.println(sampleObject);
								Files.write(Paths.get(output_to), sampleObject.toJSONString().getBytes());
				            	
				 			} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
				 				}
				        	}
				        	//return;
			        } 
			      }
			    }
			}.visit(classcode, null);
            
          
    }

    //return sig,doc,source,randomdocline_string
    private static String[] describe(Node node) {
       Random rn=new Random();
    	if (node instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration)node;
            String[] op=new String[4];
            op[0]=methodDeclaration.getDeclarationAsString();
            op[1]=methodDeclaration.getJavadoc().get().toText();
            try{
            op[2]=methodDeclaration.getBody().get().toString();
            }
            catch(Exception e)
            {op[2]="";}
            //System.out.println(op[1]);
            String[] lines=op[1].replace("\r\n\r\n", "\r\n").split("\\r?\\n");
            int docline=rn.nextInt(lines.length);
            op[3]=lines[docline];
            //System.out.println(op[0]+" "+docline+" "+lines.length+op[3]);
            return op;
        }
        return null;
       }
    

}
