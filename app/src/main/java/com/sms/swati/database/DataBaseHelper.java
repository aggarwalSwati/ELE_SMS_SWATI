package com.sms.swati.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.sms.swati.bean.SMSData;
import com.sms.swati.broadcast.SMSRequestProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sms.db";
	public static String TBL_COL_DATE = "date";
	Context mContext;
	public static String TBL_SMS = "sms_new";
	public static String TBL_COL_ID = "_id";
	public static String TBL_COL_SENDER_ID = "address";
	public static String TBL_COL_BODY = "body";
	public static String TBL_COL_TYPE = "type";
	public static String TBL_COL_SERVICE="service_center";

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL("DROP TABLE IF EXISTS " + TableItems.TABLE_SMS);
		db.execSQL(TableItems.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TableItems.TABLE_SMS);
		db.execSQL(TableItems.CREATE_TABLE);
	}

	private void isDirectoryPresent(String db_path) {
		// create "databases" directory if not in existence in
		// data/data/package_name/databases/
		File file = new File(
				db_path.substring(0, db_path.indexOf("/" + DATABASE_NAME)));

		// check if databases folder exists or not.
		if (!file.isDirectory()) {
			file.mkdir();
		}
	}

	public void createDatabaseFile() {

		// data/data/package_name/databases/db_name.db
		String db_path = mContext.getDatabasePath(DataBaseHelper.DATABASE_NAME)
				.toString();
		isDirectoryPresent(db_path);

		File file = new File(db_path);
		Log.d(getClass().getSimpleName(), file.getAbsolutePath());
		if (file.exists()) {
			Log.d(getClass().getSimpleName(), "File already exists");
		} else {
			copyDatabase(file);
		}
	}

	private void copyDatabase(File file) {
		try {
			try {
				file.createNewFile(); // create new file if it is not in
				// existence
			} catch (IOException e) {
				e.printStackTrace();
			}
			InputStream is = mContext.getAssets().open(DATABASE_NAME);
			OutputStream write = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				write.write(buffer, 0, length);
			}
			is.close();
			write.close();
			Log.d(getClass().getSimpleName(),
					"File does not exists & Newly created");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Uri getSMSTableUri() {
		Uri contentUri = Uri.withAppendedPath(SMSRequestProvider.CONTENT_URI,
				DataBaseHelper.TBL_SMS);
		return contentUri;
	}

	public void insertIntoSMSTable(SMSData sms) {
		// db.execSQL("insert into "+TBL_LOGIN+"
		// ("+TBL_COL_UNAME+","+TBL_COL_PASSWORD+")values('"+username+"','"+password+"')");

		ContentValues values = new ContentValues();
		// values.put("key", Integer.MAX_VALUE);
		values.put(DataBaseHelper.TBL_COL_ID, sms.getId());
		values.put(DataBaseHelper.TBL_COL_SENDER_ID, sms.getSender_id());
		values.put(DataBaseHelper.TBL_COL_BODY, sms.getBody());
		if (null != sms.getTransactionType()) {
			values.put(DataBaseHelper.TBL_COL_TYPE, sms.getTransactionType());
		} else {
			values.put(DataBaseHelper.TBL_COL_TYPE, "0");
		}
		 values.put(DataBaseHelper.TBL_COL_DATE, sms.getDate());
		values.put(DataBaseHelper.TBL_COL_SERVICE, sms.getService());
		mContext.getContentResolver().insert(getSMSTableUri(), values);

	}

	public void deletAllRows() {
		mContext.getContentResolver().delete(getSMSTableUri(), null, null);

	}

	public Cursor readAllSMS() {

		Uri sms = Uri.parse("content://sms/inbox");
		// String[] projection = {TBL_COL_BODY, TBL_COL_SENDER_ID, TBL_COL_DATE,
		// TBL_COL_TYPE};
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		return mContext.getContentResolver().query(sms, null, selection,
				selectionArgs, sortOrder);

		// return db.rawQuery("select * from "+TBL_LOGIN, null);
	}

	public Cursor readAllSenderCount() {
		String sortOrder = null;
		String[] projection = new String[]{"DISTINCT " + TBL_COL_SENDER_ID};
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(
				"select * from " + TBL_SMS
						+ " WHERE (type=1) AND (service_center IS NOT NULL) GROUP BY (service_center)",
				null);

		return c;

		// return database.rawQuery("select * from "+TBL_SMS +" WHERE (type=1)
		// AND (address IS NOT NULL) GROUP BY (address)", null);
	}

	public Cursor readFromSenderId(String senderid) {

		String[] projection = {TBL_COL_BODY, TBL_COL_TYPE, TBL_COL_SENDER_ID, TBL_COL_DATE};
		String selection = TBL_COL_SENDER_ID + " LIKE ?";
		String[] selectionArgs = {"%" + senderid + "%"};
		String sortOrder = null;

		return mContext.getContentResolver().query(getSMSTableUri(), projection,
				selection, selectionArgs, sortOrder);

		// return db.rawQuery("select * from "+TBL_LOGIN+" where
		// "+TBL_COL_UNAME+"=?", new String[]{username});
	}

}
