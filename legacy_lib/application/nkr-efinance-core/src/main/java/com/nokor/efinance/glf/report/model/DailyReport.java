package com.nokor.efinance.glf.report.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.seuksa.frmk.model.entity.EntityA;
import org.seuksa.frmk.tools.MyMathUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.dealer.model.Dealer;

/**
 * 
 * @author sok.vina
 *
 */
@Entity
@Table(name = "tm_daily_report")
public class DailyReport extends EntityA {
	
	private static final long serialVersionUID = -5850483262377014737L;
	
	private Date date;
	private Dealer dealer;
	private Long dealerVisitor;
	private Long companyVisitor;
	private Long companyApply;	
	private Long apply;
	private Long inProcessAtPoS;
	private Long inProcessAtUW;
	private Long reject;
	private Long approve;
	private Long pendingNewContract;
	private Long decline;
	private Long pendingPurchaseOrder;
	private Long newContract;
	
	private Long dealerAccumulateNewContracts;
	private Long nbDealerContractsUntilLastMonth;
	
	private Long nbDealerApplicationsLastMonthFromBeginToDate;
	private Long nbDealerContractsLastMonthFromBeginToDate;
	private Long nbDealerFieldChecksLastMonthFromBeginToDate;
	
	private Long nbDealerApplicationsFromBeginOfMonth;
	private Long nbDealerContractsFromBeginOfMonth;	
	private Long nbDealerFieldChecksFromBeginOfMonth;
	
	
	private Long nbDealerVisitorsLastMonthFromBeginToDate;
	private Long nbDealerVisitorsFromBeginOfMonth;
	private boolean closed;
	
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "repda_id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	/**
	 * @return the date
	 */
	@Column(name = "repda_date", nullable = true)
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
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
	 * @return the dealerVisitor
	 */
	@Transient
	public Long getDealerVisitor() {
		return dealerVisitor;
	}
	/**
	 * @param dealerVisitor the dealerVisitor to set
	 */
	public void setDealerVisitor(Long dealerVisitor) {
		this.dealerVisitor = dealerVisitor;
	}
	/**
	 * @return the companyVisitor
	 */
	@Transient
	public Long getCompanyVisitor() {
		return companyVisitor;
	}
	/**
	 * @param companyVisitor the companyVisitor to set
	 */
	public void setCompanyVisitor(Long companyVisitor) {
		this.companyVisitor = companyVisitor;
	}	
	
	/**
	 * @return the companyApply
	 */
	@Transient
	public Long getCompanyApply() {
		return companyApply;
	}

	/**
	 * @param companyApply the companyApply to set
	 */
	public void setCompanyApply(Long companyApply) {
		this.companyApply = companyApply;
	}

	/**
	 * @return the apply
	 */
	@Column(name = "repda_nu_apply", nullable = true)
	public Long getApply() {
		return apply;
	}
	/**
	 * @param apply the apply to set
	 */
	public void setApply(Long apply) {
		this.apply = apply;
	}

	/**
	 * @return the inProcessAtPoS
	 */
	@Column(name = "repda_nu_in_process_pos", nullable = true)
	public Long getInProcessAtPoS() {
		return inProcessAtPoS;
	}
	/**
	 * @param inProcessAtPoS the inProcessAtPoS to set
	 */
	public void setInProcessAtPoS(Long inProcessAtPoS) {
		this.inProcessAtPoS = inProcessAtPoS;
	}
	/**
	 * @return the inProcessAtUW
	 */
	@Column(name = "repda_nu_in_process_uw", nullable = true)
	public Long getInProcessAtUW() {
		return inProcessAtUW;
	}
	/**
	 * @param inProcessAtUW the inProcessAtUW to set
	 */
	public void setInProcessAtUW(Long inProcessAtUW) {
		this.inProcessAtUW = inProcessAtUW;
	}
	/**
	 * @return the reject
	 */
	@Column(name = "repda_nu_reject", nullable = true)
	public Long getReject() {
		return reject;
	}
	/**
	 * @param reject the reject to set
	 */
	public void setReject(Long reject) {
		this.reject = reject;
	}

