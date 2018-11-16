package com.nokor.ersys.collab.membership.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.model.entity.EntityRefA;

/**
 * 
 * @author prasnar
 *
 */
@Entity
@Table(name = "ts_membership_fee")
public class MembershipFee extends EntityRefA {
    /** */
	private static final long serialVersionUID = 7529294955490618557L;

	private Double turnoverAmount;
    private Double rateAmount;
    private Double cotisationAmountVatExcl;
    private Double minCotisationAmountVatExcl;
    private Double maxCotisationAmountVatExcl;
    private Double rateFirstYearAmount;
    private Double rateSecondYearAmount;
    
    private EFeeType feeType;
    private MemberType memberType;

    /**
     * 
     * @return
     */
    public static MembershipFee createInstance() {
    	MembershipFee fee = EntityFactory.createInstance(MembershipFee.class);
    	
    	return fee;
    }
    
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_fee_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    @Override
    @Column(name = "mem_fee_code")
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(final String code) {
        this.code = code;
    }

    @Override
    @Column(name = "mem_fee_desc")
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    @Override
    @Column(name = "mem_fee_desc_en")
    public String getDescEn() {
        return desc;
    }

    @Override
    public void setDescEn(final String desc) {
        this.desc = desc;
    }

 	/**
	 * @return the feeType
	 */
    @Column(name = "fee_typ_id", nullable = false)
    @Convert(converter = EFeeType.class)
	public EFeeType getFeeType() {
		return feeType;
	}

	/**
	 * @param feeType the feeType to set
	 */
	public void setFeeType(EFeeType feeType) {
		this.feeType = feeType;
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

	@Column(name = "mem_fee_amount_turnover")
    public Double getTurnoverAmount() {
        return turnoverAmount;
    }

    public void setTurnoverAmount(final Double turnoverAmount) {
        this.turnoverAmount = turnoverAmount;
    }

    @Column(name = "mem_fee_amount_rate")
    public Double getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(final Double rateAmount) {
        this.rateAmount = rateAmount;
    }

    @Column(name = "mem_fee_amount_cotisation_vat_excl")
    public Double getCotisationAmountVatExcl() {
        return cotisationAmountVatExcl;
    }

    public void setCotisationAmountVatExcl(final Double cotisationAmountVatExcl) {
        this.cotisationAmountVatExcl = cotisationAmountVatExcl;
    }


    @Column(name = "mem_fee_amount_min_cotisation_vat_excl")
	public Double getMinCotisationAmountVatExcl() {
		return minCotisationAmountVatExcl;
	}

	public void setMinCotisationAmountVatExcl(Double minCotisationAmountVatExcl) {
		this.minCotisationAmountVatExcl = minCotisationAmountVatExcl;
	}

	@Column(name = "mem_fee_amount_max_cotisation_vat_excl")
	public Double getMaxCotisationAmountVatExcl() {
		return maxCotisationAmountVatExcl;
	}

	public void setMaxCotisationAmountVatExcl(Double maxCotisationAmountVatExcl) {
		this.maxCotisationAmountVatExcl = maxCotisationAmountVatExcl;
	}

	/**
	 * @return the rateFirstYearAmount
	 */
	@Column(name = "mem_fee_rate_first_year")
	public Double getRateFirstYearAmount() {
		return rateFirstYearAmount;
	}

	/**
	 * @param rateFirstYearAmount the rateFirstYearAmount to set
	 */
	public void setRateFirstYearAmount(Double rateFirstYearAmount) {
		this.rateFirstYearAmount = rateFirstYearAmount;
	}

	/**
	 * @return the rateSecondYearAmount
	 */
	@Column(name = "mem_fee_rate_second_year")
	public Double getRateSecondYearAmount() {
		return rateSecondYearAmount;
	}

	/**
	 * @param rateSecondYearAmount the rateSecondYearAmount to set
	 */
	public void setRateSecondYearAmount(Double rateSecondYearAmount) {
		this.rateSecondYearAmount = rateSecondYearAmount;
	}

}
