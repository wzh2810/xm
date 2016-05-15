package com.wz.xm.controller.tabs;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


public class GovAffaireTabController extends TabController {

	public GovAffaireTabController(Context context) {
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
		tv.setText("政务");
		return tv;
	}

	/**
	 * 加载数据,然后数据和视图进行绑定
	 */
	public void initData() {
		mTvTitle.setText("政务");
	}

	@Override
	public void initStateBar() {
		// TODO Auto-generated method stub
		
	}
}
