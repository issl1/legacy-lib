package com.nokor.efinance.core.collection.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.BaseEntityDao;
import org.seuksa.frmk.dao.EntityDao;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.service.impl.BaseEntityServiceImpl;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.EColAction;
import com.nokor.efinance.core.collection.service.CollectionActionRestriction;
import com.nokor.efinance.core.collection.service.CollectionActionService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.contract.service.ContractUserInboxRestriction;
import com.nokor.efinance.core.shared.applicant.ApplicantEntityField;
import com.nokor.frmk.security.model.SecUser;

/**
 * 
 * @author buntha.chea
 *
 */
@Service("collectionActionService")
public class CollectionActionServiceImp extends BaseEntityServiceImpl implements CollectionActionService, ApplicantEntityField {

	/**	 */
	private static final long serialVersionUID = 7786032961874572157L;
	
	@Autowired
    private EntityDao dao;
	
	/**
	 * getCollectionActionOfWeek
	 */
	@Override
	public List<CollectionAction> getCollectionActionOfWeek() {
		List<CollectionAction> collectionActionsValidated = new ArrayList<>();
		List<CollectionAction> collectionActions = getCollectionActionInThisWeek();
		for (CollectionAction collectionAction : collectionActions) {
			if (isContractUserInbox(collectionAction)) {
				collectionActionsValidated.add(collectionAction);
			}
		}
		return collectionActionsValidated;
	}
	
	/**
	 * get list of collection Action of this month
	 */
	@Override
	public List<CollectionAction> getCollectionActionOfMonth() {
		List<CollectionAction> collectionActionsValidated = new ArrayList<>();
		List<CollectionAction> collectionActions = getCollectionActionInCurrentMonth();
		for (CollectionAction collectionAction : collectionActions) {
			if (isContractUserInbox(collectionAction)) {
				collectionActionsValidated.add(collectionAction);
			}
		}
		return collectionActionsValidated;
	}
	
