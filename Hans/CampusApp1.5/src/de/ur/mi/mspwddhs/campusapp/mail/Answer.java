package de.ur.mi.mspwddhs.campusapp.mail;

import de.ur.mi.mspwddhs.campusapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Answer extends Activity {

	private static final int DIALOG_ALERT = 1;

	TextView toView;
	EditText editAnswer;
	Button sendButton;
	Controller2 controller;
	ExpandableListAdapter adapter;

	Bundle bundle;
	Intent intent;

	String mailText;
	String sender;
	String user;
	String password;
	String reciever;
	String text;
	String mailSubject;
	String subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("ans1");
		super.onCreate(savedInstanceState);
		System.out.println("ans2");
		setContentView(R.layout.mail_answer_layout);
		System.out.println("setup?");
		controller = new Controller2();
		intent = getIntent();
		System.out.println("intent working");
		toView = (TextView) findViewById(R.id.an);
		editAnswer = (EditText) findViewById(R.id.answerEdit);
		sendButton = (Button) findViewById(R.id.answerButton);
		System.out.println("layout kaputt?");
		sender = intent.getStringExtra(MailActivity.USERADRESS_KEY);
		user = intent.getStringExtra(MailActivity.USER_KEY);
		password = intent.getStringExtra(MailActivity.PASSWORD_KEY);
		reciever = intent.getStringExtra(MailActivity.RECIPIENTS_KEY);
		subject = intent.getStringExtra(MailActivity.SUBJECT_KEY);
		System.out.println("auf gut Glück");
		mailSubject = "RE: " + subject;
		
		
		toView.setText("An: " + reciever);
		TextView sendSubject = (TextView) findViewById(R.id.sendSubject);
		sendSubject.setText("Betreff: " + mailSubject);
		
//		TextView titel = (TextView) findViewById(R.id.titel_answer);
//		titel.setText("Antwort");

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				text = getMailText();
				controller.execute(sender, user, password, text, mailSubject,
						reciever);

				showDialog(DIALOG_ALERT);

			}
		});

	}

	private String getMailText() {

		mailText = editAnswer.getText().toString();

		return mailText;

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ALERT:
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Email wurde verschickt!");
			builder.setCancelable(true);
			builder.setPositiveButton("Ok", new OkOnClickListener());
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		return super.onCreateDialog(id);
	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {

			Intent intent = new Intent(Answer.this, MailActivity.class);

			startActivity(intent);

		}
	}
}