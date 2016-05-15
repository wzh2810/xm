package com.wz.xm.controller.tabs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.wz.xm.MainActivity;
import com.wz.xm.bean.NetBean;
import com.wz.xm.controller.menu.MenuController;
import com.wz.xm.fragment.LeftMenuFragment;


public class NetworkServiceTabController extends TabController {
	
	private FrameLayout mFileNetworkServiceContainer;
	
	private List<MenuController> mMenuControllers;
	
	List<NetBean> netMenuBeans = new ArrayList<NetBean>();

	public NetworkServiceTabController(Context context) {
		super(context);
		initData();
	}

	/**
	 * 初始化controller里面持有的视图
	 * @param context
	 * @return
	 */
	public View initContentView(Context context) {
		mFileNetworkServiceContainer = new FrameLayout(mContext);
		return mFileNetworkServiceContainer;
	}

	/**
	 * 加载数据,然后数据和视图进行绑定
	 */
	public void initData() {
		getData();
	}

	private void getData() {
		
	}

	@Override
	public void initStateBar() {
		mTvTitle.setText("网络服务");
		
		
	}
}
