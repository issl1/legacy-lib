package com.nokor.frmk.auditlog.envers.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import com.nokor.frmk.auditlog.envers.listener.revision.AuditTrackingRevisionListener;

/**
 * @see DefaultRevisionEntity
 * @author prasnar
 *
 */
@Entity
@Table(name = "_env_revision")
@RevisionEntity(AuditTrackingRevisionListener.class)
public class AuditTrackingRevisionEntity extends DefaultRevisionEntity  {
	/** */
	private static final long serialVersionUID = -2444809601368742624L;

	private String comment;
	private Long secUsrId;
	private String secUsrLogin;

//	@ElementCollection
//    @JoinTable(name = "_env_modified_entity2", joinColumns = @JoinColumn(name = "rev_id"))
//    @Column(name = "entity_class_name")
//    @ModifiedEntityNames
//    private Set<String> modifiedEntityNames;
//	/**
//	 * @return the modifiedEntityNames
//	 */
//	public Set<String> getModifiedEntityNames() {
//		return modifiedEntityNames;
//	}
//
//	/**
//	 * @param modifiedEntityNames the modifiedEntityNames to set
//	 */
//	public void setModifiedEntityNames(Set<String> modifiedEntityNames) {
//		this.modifiedEntityNames = modifiedEntityNames;
//	}
	
    @OneToMany(mappedBy="revision", cascade={CascadeType.ALL})
    private Set<ModifiedEntityTypeEntity> modifiedEntityTypes;
    
	
	/**
	 * @return the modifiedEntityTypes
	 */
	public Set<ModifiedEntityTypeEntity> getModifiedEntityTypes() {
		return modifiedEntityTypes;
	}
	
	/**
	 * @param modifiedEntityTypes the modifiedEntityTypes to set
	 */
	public void setModifiedEntityTypes(
			Set<ModifiedEntityTypeEntity> modifiedEntityTypes) {
		this.modifiedEntityTypes = modifiedEntityTypes;
	}
    
    /**
     * 
     * @param entityClassName
     */
    public void addModifiedEntityType(String entityClassName) {
    	if (modifiedEntityTypes == null) {
    		modifiedEntityTypes = new HashSet<ModifiedEntityTypeEntity>();
    	}
        modifiedEntityTypes.add(new ModifiedEntityTypeEntity(this, entityClassName));
    }
	
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
