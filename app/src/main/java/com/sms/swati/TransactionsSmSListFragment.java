package com.sms.swati;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sms.swati.adapter.SMSDashboardAdapter;
import com.sms.swati.bean.DashboardData;
import com.sms.swati.bean.SMSData;
import com.sms.swati.database.DataBaseHelper;
import com.sms.swati.utils.Utility;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class TransactionsSmSListFragment extends Fragment
		implements
			OnItemClickListener {

	private RecyclerView recyclerView;
	private SMSDashboardAdapter mSMSDasboardAdapter;
	private DataBaseHelper dbHelper;
	ArrayList<SMSData> list = new ArrayList<>();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DataBaseHelper(getActivity());
		dbHelper.createDatabaseFile();
		// dbHelper.insertIntoSMSTable(new SMSData("1", "swati", "swati"));
		if (ContextCompat.checkSelfPermission(getActivity(),
				"android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
			readData(dbHelper.readAllSMS());
		} else {
			final int REQUEST_CODE_ASK_PERMISSIONS = 123;
			ActivityCompat.requestPermissions(getActivity(),
					new String[]{"android.permission.READ_SMS"},
					REQUEST_CODE_ASK_PERMISSIONS);
			readData(dbHelper.readAllSMS());
		}

	}

	private void setupRecyclerView() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(
				getActivity());
		mSMSDasboardAdapter = new SMSDashboardAdapter(getActivity(), this);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(mSMSDasboardAdapter);
		getDashboardData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(
				R.layout.layout_transaction_dashboard, container, false);
		recyclerView = (RecyclerView) fragmentView
				.findViewById(R.id.recycler_dashboard);
		setupRecyclerView();
		return fragmentView;
	}

	/**
	 * Read data from Database and updates the List.
	 **/
	private void readData(Cursor cursor) {
		cursor.moveToFirst();
		dbHelper.deletAllRows();
		while (!cursor.isAfterLast()) {
			String _id = cursor.getString(
					cursor.getColumnIndex(DataBaseHelper.TBL_COL_ID));
			String body = cursor.getString(
					cursor.getColumnIndex(DataBaseHelper.TBL_COL_BODY));
			String sender_id = cursor.getString(
					cursor.getColumnIndex(DataBaseHelper.TBL_COL_SENDER_ID));
			SMSData data = new SMSData(body, sender_id);
			// data.setTransactionType("0");
			data.setDate(cursor.getString(
					cursor.getColumnIndex(DataBaseHelper.TBL_COL_DATE)));
			data.setId(_id);
			// if (data.getSender_id().contains("YESBNK")) {
			data = Utility.parsevalues(data);
			// }

			dbHelper.insertIntoSMSTable(data);
			cursor.moveToNext();
		}
		cursor.close();
	}

	private void getDashboardData() {
		Cursor c = dbHelper.readAllSenderCount();
		ArrayList<DashboardData> list = new ArrayList<DashboardData>();
		list.clear();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			Cursor cursor = dbHelper.readFromSenderId(c.getString(
					c.getColumnIndex(DataBaseHelper.TBL_COL_SERVICE)));

			DashboardData data = new DashboardData();

			data.setSenderName(c.getString(
					c.getColumnIndex(DataBaseHelper.TBL_COL_SENDER_ID)));

			data.setMsgCount(returnCountOfTransactionalMessages(cursor));
			list.add(data);
			// list.add(c.getString(c.getColumnIndexOrThrow("body"))
			// .toString());
			// list.add(c.getString(c.getColumnIndexOrThrow("type"))
			// .toString());
			// }
			// }
			c.moveToNext();
		}
		c.close();
		mSMSDasboardAdapter.setList(list);
	}

	@Override
	public void onItemClick(View view) {
		if (null != view) {
			String senderId = (String) view.getTag();
			SMSListFragment fragment = new SMSListFragment();
			Bundle bundle = new Bundle();
			bundle.putString("sender_id", senderId);
			fragment.setArguments(bundle);
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack if needed
			transaction.replace(R.id.activity_main, fragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

		}
	}

	int returnCountOfTransactionalMessages(Cursor c) {
		int count = 0;
		c.moveToFirst();
		while (!c.isAfterLast()) {
			String transactionType = c
					.getString(c.getColumnIndex(DataBaseHelper.TBL_COL_TYPE));
			if (null != transactionType
					&& transactionType.equalsIgnoreCase("1")) {
				count++;
			}
			c.moveToNext();
		}
		c.close();

		return count;
	}
}