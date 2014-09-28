package de.ur.mi.mspwddhs.campusapp.mensa;

import java.util.ArrayList;

import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MensaListAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	private Database db;
	private ArrayList<MensaDay> mensaDaysArray;
	private MensaListAdapterChild adapAdap;
	
	public MensaListAdapter(ArrayList<String> dates, Database db, Context context) {
		this.db = db;
		this.context = context;
		mensaDaysArray = new ArrayList<MensaDay>();
		MensaDay m;
		for (int i = 0; i < dates.size(); i++){
			m = new MensaDay(dates.get(i), this.db);
			mensaDaysArray.add(m);
		}	
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		if(arg1 == 0) return mensaDaysArray.get(arg0).getSuppe();
		if(arg1 == 1) return mensaDaysArray.get(arg0).getHaupt();
		if(arg1 == 2) return mensaDaysArray.get(arg0).getBeilagen();
		return mensaDaysArray.get(arg0).getNachtisch();
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return 4;
	}

	@Override
	public Object getGroup(int arg0) {
		return mensaDaysArray.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return mensaDaysArray.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		
		
		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.mensa_parent, parent, false);
		}
		
		RelativeLayout rel = (RelativeLayout) convertView.findViewById(R.id.rel_mensa_parent);
		
		TextView day = (TextView) convertView.findViewById(R.id.wochentag);
		TextView date = (TextView) convertView.findViewById(R.id.date);
		ImageView collapse = (ImageView) convertView.findViewById(R.id.collapse);
		if (isExpanded) collapse.setBackgroundResource(R.drawable.collapse);
		if (!isExpanded) collapse.setBackgroundResource(R.drawable.collapse_not);
		
		date.setText(mensaDaysArray.get(groupPosition).getDate());
		
		if (groupPosition == 0) {
			day.setText("Mo");
			rel.setBackgroundResource(R.drawable.login_edit_text);
		}
		if (groupPosition == 1) {
			day.setText("Di");
			rel.setBackgroundResource(0);
		}
		if (groupPosition == 2) {
			day.setText("Mi");
			rel.setBackgroundResource(R.drawable.login_edit_text);
		}
		if (groupPosition == 3) {
			day.setText("Do");
			rel.setBackgroundResource(0);
		}
		if (groupPosition == 4) {
			day.setText("Fr");
			rel.setBackgroundResource(R.drawable.login_edit_text);
		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.mensa_child, parent, false);
		}
		TextView header = (TextView) convertView.findViewById(R.id.headerfood);
		
		if (childPosition == 0) header.setText(R.string.suppen);
		if (childPosition == 1) header.setText(R.string.hauptgerichte);
		if (childPosition == 2) header.setText(R.string.beilagen);
		if (childPosition == 3) header.setText(R.string.nachspeisen);
		
		ListView foods = (ListView) convertView.findViewById(R.id.foods);

		adapAdap = new MensaListAdapterChild(groupPosition, childPosition, mensaDaysArray, this.context);
		foods.setAdapter(adapAdap);
	
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}
