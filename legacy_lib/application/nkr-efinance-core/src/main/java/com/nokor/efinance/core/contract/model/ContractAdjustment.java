package com.nokor.efinance.core.contract.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

@Entity
@Table(name = "td_contract_adjustment")
public class ContractAdjustment  extends EntityA {

	private static final long serialVersionUID = -1893813037868210744L;
	
	private Double tiAdjustmentInterest;
	private Double teAdjustmentInterest;
	private Double vatAdjustmentInterest;
	
	private Double tiAdjustmentPrincipal;
	private Double teAdjustmentPrincipal;
	private Double vatAdjustmentPrincipal;
	
	private Double tiBalanceInterestInSuspendUsd;
	private Double teBalanceInterestInSuspendUsd;
	private Double vatBalanceInterestInSuspendUsd;
		
	private Double tiUnpaidAccruedInterestReceivableUsd;
	private Double teUnpaidAccruedInterestReceivableUsd;
	private Double vatUnpaidAccruedInterestReceivableUsd;
	
	private Double tiUnpaidDeferredCommissionReferalFeeUsd;
	private Double teUnpaidDeferredCommissionReferalFeeUsd;
	private Double vatUnpaidDeferredCommissionReferalFeeUsd;
	
	private Double tiUnpaidAcrrualExpensesReferalFeeUsd;
	private Double teUnpaidAcrrualExpensesReferalFeeUsd;
	private Double vatUnpaidAcrrualExpensesReferalFeeUsd;
	
	private Double tiBalanceServicingIncomeInSuspendUsd;
	private Double teBalanceServicingIncomeInSuspendUsd;
	private Double vatBalanceServicingIncomeInSuspendUsd;
	
	private Double tiUnpaidUnearnedServicingIncomeUsd;
	private Double teUnpaidUnearnedServicingIncomeUsd;
	private Double vatUnpaidUnearnedServicingIncomeUsd;
	
	private Double tiUnpaidAccrualReceivableServicingIncomeUsd;
	private Double teUnpaidAccrualReceivableServicingIncomeUsd;
	private Double vatUnpaidAccrualReceivableServicingIncomeUsd;
	
	private Double tiBalanceInsuranceIncomeInSuspendUsd;
	private Double teBalanceInsuranceIncomeInSuspendUsd;
	private Double vatBalanceInsuranceIncomeInSuspendUsd;
	
	private Double tiUnpaidUnearnedInsuranceIncomeUsd;
	private Double teUnpaidUnearnedInsuranceIncomeUsd;
	private Double vatUnpaidUnearnedInsuranceIncomeUsd;
	
	private Double tiUnpaidAccrualReceivableInsuranceIncomeUsd;
	private Double teUnpaidAccrualReceivableInsuranceIncomeUsd;
	private Double vatUnpaidAccrualReceivableInsuranceIncomeUsd;
		
	private Contract contract;
	
	/**
     * @return id.
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "con_adj_id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
	
    /**
	 * @return the tiAdjustmentInterest
	 */
	@Column(name = "con_adj_am_ti_adjustment_interest", nullable = true)
	public Double getTiAdjustmentInterest() {
		return tiAdjustmentInterest;
	}

	/**
	 * @param tiAdjustmentInterest the tiAdjustmentInterest to set
	 */
	public void setTiAdjustmentInterest(Double tiAdjustmentInterest) {
		this.tiAdjustmentInterest = tiAdjustmentInterest;
	}

	/**
	 * @return the teAdjustmentInterest
	 */
	@Column(name = "con_adj_am_te_adjustment_interest", nullable = true)
	public Double getTeAdjustmentInterest() {
		return teAdjustmentInterest;
	}

	/**
	 * @param teAdjustmentInterest the teAdjustmentInterest to set
	 */
	public void setTeAdjustmentInterest(Double teAdjustmentInterest) {
		this.teAdjustmentInterest = teAdjustmentInterest;
	}

	/**
	 * @return the vatAdjustmentInterest
	 */
	@Column(name = "con_adj_am_vat_adjustment_interest", nullable = true)
	public Double getVatAdjustmentInterest() {
		return vatAdjustmentInterest;
	}

