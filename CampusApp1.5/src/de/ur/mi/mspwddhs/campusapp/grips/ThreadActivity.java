package de.ur.mi.mspwddhs.campusapp.grips;

import java.util.ArrayList;

import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.ThreadParseController.ThreadParseListener;
import android.app.Activity;
import android.content.Intent;
import de.ur.mi.mspwddhs.campusapp.R;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ThreadActivity extends Activity implements ThreadParseListener {
	
	private TextView title;
	private ThreadParseController parse;
	private ThreadListAdapter listAdapt;
	private ListView list;
	private Database db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grips_activity_thread);
		System.out.println("oncreate");
		Intent intent = getIntent();
		db = new Database(this);
		db.open();
		parse = new ThreadParseController(this, this);
		title = (TextView) findViewById(R.id.thread_title);
		title.setText(intent.getStringExtra(CourseActivity.NAME_KEY));
		parse.initialize(intent.getStringExtra(CourseActivity.URL_KEY));
	}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	 }
	@Override
	public void onThreadDownloadCompleted(ArrayList<MyThread> data) {
		list = (ListView) findViewById(R.id.thread_List);
		listAdapt = new ThreadListAdapter(data, this);
		list.setAdapter(listAdapt);
		
		
	}
	@Override
	public String getUser() {
		return db.getLoginData().get(0);
	}
	@Override
	public String getPass() {
		return db.getLoginData().get(1);
	}
}