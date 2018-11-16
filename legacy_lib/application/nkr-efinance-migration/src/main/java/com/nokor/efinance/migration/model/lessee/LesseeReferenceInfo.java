package com.nokor.efinance.migration.model.lessee;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tm_lessee_reference_info")
public class LesseeReferenceInfo extends EntityRefA implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 3908018641828870550L;
	
	private Long id;
	private String migrationID;
	private String referenceTypeCode;
	private String relationshipCode;
	private String firstName;
	private String lastName;
							
	private String typeInfoCode;
	private String typeAddressCode;
	private String value;
	/**
	 * @return the id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
	 * @return the migrationID
	 */
	public String getMigrationID() {
		return migrationID;
	}
	/**
	 * @param migrationID the migrationID to set
	 */
	public void setMigrationID(String migrationID) {
		this.migrationID = migrationID;
	}
	/**
	 * @return the referenceTypeCode
	 */
	public String getReferenceTypeCode() {
		return referenceTypeCode;
	}
	/**
	 * @param referenceTypeCode the referenceTypeCode to set
	 */
	public void setReferenceTypeCode(String referenceTypeCode) {
		this.referenceTypeCode = referenceTypeCode;
	}
	/**
	 * @return the relationshipCode
	 */
	public String getRelationshipCode() {
		return relationshipCode;
	}
	/**
	 * @param relationshipCode the relationshipCode to set
	 */
	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the typeInfoCode
	 */
	public String getTypeInfoCode() {
		return typeInfoCode;
	}
	/**
	 * @param typeInfoCode the typeInfoCode to set
	 */
	public void setTypeInfoCode(String typeInfoCode) {
		this.typeInfoCode = typeInfoCode;
	}
	/**
	 * @return the typeAddressCode
	 */
	public String getTypeAddressCode() {
		return typeAddressCode;
	}
	/**
	 * @param typeAddressCode the typeAddressCode to set
	 */
	public void setTypeAddressCode(String typeAddressCode) {
		this.typeAddressCode = typeAddressCode;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
		
}
