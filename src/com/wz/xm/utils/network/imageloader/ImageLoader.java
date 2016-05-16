package com.wz.xm.utils.network.imageloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.wz.xm.R;
import com.wz.xm.utils.network.imageloader.DiskLruCache.Editor;
import com.wz.xm.utils.network.imageloader.DiskLruCache.Snapshot;

public class ImageLoader {
	public static final String TAG = ImageLoader.class.getSimpleName();

	private Context mContext;

	private LruCache<String, Bitmap> mLruCache;

	// 1.分配内存缓存的最大值
	private int mMemMaxSize = 4 * 1024 * 1024; // byte
	/*
	 * private int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 4); //
	 * byte private int totalMemory = (int) (Runtime.getRuntime().totalMemory()
	 * / 4); // byte
	 */
	private DiskLruCache mDiskLruCache;

	// 分配磁盘缓存的最大值
	private static long mDiskMaxSize = 20 * 1024 * 1024;
	// 主线程的一个handler
	private Handler mHandler;
	private static ExecutorService mExecutorService;
	static {
		// the newly created thread pool
		mExecutorService = Executors.newCachedThreadPool();
	}

	public ImageLoader(Context context) {
		mHandler = new Handler();
		mContext = context;

		mLruCache = new LruCache<String, Bitmap>(mMemMaxSize) {
			// 2.覆写sizeof方法,返回对应entry的大小,需要和maxSize定义的单位统一
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {

				return bitmap.getByteCount();

			}
		};
		/*
		 * Log.i(TAG, "mMemMaxSize:" + mMemMaxSize); Log.i(TAG, "maxMemory:" +
		 * maxMemory); Log.i(TAG, "totalMemory:" + totalMemory);
		 */

		try {
			mDiskLruCache = DiskLruCache.open(getCacheDirectory(), getAppVersionCode(), 1, mDiskMaxSize);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** 加载图片的方法 */
	public void display(final ImageView iv, final String url) {

		// 图片置空
		iv.setImageResource(R.drawable.network_pic_default);

		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * // 模拟图片加载比较耗时 Random random = new Random(); SystemClock.sleep(1000 +
		 * random.nextInt(4000)); Log.i(TAG, "size:" + mLruCache.size());
		 * 
		 * mHandler.post(new Runnable() {
		 * 
		 * @Override public void run() {
		 */

		Bitmap bitmap = null;
		String key = MD5Util.md5(url);

		// 1.内存-->展示
		bitmap = mLruCache.get(key);
		if (bitmap != null) {
			Log.i(TAG, "内存加载图片");
			// 展示
			iv.setImageBitmap(bitmap);
			return;
		}

		// 2.磁盘-->存内存,展示
		bitmap = loadBitmapFromLocal(key);
		if (bitmap != null) {
			Log.i(TAG, "磁盘加载图片");
			// 存内存
			mLruCache.put(key, bitmap);
			if (bitmap != null) {
				Log.i(TAG, key);
			}

			// 展示
			iv.setImageBitmap(bitmap);
			return;
		}
		// 3.网络-->存内存,存本地,展示
		loadBitmapFromNet(iv, url);

		/*
		 * } }); } }).start();
		 */

	}

	/**
	 * 得到应用程序的版本号
	 * 
	 * @return
	 */
	private int getAppVersionCode() {
		try {
			PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 得到磁盘缓存对应的缓存目录 最好放到外置sdcard
	 * 
	 * @return
	 */
	private File getCacheDirectory() {
		File dir = null;

		String externalStorageState = Environment.getExternalStorageState();
		if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {// 有外置sdcard
			File externalStorageDirectory = Environment.getExternalStorageDirectory();
			dir = new File(externalStorageDirectory + "/Android/data/" + mContext.getPackageName() + "/icon");

			if (!dir.exists()) {
				dir.mkdirs();
			}
			return dir;
		} else {// 没有外置sdcard
			dir = mContext.getCacheDir();
			return dir;
		}
	}

	/**
	 * 从网络加载图片
	 * 
	 * @param iv
	 * @param url
	 */
	private void loadBitmapFromNet(ImageView iv, String url) {

		// 为我们的iv,绑定一个tag
		iv.setTag(url);

		// new Thread(new LoadImageTask(iv, url)).start();
		mExecutorService.execute(new LoadImageTask(iv, url));
	}

	class LoadImageTask implements Runnable {
		private ImageView iv;
		private String url;

		public LoadImageTask(ImageView iv, String url) {
			this.iv = iv;
			this.url = url;
		}

		@Override
		public void run() {
			try {
				// SystemClock.sleep(2000);

				// 真正发起请求加载图片
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response = httpClient.execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();
					// inputstream-->bitmap
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

					// 存内存
					String key = MD5Util.md5(url);
					mLruCache.put(key, bitmap);

					if (bitmap != null) {
						Log.i(TAG, key);
					}
					// 展示
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							Log.i(TAG, "网络拉取图片成功");
							/*
							 * iv.setTag("url1"); iv.setTag("url2");
							 * iv.setTag("url3"); iv.setTag("url4");
							 * iv.setTag("url5");
							 */
							String curTagUrl = (String) iv.getTag();
							if (curTagUrl.equals(url)) {// url3
								display(iv, url);
								// iv.setImageBitmap(bitmap);
							}
						}
					});
					// 存本地
					write2Local(key, bitmap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从本地加载图片
	 * 
	 * @param key
	 * @return
	 */
	private Bitmap loadBitmapFromLocal(String key) {
		Bitmap bitmap = null;
		try {
			Snapshot snapshot = mDiskLruCache.get(key);
			InputStream inputStream = snapshot.getInputStream(0);

			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 保存bitmap到本地
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void write2Local(String key, Bitmap bitmap) {
		try {
			Editor editor = mDiskLruCache.edit(key);
			OutputStream outputStream = editor.newOutputStream(0);

			// bitmap-->outputStream
			bitmap.compress(CompressFormat.JPEG, 100, outputStream);

			// 提交
			editor.commit();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
