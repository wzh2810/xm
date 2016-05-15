package com.wz.xm.controller.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


import com.wz.xm.R;
import com.wz.xm.adapter.MusicAdapter;
import com.wz.xm.bean.Music;
import com.wz.xm.utils.CharacterParser;
import com.wz.xm.utils.ClearEditText;
import com.wz.xm.utils.MediaUtils;
import com.wz.xm.utils.MusicComparator;
import com.wz.xm.views.SideBar;
import com.wz.xm.views.SideBar.OnTouchingLetterChangedListener;

public class MusicMenuController extends MenuController {

	@ViewInject(R.id.file_music_list_view)
	ListView mMusicListView;

	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private ClearEditText mClearEditText;
	private MusicAdapter mMusicAdapter;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<Music> musicDataList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private MusicComparator musicComparator;

	public MusicMenuController(Context context) {
		super(context);

	}

	List<Music> mMusicList = null;

	public MusicMenuController(Context context, List<Music> musicList) {
		super(context);
		musicList = musicList;
	}

	@Override
	public View initView(Context context) {
		View view = View.inflate(mContext, R.layout.file_music_list, null);
		ViewUtils.inject(this, view);
		
		MediaUtils.initSongList(mContext);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		musicComparator = new MusicComparator();

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		sortListView = (ListView) view.findViewById(R.id.file_music_list_view);
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = mMusicAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(mContext, ((Music) mMusicAdapter.getItem(position)).getTitle(),
						Toast.LENGTH_SHORT).show();
			}
		});
		musicDataList = musicFilledData(MediaUtils.songList);

		System.out.println("musicDataList" + musicDataList.size());
		// 根据a-z进行排序源数据
		 Collections.sort(musicDataList,musicComparator);

		mMusicAdapter = new MusicAdapter(mContext, musicDataList);
		sortListView.setAdapter(mMusicAdapter);

		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				musicFilterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		
		return view;
	}


	private List<Music> musicFilledData(List<Music> data) {
		List<Music> mMusicList = new ArrayList<Music>();
		for (int i = 0; i < data.size(); i++) {
			Music music = new Music();
			music.setTitle(data.get(i).title);
			music.setArtist(data.get(i).artist);
			music.setPath(data.get(i).path);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(data.get(i).title);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				music.setSortLetters(sortString.toUpperCase());
			} else {
				music.setSortLetters("#");
			}
			mMusicList.add(music);
		}
		return mMusicList;
	}

	private void musicFilterData(String filterStr) {
		List<Music> filterDateList = new ArrayList<Music>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = musicDataList;
		} else {
			filterDateList.clear();
			for (Music sortModel : musicDataList) {
				String name = sortModel.getTitle();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		// Collections.sort(filterDateList, musicComparator);
		mMusicAdapter.updateListView(filterDateList);
	}
		
	

	@Override
	public void initData() {
		
		super.initData();
	}
}
