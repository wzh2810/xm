package com.wz.xm.utils.taskmanager;

import android.app.Activity;
import android.widget.Toast;

public class ShowToast {
    public static void showToast(final Activity context,final String msg){
	if("main".equals(Thread.currentThread().getName())){
	    Toast.makeText(context, msg, 0).show();
	}else{
	    context.runOnUiThread(new Runnable() {
	        
	        @Override
	        public void run() {
	            Toast.makeText(context, msg, 0).show();
	        }
	    });
	}
    }
}
