package com.nokor.frmk.config.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EntityRefA;
import org.seuksa.frmk.tools.exception.EntityAlreadyExistsException;
import org.seuksa.frmk.tools.exception.EntityNotValidParameterException;

import com.nokor.frmk.helper.SeuksaServicesHelper;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "ts_ref_data",
	indexes = {
		@Index(name = "ts_ref_data_ref_dat_code_idx", columnList = "ref_dat_code"),
		@Index(name = "ts_ref_data_ref_tab_id_idx", columnList = "ref_tab_id")
	}
)
public class RefData extends EntityRefA implements MRefData {
    /** */
	private static final long serialVersionUID = 766915489657824778L;

	private RefTable table;
    private Long ide;
    private String fieldValue1;
    private String fieldValue2;
    private String fieldValue3;
    private String fieldValue4;
    private String fieldValue5;
    

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ref_dat_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getCode()
     */
    @Column(name = "ref_dat_code", nullable = false)
    @Override
   	public String getCode() {
   		return code;
   	}


    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "ref_dat_desc", nullable = false)
   	@Override	
    public String getDesc() {
        return desc;
    }
    
    /**
     * @see org.seuksa.frmk.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "ref_dat_desc_en", nullable = false)
   	@Override	
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the table
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_tab_id", nullable = false)
	public RefTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(RefTable table) {
		this.table = table;
	}

	/**
     * 
     * @return
     */
    @Transient
    public boolean isEntity() {
    	return getTable().isEntity();
    }
    
	/**
	 * @return the ide
	 */
    @Column(name = "ref_dat_ide", nullable = false)
	public Long getIde() {
		return ide;
	}

	/**
	 * @param ide the ide to set
	 */
	public void setIde(Long ide) {
		this.ide = ide;
	}

	/**
	 * @return the fieldValue1
	 */
    @Column(name = "ref_dat_value1", nullable = true)
	public String getFieldValue1() {
		return fieldValue1;
	}

	/**
	 * @param fieldValue1 the fieldValue1 to set
	 */
	public void setFieldValue1(String fieldValue1) {
		this.fieldValue1 = fieldValue1;
	}

	/**
	 * @return the fieldValue2
	 */
    @Column(name = "ref_dat_value2", nullable = true)
	public String getFieldValue2() {
		return fieldValue2;
	}

	/**
	 * @param fieldValue2 the fieldValue2 to set
	 */
	public void setFieldValue2(String fieldValue2) {
		this.fieldValue2 = fieldValue2;
	}

	/**
	 * @return the fieldValue3
	 */
    @Column(name = "ref_dat_value3", nullable = true)
	public String getFieldValue3() {
		return fieldValue3;
	}

	/**
	 * @param fieldValue3 the fieldValue3 to set
	 */
	public void setFieldValue3(String fieldValue3) {
		this.fieldValue3 = fieldValue3;
	}

	/**
	 * @return the fieldValue4
	 */
    @Column(name = "ref_dat_value4", nullable = true)
	public String getFieldValue4() {
		return fieldValue4;
	}

	/**
	 * @param fieldValue4 the fieldValue4 to set
	 */
	public void setFieldValue4(String fieldValue4) {
		this.fieldValue4 = fieldValue4;
	}

	/**
	 * @return the fieldValue5
	 */
    @Column(name = "ref_dat_value5", nullable = true)
	public String getFieldValue5() {
		return fieldValue5;
	}

	/**
	 * @param fieldValue5 the fieldValue5 to set
	 */
	public void setFieldValue5(String fieldValue5) {
		this.fieldValue5 = fieldValue5;
	}

	@Override
	public void checkForCreation() {
		super.checkForCreation();
		
		String errMsg = null;
		
		// 
		if (getIde() == null) {
			errMsg = I18N.messageMandatoryField(RefData.IDE);
			throw new EntityNotValidParameterException(errMsg);
		}
		
		if (SeuksaServicesHelper.REFDATA_SRV.existsCode(getTable().getCode(), getCode())) {
			throw new EntityAlreadyExistsException(I18N.messageObjectAlreadyExists("RefData [" + getTable().getCode() + "] [" + getCode() + "]"));
		}
		
	}
	
	@Override
	public void checkForUpdate() {
		super.checkForUpdate();
		String errMsg = null;
		
		// 
		if (getIde() == null) {
			errMsg = I18N.messageMandatoryField(RefData.IDE);
			throw new EntityNotValidParameterException(errMsg);
		}
		
		if (!SeuksaServicesHelper.REFDATA_SRV.existsIde(getTable().getCode(), getIde())) {
			throw new EntityAlreadyExistsException("RefData [" + getTable().getCode() + "] [" + getIde() + "] - Not found");
		}
	}
}
