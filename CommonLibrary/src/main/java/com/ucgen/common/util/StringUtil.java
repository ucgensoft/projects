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
	
}
