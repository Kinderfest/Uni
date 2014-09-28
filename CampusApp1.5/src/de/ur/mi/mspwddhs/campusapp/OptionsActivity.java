package de.ur.mi.mspwddhs.campusapp;

import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.GripsActivity;
import de.ur.mi.mspwddhs.campusapp.help.HelpActivity;
import de.ur.mi.mspwddhs.campusapp.mail.MailActivity;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaActivity;
import de.ur.mi.mspwddhs.campusapp.plan.PlanActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class OptionsActivity extends Activity{
	
	Database db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new Database(this);
		db.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.help) {
			Intent intent = new Intent(OptionsActivity.this, HelpActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return true;
		}
		if( id == R.id.campusplan) {
        	startActivity(new Intent(this, PlanActivity.class).addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION));  	
		}
		if (id == R.id.logout) {
			db.clearDatabaseLogin();
			db.clearDatabaseGrips();
//			db.clearDatabaseMail();
			db.clearDatabaseMensa();
			Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			finish();
			return true;
		}
		if (id == R.id.refresh_grips) {
			db.clearDatabaseGrips();
			Intent intent = new Intent(OptionsActivity.this, GripsActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			finish();
			return true;
		}
		if (id == R.id.refresh_mensa) {
			db.clearDatabaseMensa();
			Intent intent = new Intent(OptionsActivity.this, MensaActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return true;
		}
		if (id == R.id.refresh_mail) {
//			db.clearDatabaseMail();
			Intent intent = new Intent(OptionsActivity.this, MailActivity.class);
			intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
