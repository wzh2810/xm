package com.wz.xm.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.wz.xm.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class RefreshListView extends ListView implements OnScrollListener, OnClickListener {

	public static final String	TAG						= RefreshListView.class.getSimpleName();

	private ImageView			mHeaderViewArrow;
	private FrameLayout			mHeaderViewCustomContainer;
	private ProgressBar			mHeaderViewPb;
	private TextView			mHeaderViewState;
	private TextView			mHeaderViewUpdateTime;
	private View				mHeaderView;
	private RelativeLayout		mRefreshHeaderView;
	private int					mInitRefreshHeaderViewPaddingTop;									// ����ˢ��ͷ���ֳ�ʼ��paddingTop
	private float				mDownX;
	private float				mDownY;

	// ����ˢ��
	public static final int		STATE_PULL_REFRESH		= 0;
	// �ɿ�ˢ��
	public static final int		STATE_RELEASE_REFRESH	= 1;
	// ����ˢ��
	public static final int		STATE_REFRESHING		= 2;
	// ��¼��ǰ��״̬
	public int					mCurState				= STATE_PULL_REFRESH;

	private RotateAnimation		mUp2Down;

	private RotateAnimation		mDown2Up;

	private int					mRefreshHeaderViewHeight;

	private boolean				isStartScroll			= true;

	private OnItemClickListener	mListener;

	public RefreshListView(Context context) {
		this(context, null);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView(context);
		initFooterView(context);
		// ��ʼ������
		initAnimation();

	}

	/**��ʼ��β����*/
	private void initFooterView(Context context) {
		mFooterView = View.inflate(context, R.layout.inflate_refreshlistview_footerview, null);
		// �Һ���

		mFooterViewPb = (ProgressBar) mFooterView.findViewById(R.id.refresh_footerview_pb);
		mFooterViewState = (TextView) mFooterView.findViewById(R.id.refresh_footerview_state);
		// �ӵ�listview��
		addFooterView(mFooterView);
		// ����listview�Ĺ����¼�
		this.setOnScrollListener(this);
		// ���õ������
		mFooterView.setOnClickListener(this);
	}

	/**��ʼ��ͷ����*/
	public void initHeaderView(Context context) {
		mHeaderView = View.inflate(context, R.layout.inflate_refreshlistview_headerview, null);
		// �ҳ�����ˢ��ͷ�����������еĺ���
		mHeaderViewArrow = (ImageView) mHeaderView.findViewById(R.id.refresh_headerview_arrow);
		mHeaderViewPb = (ProgressBar) mHeaderView.findViewById(R.id.refresh_headerview_pb);
		mHeaderViewState = (TextView) mHeaderView.findViewById(R.id.refresh_headerview_state);
		mHeaderViewUpdateTime = (TextView) mHeaderView.findViewById(R.id.refresh_headerview_updatetime);
		mHeaderViewCustomContainer = (FrameLayout) mHeaderView.findViewById(R.id.refresh_headerview_customheaderview);

		mRefreshHeaderView = (RelativeLayout) mHeaderView.findViewById(R.id.refresh_headerView);
		/**
		 UNSPECIFIED    wrap_content
		 EXACTLY        match_parent 100dp 100px
		 AT_MOST        ����
		 */
		/*
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);//��ϵͳ�������׿���Ƕ���
		int heightMeasureSpec= MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);//��ϵͳ�������׸߶��Ƕ���
		mRefreshHeaderView.measure(widthMeasureSpec, heightMeasureSpec);
		*/

		// �õ�����ˢ��ͷ����Ӧ�еĸ߶�
		mRefreshHeaderView.measure(0, 0);// ����һ��
		mRefreshHeaderViewHeight = mRefreshHeaderView.getMeasuredHeight();

		mInitRefreshHeaderViewPaddingTop = -mRefreshHeaderViewHeight;

		// ͨ������paddingTop��������ˢ��ͷ����
		mHeaderView.setPadding(0, mInitRefreshHeaderViewPaddingTop, 0, 0);
		addHeaderView(mHeaderView);
	}

	/**
	 * ��ʼ������
	 */
	private void initAnimation() {
		mUp2Down =
				new RotateAnimation(180, 360, RotateAnimation.RELATIVE_TO_SELF, .5f, RotateAnimation.RELATIVE_TO_SELF,
						.5f);
		mUp2Down.setDuration(400);
		mUp2Down.setFillAfter(true);// ����ֹ֮ͣ��͹̶�����Ч��

		mDown2Up =
				new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, .5f, RotateAnimation.RELATIVE_TO_SELF,
						.5f);
		mDown2Up.setDuration(400);
		mDown2Up.setFillAfter(true);// ����ֹ֮ͣ��͹̶�����Ч��
	}

	/**
	 * ����Զ����ͷ
	 */
	public void addCustomHeader(View customHeaderView) {
		mHeaderViewCustomContainer.addView(customHeaderView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "MotionEvent.ACTION_DOWN");

			mDownX = ev.getRawX();
			mDownY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, "MotionEvent.ACTION_MOVE");
			if (mDownX == 0 && mDownY == 0) {// �����¼�û�н���,��סViewPager�϶��������
				mDownX = ev.getRawX();
				mDownY = ev.getRawY();
			}

			/*--------------- 1. ����ˢ��,Listview�����������¼�,ListViewʵ��Ӧ��Ч�� ---------------*/
			if (mCurState == STATE_REFRESHING) {
				Log.i(TAG, "����ˢ��,���ܼ����϶� ");
				return super.onTouchEvent(ev);
			}

			float moveX = ev.getRawX();
			float moveY = ev.getRawY();

			int diffX = (int) (moveX - mDownX + .5f);
			int diffY = (int) (moveY - mDownY + .5f);// �϶��ľ���

			/**
			 	����ˢ�²������½�.Y>=ListView���Ͻ�.Y������ �������� 
			 */

			// ����ˢ�²������Ͻ�
			int[] refreshHeaderViewLocation = new int[2];
			mRefreshHeaderView.getLocationInWindow(refreshHeaderViewLocation);
			// ���Ͻ�
			int refreshHeaderViewTopY = refreshHeaderViewLocation[1];
			int refreshHeaderViewBottomY = refreshHeaderViewTopY + mRefreshHeaderViewHeight;

			int[] listViewLocation = new int[2];
			this.getLocationInWindow(listViewLocation);
			int listViewY = listViewLocation[1];

			if (refreshHeaderViewBottomY >= listViewY && diffY > 0) {// �޸�paddingTop������Ч��

				if (isStartScroll) {
					// ��������������ʱ��,�ſ�ʼ�������ǵ�diffY,�Ͳ������һ��������Ծ����
					// ���ȸ�ֵ
					mDownY = moveY;
					diffY = (int) (moveY - mDownY + .5f);// �϶��ľ���

					isStartScroll = false;
				}

				int paddingTop = mInitRefreshHeaderViewPaddingTop + diffY;

				// ����paddingTop������ֵ�л�״̬(����ˢ��,�ɿ�ˢ��)
				if (paddingTop > 0 && mCurState != STATE_RELEASE_REFRESH) {
					mCurState = STATE_RELEASE_REFRESH;// �ɿ�ˢ��
					Log.i(TAG, "�ɿ�ˢ��");
					// ˢ��ͷ����ui
					refreshHeaderViewUI();
				} else if (paddingTop <= 0 && mCurState != STATE_PULL_REFRESH) {
					mCurState = STATE_PULL_REFRESH;// ����ˢ��
					Log.i(TAG, "����ˢ��");
					// ˢ��ͷ����ui
					refreshHeaderViewUI();
				}

				// ��ʽ
				// paddingTop = mInitRefreshHeaderViewPaddingTop+diffY
				// ��������ˢ��ͷ����ʵʱ��ʾ����
				mHeaderView.setPadding(0, paddingTop, 0, 0);

				// break;
				// add
				super.setOnItemClickListener(null);// �ÿ��¼�
				return true;
			} else {// listview�����¼�,��������Ч��
				return super.onTouchEvent(ev);
			}

		case MotionEvent.ACTION_UP:
			Log.i(TAG, "MotionEvent.ACTION_UP");
			// ���mDonwX mDownY
			mDownX = 0;
			mDownY = 0;
			isStartScroll = true;

			if (mCurState == STATE_PULL_REFRESH) {// ����������ˢ��-->����ˢ��
				mCurState = STATE_PULL_REFRESH;

				// ˢ��ͷ���ֵ�ui
				refreshHeaderViewUI();

				// �޸�����ˢ��ͷ���ֵ���ʾ����
				// TODO
				// mHeaderView.setPadding(0, mInitRefreshHeaderViewPaddingTop, 0, 0);

				changePaddingTopAnimation(mHeaderView.getPaddingTop(), mInitRefreshHeaderViewPaddingTop);

			} else if (mCurState == STATE_RELEASE_REFRESH) {// ����������ˢ��-->����ˢ��
				mCurState = STATE_REFRESHING;

				// ˢ��ͷ���ֵ�ui
				refreshHeaderViewUI();

				// �޸�����ˢ��ͷ���ֵ���ʾ����
				// TODO
				// mHeaderView.setPadding(0, 0, 0, 0);

				changePaddingTopAnimation(mHeaderView.getPaddingTop(), 0);

				// ͨ���ӿڶ�����д�ֵ����
				if (mOnRefreshListener != null) {
					// 3.����Ҫ��ֵ�ĵط�,ʹ�ýӿڶ���,���ýӿڷ���
					mOnRefreshListener.onRefresh(this);
				}
			}

			break;

		default:
			break;
		}
		// ��ԭ�¼�
		super.setOnItemClickListener(mListener);
		return super.onTouchEvent(ev);// ������������listView�Ĺ���Ч��
	}

	/**
	 * ˢ��ͷ���ֵ�ui(3��״̬)
	 */
	private void refreshHeaderViewUI() {
		switch (mCurState) {
		case STATE_PULL_REFRESH:// ����ˢ��
			// ��ͷ
			mHeaderViewArrow.setVisibility(View.VISIBLE);

			// ��ͷ �� �� �� ��
			mHeaderViewArrow.startAnimation(mUp2Down);

			// ���ؽ�����
			mHeaderViewPb.setVisibility(View.INVISIBLE);
			// ������״̬
			mHeaderViewState.setText("����ˢ��");
			// ˢ��ʱ��

			break;
		case STATE_RELEASE_REFRESH:// �ɿ�ˢ��
			// ��ͷ
			mHeaderViewArrow.setVisibility(View.VISIBLE);

			// ��ͷ �� �� �� ��
			mHeaderViewArrow.startAnimation(mDown2Up);

			// ���ؽ�����
			mHeaderViewPb.setVisibility(View.INVISIBLE);
			// ������״̬
			mHeaderViewState.setText("�ɿ�ˢ��");

			// ˢ��ʱ��
			break;
		case STATE_REFRESHING:// ����ˢ��
			// �����ͷ֮ǰ�Ķ���
			mHeaderViewArrow.clearAnimation();

			// ��ͷ
			mHeaderViewArrow.setVisibility(View.INVISIBLE);
			// ���ؽ�����
			mHeaderViewPb.setVisibility(View.VISIBLE);
			// ������״̬
			mHeaderViewState.setText("����ˢ��");

			// ˢ��ʱ��
			mHeaderViewUpdateTime.setText(getUpdateTimeStr());

			break;

		default:
			break;
		}
	}

	public String getUpdateTimeStr() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * ͨ�������ķ�ʽ�޸�mHeaderView��paddingTop,����ֱ��ʹ��ObjectAnimator,��Ϊû�ж�Ӧ��PaddingTop������
	 * @param start �����Ŀ�ʼֵ
	 * @param end  �����Ľ���ֵ
	 */
	public void changePaddingTopAnimation(int start, int end) {
		// ͨ��ValueAnimator���ɽ���ֵ
		ValueAnimator animator = ValueAnimator.ofInt(start, end);
		animator.setDuration(400);
		animator.start();
		// ͨ������,�õ�����ֵ
		animator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// �õ�����ִ�й����еĽ���ֵ
				int paddintTop = (Integer) valueAnimator.getAnimatedValue();
				mHeaderView.setPadding(0, paddintTop, 0, 0);
			}
		});

	}

	@Override
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		mListener = listener;
		super.setOnItemClickListener(listener);
	}

	/**1.��ʼ����ˢ�µ�Ч��*/
	public void startRefresh() {
		mCurState = STATE_REFRESHING;

		refreshHeaderViewUI();
		// �޸�paddingTop
		changePaddingTopAnimation(mInitRefreshHeaderViewPaddingTop, 0);
	}

	/**2.��������ˢ�µ�Ч��*/
	public void stopRefresh() {
		mCurState = STATE_PULL_REFRESH;
		refreshHeaderViewUI();
		changePaddingTopAnimation(0, mInitRefreshHeaderViewPaddingTop);
	}

	/*--------------- 3.��������ˢ��---------------*/
	/**
	 �ӿڻص�
	 	1.����ӿ��Լ��ӿڷ���
	 	2.����ӿڶ���
	 	3.����Ҫ��ֵ�ĵط�,ʹ�ýӿڶ���,���ýӿڷ���
	 	4.��¶��������
	 */
	// 1.����ӿ��Լ��ӿڷ���
	public interface OnRefreshListener {
		void onRefresh(RefreshListView refreshListView);

		void onLoadMore(RefreshListView refreshListView);
	}

	// 2.����ӿڶ���
	OnRefreshListener	mOnRefreshListener;

	private View		mFooterView;

	private ProgressBar	mFooterViewPb;

	private TextView	mFooterViewState;

	private boolean		isLoadMoreSuccess;

	private boolean		isLoadingMore;

	// 4.��¶��������
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if (isLoadingMore) {
			return;
		}

		// �����׵�ʱ��
		if (getLastVisiblePosition() == getAdapter().getCount() - 1) {
			if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {

				// ����֪ͨ,�����Ѿ������ײ���
				if (mOnRefreshListener != null) {
					mOnRefreshListener.onLoadMore(this);
					isLoadingMore = true;
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO

	}

	/**
	 * �����ϻ����ظ���
	 * @param isLoadMoreSuccess
	 */
	public void stopLoadMore(boolean isLoadMoreSuccess) {
		this.isLoadMoreSuccess = isLoadMoreSuccess;
		isLoadingMore = false;
		if (isLoadMoreSuccess) {
			// ������
			mFooterViewPb.setVisibility(View.VISIBLE);
			// ���ظ����״̬
			mFooterViewState.setText("���ڼ��ظ���");
		} else {// ʧ��
			// ������
			mFooterViewPb.setVisibility(View.INVISIBLE);
			// ���ظ����״̬
			mFooterViewState.setText("�������");
		}
	}

	/**
	 * �����Ƿ��м��ظ���
	 * @param b
	 */
	public void setHasLoadMore(boolean isLoadMore) {
		if (isLoadMore) {
			// ������
			mFooterViewPb.setVisibility(View.VISIBLE);
			// ���ظ����״̬
			mFooterViewState.setText("���ڼ��ظ���");
		} else {
			// ������
			mFooterViewPb.setVisibility(View.INVISIBLE);
			// ���ظ����״̬
			mFooterViewState.setText("û�м��ظ���");
		}

	}

	@Override
	public void onClick(View v) {
		if (!isLoadMoreSuccess) {
			// ����
			if (mOnRefreshListener != null) {
				mOnRefreshListener.onLoadMore(this);
				// �޸�ui
				// ������
				mFooterViewPb.setVisibility(View.VISIBLE);
				// ���ظ����״̬
				mFooterViewState.setText("���ڼ��ظ���");
			}
		}
	}
}

