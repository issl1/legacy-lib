package com.nokor.efinance.gui.ui.panel.collection.callcenter;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.service.ContractRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class WelcomeCallFilterPanel extends AbstractControlPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = 6436027110538401795L;

	private static final String FOLLOWUP = I18N.message("follow.up");
	private static final String UNPROCESSED = I18N.message("unprocessed");
	private static final String COMPLETED = I18N.message("completed");
	
	private TextField txtLastName;
	private TextField txtFirstName;
	private TextField txtContractID;
	private TextField txtPhone;
	private ComboBox cbxLastResult;
	private Button btnFilter;
	private Button btnReset;
	
	private WelcomeCallPanel welcomeCallPanel;
	private ECallCenterResult callCenterResult;
	
	/**
	 * 
	 * @param welcomeCallPanel
	 */
	public WelcomeCallFilterPanel(WelcomeCallPanel welcomeCallPanel, ECallCenterResult callCenterResult) {
		this.welcomeCallPanel = welcomeCallPanel;
		this.callCenterResult = callCenterResult;
		txtLastName = ComponentFactory.getTextField(100, 110);
		txtFirstName = ComponentFactory.getTextField(100, 110);
		txtContractID = ComponentFactory.getTextField(50, 100);
		txtPhone = ComponentFactory.getTextField(50, 100);
		cbxLastResult = ComponentFactory.getComboBox();
		cbxLastResult.setWidth(100, Unit.PIXELS);
		cbxLastResult.addItem(FOLLOWUP);
		cbxLastResult.addItem(UNPROCESSED);
		cbxLastResult.addItem(COMPLETED);
		
		Label lblLastName = ComponentFactory.getLabel("lastname");
		Label lblFirstName = ComponentFactory.getLabel("firstname");
		Label lblPhone = ComponentFactory.getLabel("phone");
		Label lblContractID = ComponentFactory.getLabel("contract.id");
		Label lblLastResult = ComponentFactory.getLabel("last.result");
		
		HorizontalLayout controlLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		controlLayout.addComponent(lblLastName);
		controlLayout.addComponent(txtLastName);
		controlLayout.addComponent(lblFirstName);
		controlLayout.addComponent(txtFirstName);
		controlLayout.addComponent(lblPhone);
		controlLayout.addComponent(txtPhone);
		controlLayout.addComponent(lblContractID);
		controlLayout.addComponent(txtContractID);
		
		controlLayout.setComponentAlignment(lblLastName, Alignment.MIDDLE_LEFT);
		controlLayout.setComponentAlignment(lblFirstName, Alignment.MIDDLE_LEFT);
		controlLayout.setComponentAlignment(lblPhone, Alignment.MIDDLE_LEFT);
		controlLayout.setComponentAlignment(lblContractID, Alignment.MIDDLE_LEFT);
		
		if (callCenterResult == null) {
			controlLayout.addComponent(lblLastResult);
			controlLayout.addComponent(cbxLastResult);
			controlLayout.setComponentAlignment(lblLastResult, Alignment.MIDDLE_LEFT);
		}
		
		btnFilter = ComponentLayoutFactory.getDefaultButton("filter", FontAwesome.SEARCH, 70);
		btnReset = ComponentLayoutFactory.getDefaultButton("reset", FontAwesome.ERASER, 70);
		
		btnFilter.addClickListener(this);
		btnReset.addClickListener(this);
		
		HorizontalLayout filterLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		filterLayout.addComponent(btnFilter);
		filterLayout.addComponent(btnReset);
		
		VerticalLayout vLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, true), true);
		vLayout.addComponent(controlLayout);
		vLayout.addComponent(filterLayout);
		
		vLayout.setComponentAlignment(filterLayout, Alignment.MIDDLE_CENTER);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("filters"));
		fieldSet.setContent(vLayout);
		Panel filterPanel = new Panel(fieldSet);
		filterPanel.setStyleName(Reindeer.PANEL_LIGHT);
		addComponent(filterPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	protected List<Contract> getContracts() {
		ContractRestriction restrictions = new ContractRestriction();
		
		SecUser userLogin = UserSessionManager.getCurrentUser();
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		if (ProfileUtil.isCallCenterStaff()) {
			userContractSubCriteria.add(Restrictions.eq("secUser", userLogin));
		} else if (ProfileUtil.isCallCenterLeader()) {
			userContractSubCriteria.createAlias("secUser", "usr", JoinType.INNER_JOIN);
			userContractSubCriteria.createAlias("usr.defaultProfile", "pro", JoinType.INNER_JOIN);
			userContractSubCriteria.add(Restrictions.eq("pro.code", IProfileCode.CAL_CEN_STAFF));
		}
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
		restrictions.addCriterion(Property.forName(Contract.ID).in(userContractSubCriteria));
		
		// query find contract id don't have in table call center history
		DetachedCriteria unProcessedCriteria = DetachedCriteria.forClass(CallCenterHistory.class, "calHisto");
		unProcessedCriteria.setProjection(Projections.projectionList().add(Projections.property("calHisto.contract.id")));
		
		DetachedCriteria callCenterHistorySubCriteria = DetachedCriteria.forClass(CallCenterHistory.class, "calHisto");
		if (callCenterResult != null) {
			callCenterHistorySubCriteria.setProjection(Projections.projectionList().add(Projections.property("calHisto.contract.id")));
			if (ECallCenterResult.OTHER.equals(callCenterResult)) {
				callCenterHistorySubCriteria.add(Restrictions.eq(CallCenterHistory.RESULT, ECallCenterResult.OTHER));
				restrictions.addCriterion(Restrictions.or(Property.forName(Contract.ID).in(callCenterHistorySubCriteria), 
						Property.forName(Contract.ID).notIn(unProcessedCriteria)));
			} else {
				callCenterHistorySubCriteria.add(Restrictions.eq(CallCenterHistory.RESULT, callCenterResult));
				restrictions.addCriterion(Property.forName(Contract.ID).in(callCenterHistorySubCriteria));
			}
		}
		
		if (callCenterResult == null && cbxLastResult.getValue() != null) {
			ECallCenterResult callCenterResult = null;
			if (cbxLastResult.isSelected(FOLLOWUP)) {
				callCenterResult = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.KO);
			} else if (cbxLastResult.isSelected(COMPLETED)) {
				callCenterResult = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.OK);
			} else if (cbxLastResult.isSelected(UNPROCESSED)) {
				callCenterResult = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.OTHER);
			}
			callCenterHistorySubCriteria.setProjection(Projections.projectionList().add(Projections.property("calHisto.contract.id")));
			if (ECallCenterResult.OTHER.equals(callCenterResult)) {
				callCenterHistorySubCriteria.add(Restrictions.eq(CallCenterHistory.RESULT, ECallCenterResult.OTHER));
				restrictions.addCriterion(Restrictions.or(Property.forName(Contract.ID).in(callCenterHistorySubCriteria), 
						Property.forName(Contract.ID).notIn(unProcessedCriteria)));
			} else {
				callCenterHistorySubCriteria.add(Restrictions.eq(CallCenterHistory.RESULT, callCenterResult));
				restrictions.addCriterion(Property.forName(Contract.ID).in(callCenterHistorySubCriteria));
			}
		}
		
		if (StringUtils.isNotEmpty(txtContractID.getValue())) {
			restrictions.addCriterion(Restrictions.eq(Contract.REFERENCE, txtContractID.getValue()));
		}
		
		restrictions.setLastName(txtLastName.getValue());
		restrictions.setFirstName(txtFirstName.getValue());
		if (StringUtils.isNotEmpty(txtPhone.getValue())) {
			restrictions.addAssociation("ind.individualContactInfos", "incont", JoinType.INNER_JOIN);
			restrictions.addAssociation("incont.contactInfo", "cont", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.eq("cont.value", txtPhone.getValue()));
		}
		
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnFilter)) {
			assignValues();
		} else if (event.getButton().equals(btnReset)) {
			this.reset();
		}
	}
	
	/**
	 * 
	 */
	protected void assignValues() {
		welcomeCallPanel.assignValues();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		txtContractID.setValue(StringUtils.EMPTY);
		txtLastName.setValue(StringUtils.EMPTY);
		txtPhone.setValue(StringUtils.EMPTY);
		cbxLastResult.setValue(StringUtils.EMPTY);
	}
	
}
