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
	//1.����Դ��ʲô�ط�
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
		/**---------------��ͼ�ĳ�ʼ��---------------**/
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
		
		//����position��ȡ���������ĸ��Char asciiֵ
		int section = getSectionForPosition(position);
		//�����ǰλ�õ��ڸ÷�������ĸ��Char��λ�ã�����Ϊ�ǵ�һ�γ���
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
		holder.tv_title.setTag(position);//tag�����þ���Ϊ�˷���
		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
		TextView tv_artist;
		TextView tv_letter;
	}

	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�����ָ���ĸ��λ��
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
	 * ����ListView �ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
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
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
}
