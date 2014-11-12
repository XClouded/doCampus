package com.doschool.message;
//package us.dobell.doschool.message;
//
//import org.json.JSONObject;
//
//import us.dobell.doschool.user.UserServer;
//import us.dobell.xtools.XServer;
//
///**
// * 编号0x002 消息子系统网络接口
// * 
// * @author xxx
// * 
// */
//public class MessageServer extends XServer {
//	public static final String TAG = "MessageServer";
//	public static final String MESSAGE_KEY = "Uzb29hpFGvDRrbaKh1XG5V9m";
//
//	/**
//	 * 通用模块0x00
//	 */
//
//	/**
//	 * 编号 0x0020000 登录推送服务器
//	 * 
//	 * @param usrId
//	 *            用户的应用程序ID
//	 * @param devId
//	 *            用户的设备码
//	 * @return <h1>无</h1>
//	 * 
//	 *         <pre>
//	 * 0x00200000	成功
//	 * 	""
//	 * 0x00200001	用户不存在
//	 * 	""
//	 * 0x0020000f	服务器异常
//	 * 	""
//	 * </pre>
//	 */
//	public static JSONObject messageLogin(int usrId, String devId) {
//		try {
//			return format(new JSONObject(httpPost(UserServer.TYPE,
//					"UserLoginPush", "usrId=" + usrId + "&devId=" + devId)));
//		} catch (Exception e) {
//			return networkError();
//		}
//	}
//
//	/**
//	 * 编号 0x0020001 通知推送服务器消息成功获取
//	 * 
//	 * @param msgId
//	 *            用户的设备码
//	 * @return <h1>无</h1>
//	 * 
//	 *         <pre>
//	 * 0x00200010	成功
//	 * 	""
//	 * 0x00200011	消息不存在
//	 * 	""
//	 * 0x0020001f	服务器异常
//	 * 	""
//	 * </pre>
//	 */
//	public static JSONObject messageSuccess(String msgId) {
//		try {
//			return format(new JSONObject(httpPost(UserServer.TYPE,
//					"UserSuccessGetPush", "msgId=" + msgId)));
//		} catch (Exception e) {
//			return networkError();
//		}
//	}
//}
