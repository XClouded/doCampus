package com.doschool.plugin.kcb;

import android.graphics.Color;
import android.widget.TextView;



public class Project {

	public int ds;
	public int color = Color.GREEN;
	public int fromWeek = 1, toWeek = 18;
	public int startTime = 2;
	public int times = 2;
	public int startWeek = 1;
	public String name = "xxx";
	public String address = "xxx";

	public String teacherName = "";
	public int endTime;

	public Project(int color, int fromWeek, int toWeek, int startTime,
			int times, int startWeek, String name, String address,
			String teacherName, int ds) {
		super();
		this.color = color;
		this.fromWeek = fromWeek;
		this.toWeek = toWeek;
		this.startTime = startTime;
		this.times = times;
		this.startWeek = startWeek;
		this.name = name;
		this.address = address;
		this.teacherName = teacherName;
		this.ds = ds;
	}

}
