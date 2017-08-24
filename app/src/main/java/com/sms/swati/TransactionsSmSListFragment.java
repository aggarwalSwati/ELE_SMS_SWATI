package com.sms.swati;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sms.swati.adapter.SMSDashboardAdapter;
import com.sms.swati.bean.DashboardData;
import com.sms.swati.bean.SMSData;
import com.sms.swati.database.DataBaseHelper;

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
	TextView noTextFound;
	ArrayList<SMSData> list = new ArrayList<>();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DataBaseHelper(getActivity());
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
		noTextFound=(TextView)fragmentView.findViewById(R.id.noTextFound);
		setupRecyclerView();
		return fragmentView;
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
					c.getColumnIndex(DataBaseHelper.TBL_COL_SERVICE)));

			data.setMsgCount(returnCountOfTransactionalMessages(cursor));
			list.add(data);
			c.moveToNext();
		}
		c.close();
		mSMSDasboardAdapter.setList(list);
		if (list!=null&&list.size()>0){
			noTextFound.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
		}else {
			noTextFound.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
		}
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