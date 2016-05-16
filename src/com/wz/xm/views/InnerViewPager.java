package com.wz.xm.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;



public class InnerViewPager extends ViewPager {
	private float				mDownX;
	private float				mDownY;
	public static final String	TAG	= InnerViewPager.class.getSimpleName();

	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InnerViewPager(Context context) {
		super(context);
	}

	/**
	 getparent(������).request(����)Disallow(��)Intercept(����)TouchEvent(touch�¼�)(true(ͬ��))->�����ײ�����-->�Լ������¼�
	 requestDisallowInterceptTouchEvent(false)->������������
	 if(postion==0){
	 	 if(����){
	 		���״���
	 	 }else{
	 	 	���Ӵ���
	   	}
	 }else if(postion == getCount()-1){
	 	if(����){
	 		���Ӵ���
	 	 }else{
	 	 	���״���
	   	}
	 
	 }else{
	 	�Լ�����
	 }
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getRawX();
			mDownY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = ev.getRawX();
			float moveY = ev.getRawY();

			int diffX = (int) (moveX - mDownX + .5f);

			int position = this.getCurrentItem();
			/**
			 if(postion==0){
			 	if(����){
				���״���
			 	}else{
			 	���Ӵ���
				}
			}else if(postion == getCount()-1){
				if(����){
					���Ӵ���
			 	}else{
			 		���״���
			}
			
			}else{
				�Լ�����
			}
			 */
			if (position == 0) {
				if (diffX > 0) {// ����
					Log.i(TAG, "����-->���״���");
					getParent().requestDisallowInterceptTouchEvent(false);// ���״���
				} else {
					Log.i(TAG, "����-->�Լ�����");
					getParent().requestDisallowInterceptTouchEvent(true);// �Լ�����
				}
			} else if (position == getAdapter().getCount() - 1) {
				if (diffX > 0) {// ����
					Log.i(TAG, "����-->�Լ�����");
					getParent().requestDisallowInterceptTouchEvent(true);// �Լ�����
				} else {// ����
					Log.i(TAG, "����-->���״���");
					getParent().requestDisallowInterceptTouchEvent(false);// ���״���
				}
			} else {
				Log.i(TAG, "�Լ�����");
				getParent().requestDisallowInterceptTouchEvent(true);// �Լ�����
			}

			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO
		return super.onTouchEvent(ev);
	}

}
