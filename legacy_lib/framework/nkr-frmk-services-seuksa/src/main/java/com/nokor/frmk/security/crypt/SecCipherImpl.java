package com.nokor.frmk.security.crypt;

import org.seuksa.frmk.tools.security.CryptoHelper;


/**
 * 
 * @author prasnar
 *
 */
public class SecCipherImpl implements SecCipher {
	private static char[] masterPwdInit;
	
	/**
	 * 
	 */
	public SecCipherImpl() {
		
	}

	@Override
	public void init() {
		CryptoHelper.init(masterPwdInit);
	}
	
	@Override
	public String encrypt(String in) {
		try {
            String res = CryptoHelper.encrypt(in);
            return res;
        }
        catch (final Exception e) {
             throw new SecurityException("Could not encrypt: " + e.getMessage());
//            logger.warn("Could not encrypt: " + in);
        }
	}

	@Override
	public String decrypt(String in) {
		try {
			String res = CryptoHelper.decrypt(in);
            return res;
        }
        catch (final Exception e) {
            throw new SecurityException("Could not decrypt: " + e.getMessage());
//          logger.warn("Could not decrypt: " + in);
        }
	}

	/**
	 * @return the masterPwdInit
	 */
	public static char[] getMasterPwdInit() {
		return masterPwdInit;
	}

	/**
	 * @param masterPwdInit the masterPwdInit to set
	 */
	public static void setMasterPwdInit(char[] masterPwdInit) {
		SecCipherImpl.masterPwdInit = masterPwdInit;
	}

	
}
