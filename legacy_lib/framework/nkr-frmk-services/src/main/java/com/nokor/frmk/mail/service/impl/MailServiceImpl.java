package com.nokor.frmk.mail.service.impl;


import java.util.Date;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.seuksa.frmk.tools.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.nokor.frmk.mail.MailParameters;
import com.nokor.frmk.mail.service.MailService;

/**
 * 
 * @author prasnar
 *
 */
public class MailServiceImpl implements MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
    @Autowired
    private JavaMailSender mailSender;
    private String fromAddress;
    @Autowired
    private VelocityEngine velocityEngine;

    /**
     * {@inheritDoc}
     */
    @Async
    public void sendMessage(MailParameters mailParameters) throws MailSendException {
        if (mailParameters.isUseVMTemplate()) {
            if (mailParameters.getVmTemplateName() == null || mailParameters.getTo() == null) {
                LOGGER.error("Input parameters received for sendMessage(String,String[]) are null");
            }
            sendVMEmail(mailParameters);
        } else {
            if (mailParameters.getBodyMessage() == null || mailParameters.getTo() == null) {
                LOGGER.error("Input parameters received for sendMessage(String,String[]) are null");
            }
            sendEmail(mailParameters);
        }
    }

    private void sendEmail(MailParameters mailParameters) throws DaoException {

        try {
            final SimpleMailMessage simpleMessage = getSimpleMessage(mailParameters);
            simpleMessage.setText(mailParameters.getBodyMessage());
            mailSender.send(simpleMessage);
        } catch (MailException ex) {
            LOGGER.error("MailSenderImpl: sendMessage() Exception occured" + ex.getMessage());
            throw new DaoException(ex.getMessage());
        }
    }

    private SimpleMailMessage getSimpleMessage(MailParameters mailParameters) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setSentDate(new Date());
        message.setTo(mailParameters.getTo());
        message.setCc(mailParameters.getCc());
        message.setBcc(mailParameters.getBcc());
        message.setFrom(fromAddress);
        message.setSubject(mailParameters.getSubject());

        return message;
    }

    /**
     * Sends e-mail using Velocity template for the body and
     * the properties passed in as Velocity variables.
     *
     * @param mailParameters - email parameters
     * @throws MailSendException
     */
    private void sendVMEmail(final MailParameters mailParameters) throws MailSendException {

        try {
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setTo(mailParameters.getTo());
                    final String[] cc = mailParameters.getCc();
                    if (cc != null) {
                        message.setCc(cc);
                    }
                    final String[] bcc = mailParameters.getBcc();
                    if (bcc != null) {
                        message.setBcc(bcc);
                    }
                    message.setFrom(getFromAddress());
                    message.setSubject(mailParameters.getSubject());

                    String encoding = "UTF-8";
                    String body = VelocityEngineUtils.mergeTemplateIntoString(
                            velocityEngine,
                            mailParameters.getVmTemplateName(),
                            encoding,
                            mailParameters.getVmTemplateVariables()
                    );
                    message.setText(body, true);
                }
            };
            mailSender.send(preparator);

        } catch (MailException ex) {
            LOGGER.error("MailSenderImpl: sendMessage() Exception occured" + ex.getMessage());
            throw new MailSendException(ex.getMessage());
        }
    }


    /**
     * {@inheritDoc}
     */
    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }


}
