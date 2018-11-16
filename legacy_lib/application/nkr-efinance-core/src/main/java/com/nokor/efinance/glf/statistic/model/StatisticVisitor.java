package com.nokor.efinance.glf.statistic.model;

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

import org.seuksa.frmk.model.entity.EntityA;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.ersys.core.hr.model.eref.EMediaPromoting;

/**
 * 
 * @author sok.vina
 *
 */
@Entity
@Table(name = "td_statistic_visitor")
public class StatisticVisitor extends EntityA {

    private static final long serialVersionUID = -4053267507390718585L;
    
    private Dealer dealer;
	private Integer numberVisitorDealer;
    private Integer numberVisitorCompany;
    private Integer numberVisitorApply;
    private EMediaPromoting wayOfKnowing;
    private EComplaintType complaint1;
    private EComplaintType complaint2;
    private EComplaintType complaint3;
    private EComplaintType complaint;
    
    private String familyName;
    private String firstName;
    private String phoneNumber;
    private String productFeedback;

	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sta_vist_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    
	/**
	 * @return the dealer
	 */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dea_id")
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}
	
    /**
	 * @return the numberVisiterDealer
	 */
	@Column(name = "sta_vist_num_dealer", nullable = true)
	public Integer getNumberVisitorDealer() {
		return numberVisitorDealer;
	}

	/**
	 * @param numberVisiterDealer the numberVisiterDealer to set
	 */
	public void setNumberVisitorDealer(Integer numberVisitorDealer) {
		this.numberVisitorDealer = numberVisitorDealer;
	}

	/**
	 * @return the numberVisiterCompany
	 */
	@Column(name = "sta_vist_num_company", nullable = true)
	public Integer getNumberVisitorCompany() {
		return numberVisitorCompany;
	}

	/**
	 * @param numberVisiterCompany the numberVisiterCompany to set
	 */
	public void setNumberVisitorCompany(Integer numberVisitorCompany) {
		this.numberVisitorCompany = numberVisitorCompany;
	}

	/**
	 * @return the numberVisiterApply
	 */
	@Column(name = "sta_vist_num_apply", nullable = true)
	public Integer getNumberVisitorApply() {
		return numberVisitorApply;
	}

	/**
	 * @param numberVisiterApply the numberVisiterApply to set
	 */
	public void setNumberVisitorApply(Integer numberVisitorApply) {
		this.numberVisitorApply = numberVisitorApply;
	}
    /**
	 * @return the wayOfKnowing
	 */
    @Column(name = "way_knw_id", nullable = true)
    @Convert(converter = EMediaPromoting.class)
	public EMediaPromoting getWayOfKnowing() {
		return wayOfKnowing;
	}

	/**
	 * @param wayOfKnowing the wayOfKnowing to set
	 */
	public void setWayOfKnowing(EMediaPromoting wayOfKnowing) {
		this.wayOfKnowing = wayOfKnowing;
	}
	/**
	 * @return the complaint1
	 */
    @Column(name = "com_typ_id1", nullable = true)
    @Convert(converter = EComplaintType.class)
	public EComplaintType getComplaint1() {
		return complaint1;
	}

	/**
	 * @param complaint1 the complaint1 to set
	 */
	public void setComplaint1(EComplaintType complaint1) {
		this.complaint1 = complaint1;
	}

	/**
	 * @return the complaint2
	 */
    @Column(name = "com_typ_id2", nullable = true)
    @Convert(converter = EComplaintType.class)
	public EComplaintType getComplaint2() {
		return complaint2;
	}

	/**
	 * @param complaint2 the complaint2 to set
	 */
	public void setComplaint2(EComplaintType complaint2) {
		this.complaint2 = complaint2;
	}

	/**
	 * @return the complaint3
	 */
    @Column(name = "com_typ_id3", nullable = true)
    @Convert(converter = EComplaintType.class)
	public EComplaintType getComplaint3() {
		return complaint3;
	}

	/**
	 * @param complaint3 the complaint3 to set
	 */
	public void setComplaint3(EComplaintType complaint3) {
		this.complaint3 = complaint3;
	}

	/**
	 * @return the familyName
	 */
	@Column(name = "sta_vist_family_name", nullable = true)
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @param familyName the familyName to set
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	/**
	 * @return the firstName
	 */
	@Column(name = "sta_vist_first_name", nullable = true)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the phoneNumber
	 */
	@Column(name = "sta_vist_phone_number", nullable = true)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the productFeedback
	 */
	@Column(name = "sta_vist_product_feedback", nullable = true)
	public String getProductFeedback() {
		return productFeedback;
	}

	/**
	 * @param productFeedback the productFeedback to set
	 */
	public void setProductFeedback(String productFeedback) {
		this.productFeedback = productFeedback;
	}

	/**
	 * @return the complaint
	 */
    @Column(name = "com_typ_id", nullable = true)
    @Convert(converter = EComplaintType.class)
	public EComplaintType getComplaint() {
		return complaint;
	}

	/**
	 * @param complaint the complaint to set
	 */
	public void setComplaint(EComplaintType complaint) {
		this.complaint = complaint;
	}

}
