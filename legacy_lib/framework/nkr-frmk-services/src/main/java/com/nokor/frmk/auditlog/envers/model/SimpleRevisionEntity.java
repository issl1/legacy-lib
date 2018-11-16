package com.nokor.frmk.auditlog.envers.model;

import javax.persistence.Column;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.envers.DefaultRevisionEntity;

/**
 * @see DefaultRevisionEntity
 * @author prasnar
 *
 */
//@Entity
//@Table(name = "_env_revision")
//@RevisionEntity(SimpleRevisionListener.class)
public class SimpleRevisionEntity extends DefaultRevisionEntity  {
	/** */
	private static final long serialVersionUID = 2132857718334349075L;

	private String comment;
	private Long secUsrId;
	private String secUsrLogin;
	
	
	/**
	 * @return the secUsrId
	 */
    @Column(name="sec_usr_id", nullable = true)
	public Long getSecUsrId() {
		return secUsrId;
	}

	/**
	 * @param secUsrId the secUsrId to set
	 */
	public void setSecUsrId(Long secUsrId) {
		this.secUsrId = secUsrId;
	}

	/**
	 * @return the secUsrLogin
	 */
    @Column(name="sec_usr_login", nullable = true)
	public String getSecUsrLogin() {
		return secUsrLogin;
	}

	/**
	 * @param secUsrLogin the secUsrLogin to set
	 */
	public void setSecUsrLogin(String secUsrLogin) {
		this.secUsrLogin = secUsrLogin;
	}

	/**
	 * @return the comment
	 */
	@Column(name = "env_rev_comment", nullable = true)
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
