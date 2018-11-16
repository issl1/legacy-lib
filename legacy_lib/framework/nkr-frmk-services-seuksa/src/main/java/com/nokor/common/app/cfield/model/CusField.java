package com.nokor.common.app.cfield.model;

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

import org.seuksa.frmk.model.entity.EntityRefA;


/**
 * Customize fields
 * Field 
 * @author prasnar
 */
@Entity
@Table(name = "ts_cus_field")
public class CusField extends EntityRefA {
	/** */
	private static final long serialVersionUID = -4113368515028682669L;

	private CusTable table;
	private ECusType type;
	private String format;
	private Boolean isMandatory;
	private Boolean isNullable;
	private Boolean isEncrypted;
	private String defaultValue;
	private String constraintValues;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cus_fie_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "cus_fie_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "cus_fie_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the table
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cus_tab_id", nullable = false)
	public CusTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(CusTable table) {
		this.table = table;
	}

	/**
	 * @return the type
	 */
    @Column(name = "cus_typ_id", nullable = false)
    @Convert(converter = ECusType.class)
	public ECusType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ECusType type) {
		this.type = type;
	}

	/**
	 * @return the format
	 */
    @Column(name = "cus_fie_format", nullable = true)
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the isMandatory
	 */
    @Column(name = "cus_fie_is_mandatory", nullable = false)
	public Boolean getIsMandatory() {
		return isMandatory;
	}

	/**
	 * @param isMandatory the isMandatory to set
	 */
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	/**
	 * @return the isNullable
	 */
    @Column(name = "cus_fie_is_nullable", nullable = false)
	public Boolean getIsNullable() {
		return isNullable;
	}

	/**
	 * @param isNullable the isNullable to set
	 */
	public void setIsNullable(Boolean isNullable) {
		this.isNullable = isNullable;
	}

	/**
	 * @return the isEncrypted
	 */
    @Column(name = "cus_fie_is_encrypted", nullable = false)
	public Boolean getIsEncrypted() {
		return isEncrypted;
	}

	/**
	 * @param isEncrypted the isEncrypted to set
	 */
	public void setIsEncrypted(Boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

	/**
	 * @return the defaultValue
	 */
    @Column(name = "cus_fie_default_value", nullable = true)
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the constraintValues
	 */
    @Column(name = "cus_fie_constraint_values", nullable = true)
	public String getConstraintValues() {
		return constraintValues;
	}

	/**
	 * @param constraintValues the constraintValues to set
	 */
	public void setConstraintValues(String constraintValues) {
		this.constraintValues = constraintValues;
	}

    
   
}
