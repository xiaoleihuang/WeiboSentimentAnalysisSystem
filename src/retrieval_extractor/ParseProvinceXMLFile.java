package retrieval_extractor;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParseProvinceXMLFile {
	List<Element> list=new ArrayList<Element>();
	
	public ParseProvinceXMLFile(List<Element> l){
		this.list=l;
	}
	
	/**
	 * @param pid The id of Province
	 * @return the name of Province
	 */
	public String SearchProvinceName(String pid){
		String name="";
		for(int i=0;i<list.size();i++){
			Element temp=list.get(i);
			System.out.println(temp.getAttribute("id"));
			if(Integer.valueOf(temp.getAttribute("id"))==Integer.valueOf(pid)){
				name=temp.getAttribute("name");
				break;
			}
		}
		return name;
	}
	
	/**
	 * @param pid The id of Province
	 * @param cid The id of City
	 * @return the Name of city
	 */
	public String SearchCityName(int pid,int cid){
		String name="";
		for(int i=0;i<list.size();i++){
			Element temp=list.get(i);
			if(Integer.valueOf(temp.getAttribute("id"))==pid){
				NodeList nl=temp.getElementsByTagName("city");
				//extract elements
				if(nl!=null&&nl.getLength()>0){
					for(int m=0;m<nl.getLength();m++){
						Element e=(Element)nl.item(i);
						if(Integer.valueOf(e.getAttribute("id"))==cid){
							name=e.getAttribute("name");
							break;
						}
					}
				}
			}
		}
		return name;
	}
	
	/**
	 * @param pid The id of Province
	 * @param cid The id of City
	 * @return the Name of city
	 */
	public String SearchAllName(int pid,int cid){
		String name="";
		for(int i=0;i<list.size();i++){
			Element temp=list.get(i);
			if(Integer.valueOf(temp.getAttribute("id"))==pid){
				name=temp.getAttribute("name");
				NodeList nl=temp.getElementsByTagName("city");
				//extract elements
				if(nl!=null&&nl.getLength()>0){
					for(int m=0;m<nl.getLength();m++){
						Element e=(Element)nl.item(i);
						if(Integer.valueOf(e.getAttribute("id"))==cid){
							name=name+" "+e.getAttribute("name");
							break;
						}
					}
				}
			}
		}
		return name;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XMLParser p=new XMLParser("province","provinces.xml");
		ParseProvinceXMLFile parser=new ParseProvinceXMLFile(p.getList());
		System.out.println(parser.SearchAllName(11, 1));
	}
}