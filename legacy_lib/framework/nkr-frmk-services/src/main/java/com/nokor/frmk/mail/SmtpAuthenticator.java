package com.nokor.frmk.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author prasnar
 */
public class SmtpAuthenticator extends Authenticator {
    private String username;
    private String password;

    /**
     * 
     * @param username
     * @param password
     */
    public SmtpAuthenticator(String username,
                             String password) {
        super();
        this.username = username;
        this.password = password;
    }

    /**
     * 
     */
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
