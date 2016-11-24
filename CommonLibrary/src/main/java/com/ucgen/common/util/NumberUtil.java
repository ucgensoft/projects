package com.ucgen.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class NumberUtil {

	public static char DEFAULT_GROUP_SEPERATOR = '.';
	public static char DEFAULT_DECIMAL_SEPERATOR = ',';

	public static boolean isInteger(String value) {
		if (value != null && !value.isEmpty()) {
			return value.matches("0|([\\+-]?([1-9])|[1-9][0-9]+)");
		} else {
			return false;
		}
	}

	public static boolean isPositiveInteger(String value) {
		if (value != null && !value.isEmpty()) {
			return value.matches("([1-9])|[1-9][0-9]+");
		} else {
			return false;
		}
	}

	public static String format(BigDecimal value, char groupSeperator,
			char decimalSeperator, int decimalDigit) {
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols
				.getInstance();
		decimalFormatSymbols.setDecimalSeparator(decimalSeperator);
		decimalFormatSymbols.setGroupingSeparator(groupSeperator);

		DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat
				.getInstance(Locale.ENGLISH);

		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		decimalFormat.setMaximumFractionDigits(2);

		return decimalFormat.format(value);
	}
	
	public static String toCurrencyTextTR(BigDecimal number, String exactPartString, String decimalPartString) {
		return NumberToText.convertCurreny(number, exactPartString, decimalPartString);
	}

}

class NumberToText {

	public static final String[] birler = { "", "BİR", "IKI", "ÜÇ", "DÖRT",
			"BEŞ", "ALTI", "YEDİ", "SEKİZ", "DOKUZ" };
	public static final String[] onlar = { "", "ON", "YİRMİ", "OTUZ", "KIRK",
			"ELLİ", "ALTMIŞ", "YETMİŞ", "SEKSEN", "DOKSAN" };
	public static final String[] gruplar = { "", "BİN", "MİLYON", "MİLYAR",
			"TRİLYON", "KATRİLYON" };

	public static String convertCurreny(BigDecimal number,
			String exactPartString, String decimalPartString) {
		List<String> exactPartWordList = new ArrayList<String>();
		List<String> decimalPartWordList = new ArrayList<String>();

		String txtNumber = NumberUtil.format(number,
				NumberUtil.DEFAULT_GROUP_SEPERATOR,
				NumberUtil.DEFAULT_DECIMAL_SEPERATOR, 2);

		String[] numberParts = txtNumber.split(String
				.valueOf(NumberUtil.DEFAULT_DECIMAL_SEPERATOR));
		String exactPart = numberParts[0];
		String decimalPart = (numberParts.length > 1 ? numberParts[1] : "0");

		if (exactPart.replace("0", "").trim().length() > 0) {
			String[] exactGroups = exactPart.split("\\"
					+ String.valueOf(NumberUtil.DEFAULT_GROUP_SEPERATOR));
			for (int i = 0; i < exactGroups.length; i++) {
				String group = exactGroups[i];

				String threeDigitGroup = StringUtils.leftPad(group, 3, '0');

				int yuzlerBasamagi = Integer.valueOf(threeDigitGroup.substring(
						0, 1));
				int onlarBasamagi = Integer.valueOf(threeDigitGroup.substring(
						1, 2));
				int birlerBasamagi = Integer.valueOf(threeDigitGroup
						.substring(2));

				String grup = gruplar[exactGroups.length - i - 1];

				if (!grup.equalsIgnoreCase("BIN")
						|| Integer.valueOf(threeDigitGroup).intValue() != 1) {
					if (yuzlerBasamagi != 0) {
						if (yuzlerBasamagi != 1) {
							exactPartWordList.add(birler[yuzlerBasamagi]);
						}
						exactPartWordList.add("YÜZ");
					}
					if (onlarBasamagi != 0) {
						exactPartWordList.add(onlar[onlarBasamagi]);
					}
					if (birlerBasamagi != 0) {
						exactPartWordList.add(birler[birlerBasamagi]);
					}
				}
				if (!grup.trim().isEmpty()) {
					exactPartWordList.add(grup);
				}
			}
			exactPartWordList.add(exactPartString);
		}

		if (!decimalPart.replace("0", "").trim().isEmpty()) {
			decimalPart = StringUtils.rightPad(decimalPart, 2, '0');

			int onlarBasamagi = Integer.valueOf(decimalPart.substring(0, 1));
			int birlerBasamagi = Integer.valueOf(decimalPart.substring(1));

			if (onlarBasamagi != 0) {
				decimalPartWordList.add(onlar[onlarBasamagi]);
			}
			if (birlerBasamagi != 0) {
				decimalPartWordList.add(birler[birlerBasamagi]);
			}

			decimalPartWordList.add(decimalPartString);
		}

		String resultText = "";
		for (String word : exactPartWordList) {
			resultText += (resultText.length() == 0 ? word : "-" + word);
		}
		if (decimalPartWordList.size() > 0) {
			String decimalText = "";
			for (String word : decimalPartWordList) {
				decimalText += (decimalText.isEmpty() ? word : "-" + word);
			}
			resultText += " " + decimalText;
		}

		return resultText;
	}

}
