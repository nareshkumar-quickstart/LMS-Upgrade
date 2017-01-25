package com.softech.vu360.lms.util;
import java.util.StringTokenizer;

/**
 * Provides xor based encoding for strings.
 * @author muhammad.javed
 *
 */

public class XorEncoding extends Encoding {

	/**
	 * Encodes the string.
	 * @param string the plain string that is to be encoded.
	 * @return encoded string against the provided string or empty string if the provided string is null or empty;
	 */
	@Override
	public String encode(String string) {
		if (string == null || string.trim().length() == 0) {
			return "";
		}
		String pubkey = genPublicKey();
		byte[] encodedBytes = processXor(string.getBytes(), privateKey.getBytes());
		encodedBytes = processXor(encodedBytes, pubkey.getBytes());
		return byteToHex(encodedBytes) + keySplitToke + pubkey;
	}

	/**
	 * Decodes the string.
	 * @param string the encoded string.
	 * @throws IllegalArgumentException when its not a valid encoded string.
	 * @return decoded string or empty string when the provided string is empty or null.
	 */
	@Override
	public String decode(String string) throws IllegalArgumentException {
		if (string == null || string.trim().length() == 0) {
			return "";
		}
		StringTokenizer stringTokenizer = new StringTokenizer(string, keySplitToke);
		if (stringTokenizer.countTokens() == 2) {
			String encodedString = stringTokenizer.nextToken();
			byte[] encodedBytes = hexToByte(encodedString);
			String pubKey = stringTokenizer.nextToken();
			byte[] pubKeyBytes = pubKey.getBytes();
			byte[] decodedBytes = processXor(encodedBytes, pubKeyBytes);
			
			decodedBytes = processXor(decodedBytes, privateKey.getBytes());
			
			return new String(decodedBytes);
		} else {
			throw new IllegalArgumentException("Invalid encoded string.");
		}
	}
	
	/**
	 * Performs the XOR operation on the textBytes using the key.
	 * @param textBytes bytes that are to be XORed.
	 * @param key bytes that are used to XOR the textBytes.
	 * @return XORed byte[] of provided textBytes.
	 * @throws IllegalArgumentException when textBytes or key is null or empty.
	 */
	protected static byte[] processXor(byte[] textBytes, byte[] key) {
		if (textBytes == null || textBytes.length == 0) {
			throw new IllegalArgumentException("textBytes can not be null or empty.");
		}
		if (key == null || key.length == 0) {
			throw new IllegalArgumentException("key can not be null or empty.");
		}

		byte[] xored = new byte[textBytes.length];
		int keyIdx = 0;
		for (int idx = 0; idx < textBytes.length; ++idx) {
			int xor = (textBytes[idx] ^ key[keyIdx]);
			++keyIdx;
			if (keyIdx >= key.length) {
				keyIdx = 0;
			}
			xored[idx] = (byte) xor;
		}

		return xored;
	}

}
