package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_lock_split_type")
public class ELockSplitType extends EntityRefA implements MLockSplitType {
		
	/**
	 */
	private static final long serialVersionUID = 3893753410944577908L;
	
	private List<ELockSplitCashflowType> lockSplitCashflowTypes; 
	private ELockSplitGroup lockSplitGroup;
	
	/**
     * 
     * @return
     */
    public static ELockSplitType createInstance() {
    	ELockSplitType instance = EntityFactory.createInstance(ELockSplitType.class);
        return instance;
    }

    /**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_spl_typ_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "loc_spl_typ_code", nullable = false, length = 10)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "loc_spl_typ_desc", nullable = true, length = 255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "loc_spl_typ_desc_en", nullable = false, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }
	
	/**
	 * @return the lockSplitGroup
	 */
	@Column(name = "loc_spl_grp_id", nullable = true)
    @Convert(converter = ELockSplitGroup.class)
	public ELockSplitGroup getLockSplitGroup() {
		return lockSplitGroup;
	}

	/**
	 * @param lockSplitGroup the lockSplitGroup to set
	 */
	public void setLockSplitGroup(ELockSplitGroup lockSplitGroup) {
		this.lockSplitGroup = lockSplitGroup;
	}

	/**
	 * @return the lockSplitCashflowTypes
	 */
	@OneToMany(mappedBy="lockSplitType", fetch = FetchType.LAZY)
	@OrderBy("priority")
	public List<ELockSplitCashflowType> getLockSplitCashflowTypes() {
		return lockSplitCashflowTypes;
	}

	/**
	 * @param lockSplitCashflowTypes the lockSplitCashflowTypes to set
	 */
	public void setLockSplitCashflowTypes(
			List<ELockSplitCashflowType> lockSplitCashflowTypes) {
		this.lockSplitCashflowTypes = lockSplitCashflowTypes;
	}		
}
