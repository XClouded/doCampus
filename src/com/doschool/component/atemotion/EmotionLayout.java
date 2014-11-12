package com.doschool.component.atemotion;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.doschool.R;
import com.doschool.aa.widget.DotGroup;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.DoMethods;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class EmotionLayout extends RelativeLayout {

	protected final int mDip = DoschoolApp.pxperdp;
	protected final int mScrnWidth = DoschoolApp.widthPixels;

	private final int EMITION_BOARD_HEIGHT = (int) (DoschoolApp.heightPixels * 0.32);
	private final int INDICATOR_BOARD_HEIGHT = (int) (DoschoolApp.heightPixels * 0.08);
	private final int EMOTION_TOTAL_NUM = 95;
	private final int LINE_PER_PAGE = 4;
	private final int COUNT_PER_LINE = 6;
	private final int PAGE_COUNT = EMOTION_TOTAL_NUM / (LINE_PER_PAGE * COUNT_PER_LINE) + 1;

	private ViewPager viewPager;
	private BlogEditText blogEditText;
	private DotGroup dotGroup;
	Button btDelete;

	public EmotionLayout(Context context, BlogEditText blogEditText) {
		super(context);
		this.blogEditText = blogEditText;
		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		this.setBackgroundResource(R.color.bg_greyblue);

		viewPager = new ViewPager(getContext());
		viewPager.setId("vp".hashCode());
		viewPager.setAdapter(mPagerAdapter);
		viewPager.setOffscreenPageLimit(3);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				dotGroup.setCurrentItem(arg0);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.addView(viewPager, LayoutParams.MATCH_PARENT, EMITION_BOARD_HEIGHT);

		dotGroup = new DotGroup(getContext(), viewPager);
		RelativeLayout.LayoutParams lpDotGroup = new LayoutParams(LayoutParams.WRAP_CONTENT, INDICATOR_BOARD_HEIGHT);
		lpDotGroup.addRule(RelativeLayout.BELOW, "vp".hashCode());
		lpDotGroup.addRule(RelativeLayout.CENTER_HORIZONTAL);
		this.addView(dotGroup, lpDotGroup);

		LinearLayout llDelete = new LinearLayout(getContext());
		llDelete.setPadding(0, 0, (mScrnWidth / 6 - (INDICATOR_BOARD_HEIGHT - mDip * 16)) / 2, 0);
		llDelete.setGravity(Gravity.CENTER);
		RelativeLayout.LayoutParams lpDelete = new LayoutParams(LayoutParams.WRAP_CONTENT, INDICATOR_BOARD_HEIGHT);
		lpDelete.addRule(RelativeLayout.BELOW, "vp".hashCode());
		lpDelete.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		this.addView(llDelete, lpDelete);

		btDelete = WidgetFactory.createPicButton(getContext(), R.drawable.bt_delete_emotion, onDeleteClickListener);
		llDelete.addView(btDelete, (int) (INDICATOR_BOARD_HEIGHT / 2 * 1.5), INDICATOR_BOARD_HEIGHT / 2);
	}

	// ViewPager的适配器
	PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public View instantiateItem(ViewGroup container, int position) {

			// 填充resList,作为图片数据;
			ArrayList<EmotionBean> ebList = new ArrayList<EmotionBean>();
			for (int i = LINE_PER_PAGE * COUNT_PER_LINE * position; i < LINE_PER_PAGE * COUNT_PER_LINE * (position + 1); i++) {
				if (i >= EMOTION_TOTAL_NUM)
					break;
				try {
					Field field = R.drawable.class.getDeclaredField("emotion_" + (i + 1001));
					int resourceId = Integer.parseInt(field.get(null).toString());
					EmotionBean bean = new EmotionBean(resourceId, 1001 + i);
					ebList.add(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			GridView gridview = new GridView(getContext());
			gridview.setNumColumns(6);
			gridview.setAdapter(new EmotionGridAdapter(getContext(), ebList));
			gridview.setOnItemClickListener(onEmotionClickListener);
			container.addView(gridview, LayoutParams.MATCH_PARENT, EMITION_BOARD_HEIGHT);
			return gridview;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public int getCount() {
			return PAGE_COUNT;
		}
	};

	OnItemClickListener onEmotionClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			EmotionBean bean = (EmotionBean) arg1.getTag();

			Drawable drawable = blogEditText.getContext().getResources().getDrawable(bean.getResourceId());
			drawable.setBounds(0, 0, DoschoolApp.widthPixels / 20, DoschoolApp.widthPixels / 20);
			String str = "[e]" + bean.getEmotionId() + "[/e]";
			blogEditText.getEditableText().insert(blogEditText.getSelectionStart(), str);
			int postion = blogEditText.getSelectionStart();

			blogEditText.setText(blogEditText.strtosp(blogEditText.getText().toString()));
			blogEditText.setSelection(postion);

			// DoMethods.addAnimation(arg1);
		}
	};

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

	public ViewPager getViewPager() {
		return viewPager;
	}

}
