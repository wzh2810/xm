package com.wz.xm.bean;

public class Music {

	public String title;
	public String artist;
	public String path;// Â·¾¶

	private String sortLetters;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	@Override
	public String toString() {
		return "Music [title=" + title + ", artist=" + artist + ", path=" + path + ", sortLetters=" + sortLetters + "]";
	}

}
