package com.nokor.frmk.security.crypt;

/**
 * 
 * @author prasnar
 *
 */
public interface SecCipher {
	public static final String UTF8 = "UTF8";
	
	public abstract void init();
	public abstract String encrypt(final String in);
	public abstract String decrypt(final String in);
}
