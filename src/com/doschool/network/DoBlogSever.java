package com.doschool.network;

import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;

public class DoBlogSever extends DoServer {
	public static final String TYPE = "microblog";
	
	//	说明：上传微博图片。
	//	参数：文件流
	//	返回：
	//	code：
	//	0（成功）
	//	9（网络服务错误）
	//	data：图片名（例如：FNOIFNSIUBVSOIDFJOS234.jpeg）
	public static MJSONObject MicroblogImageUpload(String imagePath) {
		return callFileSevice(TYPE, "MicroblogImageUpload", imagePath);
	}
	
	
	//	说明：发表微博。
	//	参数：usrId，tranMblogId，rootMblogId，mblogContent，mblogImages，topic（【新增】），schoolId（【新增】）
	//	  特殊参数json结构示例：
	//	mblogContent："{\"spans\":[],\"string\":\"\u5c31\u4e0d\u80fd\u56de\u5230\u57fa\u3002\"}"
	//	$mblogImages："[a.jpeg,b.jpeg]"
	//	返回：
	//	code：
	//	0（成功）
	//	1（无此用户）
	//	2（错误内容）
	//	3（错误学校）
	//	9（网络服务错误）
	//	data：新微博Id
	public static MJSONObject MicroblogSend(int usrId, int tranMblogId,int rootMblogId, String mblogContent,String topic, MJSONArray mblogImages) {
		String params="usrId=" + usrId + 
				"&tranMblogId=" + tranMblogId+
				"&rootMblogId=" + rootMblogId + 
				"&mblogContent=" + encoding(mblogContent) + 
				"&mblogImages=" + mblogImages.toString();
		if(topic!=null &&topic.length()>0)
			params+="&topic=" + encoding(topic);
			return callSevice(TYPE, "MicroblogSend",params);
	}

	
	//	说明：删除微博。
	//	参数：usrId，mblogId，delType（作为拓展，暂时全部删除都是级联删除）	
	//	返回：
	//	code：
	//	0（成功）
	//	1（无此用户）
	//	2（无此微博）
	//	3（用户无权限）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject MicroblogDelete(int usrId, int mblogId) {
		return callSevice(TYPE, "MicroblogDelete", 
				"usrId="+ usrId + 
				"&mblogId=" + mblogId);
	}
	
	
	//	说明：赞一条微博。
	//	参数：usrId，mblogId
	//	返回：
	//	code：
	//	0（成功）
	//	1（无此用户）
	//	2（无此微博）
	//	3（已经赞过）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject MicroblogZanSend(int usrId,int mblogId) {
		return callSevice(TYPE, "MicroblogZanSend",
				"usrId=" + usrId + 
				"&mblogId=" + mblogId);
	}
	
	
	
	//	说明：获取微博列表。
	//	参数：
	//	usrId，
	//	frdId（维持原样），
	//	lastId，
	//	contentType（1.最新  2.最热 3.所有好友 4.组织 5.某个人 6.某个话题 【新增】），
	//	objCount，
	//	topicStr（若类型选择为6，即某个话题时此字段有用。【新增】）
	//	schoolId（【新增】）
	//	返回：
	//	code：
	//	0（成功）
	//	1（无此用户）
	//	2（错误的类型）
	//	9（网络服务错误）
	//	data：
	//	[Array
	//		blogId:
	//		status:
	//		isZaned:
	//		
	//		launchTime:
	//		lauchPlace:【新增】
	//		
	//		blogContent;
	//		imageUrlList:
	//		imageUrlListHD:
	//		
	//		root;
	//		rootBlog;
	//		
	//		browseCount:【新增】
	//		commentCount:
	//		transCount:
	//		zanCount:
	//
	//		"author":
	//		{
	//			int personId;
	//			String funId;
	//			String sex;
	//			String headUrl;
	//			String nickName;
	//			String trueName;
	//			String remarkName;（暂时没做）
	//			int status;
	//			int cardState;
	//			int friendState;
	//		}
	//
	//		"medal":【新增】
	//		{
	//			int mid;
	//			int status;
	//			String theme;
	//			String intro;
	//			int type;
	//			String picUrl;
	//			long rewardTime;
	//			String rewardXuhao;
	//		}
	//
	//		"topic":【新增】
	//		{
	//			int tid;
	//			int createUsrId;
	//			int hot;
	//			int status;
	//			String topic;
	//		}
	//	]
	public static MJSONObject MicroblogListGet(int usrId, int friId,int contentType, int lastId, int objCount,String topicStr) {

		String topicString="";
		if(topicStr!=null)
			topicString=topicStr;
		
			return callSevice(TYPE, "MicroblogListGet",
					"usrId=" + usrId + 
					"&frdId=" + friId + 
					"&contentType=" + contentType + 
					"&lastId=" + lastId + 
					"&objCount=" + objCount+ 
					"&topicStr=" + topicString);
	}

	
	//	说明：得到一条微博详细内容。
	//	参数：usrId，mblogId
	//	返回：
	//	code：
	//	0（成功）
	//	1（无此微博）
	//	9（网络服务错误）
	//	data：
	//	{
	//		blogId:
	//		status:
	//		isZaned:
	//		
	//		launchTime:
	//		lauchPlace:【新增】
	//		
	//		blogContent;
	//		imageUrlList:
	//		imageUrlListHD:
	//		
	//		root;
	//		rootBlog;
	//		
	//		browseCount:【新增】
	//		commentCount:
	//		transCount:
	//		zanCount:
	//		"author":
	//		{
	//			int personId;
	//			String funId;
	//			String sex;
	//			String headUrl;
	//			String nickName;
	//			String trueName;
	//			String remarkName;
	//			int status;
	//			int cardState;
	//			int friendState;
	//		}
	//		"medal":【新增】
	//		{
	//			int mid;
	//			int status;
	//			String theme;
	//			String intro;
	//			int type;
	//			String picUrl;
	//			long rewardTime;
	//			String rewardXuhao;
	//		}
	//		"topic":【新增】
	//		{
	//			int tid;
	//			int pid;
	//			int hot;
	//			int status;
	//			String topic;
	//		}
	//	}
	public static MJSONObject MicroblogDetailGet(int usrId, int mblogId) {
			return callSevice(TYPE, "MicroblogDetailGet", 
					"usrId="+ usrId + 
					"&mblogId=" + mblogId);
	}
	
	
	//	说明：获得热门的微博话题
	//	参数：lastId objCount  schoolId
	//	返回：
	//	code：
	//	0（成功）
	//	1（LastId错误）
	//	9（网络服务错误）
	//	data：[{
	//	          "topicId" => 1  ,
	//	          "topicStr" => "新生来啦",
	//	          "hot" => 3.5,
	//	          "createUsrId" =>10046
	//			}
	//	      ]
	public static MJSONObject MicroblogTopicListGet(int lastId, int objCount) {
		return callSevice(TYPE, "MicroblogTopicListGet", 
				"lastId="+ lastId + 
				"&objCount=" + objCount);
}
	

	//	说明：发表评论。
	//	参数：usrId，rootMblogId，objId，rootCommentId，commentContent
	//	返回：
	//	code：
	//	0（成功）
	//	1（无此用户）
	//	2（错误的目标用户）
	//	3（无此微博）
	//	4（不和谐的评论内容）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject MicroblogCommentSend(int usrId, int objId,
			int rootMblogId, int rootCommentId, MJSONObject commentContent) {
			return callSevice(TYPE, "MicroblogCommentSend",
					"usrId=" + usrId + 
					"&objId=" + objId + 
					"&rootMblogId="+ rootMblogId + 
					"&rootCommentId=" + rootCommentId+ 
					"&commentContent=" + encoding(commentContent.toString()));
	}
	
	
	//	说明：获得微博评论列表。
	//	参数：rootMblogId，lastId，objCount，usrId（【新增】）
	//	返回：
	//	code：
	//	0（成功）
	//	1（无此微博）
	//	9（网络服务错误）
	//	data：
	//		[Array
	//			cId
	//			time
	//			content
	//			rootMblogId
	//			rootCommentId
	//			subPerson【修改】
	//			{
	//			}
	//			objPerson【修改】
	//			{
	//			}
	//		]
	public static MJSONObject MicroblogCommentListGet(int rootMblogId, int lastId, int objCount, int usrId) {
		return callSevice(TYPE, "MicroblogCommentListGet", 
						"rootMblogId="+ rootMblogId + 
						"&lastId=" + lastId + 
						"&objCount=" + objCount+ 
						"&usrId=" + usrId);
	}
	
}