	/**
	 * @param vatAdjustmentInterest the vatAdjustmentInterest to set
	 */
	public void setVatAdjustmentInterest(Double vatAdjustmentInterest) {
		this.vatAdjustmentInterest = vatAdjustmentInterest;
	}
	
		
	/**
	 * @return the tiAdjustmentPrincipal
	 */
	@Column(name = "con_adj_am_ti_adjustment_principal", nullable = true)
	public Double getTiAdjustmentPrincipal() {
		return tiAdjustmentPrincipal;
	}

	/**
	 * @param tiAdjustmentPrincipal the tiAdjustmentPrincipal to set
	 */
	public void setTiAdjustmentPrincipal(Double tiAdjustmentPrincipal) {
		this.tiAdjustmentPrincipal = tiAdjustmentPrincipal;
	}

	/**
	 * @return the teAdjustmentPrincipal
	 */
	@Column(name = "con_adj_am_te_adjustment_principal", nullable = true)
	public Double getTeAdjustmentPrincipal() {
		return teAdjustmentPrincipal;
	}

	/**
	 * @param teAdjustmentPrincipal the teAdjustmentPrincipal to set
	 */
	public void setTeAdjustmentPrincipal(Double teAdjustmentPrincipal) {
		this.teAdjustmentPrincipal = teAdjustmentPrincipal;
	}

	/**
	 * @return the vatAdjustmentPrincipal
	 */
	@Column(name = "con_adj_am_vat_adjustment_principal", nullable = true)
	public Double getVatAdjustmentPrincipal() {
		return vatAdjustmentPrincipal;
	}

	/**
	 * @param vatAdjustmentPrincipal the vatAdjustmentPrincipal to set
	 */
	public void setVatAdjustmentPrincipal(Double vatAdjustmentPrincipal) {
		this.vatAdjustmentPrincipal = vatAdjustmentPrincipal;
	}


	
	/**
	 * @return the tiBalanceInterestInSuspendUsd
	 */
	@Column(name = "con_adj_am_ti_balance_interest_in_suspend_usd", nullable = true)
	public Double getTiBalanceInterestInSuspendUsd() {
		return tiBalanceInterestInSuspendUsd;
	}

	/**
	 * @param tiBalanceInterestInSuspendUsd the tiBalanceInterestInSuspendUsd to set
	 */
	public void setTiBalanceInterestInSuspendUsd(
			Double tiBalanceInterestInSuspendUsd) {
		this.tiBalanceInterestInSuspendUsd = tiBalanceInterestInSuspendUsd;
	}

	/**
	 * @return the teBalanceInterestInSuspendUsd
	 */
	@Column(name = "con_adj_am_te_balance_interest_in_suspend_usd", nullable = true)
	public Double getTeBalanceInterestInSuspendUsd() {
		return teBalanceInterestInSuspendUsd;
	}

	/**
	 * @param teBalanceInterestInSuspendUsd the teBalanceInterestInSuspendUsd to set
	 */
	public void setTeBalanceInterestInSuspendUsd(
			Double teBalanceInterestInSuspendUsd) {
		this.teBalanceInterestInSuspendUsd = teBalanceInterestInSuspendUsd;
	}

	/**
	 * @return the vatBalanceInterestInSuspendUsd
	 */
	@Column(name = "con_adj_am_vat_balance_interest_in_suspend_usd", nullable = true)
	public Double getVatBalanceInterestInSuspendUsd() {
		return vatBalanceInterestInSuspendUsd;
	}

	/**
	 * @param vatBalanceInterestInSuspendUsd the vatBalanceInterestInSuspendUsd to set
	 */
	public void setVatBalanceInterestInSuspendUsd(
			Double vatBalanceInterestInSuspendUsd) {
		this.vatBalanceInterestInSuspendUsd = vatBalanceInterestInSuspendUsd;
	}

	/**
	 * @return the tiUnpaidAccruedInterestReceivableUsd
	 */
	@Column(name = "con_adj_am_ti_unpaid_accrued_interest_receivable_usd", nullable = true)
	public Double getTiUnpaidAccruedInterestReceivableUsd() {
		return tiUnpaidAccruedInterestReceivableUsd;
	}

	/**
	 * @param tiUnpaidAccruedInterestReceivableUsd the tiUnpaidAccruedInterestReceivableUsd to set
	 */
	public void setTiUnpaidAccruedInterestReceivableUsd(
			Double tiUnpaidAccruedInterestReceivableUsd) {
		this.tiUnpaidAccruedInterestReceivableUsd = tiUnpaidAccruedInterestReceivableUsd;
	}

