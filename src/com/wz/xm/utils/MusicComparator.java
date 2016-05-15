package com.wz.xm.utils;

import java.util.Comparator;

import com.wz.xm.bean.Music;



public class MusicComparator implements Comparator<Music> {

	@Override
	public int compare(Music o1, Music o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
