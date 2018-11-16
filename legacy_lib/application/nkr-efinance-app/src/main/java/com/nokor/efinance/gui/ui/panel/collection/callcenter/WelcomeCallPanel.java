package com.nokor.efinance.gui.ui.panel.collection.callcenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.MApplicant;
import com.nokor.efinance.core.applicant.service.CompanyContactInfoUtils;
import com.nokor.efinance.core.callcenter.CallCenterHistoryRestriction;
import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.MCollection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColPhoneContractHistoryPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;
import com.vaadin.ui.Table.HeaderClickListener;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class WelcomeCallPanel extends AbstractControlPanel implements FinServicesHelper, MCollection, SelectedItem, ItemClickListener {

	/** */
	private static final long serialVersionUID = -3834010961901133153L;
	
	private SimpleTable<ContractUserInbox> simpleTable;
	private List<Long> selectedIds = new ArrayList<>();
	private boolean selectAll = false;
	private Item selectedItem;
	
	private WelcomeCallFilterPanel filterPanel;

	/**
	 * 
	 * @param callCenterResult
	 */
	public WelcomeCallPanel(ECallCenterResult callCenterResult) {
		simpleTable = new SimpleTable<ContractUserInbox>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(25);
		simpleTable.addItemClickListener(this);
		
		simpleTable.setColumnIcon(SELECT, FontAwesome.CHECK);
		simpleTable.addHeaderClickListener(new HeaderClickListener() {
			
			/** */
			private static final long serialVersionUID = 171185791599577635L;

			/**
			 * @see com.vaadin.ui.Table.HeaderClickListener#headerClick(com.vaadin.ui.Table.HeaderClickEvent)
			 */
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == SELECT) {
					selectAll = !selectAll;
					@SuppressWarnings("unchecked")
					java.util.Collection<Long> ids = (java.util.Collection<Long>) simpleTable.getItemIds();
					for (Long id : ids) {
						Item item = simpleTable.getItem(id);
						CheckBox cbSelect = (CheckBox) item.getItemProperty(SELECT).getValue();
						cbSelect.setImmediate(true);
						cbSelect.setValue(selectAll);
					}
				}
			}
		});
		
		filterPanel = new WelcomeCallFilterPanel(this, callCenterResult);
		
		Panel tablePanel = new Panel(simpleTable);
		tablePanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		setMargin(true);
		setSpacing(true);
		addComponent(filterPanel);
		addComponent(tablePanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(SELECT, StringUtils.EMPTY, CheckBox.class, Align.CENTER, 25));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(CONTRACTID, I18N.message("contract.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(WKFSTATUS, I18N.message("status"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(MApplicant.LASTNAME, I18N.message("lastname"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(MApplicant.FIRSTNAME, I18N.message("firstname"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(Contract.FIRSTDUEDATE, I18N.message("first.due.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(PRIMARYPHONENO, I18N.message("primary.phone.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(GUARANTOR, I18N.message("guarantor"), Integer.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition(NEXTDUEDATE, I18N.message("next.due.date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(DAYSSINCECONTRACT, I18N.message("days.since.contract"), Long.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition(LATESTRESULT, I18N.message("last.result"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * 
	 */
	public void assignValues() {
		setContainer(filterPanel.getContracts());
	}
	
	/**
	 * 
	 * @param contracts
	 */
	@SuppressWarnings("unchecked")
	private void setContainer(List<Contract> contracts) {
		simpleTable.removeAllItems();
		Container container = simpleTable.getContainerDataSource();
		if (contracts != null && !contracts.isEmpty()) {
			for (Contract con : contracts) {
				Item item = container.addItem(con.getId());
				item.getItemProperty(SELECT).setValue(getRenderSelected(con.getId()));
				item.getItemProperty(ID).setValue(con.getId());
				item.getItemProperty(CONTRACTID).setValue(con.getReference());
				item.getItemProperty(WKFSTATUS).setValue(con.getWkfStatus().getDescLocale());
				item.getItemProperty(Contract.FIRSTDUEDATE).setValue(con.getFirstDueDate());
				item.getItemProperty(GUARANTOR).setValue(con.getNumberGuarantors());
				Long daysSinceContract = null;
				if (con.getStartDate() != null) {
					daysSinceContract = DateUtils.getDiffInDays(DateUtils.today(), con.getStartDate());
				}
				item.getItemProperty(DAYSSINCECONTRACT).setValue(Math.abs(daysSinceContract));
				
				CallCenterHistory callCenterHistory = getCallCenterHistory(con.getId()); 
				String result = StringUtils.EMPTY;
				if (callCenterHistory != null && callCenterHistory.getResult() != null) {
					if (ECallCenterResult.OK.equals(callCenterHistory.getResult())) {
						result = I18N.message("completed");
					} else if (ECallCenterResult.KO.equals(callCenterHistory.getResult())) {
						result = I18N.message("follow.up");
					} else if (ECallCenterResult.OTHER.equals(callCenterHistory.getResult())) {
						result = I18N.message("unprocessed");
					}
				} else {
					result = I18N.message("unprocessed");
				}
				item.getItemProperty(LATESTRESULT).setValue(result);
				
				Applicant app = con.getApplicant();
				if (app != null) {
					item.getItemProperty(MApplicant.LASTNAME).setValue(app.getLastNameLocale());
					item.getItemProperty(MApplicant.FIRSTNAME).setValue(app.getFirstNameLocale());
					Individual ind = app.getIndividual();
					Company com = app.getCompany();
					String primaryPhone = StringUtils.EMPTY;
					if (ind != null) {
						primaryPhone = ind.getIndividualPrimaryContactInfo();
					} else if (com != null) {
						primaryPhone = CompanyContactInfoUtils.getPrimaryContactInfo(com);
					}
					item.getItemProperty(PRIMARYPHONENO).setValue(primaryPhone);
				}
				
				Collection col = con.getCollection();
				if (col != null) {
					item.getItemProperty(NEXTDUEDATE).setValue(col.getNextDueDate());
				}
			}
		}
	}
	
	/**
	 * 
	 * @param conId
	 * @return
	 */
	private CallCenterHistory getCallCenterHistory(Long conId) {
		CallCenterHistoryRestriction restrictions = new CallCenterHistoryRestriction();
		restrictions.setContractId(conId);
		if (!ENTITY_SRV.list(restrictions).isEmpty()) {
			return ENTITY_SRV.list(restrictions).get(0);
		}
		return null;
	}
	
	/**
	 * @param conId
	 * @return
	 */
	private CheckBox getRenderSelected(Long conId) {
		final CheckBox checkBox = new CheckBox();
		checkBox.setImmediate(true);
		checkBox.setData(conId);
		checkBox.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -6018292475737639109L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				Long id = (Long) checkBox.getData();
				if (checkBox.getValue()) {
					selectedIds.add(id);
				} else {
					selectedIds.remove(id);
				}
			}
		});
		return checkBox;
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick()) {
			Page.getCurrent().setUriFragment("!" + ColPhoneContractHistoryPanel.NAME + "/" + getItemSelectedId());
		}
	}
	
	/**
	 * @return
	 */
	private Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty(ID).getValue());
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
}
