package com.sms.swati.utils;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 8/24/17.
 */

public class Constants {

	public static ArrayList<String> getCabSenderList() {
		return cabSenderList;
	}

	private static ArrayList<String> cabSenderList;

	public static ArrayList<String> getSenderIdList() {
		return senderIdList;
	}

	public static void setSenderIdList() {
		senderIdList = new ArrayList<>();
		senderIdList.add("HDFCBK");
		senderIdList.add("YESBNK");
		senderIdList.add("CITIBK");
		senderIdList.add("SBIBNK");
		senderIdList.add("AxisBk");
		senderIdList.add("CorpBk");

		senderIdList.add("ICICIB");

	}

	public static void setCabSenderList() {
		cabSenderList = new ArrayList<>();
		cabSenderList.add("UBERIN");
		cabSenderList.add("MERUCB");
		cabSenderList.add("OLACAB");
	}

	public static ArrayList<String> senderIdList = new ArrayList<>();

}
