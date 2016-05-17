package com.wz.xm.utils.taskmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.wz.xm.bean.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class AppInfoParser {
    public static List<AppInfo> getAppInfos(Context context){
	PackageManager packageManager = context.getPackageManager();
	List<PackageInfo> pags = packageManager.getInstalledPackages(0);//����ѡ���ʾ   �ȸ�Ԥ����
	List<AppInfo> apps = new ArrayList<AppInfo>();
	for(PackageInfo pi:pags){
	   AppInfo appinfo = new AppInfo();
	   appinfo.setPackName(pi.packageName);
	   appinfo.setIcon(pi.applicationInfo.loadIcon(packageManager));
	   appinfo.setName(pi.applicationInfo.loadLabel(packageManager).toString());
	   appinfo.setSize(new File(pi.applicationInfo.publicSourceDir).length());
	   //apk��װ·�� 
	   appinfo.setApkpath(pi.applicationInfo.sourceDir);
	   
	   int flag = pi.applicationInfo.flags;//������ӳ��
	   //װ���ⲿ�ڲ��洢
	   if((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flag)!=0){
	       appinfo.setRom(false);
	   }else{
	       appinfo.setRom(true);
	   }
	   //�ж��Ƿ�ϵͳӦ��
	    if((ApplicationInfo.FLAG_SYSTEM & flag)!=0){
		appinfo.setSys(false);
	    }else{
		appinfo.setSys(true);
	    }
	    apps.add(appinfo);
	}
	return apps;
    }
}
