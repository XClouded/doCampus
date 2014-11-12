package com.doschool.app;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONException;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RemoteViews;

import com.baidu.frontia.FrontiaApplication;
import com.doschool.R;
import com.doschool.component.updatelater.Task;
import com.doschool.component.updatelater.TaskManage;
import com.doschool.entity.BlogMedal;
import com.doschool.entity.Person;
import com.doschool.entity.SimplePerson;
import com.doschool.entity.User;
import com.doschool.methods.ConvertMethods;
import com.doschool.methods.DBHelper;
import com.doschool.methods.DoMethods;
import com.doschool.methods.SpMethods;
import com.doschool.zother.MJSONObject;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class DoschoolApp extends FrontiaApplication {
	
//	public static final int CREATE = 0;
//	public static final int RESUME = 2;
//	public static final int RUNNING = 3; //仅作为判断使用 eg. STATU<RUNNING
//	public static final int PAUSE = 4;
//	//如果考虑奇葩用户这尼玛不是没用么。。
//	public static final int DESTROY = 5;
	//包名确定吗。。
//	public static String packageName = null;
//	DoActList<DoAct> activities;
	String mainAct = "com.doschool.aa.activity.zAct_Main";
	
	
	WebView qtwb;
	
	public WebView getQTWB(){
		return qtwb;
	}

	public void setQTWB(WebView wb){
		qtwb = wb;
	}

	
	public static NotificationManager notificationManager= null;
	public static Thread r;
	boolean alreadyInit = false;
	@Override
	public void onCreate() {
		Log.i("LOGINING","Not Dou Me");
		if(!alreadyInit){
//			FrontiaApplication.initFrontiaApplication(getApplicationContext());
			initDong();

		}
		alreadyInit = true;
		super.onCreate();

	}
	
	//到这里的方法是保证该类持有各种需求数据的
	public void finishToMe(int type){
		if(type !=6){
			Intent it = new Intent();
			it.setClassName(this,mainAct);
			it.putExtra("type", type);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(it);
		}
	}
		
	
	class mRemoteViews extends RemoteViews{
		DoschoolApp app;
		public mRemoteViews(String p,int id,Application a){
			super(p,id);
			app=(DoschoolApp)a;
			new Thread(){
				public void run(){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("BDMESSAGE","222nihao~~~");
				}
			}.start();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/******** 尺寸大小 ****************************************/
	public static int widthPixels=0;		//屏幕宽度
	public static int heightPixels=0;		//屏幕高度
	public static int pxperdp=0;
	
	/******** 数量控制 ****************************************/
	public static final int BLOG_LIST_LOAD_COUNT = 10;
	public static final int BLOG_PIC_MAX_COUNT = 9;
	public static final int BLOG_CONTENT_CHARACTER_NUM = 1000;
	public static final int CMT_CONTENT_CHARACTER_NUM = 1000;
	public static String CHANGE_LINE_STRING="<br>";
		
	/******** 文件路径 ****************************************/
	public static String BLOG_AFTER_CROP = "blog_after_crop.jpg";
	public static String OTHER_AFTER_CROP = "other_after_crop.jpg";

	public static String BTF = "_btf.jpg";
	public static final int GUESTID = 12092;

	public static String SERVER_URL;
	public static boolean isTimeGet=false;
	public static long SERVER_TIME_FWQ_LINGXIAN_PHONE;
	
	/******** 数据集合 ****************************************/
	
	public static  String UPLOAD_LATER_ACTION_NAME = "dobell.missan.uploadlater";

	public static ArrayList<SimplePerson> friendList=new ArrayList<SimplePerson>();
	public static ArrayList<Person> cardsList=new ArrayList<Person>();
	public static User thisUser;
	public static int isNotification;
	public static int versionCode;
	
	public static DBHelper mDBHelper;
	public static SQLiteDatabase db;

	
	public static ImageLoader newImageLoader = ImageLoader.getInstance(); 
	public static DisplayImageOptions dioRound;
	public static DisplayImageOptions dioSquare;
	public static DisplayImageOptions dioPersonBg;
	public static DisplayImageOptions dioGallery;
	public static DisplayImageOptions dioSquareSmall;
	
	public void initDong()
	{
		mDBHelper=new DBHelper(getApplicationContext());
		db=mDBHelper.getWritableDatabase();
		
		pxperdp=ConvertMethods.dip2px(getApplicationContext(), 1);
			DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
			widthPixels = dm.widthPixels;
			heightPixels = dm.heightPixels;
		try {
			thisUser=new User(new MJSONObject(User.loadUserInfo(getApplicationContext())));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			thisUser=new User(new MJSONObject());
		}
		versionCode=DoMethods.getVersionCode(getApplicationContext());
		initUniversalImageDisplay();
	}
	
	
	public static boolean isGuest()
	{
		return thisUser.personId==GUESTID;
	}
	
	
	

	public void initUniversalImageDisplay()
	{

		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(getApplicationContext())  
//			    .memoryCacheExtraOptions(widthPixels/2, heightPixels/3) // max width, max height，即保存的每个缓存文件的最大长宽  
			    .threadPoolSize(3)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
//			    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
//			    .memoryCacheSize(2 * 1024 * 1024)    
			    .tasksProcessingOrder(QueueProcessingType.LIFO)
			    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
			    .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
			    .writeDebugLogs() // Remove for release app  
			    .build();//开始构建  
		ImageLoader.getInstance().init(config);//全局初始化此配置  
		

        //是不是无图模式
        boolean noPicMode = SpMethods.loadBoolean(getApplicationContext(), SpMethods.MODE_NOPIC_DOWNLOAD, false);
        newImageLoader.denyNetworkDownloads(noPicMode);

		if(SpMethods.loadInt(getApplicationContext(), SpMethods.LASTEST_VERSION_CODE_USED, 0)!=DoMethods.getVersionCode(getApplicationContext()))
		{
			newImageLoader.clearDiskCache();
			newImageLoader.clearMemoryCache();
		}
		
		SpMethods.saveInt(getApplicationContext(), SpMethods.LASTEST_VERSION_CODE_USED, DoMethods.getVersionCode(getApplicationContext()));
		
		
		dioRound = new DisplayImageOptions.Builder()  
		.bitmapConfig(Bitmap.Config.RGB_565)
		 .showImageOnLoading(R.drawable.round_head_grey) //设置图片在下载期间显示的图片  
		 .showImageForEmptyUri(R.drawable.icon_network_error)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.icon_network_error)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.displayer(new RoundedBitmapDisplayer(1000))//是否设置为圆角，弧度为多少  
//		.displayer(new FadeInBitmapDisplayer(500))//是否图片加载好后渐入的动画时间  
		.build();//构建完成  
		
		dioSquare = new DisplayImageOptions.Builder()  
		.bitmapConfig(Bitmap.Config.RGB_565)
		 .showImageOnLoading(R.color.light_greyblue3) //设置图片在下载期间显示的图片  
		 .showImageForEmptyUri(R.drawable.icon_network_error)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.icon_network_error)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.build();//构建完成  
		

		dioPersonBg = new DisplayImageOptions.Builder()  
		.bitmapConfig(Bitmap.Config.RGB_565)
		 .showImageOnLoading(R.drawable.img_person_back) //设置图片在下载期间显示的图片  
		 .showImageForEmptyUri(R.drawable.img_person_back)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.icon_network_error)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.build();//构建完成  
		
		BitmapFactory.Options options=new Options();
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inSampleSize=32;
		
		dioSquareSmall = new DisplayImageOptions.Builder()
		.decodingOptions(options)
		.bitmapConfig(Bitmap.Config.RGB_565)
		 .showImageOnLoading(R.color.light_greyblue3) //设置图片在下载期间显示的图片  
		 .showImageForEmptyUri(R.color.light_greyblue3)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.color.light_greyblue3)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.build();//构建完成  
		
		dioGallery = new DisplayImageOptions.Builder()  
		.bitmapConfig(Bitmap.Config.RGB_565)
		 .showImageOnLoading(R.color.black) //设置图片在下载期间显示的图片  
		 .showImageForEmptyUri(R.drawable.icon_network_error)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.icon_network_error)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.build();//构建完成  
		
		
	}
	
}
