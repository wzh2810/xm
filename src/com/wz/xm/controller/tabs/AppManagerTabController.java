package com.wz.xm.controller.tabs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wz.xm.R;
import com.wz.xm.bean.AppInfo;
import com.wz.xm.utils.taskmanager.AppInfoParser;
import com.wz.xm.utils.taskmanager.DensityUtil;
import com.wz.xm.utils.taskmanager.ShowToast;

public class AppManagerTabController extends TabController {

	@ViewInject(R.id.lv_app_manager)
	private ListView lv_app_manager;

	@ViewInject(R.id.tv_app_load)
	private TextView tv_app_load;

	@ViewInject(R.id.tv_app_number)
	private TextView tv_app_number;

	private PopupWindow popup;// 点击打开悬浮窗口

	private AppInfo app;// 点击传递app信息 分享 卸载 开启 设置

	private MyAdap adapter;
	Handler han = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			tv_app_load.setVisibility(View.INVISIBLE);
			adapter = new MyAdap();
			lv_app_manager.setAdapter(adapter);
		}

	};

	public AppManagerTabController(Context context) {
		super(context);
	}

	/**
	 * 初始化controller里面持有的视图
	 * 
	 * @param context
	 * @return
	 */
	public View initContentView(Context context) {
		View view = View.inflate(mContext, R.layout.taskmanager_software, null);
		ViewUtils.inject(this, view);

		File exter = Environment.getExternalStorageDirectory();
		File dir = Environment.getDataDirectory();
		long freeSpace = exter.getFreeSpace();
		long dirSpace = dir.getFreeSpace();

		String outFile = Formatter.formatFileSize(mContext, freeSpace);
		String inFile = Formatter.formatFileSize(mContext, dirSpace);
		TextView tv_sdspase = (TextView) view.findViewById(R.id.tv_sdspase);
		TextView tv_dataspase = (TextView) view.findViewById(R.id.tv_dataspase);
		tv_sdspase.setText("SD卡剩余: " + outFile);
		tv_dataspase.setText("内存剩余:" + inFile);

		appData();

		lv_app_manager.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				dismiss();
				if (userApp != null && sysApp != null) {
					if (firstVisibleItem >= userApp.size() + 1) {
						tv_app_number.setText("系统程序" + sysApp.size() + "个");
					} else {
						tv_app_number.setText("用户程序" + userApp.size() + "个");
					}
				}
			}
		});

		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Object obj = lv_app_manager.getItemAtPosition(position);
				if (obj != null && obj instanceof AppInfo) {
					app = (AppInfo) obj;

					dismiss();
					// 弹出小窗体
					View vi = View.inflate(mContext, R.layout.appmanager_popup_item, null);
					LinearLayout ll_popup_uninstall = (LinearLayout) vi.findViewById(R.id.ll_popup_uninstall);
					LinearLayout ll_popup_start = (LinearLayout) vi.findViewById(R.id.ll_popup_start);
					LinearLayout ll_popup_share = (LinearLayout) vi.findViewById(R.id.ll_popup_share);
					LinearLayout ll_popup_setting = (LinearLayout) vi.findViewById(R.id.ll_popup_setting);
					ll_popup_uninstall.setOnClickListener(new UninstallOnClickListener());
					ll_popup_start.setOnClickListener(new StartOnClickListener());
					ll_popup_share.setOnClickListener(new ShareOnClickLisener());
					ll_popup_setting.setOnClickListener(new SettingOnClickListener());

					popup = new PopupWindow(vi, -2, -2);
					popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					int[] bys = new int[2];
					view.getLocationInWindow(bys);
					ObjectAnimator viewAlp = ObjectAnimator.ofFloat(vi, "alpha", 0.1f, 1);

					ObjectAnimator viewR = ObjectAnimator.ofFloat(vi, "scaleX", 0, 1);

					AnimatorSet viset = new AnimatorSet();
					viset.setDuration(500);
					viset.playTogether(viewAlp, viewR);
					viset.start();
					// int px2dip = DensityUtil.px2dip(AppManagerActivity.this,
					// 90);
					int dip2px = DensityUtil.dip2px(mContext, 60);
					popup.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, dip2px, bys[1] - 20);

				}
			}

		});
		unappReceive = new AppDeleteReceive();
		IntentFilter inf = new IntentFilter();
		inf.addAction(Intent.ACTION_PACKAGE_REMOVED);
		inf.addDataScheme("package");
		mContext.registerReceiver(unappReceive, inf);
		

		return view;
	}

	class ShareOnClickLisener implements OnClickListener {

		@Override
		public void onClick(View v) {
			shareApp();

		}

	}

	class SettingOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			setApp();
		}
	}

	class StartOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			startApp();
		}

	}

	class UninstallOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			appDelete();
			
		}

	}

	/**
	 * 加载数据,然后数据和视图进行绑定
	 */
	public void initData() {

	}

	private void dismiss() {
		if (popup != null && popup.isShowing()) {
			popup.dismiss();
			popup = null;
		}
	}

	private List<AppInfo> lai;

	private List<AppInfo> userApp;
	private List<AppInfo> sysApp;
	private AppDeleteReceive unappReceive;

	private void appData() {
		tv_app_load.setVisibility(View.VISIBLE);
		new Thread() {

			public void run() {
				lai = AppInfoParser.getAppInfos(mContext);

				userApp = new ArrayList<AppInfo>();
				sysApp = new ArrayList<AppInfo>();
				for (AppInfo app : lai) {
					if (app.isSys()) {
						// 用户应用
						userApp.add(app);
					} else {
						// 系统应用
						sysApp.add(app);
					}
				}
				han.sendEmptyMessage(0);
			};
		}.start();

	}

	private class Adaphandler {
		ImageView iv_app_ico;
		TextView tv_app_name;
		TextView tv_app_location;
		TextView tv_app_size;
	}

	private class MyAdap extends BaseAdapter {

		public int getCount() {

			return lai.size() + 2;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 0) {
				tv_app_number.setText("用户程序" + userApp.size() + "个");
				return new TextView(mContext);
			} else if (position == userApp.size() + 1) {
				TextView tvs = new TextView(mContext);
				tvs.setBackgroundColor(Color.GRAY);
				tvs.setTextColor(Color.WHITE);
				tvs.setText("系统程序" + sysApp.size() + "个");
				return tvs;
			}

			AppInfo app = null;
			if (position <= userApp.size()) {
				app = userApp.get(position - 1);
			} else {
				app = sysApp.get(position - userApp.size() - 2);
			}

			View view = null;
			Adaphandler adadler = null;
			if (convertView != null && convertView instanceof RelativeLayout) {
				// Log.i(TAG, "我是缓存");
				view = convertView;
				adadler = (Adaphandler) view.getTag();
			} else {
				view = View.inflate(mContext, R.layout.taskmanager_item, null);
				adadler = new Adaphandler();
				adadler.iv_app_ico = (ImageView) view.findViewById(R.id.iv_app_ico);
				adadler.tv_app_location = (TextView) view.findViewById(R.id.tv_app_location);
				adadler.tv_app_name = (TextView) view.findViewById(R.id.tv_app_name);
				adadler.tv_app_size = (TextView) view.findViewById(R.id.tv_app_size);
				view.setTag(adadler);
			}

			adadler.iv_app_ico.setImageDrawable(app.getIcon());
			String location = app.isRom() ? "内部存储" : "SD卡";
			adadler.tv_app_location.setText(location);
			adadler.tv_app_name.setText(app.getName());
			adadler.tv_app_size.setText(Formatter.formatFileSize(mContext, app.getSize()));
			return view;
		}

		public Object getItem(int position) {
			if (position == 0) {
				return null;
			} else if (position == userApp.size() + 1) {
				return null;
			}

			if (position <= userApp.size() + 1) {
				return userApp.get(position - 1);
			} else {
				return sysApp.get(position - userApp.size() - 2);
			}
		}

		public long getItemId(int position) {
			return position;
		}

	}

	private void startApp() {
		PackageManager pm = mContext.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(app.getPackName());
		if (intent != null) {
			a.startActivity(intent);
		} else {
			ShowToast.showToast((Activity) mContext, "打开失败");
		}

	}

	private void shareApp() {
//		Intent intent = new Intent();
//		intent.setAction("android.intent.action.SEND");
//		intent.addCategory("android.intent.category.DEFAULT");
//		intent.setType("text/plain");
//		intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件，名称叫：" + app.getName()
//				+ "下载路径：https://play.google.com/store/apps/details?id=" + app.getPackName());
//		a.startActivity(intent);
	}

	Activity a = (Activity) mContext;

	private void setApp() {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + app.getPackName()));
		// startActivity(intent);
		a.startActivity(intent);
	}

	// 卸载软件
	private void appDelete() {
		if (app != null) {
			if (app.isSys()) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DELETE);
				intent.setData(Uri.parse("package:" + app.getPackName()));
				a.startActivity(intent);
				
			} else {
				Toast.makeText(mContext, "您的手机没有Root权限", Toast.LENGTH_SHORT).show();
			}
			adapter.notifyDataSetChanged();
			appData();
		}
		
	}

	@Override
	public void initStateBar() {
		
		mTvTitle.setText("软件管理中心");
	}

	private class AppDeleteReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			appData();
		}

	}

}
