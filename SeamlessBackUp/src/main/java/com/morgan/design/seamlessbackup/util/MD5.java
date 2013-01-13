package com.morgan.design.seamlessbackup.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {

	@Deprecated
	public static String hash(final String data) {
		try {
			// Create MD5 Hash
			final MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(data.getBytes());
			final byte messageDigest[] = digest.digest();

			// Create Hex String
			final StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();

		}
		catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
			// TODO report error & log!
		}
		return "";
	}

	public static String hash(final File mFile) {
		try {
			final FileInputStream fis = new FileInputStream(mFile);
			final String md5String = new String(Hex.encodeHex(DigestUtils.md5(fis)));
			return md5String;
		}
		catch (final Exception e) {
			e.printStackTrace();
			// TODO report error & log!
		}
		return "";
	}

}
