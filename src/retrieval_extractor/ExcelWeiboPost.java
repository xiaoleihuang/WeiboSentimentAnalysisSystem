package retrieval_extractor;

/**
 * @author xiaolei
 */
public class ExcelWeiboPost {
	private String id,content,date="null",keyword,uid;
	private double degree=0,type=0;
	public ExcelWeiboPost(String pid,String content,String date,String keyword,String uid,double degree,double type){
		this.id=pid;
		this.content=content;
		this.date=date;
		this.keyword=keyword;
		this.uid=uid;
		this.degree=degree;
		this.type=type;
	}
	
	public String getPostId(){
		return this.id;
	}
	public String getContent(){
		return this.content;
	}
	public String getDate(){
		return this.date;
	}
	public String getKeyword(){
		return this.keyword;
	}
	public String getUID(){
		return this.uid;
	}
	public double getDegree(){
		return this.degree;
	}
	public double getType(){
		return this.type;
	}
}