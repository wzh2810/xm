package com.wz.xm.bean;

public class FileBean {
	public Integer number;
	public String fileName;

	public FileBean(Integer number,String fileName) {
		super();
		this.number = number;
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "FileBean [number=" + number + ", fileName=" + fileName + "]";
	}
	
	
	
}
