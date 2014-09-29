package de.ur.mi.mspwddhs.campusapp.plan;

import de.ur.mi.mspwddhs.campusapp.MainActivity;
import de.ur.mi.mspwddhs.campusapp.OptionsActivity;
import de.ur.mi.mspwddhs.campusapp.R;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.help.HelpActivity;
import de.ur.mi.mspwddhs.campusapp.mail.MailActivity;
import uk.co.senab.photoview.PhotoViewAttacher;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class PlanActivity extends OptionsActivity{

		
		ImageView mImageView;
		PhotoViewAttacher mAttacher;
		Database db;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.plan);
	        
	        mImageView = (ImageView) findViewById(R.id.img);
	        Drawable bitmap = getResources().getDrawable(R.drawable.campus);
	        mImageView.setImageDrawable(bitmap);
	        mAttacher = new PhotoViewAttacher(mImageView);

	    }
	    
	    public void onPause() {
		     super.onPause();
		     overridePendingTransition(0, 0);
		 }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.main, menu);
	        menu.removeItem(R.id.refresh_mail);
	        menu.removeItem(R.id.refresh_grips);
	        menu.removeItem(R.id.refresh_mensa); 
	        return true;
	    }

//	    @Override
//	    public boolean onOptionsItemSelected(MenuItem item) {
//	        // Handle action bar item clicks here. The action bar will
//	        // automatically handle clicks on the Home/Up button, so long
//	        // as you specify a parent activity in AndroidManifest.xml.
//	        int id = item.getItemId();
//	        if (id == R.id.logout) {
//				clearDB();
//				Intent intent = new Intent(PlanActivity.this, MainActivity.class);
//				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				startActivity(intent);
//				finish();
//			}
//	        if (id == R.id.help) {
//	        	Intent intent = new Intent(PlanActivity.this, HelpActivity.class);
//				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				startActivity(intent);
//	        }
//	        return super.onOptionsItemSelected(item);
//	    }

//		private void clearDB() {
//			db.clearDatabaseLogin();
//			db.clearDatabaseGrips();
//			db.clearDatabaseMensa();
//		}


}
