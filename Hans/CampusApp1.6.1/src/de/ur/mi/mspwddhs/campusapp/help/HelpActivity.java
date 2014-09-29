package de.ur.mi.mspwddhs.campusapp.help;

import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.plan.PlanActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class HelpActivity extends OptionsActivity{
	
	int counter = 1;
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        showHelp();
	}
	
	public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	 }
	
	private void showHelp() {
//		Button helpbutton = (Button) findViewById(R.id.weiter);
		
		ImageView help = (ImageView) findViewById(R.id.helpImage);
		help.setBackgroundResource(findRightID());

	}
	
	public void weiter(View v){
		if (counter == 9) {
			finish();
		}
		counter++;
		showHelp();
	}
	
	private int findRightID() {
		String name = "help" + counter;
		int resID = getResources().getIdentifier(name, "drawable", getPackageName());
		return resID;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.removeItem(R.id.refresh_mail);
        menu.removeItem(R.id.refresh_grips);
        menu.removeItem(R.id.refresh_mensa); 
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//    	switch (item.getItemId()) {
//        case R.id.about:
////        	startActivity(new Intent(this, AboutActivity.class).addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION));  	
//        	return true;
//        case R.id.help:
////        	startActivity(new Intent(this, Help.class));
//        	return true;
//        case R.id.campusplan:
//        	startActivity(new Intent(this, PlanActivity.class).addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION));  	
//        	return true;
////        case R.id.action_settings:
//////        	startActivity(new Intent(this, Help.class));
////            return true;
//        case R.id.logout:
////        	db.logout(); //löscht Login Daten, Grips und Email
//            return true;
//        default:
//        return super.onOptionsItemSelected(item);
//    }

}
