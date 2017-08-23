package com.sms.swati.broadcast;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.sms.swati.database.DataBaseHelper;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class SMSRequestProvider extends ContentProvider {


	public static final Uri CONTENT_URI = Uri.parse("content://" + "MyContentProviderAuthority");
	DataBaseHelper mHelper;

	@Override
	public boolean onCreate() {
		mHelper = new DataBaseHelper(getContext());
		return true;
	}

	@Override
	public int delete(Uri uri, String where, String[] args) {
		String table = getTableName(uri);
		SQLiteDatabase dataBase = mHelper.getWritableDatabase();
		return dataBase.delete(table, where, args);
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		String table = getTableName(uri);
		SQLiteDatabase database = mHelper.getWritableDatabase();
		long value = database.insert(table, null, initialValues);
		return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String table = getTableName(uri);
		SQLiteDatabase database = mHelper.getReadableDatabase();
		Cursor cursor = database.query(table, projection, selection,
				selectionArgs, null, null, sortOrder);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		String table = getTableName(uri);
		SQLiteDatabase database = mHelper.getWritableDatabase();
		return database.update(table, values, whereClause, whereArgs);
	}

	public static String getTableName(Uri uri) {
		String value = uri.getPath();
		value = value.replace("/", "");// we need to remove '/'
		return value;
	}

}
