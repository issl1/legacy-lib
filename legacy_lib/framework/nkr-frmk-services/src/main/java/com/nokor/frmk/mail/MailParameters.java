package com.nokor.frmk.mail;


import java.util.HashMap;
import java.util.Map;

/**
 * Email parameters indicating whom, what and how to send emails.
 * @author prasnar
 */
public class MailParameters {
    /**
     * The message needed to be send as plain text
     */
    private String bodyMessage;
    /**
     * The mail addresses in an array to which the mail has to be sent in TO
     */
    private String[] to;
    /**
     * The mail addresses in an array to which the mail has to be sent in CC
     */
    private String[] cc;
    /**
     * The mail addresses in an array to which the mail has to be sent in BCC
     */
    private String[] bcc;
    /**
     * The subject of the mail to be sent
     */
    private String subject;
    /**
     * Name of velocity template name;
     */
    private String vmTemplateName;
    /**
     * Indicator whether to use vm template of plain text message
     */
    private boolean useVMTemplate;
    /**
     * Variables to use when processing the template.
     */
    private Map<String, Object> vmTemplateVariables = new HashMap<String, Object>();

    public String[] getCc() {
        return cc;
    }

    public void setCc(String... cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String... bcc) {
        this.bcc = bcc;
    }

    public String getBodyMessage() {
        return bodyMessage;
    }

    public void setBodyMessage(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }

    public String getVmTemplateName() {
        return vmTemplateName;
    }

    public void setVmTemplateName(String vmTemplateName) {
        this.vmTemplateName = vmTemplateName;
    }

    public boolean isUseVMTemplate() {
        return useVMTemplate;
    }

    public void setUseVMTemplate(boolean useVMTemplate) {
        this.useVMTemplate = useVMTemplate;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String... to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Object> getVmTemplateVariables() {
        return vmTemplateVariables;
    }

    public void setVmTemplateVariables(Map<String, Object> vmTemplateVariables) {
        this.vmTemplateVariables = vmTemplateVariables;
    }

    public void addVmTemplateVariable(String key, Object value) {
        this.vmTemplateVariables.put(key, value);
    }
}
