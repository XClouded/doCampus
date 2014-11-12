package com.doschool.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;


public class Person extends SimplePerson implements Serializable {
	
	
	
	private static final long serialVersionUID = 5025629686918342351L;
	

	public String phoneNumber;
	public String qq;
	public String email;
	public String intro;
	public String background;
	
	public int blogCount;
	public double activeValue ;
	public double popValue ;
	public int contriValue;
	
	
	public ArrayList<Xunzhang> xunList;;


	public Person(SimplePerson sp)
	{
		this.personId=sp.personId;
		this.funId=sp.funId;
		
		this.sex=sp.sex;
		this.headUrl=sp.headUrl;
		
		this.nickName=sp.nickName;
		this.trueName=sp.trueName;
		this.remarkName=sp.remarkName;
		
		this.status=sp.status;
		this.cardState=sp.cardState;
		this.friendState=sp.friendState;
		
		this.isMySelf=sp.isMyCard;
		this.isMyFriend=sp.isMyFriend;
		this.doISendFriendRequest=sp.doISendCard;
		this.isMyCard=sp.isMyCard;
		this.doISendCard=sp.doISendCard;
	}
	
	 public Person(MJSONObject jObj) {

		 
		 
		 super(jObj.getMJSONObject("base"));
			this.phoneNumber=jObj.getString("phoneNumber");
			this.qq=jObj.getString("qq");
			this.email=jObj.getString("mail");
			this.intro=jObj.getString("intro");
			this.background=jObj.getString("background");
			
			this.blogCount = jObj.getInt("blogCount",0);
			this.activeValue=jObj.getDouble("activeValue");
			this.popValue=jObj.getDouble("popValue");
			this.contriValue = jObj.getInt("contriValue",0);
			
			this.xunList=new ArrayList<Xunzhang>();
			
			try {
				MJSONArray arrayXun=jObj.getMJSONArray("xunList");
				for(int i=0;i<arrayXun.length();i++)
				{
					xunList.add(new Xunzhang(arrayXun.getMJSONObject(i)));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			xunList.add(new Xunzhang("http://tb1.bdstatic.com/tb/cms/timg-ydw.jpg", "www", "ssss", 0, 0));
			
	}
		
	
}
