package de.ur.mi.mspwddhs.campusapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import de.ur.mi.mspwddhs.campusapp.LoginValidate.LoginListener;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.help.HelpActivity;
import de.ur.mi.mspwddhs.campusapp.mensa.MensaActivity;
import de.ur.mi.mspwddhs.campusapp.plan.PlanActivity;


public class MainActivity extends OptionsActivity implements LoginListener {
	EditText user;
	EditText pass;
	ImageView logo;
	Button button;
	Database db;
	LoginValidate login;
	ProgressDialog updateDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout); 
        db = new Database(this);
        db.open();
        login = new LoginValidate(this, this);
        if(db.isLoginTableEmpty()){
        logo = (ImageView) findViewById (R.id.logo_login);
        logo.setBackgroundResource(R.drawable.logo_ur);
        pass = (EditText) findViewById(R.id.editTextPass);
		user = (EditText) findViewById(R.id.editTextUser);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onDownloadInitiation();
				login.initiate(user.getText().toString(), pass.getText().toString());
			}

		});
		
        } else {
        	changeToMensa();
        }
        

    }
    
    public void onPause() {
	     super.onPause();
	     overridePendingTransition(0, 0);
	 }

    private void changeToMensa() {
		Intent intent = new Intent(MainActivity.this, MensaActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.removeItem(R.id.refresh_mail);
        menu.removeItem(R.id.refresh_grips);
        menu.removeItem(R.id.refresh_mensa); 
        menu.removeItem(R.id.logout);
        return true;
    }

	public void onDownloadInitiation() {
		updateDialog = new ProgressDialog(this);
		updateDialog.setTitle("Login");
		updateDialog.setMessage("�berpr�fe Logindaten");
		updateDialog.show();
		
	}

	@Override
	public void onFinish(boolean response) {
		updateDialog.dismiss();
		if(response){
			Toast toast = Toast.makeText(this, "Login erfolgreich!", Toast.LENGTH_SHORT);
			toast.show();
			changeToMensa();
		} else {
			Toast toast = Toast.makeText(this, "Login fehlgeschlagen!", Toast.LENGTH_SHORT);
			db.clearDatabaseLogin();
			pass.setText("");
			user.setText("");
			toast.show();
		}
	}
}
