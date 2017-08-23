package com.sms.swati.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sms.swati.R;
import com.sms.swati.bean.SMSData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class SMSListAdapter
		extends
			RecyclerView.Adapter<SMSListAdapter.CustomViewHolder> {

	private ArrayList<SMSData> smsList = new ArrayList<>();

	public SMSListAdapter(Context context) {

	}

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.layout_item_sms, parent, false);

		return new CustomViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(CustomViewHolder holder, int position) {
		SMSData data = smsList.get(position);
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(Long.valueOf(data.getDate()));
		holder.dateText.setText(formatter.format(calendar.getTime()));
		if (null != data.getTransactionType()
				&& data.getTransactionType().equalsIgnoreCase("1"))
			holder.textView1.setText(data.getBody());

	}

	@Override
	public int getItemCount() {
		return smsList.size();
	}

	public class CustomViewHolder extends RecyclerView.ViewHolder {

		public TextView textView1, dateText;

		public CustomViewHolder(View itemView) {
			super(itemView);
			textView1 = (TextView) itemView.findViewById(R.id.text_post_title);
			dateText = (TextView) itemView.findViewById(R.id.text_date);

		}

		public void setData(Cursor c) {
			textView1.setText(c.getString(c.getColumnIndex("body")));
		}
	}

	public void setList(ArrayList<SMSData> moviesList) {
		this.smsList = moviesList;
	}
}