	/**
	 * @return the teUnpaidAccruedInterestReceivableUsd
	 */
	@Column(name = "con_adj_am_te_unpaid_accrued_interest_receivable_usd", nullable = true)
	public Double getTeUnpaidAccruedInterestReceivableUsd() {
		return teUnpaidAccruedInterestReceivableUsd;
	}

	/**
	 * @param teUnpaidAccruedInterestReceivableUsd the teUnpaidAccruedInterestReceivableUsd to set
	 */
	public void setTeUnpaidAccruedInterestReceivableUsd(
			Double teUnpaidAccruedInterestReceivableUsd) {
		this.teUnpaidAccruedInterestReceivableUsd = teUnpaidAccruedInterestReceivableUsd;
	}

	/**
	 * @return the vatUnpaidAccruedInterestReceivableUsd
	 */
	@Column(name = "con_adj_am_vat_unpaid_accrued_interest_receivable_usd", nullable = true)
	public Double getVatUnpaidAccruedInterestReceivableUsd() {
		return vatUnpaidAccruedInterestReceivableUsd;
	}

	/**
	 * @param vatUnpaidAccruedInterestReceivableUsd the vatUnpaidAccruedInterestReceivableUsd to set
	 */
	public void setVatUnpaidAccruedInterestReceivableUsd(
			Double vatUnpaidAccruedInterestReceivableUsd) {
		this.vatUnpaidAccruedInterestReceivableUsd = vatUnpaidAccruedInterestReceivableUsd;
	}	
	
	/**
	 * @return the tiUnpaidDeferredCommissionReferalFeeUsd
	 */
	@Column(name = "con_adj_am_ti_unpaid_deferred_commission_referal_fee_usd", nullable = true)
	public Double getTiUnpaidDeferredCommissionReferalFeeUsd() {
		return tiUnpaidDeferredCommissionReferalFeeUsd;
	}

	/**
	 * @param tiUnpaidDeferredCommissionReferalFeeUsd the tiUnpaidDeferredCommissionReferalFeeUsd to set
	 */
	public void setTiUnpaidDeferredCommissionReferalFeeUsd(
			Double tiUnpaidDeferredCommissionReferalFeeUsd) {
		this.tiUnpaidDeferredCommissionReferalFeeUsd = tiUnpaidDeferredCommissionReferalFeeUsd;
	}

	/**
	 * @return the teUnpaidDeferredCommissionReferalFeeUsd
	 */
	@Column(name = "con_adj_am_te_unpaid_deferred_commission_referal_fee_usd", nullable = true)
	public Double getTeUnpaidDeferredCommissionReferalFeeUsd() {
		return teUnpaidDeferredCommissionReferalFeeUsd;
	}

	/**
	 * @param teUnpaidDeferredCommissionReferalFeeUsd the teUnpaidDeferredCommissionReferalFeeUsd to set
	 */
	public void setTeUnpaidDeferredCommissionReferalFeeUsd(
			Double teUnpaidDeferredCommissionReferalFeeUsd) {
		this.teUnpaidDeferredCommissionReferalFeeUsd = teUnpaidDeferredCommissionReferalFeeUsd;
	}

	/**
	 * @return the vatUnpaidDeferredCommissionReferalFeeUsd
	 */
	@Column(name = "con_adj_am_vat_unpaid_deferred_commission_referal_fee_usd", nullable = true)
	public Double getVatUnpaidDeferredCommissionReferalFeeUsd() {
		return vatUnpaidDeferredCommissionReferalFeeUsd;
	}

	/**
	 * @param vatUnpaidDeferredCommissionReferalFeeUsd the vatUnpaidDeferredCommissionReferalFeeUsd to set
	 */
	public void setVatUnpaidDeferredCommissionReferalFeeUsd(
			Double vatUnpaidDeferredCommissionReferalFeeUsd) {
		this.vatUnpaidDeferredCommissionReferalFeeUsd = vatUnpaidDeferredCommissionReferalFeeUsd;
	}

