package com.sms.swati.utils;

import com.sms.swati.bean.SMSData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aggarwal.swati on 8/22/17.
 */

public class Utility {

	public static SMSData parsevalues(SMSData smsDto) {
		Pattern regEx = Pattern
				.compile("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)");
		// Find instance of pattern matches
		Matcher m = regEx.matcher(smsDto.getBody());
		if (m.find()) {
			try {
				if (smsDto.getBody().contains("debited")
						|| smsDto.getBody().contains("purchasing")|| smsDto.getBody().contains("Debited")
						|| smsDto.getBody().contains("purchase")
						|| smsDto.getBody().contains("Purchase")
						|| smsDto.getBody().contains("dr")|smsDto.getBody().contains("spent")) {
					smsDto.setTransactionType("1");
				} else if (smsDto.getBody().contains("credited")
						| smsDto.getBody().contains("Credited")
						|| smsDto.getBody().contains("cr")) {
					smsDto.setTransactionType("1");
				} else {
					smsDto.setTransactionType("0");
				}
				// smsDto.setBody(smsDto.getBody());
				// if (!Character.isDigit(smsDto.getSenderid().charAt(0)))

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return smsDto;
	}
}
