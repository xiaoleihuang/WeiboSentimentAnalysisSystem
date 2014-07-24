package model_segmentation;

import java.util.*;
import java.io.*;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
//import edu.stanford.nlp.trees.semgraph.SemanticGraph;
//import edu.stanford.nlp.trees.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.dictionary.Dictionary;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

@SuppressWarnings("unused")
public class mytest1 {
	public static void main(String[] args) throws IOException{
		
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
   	    Properties props = new Properties();
   	    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
    	StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
       	    
       	// read some text in the text variable
       	String text = new String();// Add your text here!
       	String line = new String();
       	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("testdoc_1")));
       	while((line=br.readLine())!=null)
       		text += line;
       	br.close();
       	
       	//list of terms and pos
       	ArrayList<String> termlist = new ArrayList<String>();
	    ArrayList<String> poslist = new ArrayList<String>();
       	    
       	// create an empty Annotation just with the given text
       	Annotation document = new Annotation(text);
       	    
       	// run all Annotators on this text
       	pipeline.annotate(document);
       	    
       	// these are all the sentences in this document
       	// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
       	List<CoreMap> sentences = document.get(SentencesAnnotation.class);
       	    
       	for(CoreMap sentence: sentences) {	        	      
       	    // traversing the words in the current sentence
       	    // a CoreLabel is a CoreMap with additional token-specific methods
       	    for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
       	        // this is the text of the token
       	        String word = token.get(TextAnnotation.class);
       	        termlist.add(word);		        	        		        	  
       	        // this is the POS tag of the token
       	        String pos = token.get(PartOfSpeechAnnotation.class);
       	        poslist.add(pos);
       	        // this is the NER label of the token
       	        String ne = token.get(NamedEntityTagAnnotation.class); 
       	    }
       	    // this is the parse tree of the current sentence
       	    Tree tree = sentence.get(TreeAnnotation.class);  
       	    
       	    TreeDisplay(tree);
       	    //List<Tree> children = tree.getChildrenAsList();
       	    //for(Tree child: children)
       	    	//TreeDisplay(child);
 
       	    // this is the Stanford dependency graph of the current sentence
       	    //SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
       	      
       	    //DependDisplay(dependencies);
       	      	    
       	    // This is the coreference link graph
       	    // Each chain stores a set of mentions that link to each other,
       	    // along with a method for getting the most representative mention
       	    // Both sentence and token offsets start at 1!
       	    Map<Integer, CorefChain> graph = 
       	      document.get(CorefChainAnnotation.class);
       	}
        
	}
	
	

	private static void TreeDisplay(Tree tree) {
		
		System.out.println(tree.toString());
		return;
	}
	
	/*
	private static void DependDisplay(SemanticGraph dependencies) {

		System.out.println(dependencies.toString());
		return;
	}
	*/
	
}
