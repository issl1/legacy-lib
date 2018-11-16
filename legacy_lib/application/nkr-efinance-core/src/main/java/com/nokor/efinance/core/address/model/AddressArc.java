package com.nokor.efinance.core.address.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nokor.ersys.core.hr.model.address.BaseAddress;

/**
 * @author ly.youhort
 */
@Entity
@Table(name="td_address_arc")
public class AddressArc extends BaseAddress {
	/** */
	private static final long serialVersionUID = 3555189123313644230L;

	/**
     * @see org.seuksa.frmk.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "add_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

}
