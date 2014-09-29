package de.ur.mi.mspwddhs.campusapp.grips;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.ParseController.OnParseListener;
import de.ur.mi.mspwddhs.campusapp.help.HelpActivity;
import de.ur.mi.mspwddhs.campusapp.mail.MailActivity;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaActivity;
import de.ur.mi.mspwddhs.campusapp.plan.PlanActivity;

import android.app.Activity;
import android.app.AlertDialog;
import de.ur.mi.mspwddhs.campusapp.MainActivity;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class GripsActivity extends OptionsActivity implements OnParseListener {

	public static final String NAME_KEY = "NAME_KEY";
	public static final String URL_KEY = "URL_KEY";
	public static final String USER_KEY = "USER_KEY";
	public static final String PW_KEY = "PW_KEY";

	CourseListAdapter courseAdapt;
	ParseController parse;
	ExpandableListView list;
	ProgressDialog updateDialog;
	ImageButton grips;
	ImageButton mensa;
	ImageButton mail;
	Database db;

	final UncaughtExceptionHandler subclass = Thread.currentThread()
			.getUncaughtExceptionHandler();
	Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread paramThread,
				Throwable paramThrowable) {
			Log.getStackTraceString(paramThrowable);

			subclass.uncaughtException(paramThread, paramThrowable);
		}
	};
	private UncaughtExceptionHandler androidDefaultUEH;
	private AlertDialog.Builder adb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grips_main);
		androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(handler);
		setupNavigationButtons();
		db = new Database(this);
		db.open();
		if(db.isGripsTableEmpty()){
			parse = new ParseController(this, this);
			parse.login();
		} else {
			setupList();
		}
	}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	 }

	private void setupNavigationButtons() {
		grips = (ImageButton) findViewById(R.id.gripsButton_grips);
		grips.setClickable(false);
		grips.setBackgroundColor(getResources().getColor(R.color.heidenelke));
		
		mensa = (ImageButton) findViewById(R.id.mensaButton_grips);
		mensa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GripsActivity.this, MensaActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				finish();
				overridePendingTransition(0, 0);
			}
		});
		
		mail = (ImageButton) findViewById(R.id.mailButton_grips);
		mail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GripsActivity.this, MailActivity.class);
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
				overridePendingTransition(0, 0);
				finish();
			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.refresh_mail);
        menu.removeItem(R.id.refresh_mensa); 
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.help) {
//			Intent intent = new Intent(GripsActivity.this, HelpActivity.class);
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
//			Intent intent = new Intent(GripsActivity.this, MainActivity.class);
//			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			startActivity(intent);
//			finish();
//		}
//		if (id == R.id.action_search) {
//		db.clearDatabaseGrips();
//		Intent intent = new Intent(GripsActivity.this, GripsActivity.class);
//		intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
//		startActivity(intent);
//		return true;
//	}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public String getPass() {
		return db.getLoginData().get(1);
	}

	@Override
	public String getUser() {
		return db.getLoginData().get(0);
	}

	@Override
	public void onDownloadInitiated() {

		String title = "Download";
		String item = "Kurse werden geladen...";
		updateDialog = new ProgressDialog(this);
		updateDialog.setTitle(title);
		updateDialog.setMessage(item);
		updateDialog.setCanceledOnTouchOutside(false);
		updateDialog.setCancelable(false);
		updateDialog.show();
	}

	@Override
	public void onDownloadFinished(ArrayList<Course> courses) {
		updateDialog.dismiss();
		setupList();
		System.out.println("3");

	}

	private void setupList() {
		list = (ExpandableListView) findViewById(R.id.list);
		list.setVisibility(View.VISIBLE);
		courseAdapt = new CourseListAdapter(db.getContentGrips(), this, this);
		list.setAdapter(courseAdapt);
		list.setGroupIndicator(null);
	}

	@Override
	public void onDownloadUpdated(int current, int max) {
		String item = "Downloading: ";
		updateDialog.setMessage(item + current + "/" + max);

	}

	protected void deleteSingleCourse(final View v, final String name,
			final String url) {
		adb = new AlertDialog.Builder(this);
		adb.setTitle("Delete?");
		adb.setMessage("Are you sure you want to delete this course?");
		adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				list.removeViewInLayout(v);
				courseAdapt.notifyDataSetChanged();
				db.clearSingleRowGrips(name + ";" + url);
				dialog.dismiss();
			}
		});
		adb.show();
	}

	protected void parseSingleItem(Link link) {
		String userS = db.getLoginData().get(0);
		String passS = db.getLoginData().get(1);
		System.out.println(userS + passS);
		Intent intent = new Intent(GripsActivity.this, CourseActivity.class);
		intent.putExtra(NAME_KEY, link.getName());
		intent.putExtra(URL_KEY, link.getUrl());
		intent.putExtra(USER_KEY, userS);
		intent.putExtra(PW_KEY, passS);
		startActivity(intent);
	}

	public void updateList() {
		courseAdapt.notifyDataSetChanged();
	}
}
