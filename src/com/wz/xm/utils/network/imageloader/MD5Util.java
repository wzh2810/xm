package com.wz.xm.utils.network.imageloader;

import java.security.MessageDigest;

public class MD5Util {// 用于加密的字�?
	public final static String md5(String pwd) {
		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			// 使用平台的默认字符集将此 String 编码�? byte序列，并将结果存储到�?个新�? byte数组�?
			byte[] btInput = pwd.getBytes();

			// 获得指定摘要算法�? MessageDigest对象，此处为MD5
			// MessageDigest类为应用程序提供信息摘要算法的功能，�? MD5 �? SHA 算法�?
			// 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值�??
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// System.out.println(mdInst);
			// MD5 Message Digest from SUN, <initialized>

			// MessageDigest对象通过使用 update方法处理数据�? 使用指定的byte数组更新摘要
			mdInst.update(btInput);
			// System.out.println(mdInst);
			// MD5 Message Digest from SUN, <in progress>

			// 摘要更新之后，�?�过调用digest（）执行哈希计算，获得密�?
			byte[] md = mdInst.digest();
			// System.out.println(md);

			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			// System.out.println(j);
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) { // i = 0
				byte byte0 = md[i]; // 95
				str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
				str[k++] = md5String[byte0 & 0xf]; // F
			}
			// 返回经过加密后的字符�?
			return new String(str).toLowerCase();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
