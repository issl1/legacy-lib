package com.nokor.frmk.security.crypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * 
 * @author prasnar
 *
 */
public class SecCipherBase64Impl implements SecCipher {

	/**
	 * 
	 */
	public SecCipherBase64Impl() {
		
	}

	@Override
	public void init() {
	}
	
	@Override
	public String encrypt(String in) {
		try {
            // Encode the string into bytes using utf-8
            final byte[] str = in.getBytes(UTF8);
            // Encode bytes to base64 to get a string
            return new BASE64Encoder().encode(str);
        }
        catch (final Exception e) {
             throw new SecurityException("Could not encrypt: " + e.getMessage());
//            logger.warn("Could not encrypt: " + in);
        }
	}

	@Override
	public String decrypt(String in) {
		try {
            // Decode base64 to get bytes
            final byte[] dec = new BASE64Decoder().decodeBuffer(in);
            // Decode using utf-8
            return new String(dec, UTF8);
        }
        catch (final Exception e) {
            throw new SecurityException("Could not decrypt: " + e.getMessage());
//          logger.warn("Could not decrypt: " + in);
        }
	}
}
