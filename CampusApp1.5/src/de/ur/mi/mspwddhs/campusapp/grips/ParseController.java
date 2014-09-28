package de.ur.mi.mspwddhs.campusapp.grips;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.ur.mi.mspwddhs.campusapp.database.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ParseController {

	public static final String domainName = "https://elearning.uni-regensburg.de/login/index.php";

	private Context context;
	private OnParseListener listener;
	private ArrayList<Course> myCourses;
	private CookieStore cookieStore;
	private Database db;

	public ParseController(Context context, OnParseListener listener) {
		this.context = context;
		this.listener = listener;
		db = new Database(this.context);
		db.open();
	}

	public CookieStore getCookies() {
		return cookieStore;
	}

	public void login() {
		new LoginToGrips().execute("", "", "");
		listener.onDownloadInitiated();
	}

	private class LoginToGrips extends AsyncTask<String, Integer, Void> {

		@Override
		protected Void doInBackground(String... params) {
			myCourses = new ArrayList<Course>();

			String username = listener.getUser();
			String password = listener.getPass();
			System.out.println("Connecting to GRIPS");
			String loginHtml = loginToGrips(domainName, username, password);
			getMyCourses(loginHtml);
			return null;

		}

		@Override
		protected void onPostExecute(Void response) {
			parseMyCourses();
		}

		private String loginToGrips(String url, String username, String password) {
			String result = "";
			System.out.println("login");
			cookieStore = new BasicCookieStore();
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
				cookieStore = client.getCookieStore();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return parseMyCoursesHtml();
		}

		private String parseMyCoursesHtml() {
			String myCourseUrl = "https://elearning.uni-regensburg.de/my/";
			return getHttpFromUrl(myCourseUrl);
		}

		private String getHttpFromUrl(String url) {
			String result = "";
			@SuppressWarnings("resource")
			AbstractHttpClient client = new DefaultHttpClient();
			client.setCookieStore(cookieStore);
			HttpPost post = new HttpPost(url);
			try {
				HttpResponse response = client.execute(post);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					// System.out.println(line);
					result += line;
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		private void getMyCourses(String url) {
			Document doc = Jsoup.parse(url, "ISO-8859-1");
			String allCoursesString = "";
			Elements links = doc.select(".block_course_overview a[href]");
			for (Element link : links) {
				if (link.toString().contains("/course/view.php?id=")) {
					if (!(link.toString().contains("9090") || link.toString()
							.contains("5506"))) {
						myCourses.add(new Course(link.text(), link
								.attr("abs:href")));
					}
				}
			}
		}
	}

	public interface OnParseListener {
		public String getPass();

		public String getUser();

		public void onDownloadInitiated();

		public void onDownloadUpdated(int current, int max);

		public void onDownloadFinished(ArrayList<Course> courses);
	}

	private class GetMyCourses extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			parseAllMyCourses();
			return "";
		}

		@Override
		protected void onProgressUpdate(Integer... current) {
			listener.onDownloadUpdated(current[0], current[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			listener.onDownloadFinished(myCourses);
		}

		private void parseAllMyCourses() {
			for (int i = 0; i < myCourses.size(); i++) {
				publishProgress(i + 1, myCourses.size());
				String html = getHttpFromUrl(myCourses.get(i).getCourseUrl());
				parseCourse(html, i);
				System.out.println(i);
				addCourseToDB(i);
			}
		}

		private void parseCourse(String url, int i) {
			Document doc = Jsoup.parse(url);
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				if (link.toString().contains("/mod/forum/view.php?")
						&& !link.text().contains("ltere Beitr")) {
					myCourses.get(i).saveForum(link.text(),
							link.attr("abs:href"));
				}
				if (link.toString().contains("/grade/report/index.php?")) {
					myCourses.get(i).saveGrades(link.text(),
							link.attr("abs:href"));
					// myCourses.get(i).saveForum(link.text(),
					// link.attr("abs:href"));
				}
			}
		}

		private void addCourseToDB(int i){
			String course = myCourses.get(i).getCourseName() + ";"
					+ myCourses.get(i).getCourseUrl();
			System.out.println(course);
			String[] forums = new String[2];
			for(int j = 0; j < 2; j ++){
				forums[j] = "";
			}
			for(int j = 0; j < myCourses.get(i).getListLinks().size(); j ++){
				forums[j] = myCourses.get(i).getListLinks().get(j).getName()
						+ ";"
						+ myCourses.get(i).getListLinks().get(j).getUrl();
			}
//			String forum1;
//			String forum2;
			String grades;
//			if (myCourses.get(i).getListLinks().get(0) != null) {
//				forum1 = myCourses.get(i).getListLinks().get(0).getName()
//						+ ";"
//						+ myCourses.get(i).getListLinks().get(0).getUrl();
//			} else {
//				forum1 = "";
//			}
//			if (myCourses.get(i).getListLinks().get(1) != null) {
//				forum2 = myCourses.get(i).getListLinks().get(1).getName()
//						+ ";"
//						+ myCourses.get(i).getListLinks().get(1).getUrl();
//			} else {
//				forum2 = "";
//			}
			if (myCourses.get(i).getGrades() != null) {
				grades = myCourses.get(i).getGrades().getName()
						+ ";" + myCourses.get(i).getGrades().getUrl();
			} else {
				grades = "";
			}
//			System.out.println(forum);
//			System.out.println(forum2);
			System.out.println(grades);
			System.out.println(course + forums[0] + forums[1] + grades);
			db.addContentGrips(course, forums[0], forums[1], grades);
		}
		
		private String getHttpFromUrl(String url) {
			String result = "";
			@SuppressWarnings("resource")
			AbstractHttpClient client = new DefaultHttpClient();
			client.setCookieStore(cookieStore);
			HttpPost post = new HttpPost(url);
			try {
				HttpResponse response = client.execute(post);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					// System.out.println(line);
					result += line;
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return result;
		}

	}

	private void parseMyCourses() {
		new GetMyCourses().execute();
	}
}
