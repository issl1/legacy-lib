package com.nokor.common.app.eventlog.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.frmk.security.model.SecApplication;
import com.nokor.frmk.security.model.SecUser;




/**
 * 
 * @author prasnar
 */
@Entity
@Table(name = "td_sec_event_log")
public class SecEventLog extends EntityA {
	/** */
	private static final long serialVersionUID = -1313128856562666719L;

	public static String UNKNOWN_DATA = "N/A";

	private String sessionKey;
	private SecUser secUser;
	private SecApplication secApplication;
	private Date evenDate;
	private SecEventAction action;
	private ESecEventMode mode;
	private String text;
	
	private String urlForView;
	private String remoteIPAddress;
	private String remoteHost;
	private String remotePort;
	private String remoteUser;
	
	private String url;
	private String queryData;
	private String sessionData;
    
	private String field01;
	private String field02;

	/**
     * 
     * @return
     */
    public static SecEventLog createInstance(HttpSession sess) {
        SecEventLog sesLog = EntityFactory.createInstance(SecEventLog.class);
        if (sess != null) {
        	sesLog.setSessionKey(sess.getId());
        }
        return sesLog;
    }
    
	/**
     * 
     * @return
     */
    public static SecEventLog createInstance(String sess) {
        SecEventLog sesLog = EntityFactory.createInstance(SecEventLog.class);
        if (sess != null) {
        	sesLog.setSessionKey(sess);
        }
        return sesLog;
    }
    
    /**
     * 
     * @return
     */
    public static SecEventLog createInstance() {
        return createInstance( (String) null);
    }

    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sec_eve_log_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the sessionKey
	 */
    @Column(name = "sec_eve_log_session_key", nullable = true)
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

		
	/**
	 * @return the evenDate
	 */
    @Column(name = "sec_eve_log_dt_when", nullable = false)
	public Date getEvenDate() {
		return evenDate;
	}

	/**
	 * @param evenDate the evenDate to set
	 */
	public void setEvenDate(Date evenDate) {
		this.evenDate = evenDate;
	}

	/**
	 * @return the text
	 */
    @Column(name = "sec_eve_log_text", nullable = true)
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the urlForView
	 */
    @Column(name = "sec_eve_log_url_view", nullable = true)
	public String getUrlForView() {
		return urlForView;
	}

	/**
	 * @param urlForView the urlForView to set
	 */
	public void setUrlForView(String urlForView) {
		this.urlForView = urlForView;
	}

	/**
	 * @return the action
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_eve_act_id", nullable = false)
	public SecEventAction getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(SecEventAction action) {
		this.action = action;
	}

	/**
	 * @return the mode
	 */
    @Column(name = "sec_eve_mod_id", nullable = false)
    @Convert(converter = ESecEventMode.class)
	public ESecEventMode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(ESecEventMode mode) {
		this.mode = mode;
	}
	


	/**
	 * @return the url
	 */
    @Column(name = "sec_eve_log_url", nullable = false)
	public String getUrl() {
		return url;
	}


	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	
	/**
	 * @return the remoteIPAddress
	 */
    @Column(name = "sec_eve_log_remote_addr_ip", nullable = false)
	public String getRemoteIPAddress() {
		return StringUtils.isNotEmpty(remoteIPAddress) ? remoteIPAddress : UNKNOWN_DATA;
	}


	/**
	 * @param remoteIPAddress the remoteIPAddress to set
	 */
	public void setRemoteIPAddress(String remoteIPAddress) {
		this.remoteIPAddress =  StringUtils.isNotEmpty(remoteIPAddress) ? remoteIPAddress : UNKNOWN_DATA;
	}


	/**
	 * @return the remoteHost
	 */
    @Column(name = "sec_eve_log_remote_host", nullable = true)
	public String getRemoteHost() {
		return remoteHost;
	}


	/**
	 * @param remoteHost the remoteHost to set
	 */
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}


	/**
	 * @return the remotePort
	 */
    @Column(name = "sec_eve_log_remote_port", nullable = true)
	public String getRemotePort() {
		return remotePort;
	}


	/**
	 * @param remotePort the remotePort to set
	 */
	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}


	/**
	 * @return the remoteUser
	 */
    @Column(name = "sec_eve_log_remote_user", nullable = true)
	public String getRemoteUser() {
		return remoteUser;
	}


	/**
	 * @param remoteUser the remoteUser to set
	 */
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}


	/**
	 * @return the secUser
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id", nullable = true)
	public SecUser getSecUser() {
		return secUser;
	}

	/**
	 * @param secUser the secUser to set
	 */
	public void setSecUser(SecUser secUser) {
		this.secUser = secUser;
	}
	
	


	/**
	 * @return the secApplication
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_app_id", nullable = false)
	public SecApplication getSecApplication() {
		return secApplication;
	}


	/**
	 * @param secApplication the secApplication to set
	 */
	public void setSecApplication(SecApplication secApplication) {
		this.secApplication = secApplication;
	}

	/**
	 * @return the queryData
	 */
    @Column(name = "sec_eve_log_query_data", nullable = true)
	public String getQueryData() {
		return queryData;
	}

	/**
	 * @param queryData the queryData to set
	 */
	public void setQueryData(String queryData) {
		this.queryData = queryData;
	}

	/**
	 * @return the sessionData
	 */
    @Column(name = "sec_eve_log_session_data", nullable = true)
	public String getSessionData() {
		return sessionData;
	}

	/**
	 * @param sessionData the sessionData to set
	 */
	public void setSessionData(String sessionData) {
		this.sessionData = sessionData;
	}

	/**
	 * @return the field01
	 */
    @Column(name = "sec_eve_log_field01", nullable = true)
	public String getField01() {
		return field01;
	}

	/**
	 * @param field01 the field01 to set
	 */
	public void setField01(String field01) {
		this.field01 = field01;
	}

	/**
	 * @return the field02
	 */
    @Column(name = "sec_eve_log_field02", nullable = true)
	public String getField02() {
		return field02;
	}

	/**
	 * @param field02 the field02 to set
	 */
	public void setField02(String field02) {
		this.field02 = field02;
	}

	
   
}
