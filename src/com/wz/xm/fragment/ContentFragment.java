package com.wz.xm.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wz.xm.MainActivity;
import com.wz.xm.R;
import com.wz.xm.base.BaseFragment;
import com.wz.xm.controller.tabs.GovAffaireTabController;
import com.wz.xm.controller.tabs.HomeTabController;
import com.wz.xm.controller.tabs.FileCenterTabController;
import com.wz.xm.controller.tabs.SettingTabController;
import com.wz.xm.controller.tabs.SmartServiceTabController;
import com.wz.xm.controller.tabs.TabController;
import com.wz.xm.views.NoScrollViewPager;

public class ContentFragment extends BaseFragment {
	@ViewInject(R.id.content_rb_govaffair)
	RadioButton mRbGovaffair;

	@ViewInject(R.id.content_rb_home)
	RadioButton mRbHome;

	@ViewInject(R.id.content_rb_newscenter)
	RadioButton mRbNewscenter;

	@ViewInject(R.id.content_rb_setting)
	RadioButton mRbSetting;

	@ViewInject(R.id.content_rb_smartservice)
	RadioButton mRbSmartService;

	@ViewInject(R.id.content_viewpager)
	NoScrollViewPager mViewPager;

	@ViewInject(R.id.content_rg)
	RadioGroup mRg;

	// private String[] mToolbarTitleArrs;

	private int mCurTabIndex = 0; // 记录当前选中的饿tab的索引

	private List<TabController> mTabControllers = new ArrayList<TabController>();

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.fragment_content, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {

		// 默认让首页有选中的效果
		mRg.check(R.id.content_rb_home);// 主页的RatioButton被选中

		// 往ContentFragment对应的TabController集合中加入具体的TabController
		mTabControllers.add(new HomeTabController(mContext));
		mTabControllers.add(new FileCenterTabController(mContext));
		mTabControllers.add(new SmartServiceTabController(mContext));
		mTabControllers.add(new GovAffaireTabController(mContext));
		mTabControllers.add(new SettingTabController(mContext));

		// 通知设置viewpager的adapter,给viewpager添加具体的数据
		mViewPager.setAdapter(new ContentPagerAdapter());

		super.initData();
	}

	@Override
	public void initListener() {
		mRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 需要根据回调的checkedId,进行ViewPager的切换
				switch (checkedId) {
				case R.id.content_rb_home:// 首页
					mCurTabIndex = 0;
					break;
				case R.id.content_rb_newscenter://
					mCurTabIndex = 1;
					break;
				case R.id.content_rb_smartservice://
					mCurTabIndex = 2;
					break;
				case R.id.content_rb_govaffair://
					mCurTabIndex = 3;
					break;
				case R.id.content_rb_setting://
					mCurTabIndex = 4;
					break;

				default:
					break;
				}
				// 进行viewpager的切换
				mViewPager.setCurrentItem(mCurTabIndex);
			}
		});
		super.initListener();
	}

	class ContentPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mTabControllers != null) {
				return mTabControllers.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			/*
			 * // 实际情况-->ViewPager中的一个页面里面展示的内容是非常的复杂 //
			 * 假如说.我们不进行模块化,那么在CotentFragment里面会有很多很多的data+view // view-->提供视图
			 * TextView tv = new TextView(mContext);
			 * tv.setGravity(Gravity.CENTER); // data-->提供数据 String data =
			 * mToolbarTitleArrs[position]; // data+view-->数据和视图的绑定
			 * tv.setText(data);
			 * 
			 * // 加入容器 container.addView(tv);
			 */

			// 提供一个数据经过绑定的视图
			TabController tabController = mTabControllers.get(position);
			// 1.提供视图
			View rootView = tabController.mRootView;// 现在的情况其实就是一个静态的textView
			container.addView(rootView);

			// TabController具体如何加载数据,以及加载数据之后如何绑定数据,现在还没有处理,现在只是简单的展示了一个静态的文本

			return rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	/**
	 * @des 控制slidingMenu是否可以拖动出来
	 * @call 1.一进来的时候让首页不可以拖动
	 * @call 2.点击RadioButton的时候,决定点击的RadioButton是否可以拖出SlidingMenu
	 * 
	 */
	public void changeSlidingMenuTouchEnable(int rbId) {
		SlidingMenu slidingMenu = ((MainActivity) mContext).getSlidingMenu();
		if (rbId == R.id.content_rb_home || rbId == R.id.content_rb_setting) {
			// 控制slidingMenu不可以拖动出来
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		} else {
			// 控制slidingMenu可以拖动出来
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		}
	}

	/**
	 * @des 显示哪个TabController下MenuController集合中的第几个具体的MenuController
	 * @param position
	 *            应该显示 MenuController集合中 第position个MenuController
	 */
	public void switchContent(int position) {
		// 得到当前正在显示的TabController
		TabController tabController = mTabControllers.get(mCurTabIndex);
		// 显示TabController下MenuController集合中的第几个具体的MenuController
		tabController.switchContent(position);
	}
}
