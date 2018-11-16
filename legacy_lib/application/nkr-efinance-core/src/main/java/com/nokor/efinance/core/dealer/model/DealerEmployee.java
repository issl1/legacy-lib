package com.nokor.efinance.core.dealer.model;

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
import javax.persistence.Transient;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.IVersion;

import com.nokor.ersys.core.hr.model.organization.Staff;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name="tu_dealer_employee")
public class DealerEmployee extends Staff implements IVersion {
	
	/** */
	private static final long serialVersionUID = 5359689117110682174L;
	
	private Dealer company;
	private List<DealerEmployeeContactInfo> dealerEmployeeContactInfos;

	/**
     * 
     * @return
     */
    public static DealerEmployee createInstance() {
        DealerEmployee emp = EntityFactory.createInstance(DealerEmployee.class);
        return emp;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dea_emp_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
 	public Dealer getCompany() {
		return company;
	}

    /**
	 * 
	 * @param company
	 */
	public void setCompany(Dealer company) {
		this.company = company;
	}
	
	@Override
	@Transient
	public boolean isWkfEnabled() {
		return false;
	}

	/**
	 * @return the dealerEmployeeContactInfos
	 */
	@OneToMany(mappedBy="dealerEmployee", fetch = FetchType.LAZY)
	public List<DealerEmployeeContactInfo> getDealerEmployeeContactInfos() {
		return dealerEmployeeContactInfos;
	}

	/**
	 * @param dealerEmployeeContactInfos the dealerEmployeeContactInfos to set
	 */
	public void setDealerEmployeeContactInfos(List<DealerEmployeeContactInfo> dealerEmployeeContactInfos) {
		this.dealerEmployeeContactInfos = dealerEmployeeContactInfos;
	}
}

