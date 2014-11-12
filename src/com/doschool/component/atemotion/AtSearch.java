package com.doschool.component.atemotion;

import com.doschool.R;
import com.doschool.app.DoschoolApp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class AtSearch extends LinearLayout {

	private ImageView icon;
	public ImageView getIcon() {
		return icon;
	}

	public void setIcon(ImageView icon) {
		this.icon = icon;
	}

	public EditText getTextview() {
		return textview;
	}

	public void setTextview(EditText textview) {
		this.textview = textview;
	}

	private EditText textview;
	protected final int mDip = DoschoolApp.pxperdp;
	protected final int mScrnWidth = DoschoolApp.widthPixels;
	
	public AtSearch(Context context) {
		super(context);
		this.setPadding(0, 0, 0, 0);
		this.setGravity(Gravity.CENTER);
		this.setOrientation(LinearLayout.HORIZONTAL);
		
		icon=new ImageView(getContext());
		icon.setImageResource(R.drawable.tools_lib_search_icon);
		icon.setScaleType(ScaleType.FIT_XY);
		this.addView(icon,mDip*32,mDip*32);
		
		textview=new EditText(getContext());
		textview.setTextColor(Color.BLACK);
		textview.setBackgroundColor(Color.TRANSPARENT);
		textview.setPadding(0, 0, 0, 0);
		this.addView(textview,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		
		textview.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus)
					AtSearch.this.setBackgroundResource(R.drawable.roundsquare_blogedit);
				else

					AtSearch.this.setBackgroundColor(Color.TRANSPARENT);
				
			}
		});
		
	}
	
	public void onNoText()
	{
		icon.setImageResource(R.drawable.seachview_clear);
		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textview.getText().clear();
				
			}
		});
	}
	public void onHasText()
	{
		icon.setImageResource(R.drawable.tools_lib_search_icon);
		icon.setOnClickListener(null);
	}

}
