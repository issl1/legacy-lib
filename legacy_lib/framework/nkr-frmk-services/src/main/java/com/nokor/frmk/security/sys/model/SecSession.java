package com.nokor.frmk.security.sys.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author prasnar
 *
 */
public class SecSession extends EntityA implements MSecSession, Comparable<SecSession> {
    /**  */
    private static final long serialVersionUID = -2237184368807951284L;

    private SecUser user;
    private Date startTS;
    private Date endTS;
    private Date lastHeartbeatTS;
    private Long nbDaysAlreadyUsed;
    
	private String message;

	/**
     * 
     * @return
     */
    public static SecSession createInstance() {
    	SecSession sess = EntityFactory.createInstance(SecSession.class);
        return sess;
    }
    
 
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sec_ses_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
  

	/**
	 * @return the user
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id", nullable = false)
	public SecUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(SecUser user) {
		this.user = user;
	}


	/**
	 * @return the startTS
	 */
	public Date getStartTS() {
		return startTS;
	}



	/**
	 * @param startTS the startTS to set
	 */
	public void setStartTS(Date startTS) {
		this.startTS = startTS;
	}



	/**
	 * @return the endTS
	 */
    @Column(name = "sec_ses_dt_end", nullable = false)
	public Date getEndTS() {
		return endTS;
	}



	/**
	 * @param endTS the endTS to set
	 */
	public void setEndTS(Date endTS) {
		this.endTS = endTS;
	}



	/**
	 * @return the lastHeartbeatTS
	 */
    @Column(name = "sec_ses_dt_last_heartbeat", nullable = false)
	public Date getLastHeartbeatTS() {
		return lastHeartbeatTS;
	}



	/**
	 * @param lastHeartbeatTS the lastHeartbeatTS to set
	 */
	public void setLastHeartbeatTS(Date lastHeartbeatTS) {
		this.lastHeartbeatTS = lastHeartbeatTS;
	}



	/**
	 * @return the nbDaysAlreadyUsed
	 */
    @Column(name = "sec_ses_nb_day_already_used", nullable = false)
	public Long getNbDaysAlreadyUsed() {
		return nbDaysAlreadyUsed;
	}



	/**
	 * @param nbDaysAlreadyUsed the nbDaysAlreadyUsed to set
	 */
	public void setNbDaysAlreadyUsed(Long nbDaysAlreadyUsed) {
		this.nbDaysAlreadyUsed = nbDaysAlreadyUsed;
	}



	/**
	 * @return the message
	 */
    @Column(name = "sec_ses_message", nullable = false)
	public String getMessage() {
		return message;
	}



	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}



	/**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SecSession o) {
        return startTS.compareTo(o.startTS);
    }

}
