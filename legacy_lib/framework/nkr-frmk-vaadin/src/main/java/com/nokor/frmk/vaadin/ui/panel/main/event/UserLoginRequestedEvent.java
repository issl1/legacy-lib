/**
 * 
 */
package com.nokor.frmk.vaadin.ui.panel.main.event;

/**
 * @author prasnar
 *
 */
public class UserLoginRequestedEvent {
	private final String userName, password;

	/**
	 * 
	 * @param userName
	 * @param password
	 */
    public UserLoginRequestedEvent(final String userName,
            final String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * 
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @return
     */
    public String getPassword() {
        return password;
    }
}
