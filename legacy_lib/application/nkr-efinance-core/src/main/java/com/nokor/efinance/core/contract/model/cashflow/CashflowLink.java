package com.nokor.efinance.core.contract.model.cashflow;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author uhout.cheng
 */
@Entity
@Table(name = "td_cashflow_link")
public class CashflowLink  extends EntityA {

	/** */
	private static final long serialVersionUID = 3885055315974083310L;
	
	private Cashflow parent;
	private Cashflow child;
	private ECashflowLinkType linkType;
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafln_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the parent
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "caflw_id_parent")
	public Cashflow getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Cashflow parent) {
		this.parent = parent;
	}

	/**
	 * @return the child
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "caflw_id_child")
	public Cashflow getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(Cashflow child) {
		this.child = child;
	}

	/**
	 * @return the linkType
	 */
	@Column(name = "cfw_lnk_typ_id", nullable = true)
    @Convert(converter = ECashflowLinkType.class)
	public ECashflowLinkType getLinkType() {
		return linkType;
	}

	/**
	 * @param linkType the linkType to set
	 */
	public void setLinkType(ECashflowLinkType linkType) {
		this.linkType = linkType;
	}	
}
