package com.doschool.message;

import org.json.JSONException;
import org.json.JSONObject;

public class PushMessage {
	public static final String TAG = "PushMessage";
	// 编码的方式为 模块号+子模块号+顺序值 这样编码能够在区分整个应用程序的所有消息类型
	public static final int TYPE_GENERAL = 0x00000000;
	public static final int TYPE_USER_CARD_GET = 0x00002000;
	public static final int TYPE_USER_APPLY_GET = 0x00003000;
	public static final int TYPE_USER_APPLY_ACCEPT = 0x00003001;
	public static final int TYPE_USER_APPLY_REFUSE = 0x00003002;
	public static final int TYPE_MICROBLOG_AT = 0x00103000;
	public static final int TYPE_MICROBLOG_COMMENT = 0x00102000;
	public static final int TYPE_MICROBLOG_TRANSMIT = 0x00101000;
	public static final int TYPE_MICROBLOG_DELETE = 0x00101001;
	public static final int TYPE_MICROBLOG_COMMENT_AT = 0x00103001;
	public static final int TYPE_MICROBLOG_COMMENT_COMMENT = 0x00102001;
	public static final int TYPE_MICROBLOG_COMMENT_DELETE = 0x00102002;

	public int type;
	public String usrNick;

	public PushMessage(int type, String usrNick) {
		this.type = type;
		this.usrNick = usrNick;
	}

	public PushMessage(JSONObject jObj) throws JSONException {
		this.type = jObj.getInt("type");
		this.usrNick = jObj.getString("usrNick");
	}

	public String getMessage() {
		switch (type) {
		case TYPE_USER_CARD_GET:
			return usrNick + "向你发送了名片";
		case TYPE_USER_APPLY_GET:
			return usrNick + "申请加你为好友";
		case TYPE_USER_APPLY_ACCEPT:
			return "你和" + usrNick + "成为了好友";
		case TYPE_USER_APPLY_REFUSE:
			return null;
		case TYPE_MICROBLOG_AT:
			return usrNick + "在微博中@了你";
		case TYPE_MICROBLOG_COMMENT:
			return usrNick + "回复了你的微博";
		case TYPE_MICROBLOG_TRANSMIT:
			return usrNick + "转发了你的微博";
		case TYPE_MICROBLOG_DELETE:
			return usrNick + "删除了你的微博";
		case TYPE_MICROBLOG_COMMENT_AT:
			return usrNick + "@了你";
		case TYPE_MICROBLOG_COMMENT_COMMENT:
			return usrNick + "回复了你的评论";
		case TYPE_MICROBLOG_COMMENT_DELETE:
			return usrNick + "删除了你的评论";
		default:
			return "你有一条新的消息";
		}
	}

	public int[] getPosition() {
		switch (type) {
		case TYPE_USER_CARD_GET:
			return new int[] { 0, 1 };
		case TYPE_USER_APPLY_GET:
			return new int[] { 0, 2 };
		case TYPE_USER_APPLY_ACCEPT:
			return null;
		case TYPE_USER_APPLY_REFUSE:
			return null;
		case TYPE_MICROBLOG_AT:
			return new int[] { 1, 3 };
		case TYPE_MICROBLOG_COMMENT:
			return new int[] { 1, 3 };
		case TYPE_MICROBLOG_TRANSMIT:
			return new int[] { 1, 3 };
		case TYPE_MICROBLOG_DELETE:
			return new int[] { 1, 3 };
		case TYPE_MICROBLOG_COMMENT_AT:
			return new int[] { 1, 3 };
		case TYPE_MICROBLOG_COMMENT_COMMENT:
			return new int[] { 1, 3 };
		case TYPE_MICROBLOG_COMMENT_DELETE:
			return new int[] { 1, 3 };
		default:
			return null;
		}
	}
}
