package com.wz.xm.controller.tabs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wz.xm.R;
import com.wz.xm.utils.network.imageloader.ImageLoader;

public class NetworkServiceTabController extends TabController {
	@ViewInject(R.id.network_pics_gridview)
	GridView mGridView;

	@ViewInject(R.id.network_pics_listview)
	ListView mListView;

	private BitmapUtils mBitmapUtils;

	private boolean isListView = true;

	ImageLoader mImageLoader;

	private List<String> mListPics = new ArrayList<String>();;

	private int mPicNumber = 20;

	public NetworkServiceTabController(Context context) {
		super(context);
		mBitmapUtils = new BitmapUtils(mContext);
		mImageLoader = new ImageLoader(mContext);
		initData();
	}

	/**
	 * 初始化controller里面持有的视图
	 * 
	 * @param context
	 * @return
	 */
	public View initContentView(Context context) {
		View view = View.inflate(mContext, R.layout.network_pics, null);
		ViewUtils.inject(this, view);

		return view;
	}

	/**
	 * 加载数据,然后数据和视图进行绑定
	 */
	public void initData() {
		getData();
	}

	private void getData() {

		HttpUtils httpUtils = new HttpUtils();
		String path = "http://android.yangyuanguang.com/android/wallpager/chosenimg/";
		for (int i = 1; i < mPicNumber; i++) {
			String url = path + i + ".jpg";
			mListPics.add(url);
			System.out.println("url====" + url);
			httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					if (responseInfo.statusCode == 200 || responseInfo.statusCode == 0) {
						String result = responseInfo.result;

						// System.out.println("http:===" + result);
						// System.out.println("======pics" + resulsut);
					} else {
						Toast.makeText(mContext, "数据加载失败1", 0).show();
					}

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					Toast.makeText(mContext, "数据加载失败2", 0).show();

				}
			});
		}
		picsData();
		System.out.println("pics" + mListPics.size());
		super.initData();
	}

	private void picsData() {
		// System.out.println("-====mlist" + mlist.size());
		mGridView.setAdapter(new PicsAdapter());
	}

	class PicsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mListPics != null) {
				return mListPics.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (mListPics != null) {
				mListPics.get(position);
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
				convertView = View.inflate(mContext, R.layout.network_item_pics, null);
				holder = new ViewHolder();
				holder.mIvIcon = (ImageView) convertView.findViewById(R.id.network_item_pics_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			String path = mListPics.get(position);
			mImageLoader.display(holder.mIvIcon, path);
			return convertView;
		}

		class ViewHolder {
			ImageView mIvIcon;
		}
	}

	@Override
	public void initStateBar() {
		mTvTitle.setText("网络服务");

	}
}
