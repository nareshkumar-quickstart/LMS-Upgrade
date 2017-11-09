package com.softech.vu360.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 *
 * <p>Title: Security Utility Class </p>
 * <p>Description: This class is designed to handle the security issues like encryption , decryption. </p>
 *
 * @author Sunirmal K.
 * @version 1.0
 */
public class SecurityUtil {

	private static BASE64Encoder encoder = new BASE64Encoder();
	private static BASE64Decoder decoder = new BASE64Decoder();
	private Cipher encryptCipher = null;
	private Cipher decryptCipher;
	private static Map<String, SecurityUtil> instanceMap = new HashMap<String, SecurityUtil>();
	private static final String SECURITY_ALGORITHM = "AES";

	/**
	 * Create a SecurityUtil class for a specific Key
	 * @param keyBytes
	 * @throws BusinessException
	 */
	public SecurityUtil(byte[] keyBytes) 
	{
		try {
			if( keyBytes == null || keyBytes.length == 0 ) {
				keyBytes = this.getDefaultKeyBytes();
			}	
			SecretKey key = new SecretKeySpec(keyBytes, SECURITY_ALGORITHM);
			this.encryptCipher = Cipher.getInstance(SECURITY_ALGORITHM);
			this.encryptCipher.init(Cipher.ENCRYPT_MODE, key);
			this.decryptCipher = Cipher.getInstance(SECURITY_ALGORITHM);
			this.decryptCipher.init(Cipher.DECRYPT_MODE, key);
		} catch( Exception e ) {
			// caught the exception
		}
	}

	/**
	 * It returns the default Security Util
	 * @return
	 * @throws BusinessException
	 */
	public static SecurityUtil getInstance() throws Exception {
		return getInstance(getDefaultKeyBytes());
	}

	/**
	 * This method returns singleton instance per key
	 * @param keyBytes
	 * @return
	 * @throws BusinessException
	 */
	synchronized public static SecurityUtil getInstance(byte[] keyBytes) throws Exception{
		String keyStr = new String(keyBytes);
		SecurityUtil sUtil = instanceMap.get(keyStr);
		if(sUtil == null){
			sUtil = new SecurityUtil(keyBytes);
			instanceMap.put(keyStr, sUtil);
		}
		return sUtil;
	}

	public static String encodeReal64bit (String input){
		return encoder.encode(input.getBytes());
	}
	/**
	 * It generates a hard coded 128-bit Key
	 * @return
	 */
	public static byte[] getDefaultKeyBytes(){
		return new byte[]{0x00,0x55,0x68,0x73,0x1f,0x0d,0x3c,0x32,0x59,0x7d,0x3e,0x4a,0x5c,0x68,0x3e,0x70};
	}

	/**
	 * Encrypt to a Byte Array from a Byte Array
	 * @param data
	 * @return
	 * @throws BusinessException
	 */
	synchronized public byte[] encrypt(byte[] data) throws InvalidKeyException, BadPaddingException, 
	IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {

		byte[] encryptedBytes = this.encryptCipher.doFinal(data);
		return encryptedBytes;
	}

	/**
	 * Encrypt to a Base64Encoded String from a Byte Array
	 * @param data
	 * @return
	 * @throws BusinessException
	 */
	synchronized public String encryptData(byte[] data) throws InvalidKeyException, BadPaddingException, 
	IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {

		byte[] encryptedBytes = this.encryptCipher.doFinal(data);
		return encoder.encode(encryptedBytes);
	}

	synchronized public byte[] decrypt(byte[] encryptedBytes) throws InvalidKeyException, BadPaddingException, 
	IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {

		byte[] data = this.decryptCipher.doFinal(encryptedBytes);
		return data;
	}

	/**
	 * Decrypt to a String from a Base64Encoded cipher text
	 * @param encryptedStr
	 * @return
	 * @throws BusinessException
	 */
	synchronized public String decrypt(String encryptedStr) throws Exception
	{
		try {
			byte[] data = this.decryptCipher.doFinal(decoder.decodeBuffer(encryptedStr));
			return new String(data);
		} catch( Exception e ) {
			return encryptedStr;
		}
	}


	public static String generateActivationKey(){
		return generateRandomString(15);
	}

	/**
	 * Cheap, lightweight, low-security password generator. 
	 */
	public static String generateRandomString (int size) {

		/** The random number generator. */
		java.util.Random r = new java.util.Random();

		/** Minimum length for a decent password */
		int MIN_LENGTH = size + r.nextInt(10);
		/*
		 * Set of characters that is valid. Must be printable, memorable, and "won't
		 * break HTML" (i.e., not ' <', '>', '&', '=', ...). or break shell commands
		 * (i.e., not ' <', '>', '$', '!', ...). I, L and O are good to leave out,
		 * as are numeric zero and one.
		 */
		char[] goodChar = { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
				'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
				'1', '2', '3', '4', '5', '6', '7', '8', '9'};

		/* Generate a Password object with a random password. */
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < MIN_LENGTH; i++) {
			sb.append(goodChar[r.nextInt(goodChar.length)]);
		}
		return sb.toString();
	}

	/**
	 * Unit test for encrypt & decrypt
	 * @param args
	 */
	/*public static void main(String args[]){
		try {
			String msg = "Sunirmal123";
			SecurityUtil su = SecurityUtil.getInstance();
			String encryptedStr = su.encryptData(msg.getBytes());

			su = SecurityUtil.getInstance();
			String org = su.decrypt(encryptedStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}