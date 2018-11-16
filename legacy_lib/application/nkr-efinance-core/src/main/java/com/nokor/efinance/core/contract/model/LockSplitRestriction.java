package com.nokor.efinance.core.contract.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.ELockSplitGroup;
import com.nokor.efinance.core.collection.model.EPaymentChannel;

/**
 * 
 * @author uhout.cheng
 */
public class LockSplitRestriction extends BaseRestrictions<LockSplit> implements MLockSplit {
	
	/** */
	private static final long serialVersionUID = 4348810076273295944L;
	
	private static final String CONTRACT = "contract";
	private static final String CON = "con";
	private static final String DOT = ".";
	private static final String ITE = "ite";
	private static final String LCKTYPE = "lcktype";
	
	private String lockSplitNo;
	private String contractID;
	private Date dueDateFrom;
	private Date dueDateTo;
	private EPaymentChannel paymentChannel;
	private boolean installments;
	private boolean afterSales;
	private boolean fees;
	
	/**
	 * 
	 */
    public LockSplitRestriction() {
		super(LockSplit.class);
	}
    
    /**
     * @see org.seuksa.frmk.dao.hql.BaseRestrictions#preBuildCommunMapCriteria()
     */
    @Override
	public void preBuildSpecificCriteria() {
    	if (StringUtils.isNotEmpty(lockSplitNo)) {
    		addCriterion(Restrictions.like(REFERENCE, lockSplitNo));
    	}
    	if (StringUtils.isNotEmpty(contractID)) {
    		addAssociation(CONTRACT, CON, JoinType.INNER_JOIN);
    		addCriterion(Restrictions.like(CON + DOT + REFERENCE, contractID));
    	}
    	if (dueDateFrom != null && dueDateTo != null) {
    		addCriterion(Restrictions.or(
    				Restrictions.and(Restrictions.ge(PAYMENTDATE, DateUtils.getDateAtBeginningOfDay(dueDateFrom)),
    						Restrictions.le(PAYMENTDATE, DateUtils.getDateAtEndOfDay(dueDateTo))),
    				Restrictions.and(Restrictions.ge(EXPIRYDATE, DateUtils.getDateAtBeginningOfDay(dueDateFrom)),
    						Restrictions.le(EXPIRYDATE, DateUtils.getDateAtEndOfDay(dueDateTo)))));
		} else {
			if (dueDateFrom != null) {
				addCriterion(Restrictions.or(
	    				Restrictions.ge(PAYMENTDATE, DateUtils.getDateAtBeginningOfDay(dueDateFrom)), 
	    				Restrictions.ge(EXPIRYDATE, DateUtils.getDateAtBeginningOfDay(dueDateFrom))));
	    	} else if (dueDateTo != null) {
				addCriterion(Restrictions.or(
	    				Restrictions.ge(PAYMENTDATE, DateUtils.getDateAtBeginningOfDay(dueDateTo)),
	    				Restrictions.ge(EXPIRYDATE, DateUtils.getDateAtBeginningOfDay(dueDateTo))));
	    	}
		}
    	if (paymentChannel != null) {
    		addCriterion(Restrictions.eq(PAYMENTCHANNEL, paymentChannel));
    	}
    	List<Criterion> criLckSplitGroups = new ArrayList<>();
    	if (installments || afterSales || fees) {
    		addAssociation(ITEMS, ITE, JoinType.INNER_JOIN);
    		addAssociation(ITE + DOT + LOCKSPLITTYPE, LCKTYPE, JoinType.INNER_JOIN);
    		if (installments) {
    			criLckSplitGroups.add(Restrictions.eq(LCKTYPE + DOT + LOCKSPLITGROUP, ELockSplitGroup.INS));
    		}
    		if (afterSales) {
    			criLckSplitGroups.add(Restrictions.eq(LCKTYPE + DOT + LOCKSPLITGROUP, ELockSplitGroup.AFTERSALE));
    		}
    		if (fees) {
    			criLckSplitGroups.add(Restrictions.eq(LCKTYPE + DOT + LOCKSPLITGROUP, ELockSplitGroup.FEE));
    		}
    		setDistinctRootEntity(true);
    	}
    	if (!criLckSplitGroups.isEmpty()) {
			addCriterion(Restrictions.or(criLckSplitGroups.toArray(new Criterion[criLckSplitGroups.size()])));
		}
    	addOrder(Order.desc(LockSplit.ID));
	}

	/**
	 * @return the lockSplitNo
	 */
	public String getLockSplitNo() {
		return lockSplitNo;
	}

	/**
	 * @param lockSplitNo the lockSplitNo to set
	 */
	public void setLockSplitNo(String lockSplitNo) {
		this.lockSplitNo = lockSplitNo;
	}

	/**
	 * @return the contractID
	 */
	public String getContractID() {
		return contractID;
	}

	/**
	 * @param contractID the contractID to set
	 */
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	/**
	 * @return the dueDateFrom
	 */
	public Date getDueDateFrom() {
		return dueDateFrom;
	}

	/**
	 * @param dueDateFrom the dueDateFrom to set
	 */
	public void setDueDateFrom(Date dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	/**
	 * @return the dueDateTo
	 */
	public Date getDueDateTo() {
		return dueDateTo;
	}

	/**
	 * @param dueDateTo the dueDateTo to set
	 */
	public void setDueDateTo(Date dueDateTo) {
		this.dueDateTo = dueDateTo;
	}

	/**
	 * @return the paymentChannel
	 */
	public EPaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	/**
	 * @param paymentChannel the paymentChannel to set
	 */
	public void setPaymentChannel(EPaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	/**
	 * @return the installments
	 */
	public boolean isInstallments() {
		return installments;
	}

	/**
	 * @param installments the installments to set
	 */
	public void setInstallments(boolean installments) {
		this.installments = installments;
	}

	/**
	 * @return the afterSales
	 */
	public boolean isAfterSales() {
		return afterSales;
	}

	/**
	 * @param afterSales the afterSales to set
	 */
	public void setAfterSales(boolean afterSales) {
		this.afterSales = afterSales;
	}

	/**
	 * @return the fees
	 */
	public boolean isFees() {
		return fees;
	}

	/**
	 * @param fees the fees to set
	 */
	public void setFees(boolean fees) {
		this.fees = fees;
	}
	
}
