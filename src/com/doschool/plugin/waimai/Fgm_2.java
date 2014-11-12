package com.doschool.plugin.waimai;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doschool.R;

public class Fgm_2 extends Fragment {
	final String PACKAGE_NAME = "com.doschool.plugin.waimai";
	
	LayoutInflater inflater;
	private List<String> v;
//	Resources pluginRes;

	public Fgm_2(List<String> v) {
		this.v = v;
	}
	
	TextView t1, t2, t3, t4;
	TextView head;
	ImageButton imbt;

	private Context context;

	@Override
	public View onCreateView(LayoutInflater ii, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null)
			return super.onCreateView(ii, container, savedInstanceState);
		readyRes();
		inflater = LayoutInflater.from(context);
		View root = inflater.inflate(R.layout.tools_activity_waimaidetail, null);
		t1 = (TextView) root.findViewById(R.id.tools_activity_waimai_text1);
		t2 = (TextView) root.findViewById(R.id.tools_activity_waimai_text2);
		t4 = (TextView) root.findViewById(R.id.tools_activity_waimai_text4);
		imbt = (ImageButton) root.findViewById(R.id.tools_activity_waimai_callbt);		
//		it.putExtra("data", data.get(i).get(3));
//		it.putExtra("notice", data.get(i).get(2));
//		it.putExtra("name", data.get(i).get(0));
//		it.putExtra("phone", data.get(i).get(1));

		t2.setText(v.get(0));
		String notice = v.get(2);
		String data = v.get(3);
		data = data.replace("#", "\r\n");
		Log.i("data1",data);
		t1.setText(notice);
		t4.setText(data);
		final String tel = v.get(1);
		imbt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				MobclickAgent.onEvent(getActivity().getApplicationContext(),"event_tool_waimai_call");
				Uri telUri = Uri.parse("tel:" + tel);
				Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
				startActivity(intent);

			}
		});
		return root;
	
	}
	
	private void readyRes() {
		try {
			context = getActivity();
//			.createPackageContext(
//					PACKAGE_NAME,
//					Context.CONTEXT_INCLUDE_CODE
//							| Context.CONTEXT_IGNORE_SECURITY);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			pluginRes = getActivity().getPackageManager().getResourcesForApplication(PACKAGE_NAME);
//		} catch (NameNotFoundException e) {
//			Log.i("EEEEE","EE");
//			e.printStackTrace();
//		}
		
	}


	
}
