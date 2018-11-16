package com.nokor.efinance.core.collection.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author youhort.ly
 */
@Entity
@Table(name = "tu_col_weight")
public class EColWeight extends EntityRefA {
	
	/** */
	private static final long serialVersionUID = 5974907683171633401L;
	
	private SecUser staff;
	private Double distributionRate;
	
	/**
	 * @see org.seuksa.frmk.model.entity.EntityA#getId()
	 */
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_wgh_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the staff
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id", nullable = false, unique = true)
	public SecUser getStaff() {
		return staff;
	}

	/**
	 * @param staff the staff to set
	 */
	public void setStaff(SecUser staff) {
		this.staff = staff;
	}

	/**
	 * @return the distributionRate
	 */
	@Column(name = "col_wgh_rt_distribution", nullable = true)
	public Double getDistributionRate() {
		return distributionRate;
	}

	/**
	 * @param distributionRate the distributionRate to set
	 */
	public void setDistributionRate(Double distributionRate) {
		this.distributionRate = distributionRate;
	}	
}