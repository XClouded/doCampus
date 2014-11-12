package com.doschool.network;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.doschool.zother.MJSONObject;

public class DoUserSever extends DoServer {
	public static final String TYPE = "user";
	
	
	// 说明：上传个人主页背景图。
	// 参数：文件流
	// 返回：
	// code：
	// 0（成功）
	// 9（网络服务错误）
	// data：NULL
	public static MJSONObject UserUploadBackgroundPic(String imagePath) {
		return callFileSevice(TYPE, "UserUploadBackgroundPic", imagePath);
	}
	
	
	//	说明：上传个人背景。
	//	参数：usrId、bgName
	//	返回：
	//	code：
	//	0（成功）
	//	1（没有该用户）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject UserUpdateBackgroundPic(int usrId, String bgName) {
		return callSevice(TYPE, "UserUpdateBackgroundPic", "usrId=" + usrId + "&bgName=" + bgName);
	}

	
	//	说明：检查一个用户是否是某学校的学生。
	//	参数：funId、funPasswd、schoolId
	//	返回：
	//	code：
	//	0（成功）
	//	1（该用户不是该大学的学生）
	//	2（该用户已经注册）
	//	3（学校不存在）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject UserCheck(String funId, String funPasswd) {
		return callSevice(TYPE, "UserCheck", "funId=" + encoding(funId) + "&funPasswd=" + encoding(funPasswd));
	}
	
	
	//	说明：注册新用户。
	//	参数：funId、usrName、usrNick、usrPasswd、schoolId、sex、usrHead
	//	返回：
	//	code：
	//	0（成功）
	//	1（用户已经注册过）
	//	2（该昵称已经存在）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject UserRegister(String funId, String usrName, String usrNick, String usrPasswd, String sex) {
		return callSevice(TYPE, "UserRegister", "funId=" + funId + "&usrName=" + encoding(usrName) + "&usrNick=" + encoding(usrNick) + "&usrPasswd=" + encoding(usrPasswd) + "&sex=" + sex);

	}
	
	
	//	说明：修改密码。
	//	参数：funcId（学号）、password（新密码）、schoolId
	//	返回：
	//	code：
	//	0（成功）
	//	1（没有该用户）
	//	9（网络服务错误）
	//	data：usrId
	public static MJSONObject UserAlterPassword(String funcId, String password) {
		return callSevice(TYPE, "UserAlterPassword", "funcId=" + funcId + "&password=" + encoding(password));
	}
	
	
	//	说明：用户登录。
	//	参数：funcId、password、schoolId
	//	返回：
	//	code：
	//	0（成功）
	//	1（用户不存在）
	//	2（用户名或密码不正确）
	//	9（网络服务错误）
	//	data：uid
	public static MJSONObject UserLogin(String funcId, String password) {
		return callSevice(TYPE, "UserLogin", "funcId=" + encoding(funcId) + "&password=" + encoding(password));
	}
	

	//	说明：获取用户完全信息。
	//	参数：myId（自己的id）、usrId（需要获取的用户的id）
	//	返回：
	//	code：
	//	0（成功）
	//	1（两个用户均不存在）
	//	2（自己不存在）
	//	3（目标不存在）
	//	9（网络服务错误）
	//	data：
	//		{
	//		"base":【修改】
	//			{
	//				int personId;
	//				String funId;
	//				String sex;
	//				String headUrl;
	//				String nickName;
	//				String trueName;
	//				String remarkName;
	//				int status;
	//				int cardState;
	//				int friendState;
	//			}
	//		String phoneNumber;
	//		String qq;
	//		String email;
	//		String intro;
	//		String background;
	//
	//		int blogCount;
	//		double activeValue ;
	//		double popValue ;
	//		int contriValue;	
	//		}
	public static MJSONObject UserCompleteInfoGet(int myId, int usrId) {
		return callSevice(TYPE, "UserCompleteInfoGet", "myId=" + myId + "&usrId=" + usrId);
	}

	//	说明：更改用户信息。
	//	参数：usrId、nick、phone、qq、mail、intro、head【新增】
	//	返回：
	//	code：
	//	0（成功）
	//	1（用户不存在）
	//	2（不合法的昵称）
	//	3（昵称已经存在）
	//	9（网络服务错误）
	//	data：最新的用户完全信息
	public static MJSONObject UserInfoUpdate(int usrId, String nick, String head, String phone, String qq, String mail, String intro) {
		return callSevice(TYPE, "UserInfoUpdate", "usrId=" + usrId + "&nick=" + encoding(nick) + "&head=" + head + "&phone=" + encoding(phone) + "&qq=" + encoding(qq) + "&mail=" + encoding(mail)
				+ "&intro=" + encoding(intro));
	}
	
	
	//	说明：搜索用户。
	//	参数：usrId、objNick、schoolId
	//	返回：
	//	code：
	//	0（成功）
	//	1（自己不存在）
	//	9（网络服务错误）
	//	data：
	//		[Array【修改】
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
	//		]
	public static MJSONObject UserSearch(int usrId, String objNick) {
		return callSevice(TYPE, "UserSearch", "usrId=" + usrId + "&objNick=" + encoding(objNick));
	}
	
	//	说明：发好友请求。
	//	参数：usrId、objId、applyReason（申请理由）
	//	返回：
	//	code：
	//	0（成功）
	//	1（两个都不存在）
	//	2（自己不存在）
	//	3（目标不存在）
	//	4（已经发送过请求）
	//	9（网络服务错误）
	//	data：最新的好友关系 如：2
	public static MJSONObject UserApplySend(int usrId, int objId, String applyReason) {
		return callSevice(TYPE, "UserApplySend", "usrId=" + usrId + "&objId=" + objId + "&applyReason=" + encoding(applyReason));
	}
	
	
	//	说明：发小纸条（不变）。
	//	参数：fromUsrId toUsrId sendTime content 
	//	返回：
	//	code：
	//	0（成功）
	//	1（无发送人）
	//	2（无收到人）
	//	3（错误时间）
	//	4（不合法内容）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject UserScripSend(int fromUsrId, int toUsrId, String content) {
		String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return callSevice(TYPE, "UserScripSend", "fromUsrId=" + fromUsrId + "&toUsrId=" + toUsrId + "&sendTime=" + sendTime + "&content=" + encoding(content));
	}
	
	
	//	说明：获取用户的好友/名片列表。
	//	参数：usrId(发起人Id)、type(0代表好友，1代表名片)
	//	返回：
	//	code：
	//	0（成功）
	//	1（发起人不存在）
	//	2（错误的类型）
	//	9（网络服务错误）
	//	data：
	//	[Array【修改】
	//		int personId;
	//		String funId;
	//		String sex;
	//		String headUrl;
	//		String nickName;
	//		String trueName;
	//		String remarkName;
	//		int status;
	//		int cardState;
	//		int friendState;
	//	]
	public static MJSONObject UserFriendAndCardListGet(int usrId, int type) {
		return callSevice(TYPE, "UserFriendAndCardListGet", "usrId=" + usrId + "&type=" + type);
	}
	
	
	//	说明：获取收到的好友申请列表。
	//	参数：usrId(发起人Id)、lastId、objCount
	//	返回：
	//	code：
	//	0（成功）
	//	1（发起人不存在）
	//	2（错误的类型）
	//	9（网络服务错误）
	//	data：
	//	[Array【修改】
	//		"id":好友申请的id，不是人，是两人之间的关系表的rid
	//		"time":1243545123,
	//		"reason":"这里是申请的理由"
	//		launchPerson
	//		{
	//		}
	//	]
	public static MJSONObject UserApplyListGet(int usrId, int lastId, int objCount) {
		return callSevice(TYPE, "UserApplyListGet", "usrId=" + usrId + "&lastId=" + lastId + "&objCount=" + objCount);
	}
	
	//	说明：好友关系处理。
	//	参数：applyId、handleCode（2接收申请,3拒绝申请,4删除好友）
	//	返回：
	//	code：
	//	0（成功）
	//	1（发起id不存在）
	//	2（不合法的代码）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject UserApplyHandle(int applyId, int handleCode) {
		return callSevice(TYPE, "UserApplyHandle", "applyId=" + applyId + "&handleCode=" + handleCode);
	}
	
	//	说明：获取用户的好友/名片列表。
	//	参数：usrId(发起人Id)、deviId
	//	返回：
	//	code：
	//	0（成功）
	//	9（网络服务错误）
	//	data：NULL
	public static MJSONObject UserLoginPush(int usrId, String deviId) {
		return callSevice(TYPE, "UserLoginPush", "usrId=" + usrId + "&deviId=" + deviId);
	}
	
	//	说明：获取消息盒子列表GetMsgList。
	//	参数：usrId(发起人Id)、lastMsgId、objCount
	//	返回：
	//	code：
	//	0（成功）
	//	1（无用户）
	//	2（错误的类型）
	//	9（网络服务错误）
	//	data：[Array
	//			msgId
	//			msgType
	//			msgState
	//			msgPerson【新增】
	//			{
	//			}
	//			blogId
	//			oldContent
	//			newContent
	//			timerootMblog（原来就有，不变）
	//			newMblog（原来就有，不变）
	//		]
	public static MJSONObject UserMsgListGet(int usrId, int lastMsgId, int objCount) {
		return callSevice(TYPE, "UserMsgListGet", "usrId=" + usrId + "&lastMsgId=" + lastMsgId + "&objCount=" + objCount);
	}
	
	//	说明：用户上传头像。
	//	参数：文件流
	//	返回：
	//	code：
	//	0（成功）
	//	9（网络服务错误）
	//	data：头像名称。
	public static MJSONObject UserHeadUpload(String imagePath) {
		return callFileSevice(TYPE, "UserHeadUpload", imagePath);
	}

	public static MJSONObject CommonPicGet(int type) {
		return callSevice(TYPE, "CommonPicGet", "usrId=" + type);
	}
	




	


	//	说明：发送名片。
	//	参数：usrId、objId
	//	返回：
	//	code：
	//	0（成功）
	//	1（两个都不存在）
	//	2（自己不存在）
	//	3（目标不存在）
	//	4（已经发送过名片）
	//	9（网络服务错误）
	//	data：最新的名片关系 如：2
	public static MJSONObject UserCardSend(int usrId, int objId) {
		return callSevice(TYPE, "UserCardSend", "usrId=" + usrId + "&objId=" + objId);
	}


	//	说明：用户获得小纸条列表。
	//	参数：usrId、lastId、objCount
	//	返回：
	//	code：
	//	0（成功）
	//	1（没有该用户）
	//	9（网络服务错误）
	//	data：Array[
	//	{'xid' => NULL,
	//	     'sendTime' => NULL,
	//	     'content' => NULL,
	//	     'author' => NULL,
	//	     'toPerson' => NULL,
	//	     'status' => NULL}
	//		]
	public static MJSONObject UserScripListGet(int usrId, int lastId, int objCount) {
		return callSevice(TYPE, "UserScripListGet", "usrId=" + usrId + "&lastId=" + lastId + "&objCount=" + objCount);
	}

	//
	public static MJSONObject FriendRemove(int usrId, int objId) {
		return callSevice(TYPE, "FriendRemove", "fromUsrId=" + usrId + "&toUsrId=" + objId);
	}


}