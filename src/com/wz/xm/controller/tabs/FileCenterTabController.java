package com.wz.xm.controller.tabs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.wz.xm.MainActivity;
import com.wz.xm.bean.FileBean;
import com.wz.xm.bean.Music;
import com.wz.xm.controller.menu.MenuController;
import com.wz.xm.controller.menu.MusicMenuController;
import com.wz.xm.controller.menu.PicsMenuController;
import com.wz.xm.fragment.LeftMenuFragment;
import com.wz.xm.utils.MediaUtils;

public class FileCenterTabController extends TabController {

	private FrameLayout mFileCenterContentContainer;

	private List<MenuController> mMenuControllers;
	
	private List<Music> mMusicList = new ArrayList<Music>();
	
	List<FileBean> fileCenterMenuBean = new ArrayList<FileBean>();

	

	public FileCenterTabController(Context context) {
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

		mFileCenterContentContainer = new FrameLayout(mContext);
		return mFileCenterContentContainer;
	}

	/**
	 * 加载数据,然后数据和视图进行绑定
	 */
	public void initData() {
		getData();
	}

	@Override
	public void initStateBar() {
		mTvTitle.setText("文件中心");
		// mIbMenu.setVisibility(8);

	}

	public void getData() {
		
		LeftMenuFragment leftMenuFragment = ((MainActivity) mContext).getLeftMenuFragment();
//		List<FileBean> fileCenterMenuBean = new ArrayList<FileBean>();
		
		fileCenterMenuBean.add(new FileBean(0,"音乐查询"));
		fileCenterMenuBean.add(new FileBean(1,"图片浏览"));
		
		mMusicList = MediaUtils.songList;
		
		System.out.println("mMusicList" + mMusicList.size());
		
	//	System.out.println("=========" + fileCenterMenuBean.toString());
		
		
		leftMenuFragment.setData(fileCenterMenuBean);
		
		
	//	System.out.println("FileCenter===" + leftMenuFragment.toString());
		/*--------------- 所有的MenuController通过集合进行管理 ---------------*/
		mMenuControllers = new ArrayList<MenuController>();
		for (FileBean fileBean : fileCenterMenuBean) {
			MenuController menuController = null;
			int type = fileBean.number;
			switch (type) {
			case 1:
				menuController = new PicsMenuController(mContext);
				break;
			case 0:
				menuController = new MusicMenuController(mContext,mMusicList);
				break;

			default:
				break;
			}
			// 加入集合
			mMenuControllers.add(menuController);

			// 加入容器
			mFileCenterContentContainer.addView(menuController.mRootView);
		}
		// 初始化显示LeftMenuFragment中的第一个条目对应的视图信息
		switchContent(0);

	}

	/**
	 * 切换到MenuController集合中的第几个视图
	 * 
	 * @param position
	 */
	@Override
	public void switchContent(int position) {
		// 移除容器里面的所有内容
		mFileCenterContentContainer.removeAllViews();

		MenuController menuController = mMenuControllers.get(position);
		View rootView = menuController.mRootView;
		mFileCenterContentContainer.addView(rootView);

		// 让MenuController加载对应的数据,然后刷新ui
		menuController.initData();

		super.switchContent(position);
	}
}
