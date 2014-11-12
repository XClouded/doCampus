package com.doschool.component.atemotion;

public class EmotionBean {

	public EmotionBean(int resourceId, int emotionId) {
		super();
		this.resourceId = resourceId;
		this.emotionId = emotionId;
	}
	private int resourceId;
	private int emotionId;
	
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	public int getEmotionId() {
		return emotionId;
	}
	public void setEmotionId(int emotionId) {
		this.emotionId = emotionId;
	}

}
