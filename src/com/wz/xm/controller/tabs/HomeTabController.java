package com.wz.xm.controller.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wz.xm.R;

public class HomeTabController extends TabController {

	@ViewInject(R.id.home_viewpager)
	private ViewPager mPager;

	@ViewInject(R.id.point_container)
	private LinearLayout mPointContainer;

	@ViewInject(R.id.home_page_title)
	private TextView mHomePageTitle;

	@ViewInject(R.id.home_list_iv)
	// item
	private ImageView home_list_iv;

	@ViewInject(R.id.home_list_lv1)
	private ListView home_list_lv1;

	@ViewInject(R.id.home_list_lv2)
	private ListView home_list_lv2;

	@ViewInject(R.id.home_list_lv3)
	private ListView home_list_lv3;

	private List<ImageView> mListDatas;

	private Timer mTimer;

	private int[] pageImgs = { R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5 };
	private String[] titles = { "美女", "海景", "荷花", "美女", "最美傍晚" };
	private int[] listImgs = { R.drawable.list1, R.drawable.list2, R.drawable.list3, R.drawable.list4, R.drawable.list5 };

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			int currentItem = mPager.getCurrentItem();
			if (currentItem == mPager.getAdapter().getCount() - 1) {
				currentItem = 0;
			} else {
				currentItem++;
			} // 设置最新的条目索引
			mPager.setCurrentItem(currentItem);

		};
	};

	public HomeTabController(Context context) {
		super(context);
		initData();
	}

	/**
	 * 初始化controller里面持有的视图
	 * 
	 * @param context
	 * @return
	 */
	public View initContentView(Context context) {
		View view = View.inflate(mContext, R.layout.home_page, null);

		ViewUtils.inject(this, view);
		return view;
	}

	/**
	 * 加载数据,然后数据和视图进行绑定
	 */
	public void initData() {
		viewPageData();
		listViewData();
		startScroll();
	}

	public void startScroll() {
		if (mTimer == null) {
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					mHandler.sendEmptyMessage(0);

				}
			}, 3000, 3000);
		}
	}

	public void listViewData() {
		home_list_lv1.setAdapter(new HomeListAdapter());
		home_list_lv2.setAdapter(new HomeListAdapter());
		home_list_lv3.setAdapter(new HomeListAdapter());
	}

	public void viewPageData() {
		mListDatas = new ArrayList<ImageView>();
		for (int i = 0; i < pageImgs.length; i++) {
			// 给集合添加ImageView
			ImageView iv = new ImageView(mContext);
			iv.setImageResource(pageImgs[i]);
			iv.setScaleType(ScaleType.FIT_XY);

			mListDatas.add(iv);

			// 添加点
			View point = new View(mContext);
			point.setBackgroundResource(R.drawable.point_normal);
			LayoutParams params = new LayoutParams(10,10);
			
			if (i != 0) {
				params.leftMargin = 10;
				
			} else {
				point.setBackgroundResource(R.drawable.point_selected);

				mHomePageTitle.setText(titles[i]);
			}
			point.setLayoutParams(params);
			mPointContainer.addView(point, params);

		}
		mPager.setAdapter(new HomeAdapter());
		int middle = Integer.MAX_VALUE / 2;
		int extra = middle % mListDatas.size();
		int item = middle - extra;
		mPager.setCurrentItem(item);

		mPager.setOnPageChangeListener(new HomePageChangeListener());
	}

	class HomeListAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return 3000;
		}

		@Override
		public Object getItem(int position) {

			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv = (ImageView) View.inflate(mContext, R.layout.home_list_item, null);
			int resId = (int) (Math.random() * 5);
			iv.setImageResource(listImgs[resId]);
			return iv;
		}

	}

	class HomePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {

		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			position = position % mListDatas.size();

			// 设置选中的点的样式
			int count = mPointContainer.getChildCount();
			for (int i = 0; i < count; i++) {
				View view = mPointContainer.getChildAt(i);

				view.setBackgroundResource(position == i ? R.drawable.point_selected : R.drawable.point_normal);
			}

			mHomePageTitle.setText(titles[position]);

		}

	}

	class HomeAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mListDatas != null) {

				return Integer.MAX_VALUE;
			}

			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % mListDatas.size();

			// position： 要加载的位置
			ImageView iv = mListDatas.get(position);

			// 用来添加要显示的View的
			mPager.addView(iv);

			// 记录缓存标记--return 标记
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// 销毁移除item
			// object:标记

			position = position % mListDatas.size();

			ImageView iv = mListDatas.get(position);
			mPager.removeView(iv);
		}

	}

	@Override
	public void initStateBar() {
		mTvTitle.setText("首页");
		mIbMenu.setVisibility(8);

	}
}
