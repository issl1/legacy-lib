package com.nokor.efinance.gui.ui.panel.inbox.collection.phonestaff.task;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.contract.service.ContractUserInboxRestriction;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.gui.ui.panel.inbox.collection.AbstractColContractFilterPanel;
import com.nokor.efinance.gui.ui.panel.inbox.collection.CollectionContractsTablePanel;
import com.nokor.frmk.security.model.SecUser;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhoneStaffTaskFilterPanel extends AbstractColContractFilterPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7599060734614382615L;
	
	private CollectionContractsTablePanel tablePanel;
	
	public ColPhoneStaffTaskFilterPanel(CollectionContractsTablePanel tablePanel) {
		this.tablePanel = tablePanel;
		btnSearch.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 460619927199338156L;

			@Override
			public void buttonClick(ClickEvent event) {
				tablePanel.refresh(getRestrictions());
			}
		});
	}

	@Override
	public BaseRestrictions<ContractUserInbox> getRestrictions() {
		
		SecUser secUser = UserSessionManager.getCurrentUser();
		
		ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);
		restrictions.addAssociation("con.collections", "col", JoinType.INNER_JOIN);
		restrictions.addAssociation("con.applicant", "app", JoinType.INNER_JOIN);
		restrictions.addAssociation("app.individual", "ind", JoinType.INNER_JOIN);
		restrictions.addAssociation("ind.individualContactInfos", "indcon", JoinType.INNER_JOIN);
		restrictions.addAssociation("indcon.contactInfo", "coninfo", JoinType.INNER_JOIN);
		
		if (StringUtils.isNotEmpty(txtFullName.getValue())) {
			String fullName = txtFullName.getValue();
			String[] fullNames = fullName.split("\\s+");
			if (fullNames.length == 2) {
				restrictions.addCriterion(Restrictions.ilike("ind.lastNameEn", fullNames[0], MatchMode.ANYWHERE));
				restrictions.addCriterion(Restrictions.ilike("ind.firstNameEn", fullNames[1], MatchMode.ANYWHERE));
			} else {
				restrictions.addCriterion(Restrictions.or(Restrictions.ilike("ind.firstNameEn", txtFullName.getValue(), MatchMode.ANYWHERE), Restrictions.ilike("ind.lastNameEn", txtFullName.getValue(), MatchMode.ANYWHERE)));
			}
		}
		
		if (StringUtils.isNotEmpty(txtContractId.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("con.reference", txtContractId.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtDueDayFrom.getValue())) {
			restrictions.addCriterion(Restrictions.ge("col.dueDay", MyNumberUtils.getInteger(txtDueDayFrom.getValue(), 0)));
		}
		
		if (StringUtils.isNotEmpty(txtDueDayTo.getValue())) {
			restrictions.addCriterion(Restrictions.le("col.dueDay", MyNumberUtils.getInteger(txtDueDayTo.getValue(), 0)));
		}
		
		if (dfLPDFrom.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("col.lastPaymentDate", dfLPDFrom.getValue()));
		}
		
		if (dfLPDTo.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("col.lastPaymentDate", dfLPDTo.getValue()));
		}
		
		if (StringUtils.isNotEmpty(txtPhone.getValue())) {
			restrictions.addCriterion(Restrictions.eq("coninfo.primary", true));
			restrictions.addCriterion(Restrictions.eq("coninfo.value", txtPhone.getValue()));
		}
		
		if (StringUtils.isNotEmpty(txtCollectedFrom.getValue())) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(Payment.class, "payment");
			subCriteria.add(Restrictions.ge("payment.tiPaidAmount", Double.valueOf(txtCollectedFrom.getValue())));
			subCriteria.setProjection(Projections.projectionList().add(Projections.property("payment.contract.id")));
			restrictions.addCriterion(Property.forName("con.id").in(subCriteria));
		}
		
		if (StringUtils.isNotEmpty(txtCollectedTo.getValue())) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(Payment.class, "payment");
			subCriteria.add(Restrictions.le("payment.tiPaidAmount", Double.valueOf(txtCollectedTo.getValue())));
			subCriteria.setProjection(Projections.projectionList().add(Projections.property("payment.contract.id")));
			restrictions.addCriterion(Property.forName("con.id").in(subCriteria));
		}
		
		if (cbxColResutl.getSelectedEntity() != null) {
			restrictions.addAssociation("col.lastCollectionHistory", "colHis", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("colHis.result", cbxColResutl.getSelectedEntity()));
		}
		
		if (StringUtils.isNotEmpty(txtTotalDueFrom.getValue())) {
			restrictions.addCriterion(Restrictions.ge("col.tiTotalAmountInOverdue", MyNumberUtils.getDouble(txtTotalDueFrom.getValue(), 0d)));
		}
		
		if (StringUtils.isNotEmpty(txtTotalDueTo.getValue())) {
			restrictions.addCriterion(Restrictions.le("col.tiTotalAmountInOverdue", MyNumberUtils.getDouble(txtTotalDueTo.getValue(), 0d)));
		}
		
		if (dfPromisedDateFrom.getValue() != null) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(LockSplit.class, "lckspit");
			subCriteria.add(Restrictions.ge("lckspit.to", dfPromisedDateFrom.getValue()));
			subCriteria.setProjection(Projections.projectionList().add(Projections.property("lckspit.contract.id")));
			restrictions.addCriterion(Property.forName("con.id").in(subCriteria));
		} 
		
		if (dfPromisedDateTo.getValue() != null) {
			DetachedCriteria subCriteria = DetachedCriteria.forClass(LockSplit.class, "lckspit");
			subCriteria.add(Restrictions.le("lckspit.to", dfPromisedDateTo.getValue()));
			subCriteria.setProjection(Projections.projectionList().add(Projections.property("lckspit.contract.id")));
			restrictions.addCriterion(Property.forName("con.id").in(subCriteria));
		}
		
		restrictions.addCriterion(Restrictions.eq(ContractUserInbox.SECUSER, secUser));
		restrictions.setDistinctRootEntity(true);
		
		tablePanel.setRestriction(restrictions);
		return restrictions;
	}

}
