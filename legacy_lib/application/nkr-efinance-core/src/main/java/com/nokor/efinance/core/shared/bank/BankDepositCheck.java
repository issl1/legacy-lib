package com.nokor.efinance.core.shared.bank;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.payment.model.BankDepositChecked;
import com.nokor.efinance.core.shared.payment.PaymentEntityField;

/**
 * @author bunlong.taing
 */
public class BankDepositCheck implements PaymentEntityField {
	
	private static EntityService entityService = SpringUtils.getBean(EntityService.class);

	public static List<BankDepositChecked> getBankDepositChecked(Dealer dealer, Date startDate, Date endDate) {
		BaseRestrictions<BankDepositChecked> restrictions = new BaseRestrictions<BankDepositChecked>(BankDepositChecked.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		restrictions.addCriterion(Restrictions.ge("checkedDate", DateUtils.getDateAtBeginningOfDay(startDate)));
		restrictions.addCriterion(Restrictions.le("checkedDate", DateUtils.getDateAtEndOfDay(endDate)));
		return entityService.list(restrictions);
	}
}
