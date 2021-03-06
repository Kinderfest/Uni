package de.ur.mi.mspwddhs.campusapp.mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import de.ur.mi.mspwddhs.campusapp.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private ArrayList<Email> data;
	private Context context;
	private Activity activity;
	private String subject;
		
	public ExpandableListAdapter(ArrayList<Email> data, Context context,
			Activity activity) {

		this.data = data;
		this.context = context;
		this.activity = activity;
		
	}

	@Override
	public int getGroupCount() {

		return data.size();

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return data.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(groupPosition);
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
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.mail_list_group, parent,
					false);
		}
		
		ImageView collapse = (ImageView) convertView.findViewById(R.id.collapse);
		if (isExpanded) collapse.setBackgroundResource(R.drawable.collapse);
		if (!isExpanded) collapse.setBackgroundResource(R.drawable.collapse_not);
		
		
		ImageView answered = (ImageView) convertView.findViewById(R.id.answered);
		if (isExpanded) answered.setBackgroundResource(R.drawable.answered);
		if (!isExpanded) answered.setBackground(null);
		
		
		TextView fromEmail = (TextView) convertView.findViewById(R.id.fromEmail);
		fromEmail.setText(getLastFrom(groupPosition));
		
		TextView from = (TextView) convertView.findViewById(R.id.from);
		from.setText("Von: " + makeRightFrom(groupPosition));
		
		TextView date = (TextView) convertView.findViewById(R.id.date);
		date.setText(makeDateString(groupPosition));

		TextView subject = (TextView) convertView.findViewById(R.id.subject);
		subject.setText("Betreff: " + data.get(groupPosition).getSubject());

		return convertView;

	}

	private String makeRightFrom(int groupPosition) {
		String textFrom = data.get(groupPosition).getFrom();
		String[] sepFrom = textFrom.split(" ");
		int lastSep = sepFrom.length;
		String rightFrom = "";
		for (int i = 0; i < lastSep-1; i++) {
			rightFrom = rightFrom+ sepFrom[i];
		}
		return rightFrom;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.mail_list_item, parent,
					false);
		}

		Button answerButton = (Button) convertView
				.findViewById(R.id.answerButton);
		answerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MailActivity) activity).mailAnswer(data.get(groupPosition)
						.getSubject(), data.get(groupPosition).getFrom());

			}
		});

//		TextView fromView = (TextView) convertView.findViewById(R.id.fromView);
//		fromView.setText("Von: " + makeRightFrom(groupPosition));
//		
//		TextView fromViewEmail = (TextView) convertView.findViewById(R.id.fromViewEmail);
//		fromViewEmail.setText("Mailadresse: " + getLastFrom(groupPosition));
//
//		TextView dateView = (TextView) convertView.findViewById(R.id.dateView);
//		dateView.setText(makeDateString(groupPosition));
//
//		TextView subjectView = (TextView) convertView
//				.findViewById(R.id.subjectView);
//		subjectView.setText("Betreff: " + data.get(groupPosition).getSubject());
//		subject = data.get(groupPosition).getSubject();

		TextView textView = (TextView) convertView.findViewById(R.id.textView);
		String text = data.get(groupPosition).getContent();
//		String lineSeparator = System.getProperty("line.separator");
//		text.replaceAll("(\r?\n){2,}", lineSeparator);
		
		textView.setText(Html.fromHtml(text));
		textView.setMovementMethod(LinkMovementMethod.getInstance());

		return convertView;
	}
	
	private String getLastFrom(int groupPosition) {
		String textFrom = data.get(groupPosition).getFrom();
		String[] sepFrom = textFrom.split(" ");
		int lastSep = sepFrom.length;
		String rightFrom = sepFrom[lastSep-1];
		return rightFrom;
	}

	private String makeDateString (int groupPosition) {
		String dateString = data.get(groupPosition).getDate();
		String[] sep = dateString.split(" ");
		dateString = "Am " + sep[0] + " um " + sep[1] + " Uhr";
		return dateString;
	}
	

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSubject() {

		return subject;

	}
}