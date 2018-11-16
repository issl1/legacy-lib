package com.nokor.efinance.gui.ui.panel.collection.field.leader;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.service.ContractUserInboxRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.filter.AbstractCollectionContractFilterPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.nokor.frmk.security.model.SecUser;

public class ColFieldLeaderFlagFilterPanel extends AbstractCollectionContractFilterPanel implements MCollection, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4819196918334864596L;
	
	private CollectionContractTablePanel tablePanel;
	private String profileCode;
	
	/**
	 * 
	 * @param tablePanel
	 * @param profileCode
	 */
	public ColFieldLeaderFlagFilterPanel(CollectionContractTablePanel tablePanel, String profileCode) {
		super(tablePanel);
		this.tablePanel = tablePanel;
		this.profileCode = profileCode;
	}

	/**
	 * @see com.nokor.efinance.gui.ui.panel.collection.filter.AbstractCollectionContractFilterPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<ContractUserInbox> getRestrictions() {
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
		restrictions.addAssociation("con.collections", "col", JoinType.INNER_JOIN);		
		restrictions.addCriterion(Restrictions.isNotNull("col.lastCollectionFlag"));
		
		if (storeControl != null) {
			if ("0".equals(storeControl.getGuarantor())) {
				restrictions.addCriterion(Restrictions.eq("con." + Contract.NUMBERGUARANTORS, 0));
			} else {
				restrictions.addCriterion(Restrictions.ge("con." + Contract.NUMBERGUARANTORS, 1));
			}
			
			if (StringUtils.isNotBlank(storeControl.getContractId())) {
				restrictions.addCriterion(Restrictions.eq("con." + Contract.REFERENCE, storeControl.getContractId()));
			}
			
			int dueDateFrom;
			int dueDateTo;
			
			if (StringUtils.isNotEmpty(storeControl.getDueDateFrom()) && StringUtils.isNotEmpty(storeControl.getDueDateTo())) {
				dueDateFrom = Integer.valueOf(storeControl.getDueDateFrom());
				dueDateTo =  Integer.valueOf(storeControl.getDueDateTo());
				
				restrictions.addCriterion(Restrictions.and(Restrictions.ge("col." + Collection.DUEDAY, dueDateFrom), Restrictions.le("col." + Collection.DUEDAY, dueDateTo)));
			}
			
			if (storeControl.getArea() != null) {
				restrictions.addCriterion(Restrictions.eq("col.area", storeControl.getArea()));
			}
			
			if (storeControl.getAssignDateFrom() != null) {
				restrictions.addCriterion(Restrictions.ge(Contract.CREATEDATE, DateUtils.getDateAtBeginningOfDay(storeControl.getAssignDateFrom())));
			}
			
			if (storeControl.getAssignDateTo() != null) {
				restrictions.addCriterion(Restrictions.le(Contract.CREATEDATE, DateUtils.getDateAtEndOfDay(storeControl.getAssignDateTo())));
			}
			
			if (StringUtils.isNotEmpty(storeControl.getStage())) {
				int debtLevel = 0;
				if (FIRST_INSTALLMENT.equals(storeControl.getStage())) {
					debtLevel = 1;
				} else if (SECOND_INSTALLMENT.equals(storeControl.getStage())) {
					debtLevel = 2;
				} else if (THIRD_INSTALLMENT.equals(storeControl.getStage())) {
					debtLevel = 3;
				}
				restrictions.addCriterion(Restrictions.eq("col.debtLevel", debtLevel));
			}
			
			if (StringUtils.isNotEmpty(storeControl.getStatus())) {
				restrictions.addAssociation("col.lastAction", "lasAct", JoinType.LEFT_OUTER_JOIN);
				if (FOLLOW_UP.equals(storeControl.getStatus())) {
					restrictions.addCriterion(Restrictions.eq("lasAct.completed", false));
				} else {
					restrictions.addCriterion(Restrictions.eq("lasAct.completed", true));
				}
			}
			
			if (storeControl.getOdm() != null) {
				restrictions.addCriterion(Restrictions.eq("col.debtLevel", storeControl.getOdm()));
			}
			
			if (storeControl.getLockSplitStatus() != null) {
				DetachedCriteria contractFlagSubCriteria = DetachedCriteria.forClass(LockSplit.class, "locSpit");
				contractFlagSubCriteria.add(Restrictions.eq("locSpit.wkfSubStatus", storeControl.getLockSplitStatus()));
				contractFlagSubCriteria.setProjection(Projections.projectionList().add(Projections.property("locSpit.contract.id")));
				restrictions.addCriterion(Property.forName("con."+ ContractFlag.ID).in(contractFlagSubCriteria));
			}

			
			if (storeControl.getSecUsers() != null && !storeControl.getSecUsers().isEmpty()) {
				restrictions.addCriterion(Restrictions.in(ContractUserInbox.SECUSER, storeControl.getSecUsers()));
			} else {
				restrictions.addCriterion(Restrictions.in(ContractUserInbox.SECUSER, getSecUserByProfile()));
			}			
		} else {
			restrictions.addCriterion(Restrictions.in(ContractUserInbox.SECUSER, getSecUserByProfile()));
		}
		tablePanel.setRestriction(restrictions);
		return restrictions;
	}
	

	/**
	 * get list secuser by profile code
	 * @return
	 */
	private List<SecUser> getSecUserByProfile() {
		return COL_SRV.getCollectionUsers(new String[] {this.profileCode}); 
	}

}
