package de.ur.mi.android.excercises.starter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CourseListAdapter extends BaseExpandableListAdapter {
	private ArrayList<Course> data;
	private Context context;

	public CourseListAdapter(ArrayList<Course> data, Context context) {
		this.data = data;
		this.context = context;
		postItems();
	}
	
	private void postItems(){
		System.out.println("Data size is: " + data.size());
		for(int i = 0; i < data.size(); i++){
			System.out.println(data.get(i).getCourseName());
			for(int j = 0; j < data.get(i).getListLinks().size(); j++){
				System.out.println(data.get(i).getListLinks().get(j).getName());
			}
		}
	}

	@Override
	public int getGroupCount() {
		return data.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return data.get(groupPosition).getListLinks().size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return data.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(groupPosition).getListLinks().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		

		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.list_title, parent, false);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.list_title_name);
		title.setText(((Course) getGroup(groupPosition)).getCourseName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.list_item, parent, false);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.list_item_name);
		title.setText(((Link) getChild(groupPosition, childPosition)).getName());
		title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((TextView) v).setText("Clicked");
			}
		});

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
