package com.sms.swati;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sms.swati.adapter.SMSListAdapter;
import com.sms.swati.bean.SMSData;
import com.sms.swati.database.DataBaseHelper;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class SMSListFragment extends Fragment {

	private RecyclerView recyclerView;
	private SMSListAdapter mSMSListAdapter;
	String senderId;
	private DataBaseHelper dbHelper;


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DataBaseHelper(getActivity());
		dbHelper.createDatabaseFile();
		Bundle bundle = getArguments();
		if (null != bundle) {
			senderId = bundle.getString("sender_id");
		}

	}

	private void setupRecyclerView() {
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		mSMSListAdapter = new SMSListAdapter(getActivity());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(mSMSListAdapter);
		readData(senderId);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.layout_sms_list, container, false);
		recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_sms);
		setupRecyclerView();
		return fragmentView;
	}

	/**
	 * Read data from Database and updates the List.
	 **/
	private void readData(String senderid) {
		Cursor c = dbHelper.readFromSenderId(senderid);
		ArrayList<SMSData> list = new ArrayList<SMSData>();
		list.clear();
		c.moveToFirst();
		while (!c.isAfterLast()) {
			String transactionType = c.getString(c.getColumnIndex(DataBaseHelper.TBL_COL_TYPE));
			if (null != transactionType && transactionType.equalsIgnoreCase("1")) {
				SMSData data = new SMSData(c.getString(c.getColumnIndexOrThrow("body")).toString(),
						c.getString(c.getColumnIndexOrThrow(DataBaseHelper.TBL_COL_SENDER_ID))
								.toString());
				data.setTransactionType(transactionType);
//				data.setId(c.getString(c.getColumnIndex(DataBaseHelper.TBL_COL_ID)));
				data.setDate(c.getString(c.getColumnIndexOrThrow(DataBaseHelper.TBL_COL_DATE)));
				list.add(data);
			}
			// }
			c.moveToNext();
		}
		c.close();
		mSMSListAdapter.setList(list);
	}

}
