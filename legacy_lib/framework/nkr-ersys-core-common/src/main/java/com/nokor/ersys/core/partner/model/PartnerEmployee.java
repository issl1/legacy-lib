package com.nokor.ersys.core.partner.model;

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

import com.nokor.ersys.core.hr.model.organization.Staff;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="tu_partner_employee")
public class PartnerEmployee extends Staff {
	/** */
	private static final long serialVersionUID = 4812161287088868683L;

	private Partner partner;
	

	/**
     * 
     * @return
     */
    public static PartnerEmployee createInstance() {
        PartnerEmployee emp = EntityFactory.createInstance(PartnerEmployee.class);
        return emp;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "par_emp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "par_id")
 	public Partner getPartner() {
		return partner;
	}

    /**
	 * 
	 * @param partner
	 */
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
}
