package com.wz.xm.controller.tabs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class NetworkServiceTabController extends TabController implements OnScrollListener, OnTouchListener {
	@ViewInject(R.id.network_pics_gridview)
	GridView mGridView;

	@ViewInject(R.id.network_foot)
	RelativeLayout network_foot;

	// @ViewInject(R.id.network_pics_listview)
	// ListView mListView;

	private BitmapUtils mBitmapUtils;

	private boolean isListView = true;
	private View mHeaderView;
	boolean isLastRow = false;

	ImageLoader mImageLoader;

	private List<String> mListPics = new ArrayList<String>();;

	private int mPicNumber = 20;

	public NetworkServiceTabController(Context context) {
		super(context);
		mBitmapUtils = new BitmapUtils(mContext);
		mImageLoader = new ImageLoader(mContext);
		initData();
		footState(isLastRow);
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
		
		mGridView.setOnScrollListener(this);
		mGridView.setOnTouchListener(this);
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
		for (int i = 1; i <= mPicNumber; i++) {
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

	

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
		// firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
		// visibleItemCount：当前能看见的列表项个数（小半个也算）
		// totalItemCount：列表项共数

		System.out.println("----firstVisibleItem" + firstVisibleItem + "++" +  visibleItemCount + "++" +totalItemCount);
		// 判断是否滚到最后一行
		if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
			isLastRow = true;
		}
		footState(isLastRow);

	}

	// 监听滑动状态的变化
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// OnScrollListener.SCROLL_STATE_FLING; //屏幕处于甩动状态
		// OnScrollListener.SCROLL_STATE_IDLE; //停止滑动状态
		// OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;// 手指接触状态
		// 记录当前滑动状态
		//当滚到最后一行且停止滚动时，执行加载      
        if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {      
            //加载元素      
        	mPicNumber += 20;
        	initData();
      
            isLastRow = false;      
        }    

	}

	public void footState(boolean isLastRow) {
		if (isLastRow) {
			network_foot.setVisibility(View.VISIBLE);
		} else {
			network_foot.setVisibility(View.GONE);
		}
	}

	float downX;
	float downY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = event.getX();
			float moveY = event.getY();
			float dx = moveX - downX;
			float dy = moveY - downY;
			System.out.println("downX:" + downX);
			System.out.println("moveX:" + moveX);
			
			break;
		case MotionEvent.ACTION_UP:

			break;

		default:
			break;
		}
		return false;
	}

}
