package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * @author prasnar
 */
@Entity
@Table(name = "tu_lock_split_rule")
public class LockSplitRule extends EntityRefA {
	
	private boolean isDefault;
	private List<LockSplitRuleItem> lockSplitRuleItems; 
	
	/**
	 */
	private static final long serialVersionUID = 3893753410944577908L;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_spl_rul_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	@Override
	@Transient
    public String getCode() {
        return "XXX";
    }

    /**
     * @return <String>
     */
    @Override
    @Column(name = "loc_spl_rul_desc", nullable = true, length = 255)
    public String getDesc() {
        return super.getDesc();
    }

    /**
     * @return <String>
     */
    @Override
    @Column(name = "loc_spl_rul_desc_en", nullable = false, length = 255)
    public String getDescEn() {
        return super.getDescEn();
    }
	
	/**
	 * @return the isDefault
	 */
    @Column(name = "loc_spl_rul_bl_default", nullable = true, columnDefinition = "boolean default false")
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the lockSplitRuleItems
	 */
	@OneToMany(mappedBy="lockSplitRule", fetch = FetchType.LAZY)
	@OrderBy("priority")
	public List<LockSplitRuleItem> getLockSplitRuleItems() {
		return lockSplitRuleItems;
	}

	/**
	 * @param lockSplitRuleItems the lockSplitRuleItems to set
	 */
	public void setLockSplitRuleItems(List<LockSplitRuleItem> lockSplitRuleItems) {
		this.lockSplitRuleItems = lockSplitRuleItems;
	}		
}
