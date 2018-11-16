package org.seuksa.frmk.tools.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;


/**
 * @author prasnar
 * @version $Revision$
 */
public class CryptoHelper {

    private final static String KEY_ALGORITHM = "PBEWithMD5AndDES";
    private final static String CIPHER_ALGORITHM = KEY_ALGORITHM + "/CBC/PKCS5Padding";
    private static Logger logger = LoggerFactory.getLogger(CryptoHelper.class.getName());

    private static byte[] DEFAULT_SALT = {
									        (byte) 0xA9,
									        (byte) 0x9B,
									        (byte) 0xC8,
									        (byte) 0x32,
									        (byte) 0x56,
									        (byte) 0x35,
									        (byte) 0xE3,
									        (byte) 0x03
									    };

    private static byte[] salt = DEFAULT_SALT; 

    private static Cipher ecipher;
    private static Cipher dcipher;
    private static SecretKey masterKey = null;
    private static String masterPwdInit = null;

    private static int iterationCount = 19;

    /**
     * 
     */
    private CryptoHelper() {

    }

    /**
     * 
     * @param pass
     */
    public static void init(final char[] pass) {
        try {
            masterPwdInit = String.valueOf(pass);
            // Create the key
            final PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
            masterKey = SecretKeyFactory.getInstance(KEY_ALGORITHM).generateSecret(new PBEKeySpec(pass));
            ecipher = Cipher.getInstance(CIPHER_ALGORITHM);
            dcipher = Cipher.getInstance(CIPHER_ALGORITHM);

            // Prepare the parameter to the ciphers
            // AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
            // iterationCount);
            ecipher.init(Cipher.ENCRYPT_MODE, masterKey, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, masterKey, paramSpec);

        }
        catch (final Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static synchronized String decrypt(final String in) {
        try {
            // Decode base64 to get bytes
            final byte[] dec = Base64Utils.decodeFromString(in);
            // Decrypt
            final byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");
        }
        catch (final Exception e) {
            // throw new SecurityException("Could not decrypt: " +
            // e.getMessage());
            logger.warn("Could not decrypt: " + in);
        }
        return null;
    }
    
    /**
     * 
     * @param masterPwdForEncryption
     * @param message
     * @return
     */
    public static String decryptWithMasterPwd(final String masterPwdForEncryption, final String message) {
		// Backup the current MasterPwd
		String masterPwdInit = CryptoHelper.getMasterPwdInit();

		// Re-Init the MasterPwd for message decryption
		CryptoHelper.init(masterPwdForEncryption.toCharArray());
		String clearMsg = CryptoHelper.decrypt(message);

		// Restore the current MasterPwd
		if (masterPwdInit != null) {
			CryptoHelper.init(masterPwdInit.toCharArray());
		}
		
		return clearMsg;
    }
    
    /**
     * 
     * @param masterPwdForEncryption
     * @param message
     * @return
     */
    public static String encryptWithMasterPwd(final String masterPwdForEncryption, final String message) {
		// Backup the current MasterPwd
		String masterPwdInit = CryptoHelper.getMasterPwdInit();

		// Re-Init the MasterPwd for message encryption
		CryptoHelper.init(masterPwdForEncryption.toCharArray());
		String encryptedMsg = CryptoHelper.encrypt(message);

		// Restore the current MasterPwd
		if (masterPwdInit != null) {
			CryptoHelper.init(masterPwdInit.toCharArray());
		}
		
		return encryptedMsg;
    }

    /**
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static synchronized String encrypt(final String in) {
        try {

            // Encode the string into bytes using utf-8
            final byte[] utf8 = in.getBytes("UTF8");
            // Encrypt
            final byte[] enc = ecipher.doFinal(utf8);
            // Encode bytes to base64 to get a string
            return Base64Utils.encodeToString(enc);
        }
        catch (final Exception e) {
            // throw new SecurityException("Could not encrypt: " +
            // e.getMessage());
            logger.warn("Could not encrypt: " + in);
        }
        return null;
    }

    /**
     * @return the masterPwdInit
     */
    public static String getMasterPwdInit() {
        return masterPwdInit;
    }

	/**
	 * @return the salt
	 */
	public static byte[] getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public static void setSalt(byte[] salt) {
		CryptoHelper.salt = salt;
	}

    
}
