package com.doschool.component.atemotion;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;


public class BlogContentTextWatcher implements TextWatcher{
	
	View btConfirm;
//	TextView tvWordsCount;
	BlogEditText xetContent;
	int wordsLimit;
	
	public BlogContentTextWatcher(View btConfirm, TextView tvWordsCount, BlogEditText xetContent,int wordsLimit)
	{
		this.wordsLimit=wordsLimit;
		this.btConfirm=btConfirm;
//		this.tvWordsCount=tvWordsCount;
		this.xetContent=xetContent;
	}		
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}
	@Override
	public void afterTextChanged(Editable s) {
		int realNum=0,enterNum=0;
		for(int i=0;i<s.length();i++)
		{
			if(s.subSequence(i, i+1)!=" ")
				realNum++;
			if(s.length()>2 && i>1 && s.subSequence(i-1, i).toString().equals("\n"))
			{
				realNum-=2;
				enterNum++;
			}
		}
		
		if(btConfirm!=null)
		{
			if(s.length()==0 || realNum==0 || s.length()>wordsLimit || enterNum>10)
				btConfirm.setVisibility(View.INVISIBLE);
			else
				btConfirm.setVisibility(View.VISIBLE);
		}
//		tvWordsCount.setText(wordsLimit-s.length()+"");
		
	}
}