package com.ucgen.common.util;

public class StringUtil {

	//Bir Stringe sagdan padding yapar verilen adet padding sonucu olacak karakter sayisidir.
	public static String padRight(String s, int n, String padding){
	    String pad = "";
	    int adet = n-s.length();
	    for(int i = 0; i < adet; i++){
	        pad += padding;
	    }
	    return  s + pad;	
	}
	
	//Bir Stringe soldan padding yapar verilen adet padding sonucu olacak karakter sayisidir.
	public static String padLeft(String s, int n,  String padding) {
	    String pad = "";
	    int adet = n-s.length();
	    for(int i = 0; i < adet ; i++){
	        pad += padding;
	    }
	    return pad + s;
	}
	
	public static String append(String oldString, String appendText, String separator) {
		if (oldString == null) {
			return appendText;
		} else {
			return oldString + separator + appendText;
		}
	}
	
	public static void append(StringBuilder oldStringBuilder, String appendText, String separator) {
		if (oldStringBuilder.length() == 0) {
			oldStringBuilder.append(appendText);
		} else {
			oldStringBuilder.append(separator);
			oldStringBuilder.append(appendText);
		}
	}
	
	public static boolean isEmpty(String value) {
		return (value == null || value.trim().isEmpty());
	}
	
	public static void addNewLine(StringBuilder strBuilder, String newLine) {
		if (newLine != null && !newLine.isEmpty()) {
			if (strBuilder.length() > 0) {
				String lineSeparator = System.getProperty("line.separator");
				strBuilder.append(lineSeparator);
			}
			strBuilder.append(newLine);
		}
	}
	
	public static String addNewLine(String text, String newLine) {
		if (newLine != null && !newLine.isEmpty()) {
			if (text.length() > 0) {
				String lineSeparator = System.getProperty("line.separator");
				text += lineSeparator;
			}
			text += newLine;
		}
		return text;
	}
	
}