	/**
	 * @return the approve
	 */
	@Column(name = "repda_nu_approve", nullable = true)
	public Long getApprove() {
		return approve;
	}
	/**
	 * @param approve the approve to set
	 */
	public void setApprove(Long approve) {
		this.approve = approve;
	}
	/**
	 * @return the pendingNewContract
	 */
	@Column(name = "repda_nu_pending_new_contract", nullable = true)
	public Long getPendingNewContract() {
		return pendingNewContract;
	}
	/**
	 * @param pendingNewContract the pendingNewContract to set
	 */
	public void setPendingNewContract(Long pendingNewContract) {
		this.pendingNewContract = pendingNewContract;
	}
	/**
	 * @return the decline
	 */
	@Column(name = "repda_nu_decline", nullable = true)
	public Long getDecline() {
		return decline;
	}
	/**
	 * @param decline the decline to set
	 */
	public void setDecline(Long decline) {
		this.decline = decline;
	}

	/**
	 * @return the newContra
	 */
	@Column(name = "repda_nu_new_contract", nullable = true)
	public Long getNewContract() {
		return newContract;
	}
	/**
	 * @param newContra the newContra to set
	 */
	public void setNewContract(Long newContract) {
		this.newContract = newContract;
	}
	
	/**
	 * @return the pendingPurchaseOrder
	 */
	@Column(name = "repda_nu_pending_purchase_order", nullable = true)
	public Long getPendingPurchaseOrder() {
		return pendingPurchaseOrder;
	}

	/**
	 * @param pendingPurchaseOrder the pendingPurchaseOrder to set
	 */
	public void setPendingPurchaseOrder(Long pendingPurchaseOrder) {
		this.pendingPurchaseOrder = pendingPurchaseOrder;
	}	

	/**
	 * @return the dealerVisitorPercentage
	 */
	@Transient
	public Double getDealerVisitorsPercentage() {
		if (date != null) {		    
		    long totalVisitorLastMonth = MyNumberUtils.getLong(getNbDealerVisitorsLastMonthFromBeginToDate());
		    long dealerVisitorFromBeginOfMonth = MyNumberUtils.getLong(getNbDealerVisitorsFromBeginOfMonth());
		    
		    if (dealer != null) {
		    	System.out.println(dealer.getNameEn() + " : " + dealerVisitorFromBeginOfMonth + ", "+ totalVisitorLastMonth);
		    }
		    
		    if (totalVisitorLastMonth > 0) {
		    	double totalVisitorsDealer = ((double) dealerVisitorFromBeginOfMonth / totalVisitorLastMonth) - 1;
		    	return MyMathUtils.roundTo(totalVisitorsDealer * 100, 2);
		    }
		}
		return 0.00d;
	}

	/**
	 * @return the applicationsPercentage
	 */
	@Transient
	public Double getApplicationsPercentage() {
		if (date != null) {
	    	long nbApplicationsLastMonth = MyNumberUtils.getLong(getNbDealerApplicationsLastMonthFromBeginToDate());
	    	long nbApplicationsFromBeginOfMonth = MyNumberUtils.getLong(getNbDealerApplicationsFromBeginOfMonth());
	    	if (nbApplicationsLastMonth > 0) {
		    	double totalApplications = ((double) nbApplicationsFromBeginOfMonth / nbApplicationsLastMonth) - 1;
				return MyMathUtils.roundTo(totalApplications * 100, 2);
	    	}
		}
		return 0.00d;
	}

	/**
	 * @return the fieldChecksPercentage
	 */
	@Transient
	public Double getFieldChecksPercentage() {
		if (date != null) {
	    	long nbFieldChecksLastMonth = MyNumberUtils.getLong(getNbDealerFieldChecksLastMonthFromBeginToDate());
	    	long nbFieldChecksFromBeginOfMonth = MyNumberUtils.getLong(getNbDealerFieldChecksFromBeginOfMonth());
	    	if (nbFieldChecksLastMonth > 0) {
		    	double totalFieldChecks = ((double) nbFieldChecksFromBeginOfMonth / nbFieldChecksLastMonth) - 1;
				return MyMathUtils.roundTo(totalFieldChecks * 100, 2);
	    	}
		}
		return 0.00d;
	}

	/**
	 * @return the contractsPercentage
	 */
	@Transient
	public Double getContractsPercentage() {
		if (date != null) {
	    	long nbContractsLastMonth = MyNumberUtils.getLong(getNbDealerContractsLastMonthFromBeginToDate());
	    	long nbContractsFromBeginOfMonth = MyNumberUtils.getLong(getNbDealerContractsFromBeginOfMonth());
	    	
	    	if (nbContractsLastMonth > 0) {
				double totalContracts = ((double) nbContractsFromBeginOfMonth / nbContractsLastMonth) - 1;
				return MyMathUtils.roundTo(totalContracts * 100, 2);
	    	}
		}
		return 0.00d;
	}

