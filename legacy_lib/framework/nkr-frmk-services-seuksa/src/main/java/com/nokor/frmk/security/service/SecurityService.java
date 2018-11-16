package com.nokor.frmk.security.service;

import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.service.BaseEntityService;
import org.seuksa.frmk.tools.exception.DaoException;
import org.springframework.security.authentication.BadCredentialsException;

import com.nokor.frmk.security.model.SecPage;
import com.nokor.frmk.security.model.ESecControlType;
import com.nokor.frmk.security.model.ESecPrivilege;
import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecControl;
import com.nokor.frmk.security.model.SecControlPrivilege;
import com.nokor.frmk.security.model.SecControlProfilePrivilege;
import com.nokor.frmk.security.model.SecManagedProfile;
import com.nokor.frmk.security.model.SecProfile;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.security.model.SecUserAware;


/**
 * 
 * @author prasnar
 * @version $Revision$
 */
public interface SecurityService extends BaseEntityService {
	
	/**
	 * Find secProfile by code.
	 * 
	 * @param secProCode The secProfile's code.
	 * @return The secProfile or null.
	 */
	SecProfile findProfileByCode(String secProCode) throws DaoException;

	/**
	 * Create secProfile and create control privileges by secProfile.
	 * 
	 * @param secProfile The SecProfile.
	 * @throws IllegalArgumentException<br/>
	 * 		- The secProfile is null.<br/>
	 * 		- The secProfile's id is not null or id greater 0.
	 */
	SecProfile createProfile(SecProfile secProfile);
	
	SecApplication creatApplication(String code, String desc);

	SecProfile createProfile(String code, String desc);

	ESecPrivilege createPrivilege(String code, String desc);

	SecPage creatPage(String code, String desc, SecApplication app);


	/**
	 * Update secProfile and update control privilege by secProfile.
	 * 
	 * @param secProfile The SecProfile.
	 * @throws IllegalArgumentException<br/>
	 * 		- The secProfile is null.<br/>
	 * 		- The secProfile's id is null or id equal 0.
	 */
	SecProfile updateProfile(SecProfile secProfile);
	
	void bulkUpdateProfileAccessControls(SecApplication app, List<SecProfile> profiles);
	
	void grantProfileAllAccessControls(SecApplication app, SecProfile secProfile);
	
	/**
	 * Update the profile by the new list controls accesses
	 * 
	 * @param secProfile
	 * @param lstCpp
	 */
	void updateProfileAccessControls(SecProfile secProfile, List<SecControlProfilePrivilege> lstCpp);
	
	/**
	 * Update the profile by the new list controls accesses
	 * 
	 * @param secProfile
	 * @param lstCpp
	 * @param app
	 */
	void updateProfileAccessControls(SecApplication app, SecProfile secProfile, List<SecControlProfilePrivilege> lstCpp);

	List<ESecPrivilege> getListPrivileges();

	/**
	 * List secProfiles.
	 * 
	 * @return The secProfile List or empty array list.
	 */
	List<SecProfile> getListProfiles() throws DaoException;
	
	
	/**
	 * List secApplications.
	 * 
	 * @return The secApplication List or empty array list.
	 */
    List<SecApplication> getListApplications() throws DaoException;

    /**
	 * List secUsers.
	 * 
	 * @return The secUser List or empty array list.
	 */
    List<SecUser> getListUsers() throws DaoException;
    
    /**
	 * List secUser with order.
	 * 
	 * @param order The order.
	 * @return The secUser List or empty array list.
	 */
    List<SecUser> getListUsers(Order order) throws DaoException;
    
    /**
	 * Load secUser by username(login).
	 * 
	 * @param username The user login.
	 * 
	 * @return The secUser or null.
	 */
    SecUser loadUserByUsername(String username)  throws DaoException;

    /**
	 * Get secApplication by code.
	 * 
	 * @param code The secApplication's code.
	 * @return The secApplication or null.
	 */
    SecApplication getApplication(String code) throws DaoException;

	SecUser getAnonynmousUser();

    /**
	 * Create new secUser with default profile.
	 * 
	 * @param login The user login name.
	 * @param desc The user desc.
	 * @param password The user password.
	 * @param defaultProfile The default profile.
	 * @throws SecUserCreationException
	 */
	SecUser createUser(String login, String descLogin, String password, SecProfile defaultProfile) throws SecUserCreationException;

	SecUser createUser(String login, String desc, String password, SecProfile defaultProfile, boolean createAlsoInLdap) throws SecUserCreationException;

	/**
	 * Create new secUser.
	 * 
	 * @param secUser The secUser.
	 * @param rawPwd The password.
	 * @throws SecUserCreationException<br/>
	 *   - The secUser is null.<br/>
	 *   - The secUser'is > 0.<br/>
	 *   - The rawPwd is null or empty.<br/>
	 *   - The default profile is null.<br/>
	 *   - The duplicate login.
	 */
	SecUser createUser(SecUser secUser, String rawPwd) throws SecUserCreationException;

	SecUser createUser(SecUser user, String rawPwd, boolean createAlsoInLdap) throws SecUserCreationException;
	


	/**
     * Update the secUser.
     * 
     * @param secUser The secUser.
     * @throws IllegalArgumentException The secUser is null or id <= 0.
     */
	void update(SecUser secUser);
	
	/**
     * Update the secUser and change password if the new password is set.
     * 
     * @param secUser The secUser.
     * @param newPassword The new password.
     * @throws IllegalArgumentException The secUser is null or id <= 0.
     */
	void update(SecUser secUser, String newPassword);
	
	/**
     * Archive the secUser.<br/>
     * This method execute the {@link #removeUser} method.
     * 
     * @param secUser The secUser.
     */
	void archive(SecUser secUser);

