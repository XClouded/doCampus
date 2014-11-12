package com.doschool.entity;

import java.io.Serializable;

import com.doschool.zother.MJSONObject;

public class SimplePerson implements Serializable {

	private static final long serialVersionUID = -7908982102940752845L;

	public int personId;
	public String funId;

	public String sex;
	public String headUrl;

	public String nickName;
	public String trueName;
	public String remarkName;

	public int status;
	public int cardState;
	public int friendState;

	public boolean isMySelf;
	public boolean isMyFriend;
	public boolean doISendFriendRequest;
	public boolean isMyCard;
	public boolean doISendCard;

	public SimplePerson() {

	}

	public SimplePerson(MJSONObject jObj) {

		 	this.personId = jObj.getInt("personId",-1);
			this.funId=jObj.getString("funId");
			this.sex=jObj.getString("sex");
			this.headUrl = jObj.getString("headUrl");
			this.nickName = jObj.getString("nickName");
			this.trueName=jObj.getString("trueName");
			this.remarkName=jObj.getString("remarkName");
			this.status= jObj.getInt("status",-1);
					
			this.cardState = jObj.getInt("cardState",-1);
			this.friendState = jObj.getInt("friendState",-1);
			refreshFlags();

	}

	public SimplePerson(int personId, String funId, String sex, String headUrl, String nickName, String trueName, String remarkName, int status, int cardState, int friendState) {
		super();
		this.personId = personId;
		this.funId = funId;
		this.sex = sex;
		this.headUrl = headUrl;
		this.nickName = nickName;
		this.trueName = trueName;
		this.remarkName = remarkName;
		this.status = status;
		this.cardState = cardState;
		this.friendState = friendState;
		refreshFlags();
	}

	public void refreshFlags() {
		if (friendState == 2)
			isMySelf = true;
		else if (friendState != -100)
			isMySelf = false;

		if (friendState == 1)
			isMyFriend = true;
		else if (friendState != -100)
			isMyFriend = false;

		if (friendState == -1 || friendState == -3)
			doISendFriendRequest = true;
		else if (friendState != -100)
			doISendFriendRequest = false;

		if (cardState == 1 || cardState == -2 || cardState == 2)
			isMyCard = true;
		else if (cardState != -100)
			isMyCard = false;

		if (cardState == -1 || cardState == 1)
			doISendCard = true;
		else if (cardState != -100)
			doISendCard = false;
	}

}