package com.gsccs.smart.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gsccs.smart.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class PhotoAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;

	public PhotoAdapter(Context context, List<String> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setListData(List<String> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.photo_check_list_view, null);
			ViewHolder holder = new ViewHolder();
			ViewUtils.inject(holder, view);
			view.setTag(holder);

		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		if (position == list.size()) {
			holder.image.setImageResource(R.drawable.photo_add);
		} else {
			holder.image.setImageURI(Uri.parse(list.get(position)));

		}

		return view;
	}

	class ViewHolder {
		@ViewInject(R.id.itemImage)
		ImageView image;

	}
}
