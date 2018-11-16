package com.nokor.ersys.collab.membership.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.nokor.common.app.workflow.model.EntityWkf;
import com.nokor.ersys.core.hr.model.eref.EMediaPromoting;
import com.nokor.ersys.core.partner.model.Partner;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "td_member")
public class Member extends EntityWkf {
    /** */
	private static final long serialVersionUID = -5958219699389336879L;

	private Partner partner;
    private MemberType memberType;
    private ERemovedReason removedReason;
    
    private Date registeredDate;
    
    private EMediaPromoting knownBy;
    
    private Boolean isOnline;

    private List<MemberRegistration> registrations;

    /**
     * 
     * @return
     */
    public static Member createInstance() {
        Member mem = EntityFactory.createInstance(Member.class);
        return mem;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

	/**
	 * @return the partner
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "par_id", nullable = false)
	public Partner getPartner() {
		return partner;
	}

	/**
	 * @param partner the partner to set
	 */
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	/**
	 * @return the memberType
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_typ_id", nullable = false)
	public MemberType getMemberType() {
		return memberType;
	}

	/**
	 * @param memberType the memberType to set
	 */
	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	/**
	 * @return the removedReason
	 */
    @Column(name = "rem_rea_id", nullable = true)
    @Convert(converter = EFeeType.class)
	public ERemovedReason getRemovedReason() {
		return removedReason;
	}

	/**
	 * @param removedReason the removedReason to set
	 */
	public void setRemovedReason(ERemovedReason removedReason) {
		this.removedReason = removedReason;
	}

	/**
	 * @return the registeredDate
	 */
    @Column(name = "mem_registered_date", nullable = true)
	public Date getRegisteredDate() {
		return registeredDate;
	}

	/**
	 * @param registeredDate the registeredDate to set
	 */
	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	/**
	 * @return the knownBy
	 */
    @Column(name = "known_by_med_pro_id", nullable = true)
    @Convert(converter = EFeeType.class)
	public EMediaPromoting getKnownBy() {
		return knownBy;
	}

	/**
	 * @param knownBy the knownBy to set
	 */
	public void setKnownBy(EMediaPromoting knownBy) {
		this.knownBy = knownBy;
	}

	/**
	 * @return the isOnline
	 */
    @Column(name = "mem_is_on_line", nullable = true)
	public Boolean getIsOnline() {
		return isOnline;
	}

	/**
	 * @param isOnline the isOnline to set
	 */
	public void setIsOnline(Boolean isOnline) {
		this.isOnline = isOnline;
	}

	/**
	 * @return the registrations
	 */
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
	public List<MemberRegistration> getRegistrations() {
		return registrations;
	}

	/**
	 * @param registrations the registrations to set
	 */
	public void setRegistrations(List<MemberRegistration> registrations) {
		this.registrations = registrations;
	}

	
   
}