	/**
     * Change the secUser password.
     * 
     * @param secUser The secUser.
     * @param newRawPwd The new password.
     */
	void changePassword(SecUser secUser, String newRawPwd);

	/**
     * Change the secUser password with validate the old password.
     * 
     * @param secUser The secUser.
     * @param oldRawPwd The old password.
     * @param newRawPwd The new password.
     * @throws BadCredentialsException The old password doesn't match current passowrd in database.
     */
	void changePassword(SecUser secUser, String oldRawPwd, String newRawPwd);
	
	/**
     * Change the secUser password salt.
     * 
     * @param secUser The secUser.
     */
	void changePasswordSalt(SecUser secUser);

	/**
     * Add secProfile into secUser and update secUser.
     * 
     * @param secUser The secUser.
     * @param secPro The secProfile.
     */
	void addProfileToUser(SecUser secUser, SecProfile secPro);

	/**
     * Add secApplication into secProfile and update secProfile.
     * 
     * @param secApp The secApplication.
     * @param secPro The secProfile.
     */
	void addApplicationToProfile(SecApplication secApp, SecProfile secPro);

	/**
     * Check exists secUser by username.
     * 
     * @param username The secUser login.
     * @return <b>True</b> The secUser exists. <br/><b>False</b> The secUser doesn't exists.
     */
	boolean checkUserByUsername(String username);
	
	/**
     * Check secApplication is in secProfile list of secUser.
     * 
     *  @param secUser The secUser.
     *  @param secApp The secApplication.
     *  @return <b>True</b> The secApplication in secProfile list of secUser.<br/>
     * 	 		<b>False</b> The secApplication not in secProfile list of secUser.
     */
	boolean checkUserInProfileApplication(SecUser secUser, SecApplication secApp);

	/**
     * Check secProfile is in secUser's profiles.
     * 
     *  @param secUser The secUser.
     *  @param secProfile The secProfile.
     *  @return <b>True</b> The secProfile in secUser's profiles.<br/>
     * 	 		<b>False</b> The secProfile not in secUser's profiles.
     */
	boolean checkProfileInUserProfile(SecUser secUser, SecProfile secProfile);

	/**
	 * Force remove the secUser.
	 * 
	 * @param secUser The secUser.
	 * @param entitySecLog The entitySecLog Class.
	 * 
	 * @return
	 */
	<T extends SecUserAware> boolean forceRemoveUser(SecUser secUsr, Class<T> entitySecLog);

	/**
	 * Remove by archiving the user
	 *  (by ARCHI status + by renaming the login)  
	 * @param secUser The secUser.
	 * @return
	 */
	boolean removeUser(SecUser secUsr);

	/**
	 * List Control privileges by secProfile's id.
	 * 
	 * @param profileId The secProfile's id.
	 * @return The secControlProfilePrivilege List or empty array list.
	 */
	List<SecControlProfilePrivilege> listControlsPrivilegesByProfileIdAndAppId(Long profileId, Long appId);
	
	List<SecControlProfilePrivilege> listControlsPrivilegesByParentControlAndProfileIdAndAppId(Long parentControlId, Long profileId, Long appId);

	/**
	 * List Control privileges by secControl's id.
	 * 
	 * @param controlId The secControl's id.
	 * @return The secControlPrivilege List or empty array list.
	 */
	List<SecControlPrivilege> listControlsPrivileges(SecControl control);
	
	/**
	 * List Control privileges by secProfile's id and secApplication's id.
	 * 
	 * @param profileId The secProfile's id.
	 * @param appId The secApplication's id.
	 * @return The secControlProfilePrivilege List or empty array list.
	 */
	List<SecControlProfilePrivilege> listControlsPrivilegesByProfileId(Long profileId);
	
	/**
	 * List Control by parent's id.
	 * 
	 * @param parentId The parent's's id.
	 * @return The secControl List or empty array list.
	 */
	List<SecControl> listControlsByParentId(Long parentId);
	
	List<SecControl> listControlsByParentIdAndProfile(Long parentId, Long profileId);
	
	/**
	 * List Parent Control by secApplication's id.
	 * 
	 * @param appId The secApplication's id.
	 * @return The secControl List or empty array list.
	 */
	List<SecControl> listParentControlsByAppId(Long appId);
	
	List<SecControl> listParentControlsByAppIdAndProfile(Long appId, Long profileId);

	List<SecControl> listControlsByAppId(Long appId);

	List<SecControl> listControlsByAppIdAndProfile(Long appId, Long profileId);

	SecControl findControlByAppId(String ctlCode, Long appId);

	SecControl findControlByAppCode(String ctlCode, String appCode);
	
	void createManagedProfile(SecProfile parent, SecProfile child);

	List<SecProfile> listProfiles();

	List<SecProfile> listProfilesNotAdmin();

	List<SecProfile> listProfilesByManagedProfile(Long profileId);
	
	SecManagedProfile findManagedProfile(Long mainProId, Long managedProId);
	
	List<SecManagedProfile> listManagedProfileByParent(Long parentId);

	void initProfileControlAccess(SecProfile pro, SecControl ctl, List<ESecPrivilege> privileges);

	void initProfileControlAccess(SecProfile pro, SecControl ctl);

	void initControlPrivilege(SecControl ctl, List<ESecPrivilege> privileges);

	void initControlPrivilege(SecControl ctl);

	void saveOrUpdateControl(SecControl ctl);
	
	void deleteControl(SecControl ctl);

	List<SecControl> listControls(SecApplication app, ESecControlType type);

	List<SecControl> listControls(ESecControlType type);


}
