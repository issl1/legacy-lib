package com.nokor.efinance.third.finwiz.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.frmk.security.ldap.model.LdapUser;

/**
 * 
 * @author prasnar
 *
 */
public class FinwizSecurityHelper {
    private final static Logger logger = LoggerFactory.getLogger(FinwizSecurityHelper.class);

    private static final String MSG_CODE_ERROR = "1"; 
    private static final String MSG_CODE_OK = "0"; 

    private static final String COUNTRY_OK = "TH"; 
    private static final String COMPANY_CODE = "GL"; 

    /**
     * 
     * @param login
     * @param pwd
     * @return
     */
	public static LdapUser createUser(String login, String pwd) {
		
		/*UserLoginM userLoginM = new UserLoginM();
		userLoginM.setUserId(login);
		userLoginM.setPassword(pwd);
		
		ParamCountryM paramCountryM = new ParamCountryM();
		
		BaseOrganization org = AppSettingConfigHelper.getMainOrganization();
		if (!org.getCountry().equals(ECountry.THA)) {
			throw new IllegalStateException("Country not THAILAND");
		}
		paramCountryM.setCountryId(COUNTRY_OK); // to map
		userLoginM.setParamCountryM(paramCountryM);
		
		ParamCompanyM paramCompanyM = new ParamCompanyM();
		paramCompanyM.setCompanyId(COMPANY_CODE);
		userLoginM.setParamCompanyM(paramCompanyM);
		
		userLoginM.setServiceName("createUserLogin");

		FinWizResultMessage resultMessage = ClientServiceHelper.postMessage(userLoginM, RouterConStant.USER_ENDPOINT);
		if (resultMessage != null && resultMessage.getResultListObj().size() > 0) {
			userLoginM = (UserLoginM) resultMessage.getResultListObj().get(0);
			logger.debug("FinWiz - User created: id {}", userLoginM.getUserId());
		}*/
		
		LdapUser user = null; // convertToLdpUser(userLoginM);
		
		return user;
	}
	
	/**
	 * 
	 * @param userLoginM
	 * @return
	 */
/*	private static LdapUser convertToLdpUser(UserLoginM userLoginM) {
		LdapUser user = LdapUser.createInstance();
		user.setLogin(userLoginM.getUserId());
		user.setPassword(userLoginM.getPassword());
		if (userLoginM.getUserRolesM() != null && userLoginM.getUserRolesM().size() > 0) {
				user.addProfile(userLoginM.getUserRolesM().get(0).getRoleId());
		}
		
		return user;
	}*/
	
	/**
	 * 
	 * @param login
	 * @param pwd
	 * @return
	 */
	public static LdapUser authenticateUser(String login, String pwd) {
		
/*		UserLoginM userLoginM = new UserLoginM();
		userLoginM.setUserId(login);
		userLoginM.setPassword(pwd);
		
		ParamCountryM paramCountryM = new ParamCountryM();
		paramCountryM.setCountryId(COUNTRY_OK);
		userLoginM.setParamCountryM(paramCountryM);
		
		ParamCompanyM paramCompanyM = new ParamCompanyM();
		paramCompanyM.setCompanyId(COMPANY_CODE);
		userLoginM.setParamCompanyM(paramCompanyM);
		
		
		userLoginM.setServiceName(ServiceConstant.USER_PROFILE_LOGIN);*/

		LdapUser user = null;
/*		FinWizResultMessage resultMessage = ClientServiceHelper.postMessage(userLoginM, RouterConStant.USER_ENDPOINT);
		if (resultMessage != null) {
			if (resultMessage.getResultMessage().getMsgCode().equals(MSG_CODE_OK)
						&& resultMessage.getResultListObj().size() > 0) {
				userLoginM = (UserLoginM) resultMessage.getResultListObj().get(0);
				logger.debug("FinWiz - User loaded: id {}", userLoginM.getUserId());
				user = convertToLdpUser(userLoginM);
			}
		}*/
		
		
		return user;
	}
	
}
