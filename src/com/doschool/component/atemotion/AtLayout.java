package com.doschool.component.atemotion;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.doschool.R;
import com.doschool.aa.item.Item_AtListView;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.entity.SimplePerson;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class AtLayout extends RelativeLayout {

	protected final int mDip = DoschoolApp.pxperdp;
	protected final int mScrnWidth = DoschoolApp.widthPixels;

	private final int AT_BOARD_HEIGHT = (int) (DoschoolApp.heightPixels * 0.32);
	private final int SEARCH_BOARD_HEIGHT = (int) (DoschoolApp.heightPixels * 0.08);
//	private final int AT_ITEM_NUM = 6;

	AtSearch searchLayout;
	private ListView listView;
	public ListView getListView() {
		return listView;
	}

	private BlogEditText blogEditText;
	ListAdapter listAdapter;
	Button btDelete;

	public AtLayout(Context context, BlogEditText blogEditText) {
		super(context);
		this.blogEditText = blogEditText;
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		this.setBackgroundResource(R.color.bg_greyblue);
		
		
		
		
		
		listAdapter = new ListAdapter("");
		listView = new ListView(getContext());
		listView.setId("listView".hashCode());
		listView.setBackgroundResource(R.color.bg_greyblue);
		listView.setAdapter(listAdapter);
		listView.setMinimumHeight(1);
		listView.setOnItemClickListener(new ListListener());
		listView.setDivider(getResources().getDrawable(R.color.bg_grey));
		this.addView(listView, LayoutParams.MATCH_PARENT, AT_BOARD_HEIGHT);

		RelativeLayout.LayoutParams lpDown = new LayoutParams(LayoutParams.MATCH_PARENT, SEARCH_BOARD_HEIGHT);
		lpDown.addRule(RelativeLayout.BELOW, "listView".hashCode());
		LinearLayout llDown = new LinearLayout(getContext());
		llDown.setOrientation(LinearLayout.HORIZONTAL);
		llDown.setPadding(mDip*18, 0, 0, 0);
		llDown.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(llDown, lpDown);

		
		searchLayout=new AtSearch(getContext());
		llDown.addView(searchLayout, (int) (mScrnWidth-mDip*(24+16)-SEARCH_BOARD_HEIGHT / 2 * 1.5),LayoutParams.WRAP_CONTENT);
		
		
		searchLayout.getTextview().setHint("搜索好友");
		searchLayout.getTextview().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				listAdapter = new ListAdapter(s.toString());
				listView.setAdapter(listAdapter);
				if(s.length()>0)
				{
					searchLayout.onNoText();
				}
				else
				{
					searchLayout.onHasText();
				}
			}
		});
		
		
		
		RelativeLayout.LayoutParams lpDelete = new LayoutParams((int) (SEARCH_BOARD_HEIGHT / 2 * 1.5), SEARCH_BOARD_HEIGHT / 2);

		btDelete = WidgetFactory.createPicButton(getContext(), R.drawable.bt_delete_emotion, null);
		btDelete.setOnClickListener(onDeleteClickListener);
		llDown.addView(btDelete, lpDelete);
		
		
		

	}
	OnClickListener onDeleteClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int index = blogEditText.getSelectionStart();
			Editable editable = blogEditText.getText();
			if(index==0)
			{
				
			}
			else if(index>10)
			{
				if (editable.subSequence(index - 4, index).toString().equals("[/e]"))
					editable.delete(index - 11, index);
				else
					editable.delete(index - 1, index);
			}
			else
			{
				editable.delete(index - 1, index);
			}
			
		}
	};

	class ListListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			ListAdapter la=(ListAdapter) arg0.getAdapter();
			
			Item_AtListView item_At = (com.doschool.aa.item.Item_AtListView) arg1;
			int insertPosition;
			insertPosition = blogEditText.getSelectionStart();
			if (item_At.isChecked() == false) {
				String str = "[a=" + la.personList.get(arg2).personId + "]@" + la.personList.get(arg2).nickName + "[/a]";

				String orgin = blogEditText.getText().toString();
				Log.v("XEditText", "原来的字符串:" + orgin);

				int Aend = blogEditText.getSelectionStart();
				insertPosition = Aend;
				boolean isInside = false;
				for (int i = 0; i < blogEditText.at3spanList.size(); i++) {

					Log.v("XEditText", "at的前后区间" + blogEditText.at3spanList.get(i).startPosition + "," + blogEditText.at3spanList.get(i).endPosition);
					if (insertPosition > blogEditText.at3spanList.get(i).startPosition && insertPosition <= blogEditText.at3spanList.get(i).endPosition) {
						isInside = true;
						if (blogEditText.at3spanList.get(i).startPosition == 0 && blogEditText.at3spanList.get(i).endPosition == orgin.length() - 1) {
							Log.v("XEditText", "1");
							orgin = str;
							blogEditText.shouldWordk = false;
							blogEditText.setText(blogEditText.strtosp(orgin));
							blogEditText.shouldWordk = true;
							blogEditText.setSelection(orgin.length());

						} else if (blogEditText.at3spanList.get(i).startPosition == 0) {
							Log.v("XEditText", "2");
							orgin = str + orgin.substring(blogEditText.at3spanList.get(i).endPosition + 1, orgin.length());
							blogEditText.shouldWordk = false;
							blogEditText.setText(blogEditText.strtosp(orgin));
							blogEditText.shouldWordk = true;
							blogEditText.setSelection(str.length());

						} else if (blogEditText.at3spanList.get(i).endPosition == orgin.length() - 1) {
							Log.v("XEditText", "3");
							orgin = orgin.substring(0, blogEditText.at3spanList.get(i).startPosition) + str;
							blogEditText.shouldWordk = false;
							blogEditText.setText(blogEditText.strtosp(orgin));
							blogEditText.shouldWordk = true;
							blogEditText.setSelection(orgin.length());

						} else {
							Log.v("XEditText", "4");
							int len = blogEditText.at3spanList.get(i).startPosition + str.length();
							orgin = orgin.substring(0, blogEditText.at3spanList.get(i).startPosition) + str + orgin.substring(blogEditText.at3spanList.get(i).endPosition + 1, orgin.length());
							blogEditText.shouldWordk = false;
							blogEditText.setText(blogEditText.strtosp(orgin));
							blogEditText.shouldWordk = true;
							blogEditText.setSelection(len);

						}

					}
				}

				if (!isInside) {
					Log.v("XEditText", "a");
					if (insertPosition == 0) {
						Log.v("XEditText", "b");
						int end = orgin.length();
						if (end < 0)
							end = 0;
						orgin = str + orgin.substring(0, end);
						blogEditText.shouldWordk = false;
						blogEditText.setText(blogEditText.strtosp(orgin));
						blogEditText.shouldWordk = true;
						blogEditText.setSelection(str.length());
					}

					else if (insertPosition == orgin.length()) {
						Log.v("XEditText", "c");
						orgin = orgin.substring(0, orgin.length()) + str;
						blogEditText.shouldWordk = false;
						blogEditText.setText(blogEditText.strtosp(orgin));
						blogEditText.shouldWordk = true;
						blogEditText.setSelection(insertPosition + str.length());
					}

					else {
						Log.v("XEditText", "d");
						orgin = orgin.substring(0, insertPosition) + str + orgin.substring(insertPosition, orgin.length());
						blogEditText.shouldWordk = false;
						blogEditText.setText(blogEditText.strtosp(orgin));
						blogEditText.shouldWordk = true;
						blogEditText.setSelection(insertPosition + str.length());
					}

				}
			} else {

				String orgin = blogEditText.getText().toString();
				String A = (String) orgin.subSequence(0, item_At.span.startPosition);
				String B = (String) orgin.subSequence(item_At.span.endPosition + 1, orgin.length());
				orgin = A + B;
				int position;
				if (insertPosition < item_At.span.startPosition)
					position = insertPosition;
				else if (insertPosition > item_At.span.endPosition)
					position = insertPosition - item_At.span.endPosition + item_At.span.startPosition - 1;
				else
					position = item_At.span.startPosition;
				blogEditText.shouldWordk = false;
				blogEditText.setText(blogEditText.strtosp(orgin));
				blogEditText.shouldWordk = true;

				blogEditText.setSelection(position);
			}
		}

	}
	
	public class ListAdapter extends BaseAdapter {
		
		ArrayList<SimplePerson> personList;
		
		public ListAdapter(String keywords) {
			super();
			personList=new ArrayList<SimplePerson>();
			for (SimplePerson person : DoschoolApp.friendList) {
				if(person.nickName.contains(keywords) || (person.trueName.contains(keywords)&&person.isMyCard==true))
				{
					personList.add(person);
				}
				else if(keywords==null || keywords.length()==0)
				{
					personList.add(person);
				}
			}
		}

		@Override
		public int getCount() {
			return personList.size();
		}

		@Override
		public Object getItem(int position) {
			return personList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			
			if (convertView == null) {
				Item_AtListView item_at = new Item_AtListView(getContext());
				convertView = item_at;
			}
			boolean isAted = false;
			int i = 0;
			for (i = 0; i < blogEditText.at3spanList.size(); i++) {
				if (blogEditText.at3spanList.get(i).userId == personList.get(position).personId) {
					isAted = true;
					break;
				}
			}
			AtSpan atSpan = null;
			if (isAted)
				atSpan = blogEditText.at3spanList.get(i);

			((Item_AtListView) convertView).setData(personList.get(position), isAted, atSpan);
			return convertView;
		}
	}
	

	



	
}
