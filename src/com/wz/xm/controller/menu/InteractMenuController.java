package com.wz.xm.controller.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


public class InteractMenuController extends MenuController {

	public InteractMenuController(Context context) {
		super(context);
	}

	@Override
	public View initView(Context context) {
		// 先用静态的textView做一个展示
		TextView tv = new TextView(mContext);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.RED);
		tv.setText("互动对应菜单应该展示的数据");
		return tv;
	}

}
