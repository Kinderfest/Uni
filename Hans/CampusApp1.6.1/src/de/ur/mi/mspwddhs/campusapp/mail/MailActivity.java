package de.ur.mi.mspwddhs.campusapp.mail;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import de.ur.mi.mspwddhs.campusapp.MainActivity;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.GripsActivity;
import de.ur.mi.mspwddhs.campusapp.help.HelpActivity;
import de.ur.mi.mspwddhs.campusapp.mail.Controller1.emailListener;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaActivity;
import de.ur.mi.mspwddhs.campusapp.plan.PlanActivity;


public class MailActivity extends OptionsActivity implements emailListener {
	public static final String SUBJECT_KEY = "SUBJECT_KEY";
	public static final String USER_KEY ="USER_KEY";
	public static final String RECIPIENTS_KEY = "RECIPIENTS_KEY";
	public static final String PASSWORD_KEY = "PASSWORD_KEY";
	public static final String USERADRESS_KEY = "USERADRESS_KEY";
	
	Button checkForMails;
	ExpandableListView listView;
	ExpandableListAdapter listAdapter;
	
	Database db;
	
	ImageButton grips;
	ImageButton mensa;
	ImageButton mail;
	
	ProgressDialog dialog;
	Controller1 controller;
	Controller2 controller2;
	Answer answer;
	
	private String user;
	private String password;
	private String userAdress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mail_activity_main);
		setupNavigationButtons();
		db = new Database(this);
		db.open();
		
		user = db.getLoginData().get(0);
		password = db.getLoginData().get(1);
		userAdress = db.getLoginData().get(2);
		System.out.println(userAdress);
		
		controller = new Controller1(this);
		listView = (ExpandableListView) findViewById(R.id.expand);
		controller.execute(user, password, userAdress);
		
		setupDialog();
	}
	
	private void setupNavigationButtons() {
		mail = (ImageButton) findViewById(R.id.mailButton_mail);
		mail.setClickable(false);
		mail.setBackgroundColor(getResources().getColor(R.color.heidenelke));
		
		mensa = (ImageButton) findViewById(R.id.mensaButton_mail);
		mensa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MailActivity.this, MensaActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
//				finish();
			}
		});
		
		grips = (ImageButton) findViewById(R.id.gripsButton_mail);
	//	grips.setBackgroundColor(getResources().getColor(R.color.light_grey));
		grips.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MailActivity.this, GripsActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
//				finish();
			}
		});
	}
	
	private void setupDialog() {
		dialog = new ProgressDialog(this);
		dialog.setTitle("Download");
		dialog.setMessage("Emails werden heruntergeladen...");
		dialog.show();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.removeItem(R.id.refresh_grips);
        menu.removeItem(R.id.refresh_mensa);    
        return true;
    }
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	 }
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.help) {
//			Intent intent = new Intent(MailActivity.this, HelpActivity.class);
//			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			startActivity(intent);
//			return true;
//		}
//		if( id == R.id.campusplan) {
//        	startActivity(new Intent(this, PlanActivity.class).addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION));  	
//		}
//		if (id == R.id.logout) {
//			db.clearDatabaseLogin();
//			db.clearDatabaseGrips();
//			Intent intent = new Intent(MailActivity.this, MainActivity.class);
//			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			startActivity(intent);
//			finish();
//		}
//		if (id == R.id.action_search) {
//			onCreate(null);
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		public PlaceholderFragment() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.mail_fragment_main, container,
					false);
			return rootView;
		}
	}
	@Override
	public void onUpdateCompleted(ArrayList<Email> mails) {
		
		Collections.reverse(mails);
		listAdapter = new ExpandableListAdapter(mails, this, this);
		listView.setAdapter(listAdapter);
		
		dialog.dismiss();
		
	}
	protected void mailAnswer(String subject, String to) {
		Intent intent = new Intent(MailActivity.this, Answer.class);
		intent.putExtra(USER_KEY, user);
		intent.putExtra(PASSWORD_KEY, password);
		intent.putExtra(USERADRESS_KEY, userAdress);
		intent.putExtra(RECIPIENTS_KEY, to);
		intent.putExtra(SUBJECT_KEY, subject);
		startActivity(intent);
	}
}