package com.doschool.aa.widget;

import com.doschool.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class WidgetFactory {
	
	public static TextView createTextView(Context context,String text,int color,int size)
	{
		TextView textView=new TextView(context);
		if(color==0)
			textView.setTextColor(Color.WHITE);
		else if(color==1)
			textView.setTextColor(Color.BLACK);
		else
			textView.setTextColor(color);
		if(size!=0)
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setText(text);
		return textView;
	}

	public static ImageButton createTopButton(Context context,int imageRid,int tag,OnClickListener listner)
	{
		ImageButton imageButton = new ImageButton(context);
		imageButton.setImageResource(imageRid);
		imageButton.setTag(tag);
		imageButton.setOnClickListener(listner);
		return imageButton;
	}
	
	public static EditText createEditText(Context context,String hint,int color,int size)
	{
		EditText editText=new EditText(context);
		editText.setHint(hint);
		if(color==0)
			editText.setTextColor(Color.BLACK);
		else
			editText.setTextColor(color);
		
		if(size!=0)
			editText.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
		return editText;
	}
	
	public static Button createButton(Context context,String text,int bgresid,int color,int size) {
		Button button=new Button(context);
		button.setText(text);
		if(bgresid!=0)
			button.setBackgroundResource(bgresid);
		if(color!=0)
			button.setTextColor(context.getResources().getColor(color));
		else
			button.setTextColor(Color.BLACK);
		if(size!=0)
			button.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
		return button;
	}
	
	public static Button createPicButton(Context context,int bgresid, OnClickListener listener) {
		Button button=new Button(context);
		if(bgresid!=0)
			button.setBackgroundResource(bgresid);
		else
			button.setTextColor(Color.BLACK);
		if(listener!=null)
			button.setOnClickListener(listener);
		return button;
	}
	

	public static Button createButtonWithMainStyle(Context context,String text) {
		Button button=new Button(context);
		button.setText(text);
		button.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		button.setTextColor(context.getResources().getColorStateList(R.color.btn_style_launchcmt_textcolor));
		button.setBackgroundResource(R.drawable.btn_style_launchcmt);
		return button;
	}
	
	
	
	
	
}
