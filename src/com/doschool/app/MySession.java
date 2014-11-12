package com.doschool.app;

import java.util.HashMap;
import java.util.Map;

public class MySession {

	private Map _objectContainer;

	private static MySession session;
	
	private MySession() {
		_objectContainer = new HashMap();
	}

	public static MySession getSession() {
		if (session == null) {
			session = new MySession();
			return session;
		} else {
			return session;
		}
	}

	public void put(Object key, Object value) {
		_objectContainer.put(key, value);
	}

	public Object get(Object key) {
		return _objectContainer.get(key);
	}

	public void cleanUpSession() {
		_objectContainer.clear();
	}

	public void remove(Object key) {
		_objectContainer.remove(key);
	}
}
 


















//public class UglyTrans {
////	public static Adp_person_Listview adptPerson;
////	public static Adp_Blog_Listview adaptBlog;
////	public static int blogID=-1;
////	public static boolean isDeleted=false;
//	
//	public static Microblog microblog;
//}


