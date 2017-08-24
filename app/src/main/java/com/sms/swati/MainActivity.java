package com.sms.swati;

import com.sms.swati.bean.SMSData;
import com.sms.swati.database.DataBaseHelper;
import com.sms.swati.utils.Constants;
import com.sms.swati.utils.Utility;

import android.Manifest.permission;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private TransactionsSmSListFragment listFragment;
	private DataBaseHelper dbHelper;
	final int REQUEST_CODE_ASK_PERMISSIONS = 123;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = MainActivity.this;
		dbHelper = new DataBaseHelper(this);
		dbHelper.createDatabaseFile();
		Constants.setSenderIdList();
		getPermissionToReadUserSMS();


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void addSMSListFragment() {
		new InsertDataTask().execute();
		//		t.start();

	}

	/**
	 * Read data from Database and updates the List.
	 **/
	private void readData(Cursor cursor) {
		cursor.moveToFirst();
		dbHelper.deletAllRows();
		while (!cursor.isAfterLast()) {
			String sender_id =
					cursor.getString(cursor.getColumnIndex(DataBaseHelper.TBL_COL_SENDER_ID));
			sender_id = Utility.parsevaluesForSender(sender_id);
			if (Constants.getSenderIdList().contains(sender_id)) {
				String body = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TBL_COL_BODY));
				SMSData data = new SMSData(body, sender_id);
				data = Utility.parsevalues(data);
				String _id = cursor.getString(cursor.getColumnIndex(DataBaseHelper.TBL_COL_ID));
				data.setService(sender_id);
				data.setDate(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TBL_COL_DATE)));
				data.setId(_id);
				dbHelper.insertIntoSMSTable(data);

			}
			cursor.moveToNext();
		}
		cursor.close();
	}

	private class InsertDataTask extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(context);

		@Override
		protected Void doInBackground(Void... params) {
			readData(dbHelper.readAllSMS());
			return null;
		}

		// can use UI thread here
		protected void onPreExecute() {
			if (null != dialog) {
				this.dialog.setMessage("Fetching data...");
				this.dialog.show();
			}
		}

		// can use UI thread here
		protected void onPostExecute(final Void unused) {
			if (dialog != null && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			listFragment = new TransactionsSmSListFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, listFragment)
					.commit();

		}
	}


	// Called when the user is performing an action which requires the app to read the
	// user's SMS

	public void getPermissionToReadUserSMS() {

		if (ContextCompat.checkSelfPermission(this, permission.READ_SMS) !=
				PackageManager.PERMISSION_GRANTED) {
			// The permission is NOT already granted.
			// Check if the user has been asked about this permission already and denied
			// it. If so, we want to give more explanation about why the permission is needed.
			if (shouldShowRequestPermissionRationale(permission.READ_SMS)) {
				// Show our own UI to explain to the user why we need to read the sms
				// before actually requesting the permission and showing the default UI
			}

			// Fire off an async request to actually get the permission
			// This will show the standard permission request dialog UI
			requestPermissions(new String[]{permission.READ_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
		} else {
			addSMSListFragment();
		}
	}

	// Callback with the request from calling requestPermissions(...)
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		// Make sure it's our original READ_SMS request
		if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				addSMSListFragment();
			} else {
				Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
				finish();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}


