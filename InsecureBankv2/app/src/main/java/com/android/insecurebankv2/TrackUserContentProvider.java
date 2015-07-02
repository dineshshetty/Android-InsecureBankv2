package com.android.insecurebankv2;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/*
The class that keeps a track of all the logged in users' on the device
@author Dinesh Shetty
*/
public class TrackUserContentProvider extends ContentProvider {


	//   This content provider vuln is a modified code from www.androidpentesting.com

	static final String PROVIDER_NAME = "com.android.insecurebankv2.TrackUserContentProvider";
	//   The Content provider that handles all the tracked user history
	static final String URL = "content://" + PROVIDER_NAME + "/trackerusers";
	static final Uri CONTENT_URI = Uri.parse(URL);
	static final String name = "name";
	static final int uriCode = 1;
	static final UriMatcher uriMatcher;
	private static HashMap < String, String > values;
	private SQLiteDatabase db;
	static final String DATABASE_NAME = "mydb";
	static final String TABLE_NAME = "names";
	static final int DATABASE_VERSION = 1;
	static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT NOT NULL);";

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "trackerusers", uriCode);
		uriMatcher.addURI(PROVIDER_NAME, "trackerusers/*", uriCode);
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		switch (uriMatcher.match(uri)) {
			case uriCode:
				count = db.delete(TABLE_NAME, selection, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
			case uriCode:
				return "vnd.android.cursor.dir/u";

			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long rowID = db.insert(TABLE_NAME, "", values);
		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			return true;
		}
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
	String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);

		switch (uriMatcher.match(uri)) {
			case uriCode:
				qb.setProjectionMap(values);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		if (sortOrder == null || sortOrder == "") {
			sortOrder = name;
		}
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
		null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
	String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		switch (uriMatcher.match(uri)) {
			case uriCode:
				count = db.update(TABLE_NAME, values, selection, selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_DB_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
}