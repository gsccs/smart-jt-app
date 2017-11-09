package com.gsccs.smart.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class SystemProgressDialog extends ProgressDialog {
	private static SystemProgressDialog instance = null;

	private Context context;
	public static SystemProgressDialog newInstance(Context context) {
		if(instance == null){
			instance = new SystemProgressDialog(context);
			instance.context = context;
		}else if(instance.context != context){
			instance = new SystemProgressDialog(context);
			instance.context = context;
		}
		return instance;
	}

	private SystemProgressDialog(Context context) {
		super(context);
		setMessage("正在加载中....");
		setCancelable(false);
		//只有当点击 【返回按键】时才会关闭 提示框
		setOnKeyListener(onKeyListener);
	}

	private OnKeyListener onKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				instance.dismiss();
			}

			return false;
		}
	};
}
