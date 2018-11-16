package com.nokor.common.app.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.frmk.security.model.SecControl;

/**
 * List of actions (controls) 
 * An action is linked to a WkfFlowItem (fromStatus/toStatus)
 * 
 *  
 * @author prasnar
 * 
 */
@Entity
@Table(name = "tu_wkf_flow_control")
public class WkfFlowControl extends EntityA {
	/** */
	private static final long serialVersionUID = -4145821577096925053L;

	private SecControl control;
	private WkfFlowItem item;
	private Integer sortIndex;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkf_flo_ctl_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the control
	 */
    @ManyToOne
    @JoinColumn(name="sec_ctl_id", nullable = false)
	public SecControl getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(SecControl control) {
		this.control = control;
	}

	/**
	 * @return the item
	 */
    @ManyToOne
    @JoinColumn(name="wkf_flo_ite_id", nullable = true)
	public WkfFlowItem getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(WkfFlowItem item) {
		this.item = item;
	}

	/**
     * @return SortIndex
     */
    @Column(name = "sort_index")
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param sortIndex
     */
    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }
}
