package de.ur.mi.mspwddhs.campusapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import de.ur.mi.mspwddhs.campusapp.database.Database;
import de.ur.mi.mspwddhs.campusapp.grips.ParseController;

public class LoginValidate {
	
	private LoginListener listener;
	private boolean response;
	
	public LoginValidate(LoginListener listener){
		this.listener = listener;
		response = false;
	}
	
	
	private class Login extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];
			String loginHtml = loginToGrips(ParseController.domainName,
					user, pass);
			if(loginHtml.contains("Benutzername oder Kennwort vergessen?")){
				response = false;
			} else {
				response = true;
			}
			
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
		listener.onFinish(response);
		}
		
		private String loginToGrips(String url, String username, String password) {
			String result = "";
			System.out.println("login");
			AbstractHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			try {
				final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("realm", "ur"));
				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs
						.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("rememberusername",
						"1"));
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = client.execute(post);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					result += line;
				}
				
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return result;
		}
	}
	
	public interface LoginListener{
		public void onFinish(boolean response);
	}

	public void initiate(Database db) {
		new Login().execute(db.getLoginData().get(0), db.getLoginData().get(1));
	}
}
