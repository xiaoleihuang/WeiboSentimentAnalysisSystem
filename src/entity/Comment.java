package entity;

/**
 * one weibo post's one comment
 * @author xiaolei
 */
public class Comment {
	private String id;
	private String content;
	private String commenter;
	private String time;
	private String device;
	private int favorite=0;
	
	/**
	 * Set comment id
	 * @param id comment id
	 */
	public void setId(String id){
		this.id=id;
	}
	
	/**
	 * Get comment id
	 * @return id comment id
	 */
	public String getId(){
		return this.id;
	}
	
	/**
	 * Set comment content
	 * @param content comment content
	 */
	public void setContent(String content){
		this.content=content;
	}
	
	/**
	 * Get comment content
	 * @return comment content
	 */
	public String getContent(){
		return this.content;
	}
	
	/**
	 * Set commenter
	 * @param commenter
	 */
	public void setCommenter(String commenter){
		this.commenter=commenter;
	}
	
	/**
	 * Get commenter
	 * @return commenter
	 */
	public String getCommenter(){
		return this.commenter;
	}
	
	/**
	 * Set time
	 * @param time
	 */
	public void setTime(String time){
		this.time=time;
	}
	
	/**
	 * Get time
	 * @return time
	 */
	public String getTime(){
		return this.time;
	}
	
	/**
	 * Set device, which people used to post the comment
	 * @param device
	 */
	public void setDevice(String device){
		this.device=device;
	}
	
	/**
	 * Get device
	 * @return device
	 */
	public String getDevice(){
		return this.device;
	}
	
	/**
	 * Set count of favorite
	 * @param favorite
	 */
	public void setFavorite(int favorite){
		this.favorite=favorite;
	}
	
	/**
	 * Get favorite
	 * @return favorite
	 */
	public int getFavorite(){
		return this.favorite;
	}
}
