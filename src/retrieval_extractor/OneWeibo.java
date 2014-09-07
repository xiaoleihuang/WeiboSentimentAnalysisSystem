package retrieval_extractor;

/**
 * store one WeiBo post information, using different constructor to store two types of post, 
 * one is forward other's post and maybe write down some critics, the other is create his original post
 * @author xiaolei
 */
public class OneWeibo{
	private String content;
	private String date;
	private int type;
	private String fromWho;
	private String forwardReason;
	private String forwardChain;
	private String pid;
	private String suicide;
	
	/**
	 * Store one post's information, this post is original post
	 * @param c the content of post
	 * @param d the date of post, like 2014-01-01:15:30,that would be 201401011530
	 * @param t the type of post, original is 0, forward is 1; this one is 0
	 */
	public OneWeibo(String c,String d, int t){
		this.content=c;
		this.date=d;
		this.type=t;
	}
	/**
	 * Store one post's information, this post is forward post
	 * @param c original content of the forward
	 * @param d the date of post, like 2014-01-01:15:30
	 * @param t the type of post, original is 0, forward is 1; this one is <b>1</b>
	 * @param fw the forwarded people's account name, from who.
	 * @param fR forward Reason for each forward post
	 * @param fC forward Chain for recording everyone who has previously forwarded this post
	 */
	public OneWeibo(String c,String d, int t,String fw,String fR,String fC){
		this.content=c;
		this.date=d;
		this.type=t;
		this.fromWho=fw;
		this.forwardChain=fC;
		this.forwardReason=fR;
	}
	
	/**
	 * @param content
	 * @param date the date of post
	 * @param pid post id
	 */
	public OneWeibo(String content, String date, String pid) {
		// TODO Auto-generated constructor stub
		this.content=content;
		this.date=date;
		this.pid=pid;
	}
	
	/**
	 * 
	 * @param content
	 * @param pid post id
	 * @param type 1 for original post, 0 for forward post
	 * @param date
	 */
	public OneWeibo(String content,String pid,String type,String date){
		this.content=content;
		this.pid=pid;
		this.type=Integer.parseInt(type);
		this.date=date;
	}
	
	/**
	 * Constructor
	 * @param content
	 * @param pid post id
	 * @param type 1 for original post, 0 for forward post
	 * @param date
	 * @param suicide 1 for suicidal post, 0 for none;
	 */
	public OneWeibo(String content,String pid,String type,String date,String suicide){
		this.content=content;
		this.pid=pid;
		this.type=Integer.parseInt(type);
		this.date=date;
		this.suicide=suicide;
	}
	
	/**
	 * @return post's id
	 */
	public String getPid(){
		return this.pid;
	}
	/**
	 * posts' content, for forward post, this is original post's information, not containing current user's opinions
	 * @return
	 */
	public String getContent(){
		return this.content;
	}
	
	/**
	 * @return when posts this post
	 */
	public String getDate(){
		return this.date;
	}
	
	/**
	 * @return the type of post, original is 0, forward is 1; original one is 0
	 */
	public int getType(){
		return this.type;
	}
	
	/**
	 * @return the former person who posts this
	 */
	public String getFromWho(){
		return this.fromWho;
	}
	
	/**
	 * @return the reason why user forward this post
	 */
	public String getForwardReason(){
		return this.forwardReason;
	}
	
	/**
	 * @return who also forward this post previously
	 */
	public String getForwardChain(){
		return this.forwardChain;
	}
	
	/**
	 * @return post is suicidal or not
	 */
	public String getSuicide(){
		return this.suicide;
	}
}