package com.nokor.ersys.collab.wkgroup.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_wkg_member_position")
public class WkgMemberPosition extends EntityA {
	/** */
	private static final long serialVersionUID = -3704435736256179324L;

	private WkgMember member;
	private WkgPosition position;
	private Integer sortIndex;
	

	/**
	 * 
	 * @return
	 */
	public static WkgMemberPosition createInstance() {
        WkgMemberPosition wkg = EntityFactory.createInstance(WkgMemberPosition.class);
        return wkg;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkg_mem_pos_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	

	/**
	 * @return the member
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wkg_mem_id", nullable=false)
	public WkgMember getMember() {
		return member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(WkgMember member) {
		this.member = member;
	}

	/**
	 * @return the position
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wkg_pos_id", nullable = false)
	public WkgPosition getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(WkgPosition position) {
		this.position = position;
	}

	/**
     * @return the sortIndex
     */
    @Column(name = "sort_index", nullable = true)
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * @param sortIndex the sortIndex to set
     */
    public void setSortIndex(final Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

	

}
