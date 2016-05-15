package com.wz.xm.controller.menu;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


public class PicsMenuController extends MenuController {

	public PicsMenuController(Context context) {
		super(context);
	}

	@Override
	public View initView(Context context) {
		// 先用静态的textView做一个展示
		TextView tv = new TextView(mContext);
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.RED);
		tv.setText("图片");
		return tv;
	}

}
