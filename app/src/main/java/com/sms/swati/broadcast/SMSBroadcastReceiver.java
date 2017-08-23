package com.sms.swati.broadcast;

import java.util.Calendar;

import com.sms.swati.database.DataBaseHelper;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {

	final SmsManager sms = SmsManager.getDefault();
	@Override
	public void onReceive(Context context, Intent intent) {
		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();

					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();

					Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
					saveMessagesToDB(currentMessage, context);

					// Show Alert
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context,
							"senderNum: "+ senderNum + ", message: " + message, duration);
					toast.show();

				} // end for loop
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" +e);

		}

	}

	private void saveMessagesToDB( SmsMessage sms, Context context )
	{
		DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

		SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

		String mydate =java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		// Create SMS row
		ContentValues values = new ContentValues();

		values.put("address", sms.getOriginatingAddress().toString() );
		values.put("date", mydate);
		values.put("body", sms.getMessageBody().toString());
		db.insert("datatable", null, values);

		db.close();

	}
}
