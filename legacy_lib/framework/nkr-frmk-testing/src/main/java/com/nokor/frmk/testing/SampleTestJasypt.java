package com.nokor.frmk.testing;

import java.security.Provider;
import java.security.Security;

import junit.framework.TestCase;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.jasypt.salt.SaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author prasnar
 *
 */
public class SampleTestJasypt extends TestCase {
    private static Logger logger = LoggerFactory.getLogger(SampleTestJasypt.class);

	/**
	 * 
	 */
	public SampleTestJasypt() {
	}
	
	/**
	 * For placeholder: database.properties
	 */
	public void testStandardPBEStringEncryptor() {
		try {
			//String fixedSalt = @$#nKr-DbmYS_qL#$@ / @$#NKR-EFINANCE;
			String keyPwd = "@$#nKr-DbmYS_qL#$@";
			// admin, proto@nkr
			String propertyPwd = "proto@nkr";
			SimplePBEConfig config = new SimplePBEConfig(); 
			//config.setSaltGenerator(new MySaltGenerator(fixedSalt));
			// SHA, md5, PBEWithMD5AndDES
			config.setAlgorithm("PBEWithMD5AndDES");
			config.setKeyObtentionIterations(1000);
			config.setPassword(keyPwd);
	
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setConfig(config);
			encryptor.initialize();	
			
			String encPwd = PropertyValueEncryptionUtils.encrypt(propertyPwd, encryptor);
			String decPwd = PropertyValueEncryptionUtils.decrypt(encPwd, encryptor);
			
			logger.info("ENC(" + encPwd + ")"); // admim:SiS0iO/uOemeoJBfk2/DZw==
			logger.info("DEC(" + decPwd + ")");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	public void xxtestDisplaySecurityProviders() {
		for (Provider provider : Security.getProviders()) {
			System.out.println("Provider: " + provider.getName());
			for (Provider.Service service : provider.getServices()) {
				System.out.println("  Algorithm: " + service.getAlgorithm());
			}
		}
	}
	
	class MySaltGenerator implements SaltGenerator {
		private String fixedSalt;
		
		/**
		 * 
		 * @param fixedSalt
		 */
		public MySaltGenerator(String fixedSalt) {
			this.fixedSalt = fixedSalt;
		}
		
		@Override
		public byte[] generateSalt(int lengthBytes) {
			// TODO Auto-generated method stub
			return fixedSalt.getBytes();
		}

		@Override
		public boolean includePlainSaltInEncryptionResults() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
