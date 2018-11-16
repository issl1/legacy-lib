package com.nokor.efinance.gui.ui.panel.report.registrationexpenses;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationRegistrationExpense;
import com.nokor.efinance.core.shared.accounting.RegistrationExpense;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AmountField;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ButtonFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 *@author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(QuotationRegistrationExpensesReportPanel.NAME)
public class QuotationRegistrationExpensesReportPanel extends AbstractTabPanel implements View, ContractEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = 8019051395954081483L;

	public static final String NAME = "registration.expenses.report.panel";
			
	private TabSheet tabSheet;
	List<Quotation> quotations;
	
	private SimplePagedTable<RegistrationExpense> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private TextField txtContractReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	private Button btnSave;
	private AmountField txtRegistrationFee;
	private AmountField txtOtherFees;
	private AmountField txtGasoline;
	private AmountField txtParking;
    private AmountField txtTotal;
    private List<QuotationRegistrationExpense> quotationRegistrationExpenses;
    private SecUserDetail usrDetail;
    private ValueChangeListener valueChangeListener;
    private SecUser secUser;
	
	public QuotationRegistrationExpensesReportPanel() {
		super();
		setSizeFull();
	}
	
	@SuppressWarnings("serial")
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		
		ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		
		btnSave = ButtonFactory.getSaveButton();

		tblButtonsPanel.addButton(btnSave);

		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1717161084451001316L;

			@Override
			public void buttonClick(ClickEvent event) {
				//do something
				save();
			}
		});
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout gridLayoutPanel = new VerticalLayout();
		VerticalLayout searchLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		final GridLayout gridLayout = new GridLayout(8, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.values());
		cbxDealerType.setImmediate(true);
		valueChangeListener = new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
				restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		};
		cbxDealerType.addValueChangeListener(valueChangeListener);
		/**
		 * Set cbxDealer and cbxDealerType Disable
		 */
		if (ProfileUtil.isCreditOfficer() || ProfileUtil.isProductionOfficer()) {
        	cbxDealer.setEnabled(false);
        	cbxDealerType.setEnabled(false);
        }
		
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		usrDetail = ENTITY_SRV.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
		/**
		 * Set Dealer name and Dealer type from Dashboard to cbxDealer & cbxDealerType
		 */
		if (ProfileUtil.isPOS() && usrDetail != null && usrDetail.getDealer() != null) {
			cbxDealer.setSelectedEntity(usrDetail.getDealer());
			cbxDealerType.removeValueChangeListener(valueChangeListener);
			cbxDealerType.setSelectedEntity(cbxDealer.getSelectedEntity() != null ? cbxDealer.getSelectedEntity().getDealerType() : null);
			cbxDealerType.addValueChangeListener(valueChangeListener);
		} 
		
		txtContractReference = ComponentFactory.getTextField(false, 20, 150);
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.getDateAtBeginningOfMonth(DateUtils.todayH00M00S00()));
		
		dfEndDate = ComponentFactory.getAutoDateField("", false);
		dfEndDate.setValue(DateUtils.getDateAtEndOfMonth(DateUtils.todayH00M00S00()));

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtContractReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("startdate")), iCol++, 1);
        gridLayout.addComponent(dfStartDate, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("enddate")), iCol++, 1);
        gridLayout.addComponent(dfEndDate, iCol++, 1);
        
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<>(this.columnDefinitions);
        pagedTable.setFooterVisible(true);
        pagedTable.setColumnFooter(DATE, I18N.message("total"));
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(tblButtonsPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("registration.expenses"));
        
        return tabSheet;
	}
	
	public void reset() {
		txtContractReference.setValue("");
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}
	
	/**
	 * Search
	 */
	private void search() {
		setIndexedContainer();
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer() {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		quotations = ENTITY_SRV.list(getQuontationRestrictions());
		indexedContainer.removeAllItems();
		double totalRegistrationFee = 0d;
		double totalOtherFees = 0d;
		double totalGasoline = 0d;
		double totalParking = 0d;
		double totalFooter = 0d;
		
		quotationRegistrationExpenses = new ArrayList<QuotationRegistrationExpense>();
		for (Quotation quotation : quotations) {
			/*txtRegistrationFee.setEnabled(true);
			txtOtherFees.setEnabled(true);
			txtGasoline.setEnabled(true);
			txtParking.setEnabled(true);*/
			QuotationRegistrationExpense quotationRegistrationExpense = ENTITY_SRV.getByField(QuotationRegistrationExpense.class, "quotation", quotation);
			if (quotationRegistrationExpense == null) {
				quotationRegistrationExpense = new QuotationRegistrationExpense();
				quotationRegistrationExpense.setQuotation(quotation);
			}
			//set all item to array list 
		    secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			quotationRegistrationExpenses.add(quotationRegistrationExpense);
			Item item = indexedContainer.addItem(quotation.getId());
			
			item.getItemProperty(REFERENCE).setValue(quotation.getReference());
			item.getItemProperty(DATE).setValue(quotationRegistrationExpense.getUpdateDate());
			
			txtRegistrationFee = new AmountField();
			txtOtherFees = new AmountField();
			txtGasoline = new AmountField();
			txtParking = new AmountField();
			txtTotal = new AmountField();
			
			if(quotationRegistrationExpense.getSecUser() != null){
				item.getItemProperty("po.name").setValue(quotationRegistrationExpense.getSecUser().getDesc());
				txtRegistrationFee.setEnabled(false);
				txtOtherFees.setEnabled(false);
				txtGasoline.setEnabled(false);
				txtParking.setEnabled(false);
			}else{
				item.getItemProperty("po.name").setValue(secUser.getDesc());
			}
			
			//Insert Amount to TextField
			txtRegistrationFee.setValue(AmountUtils.format(quotationRegistrationExpense.getRegistrationFeeUsd() == null ? 0d
							                : quotationRegistrationExpense.getRegistrationFeeUsd()));
			txtOtherFees.setValue(AmountUtils.format(quotationRegistrationExpense.getOtherFeeUsd() == null ? 0d
	                                        : quotationRegistrationExpense.getOtherFeeUsd()));
			txtGasoline.setValue(AmountUtils.format(quotationRegistrationExpense.getGasolineFeeUsd() == null ? 0d
	                                        : quotationRegistrationExpense.getGasolineFeeUsd()));
			txtParking.setValue(AmountUtils.format(quotationRegistrationExpense.getParkingFeeUsd() == null ? 0d
                                            : quotationRegistrationExpense.getParkingFeeUsd()));
			if(quotationRegistrationExpense.getRegistrationFeeUsd() != null && quotationRegistrationExpense.getOtherFeeUsd() != null
				&&	quotationRegistrationExpense.getGasolineFeeUsd() != null && quotationRegistrationExpense.getParkingFeeUsd() != null){
				txtTotal.setValue(AmountUtils.format(quotationRegistrationExpense.getRegistrationFeeUsd()
						                                      + quotationRegistrationExpense.getOtherFeeUsd()
						                                      + quotationRegistrationExpense.getGasolineFeeUsd()
						                                      + quotationRegistrationExpense.getParkingFeeUsd()));
			}else {
				txtTotal.setValue("" + 0d);
			}
			
			txtTotal.setEnabled(false);
					                                      
			item.getItemProperty("registration.fee").setValue(txtRegistrationFee);
			item.getItemProperty("other.fees").setValue(txtOtherFees);
			item.getItemProperty("gasoline").setValue(txtGasoline);
			item.getItemProperty("parking").setValue(txtParking);
			item.getItemProperty("total").setValue(txtTotal);
			
			//Total each field
			totalRegistrationFee += MyNumberUtils.getDouble(Double.valueOf(txtRegistrationFee.getValue()));
			totalOtherFees += MyNumberUtils.getDouble(Double.valueOf(txtOtherFees.getValue()));
			totalGasoline += MyNumberUtils.getDouble(Double.valueOf(txtGasoline.getValue()));
			totalParking += MyNumberUtils.getDouble(Double.valueOf(txtParking.getValue()));
			totalFooter += MyNumberUtils.getDouble(Double.valueOf(txtTotal.getValue()));
		}
		
		pagedTable.setColumnFooter("registration.fee", AmountUtils.format(totalRegistrationFee));
		pagedTable.setColumnFooter("other.fees", AmountUtils.format(totalOtherFees));
		pagedTable.setColumnFooter("gasoline", AmountUtils.format(totalGasoline));
		pagedTable.setColumnFooter("parking", AmountUtils.format(totalParking));
		pagedTable.setColumnFooter("total", AmountUtils.format(totalFooter));
		
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("po.name", I18N.message("po.name"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("contract.reference"), String.class, Align.LEFT, 170));
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("registration.fee", I18N.message("registration.fee"), AmountField.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("other.fees", I18N.message("other.fees"), AmountField.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("gasoline", I18N.message("gasoline"), AmountField.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("parking", I18N.message("parking"), AmountField.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("total", I18N.message("total"), AmountField.class, Align.LEFT, 160));
		return columnDefinitions;
	}
	/**
	 * Query Date Form Quotation
	 * @return
	 */
	public BaseRestrictions<Quotation> getQuontationRestrictions(){
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		if (isValid()) {
			Date startDate;
			Date endDate;
			Dealer dealer = cbxDealer.getSelectedEntity();
			String reference = txtContractReference.getValue();
			startDate = DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue());
			endDate = DateUtils.getDateAtBeginningOfDay(dfEndDate.getValue());
			restrictions.addCriterion(Restrictions.in("quotationStatus", new EWkfStatus[] {QuotationWkfStatus.ACT, QuotationWkfStatus.ACG, QuotationWkfStatus.RVG, QuotationWkfStatus.RCG, QuotationWkfStatus.LCG}));
			
			restrictions.addCriterion(Restrictions.le("contractStartDate", endDate));
			restrictions.addCriterion(Restrictions.ge("contractStartDate", startDate));
			
			if (dealer != null) {
				restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
			}
			
			if (StringUtils.isNotEmpty(reference)) {
				restrictions.addCriterion(Restrictions.ilike(REFERENCE, reference, MatchMode.ANYWHERE));
			}
			
		}
		return restrictions;
	}
	public void save(){
		boolean saveSuccess = false;
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		//quotations = ENTITY_SRV.list(getQuontationRestrictions());
		if(quotationRegistrationExpenses != null ){
			for (QuotationRegistrationExpense quotationRegistrationExpense : quotationRegistrationExpenses) {
				Item item = indexedContainer.getItem(quotationRegistrationExpense.getQuotation().getId());
				
				//Set each field to Database
				if(quotationRegistrationExpense.getSecUser() == null){
					if (Double.valueOf(item.getItemProperty("registration.fee").getValue().toString()) != 0d
							|| Double.valueOf(item.getItemProperty("other.fees").getValue().toString()) != 0d
							|| Double.valueOf(item.getItemProperty("gasoline").getValue().toString()) != 0d
							|| Double.valueOf(item.getItemProperty("parking").getValue().toString()) != 0d){
						quotationRegistrationExpense.setSecUser(secUser);
						saveSuccess = true;
					}
				}
				quotationRegistrationExpense.setRegistrationFeeUsd(getDouble((TextField)item.getItemProperty(
						"registration.fee").getValue()));
				quotationRegistrationExpense.setOtherFeeUsd(getDouble((TextField)item.getItemProperty(
						"other.fees").getValue()));
				quotationRegistrationExpense.setGasolineFeeUsd(getDouble((TextField)item.getItemProperty(
						"gasoline").getValue()));
				quotationRegistrationExpense.setParkingFeeUsd(getDouble((TextField)item.getItemProperty(
						"parking").getValue()));
				if(quotationRegistrationExpense.getSecUser() != null){
					ENTITY_SRV.saveOrUpdate(quotationRegistrationExpense);
				}	
			}
			if(saveSuccess){
				Notification.show("", "Save Successfuly!!",Type.HUMANIZED_MESSAGE);
				search();
			}
			
		}
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {
		removeErrorsPanel();
		checkMandatoryDateField(dfStartDate, "startdate");
		checkMandatoryDateField(dfEndDate, "enddate");
		if (!errors.isEmpty()) {
			displayErrorsPanel();
		}
		return errors.isEmpty();
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
