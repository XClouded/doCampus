package com.doschool.component.atemotion;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import com.doschool.R;
import com.doschool.aa.item.Item_AtListView;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.DoMethods;
import com.doschool.zother.MJSONArray;
import com.doschool.zother.MJSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class BlogEditText extends EditText {

	public EmotionLayout emotionLayout;
	public AtLayout atListLayout;
	public boolean shouldWordk = true;
	public ArrayList<AtSpan> at3spanList = new ArrayList<AtSpan>();

	View viewEmotion, viewAt, viewDismiss;


	public BlogEditText(Context context, View emotion, View at, View dismiss) {
		super(context);
		this.setPadding(0, 0, 0, 0);
		addTextChangedListener(new XWatcher());
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				emotionLayout.setVisibility(View.GONE);
				viewEmotion.setBackgroundResource(R.drawable.img_emotion);
				atListLayout.setVisibility(View.GONE);
				viewAt.setBackgroundResource(R.drawable.img_at);
				
			}
		});
		atListLayout=new AtLayout(getContext(), this);
		atListLayout.setVisibility(View.GONE);
		
		emotionLayout=new EmotionLayout(getContext(),BlogEditText.this);
		emotionLayout.setVisibility(View.GONE);
		
		this.viewEmotion = emotion;
		this.viewAt = at;
		this.viewDismiss = dismiss;
		this.viewEmotion.setOnClickListener(onEmotionClickListener);
		this.viewAt.setOnClickListener(onAtClickListener);
		this.viewDismiss.setOnTouchListener(onDismissTouchListener);
	}



	class XWatcher implements TextWatcher {
		private int editStart, editCount, editAfter;
		String strBefore;

		@Override
		public void beforeTextChanged(CharSequence beforeCS, int start, int count, int after) {
			if (shouldWordk) {
				editStart = start;
				editCount = count;
				editAfter = after;
				strBefore = new String(beforeCS.toString());
			}
		}

		@Override
		public void onTextChanged(CharSequence afterCS, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {

			if (shouldWordk &&  s.length()>0) {
				boolean isChecked = false;
				String orgin = BlogEditText.this.getText().toString();
				for (int i = 0; i < at3spanList.size(); i++) {
					if (editAfter > 0) {
						if (at3spanList.get(i).startPosition < editStart && editStart < at3spanList.get(i).endPosition) {
							String A = orgin.substring(0, at3spanList.get(i).startPosition);
							String B = orgin.substring(editStart, editStart + editAfter);
							String C = orgin.substring(at3spanList.get(i).endPosition + editAfter + 1, orgin.length());
							orgin = A + B + C;
							shouldWordk = false;
							int position = at3spanList.get(i).startPosition;
							BlogEditText.this.setText(strtosp(orgin));
							BlogEditText.this.setSelection(position);
							shouldWordk = true;
							isChecked = true;
						}
					} else if (editCount > 0) {
						if (at3spanList.get(i).startPosition <= editStart && editStart <= at3spanList.get(i).endPosition) {
							Log.v("XEditText", "c");
							String A = orgin.substring(0, at3spanList.get(i).startPosition);
							String C = orgin.substring(at3spanList.get(i).endPosition - editCount + 1, orgin.length());
							orgin = A + C;
							shouldWordk = false;
							int position = at3spanList.get(i).startPosition;
							BlogEditText.this.setText(strtosp(orgin));
							BlogEditText.this.setSelection(position);
							shouldWordk = true;
							isChecked = true;
						}
					}
				}
				if (!isChecked) {
					shouldWordk = false;
					BlogEditText.this.setText(strtosp(orgin));
					shouldWordk = true;
					if (editAfter > 0)
						BlogEditText.this.setSelection(editStart + editAfter);
					else
						BlogEditText.this.setSelection(editStart);
				}
			}
		}
	}

	public SpannableString strtosp(String str) {
		Drawable drawableAt = getContext().getResources().getDrawable(R.drawable.img_at_dark);
		drawableAt.setBounds(0, 0, DoschoolApp.widthPixels / 20, DoschoolApp.widthPixels / 20);

		Drawable drawableAtEnd = getContext().getResources().getDrawable(R.drawable.img_transparent);
		drawableAtEnd.setBounds(0, 0, DoschoolApp.widthPixels / 100, DoschoolApp.widthPixels / 100);

		Log.v("开始匹配", "match");
		SpannableString spannableString = new SpannableString(str);
		String zhengze = "\\[a=.*?\\].*?\\[\\/a\\]";

		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		Matcher matcher = sinaPatten.matcher(spannableString);
		at3spanList.clear();

		while (matcher.find()) {

			String key = matcher.group();
			Log.v("找到大的了:" + key, "match");

			Pattern patternUid = Pattern.compile("\\[a=\\d*?\\]@", Pattern.CASE_INSENSITIVE);
			Matcher matcherUid = patternUid.matcher(key);
			String keyUid = "";
			while (matcherUid.find()) {
				keyUid = matcherUid.group();
				Log.v("找到@的了:" + keyUid, "match");

				ImageSpan imageSpanAt = new ImageSpan(drawableAt);
				spannableString.setSpan(imageSpanAt, matcher.start() + matcherUid.start(), matcher.start() + matcherUid.start() + keyUid.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			int id = Integer.valueOf(keyUid.substring(3, keyUid.length() - 2));
			at3spanList.add(new AtSpan(matcher.start(), matcher.end() - 1, id));
			Pattern patternNick = Pattern.compile("]@.*?\\[/a\\]", Pattern.CASE_INSENSITIVE);
			Matcher matcherNick = patternNick.matcher(key);
			String keyNick = null;
			while (matcherNick.find()) {
				keyNick = matcherNick.group();
				keyNick = keyNick.substring(2, keyNick.length() - 4);
				ForegroundColorSpan sp = new ForegroundColorSpan(getContext().getResources().getColor(R.color.at_color));
				spannableString.setSpan(sp, matcher.start() + matcherNick.start() - 2, matcher.start() + matcherNick.end() - 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				Log.v("keyNick1==" + keyNick, "keyUid");
				break;
			}

			ImageSpan imageSpanAt = new ImageSpan(drawableAtEnd);
			spannableString.setSpan(imageSpanAt, matcher.end() - 4, matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

		}

		String zhengze2 = "\\[e\\]\\d{4}\\[\\/e\\]";
		try {
			spannableString = ExpressionUtil.getExpressionString(getContext(), spannableString, zhengze2);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		((BaseAdapter) atListLayout.getListView().getAdapter()).notifyDataSetChanged();
		((BaseAdapter) atListLayout.getListView().getAdapter()).notifyDataSetInvalidated();
		return spannableString;
	}



	
	
	
	
	
	
	private OnClickListener onEmotionClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(BlogEditText.this.getWindowToken(), 0);
			if (emotionLayout.getVisibility() == View.VISIBLE) {
				emotionLayout.setVisibility(View.GONE);
				viewEmotion.setBackgroundResource(R.drawable.img_emotion);
			} else {
				atListLayout.setVisibility(View.GONE);
				viewAt.setBackgroundResource(R.drawable.img_at);
				
				emotionLayout.setVisibility(View.VISIBLE);
				Animation alphaAnim = new TranslateAnimation(0, 0, DoschoolApp.heightPixels/2, 0);
				alphaAnim.setDuration(500);
				emotionLayout.startAnimation(alphaAnim);
				
				viewEmotion.setBackgroundResource(R.drawable.img_emotion_checked);
			}
			DoMethods.addAnimation(v);
		}
	};

	private OnClickListener onAtClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			InputMethodManager imm2 = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm2.hideSoftInputFromWindow(BlogEditText.this.getWindowToken(), 0);
			if (atListLayout.getVisibility() == View.VISIBLE) {
				atListLayout.setVisibility(View.GONE);
				viewAt.setBackgroundResource(R.drawable.img_at);
			} else {
				emotionLayout.setVisibility(View.GONE);
				viewEmotion.setBackgroundResource(R.drawable.img_emotion);
				
				atListLayout.setVisibility(View.VISIBLE);
				Animation alphaAnim = new TranslateAnimation(0, 0, DoschoolApp.heightPixels/2, 0);
				alphaAnim.setDuration(500);
				atListLayout.startAnimation(alphaAnim);
				
				viewAt.setBackgroundResource(R.drawable.img_at_checked);
			}
			DoMethods.addAnimation(v);
		}
	};


	private OnTouchListener onDismissTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			InputMethodManager imm3 = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm3.hideSoftInputFromWindow(BlogEditText.this.getWindowToken(), 0);
			emotionLayout.setVisibility(View.GONE);
			viewEmotion.setBackgroundResource(R.drawable.img_emotion);
			atListLayout.setVisibility(View.GONE);
			viewAt.setBackgroundResource(R.drawable.img_at);
			return false;
		}
	};
	
	public MJSONObject toJMjsonObject() {
		MJSONObject mObject = new MJSONObject();
		try {
			mObject.put("string", this.getText().toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		MJSONArray mArray = new MJSONArray();
		for (int i = 0; i < this.at3spanList.size(); i++) {
			MJSONObject span = new MJSONObject();
			try {
				span.put("type", 1);
				span.put("id", this.at3spanList.get(i).userId);
				span.put("start", 0);
				span.put("end", 1);
			} catch (JSONException e) {
			}
			mArray.put(span);
		}
		try {
			mObject.put("spans", mArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mObject;
	}

	
}