	/**
	 * @return the dealerAccumulateNewContracts
	 */
	@Column(name = "repda_nu_dealer_accumulate_new_contracts", nullable = true)
	public Long getDealerAccumulateNewContracts() {
		return dealerAccumulateNewContracts;
	}

	/**
	 * @param dealerAccumulateNewContracts the dealerAccumulateNewContracts to set
	 */
	public void setDealerAccumulateNewContracts(Long dealerAccumulateNewContracts) {
		this.dealerAccumulateNewContracts = dealerAccumulateNewContracts;
	}
	
	
	/**
	 * @return the nbDealerContractsUntilLastMonth
	 */
	@Column(name = "repda_nu_dealer_contracts_until_last_month", nullable = true)
	public Long getNbDealerContractsUntilLastMonth() {
		return nbDealerContractsUntilLastMonth;
	}

	/**
	 * @param nbDealerContractsUntilLastMonth the nbDealerContractsUntilLastMonth to set
	 */
	public void setNbDealerContractsUntilLastMonth(Long nbDealerContractsUntilLastMonth) {
		this.nbDealerContractsUntilLastMonth = nbDealerContractsUntilLastMonth;
	}
		
	/**
	 * @return the nbDealerApplicationsFromBeginOfMonth
	 */
	@Column(name = "repda_nu_dealer_applications_from_begin_month", nullable = true)
	public Long getNbDealerApplicationsFromBeginOfMonth() {
		return nbDealerApplicationsFromBeginOfMonth;
	}

	/**
	 * @param nbDealerApplicationsFromBeginOfMonth the nbDealerApplicationsFromBeginOfMonth to set
	 */
	public void setNbDealerApplicationsFromBeginOfMonth(
			Long nbDealerApplicationsFromBeginOfMonth) {
		this.nbDealerApplicationsFromBeginOfMonth = nbDealerApplicationsFromBeginOfMonth;
	}

	/**
	 * @return the nbDealerContractsFromBeginOfMonth
	 */
	@Column(name = "repda_nu_dealer_contracts_from_begin_month", nullable = true)
	public Long getNbDealerContractsFromBeginOfMonth() {
		return nbDealerContractsFromBeginOfMonth;
	}

	/**
	 * @param nbDealerContractsFromBeginOfMonth the nbDealerContractsFromBeginOfMonth to set
	 */
	public void setNbDealerContractsFromBeginOfMonth(Long nbDealerContractsFromBeginOfMonth) {
		this.nbDealerContractsFromBeginOfMonth = nbDealerContractsFromBeginOfMonth;
	}	

	/**
	 * @return the nbDealerApplicationsLastMonthFromBeginToDate
	 */
	@Column(name = "repda_nu_dealer_applications_last_month_from_begin_to_date", nullable = true)
	public Long getNbDealerApplicationsLastMonthFromBeginToDate() {
		return nbDealerApplicationsLastMonthFromBeginToDate;
	}

	/**
	 * @param nbDealerApplicationsLastMonthFromBeginToDate the nbDealerApplicationsLastMonthFromBeginToDate to set
	 */
	public void setNbDealerApplicationsLastMonthFromBeginToDate(
			Long nbDealerApplicationsLastMonthFromBeginToDate) {
		this.nbDealerApplicationsLastMonthFromBeginToDate = nbDealerApplicationsLastMonthFromBeginToDate;
	}

	/**
	 * @return the nbDealerContractsLastMonthFromBeginToDate
	 */
	@Column(name = "repda_nu_dealer_contracts_last_month_from_begin_to_date", nullable = true)
	public Long getNbDealerContractsLastMonthFromBeginToDate() {
		return nbDealerContractsLastMonthFromBeginToDate;
	}

	/**
	 * @param nbDealerContractsLastMonthFromBeginToDate the nbDealerContractsLastMonthFromBeginToDate to set
	 */
	public void setNbDealerContractsLastMonthFromBeginToDate(
			Long nbDealerContractsLastMonthFromBeginToDate) {
		this.nbDealerContractsLastMonthFromBeginToDate = nbDealerContractsLastMonthFromBeginToDate;
	}

	/**
	 * @return the nbDealerFieldChecksLastMonthFromBeginToDate
	 */
	@Column(name = "repda_nu_dealer_field_checks_last_month_from_begin_to_date", nullable = true)
	public Long getNbDealerFieldChecksLastMonthFromBeginToDate() {
		return nbDealerFieldChecksLastMonthFromBeginToDate;
	}

