package com.sms.swati.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sms.swati.OnItemClickListener;
import com.sms.swati.R;
import com.sms.swati.bean.DashboardData;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class SMSDashboardAdapter extends RecyclerView.Adapter<SMSDashboardAdapter.CustomViewHolder>
		implements OnClickListener {

	private ArrayList<DashboardData> smsList = new ArrayList<>();
	private OnItemClickListener mItemClickListener;

	public SMSDashboardAdapter(Context context, OnItemClickListener listener) {
		mItemClickListener = listener;
	}

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.layout_item_dashboard, parent, false);

		return new CustomViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(CustomViewHolder holder, int position) {
		DashboardData data = smsList.get(position);
		holder.countText.setText(String.valueOf(data.getMsgCount()));
		holder.itemView.setTag(data.getSenderName());
		holder.transactionName.setText(data.getSenderName());
		holder.card_view.setOnClickListener(this);

	}

	@Override
	public int getItemCount() {
		return smsList.size();
	}

	@Override
	public void onClick(View v) {
		mItemClickListener.onItemClick(v); //OnItemClickListener mItemClickListener;

	}

	public class CustomViewHolder extends RecyclerView.ViewHolder {

		public TextView transactionName, countText;
		CardView card_view;

		public CustomViewHolder(View itemView) {
			super(itemView);
			transactionName = (TextView) itemView.findViewById(R.id.text_sender_name);
			countText = (TextView) itemView.findViewById(R.id.text_msg_count);
			card_view = (CardView) itemView.findViewById(R.id.card_view);
		}

	}

	public void setList(ArrayList<DashboardData> datalist) {
		this.smsList = datalist;
	}
}
