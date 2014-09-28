package de.ur.mi.mspwddhs.campusapp.mensa;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.*;

import de.ur.mi.mspwddhs.campusapp.database.Database;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MensaController extends AsyncTask<String, String, String>{
	
	private Database db;
	private String Result;
	
	private Context context;
	private OnResultListener listener;
	
	public MensaController(Context context, OnResultListener listener, Database db)
	{
		init(context,listener,db);
	}
	
	private void init(Context context, OnResultListener listener, Database db)
	{
		this.context = context;
		this.listener = listener;
		this.db = db;
	}
	
	public String getResult()
	{
		return Result;
	}
	
	protected String doInBackground(String... arg0){
	
	Calendar calender = Calendar.getInstance();
	int currentWeekNum = calender.get(Calendar.WEEK_OF_YEAR);
	String url = "http://www.stwno.de/infomax/daten-extern/csv/UNI-R/"+ currentWeekNum +".csv";
	
    int timeoutSocket = 5000;
    int timeoutConnection = 5000;
    System.out.println(url);
    HttpParams httpParameters = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    HttpClient client = new DefaultHttpClient(httpParameters);

    HttpGet httpget = new HttpGet(url);

    try {
        HttpResponse getResponse = client.execute(httpget);
        final int statusCode = getResponse.getStatusLine().getStatusCode();

        if(statusCode != HttpStatus.SC_OK) {
            Log.w("MyApp", "Download Error: " + statusCode + "| for URL: " + url);
            return null;
        }

        String line = "";
        StringBuilder total = new StringBuilder();
        

        HttpEntity getResponseEntity = getResponse.getEntity();

        BufferedReader reader = new BufferedReader(new InputStreamReader(getResponseEntity.getContent(),"ISO8859_1"));  
        while((line = reader.readLine()) != null) {
        	
        	String[] sep = line.split(";");
        	String warengruppeOhneZahl = sep[2].replaceAll("[0-9]","");
        	db.addContentMensa(sep[0], sep[1], warengruppeOhneZahl, sep[3], sep[4], sep[5]);
            total.append(line);
        }
        
        line = total.toString();
        return line;
        
    } catch (Exception e) {
        Log.w("MyApp", "Download Exception : " + e.toString());
    }
    return null;
}

public interface OnResultListener
{
	public void OnUpdateCompleted();
}

@Override
protected void onPostExecute(String result) 
{
	listener.OnUpdateCompleted();
}

}
