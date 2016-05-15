package com.wz.xm.adapter;

import java.util.List;

import com.wz.xm.R;
import com.wz.xm.bean.Music;
import com.wz.xm.utils.MediaUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;




public class MusicAdapter extends BaseAdapter implements SectionIndexer{
	//1.数据源在什么地方
	private Context mContext;
	private List<Music> list = null;
	

	public MusicAdapter(Context context,List<Music> list) {
		this.mContext = context;
		this.list = list;
	}
	
	public void updateListView(List<Music> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(list != null) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/**---------------视图的初始化---------------**/
		ViewHolder holder = null;
		Music mContent= list.get(position);
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.file_item_music, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_artist = (TextView) convertView.findViewById(R.id.tv_artist);
			holder.tv_letter = (TextView) convertView.findViewById(R.id.catalog);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置，则认为是第一次出现
		if(position == getPositionForSection(section)) {
			holder.tv_letter.setVisibility(View.VISIBLE);
			holder.tv_letter.setText(mContent.getSortLetters());
		} else {
			holder.tv_letter.setVisibility(View.GONE);
		}
		
		holder.tv_artist.setText(this.list.get(position).getArtist());
		holder.tv_title.setText(this.list.get(position).getTitle());
//		
//		if(MediaUtils.CURPOSITION == position) {
//			holder.tv_title.setTextColor(Color.GREEN);
//		} else {
//			holder.tv_title.setTextColor(Color.WHITE);
//		}
		holder.tv_title.setTag(MediaUtils.CURPOSITION);
		holder.tv_title.setTag(position);//tag的作用就是为了反查
		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
		TextView tv_artist;
		TextView tv_letter;
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一个出现该字母的位置
	 */
	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == sectionIndex) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * 根据ListView 的当前位置获取分类的首字母的Char ascii值
	 */
	@Override
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
}
