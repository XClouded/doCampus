package com.doschool.aa.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.doschool.R;
import com.doschool.aa.aa.Fgm_Standard_Linear;
import com.doschool.aa.aa.Fgm_Standard_Relative;
import com.doschool.app.ActivityName;

public class Fgm_Mine extends Fgm_Standard_Linear {


	public final int GOTO_ACT_SETTING = 1;
	
	@Override
	public void initData() {
	}

	@Override
	public void addViewToFgm() {
		
		getActionBar().setTittle("口袋小安");
		getActionBar().addOperateButton(R.drawable.ssb_setting_pressed, new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), Act_Setting.class);
				startActivityForResult(intent, GOTO_ACT_SETTING);
				getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				
			}
		});

//		getActionBar().setTittle("口袋小安");
//		getActionBar().addOperateButton(R.drawable.sab_share, null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==GOTO_ACT_SETTING && resultCode==Act_Setting.RESULT_LOGOUT)
		{
			startActivity(new Intent(getActivity(), Act_Wele.class));
			getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);  
			getActivity().finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	

}
