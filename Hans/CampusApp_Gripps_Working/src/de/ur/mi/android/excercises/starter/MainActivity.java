package de.ur.mi.android.excercises.starter;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import de.ur.mi.android.excercises.starter.ParseController.OnParseListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnParseListener {
	CourseListAdapter courseAdapt;
	ParseController parse;
	ExpandableListView list;
	ProgressDialog updateDialog;
	TextView view;
	EditText user;
	EditText pass;
	Button button;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(handler);
		parse = new ParseController(this, this);
		pass = (EditText) findViewById(R.id.editTextPass);
		list = (ExpandableListView) findViewById(R.id.list);
		user = (EditText) findViewById(R.id.editTextUser);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				parse.login();
				button.setVisibility(View.GONE);
				pass.setVisibility(View.GONE);
				user.setVisibility(View.GONE);
				view = (TextView) findViewById(R.id.textView1);
				view.setVisibility(View.GONE);
				view = (TextView) findViewById(R.id.textView2);
				view.setVisibility(View.GONE);
				list.setVisibility(View.VISIBLE);
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(pass.getWindowToken(), 0);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public String getPass() {
		return pass.getText().toString();
	}

	@Override
	public String getUser() {
		return user.getText().toString();
	}

	@Override
	public void onDownloadInitiated() {
		String title = "Download in progress";
		String item = "Downloading...";
		updateDialog = new ProgressDialog(this);
		updateDialog.setTitle(title);
		updateDialog.setMessage(item);
		updateDialog.show();
	}

	@Override
	public void onDownloadFinished(ArrayList<Course> courses) {
		updateDialog.dismiss();
		courseAdapt = new CourseListAdapter(courses, this);
		list.setAdapter(courseAdapt);
		list.setGroupIndicator(null);
		System.out.println("3");

	}

	@Override
	public void onDownloadUpdated(int current, int max) {
		String item = "Downloading: ";
		updateDialog.setMessage(item + current + "/" + max);

	}
}
