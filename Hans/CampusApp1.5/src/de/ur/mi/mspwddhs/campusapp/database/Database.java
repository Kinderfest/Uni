package de.ur.mi.mspwddhs.campusapp.database;

import java.util.ArrayList;

import de.ur.mi.mspwddhs.campusapp.grips.Course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {

	private static final String DATABASE_NAME = "campus.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_NAME_MENSA = "Mensa";
	private static final String TABLE_NAME_GRIPS = "Grips";
	private static final String TABLE_NAME_MAIL = "mail";
	private static final String TABLE_NAME_LOGIN = "login";

	private static final String ID = "_id";
	// Mensa Table
	private static String KEY_DATUM = "datum";
	private static String KEY_TAG = "tag";
	private static String KEY_WARENGRUPPE = "warengruppe";
	private static String KEY_NAME = "name";
	private static String KEY_KENNZ = "kennz";
	private static String KEY_PREIS = "preis";
	// Grips Table
	private static String KEY_KURS = "kurs";
	private static String KEY_FORUM1 = "forum1";
	private static String KEY_FORUM2 = "forum2";
	private static String KEY_BEWERTUNG = "bewertung";
	// Login Table
	private static String KEY_USER = "user";
	private static String KEY_PASS = "pass";
	private static String KEY_EMAIL = "email";

	private ToDoDBOpenHelper dbHelper;

	private SQLiteDatabase db;

	public Database(Context context) {
		dbHelper = new ToDoDBOpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}

	public void addContentMensa(String datum, String tag, String warengruppe,
			String name, String kennz, String preis) {
		ContentValues values = new ContentValues();
		values.put(KEY_DATUM, datum);
		values.put(KEY_TAG, tag);
		values.put(KEY_WARENGRUPPE, warengruppe);
		values.put(KEY_NAME, name);
		values.put(KEY_KENNZ, kennz);
		values.put(KEY_PREIS, preis);

		db.insert(TABLE_NAME_MENSA, null, values);
	}

	public void addContentGrips(String kurs, String forum1, String forum2,
			String bewertung) {
		ContentValues values = new ContentValues();
		values.put(KEY_KURS, kurs);
		values.put(KEY_FORUM1, forum1);
		values.put(KEY_FORUM2, forum2);
		values.put(KEY_BEWERTUNG, bewertung);

		db.insert(TABLE_NAME_GRIPS, null, values);
	}

	public void saveLoginData(String user, String pass, String email) {
		ContentValues values = new ContentValues();
		values.put(KEY_USER, user);
		values.put(KEY_PASS, pass);
		values.put(KEY_EMAIL, email);

		db.insert(TABLE_NAME_LOGIN, null, values);
	}

	public boolean getCount(String getDatum) {
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME_MENSA
				+ " WHERE " + KEY_DATUM + "=?",
				new String[] { String.valueOf(getDatum) });
		cursor.moveToFirst();
		if (cursor.getInt(0) > 0) {
			return true;
		}
		return false;
	}

	public boolean isGripsTableEmpty() {
		boolean flag;
		String quString = "select exists(select 1 from " + TABLE_NAME_GRIPS
				+ ");";

		Cursor cursor = db.rawQuery(quString, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		if (count == 1) {
			flag = false;
		} else {
			flag = true;
		}
		cursor.close();

		return flag;
	}

	public boolean isLoginTableEmpty() {
		boolean flag;
		String quString = "select exists(select 1 from " + TABLE_NAME_LOGIN
				+ ");";

		Cursor cursor = db.rawQuery(quString, null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		if (count == 1) {
			flag = false;
		} else {
			flag = true;
		}
		cursor.close();

		return flag;
	}

	public ArrayList<String> getProdcuts(String getDatum, String getWarengruppe)
	{
		ArrayList<String> content = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_NAME_MENSA, new String[] { KEY_NAME, KEY_PREIS, KEY_KENNZ }
				,KEY_DATUM + "=? AND " + KEY_WARENGRUPPE + "=?", 
				new String[]{String.valueOf(getDatum),String.valueOf(getWarengruppe)}, null, null, null);
		if(cursor.moveToFirst())
		{
			do{
				String name = cursor.getString(0);
				String preis = cursor.getString(1);
				String kennz = cursor.getString(2);
				
				
				if (kennz == null) {
					kennz = "";
				}
				
				content.add(name+";"+preis +";"+kennz);
				
			}while(cursor.moveToNext());
		}
		return content;
	}

	public ArrayList<String> getLoginData() {
		ArrayList<String> content = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_NAME_LOGIN, new String[] { KEY_USER,
				KEY_PASS, KEY_EMAIL }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				String user = cursor.getString(0);
				String pass = cursor.getString(1);
				String email = cursor.getString(2);

				content.add(user);
				content.add(pass);
				content.add(email);
			} while (cursor.moveToNext());
		}
		return content;
	}

	public ArrayList<String> getContentMensa(String getDatum) {
		ArrayList<String> content = new ArrayList<String>();
		Cursor cursor = db.query(TABLE_NAME_MENSA, new String[] { KEY_DATUM,
				KEY_TAG, KEY_WARENGRUPPE, KEY_NAME, KEY_KENNZ, KEY_PREIS },
				KEY_DATUM + "=?", new String[] { String.valueOf(getDatum) },
				null, null, null);

		if (cursor.moveToFirst()) {
			do {
				String datum = cursor.getString(0);
				String tag = cursor.getString(1);
				String warengruppe = cursor.getString(2);
				String name = cursor.getString(3);
				String kennz = cursor.getString(4);
				String preis = cursor.getString(5);

				content.add(datum + ";" + tag + ";" + warengruppe + ";" + name
						+ ";" + kennz + ";" + preis);

			} while (cursor.moveToNext());
		}
		return content;
	}

	public ArrayList<Course> getContentGrips() {
		ArrayList<Course> content = new ArrayList<Course>();
		Cursor cursor = db.query(TABLE_NAME_GRIPS, new String[] { KEY_KURS,
				KEY_FORUM1, KEY_FORUM2, KEY_BEWERTUNG }, null, null, null,
				null, null);

		if (cursor.moveToFirst()) {
			do {
				String kurs = cursor.getString(0);
				String forum1 = cursor.getString(1);
				String forum2 = cursor.getString(2);
				String bewertung = cursor.getString(3);

				String[] course = kurs.split(";");
				Course c = new Course(course[0], course[1]);
				if (!forum1.isEmpty()) {
					String[] forum1A = forum1.split(";");
					c.saveForum(forum1A[0], forum1A[1]);
				}
				if (!forum2.isEmpty()) {
					String[] forum2A = forum2.split(";");
					c.saveForum(forum2A[0], forum2A[1]);
				}
				if (!bewertung.isEmpty()) {
					String[] grades = bewertung.split(";");
					c.saveForum(grades[0], grades[1]);
				}

				content.add(c);

			} while (cursor.moveToNext());
		}
		return content;
	}

	public int getCountOfProducts(String getDatum, String getWarengruppe) {
		Cursor cursor = db.rawQuery(
				"SELECT COUNT(*) FROM " + TABLE_NAME_MENSA + " WHERE "
						+ KEY_DATUM + "=? AND " + KEY_WARENGRUPPE + "=?",
				new String[] { String.valueOf(getDatum),
						String.valueOf(getWarengruppe) });
		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	public void clearDatabaseMensa() {
		db.delete(TABLE_NAME_MENSA, null, null);
	}

	public void clearDatabaseLogin() {
		db.delete(TABLE_NAME_LOGIN, null, null);
	}
	
	public void clearDatabaseGrips(){
		db.delete(TABLE_NAME_GRIPS, null, null);
	}

	public void clearSingleRowGrips(String getKurs) {
		db.delete(TABLE_NAME_GRIPS, KEY_KURS + "=?",
				new String[] { String.valueOf(getKurs) });
	}

	private class ToDoDBOpenHelper extends SQLiteOpenHelper {
		private final String DATABASE_CREATE_MENSA = "create table "
				+ TABLE_NAME_MENSA + " (" + ID
				+ " integer primary key autoincrement, " + KEY_DATUM
				+ " text not null, " + KEY_TAG + " text, " + KEY_WARENGRUPPE
				+ " text, " + KEY_NAME + " text, " + KEY_KENNZ + " text, "
				+ KEY_PREIS + " text);";
		private final String DATABASE_CREATE_GRIPS = "create table "
				+ TABLE_NAME_GRIPS + " (" + ID
				+ " integer primary key autoincrement, " + KEY_KURS
				+ " text not null, " + KEY_FORUM1 + " text, " + KEY_FORUM2
				+ " text, " + KEY_BEWERTUNG + " text);";
		private final String DATABASE_CREATE_LOGIN = "create table "
				+ TABLE_NAME_LOGIN + " (" + ID
				+ " integer primary key autoincrement, " + KEY_USER + " text, "
				+ KEY_PASS + " text, " + KEY_EMAIL + " text);";

		public ToDoDBOpenHelper(Context c, String dbname,
				SQLiteDatabase.CursorFactory factory, int version) {
			super(c, dbname, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_MENSA);
			db.execSQL(DATABASE_CREATE_GRIPS);
			db.execSQL(DATABASE_CREATE_LOGIN);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

}