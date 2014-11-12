package com.doschool.plugin.Gongdakcb;

import android.graphics.Color;
import android.widget.TextView;



public class Project {

	public String subName = "ÃüÀí";
	public int weeks[];
	public String address = "ÏåÑô";
	public int weekday = 1;
	public int begin = 1;
	public int last = 1;
	public Project(String subName, int[] weeks, String address, int weekday,
			int begin, int last) {
		this.subName = subName;
		this.weeks = weeks;
		this.address = address;
		this.weekday = weekday;
		this.begin = begin;
		this.last = last;
	}

}