	/**
	 * @return the tiUnpaidAcrrualExpensesReferalFeeUsd
	 */
	@Column(name = "con_adj_am_ti_unpaid_acrrual_expenses_referal_fee_usd", nullable = true)
	public Double getTiUnpaidAcrrualExpensesReferalFeeUsd() {
		return tiUnpaidAcrrualExpensesReferalFeeUsd;
	}

	/**
	 * @param tiUnpaidAcrrualExpensesReferalFeeUsd the tiUnpaidAcrrualExpensesReferalFeeUsd to set
	 */
	public void setTiUnpaidAcrrualExpensesReferalFeeUsd(
			Double tiUnpaidAcrrualExpensesReferalFeeUsd) {
		this.tiUnpaidAcrrualExpensesReferalFeeUsd = tiUnpaidAcrrualExpensesReferalFeeUsd;
	}

	/**
	 * @return the teUnpaidAcrrualExpensesReferalFeeUsd
	 */
	@Column(name = "con_adj_am_te_unpaid_acrrual_expenses_referal_fee_usd", nullable = true)
	public Double getTeUnpaidAcrrualExpensesReferalFeeUsd() {
		return teUnpaidAcrrualExpensesReferalFeeUsd;
	}

	/**
	 * @param teUnpaidAcrrualExpensesReferalFeeUsd the teUnpaidAcrrualExpensesReferalFeeUsd to set
	 */
	public void setTeUnpaidAcrrualExpensesReferalFeeUsd(
			Double teUnpaidAcrrualExpensesReferalFeeUsd) {
		this.teUnpaidAcrrualExpensesReferalFeeUsd = teUnpaidAcrrualExpensesReferalFeeUsd;
	}

	/**
	 * @return the vatUnpaidAcrrualExpensesReferalFeeUsd
	 */
	@Column(name = "con_adj_am_vat_unpaid_acrrual_expenses_referal_fee_usd", nullable = true)
	public Double getVatUnpaidAcrrualExpensesReferalFeeUsd() {
		return vatUnpaidAcrrualExpensesReferalFeeUsd;
	}

	/**
	 * @param vatUnpaidAcrrualExpensesReferalFeeUsd the vatUnpaidAcrrualExpensesReferalFeeUsd to set
	 */
	public void setVatUnpaidAcrrualExpensesReferalFeeUsd(
			Double vatUnpaidAcrrualExpensesReferalFeeUsd) {
		this.vatUnpaidAcrrualExpensesReferalFeeUsd = vatUnpaidAcrrualExpensesReferalFeeUsd;
	}

	/**
	 * @return the tiBalanceServicingIncomeInSuspendUsd
	 */
	@Column(name = "con_adj_am_ti_balance_servicing_income_in_suspend_usd", nullable = true)
	public Double getTiBalanceServicingIncomeInSuspendUsd() {
		return tiBalanceServicingIncomeInSuspendUsd;
	}

	/**
	 * @param tiBalanceServicingIncomeInSuspendUsd the tiBalanceServicingIncomeInSuspendUsd to set
	 */
	public void setTiBalanceServicingIncomeInSuspendUsd(
			Double tiBalanceServicingIncomeInSuspendUsd) {
		this.tiBalanceServicingIncomeInSuspendUsd = tiBalanceServicingIncomeInSuspendUsd;
	}

	/**
	 * @return the teBalanceServicingIncomeInSuspendUsd
	 */
	@Column(name = "con_adj_am_te_balance_servicing_income_in_suspend_usd", nullable = true)
	public Double getTeBalanceServicingIncomeInSuspendUsd() {
		return teBalanceServicingIncomeInSuspendUsd;
	}

	/**
	 * @param teBalanceServicingIncomeInSuspendUsd the teBalanceServicingIncomeInSuspendUsd to set
	 */
	public void setTeBalanceServicingIncomeInSuspendUsd(
			Double teBalanceServicingIncomeInSuspendUsd) {
		this.teBalanceServicingIncomeInSuspendUsd = teBalanceServicingIncomeInSuspendUsd;
	}

	/**
	 * @return the vatBalanceServicingIncomeInSuspendUsd
	 */
	@Column(name = "con_adj_am_vat_balance_servicing_income_in_suspend_usd", nullable = true)
	public Double getVatBalanceServicingIncomeInSuspendUsd() {
		return vatBalanceServicingIncomeInSuspendUsd;
	}

