package com.nokor.efinance.core.collection.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.frmk.security.model.SecUser;

/**
 * A Group covers an area with other infos (to define later)  
 * 
 * @author prasnar
 */
@Entity
@Table(name = "tu_col_group")
public class EColGroup extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = 5974907683171633401L;
	
	private List<SecUser> staffs;
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_grp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "col_grp_code", nullable = false, length=10)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "col_grp_desc", nullable = true, length=255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "col_grp_desc_en", nullable = false, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }
	
	/**
	 * @return the colTypes
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_col_group_staff",
				joinColumns = { @JoinColumn(name = "col_grp_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "sec_usr_id") })
	public List<SecUser> getStaffs() {
		return staffs;
	}

	/**
	 * @param staffs the staffs to set
	 */
	public void setStaffs(List<SecUser> staffs) {
		this.staffs = staffs;
	}
}