package com.doschool.aa.aa;

import com.doschool.aa.widget.ActionBarLayout;
import com.doschool.aa.widget.SlidingTab;
import com.doschool.aa.widget.TabPagerSpec;
import com.doschool.aa.widget.TaskLayout;
import com.doschool.methods.DoMethods;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public abstract class Fgm_TabPager extends Fgm_Standard_Relative{

	/******** 尺寸控制 ****************************************/
	protected int mSwitchWidth;
	protected boolean isHideWhenBrowse = false;
	
	/******** 界面组件 ****************************************/
	protected SlidingTab mSlidingTab;
	protected ViewPager mViewPager;
	protected TaskLayout taskLayout;

	/******** 数据等 ****************************************/
	protected TabPagerSpec[] mTabSpecList;
	protected int mPageCount = 0; // 总共的页数
	protected int currentPageIndex = 0;
	protected String[] indicateList;
	
	/******** SlidingTab状态控制 ****************************************/
	int STATE_SLIDETAB_DOWN = 1;
	int STATE_SLIDETAB_UP = 2;
	int STATE_SLIDTAB_MOVING = 3;
	int state = STATE_SLIDETAB_DOWN;

	@Override
	public void addViewToFgm() {
		// 构造Tab，只需要一个字符串就ok
		mTabSpecList = giveTabPagerSpec();
		mPageCount = mTabSpecList.length;

		// 构造ViewPager
		mViewPager = new ViewPager(getActivity().getApplicationContext());
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setId("vp".hashCode());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(onPageChangeListener);

//		mSlidingTab = new zSlidingTab(getActivity().getApplicationContext());
//		mSlidingTab.setBackgroundColor(Color.WHITE);
//		mSlidingTab.setViewPager(mViewPager);
//		mSlidingTab.setOnPageChangeListener(onPageChangeListener);
		

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mScrnWidth, LayoutParams.MATCH_PARENT);
		lp.setMargins(0, ActionBarLayout.TOPBAR_HEIGHT, 0, 0);
		mParent.addView(mViewPager, lp);


		
		mSlidingTab=new SlidingTab(getActivity());
		mSlidingTab.setViewPager(mViewPager,onPageChangeListener);
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(mScrnWidth, mDip*40);
		lp2.setMargins(0, ActionBarLayout.TOPBAR_HEIGHT, 0, 0);
		mParent.addView(mSlidingTab, lp2);
		
		
		
		
	}

	public abstract TabPagerSpec[] giveTabPagerSpec();

	public abstract void onPageChanged(int newIndex);

	private PagerAdapter mPagerAdapter = new android.support.v4.view.PagerAdapter() {

		// 这个方法决定了ViewPager每个页面显示的东西
		@Override
		public View instantiateItem(ViewGroup container, int position) {

			View view = mTabSpecList[position].content;
			container.addView(view, position);
			return view;
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
		public CharSequence getPageTitle(int position) {
			return mTabSpecList[position].indicator;
		}

		@Override
		public int getCount() {
			return mTabSpecList.length;
		}
	};

	OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			currentPageIndex = arg0;
			onPageChanged(currentPageIndex);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	};

	/**
	 * ListView的滚动监听，主要实现SlidingTab的动态显示隐藏。
	 */
	protected class onListViewScrollListener extends PauseOnScrollListener {

		int oldFirstVisibleItem;
		int oldTopOffest;
		
		public onListViewScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
			super(imageLoader, pauseOnScroll, pauseOnFling);
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			super.onScrollStateChanged(view, scrollState);
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			
			if (isHideWhenBrowse) {
				
				Log.v("FI="+firstVisibleItem, "yoyoyo");
				int newOffset=0;
				try {
					newOffset=-view.getChildAt(0).getTop();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (firstVisibleItem > oldFirstVisibleItem && state == STATE_SLIDETAB_DOWN) {
					smoothHideSlidingTab();
				} else if (firstVisibleItem < oldFirstVisibleItem && state == STATE_SLIDETAB_UP) {
					smoothShowSlidingTab();
				}
				
				else if(firstVisibleItem == oldFirstVisibleItem)
				{
					if(newOffset-oldTopOffest>mDip*3 && state == STATE_SLIDETAB_DOWN){
						smoothHideSlidingTab();
					} else if (newOffset - oldTopOffest<-mDip*3 && state == STATE_SLIDETAB_UP) {
						smoothShowSlidingTab();
					}
				}
				oldFirstVisibleItem=firstVisibleItem;
				oldTopOffest=newOffset;
			}
			
			
		}
	}

	private void performAnimate(final View target, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);

        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //获得当前动画的进度值，整型，1-100之间
                int currentValue = (Integer)animator.getAnimatedValue();
