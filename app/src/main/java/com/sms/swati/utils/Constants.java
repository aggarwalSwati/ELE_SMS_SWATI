package com.sms.swati.utils;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 8/24/17.
 */

public class Constants {

	public static ArrayList<String> getSenderIdList() {
		return senderIdList;
	}

	public static void setSenderIdList() {
		senderIdList = new ArrayList<>();
		senderIdList.add("HDFCBK");
		senderIdList.add("YESBNK");
		senderIdList.add("CITIBK");
		senderIdList.add("SBIBNK");
		senderIdList.add("AXISBK");
		senderIdList.add("CorpBk");
		senderIdList.add("OLACBS");
		senderIdList.add("OLACAB");
		senderIdList.add("ICICIB");
		senderIdList.add("UBERIN");
		senderIdList.add("MERUCB");
	}

	public static ArrayList<String> senderIdList = new ArrayList<>();

}
