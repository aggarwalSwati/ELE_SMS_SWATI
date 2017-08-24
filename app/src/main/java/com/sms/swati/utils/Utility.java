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
		Pattern cabsregX = Pattern.compile("[a-zA-Z]{3}: [0-9]{4}");
		Pattern krnAUTOregX = Pattern.compile("[a-zA-Z]{3}[0-9]{6}");
		Pattern crnOlaregX = Pattern.compile("[a-zA-Z]{3}[0-9]{7}");
		Pattern regEx = Pattern.compile(
				"(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)");
		Matcher m = regEx.matcher(smsDto.getBody());
		Matcher crnOLAMatcher = crnOlaregX.matcher(smsDto.getBody());
		Matcher cabMatcher = cabsregX.matcher(smsDto.getBody());
		Matcher autoMatcher = krnAUTOregX.matcher(smsDto.getBody());
		// Find instance of pattern matches
		if (smsDto.getService().equalsIgnoreCase("BANKS")) {
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
		} else if (smsDto.getService().equalsIgnoreCase("CABS")) {
			if (m.find()) {
				if (smsDto.getBody().contains("debited")
						|| smsDto.getBody().contains("credited")) {
					smsDto.setTransactionType("1");
				}
			} else if (smsDto.getBody()
					.contains("verification code for Olacabs")
					|| smsDto.getBody()
							.contains("Ola Money has been deducted")) {
				smsDto.setTransactionType("1");
			} else if (cabMatcher.find() || autoMatcher.find()
					|| crnOLAMatcher.find()) {
				smsDto.setTransactionType("1");
			} else {
				smsDto.setTransactionType("0");
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
