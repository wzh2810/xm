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
 * �Ҳ����ĸ����view
 * 
 * @author wz
 * 
 */
public class SideBar extends View {

	// �����¼�
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

	// 26����ĸ
	public static String[] alphabet = {  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" ,"#"};
	// ѡ��
	private int choose = -1;

	private Paint paint = new Paint();

	private TextView mTextDialog;

	/**
	 * λsideBar��ʾ��ĸ��TextView
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
	 * ��дonDraw�ķ���
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();// ��ȡ��Ӧ�ĸ߶�
		int width = getWidth();// ��ȡ����Ŀ��
		int singleHeight = height / alphabet.length;// ��ȡÿһ����ĸ�ĸ߶�
		for (int i = 0; i < alphabet.length; i++) {
			paint.setColor(Color.rgb(33, 65, 98));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(20);
			// ѡ�е�״̬
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);// �����Ƿ�Ϊ��������
			}
			// x�������=�м�-�ַ�����ȵ�һ��
			float xPos = (width - paint.measureText(alphabet[i])) / 2;
			float yPos = singleHeight * i + singleHeight;

			canvas.drawText(alphabet[i], xPos, yPos, paint);
			paint.reset(); // ���û���
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		int action = event.getAction();
		float y = event.getY();     //���y����
		int oldChoose = choose;
		
		OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		
		//���y������ռ�߶ȵı��� * alphabet����ĳ��Ⱦ͵��ڵ��b�еĸ���
		int c = (int) (y / getHeight() * alphabet.length);
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));//���ñ�����ɫ
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
	 *  �����ɿ��ķ���
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * �ӿ�
	 * 
	 * @author wz
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
}