	/**
	 * @param nbDealerFieldChecksLastMonthFromBeginToDate the nbDealerFieldChecksLastMonthFromBeginToDate to set
	 */
	public void setNbDealerFieldChecksLastMonthFromBeginToDate(
			Long nbDealerFieldChecksLastMonthFromBeginToDate) {
		this.nbDealerFieldChecksLastMonthFromBeginToDate = nbDealerFieldChecksLastMonthFromBeginToDate;
	}

	/**
	 * @return the nbDealerFieldChecksFromBeginOfMonth
	 */
	@Column(name = "repda_nu_dealer_field_checks_begin_of_month", nullable = true)
	public Long getNbDealerFieldChecksFromBeginOfMonth() {
		return nbDealerFieldChecksFromBeginOfMonth;
	}

	/**
	 * @param nbDealerFieldChecksFromBeginOfMonth the nbDealerFieldChecksFromBeginOfMonth to set
	 */
	public void setNbDealerFieldChecksFromBeginOfMonth(
			Long nbDealerFieldChecksFromBeginOfMonth) {
		this.nbDealerFieldChecksFromBeginOfMonth = nbDealerFieldChecksFromBeginOfMonth;
	}

	/**
	 * @return the nbDealerVisitorsLastMonthFromBeginToDate
	 */
	@Transient
	public Long getNbDealerVisitorsLastMonthFromBeginToDate() {
		return nbDealerVisitorsLastMonthFromBeginToDate;
	}

	/**
	 * @param nbDealerVisitorsLastMonthFromBeginToDate the nbDealerVisitorsLastMonthFromBeginToDate to set
	 */
	public void setNbDealerVisitorsLastMonthFromBeginToDate(
			Long nbDealerVisitorsLastMonthFromBeginToDate) {
		this.nbDealerVisitorsLastMonthFromBeginToDate = nbDealerVisitorsLastMonthFromBeginToDate;
	}	

	/**
	 * @return the nbDealerVisitorsFromBeginOfMonth
	 */
	@Transient
	public Long getNbDealerVisitorsFromBeginOfMonth() {
		return nbDealerVisitorsFromBeginOfMonth;
	}

	/**
	 * @param nbDealerVisitorsFromBeginOfMonth the nbDealerVisitorsFromBeginOfMonth to set
	 */
	public void setNbDealerVisitorsFromBeginOfMonth(
			Long nbDealerVisitorsFromBeginOfMonth) {
		this.nbDealerVisitorsFromBeginOfMonth = nbDealerVisitorsFromBeginOfMonth;
	}	
	
	/**
	 * @return the closed
	 */
	@Column(name = "repda_bl_closed", nullable = true, columnDefinition  = "boolean default false")
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @param closed the closed to set
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	/**
	 * @param dest
	 * @return
	 */
	@Transient
	public DailyReport plus(DailyReport dest) {
		this.setApply(MyNumberUtils.getLong(this.getApply()) + MyNumberUtils.getLong(dest.getApply()));
		this.setApprove(MyNumberUtils.getLong(this.getApprove()) + MyNumberUtils.getLong(dest.getApprove()));
		this.setCompanyVisitor(MyNumberUtils.getLong(this.getCompanyVisitor()) + MyNumberUtils.getLong(dest.getCompanyVisitor()));
		this.setDealerVisitor(MyNumberUtils.getLong(this.getDealerVisitor()) + MyNumberUtils.getLong(dest.getDealerVisitor()));
		this.setCompanyApply(MyNumberUtils.getLong(this.getCompanyApply()) + MyNumberUtils.getLong(dest.getCompanyApply()));
		this.setDecline(MyNumberUtils.getLong(this.getDecline()) + MyNumberUtils.getLong(dest.getDecline()));
		this.setInProcessAtPoS(MyNumberUtils.getLong(this.getInProcessAtPoS()) + MyNumberUtils.getLong(dest.getInProcessAtPoS()));
		this.setInProcessAtUW(MyNumberUtils.getLong(this.getInProcessAtUW()) + MyNumberUtils.getLong(dest.getInProcessAtUW()));
		this.setNewContract(MyNumberUtils.getLong(this.getNewContract()) + MyNumberUtils.getLong(dest.getNewContract()));
		this.setPendingNewContract(MyNumberUtils.getLong(this.getPendingNewContract()) + MyNumberUtils.getLong(dest.getPendingNewContract()));
		this.setReject(MyNumberUtils.getLong(this.getReject()) + MyNumberUtils.getLong(dest.getReject()));
		this.setPendingPurchaseOrder(MyNumberUtils.getLong(this.getPendingPurchaseOrder()) + MyNumberUtils.getLong(dest.getPendingPurchaseOrder()));
		return this;
	}
}
