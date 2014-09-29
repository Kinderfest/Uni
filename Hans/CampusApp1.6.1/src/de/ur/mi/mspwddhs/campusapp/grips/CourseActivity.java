package de.ur.mi.mspwddhs.campusapp.grips;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.ForumParseController.ForumListener;
import de.ur.mi.mspwddhs.campusapp.grips.GradesParseController.GradesParseListener;

public class CourseActivity extends Activity implements ForumListener, GradesParseListener{
	
	public static final String URL_KEY = "URL_KEY";
	public static final String NAME_KEY = "NAME_KEY";
	TextView title;
	String pass;
	String user;
	ForumParseController forumsParse;
	GradesParseController gradesParse;
	ListView list;
	ForumListAdapter listAdaptForum;
	GradesListAdapter listAdaptGrades;
	Database db;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grips_acivity_course);
		Intent intent = getIntent();
		title = (TextView) findViewById(R.id.forum_title);
		title.setText(intent.getStringExtra(GripsActivity.NAME_KEY));
		user = intent.getStringExtra(GripsActivity.USER_KEY);
		pass = intent.getStringExtra(GripsActivity.PW_KEY);
		
		
		dialog = new  ProgressDialog(this);
		
		if(intent.getStringExtra(GripsActivity.NAME_KEY).contains("Bewertung")){
			gradesParse = new GradesParseController(this);
			dialog.setMessage("Bewertungen werden geladen...");
			gradesParse.initialize(intent.getStringExtra(GripsActivity.URL_KEY));
		} else {
			forumsParse = new ForumParseController(this);
			dialog.setMessage("Forum wird geladen...");
			forumsParse.initialize(intent.getStringExtra(GripsActivity.URL_KEY));
		}
		
		dialog.setTitle("Download");
		
		dialog.show();
		}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	 }

	@Override
	public String getPass() {
		return pass;
	}

	@Override
	public String getUser() {
		return user;
	}
	
	@Override
	public void onForumDownloadCompleted(ArrayList<Forum> result) {
		list = (ListView) findViewById(R.id.forum_list);
		listAdaptForum = new ForumListAdapter(result, this, this);
		list.setAdapter(listAdaptForum);
		dialog.dismiss();
	}
	
	public void switchToThreadActivity(String url, String name){
		Intent intent = new Intent(CourseActivity.this, ThreadActivity.class);
		intent.putExtra(URL_KEY, url);
		intent.putExtra(NAME_KEY, name);
		startActivity(intent);
	}

	@Override
	public void onGradesDownloadCompleted(ArrayList<Grades> data) {
		dialog.dismiss();
		list = (ListView) findViewById(R.id.forum_list);
		listAdaptGrades = new GradesListAdapter(data, this);
		list.setAdapter(listAdaptGrades);
		
		
	}
	
	

}
