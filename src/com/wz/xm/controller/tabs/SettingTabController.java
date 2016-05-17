package com.wz.xm.controller.tabs;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


public class SettingTabController extends TabController {

	public SettingTabController(Context context) {
		super(context);
	}

	/**
	 * 初始化controller里面持有的视图
	 * @param context
	 * @return
	 */
	public View initContentView(Context context) {
		TextView tv = new TextView(context);
		tv.setGravity(Gravity.CENTER);
		tv.setText("还没想好");
		return tv;
	}

	/**
	 * 加载数据,然后数据和视图进行绑定
	 */
	public void initData() {
		
	}

	@Override
	public void initStateBar() {
		mTvTitle.setText("设置");
		// 隐藏menu
		mIbMenu.setVisibility(8);
		
	}
}
