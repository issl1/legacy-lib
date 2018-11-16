package com.nokor.frmk.mail.service;


import org.springframework.mail.MailSendException;

import com.nokor.frmk.mail.MailParameters;

/**
 * Functionality for sending asynchronous emails.
 */
public interface MailService {

    /**
     * This method sends the mail to multiple recipients with the given subject
     *
     * @param mailParameters The email message parameters needed to be send as plain text
     * @throws MailSenderException this exception wraps the MailException throw by the Spring framework
     */
    public void sendMessage(MailParameters mailParameters) throws MailSendException;

    /**
     * Email address from which application emails are sent.
     *
     * @return email address
     */
    public String getFromAddress();
}
