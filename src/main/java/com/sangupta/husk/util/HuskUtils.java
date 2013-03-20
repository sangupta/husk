package com.sangupta.husk.util;

public class HuskUtils {

	public static String rightPad(String str, int max, char pad) {
		StringBuilder builder = new StringBuilder(str);
		builder.setLength(max);
		for(int index = str.length(); index < max; index++) {
			builder.setCharAt(index, pad);
		}
		
		return builder.toString();
	}

}
