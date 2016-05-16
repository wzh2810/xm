package com.wz.xm.bean;

public class NetBean {
	public Integer number;
	public String netName;

	public NetBean(Integer number,String netName) {
		super();
		this.number = number;
		this.netName = netName;
	}

	@Override
	public String toString() {
		return "NetBean [number=" + number + ", netName=" + netName + "]";
	}

	
}
