package de.ur.mi.android.excercises.starter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SimpleListAdapter extends BaseAdapter {
	private ArrayList<Course> data;
	private Context context;

	public SimpleListAdapter(ArrayList<Course> data, Context context) {
		this.data = data;
		this.context = context;
		postItems();
	}

	private void postItems() {
		System.out.println("Data size is: " + data.size());
		for (int i = 0; i < data.size(); i++) {
			System.out.println(data.get(i).getCourseName());
			for (int j = 0; j < data.get(i).getListLinks().size(); j++) {
				System.out.println(data.get(i).getListLinks().get(j).getName());
			}
		}
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.list_title, parent, false);
		}
		 TextView course = (TextView) convertView.findViewById(R.id.list_title_name);
		course.setText(data.get(position).getCourseName());
		course.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((TextView) v).setText(data.get(position).getListLinks().get(0).getName());
			}
		});
		
		return convertView;

	}

}
