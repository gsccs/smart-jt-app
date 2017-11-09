package com.gsccs.smart.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gsccs.smart.R;

public class SystemToastDialog {

	public static void showShortToast(Context context, String text) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		View view = LayoutInflater.from(context).inflate(R.layout.system_toast_view, null);
		TextView toastText = (TextView) view.findViewById(R.id.toastText);
		toastText.setText(text);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public static void showShortToast(Context context, int stringId) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
//		TextView view = (TextView) LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
//		view.setBackgroundColor(context.getResources().getColor(R.color.red));
//		view.setTextColor(context.getResources().getColor(R.color.white));
//		view.setText(text);
		View view = LayoutInflater.from(context).inflate(R.layout.system_toast_view, null);
		TextView toastText = (TextView) view.findViewById(R.id.toastText);
		toastText.setText(stringId);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
