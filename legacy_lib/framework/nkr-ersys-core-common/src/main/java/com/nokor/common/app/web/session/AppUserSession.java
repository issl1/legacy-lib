package com.nokor.common.app.web.session;

import com.nokor.common.app.eref.ELanguage;
import com.nokor.common.app.tools.UserSession;
import com.nokor.common.app.tools.helper.AppServicesHelper;
import com.nokor.common.app.web.ISearchCriteriaVO;

/**
 * 
 * @author prasnar
 *
 */
public class AppUserSession extends UserSession implements AppServicesHelper, AppSessionKeys {
    /** */
	private static final long serialVersionUID = -2657626380257444363L;
	

	
	/**
	 * 
	 */
	public AppUserSession() {
	}
	
	
	/**
     * 
     */
    @Override
    public void start() {
    	super.start();
    }
    
    /**
     * 
     */
    @Override
    public void end() {
    	super.end();
    }
    

	/**
	 * 
	 * @return
	 */
	public Long getEmpId() {
		return (Long) attributes.get(KEY_EMP_ID);
	}

	/**
	 * 
	 * @param empId
	 */
	public void setEmpId(Long empId) {
		attributes.put(KEY_EMP_ID, empId);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return (String) attributes.get(KEY_EMP_NAME);
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		attributes.put(KEY_EMP_NAME, name);
	}

	/**
	 * @return the civility
	 */
	public String getCivility() {
		return (String) attributes.get(KEY_EMP_CIVILITY);
	}

	/**
	 * @param civility the civility to set
	 */
	public void setCivility(String civility) {
		attributes.put(KEY_EMP_CIVILITY, civility);
	}

	

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return (String) attributes.get(KEY_EMP_LASTNAME);
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		attributes.put(KEY_EMP_LASTNAME, lastName);
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return (String) attributes.get(KEY_EMP_FIRSTNAME);
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		attributes.put(KEY_EMP_FIRSTNAME, firstName);
	}

	/**
	 * @return the photoUrl
	 */
	public String getPhotoUrl() {
		return (String) attributes.get(KEY_EMP_PHOTO);
	}

	/**
	 * @param photoUrl the photoUrl to set
	 */
	public void setPhotoUrl(String photoUrl) {
		attributes.put(KEY_EMP_PHOTO, photoUrl);
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return (String) attributes.get(KEY_EMP_EMAIL);
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		attributes.put(KEY_EMP_EMAIL, email);
	}

	/**
	 * @return the jobPosition
	 */
	public String getJobPosition() {
		return (String) attributes.get(KEY_EMP_JOB);
	}

	/**
	 * @param jobPosition the jobPosition to set
	 */
	public void setJobPosition(String jobPosition) {
		attributes.put(KEY_EMP_JOB, jobPosition);
	}

	/**
	 * @return the comName
	 */
	public String getComName() {
		return (String) attributes.get(KEY_COM_NAME);
	}

	/**
	 * @param comName the comName to set
	 */
	public void setComName(String comName) {
		attributes.put(KEY_COM_NAME, comName);
	}


	/**
	 * 
	 * @return
	 */
	public ELanguage getLanguageInterface () {
		return (ELanguage) attributes.get(KEY_LANG_WEBSITE);
	}
	
	/**
	 * 
	 * @param languageInterface
	 */
	public void setLanguageInterface(ELanguage languageInterface) {
		attributes.put(KEY_LANG_WEBSITE, languageInterface);
	}
	/**
	 * @return the isAdmin
	 */
	public Boolean getIsAdmin() {
		return getSecUser().isAdmin();
	}

	/**
	 * @return the isSysAdmin
	 */
	public Boolean getIsSysAdmin() {
		return getSecUser().isSysAdmin();
	}
	/**
	 * 
	 * @param classCriteriaVO
	 * @return
	 */
	public <T> ISearchCriteriaVO getSearchCriteria(Class<T> classCriteriaVO) {
		Object obj = attributes.get(SESS_KEY_SEARCH_CRITERIA);

		if (obj != null && obj.getClass() == classCriteriaVO) {
			return (ISearchCriteriaVO) obj;
		}
		return null;

	}
 
 
	/**
	 * 
	 * @param criteriaVO
	 */
	public void setSearchCriteria(ISearchCriteriaVO criteriaVO) {
		attributes.put(AppSessionKeys.SESS_KEY_SEARCH_CRITERIA, criteriaVO);
	}

}
