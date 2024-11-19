package com.bark.utils;

import java.util.Base64;

public class Base64Codec {

	public static String encode(byte[] src) {
		return Base64.getEncoder().encodeToString(src);
	}

	public static byte[] decode(String src) {
		return Base64.getDecoder().decode(src);
	}
}
