package com.doschool.aa.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.R;
import com.doschool.aa.aa.Fgm_Standard_Linear;
import com.doschool.aa.aa.Fgm_Standard_Relative;
import com.doschool.aa.tools.ActivityTool;
import com.doschool.aa.tools.LoadingProgressView;
import com.doschool.aa.tools.NetWorkUtils;
import com.doschool.aa.tools.OnGetToolsInfoLis;
import com.doschool.aa.tools.ToolInfo;
import com.doschool.aa.widget.ProgressImageView;
import com.doschool.aa.widget.WidgetFactory;
import com.doschool.app.DoschoolApp;
import com.doschool.methods.SpMethods;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.umeng.analytics.MobclickAgent;

public class Fgm_Tools extends Fgm_Standard_Linear implements OnItemClickListener {

	/**** 小工具列表的Adapter **********/
	class ToolsListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// return 1;
			return toolsInfo==null?0:toolsInfo.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			if(convertView!=null)return convertView;
			final RelativeLayout rl = new RelativeLayout(getActivity());
			LinearLayout ll = new LinearLayout(getActivity());
			rl.setLayoutParams(new GridView.LayoutParams(
					DoschoolApp.widthPixels * 10 / 50,
					DoschoolApp.widthPixels * 13 / 40));
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					DoschoolApp.widthPixels * 10 / 50,
					DoschoolApp.widthPixels * 10 / 50);

			ll.setOrientation(LinearLayout.VERTICAL);
			
			final ProgressImageView ivImage = new ProgressImageView(
					getActivity(), 200);
			ll.addView(ivImage, llp);
			TextView t = WidgetFactory.createTextView(getActivity(),
					toolsInfo.get(position).name, 0, 15);
			ll.addView(t, LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			t.setGravity(Gravity.CENTER);
			rl.addView(ll, RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			final int status = toolsInfo.get(position).status;
			final String name = toolsInfo.get(position).name;
			Log.i("TOOLINFO", toolsInfo.get(position).iconURL + "  n: " + name
					+ " " + status);
			String icU = toolsInfo.get(position).iconURL;
			// if(position==0){
			// icU =
			// "http://bcs.duapp.com/doschool-database/%2FToolLogo%2Fkscx.png?sign=MBO:E041ffd82e2e6068b99429f8eebde832:VODpJGPFPwBDTDLhFk2cypsUNjQ%3D";
			// }
			final String ficu = icU;
			final int tid = toolsInfo.get(position).toolid;
			final int isPb = toolsInfo.get(position).isPublic;
			DoschoolApp.newImageLoader.displayImage(ficu, ivImage.mImageView,
					DoschoolApp.dioSquare, new ImageLoadingListener() {

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1,
								Bitmap arg2) {
							ivImage.mCircleProgress
									.setVisibility(View.INVISIBLE);
							if (getActivity()!=null && (status == 2 || (isVisitor && isPb != 1))) {
								Log.i("TOOLINFO", "wawa stat" + status);
								View cov = new View(getActivity());
								cov.setBackgroundDrawable(new ColorDrawable(
										0xaff2f2f2));
								rl.addView(cov,
										DoschoolApp.widthPixels * 10 / 50,
										DoschoolApp.widthPixels * 10 / 50);
							}

							else if (SpMethods.loadInt(getActivity(), name
									+ "isNewTool", 1) == 1
									&& tid != 3 && tid != 4 && tid != 5) {
								Log.i("TOOLINFO", "wawa isNewTool");
								View cov = new View(getActivity());
								Bitmap bitmap = BitmapFactory.decodeResource(
										getResources(), R.drawable.tool_new);
								cov.setBackgroundDrawable(new BitmapDrawable(
										bitmap));
								rl.addView(cov);
								((RelativeLayout.LayoutParams) cov
										.getLayoutParams()).topMargin = 0;
								((RelativeLayout.LayoutParams) cov
										.getLayoutParams()).leftMargin = 0;
								((RelativeLayout.LayoutParams) cov
										.getLayoutParams()).width = DoschoolApp.widthPixels * 10 / (50);
								((RelativeLayout.LayoutParams) cov
										.getLayoutParams()).height = DoschoolApp.widthPixels * 10 / (50);
							}
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
							
						}

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							ivImage.mCircleProgress.setProgress(0);
							ivImage.mCircleProgress.setVisibility(View.VISIBLE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, final int current, final int total) {
							Log.i("TOOOLonProgressUpdate", "" + 100 * current
									/ (total + 1));
							ivImage.mCircleProgress.setProgress(100 * current
									/ (total + 1));
						}
					});
//			convertView = rl;
			return rl;
		}
	}

	List<ToolInfo> toolsInfo;

	// Resources pluginRes[];
	// List<ApplicationInfo> pluginList;
	// List<Integer> pluginIconId = new ArrayList<Integer>();
	// List<String> pluginName = new ArrayList<String>();


	Handler handler;

	@Override
	public void addViewToFgm() {
		
		handler = new Handler();
		adp = new ToolsListAdapter();
		rl = new RelativeLayout(getActivity());
		proLay = new LoadingProgressView(getActivity(), 8);
		GridView grid = new GridView(getActivity());
		grid.setNumColumns(3);
		grid.setHorizontalSpacing(DoschoolApp.widthPixels / 10);
		grid.setGravity(Gravity.TOP);
		grid.setAdapter(adp);
		grid.setOnItemClickListener(this);
		grid.setPadding(DoschoolApp.widthPixels / 10,
				DoschoolApp.widthPixels / 30, DoschoolApp.widthPixels / 10, 0);
		rl.addView(grid);
		upDataAndUI();
		mParent.addView(rl, new LinearLayout.LayoutParams(mScrnWidth, LayoutParams.MATCH_PARENT, 2000));
		
	}
	boolean isVisitor;
	private List<ToolInfo> disposeToolsInfo(String info) throws JSONException {
		JSONObject json = new JSONObject(info);
		List<ToolInfo> infos = new ArrayList<ToolInfo>();
		int code = json.getInt("code");
		JSONArray ary = json.getJSONArray("data");
		isVisitor = DoschoolApp.isGuest();
//		Log.
		if (code == 0) {
			for (int i = 0; i < ary.length(); i++) {
				JSONObject obj = ary.getJSONObject(i);
				int toolid = obj.getInt("toolid");
				String name = obj.getString("cnname");
				int type = obj.getInt("type");
				String iconURL = obj.getString("logosite");
				String serURL = obj.getString("locationsite");
				int ver = obj.getInt("version");
				int isPublic = obj.getInt("public");
				int status = obj.getInt("status");
				
				int needShare = obj.getInt("needshare");
				
				String jsName = "",shareURL = "";
				if(needShare == 1){
				   jsName = obj.getString("jsname");
				   shareURL = obj.getString("sharesite");
				}
				
				ToolInfo tin = new ToolInfo(toolid, name, type, iconURL,
						serURL, ver, isPublic, status,needShare,shareURL,jsName);
				infos.add(tin);
				
			}
		}
		return infos;
	}
	/*
	// 初始化数据
	// public void initData() {
	// initPlugin();
	// PackageManager pm = getActivity().getPackageManager();
	// pluginRes = new Resources[pluginList.size()];
	//
	// for (int i = 0; i < pluginList.size(); i++) {
	// ApplicationInfo ainfo = pluginList.get(i);
	// // String div = System.getProperty("path.separator");
	// String packageName = ainfo.packageName;
	// String dexPath = ainfo.sourceDir;
	// String dexOutputDir = getActivity().getApplicationInfo().dataDir;
	// String libPath = ainfo.nativeLibraryDir;
	// DexClassLoader cl = new DexClassLoader(dexPath, dexOutputDir,
	// libPath, getClass().getClassLoader());
	// try {
	// pluginRes[i] = pm.getResourcesForApplication(packageName);
	// } catch (NameNotFoundException e) {
	// e.printStackTrace();
	// continue;
	// }
	// int id = 0;
	// // 获取图标资源
	// id = pluginRes[i].getIdentifier("icon_plugin", "drawable",
	// packageName);
	// pluginIconId.add(id);
	// // 生成一个类实例
	// Class<?> clz = null;
	// try {
	// clz = cl.loadClass(packageName + ".PluginInfo");
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// continue;
	// }
	// Object obj;
	// String name = null;
	//
	// try {
	// obj = clz.newInstance();// PluginInfo类的实例
	// Method action = obj.getClass().getDeclaredMethod(
	// "getPluginName");
	// action.setAccessible(true);
	// Object ret = action.invoke(obj);
	// name = (String) ret;
	// } catch (Exception e) {
	// e.printStackTrace();
	// continue;
	// }
	// pluginName.add(name);
	// }
	// }
	//
	// private void initPlugin() {
	// PackageManager pm = getActivity().getPackageManager();
	// List<ApplicationInfo> appList = pm
	// .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
	// pluginList = new ArrayList<ApplicationInfo>();
	// for (int i = 0; i < appList.size(); i++) {
	// if (appList.get(i).packageName.contains("doschool.plugin")) {
	// Log.i("pluginT", appList.get(i).packageName);
	// pluginList.add(appList.get(i));
	// }
	// }
	// }
	*/

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// inflater.inflate(R.menu.fgm_friends, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	ToolsListAdapter adp;
	RelativeLayout rl;
	LoadingProgressView  proLay;

	/**** 小工具列表的item监听器 **********/
	// TODO 我修改这个方法，通过传intent键值对，让页面知道调用哪一个插件。
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int p, long arg3) {
		SpMethods.saveInt(getActivity(), toolsInfo.get(p).name
				+ "isNewTool", 0);
		Intent it = new Intent(getActivity(), ActivityTool.class);
		
		it.putExtra("name", toolsInfo.get(p).name);
		it.putExtra("toolid", toolsInfo.get(p).toolid);
		it.putExtra("serURL", toolsInfo.get(p).serURL);
		it.putExtra("status", toolsInfo.get(p).status);
		it.putExtra("isPublic", toolsInfo.get(p).isPublic);
		it.putExtra("type", toolsInfo.get(p).type);
		it.putExtra("needShare", toolsInfo.get(p).needShare);
		if(toolsInfo.get(p).needShare == 1){
			it.putExtra("jsName", toolsInfo.get(p).jsName);
			it.putExtra("shareURL", toolsInfo.get(p).shareURL);
		}
		
		adp.notifyDataSetChanged();
		if (getToolsIfL != null) {
			getToolsIfL.changeUI(isNew());
		}
		
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("name", toolsInfo.get(p).name);
		map.put("serURL", toolsInfo.get(p).serURL);
		map.put("status", toolsInfo.get(p).status+"");
		map.put("ToolsId", ""+toolsInfo.get(p).toolid);
		MobclickAgent.onEvent(getActivity(), "ToolsClick",  map);
		
		startActivity(it);
		getActivity().overridePendingTransition(R.anim.in_from_right,
				R.anim.out_to_left);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			((SlidingFragmentActivity) getActivity()).getSlidingMenu()
					.showMenu();
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	private void upToolInfoData() {
		Log.i("TOOLINFO","下载小工具列表信息");
		// 参数：schoolId（安徽大学为1，合工大为2），terminalId（例如1表示andriod，2表示ios）
		String jstr = NetWorkUtils.post(
				"http://commsev4all.duapp.com/common/ToolsConfigGet.php",
				"schoolId=1&terminalId=1");
		JSONObject json = null;
		try {
			json = new JSONObject(jstr);
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
		int code = -1;
		try {
			code = json.getInt("code");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
		if (code == 0 && getActivity()!=null)
			SpMethods.saveString(getActivity(), SpMethods.TOOL_INFOS,
					jstr);

	}

	private void upDataAndUI() {
		new Thread() {
			public void run() {
				
				handler.post(new Runnable() {

					@Override
					public void run() {
						upUI();
						rl.addView(proLay,
								RelativeLayout.LayoutParams.MATCH_PARENT,
								RelativeLayout.LayoutParams.MATCH_PARENT);
						proLay.beginMoving();

					}
				});

				upToolInfoData();
				handler.post(new Runnable() {

					@Override
					public void run() {
						proLay.stopMoving();
						ViewGroup vgp = ((ViewGroup) proLay.getParent());
						if (vgp != null)
							vgp.removeView(proLay);
					}
				});

				handler.post(new Runnable() {

					@Override
					public void run() {
						upUI();
					}
				});

			}
		}.start();
	}
	
	private boolean isNew(){
		for(int i=0;i<toolsInfo.size();i++){
			int tid = toolsInfo.get(i).toolid;
			String name = toolsInfo.get(i).name;
			if (SpMethods.loadInt(getActivity(), name + "isNewTool", 1) == 1
					&& tid != 3 && tid != 4 && tid != 5)

			{
				return true;
			}
		}
		return false;
	}
	
	
	
	OnGetToolsInfoLis getToolsIfL;
	
	public void setOnGetToolsInfoLis(OnGetToolsInfoLis l){
		this.getToolsIfL = l;
	}
	
	private void upUI() {
		if (getActivity() == null)
			return;
		try {
			toolsInfo = disposeToolsInfo(SpMethods.loadString(
					getActivity(), SpMethods.TOOL_INFOS));
			if(getToolsIfL != null){
				getToolsIfL.changeUI(isNew());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
		adp.notifyDataSetChanged();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}


}