	/**
	 * @param vatBalanceServicingIncomeInSuspendUsd the vatBalanceServicingIncomeInSuspendUsd to set
	 */
	public void setVatBalanceServicingIncomeInSuspendUsd(
			Double vatBalanceServicingIncomeInSuspendUsd) {
		this.vatBalanceServicingIncomeInSuspendUsd = vatBalanceServicingIncomeInSuspendUsd;
	}

	/**
	 * @return the tiUnpaidUnearnedServicingIncomeUsd
	 */
	@Column(name = "con_adj_am_ti_unpaid_unearned_servicing_income_usd", nullable = true)
	public Double getTiUnpaidUnearnedServicingIncomeUsd() {
		return tiUnpaidUnearnedServicingIncomeUsd;
	}

	/**
	 * @param tiUnpaidUnearnedServicingIncomeUsd the tiUnpaidUnearnedServicingIncomeUsd to set
	 */
	public void setTiUnpaidUnearnedServicingIncomeUsd(
			Double tiUnpaidUnearnedServicingIncomeUsd) {
		this.tiUnpaidUnearnedServicingIncomeUsd = tiUnpaidUnearnedServicingIncomeUsd;
	}

	/**
	 * @return the teUnpaidUnearnedServicingIncomeUsd
	 */
	@Column(name = "con_adj_am_te_unpaid_unearned_servicing_income_usd", nullable = true)
	public Double getTeUnpaidUnearnedServicingIncomeUsd() {
		return teUnpaidUnearnedServicingIncomeUsd;
	}

	/**
	 * @param teUnpaidUnearnedServicingIncomeUsd the teUnpaidUnearnedServicingIncomeUsd to set
	 */
	public void setTeUnpaidUnearnedServicingIncomeUsd(
			Double teUnpaidUnearnedServicingIncomeUsd) {
		this.teUnpaidUnearnedServicingIncomeUsd = teUnpaidUnearnedServicingIncomeUsd;
	}

	/**
	 * @return the vatUnpaidUnearnedServicingIncomeUsd
	 */
	@Column(name = "con_adj_am_vat_unpaid_unearned_servicing_income_usd", nullable = true)
	public Double getVatUnpaidUnearnedServicingIncomeUsd() {
		return vatUnpaidUnearnedServicingIncomeUsd;
	}

	/**
	 * @param vatUnpaidUnearnedServicingIncomeUsd the vatUnpaidUnearnedServicingIncomeUsd to set
	 */
	public void setVatUnpaidUnearnedServicingIncomeUsd(
			Double vatUnpaidUnearnedServicingIncomeUsd) {
		this.vatUnpaidUnearnedServicingIncomeUsd = vatUnpaidUnearnedServicingIncomeUsd;
	}

	/**
	 * @return the tiUnpaidAccrualReceivableServicingIncomeUsd
	 */
	@Column(name = "con_adj_am_ti_unpaid_accrual_receivable_servicing_income_usd", nullable = true)
	public Double getTiUnpaidAccrualReceivableServicingIncomeUsd() {
		return tiUnpaidAccrualReceivableServicingIncomeUsd;
	}

	/**
	 * @param tiUnpaidAccrualReceivableServicingIncomeUsd the tiUnpaidAccrualReceivableServicingIncomeUsd to set
	 */
	public void setTiUnpaidAccrualReceivableServicingIncomeUsd(
			Double tiUnpaidAccrualReceivableServicingIncomeUsd) {
		this.tiUnpaidAccrualReceivableServicingIncomeUsd = tiUnpaidAccrualReceivableServicingIncomeUsd;
	}

	/**
	 * @return the teUnpaidAccrualReceivableServicingIncomeUsd
	 */
	@Column(name = "con_adj_am_te_unpaid_accrual_receivable_servicing_income_usd", nullable = true)
	public Double getTeUnpaidAccrualReceivableServicingIncomeUsd() {
		return teUnpaidAccrualReceivableServicingIncomeUsd;
	}

	/**
	 * @param teUnpaidAccrualReceivableServicingIncomeUsd the teUnpaidAccrualReceivableServicingIncomeUsd to set
	 */
	public void setTeUnpaidAccrualReceivableServicingIncomeUsd(
			Double teUnpaidAccrualReceivableServicingIncomeUsd) {
		this.teUnpaidAccrualReceivableServicingIncomeUsd = teUnpaidAccrualReceivableServicingIncomeUsd;
	}

