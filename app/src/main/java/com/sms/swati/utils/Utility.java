package com.sms.swati.utils;

import com.sms.swati.bean.SMSData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class Utility {

	public static SMSData parsevalues(SMSData smsDto) {
		// Pattern regEx =
		// Pattern.compile("[a-zA-Z0-9]{2}-[a-zA-Z0-9]{6}");
		Pattern regEx = Pattern.compile(
				"(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)");
		// Find instance of pattern matches
		Matcher m = regEx.matcher(smsDto.getBody());
		if (m.find()) {
			try {
				if (smsDto.getBody().contains("debited from")
						|| smsDto.getBody().contains("purchasing")
						|| smsDto.getBody().contains("debited")
						|| smsDto.getBody().contains("Debited")
						|| smsDto.getBody().contains("Purchase of")
						|| smsDto.getBody().contains("withdrawn via atm")
						|| smsDto.getBody().contains("withdrawn via ATM")
								| smsDto.getBody().contains("spent on")) {
					smsDto.setTransactionType("1");
				} else if (smsDto.getBody().contains("credited")
						|| smsDto.getBody().contains("Credited")) {
					smsDto.setTransactionType("1");
				} else {
					smsDto.setTransactionType("0");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return smsDto;
	}

	public static String parsevaluesForSender(String senderId) {

		String[] separated = senderId.split("-");
		if (null != separated && separated.length > 1) {
			String second = separated[1];
			return second;
			// senderId.setService(second);
		}
		return senderId;

	}

}
