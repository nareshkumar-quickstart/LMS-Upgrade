package com.softech.vu360.lms.util;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Base class for providing the dynamic encoding and decoding of string.
 * @author muhammad.javed
 *
 */
public abstract class Encoding {

	/**
	 * Private key that is used for encoding and decoding.
	 */
	protected String privateKey = "krypt0n_360_sup3rm@n";
	
	/**
	 * Token used for splitting the keys
	 */
	protected String keySplitToke = ":";
	
	/**
	 * Digest algorithm that is used in random public key generation.
	 */
	protected String digestAlgo = "MD5";

	/**
	 * Method that will encode the string.
	 * @param string the plain string that is to be encoded.
	 * @return the encoded string.
	 */
	public abstract String encode(String string);
	
	/**
	 * Method that will decode the string.
	 * @param string that is going to be decoded.
	 * @return the decoded string.
	 * @throws IllegalArgumentException if the provided string is not a valid encoded string.
	 */
	public abstract String decode(String string) throws IllegalArgumentException;
	
	
	/**
	 * Generates the random public key. 
	 * @return random public key.
	 */
	protected String genPublicKey() {
		String key = System.nanoTime() + privateKey + System.nanoTime();
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(digestAlgo);
			byte[] bytes = messageDigest.digest(key.getBytes());
			key = byteToHex(bytes);
		} catch (NoSuchAlgorithmException e) {
			key = byteToHex(key.getBytes());
		}
		return key;
	}

	/**
	 * Converts byte array to hex based string.
	 * @param bytes that are to be represented in hex string.
	 * @return hex string against the provided bytes.
	 */
	protected static String byteToHex(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException("given bytes are null or empty.");
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (int idx = 0; idx < bytes.length; ++idx) {
			stringBuffer.append(Integer.toString((bytes[idx] & 0xFF) + 0x100, 16).substring(1));
		}
		return stringBuffer.toString();
	}

	/**
	 * Converts hex based string to bytes.
	 * @param hexString hex based string.
	 * @return byte[] that is represented by hexString.
	 */
	protected static byte[] hexToByte(String hexString) {
		if (hexString == null || hexString.length() == 0) {
			throw new IllegalArgumentException("hexString can not be null or empty.");
		}

		return new BigInteger(hexString, 16).toByteArray();
	}

}