	/**
	 * @return the vatUnpaidAccrualReceivableServicingIncomeUsd
	 */
	@Column(name = "con_adj_am_vat_unpaid_accrual_receivable_servicing_income_usd", nullable = true)
	public Double getVatUnpaidAccrualReceivableServicingIncomeUsd() {
		return vatUnpaidAccrualReceivableServicingIncomeUsd;
	}

	/**
	 * @param vatUnpaidAccrualReceivableServicingIncomeUsd the vatUnpaidAccrualReceivableServicingIncomeUsd to set
	 */
	public void setVatUnpaidAccrualReceivableServicingIncomeUsd(
			Double vatUnpaidAccrualReceivableServicingIncomeUsd) {
		this.vatUnpaidAccrualReceivableServicingIncomeUsd = vatUnpaidAccrualReceivableServicingIncomeUsd;
	}

	/**
	 * @return the tiBalanceInsuranceIncomeInSuspendUsd
	 */
	@Column(name = "con_adj_am_ti_balance_insurance_income_in_suspend_usd", nullable = true)
	public Double getTiBalanceInsuranceIncomeInSuspendUsd() {
		return tiBalanceInsuranceIncomeInSuspendUsd;
	}

	/**
	 * @param tiBalanceInsuranceIncomeInSuspendUsd the tiBalanceInsuranceIncomeInSuspendUsd to set
	 */
	public void setTiBalanceInsuranceIncomeInSuspendUsd(
			Double tiBalanceInsuranceIncomeInSuspendUsd) {
		this.tiBalanceInsuranceIncomeInSuspendUsd = tiBalanceInsuranceIncomeInSuspendUsd;
	}

	/**
	 * @return the teBalanceInsuranceIncomeInSuspendUsd
	 */
	@Column(name = "con_adj_am_te_balance_insurance_income_in_suspend_usd", nullable = true)
	public Double getTeBalanceInsuranceIncomeInSuspendUsd() {
		return teBalanceInsuranceIncomeInSuspendUsd;
	}

	/**
	 * @param teBalanceInsuranceIncomeInSuspendUsd the teBalanceInsuranceIncomeInSuspendUsd to set
	 */
	public void setTeBalanceInsuranceIncomeInSuspendUsd(
			Double teBalanceInsuranceIncomeInSuspendUsd) {
		this.teBalanceInsuranceIncomeInSuspendUsd = teBalanceInsuranceIncomeInSuspendUsd;
	}

	/**
	 * @return the vatBalanceInsuranceIncomeInSuspendUsd
	 */
	@Column(name = "con_adj_am_vat_balance_insurance_income_in_suspend_usd", nullable = true)
	public Double getVatBalanceInsuranceIncomeInSuspendUsd() {
		return vatBalanceInsuranceIncomeInSuspendUsd;
	}

	/**
	 * @param vatBalanceInsuranceIncomeInSuspendUsd the vatBalanceInsuranceIncomeInSuspendUsd to set
	 */
	public void setVatBalanceInsuranceIncomeInSuspendUsd(
			Double vatBalanceInsuranceIncomeInSuspendUsd) {
		this.vatBalanceInsuranceIncomeInSuspendUsd = vatBalanceInsuranceIncomeInSuspendUsd;
	}

	/**
	 * @return the tiUnpaidUnearnedInsuranceIncomeUsd
	 */
	@Column(name = "con_adj_am_ti_unpaid_unearned_insurance_income_usd", nullable = true)
	public Double getTiUnpaidUnearnedInsuranceIncomeUsd() {
		return tiUnpaidUnearnedInsuranceIncomeUsd;
	}

	/**
	 * @param tiUnpaidUnearnedInsuranceIncomeUsd the tiUnpaidUnearnedInsuranceIncomeUsd to set
	 */
	public void setTiUnpaidUnearnedInsuranceIncomeUsd(
			Double tiUnpaidUnearnedInsuranceIncomeUsd) {
		this.tiUnpaidUnearnedInsuranceIncomeUsd = tiUnpaidUnearnedInsuranceIncomeUsd;
	}

