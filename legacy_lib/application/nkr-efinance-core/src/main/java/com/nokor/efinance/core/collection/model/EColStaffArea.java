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

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.address.model.Area;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author buntha.chea
 *
 */
@Entity
@Table(name = "tu_col_staff_area")
public class EColStaffArea extends EntityA {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6981644420652146777L;
	
	private SecUser user;
	private Area area;

	@Override
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "col_staff_area_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * @return the user
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_usr_id", nullable = true)
	public SecUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(SecUser user) {
		this.user = user;
	}

	/**
	 * @return the area
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "are_id", nullable = true)
	public Area getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}

}
