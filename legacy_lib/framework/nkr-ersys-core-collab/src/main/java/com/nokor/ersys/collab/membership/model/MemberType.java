package com.nokor.ersys.collab.membership.model;

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

import org.seuksa.frmk.model.entity.EntityRefA;

import com.nokor.ersys.core.hr.model.eref.EEmploymentActivity;
import com.nokor.ersys.core.hr.model.eref.EEmploymentIndustry;


/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "tu_type_member")
public class MemberType extends EntityRefA {
    /** */
    private static final long serialVersionUID = -4881859225905579L;

	public static long PERMANENT = 1L;
    public static long NORMAL = 2L;
    public static long INVIDUAL = 3L;
    public static long SCIENTIFIC_COUNCIL = 4L;
    public static long HONOR = 5L;
    public static long EXPERT = 6L;
	
    private MembershipFee fee;
    
    private EEmploymentIndustry industry;
    private EEmploymentActivity activity;
    
    private List<RegistrationDocTypeMember> documentTypeMembers;
    private List<Member> members;

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityA#getId()
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_typ_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getCode()
     */
    @Column(name = "mem_typ_code", nullable = false)
    @Override
    public String getCode() {
        return code;
    }

    /**
     * @see org.seuksa.frmk.mvc.model.entity.EntityRefA#getDesc()
     */
    @Column(name = "mem_typ_desc", nullable = false)
    @Override
    public String getDesc() {
        return desc;
    }

	/**
	 * @return the fee
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_fee_id", nullable = false)
	public MembershipFee getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(MembershipFee fee) {
		this.fee = fee;
	}

	/**
	 * @return the industry
	 */
    @Column(name = "emp_ind_id", nullable = true)
    @Convert(converter = EEmploymentIndustry.class)
	public EEmploymentIndustry getIndustry() {
		return industry;
	}

	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(EEmploymentIndustry industry) {
		this.industry = industry;
	}

	/**
	 * @return the activity
	 */
    @Column(name = "emp_act_id", nullable = true)
    @Convert(converter = EEmploymentIndustry.class)
	public EEmploymentActivity getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(EEmploymentActivity activity) {
		this.activity = activity;
	}

	/**
	 * @return the documentTypeMembers
	 */
	@OneToMany(mappedBy = "memberType", fetch = FetchType.LAZY)
	public List<RegistrationDocTypeMember> getDocumentTypeMembers() {
		return documentTypeMembers;
	}

	/**
	 * @param documentTypeMembers the documentTypeMembers to set
	 */
	public void setDocumentTypeMembers(List<RegistrationDocTypeMember> documentTypeMembers) {
		this.documentTypeMembers = documentTypeMembers;
	}

	/**
	 * @return the members
	 */
	@OneToMany(mappedBy = "memberType", fetch = FetchType.LAZY)
	public List<Member> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(List<Member> members) {
		this.members = members;
	}

    
}
