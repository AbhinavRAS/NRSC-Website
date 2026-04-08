package ehiring.dao;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CipherCreator {
	// Algorithm used
	private final static String ALGORITHM = "DES";
	private final static String secretKey = "05819734";

	/**
	 * Encrypt data
	 * 
	 * @param secretKey - a secret key used for encryption
	 * @param data      - data to encrypt
	 * @return Encrypted data
	 * @throws Exception
	 */
	public static String cipher(String data) throws Exception {
		// Key has to be of length 8
		if (secretKey == null || secretKey.length() != 8)
			throw new Exception("Invalid key length - 8 bytes key needed!");

		SecretKey key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		return toHex(cipher.doFinal(data.getBytes()));
	}

	/**
	 * Decrypt data
	 * 
	 * @param secretKey - a secret key used for decryption
	 * @param data      - data to decrypt
	 * @return Decrypted data
	 * @throws Exception
	 */
	public static String decipher(String data) throws Exception {
		// Key has to be of length 8
		String msg = "error";
		System.out.println("THE DECIPHERed1111:"+data);
		try {
		if (secretKey == null || secretKey.length() != 8)
		{
			System.out.println("THE DECIPHERed222:"+msg);
			throw new Exception("Invalid key length - 8 bytes key needed!");
		}
		SecretKey key = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		msg= new String(cipher.doFinal(toByte(data)));
		System.out.println("THE DECIPHERed3333:"+msg);
		}catch(Exception e)
		{
			msg = "error";
		}

		System.out.println("THE DECIPHERed:"+msg);
		return msg;
	}

	// Helper methods

	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;

		byte[] result = new byte[len];

		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		return result;
	}

	public static String toHex(byte[] stringBytes) {
		StringBuffer result = new StringBuffer(2 * stringBytes.length);

		for (int i = 0; i < stringBytes.length; i++) {
			result.append(HEX.charAt((stringBytes[i] >> 4) & 0x0f)).append(HEX.charAt(stringBytes[i] & 0x0f));
		}

		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	// Helper methods - end

	/**
	 * Quick test
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String data = "saikalpana_t@nrsc.gov.in";
			// String secretKey = "01234567";

			String encryptedData = cipher(data);
			System.out.println("encryptedData: " + encryptedData);

			String decryptedData = decipher("57F4D8EA78F5B46DC688645F1680770B803D91C7EAA74C980012F98765C3EDAF");

			System.out.println("decryptedData: " + decryptedData);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
