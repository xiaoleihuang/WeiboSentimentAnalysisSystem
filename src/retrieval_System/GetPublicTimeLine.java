package retrieval_System;

import java.util.List;

import retrieval_receiver.GetPublicLatestWeibo;
import weibo4j.model.Status;
/**
 * Test, get latest posts data as JSon format
 * @author xiaolei
 * @example Status [user=User [id=3850742817, screenName=红藕藕藕女郎, name=红藕藕藕女郎, province=21, city=1, location=辽宁 沈阳, description=, url=, profileImageUrl=http://tp2.sinaimg.cn/3850742817/50/5676624139/0, userDomain=, gender=f, followersCount=1315, friendsCount=1357, statusesCount=1279, favouritesCount=0, createdAt=Sun Oct 13 17:32:08 CST 2013, following=false, verified=false, verifiedType=-1, allowAllActMsg=false, allowAllComment=true, followMe=false, avatarLarge=http://tp2.sinaimg.cn/3850742817/180/5676624139/0, onlineStatus=0, status=null, biFollowersCount=1163, remark=null, lang=zh-cn, verifiedReason=, weihao=235671041, statusId=], idstr=3710983896106730, createdAt=Fri May 16 22:36:07 CST 2014, id=3710983896106730, text=【High到无法直视！大妈火车上狂飙广场舞】“亚洲女子天团”火车上battle！人类已经无法阻止广场舞了，大妈彪悍的人生不解释爱！咋！咋！地！ http://t.cn/RvvH2fP, source=Source [url=http://app.weibo.com/t/feed/3auC5p, relationShip=nofollow, name=皮皮时光机], favorited=false, truncated=false, inReplyToStatusId=-1, inReplyToUserId=-1, inReplyToScreenName=, thumbnailPic=, bmiddlePic=, originalPic=, retweetedStatus=null, geo=null, latitude=-1.0, longitude=-1.0, repostsCount=0, commentsCount=0, mid=3710983896106730, annotations=, mlevel=0, visible=Visible [type=0, list_id=0]]
 */
public class GetPublicTimeLine {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetPublicLatestWeibo latestWeibo=new GetPublicLatestWeibo();
		List<Status> statuses=latestWeibo.getLastestStatus();
		for(Status s:statuses){
			System.err.println(s.toString());
		}
	}
}