	/**
	 * isNextActionDateInThisWeek
	 * @param collectionAction
	 * @return
	 */
	@Override
	public boolean isNextActionDateInThisWeek(CollectionAction collectionAction) {
		Date nextActionDate = collectionAction.getNextActionDate();
		Date[] datesInThisWeek = getDateRangThisWeek(DateUtils.getDateAtBeginningOfWeek(DateUtils.today()));
		if (nextActionDate != null) {
			if (datesInThisWeek[0].getTime() <= nextActionDate.getTime() && datesInThisWeek[1].getTime() >= nextActionDate.getTime()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * isNextActionDateInNextWeek
	 */
	@Override
	public boolean isNextActionDateInNextWeek(CollectionAction collectionAction) {
		Date nextActionDate = DateUtils.getDateAtEndOfDay(collectionAction.getNextActionDate());
		Date[] datesInNextMonth = getDateRangeNextWeek(DateUtils.getDateAtBeginningOfWeek(DateUtils.today()));
		if (nextActionDate != null) {
			if (nextActionDate.after(datesInNextMonth[0]) && nextActionDate.before(datesInNextMonth[1])) {
				if (isNextActionDateInThisMonth(collectionAction)) {
					return true;
				}
			}
		}
		return false;
	}
	

	/**
	 * isNextActionDateInWeekAfter
	 */
	@Override
	public boolean isNextActionDateInWeekAfter(CollectionAction collectionAction) {
		Date nextActionDate = DateUtils.getDateAtEndOfDay(collectionAction.getNextActionDate());
		Date[] datesInNextMonth = getDateRangeAfterNextWeek(DateUtils.getDateAtBeginningOfWeek(DateUtils.today()));
		if (nextActionDate != null) {
			if (nextActionDate.after(datesInNextMonth[0])) {
				if (isNextActionDateInThisMonth(collectionAction)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionActionService#getCollectionActionsUnProcessed(java.lang.Long)
	 */
	@Override
	public List<CollectionAction> getCollectionActionsUnProcessed(Long conId) {
		CollectionActionRestriction restrictions = new CollectionActionRestriction();
		restrictions.setContractId(conId);
		restrictions.setUnProcessed(true);
		return list(restrictions);
	}
	
	/**
	 * @see com.nokor.efinance.core.collection.service.CollectionActionService#getCollectionActionsByNextActionDate(java.util.Date, java.util.Date, java.lang.Long)
	 */
	@Override
	public List<CollectionAction> getCollectionActionsByNextActionDate(Date startDate, Date endDate, Long conId) {
		CollectionActionRestriction restrictions = new CollectionActionRestriction();
		restrictions.setContractId(conId);
		restrictions.setUnProcessed(false);
		restrictions.setStartDate(startDate);
		restrictions.setEndDate(endDate);
		return list(restrictions);
	}
	
	/**
	 * getNameOfDay
	 * get the name of day in week
	 */
	@Override
	public String getNameOfDay(Date nextActionDate) {
		int day = getDayIndexOfWeek(nextActionDate);
		return DateUtils.getDayString(day);
	}

	/**
	 * isContractUserInbox
	 * @param collectionAction
	 * @return
	 */
	private boolean	isContractUserInbox(CollectionAction collectionAction) {	
		SecUser secUser = UserSessionManager.getCurrentUser();
		ContractUserInboxRestriction restriction = new ContractUserInboxRestriction();
		restriction.setConId(collectionAction.getContract().getId());
		restriction.addAssociation(SEC_USER, "sec", JoinType.INNER_JOIN);
		restriction.addCriterion(Restrictions.eq(SEC_USER , secUser));
		List<ContractUserInbox> contractUserInboxs = list(restriction);
		if (!contractUserInboxs.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param collectionAction
	 * @return
	 */
	private boolean isNextActionDateInThisMonth(CollectionAction collectionAction) {
		Date nextActionDate = DateUtils.getDateAtEndOfDay(collectionAction.getNextActionDate());
		Date[] datesInCurrentMonth = DateUtils.getDateRangeCurrentMonth();
		if (nextActionDate != null) {
			if (nextActionDate.after(datesInCurrentMonth[0]) && nextActionDate.before(datesInCurrentMonth[1])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * getDateRangThisWeek
	 * @return
	 */
	private Date[] getDateRangThisWeek(Date nextActionDate) {
		Calendar start = new GregorianCalendar();
		
		start.setTime(nextActionDate);
		
		start.add(Calendar.DATE, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        
        Calendar end = new GregorianCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.DATE, 6);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
	    return new Date[] { start.getTime(), end.getTime() };
	}
	
	/**
	 * getDateRangeNextWeek
	 * @param nextActionDate
	 * @return
	 */
	public static Date[] getDateRangeNextWeek(Date nextActionDate) {
		Calendar start = new GregorianCalendar();
		start.setTime(nextActionDate);
		
		start.add(Calendar.DATE, 8);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        
        Calendar end = new GregorianCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.DATE, 6);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
	    return new Date[] { start.getTime(), end.getTime() };
	}
	
	/**
	 * getDateRangeAfterNextWeek
	 * @param nextActionDate
	 * @return
	 */
	public static Date[] getDateRangeAfterNextWeek(Date nextActionDate) {
		Calendar start = new GregorianCalendar();
		start.setTime(nextActionDate);
		
		start.add(Calendar.DATE, 15);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        
        Calendar end = new GregorianCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.DATE, 6);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
	    return new Date[] { start.getTime(), end.getTime() };
	}
	
	/**
	 * getDayIndexOfWeek
	 * @param date
	 * @return
	 */
	private int getDayIndexOfWeek(Date date) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int day = cal.get(Calendar.DAY_OF_WEEK);
	    return day;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<CollectionAction> getCollectionActionInThisWeek() {
		Date[] datesInThisWeek = getDateRangThisWeek(DateUtils.getDateAtBeginningOfWeek(DateUtils.today()));
		CollectionActionRestriction restrictions = new CollectionActionRestriction();
		restrictions.addCriterion(Restrictions.ge("nextActionDate", datesInThisWeek[0]));
		restrictions.addCriterion(Restrictions.le("nextActionDate", datesInThisWeek[1]));
		restrictions.addOrder(Order.asc("nextActionDate"));
		return list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<CollectionAction> getCollectionActionInCurrentMonth() {
		Date[] datesInCurrentMonth = DateUtils.getDateRangeCurrentMonth();
		CollectionActionRestriction restrictions = new CollectionActionRestriction();
		restrictions.addCriterion(Restrictions.ge("nextActionDate", datesInCurrentMonth[0]));
		restrictions.addCriterion(Restrictions.le("nextActionDate", datesInCurrentMonth[1]));
		restrictions.addOrder(Order.asc("nextActionDate"));
		return list(restrictions);
	}
	
	/**
	 * 
	 */
	@Override
	public List<Contract> getContractsAssigned() {
		Date currrentMonth[] = DateUtils.getDateRangeCurrentMonth();
		SecUser secUser = UserSessionManager.getCurrentUser();
		ContractRestriction restrictions = new ContractRestriction();
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
		userContractSubCriteria.add(Restrictions.eq("usr.id", secUser.getId()));
		userContractSubCriteria.add(Restrictions.and(Restrictions.ge("cousr.createDate", currrentMonth[0]), Restrictions.le("cousr.createDate", currrentMonth[1])));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
			
		restrictions.addCriterion(Property.forName(ContractUserInbox.ID).in(userContractSubCriteria));
		return list(restrictions);
	}
	
	/**
	 * 
	 */
	public List<Contract> getContracts() {
		SecUser secUser = UserSessionManager.getCurrentUser();
		ContractRestriction restrictions = new ContractRestriction();
		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
		userContractSubCriteria.add(Restrictions.eq("usr.id", secUser.getId()));
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
			
		restrictions.addCriterion(Property.forName(ContractUserInbox.ID).in(userContractSubCriteria));
		return list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<EColAction> getCollectionActionInCollection() {
		List<CollectionAction> collectionActions = new ArrayList<>();
		List<Contract> contracts = getContracts();
		for (Contract contract : contracts) {
			for (Collection collection : contract.getCollections()) {
				if (collection.getLastAction() != null) {
					collectionActions.add(collection.getLastAction());
				}
			}
		}
		return checkDuplicateCollectionAction(collectionActions);
	}
	
	/**
	 * 
	 * @param collectionAction
	 * @return
	 */
	@Override
	public int countCollectionAction(EColAction action, Date date) {
		Date today = DateUtils.todayH00M00S00();
		Date tomorrow = DateUtils.addDaysDate(today, 1);
		Date endDayOfMonth = DateUtils.getDateAtEndOfMonth();
		BaseRestrictions<Collection> restrictions = new BaseRestrictions<>(Collection.class);
		restrictions.addAssociation("lastAction", "lasAct", JoinType.INNER_JOIN);
		restrictions.addCriterion(Restrictions.eq("lasAct.colAction", action));
		if (date == null) {
			restrictions.addCriterion(Restrictions.ne("lasAct.colAction", EColAction.NONE));
			restrictions.addCriterion(Restrictions.lt("lasAct.nextActionDate", today));
		} else if (DateUtils.isToday(date) || DateUtils.getDateAtBeginningOfDay(tomorrow).equals(DateUtils.getDateAtBeginningOfDay(date))) {
			restrictions.addCriterion(Restrictions.and(Restrictions.ge("lasAct.nextActionDate", DateUtils.getDateAtBeginningOfDay(date)), 
					Restrictions.le("lasAct.nextActionDate", DateUtils.getDateAtEndOfDay(date))));
			restrictions.addCriterion(Restrictions.le("lasAct.nextActionDate", endDayOfMonth));
		} else {
			restrictions.addCriterion(Restrictions.ge("lasAct.nextActionDate", date));
			restrictions.addCriterion(Restrictions.le("lasAct.nextActionDate", endDayOfMonth));
		}
		return list(restrictions).size();
	}
	
	/**
	 * 
	 * @param collectionActions
	 * @return
	 */
	public List<EColAction> checkDuplicateCollectionAction(List<CollectionAction> collectionActions) {
		List<EColAction> colActions = new ArrayList<>();
		for (CollectionAction collectionAction : collectionActions) {
			if (!colActions.contains(collectionAction.getColAction())) {
				colActions.add(collectionAction.getColAction());
			}
		}
		return colActions;
	}
	
	/**
	 * Count contract collection that last action is null
	 */
	@Override
	public int countNoAction() {
		List<Contract> contracts = getContracts();
		int nbNoAction = 0;
		for (Contract contract : contracts) {
			for (Collection collection : contract.getCollections()) {
				if (collection.getLastAction() == null) {
					nbNoAction++;
				}
			}
		}
		return nbNoAction;
	}	
	
	/**
	 * getDao
	 */
	@Override
	public BaseEntityDao getDao() {
		return dao;
	}
	
}