	/**
	 * @return the teUnpaidUnearnedInsuranceIncomeUsd
	 */
	@Column(name = "con_adj_am_te_unpaid_unearned_insurance_income_usd", nullable = true)
	public Double getTeUnpaidUnearnedInsuranceIncomeUsd() {
		return teUnpaidUnearnedInsuranceIncomeUsd;
	}

	/**
	 * @param teUnpaidUnearnedInsuranceIncomeUsd the teUnpaidUnearnedInsuranceIncomeUsd to set
	 */
	public void setTeUnpaidUnearnedInsuranceIncomeUsd(
			Double teUnpaidUnearnedInsuranceIncomeUsd) {
		this.teUnpaidUnearnedInsuranceIncomeUsd = teUnpaidUnearnedInsuranceIncomeUsd;
	}

	/**
	 * @return the vatUnpaidUnearnedInsuranceIncomeUsd
	 */
	@Column(name = "con_adj_am_vat_unpaid_unearned_insurance_income_usd", nullable = true)
	public Double getVatUnpaidUnearnedInsuranceIncomeUsd() {
		return vatUnpaidUnearnedInsuranceIncomeUsd;
	}

	/**
	 * @param vatUnpaidUnearnedInsuranceIncomeUsd the vatUnpaidUnearnedInsuranceIncomeUsd to set
	 */
	public void setVatUnpaidUnearnedInsuranceIncomeUsd(
			Double vatUnpaidUnearnedInsuranceIncomeUsd) {
		this.vatUnpaidUnearnedInsuranceIncomeUsd = vatUnpaidUnearnedInsuranceIncomeUsd;
	}

	/**
	 * @return the tiUnpaidAccrualReceivableInsuranceIncomeUsd
	 */
	@Column(name = "con_adj_am_ti_unpaid_accrual_receivable_insurance_income_usd", nullable = true)
	public Double getTiUnpaidAccrualReceivableInsuranceIncomeUsd() {
		return tiUnpaidAccrualReceivableInsuranceIncomeUsd;
	}

	/**
	 * @param tiUnpaidAccrualReceivableInsuranceIncomeUsd the tiUnpaidAccrualReceivableInsuranceIncomeUsd to set
	 */
	public void setTiUnpaidAccrualReceivableInsuranceIncomeUsd(
			Double tiUnpaidAccrualReceivableInsuranceIncomeUsd) {
		this.tiUnpaidAccrualReceivableInsuranceIncomeUsd = tiUnpaidAccrualReceivableInsuranceIncomeUsd;
	}

	/**
	 * @return the teUnpaidAccrualReceivableInsuranceIncomeUsd
	 */
	@Column(name = "con_adj_am_te_unpaid_accrual_receivable_insurance_income_usd", nullable = true)
	public Double getTeUnpaidAccrualReceivableInsuranceIncomeUsd() {
		return teUnpaidAccrualReceivableInsuranceIncomeUsd;
	}

	/**
	 * @param teUnpaidAccrualReceivableInsuranceIncomeUsd the teUnpaidAccrualReceivableInsuranceIncomeUsd to set
	 */
	public void setTeUnpaidAccrualReceivableInsuranceIncomeUsd(
			Double teUnpaidAccrualReceivableInsuranceIncomeUsd) {
		this.teUnpaidAccrualReceivableInsuranceIncomeUsd = teUnpaidAccrualReceivableInsuranceIncomeUsd;
	}

	/**
	 * @return the vatUnpaidAccrualReceivableInsuranceIncomeUsd
	 */
	@Column(name = "con_adj_am_vat_unpaid_accrual_receivable_insurance_income_usd", nullable = true)
	public Double getVatUnpaidAccrualReceivableInsuranceIncomeUsd() {
		return vatUnpaidAccrualReceivableInsuranceIncomeUsd;
	}

	/**
	 * @param vatUnpaidAccrualReceivableInsuranceIncomeUsd the vatUnpaidAccrualReceivableInsuranceIncomeUsd to set
	 */
	public void setVatUnpaidAccrualReceivableInsuranceIncomeUsd(
			Double vatUnpaidAccrualReceivableInsuranceIncomeUsd) {
		this.vatUnpaidAccrualReceivableInsuranceIncomeUsd = vatUnpaidAccrualReceivableInsuranceIncomeUsd;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "con_id")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

}
