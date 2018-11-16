package com.nokor.efinance.core.collection.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nokor.efinance.core.collection.service.ContractRedemptionService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.third.finwiz.client.ins.ClientInsurance;


/**
 * Calculate other data of contract
 * @author youhort.ly
 *
 */
@Service("contractRedemptionService")
@Transactional
public class ContractRedemptionServiceImpl extends BaseEntityServiceImpl implements ContractRedemptionService, ContractEntityField {
	
	/** */
	private static final long serialVersionUID = 8637497135643835740L;

	@Autowired
    private EntityDao dao;
	
	/**
     * @see org.seuksa.frmk.mvc.service.impl.BaseEntityServiceImpl#getDao()
     */
	@Override
	public EntityDao getDao() {
		return dao;
	}
	
	/**
	 * Calculate Redemption Period +15day before today
	 */
	@Override
	public void calculateRedemptionPeriod() {
		List<Contract> contracts = getContractRedemption();
		for (Contract contract : contracts) {
			Date redemptionPeriod = DateUtils.addDaysDate(DateUtils.getDateAtBeginningOfDay(contract.getForeclosureDate()), 15);
			if (redemptionPeriod.before(DateUtils.todayH00M00S00()) || redemptionPeriod.equals(DateUtils.todayH00M00S00())) {
				ClientInsurance.createPremiumClaimTask(contract.getReference(), contract.getCreateUser());
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Contract> getContractRedemption() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addCriterion(Restrictions.isNotNull("foreclosureDate"));
		return list(restrictions);
	}
	
}
