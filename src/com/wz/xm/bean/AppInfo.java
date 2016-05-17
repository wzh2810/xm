package com.wz.xm.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
	// apk路径
	private String apkpath;

	public String getApkpath() {
		return apkpath;
	}

	public void setApkpath(String apkpath) {
		this.apkpath = apkpath;
	}

	private Drawable icon;
	private String name;
	// false 是外部
	private boolean isRom;
	private long size;
	// 系统是false
	private boolean isSys;
	private String packName;

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRom() {
		return isRom;
	}

	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isSys() {
		return isSys;
	}

	public void setSys(boolean isSys) {
		this.isSys = isSys;
	}

	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", name=" + name + ", isRom=" + isRom + ", size=" + size + ", isSys=" + isSys
				+ ", packName=" + packName + "]";
	}

}
