package com.wz.xm.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class HomeLinearLayout extends LinearLayout {

	public HomeLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//屏幕的三分之一
				int width = getWidth() / getChildCount();
				int height = getHeight();
				int count = getChildCount();
				
				float eventX = event.getX();
				
				if(eventX < width) {
					event.setLocation(width / 2, event.getY());
					getChildAt(0).dispatchTouchEvent(event);
					return true;
				} else if (eventX > width && eventX < 2 * width) {
					float eventY = event.getY();
					if(eventY < height / 2) {
						event.setLocation(width / 2, event.getY());
						for(int i = 0; i < count; i ++) {
							View child = getChildAt(i);
							child.dispatchTouchEvent(event);
						}
						return true;
					} else if (eventY > height / 2) {
						event.setLocation(width / 2, event.getY());
						getChildAt(1).dispatchTouchEvent(event);
						return true;
					}
				} else if(eventX > 2 * width) {
					event.setLocation(width / 2, event.getY());
					getChildAt(2).dispatchTouchEvent(event);
					return true;
				}
				return true;
	}

}
