package com.wz.xm.fragment;

import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wz.xm.MainActivity;
import com.wz.xm.R;
import com.wz.xm.base.BaseFragment;
import com.wz.xm.bean.FileBean;

public class LeftMenuFragment extends BaseFragment implements OnItemClickListener {
	@ViewInject(R.id.file_menu_left_listview)
	ListView mListView;

	private List<FileBean> mFileCenterMenuBeans;

	private int mCurSelectIndex = 0;

	private MenuAdapter mAdapter;

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.fragment_menu_left, null);
		ViewUtils.inject(this, view);
		System.out.println("initView:=====" + mListView);
		// listView相关常用的设置
		mListView.setDividerHeight(0);
		int paddingTop = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, mContext.getResources()
				.getDisplayMetrics()) + .5f);
		mListView.setPadding(0, paddingTop, 0, 0);
		// 取消selector
		mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));

		// 针对2.3的手机做一些兼容性的设置
		mListView.setCacheColorHint(Color.TRANSPARENT);

		mListView.setFadingEdgeLength(0);
		mAdapter = new MenuAdapter();
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(this);

		return view;
	}

	public void setData(List<FileBean> fileCenterMenuBeas) {
	
		mFileCenterMenuBeans = fileCenterMenuBeas;
	//	System.out.println("----------" + mFileCenterMenuBeans.toString());
		// 刷新listview-->设置adapter
	//	System.out.println("setData:=====" + mListView);
		mAdapter = new MenuAdapter();
		// mListView.setAdapter(new MenuAdapter());

	}

	class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mFileCenterMenuBeans != null) {
				return mFileCenterMenuBeans.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (mFileCenterMenuBeans != null) {
				return mFileCenterMenuBeans.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.file_menu_left, null);
				holder.tvMenu = (TextView) convertView.findViewById(R.id.file_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			FileBean fileBean = mFileCenterMenuBeans.get(position);
			holder.tvMenu.setText(fileBean.fileName);

			if (mCurSelectIndex == position) {
				holder.tvMenu.setEnabled(true);
			} else {
				holder.tvMenu.setEnabled(false);
			}
			return convertView;
		}

		class ViewHolder {
			TextView tvMenu;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mCurSelectIndex = position;
		// 刷新listview
		mAdapter.notifyDataSetChanged();
		// 切换-->显示哪个TabController下的第几个具体的MenuController
		// 拿到contentFragment
		ContentFragment contentFragment = ((MainActivity) mContext).getContentFragment();

		contentFragment.switchContent(position);

		// 进行slidingMenu的打开和关闭
		((MainActivity) mContext).getSlidingMenu().toggle();

	}

}
