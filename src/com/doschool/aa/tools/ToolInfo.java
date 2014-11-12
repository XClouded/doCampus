package com.doschool.aa.tools;

public class ToolInfo {
	public int toolid = 1; //from 1 to ..
	public String name = "外卖速叫"; 
	public int type = 1;// 1 is local tools
	public String iconURL = ""; 
	public String serURL = "";
	public int version = 1;
	public int isPublic = 1;// 2 is not public 
	public int status = 1;// 2 is not used
	public int needShare = 0;
	public String shareURL = "",jsName = "";
	public ToolInfo(int toolid, String name, int type, String iconURL,
			String serURL, int version, int isPublic, int status, int needShare, String shareURL,String jsName) {
		super();
		this.toolid = toolid;
		this.name = name;
		this.type = type;
		this.iconURL = iconURL;
		this.serURL = serURL;
		this.version = version;
		this.isPublic = isPublic;
		this.status = status;
		this.needShare = needShare;
		this.shareURL = shareURL;
		this.jsName = jsName;
	}

}
