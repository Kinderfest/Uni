package de.ur.mi.mspwddhs.campusapp.mensa;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import de.ur.mi.mspwddhs.campusapp.MainActivity;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.GripsActivity;
import de.ur.mi.mspwddhs.campusapp.help.HelpActivity;
import de.ur.mi.mspwddhs.campusapp.mail.MailActivity;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaController.OnResultListener;
import de.ur.mi.mspwddhs.campusapp.plan.PlanActivity;


public class MensaActivity extends OptionsActivity implements OnResultListener {
	
	
	private ArrayList<String> content = new ArrayList<String>();
	private Database db;
	MensaController Maike;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	ExpandableListView listView;
	Calendar calendar;
	MensaListAdapter mensaListAdap;
	TextView text;	
	Button buttonGrips;
	Button buttonMensa;
	Button buttonEmail;
	String monday;
	private static final int DIALOG_ALERT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mensa);
		initDB();
		doIt();
		CreateDialogs();
		progressDialog.show();
		Maike = new MensaController(this,this,db);
		checkDatabase();	
	}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	 }
	
	private void doIt() {
		buttonGrips = (Button) findViewById(R.id.grips);
		buttonGrips.setText("Grips");
		buttonMensa = (Button) findViewById(R.id.mensa);
		buttonMensa.setText("Mensa");
		buttonEmail = (Button) findViewById(R.id.mail);
		buttonEmail.setText("Mail");
		buttonMensa.setBackgroundColor(getResources().getColor(R.color.heidenelke));
		buttonMensa.setTextColor(Color.WHITE);
	}

public void onClick(View v) {
	Button ClickedButton = (Button) findViewById(v.getId());
	if (ClickedButton == buttonGrips) {
		Intent grips = new Intent(MensaActivity.this, GripsActivity.class);
		grips.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(grips);
//		finish();
	}
	if (ClickedButton == buttonEmail) {
		Intent mail = new Intent(MensaActivity.this, MailActivity.class);
		mail.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(mail);
//		finish();
	}
}
	
	private void CreateDialogs()
	{
		//ProgressDialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getString(R.string.updating_title));
		progressDialog.setMessage(getString(R.string.updating_text));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		
		//AlertDialog
		alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(getString(R.string.error_title));
		alertDialogBuilder.setMessage(getString(R.string.error_text));
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(R.string.ok_button,
				new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				}
				});
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.removeItem(R.id.refresh_mail);
        menu.removeItem(R.id.refresh_grips);
		return true;
	}
	
	private void initDB() {
		db = new Database(this);
		db.open();
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		 
//		int id = item.getItemId();
//		if (id == R.id.action_search) {
//			db.clearDatabaseMensa();
//			Intent intent = new Intent(MensaActivity.this, MensaActivity.class);
//			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			startActivity(intent);
//			finish();
//			return true;
//		}
//		if (id == R.id.help) {
//			Intent intent = new Intent(MensaActivity.this, HelpActivity.class);
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
//			Intent intent = new Intent(MensaActivity.this, MainActivity.class);
//			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
//			startActivity(intent);
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
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container, false);
			return rootView;
		}
	}


	@Override
	public void OnUpdateCompleted() {
		progressDialog.dismiss();
		createLists();
	}
	
	public void showLegende(View w) {
		showDialog(DIALOG_ALERT);
 		 
	}
	
	protected Dialog onCreateDialog (int id) {
		ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.legende);
		switch (id) {
		case DIALOG_ALERT:
			Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true).setView(image);
//			builder.setPositiveButton("Ok", new OkOnClickListener()).setView(image);
			
			AlertDialog dialog = builder.create();
			dialog.show();
			
		}
		return super.onCreateDialog(id);
	}
	
	private final class OkOnClickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			Intent intent = new Intent(MensaActivity.this, MensaActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			// TODO Auto-generated method stub
			
		}
		
	}
	
	

	private void createLists() {
		
//		Button legendeButton = (Button) findViewById(R.id.legende);
		
		TextView mensatitle = (TextView) findViewById(R.id.mensatitle);
		mensatitle.setText("Das gibt's diese Woche...");
		
		TextView mensaschluss = (TextView) findViewById(R.id.mensaschluss);
		mensaschluss.setText("An Guaden!");
		
		
		listView = (ExpandableListView) findViewById(R.id.mensa_week);
		mensaListAdap = new MensaListAdapter(datesOfWeek(), db, this);
		listView.setAdapter(mensaListAdap);
		listView.setGroupIndicator(null);
	
	}
	
	private ArrayList<String> datesOfWeek() {
		ArrayList<String> datesOfWeek = new ArrayList<String>();
		calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		for (int i = 0; i < 5; i++) {
			Date currentDate = calendar.getTime();
			String currentDateString = df.format(currentDate);
			datesOfWeek.add(currentDateString);
		    calendar.add(Calendar.DATE, 1);
		}
        return datesOfWeek;
	}
	
	private String getDateOfMonday() {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date currentDate = calendar.getTime();
		String currentDateString = df.format(currentDate);

		return currentDateString;
	}
	
	private void checkDatabase() {
		if(db.getCount(getDateOfMonday()) != true) {
			db.clearDatabaseMensa();
			Maike.execute();
			createLists();
		}
		else {
			OnUpdateCompleted();
		}
	}
}