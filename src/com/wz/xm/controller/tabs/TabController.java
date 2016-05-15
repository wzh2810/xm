package com.wz.xm.controller.tabs;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wz.xm.MainActivity;
import com.wz.xm.R;

/**
 * @描述 1.提供视图
 * @描述 2.接收数据/加载数据
 * @描述 3.数据和视图的绑定
 * 
 */
public abstract class TabController {

	public View mRootView; // -->textview
	public Context mContext; // 保存一个上下文

	FrameLayout mContentContainer;

	ImageButton mIbMenu;

	TextView mTvTitle;

	public TabController(Context context) {
		mContext = context;
		mRootView = initView(context);
		
	}

	/**
	 * @des 初始化controller里面持有的视图
	 * @des 完成一些共有的布局
	 * @call TabController创建的时候
	 * @param context
	 * @return
	 */
	public View initView(Context context) {
		View view = View.inflate(mContext, R.layout.layout_base_tab, null);
		/**
		 * 重点,在基类里面不能使用Xutils中的ViewUtils去使用ioc的方式完成对象的时候
		 */
		// ViewUtils.inject(this, view);
		mContentContainer = (FrameLayout) view.findViewById(R.id.base_tab_fl_content);
		mIbMenu = (ImageButton) view.findViewById(R.id.base_tab_ib_menu);
		mTvTitle = (TextView) view.findViewById(R.id.base_tab_tv_title);

		// 往容器加入具体的内容
		mContentContainer.addView(initContentView(context));

		initStateBar();

		// 给mIbMenu设置点击事件
		mIbMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 拿到MainActivity的实例,就可以操作MainActivity里面的slidingMenu对象
				// Toggle the SlidingMenu. If it is open, it will be closed, and
				// vice versa.
				((MainActivity) mContext).getSlidingMenu().toggle();
			}
		});
		return view;
	};

	/**
	 * @des 处理状态栏
	 * @des 必须实现,但是不知道具体实现,定义成为抽象方法,交给子类具体实现
	 */
	public abstract void initStateBar();

	/**
	 * @param context
	 * @des 初始化基类里面mContentContainer里面应有的具体视图
	 * @des 必须实现,但是不知道具体实现,定义成为抽象方法,交给子类具体实现
	 * @return
	 */
	public abstract View initContentView(Context context);

	/**
	 * @des 1.加载数据
	 * @des 2.数据和视图进行绑定
	 * @des 选择性实现,子类如果有数据加载过程,就覆写该方法即可
	 * @call 如果需要加载数据,然后根据回来的时候进行数据刷新的时候,就调用该方法
	 */
	public void initData() {

	}

	/**
	 * 切换到MenuController集合中的第几个视图
	 * 
	 * @param position
	 */
	public void switchContent(int position) {

	}

}