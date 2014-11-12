package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.clicklistener.Click_Blog;
import com.doschool.entity.Microblog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * 微博广场ListView的Item布局
 * 
 * @author 是我的海
 */

public class Blog_Item extends LinearLayout {

	/******** 界面组件 ****************************************/
	private Blog_Item_Header item_Header; 	// 头部面板
	public Blog_Item_Content item_Content; // 微博图片
	public Blog_Item_Content item_Trans; 	// 根微博
	private Blog_Item_Bottom item_Bottom; 	// 底部面板

	/******** 数据等 ****************************************/
	private Microblog blogData;

	
	// 初始化界面
	public Blog_Item(Context context) {
		super(context);
		createUI();
	}

	public Blog_Item(Context context, AttributeSet attrs) {
		super(context, attrs);
		createUI();
	}

	private void createUI() {
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_blog, this);
		this.setBackgroundResource(R.color.bg_yellow);
		
		item_Header = (Blog_Item_Header) findViewById(R.id.blog_item_header);
		item_Content = (Blog_Item_Content) findViewById(R.id.blog_item_content);
		item_Trans = (Blog_Item_Content) findViewById(R.id.blog_item_trans);
		item_Bottom = (Blog_Item_Bottom) findViewById(R.id.blog_item_footer);
	}

	// 注入数据
	public void updateUI(Microblog data) {
		blogData = data;
		this.setOnClickListener(new Click_Blog(getContext(), blogData));
		
		item_Header.updateUI(blogData);
		item_Content.updateUI(blogData);
		item_Trans.updateUI(blogData.rootBlog);
		item_Bottom.updateUI(data,null);
	}


}
