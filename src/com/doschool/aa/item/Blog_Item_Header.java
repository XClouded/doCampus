package com.doschool.aa.item;

import com.doschool.R;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.clicklistener.Click_Person;
import com.doschool.entity.Microblog;
import com.doschool.methods.ConvertMethods;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 微博用户信息面板的布局
 * 
 * @author 是我的海
 * 
 */
public class Blog_Item_Header extends LinearLayout {

	private Microblog blogData;

	private ImageView ivHead;
	private ImageView ivIdentify;
	private TextView tvNick;
	private ImageView ivGender;
	private TextView tvTime;
	private ImageView ivHuodong;

	
	public Blog_Item_Header(Context context, AttributeSet attrs) {
		super(context, attrs);
		createUI();
	}
	
	public Blog_Item_Header(Context context) {
		super(context);
		createUI();
	}

	public void createUI() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.item_blog_header, this);
		ivIdentify = (ImageView) findViewById(R.id.ivIdentify);
		ivGender = (ImageView) findViewById(R.id.ivGender);
		ivHead = (ImageView) findViewById(R.id.ivHead);
		ivHuodong = (ImageView) findViewById(R.id.ivHuodong);
		tvNick = (TextView) findViewById(R.id.tvNick);
		tvTime = (TextView) findViewById(R.id.tvTime);
	}




	
	public void updateUI(Microblog blog) {
		this.blogData = blog;
		
		//头像
		DoschoolApp.newImageLoader.displayImage(blogData.author.headUrl, ivHead, DoschoolApp.dioRound);
		Click_Person click_Person = new Click_Person(getContext(), blogData.author);
		ivHead.setOnClickListener(click_Person);
		
		//昵称
		tvNick.setOnClickListener(click_Person);
		tvNick.setText(ConvertMethods.getShowName(blogData.author));

		//V
		if (blogData.author.status == 2) {
			ivIdentify.setVisibility(View.VISIBLE);
			ivIdentify.setImageResource(R.drawable.icon_vip);
		} else {
			ivIdentify.setVisibility(View.GONE);
			//性别
			if (blogData.author.sex != null) {
				if (blogData.author.sex.equals("男")) {
					ivGender.setImageResource(R.drawable.icon_sex_boy);
				} else if (blogData.author.sex.equals("女")) {
					ivGender.setImageResource(R.drawable.icon_sex_girl);
				}
			}
		}
		
		

		//日期浏览量
		String timeStr = ConvertMethods.dateLongToDiyStr(blogData.launchTime);
		tvTime.setText(timeStr + "  浏览量" + blogData.browseCount + "次");

		
		//活动

		if (blogData.status > 1) {
			DoschoolApp.newImageLoader.displayImage(blogData.medal.picUrl, ivHuodong, DoschoolApp.dioSquare);
			ivHuodong.setVisibility(View.VISIBLE);
			ivHuodong.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String msg = blogData.medal.intro.replace(DoschoolApp.CHANGE_LINE_STRING, "\n") + "\n\n";
					if (blogData.author.personId == DoschoolApp.thisUser.personId) {
						msg += "获奖姓名:" + DoschoolApp.thisUser.trueName + "\n";
						msg += "获奖日期:" + ConvertMethods.longToMDHM(blogData.medal.rewardTime) + "\n";
						msg += "获奖序号:" + blogData.medal.rewardXuhao + "\n";
						TextView mtv = WidgetFactory.createTextView(getContext(), msg, 0, 0);
								
						new AlertDialog.Builder(getContext()).setTitle("恭喜你获得”" + blogData.medal.theme + "”的勋章\n").setView(mtv).setNegativeButton("关闭", null).show();
					} else {
						new AlertDialog.Builder(getContext()).setTitle(blogData.medal.theme).setMessage(msg).setNegativeButton("关闭", null).show();
					}
				}
			});
		}
		else
		{
			ivHuodong.setVisibility(View.INVISIBLE);
		}

	}

}
