package com.wz.xm.views;

import com.wz.xm.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 右侧的字母索引view
 * 
 * @author wz
 * 
 */
public class SideBar extends View {

	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	// 26个字母
	public static String[] alphabet = {  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" ,"#"};
	// 选中
	private int choose = -1;

	private Paint paint = new Paint();

	private TextView mTextDialog;

	/**
	 * 位sideBar显示字母的TextView
	 * 
	 * @param mTextDialog
	 */
	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context) {
		super(context);
	}

	/**
	 * 重写onDraw的方法
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();// 获取对应的高度
		int width = getWidth();// 获取对象的宽度
		int singleHeight = height / alphabet.length;// 获取每一个字母的高度
		for (int i = 0; i < alphabet.length; i++) {
			paint.setColor(Color.rgb(33, 65, 98));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(20);
			// 选中的状态
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);// 设置是否为粗体文字
			}
			// x坐标等于=中间-字符串宽度的一半
			float xPos = (width - paint.measureText(alphabet[i])) / 2;
			float yPos = singleHeight * i + singleHeight;

			canvas.drawText(alphabet[i], xPos, yPos, paint);
			paint.reset(); // 重置画笔
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		int action = event.getAction();
		float y = event.getY();     //点击y坐标
		int oldChoose = choose;
		
		OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		
		//点击y坐标所占高度的比例 * alphabet数组的长度就等于点击b中的个数
		int c = (int) (y / getHeight() * alphabet.length);
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));//设置背景颜色
			choose = -1;
			invalidate();
			if(mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_background);
			if(oldChoose != c) {
				if(c >= 0 && c < alphabet.length) {
					if(listener != null) {
						listener.onTouchingLetterChanged(alphabet[c]);
					}
					if(mTextDialog != null) {
						mTextDialog.setText(alphabet[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}
					choose = c;
					invalidate();
				}
			}
			break;
		}
		
		return true;
	}

	/**
	 *  向外松开的方法
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author wz
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
}
