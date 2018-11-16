package com.nokor.efinance.gui.ui.panel.dashboard.summaries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ContractSummariedSearchPanel extends AbstractControlPanel implements FinServicesHelper, ClickListener {

	private static final long serialVersionUID = 7029703567024580095L;
	
	private static final String SUMMERY = I18N.message("summary");
	private static final String DETAIL = I18N.message("detail");
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private SecUserComboBox cbxStaff;
	private OptionGroup cbOptionGroup;
	private ERefDataComboBox<EWkfStatus> cbxStatus;
	
	private Button btnSearch;
	private Button btnReset;
	
	private ContractSummariedTablePanel tablePanel;
	
	/**
	 * 
	 */
	public ContractSummariedSearchPanel(ContractSummariedTablePanel tablePanel) {
		this.tablePanel = tablePanel;

		List<EWkfStatus> wkfStatus = ContractWkfStatus.listSummaryStatus();
		wkfStatus.remove(ContractWkfStatus.PEN);
		cbxStatus = new ERefDataComboBox<>(wkfStatus);
		
		String[] profiles = {IProfileCode.CMLEADE, IProfileCode.CMSTAFF};
		cbxStaff = new SecUserComboBox(COL_SRV.getCollectionUsers(profiles));
		
		dfStartDate = ComponentFactory.getAutoDateField();
		dfStartDate.setValue(DateUtils.today());
		
		dfEndDate = ComponentFactory.getAutoDateField();
		dfEndDate.setValue(DateUtils.today());
		
		List<String> options = Arrays.asList(new String[] { DETAIL, SUMMERY});
		cbOptionGroup = ComponentLayoutFactory.getOptionGroup(options);
		cbOptionGroup.select(DETAIL);
		cbOptionGroup.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -3910160882457408324L;
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbOptionGroup.isSelected(DETAIL)) {
					tablePanel.isDetail();
				} else if (cbOptionGroup.isSelected(SUMMERY)) {
					tablePanel.isSummary();
				}				
			}
		});
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnSearch.addClickListener(this);
		
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(this);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnSearch);
		buttonLayout.addComponent(btnReset);
		
		Label lblStatus = ComponentLayoutFactory.getLabelCaption("status");
		Label lblStaff = ComponentLayoutFactory.getLabelCaption("staff");
		Label lblStartDate = ComponentLayoutFactory.getLabelCaption("start.date");
		Label lblEndDate = ComponentLayoutFactory.getLabelCaption("end.date");
		
		GridLayout searchLayout = new GridLayout(11, 2);
		searchLayout.setSpacing(true);
		
		int iCol = 0;
		searchLayout.addComponent(lblStatus, iCol++, 0);
		searchLayout.addComponent(cbxStatus, iCol++, 0);
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
		searchLayout.addComponent(lblStartDate, iCol++, 0);
		searchLayout.addComponent(dfStartDate, iCol++, 0);
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
		searchLayout.addComponent(lblEndDate, iCol++, 0);
		searchLayout.addComponent(dfEndDate, iCol++, 0);
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
		searchLayout.addComponent(lblStaff, iCol++, 0);
		searchLayout.addComponent(cbxStaff, iCol++, 0);
		
		searchLayout.addComponent(cbOptionGroup, 10, 1);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.addComponent(searchLayout);
		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(searchLayout, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		
		Panel mainSearchPanel = ComponentLayoutFactory.getPanel(mainLayout, true, false);
		mainSearchPanel.setCaption(I18N.message("search"));
		
		addComponent(mainSearchPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ContractUserInbox> getRestriction() {
		BaseRestrictions<ContractUserInbox> restrictions = new BaseRestrictions<>(ContractUserInbox.class);
		restrictions.addAssociation("contract", "con", JoinType.INNER_JOIN);

		List<EWkfStatus> wkfStatus = ContractWkfStatus.listSummaryStatus();
		wkfStatus.remove(ContractWkfStatus.RECEIVED);
		restrictions.addCriterion(Restrictions.in("con.wkfStatus", wkfStatus));
		
		if (cbxStatus.getSelectedEntity() != null) {
			if (ContractWkfStatus.RECEIVED.equals(cbxStatus.getSelectedEntity())) {
				restrictions.addCriterion(Restrictions.isNotNull("contract"));
				restrictions.addCriterion(Restrictions.isNull("secUser"));
			} else {
				restrictions.addCriterion(Restrictions.eq("con.wkfStatus", cbxStatus.getSelectedEntity()));
				
				List<String> userUpdates = new ArrayList<>();
				List<SecUser> secUsers = COL_SRV.getCollectionUsers(new String[] {IProfileCode.CMSTAFF});
				secUsers.add(UserSessionManager.getCurrentUser());
				for (SecUser secUser : secUsers) {
					userUpdates.add(secUser.getLogin());
				}
				restrictions.addCriterion(Restrictions.in("con.updateUser", userUpdates));
			}
		}
		
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("con.updateDate", DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("con.updateDate", DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		
		if (cbxStaff.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.like("con.updateUser", cbxStaff.getSelectedEntity().getLogin(), MatchMode.ANYWHERE));
		}
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		cbxStatus.setSelectedEntity(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
		cbxStaff.setSelectedEntity(null);
	}
	
	/**
	 * 
	 * @param event
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			List<Contract> contracts = new ArrayList<Contract>();
			List<ContractUserInbox> contractUserInboxs = getRestriction();
			for (ContractUserInbox contractUserInbox : contractUserInboxs) {
				contracts.add(contractUserInbox.getContract());
			}
			tablePanel.assignValue(contracts);
		} else if (event.getButton() == btnReset) {
			reset();
		}
	}
}
