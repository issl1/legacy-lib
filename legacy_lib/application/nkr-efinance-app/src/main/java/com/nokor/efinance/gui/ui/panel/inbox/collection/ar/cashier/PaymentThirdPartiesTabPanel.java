package com.nokor.efinance.gui.ui.panel.inbox.collection.ar.cashier;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.actor.model.ThirdParty;
import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentThirdParty;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.share.contract.PaymentType;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class PaymentThirdPartiesTabPanel extends AbstractControlPanel implements FinServicesHelper, SelectedTabChangeListener, ClickListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 875199992565396223L;
	private static final String PAYEE_REFERENCE = "payee.reference";
	private static final String PAYEE_FIRSTNAME = "payee.firstname";
	private static final String PAYEE_LASTNAME = "payee.lastname";
	private static final String BALANCE = "balance";
	private static final String ACTIONS = "actions";
	
	private TextField txtFirstName;
	private TextField txtLastName;
	private TextField txtPayeeReference;
	
	private Button btnSearch;
	private Button btnReset;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Entity> simplePagedTable;
	
	private VerticalLayout mainLayout;
	
	private TabSheet tabSheet;
	private ColAuctionBidderDetailPanel bidderDetailPanel;
	
	private PaymentType paymentType;
	
	
	public PaymentThirdPartiesTabPanel(PaymentType paymentType) {
		this.paymentType = paymentType;
		
		setMargin(true);
		init();
		
		tabSheet = new TabSheet();
		tabSheet.addSelectedTabChangeListener(this);
		bidderDetailPanel = new ColAuctionBidderDetailPanel();
		
		VerticalLayout tablePanel = ComponentLayoutFactory.getVerticalLayout(false, true);
		tablePanel.addComponent(simplePagedTable);
		tablePanel.addComponent(simplePagedTable.createControls());
		
		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(createFilterPanel());
		mainLayout.addComponent(tablePanel);
		
		if (paymentType.equals(PaymentType.AUCTION)) {
			tabSheet.addTab(mainLayout, I18N.message("auctions"));
		} else if (paymentType.equals(PaymentType.CLAIM)) {
			tabSheet.addTab(mainLayout, I18N.message("claims"));
		} else if (paymentType.equals(PaymentType.COMPENSATION)) {
			tabSheet.addTab(mainLayout, I18N.message("compensations"));
		}
		addComponent(tabSheet);
	}
	
	
	/**
	 * 
	 */
	private void init() {
		txtFirstName = ComponentFactory.getTextField();
		txtLastName = ComponentFactory.getTextField();
		txtPayeeReference = ComponentFactory.getTextField();
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnSearch.addClickListener(this);
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(this);
		
		this.columnDefinitions = createColumnDifinition();
		simplePagedTable = new SimplePagedTable<>(this.columnDefinitions);
		simplePagedTable.addItemClickListener(new ItemClickListener() {
			
			private static final long serialVersionUID = -2285461979199500768L;

			@Override
			public void itemClick(ItemClickEvent event) {
				Item item = event.getItem();
				if (event.isDoubleClick()) {
					Long paymentThirdPartiesID = (Long) item.getItemProperty(PaymentThirdParty.ID).getValue();
					tabSheet.addTab(bidderDetailPanel, I18N.message("detail"));
					tabSheet.setSelectedTab(bidderDetailPanel);
				}
			}
		});
		
		assignValue();
	}
	
	/**
	 * AssignValue to table
	 */
	public void assignValue() {
		List<PaymentThirdParty> paymentThirdParties = PAYMENT_SRV.getPaymentThirdParty(paymentType);
		simplePagedTable.setContainerDataSource(getIndexedContainer(paymentThirdParties));
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel createFilterPanel() {
		GridLayout filterGridPanel = ComponentLayoutFactory.getGridLayout(6, 1);
		filterGridPanel.setSpacing(true);
		 
		Label lblFirstName = ComponentLayoutFactory.getLabelCaption("firstname");
		Label lblLastName = ComponentLayoutFactory.getLabelCaption("lastname");
		Label lblPayeeReference = ComponentLayoutFactory.getLabelCaption("reference");
		
		filterGridPanel.addComponent(lblFirstName, 0, 0);
		filterGridPanel.addComponent(txtFirstName, 1, 0);
		filterGridPanel.addComponent(lblLastName, 2, 0);
		filterGridPanel.addComponent(txtLastName, 3, 0);
		filterGridPanel.addComponent(lblPayeeReference, 4, 0);
		filterGridPanel.addComponent(txtPayeeReference, 5, 0);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonLayout.addComponent(btnSearch);
		buttonLayout.addComponent(btnReset);
		
		VerticalLayout filterLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		filterLayout.addComponent(filterGridPanel);
		filterLayout.addComponent(buttonLayout);
		filterLayout.setComponentAlignment(filterGridPanel, Alignment.TOP_CENTER);
		filterLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("filters"));
		fieldSet.setContent(filterLayout);
		
		Panel filterPanel = new Panel();
		filterPanel.setStyleName(Reindeer.PANEL_LIGHT);
		filterPanel.setContent(fieldSet);
		
		return filterPanel;
	}
	
	public void refresh() {
		reset();
		doSearch();
	}
	
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDifinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(PaymentThirdParty.ID, I18N.message("id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition(PAYEE_REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(PAYEE_FIRSTNAME, I18N.message("firstname"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(PAYEE_LASTNAME, I18N.message("lastname"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(BALANCE, I18N.message("balance"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ACTIONS, I18N.message("actions"), Button.class, Align.CENTER, 60));
		return columnDefinitions;
	}
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer(List<PaymentThirdParty> paymentThirdParties) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (PaymentThirdParty paymentThirdParty : paymentThirdParties) {
			Cashflow cashflow = paymentThirdParty.getCashflow();
			ThirdParty thirdParty = paymentThirdParty.getThirdParty();
			
			Item item = indexedContainer.addItem(paymentThirdParty.getId());
			item.getItemProperty(PaymentThirdParty.ID).setValue(paymentThirdParty.getId());
			if (thirdParty != null) {
				item.getItemProperty(PAYEE_REFERENCE).setValue(thirdParty.getReference());
				item.getItemProperty(PAYEE_FIRSTNAME).setValue(thirdParty.getFirstName());
				item.getItemProperty(PAYEE_LASTNAME).setValue(thirdParty.getLastName());
			}
			item.getItemProperty(BALANCE).setValue(AmountUtils.format(cashflow != null ? cashflow.getTiInstallmentAmount() : 0d));
			item.getItemProperty(ACTIONS).setValue(new ButtonRenderer(paymentThirdParty));
		}
		return indexedContainer;
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		txtFirstName.setValue("");
		txtLastName.setValue("");
		txtPayeeReference.setValue("");
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			doSearch();
		} else if (event.getButton() == btnReset) {
			reset();
		}
		
	}
	/**
	 * search
	 */
	private void doSearch() {
		BaseRestrictions<PaymentThirdParty> restrictions = new BaseRestrictions<PaymentThirdParty>(PaymentThirdParty.class);
		restrictions.addCriterion(Restrictions.eq("paymentType", paymentType));
		
		if (StringUtils.isNotEmpty(txtFirstName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("payeeFirstName", txtFirstName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtLastName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("payeeLastName", txtLastName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtPayeeReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("payeeReference", txtPayeeReference.getValue(), MatchMode.ANYWHERE));
		}
		
		List<PaymentThirdParty> paymentThirdParties = PAYMENT_SRV.list(restrictions);
		simplePagedTable.setContainerDataSource(getIndexedContainer(paymentThirdParties));
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class ButtonRenderer extends Button {
		
		private static final long serialVersionUID = 1054792636555766271L;

		public ButtonRenderer(PaymentThirdParty paymentThirdParty) {
			setStyleName(Reindeer.BUTTON_LINK);
			setIcon(FontAwesome.EDIT);
			addClickListener(new ClickListener() {
				private static final long serialVersionUID = 5421345974152699417L;

				@Override
				public void buttonClick(ClickEvent event) {
					tabSheet.addTab(bidderDetailPanel, I18N.message("detail"));
					tabSheet.setSelectedTab(bidderDetailPanel);
				}
			});
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		if (tabSheet.getSelectedTab().equals(mainLayout)) {
			if (tabSheet.getTab(bidderDetailPanel) != null) {
				tabSheet.removeTab(tabSheet.getTab(bidderDetailPanel));
			}
		} 
	}

}
