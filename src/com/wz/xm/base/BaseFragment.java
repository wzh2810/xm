package com.wz.xm.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	public Context mContext;

	/**
	 * ��ȡ������ʲô�ô� 1.��д����,����������ù��е������Լ����еķ��� 2.���ù���Fragment���������ڷ���,ֻ��Ҫ�����Լ�����ķ�������
	 * 3.����ĳһ�������Ǳ���ʵ��,����ѡ����ʵ��
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();// ���Խ��ܱ��˴��ݹ����Ĳ���
		mContext = getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return initView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();

		initListener();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * ��ʼ����ʱ��,���Խ��ձ��˴��ݽ����Ĳ���
	 */
	public void init() {

	}

	/**
	 * ��ʼ��FragmentӦ�е���ͼ
	 * 
	 * @return
	 */
	public abstract View initView();

	/**
	 * ��ʼ��FragmentӦ�е�����
	 */
	public void initData() {

	}

	/**
	 * ��ʼ��������
	 */
	public void initListener() {

	}

}
