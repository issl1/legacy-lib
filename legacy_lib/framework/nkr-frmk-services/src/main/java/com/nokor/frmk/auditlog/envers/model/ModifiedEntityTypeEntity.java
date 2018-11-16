/**
 * 
 */
package com.nokor.frmk.auditlog.envers.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author prasnar
 *
 */
@Entity
@Table(name = "_env_modified_entity")
public class ModifiedEntityTypeEntity {
	@Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private AuditTrackingRevisionEntity revision;
    
    private String entityClassName;

    /**
     * 
     * @param revEntity
     * @param entityClassName
     */
    public ModifiedEntityTypeEntity(AuditTrackingRevisionEntity revEntity, String entityClassName) {
    	this.revision = revEntity;
    	this.entityClassName = entityClassName;
    }
    
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the revision
	 */
	public AuditTrackingRevisionEntity getRevision() {
		return revision;
	}

	/**
	 * @param revision the revision to set
	 */
	public void setRevision(AuditTrackingRevisionEntity revision) {
		this.revision = revision;
	}

	/**
	 * @return the entityClassName
	 */
	public String getEntityClassName() {
		return entityClassName;
	}

	/**
	 * @param entityClassName the entityClassName to set
	 */
	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}
    
    
}
