package com.nokor.ersys.collab.wkgroup.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.ersys.core.hr.model.organization.Employee;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_wkg_member")
public class WkgMember extends EntityA {
	/** */
	private static final long serialVersionUID = -9086308519853592658L;

	private WkgGroup group;
	private Employee employee;
	private Boolean showInGroup;
	private Integer sortIndex;

	private List<WkgMemberPosition> positions;

	/**
	 * 
	 * @return
	 */
	public static WkgMember createInstance() {
        WkgMember wkg = EntityFactory.createInstance(WkgMember.class);
        wkg.setEmployee(Employee.createInstance());
        wkg.setGroup(WkgGroup.createInstance());
        return wkg;
    }
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wkg_mem_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the group
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wkg_id", nullable=false)
	public WkgGroup getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(WkgGroup group) {
		this.group = group;
	}
	

	/**
	 * @return the showInGroup
	 */
    @Column(name = "wkg_mem_show_in_group", nullable = true)
	public Boolean getShowInGroup() {
		return showInGroup;
	}

	/**
	 * @param showInGroup the showInGroup to set
	 */
	public void setShowInGroup(Boolean showInGroup) {
		this.showInGroup = showInGroup;
	}

	/**
	 * @return the positions
	 */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	public List<WkgMemberPosition> getPositions() {
    	if (positions == null) {
    		positions = new ArrayList<WkgMemberPosition>();
    	}
		return positions;
	}

	/**
	 * @param positions the positions to set
	 */
	public void setPositions(List<WkgMemberPosition> positions) {
		this.positions = positions;
	}

	/**
	 * @return the employee
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable=false)
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
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