//                Log.d(TAG, current value:  + currentValue);

                //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                float fraction = currentValue / 100f;

                //这里我偷懒了，不过有现成的干吗不用呢
                //直接调用整型估值器通过比例计算出宽度，然后再设给Button
                RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) target.getLayoutParams();
        		
                lp.topMargin = mEvaluator.evaluate(fraction, start, end);
                target.requestLayout();
            }
        });

        valueAnimator.setDuration(300).start();
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 舒缓的隐藏SlidingTab
	 */
	public void smoothHideSlidingTab() {
//		Animation translateAnim = new TranslateAnimation(0, 0, 0, -mSlidingTab.getHeight());
//		translateAnim.setDuration(300);
//		translateAnim.setFillAfter(true);
//		mSlidingTab.startAnimation(translateAnim);
////		getActionBar().startAnimation(translateAnim);
//		translateAnim.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//				state = STATE_SLIDTAB_MOVING;
//			}
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				state = STATE_SLIDETAB_UP;
//				RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) mSlidingTab.getLayoutParams();
//				lp.setMargins(lp.leftMargin, 8*mDip, lp.rightMargin, lp.bottomMargin);
//				mSlidingTab.setLayoutParams(lp);
//			}
//		});
		
		
//		for (int i = 0; i < 10; i++) {
//			DoMethods.sleep(300);
//			state = STATE_SLIDTAB_MOVING;
//			RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) mSlidingTab.getLayoutParams();
//			lp.setMargins(lp.leftMargin, (48-40/10*i)*mDip, lp.rightMargin, lp.bottomMargin);
//			mSlidingTab.setLayoutParams(lp);
//		}
//		RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) mSlidingTab.getLayoutParams();
//		
		
		state = STATE_SLIDTAB_MOVING;
		RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) mSlidingTab.getLayoutParams();
		performAnimate(mSlidingTab, lp.topMargin, mDip*8);

		state = STATE_SLIDETAB_UP;
	}

	
	
	
	
	
	
	
	/**
	 * 舒缓的显示SlidingTab
	 */
	public void smoothShowSlidingTab() {

//		for (int i = 0; i < 10; i++) {
//			DoMethods.sleep(300);
//			state = STATE_SLIDTAB_MOVING;
//			RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) mSlidingTab.getLayoutParams();
//			lp.setMargins(lp.leftMargin, (8+40/10*i)*mDip, lp.rightMargin, lp.bottomMargin);
//			mSlidingTab.setLayoutParams(lp);
//		}
//
//		state = STATE_SLIDETAB_DOWN;
		
		state = STATE_SLIDTAB_MOVING;
		RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) mSlidingTab.getLayoutParams();
		performAnimate(mSlidingTab, lp.topMargin, mDip*48);
		state = STATE_SLIDETAB_DOWN;
		
//		
//		Animation translateAnim = new TranslateAnimation(0, 0, 0, mSlidingTab.getHeight());
//		translateAnim.setDuration(300);
//		translateAnim.setFillAfter(true);
//		mSlidingTab.startAnimation(translateAnim);
////		getActionBar().startAnimation(translateAnim);
//		translateAnim.setAnimationListener(new AnimationListener() {
//			@Override
//			public void onAnimationStart(Animation animation) {
//				state = STATE_SLIDTAB_MOVING;
//			}
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				state = STATE_SLIDETAB_DOWN;
//				RelativeLayout.LayoutParams lp=(android.widget.RelativeLayout.LayoutParams) mSlidingTab.getLayoutParams();
//				lp.setMargins(lp.leftMargin, 48*mDip, lp.rightMargin, lp.bottomMargin);
//				mSlidingTab.setLayoutParams(lp);
//			}
//		});

	}
	
	
//	/**
//	 * ListView的滚动监听，主要实现SlidingTab的动态显示隐藏。
//	 */
//	protected class onListViewScrollListener extends PauseOnScrollListener {
//
//		private int lastY;
//		boolean counting;
//		HashMap<Integer, Integer> heightMap = new HashMap<Integer, Integer>();
//
//		public onListViewScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
//			super(imageLoader, pauseOnScroll, pauseOnFling);
//		}
//
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//			super.onScrollStateChanged(view, scrollState);
//		}
//
//
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//			super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//
//			View firstView = view.getChildAt(0);
//			if (firstView != null)
//				heightMap.put(firstVisibleItem, firstView.getHeight());
//
//			if (counting == false) {
//				counting = true;
//
//				int scrollY = 0;
//				if (firstView != null) {
//					int topOffest = firstView.getTop();
//					scrollY -= topOffest;
//					for (int i = 0; i < firstVisibleItem; i++) {
//						try {
//							scrollY += heightMap.get(i);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//
//				if (scrollY > lastY && state == STATE_SLIDETAB_DOWN) {
//					smoothHideSlidingTab();
//				} else if (scrollY < lastY && state == STATE_SLIDETAB_UP) {
//					smoothShowSlidingTab();
//				}
//
//				lastY = scrollY;
//				counting = false;
//			}
//		}
//	}
//
//	int STATE_SLIDETAB_DOWN = 1;
//	int STATE_SLIDETAB_UP = 2;
//	int STATE_SLIDTAB_MOVING = 3;
//	int state = STATE_SLIDETAB_DOWN;

}
