package crawler.extractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parse XML Files
 * @author xiaolei
 * @version 1.0
 */
public class XMLExtractor {
	String elementName="";
	List<Element> list=new ArrayList<Element>();
	Document dom;
	/**
	 * @param eName Basic Unit name in XML File
	 * @param path XML file path
	 */
	public XMLExtractor(String eName,String path){
		this.elementName=eName;
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db=dbf.newDocumentBuilder();
			dom=db.parse(new File(path));
		}catch(IOException|ParserConfigurationException | SAXException e){
			e.printStackTrace();
		}
		
		//get root element
		Element docEle=dom.getDocumentElement();
		//get a list of unit elements
		NodeList nl=docEle.getElementsByTagName(eName);
		//extract elements
		if(nl!=null&&nl.getLength()>0){
			for(int i=0;i<nl.getLength();i++){
				Element e=(Element)nl.item(i);
				list.add(e);
			}
		}
	}
	
	public List<Element> getList(){
		return this.list;
	}
}