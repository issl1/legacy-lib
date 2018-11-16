package com.nokor.efinance.core.collection.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.frmk.security.model.SecUser;

/**
 * @author youhort.ly
 */
@Entity
@Table(name = "tu_col_team")
public class EColTeam extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = -1689786438480248606L;
	
	private List<SecUser> staffs;
	private EColType colType;
	private List<EColTeamDeptLevel> debtLevels;

	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_tea_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getCode()
	 */
	@Column(name = "col_tea_code", nullable = false, length=10)
	@Override
	public String getCode() {
		return code;
	}

	/**
	 * @see org.seuksa.frmk.model.entity.AuditEntityRef#getDesc()
	 */
	@Column(name = "col_tea_desc", nullable = true, length=255)
	@Override
    public String getDesc() {
        return desc;
    }
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityRefA#getDescEn()
	 */
	@Column(name = "col_tea_desc_en", nullable = false, length = 255)
	@Override
    public String getDescEn() {
        return descEn;
    }

	/**
	 * @return the staffs
	 */
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tu_col_team_staff",
				joinColumns = { @JoinColumn(name = "col_tea_id") }, 
				inverseJoinColumns = { @JoinColumn(name = "sec_usr_id") })
	public List<SecUser> getStaffs() {
		if (staffs == null) {
			staffs = new ArrayList<>();
		}
		return staffs;
	}

	/**
	 * @param staffs the staffs to set
	 */
	public void setStaffs(List<SecUser> staffs) {
		this.staffs = staffs;
	}

	/**
	 * @return the colType
	 */
	@Column(name = "col_typ_id", nullable = true)
	@Convert(converter = EColType.class)
	public EColType getColType() {
		return colType;
	}

	/**
	 * @param colType the colType to set
	 */
	public void setColType(EColType colType) {
		this.colType = colType;
	}
	
	/**
	 * @return the debtLevels
	 */
	@OneToMany(mappedBy="colTeam", fetch = FetchType.LAZY)
	public List<EColTeamDeptLevel> getDebtLevels() {
		return debtLevels;
	}

	/**
	 * @param debtLevels the debtLevels to set
	 */
	public void setDebtLevels(List<EColTeamDeptLevel> debtLevels) {
		this.debtLevels = debtLevels;
	}

}
