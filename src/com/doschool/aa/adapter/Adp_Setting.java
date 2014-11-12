package com.doschool.aa.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.R;
import com.doschool.aa.item.Item_SettingListView;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.SpMethods;

public class Adp_Setting extends BaseAdapter {
	ImageView ivHead;
	TextView tvNick;
	CheckBox cbox;
	Context context;
	String TOOLS_NAME[];
	int RID[] = { R.drawable.setting_nopic, R.drawable.setting_changepassword,
			R.drawable.setting_advise, R.drawable.setting_update,
			R.drawable.setting_feature, R.drawable.setting_aboutus,
			R.drawable.setting_logout };

	CheckBox tlbNopic;

	public Adp_Setting(Context context, String TOOLS_NAME[]) {
		this.context = context;
		this.TOOLS_NAME = TOOLS_NAME;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item_SettingListView item_Setting =new Item_SettingListView(context);
		
		
		if (TOOLS_NAME[position].equals("注销登陆"))
			item_Setting.tvText.setTextColor(context.getResources().getColor(R.color.pink2));
		
		
		if (TOOLS_NAME[position].equals("无图模式")) {
			
			if (SpMethods.loadBoolean(context,
					SpMethods.MODE_NOPIC_DOWNLOAD, false))
				item_Setting.cbox.setChecked(true);
			else
				item_Setting.cbox.setChecked(false);
			item_Setting.setData(RID[position], TOOLS_NAME[position], true);

		}
		else
		{
			item_Setting.setData(RID[position], TOOLS_NAME[position], false);
		}

		return item_Setting;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return TOOLS_NAME.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
