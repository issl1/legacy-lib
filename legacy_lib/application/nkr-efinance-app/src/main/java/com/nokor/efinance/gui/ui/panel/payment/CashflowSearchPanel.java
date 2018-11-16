package com.nokor.efinance.gui.ui.panel.payment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.efinance.core.contract.model.cashflow.ECashflowType;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.contract.service.cashflow.CashflowService;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

/**
 * @author heng.mao
 */
public class CashflowSearchPanel extends VerticalLayout implements CashflowEntityField {

	private static final long serialVersionUID = -1336880058348434361L;
	
	private final CashflowService cashflowService = SpringUtils.getBean(CashflowService.class);
	
	//Search cash flow
	private ERefDataComboBox<ETreasuryType> cbxTreasuryType;
	private ERefDataComboBox<ECashflowType> cbxType;
	//private ERefDataComboBox<EPaymentMethod> cbxPaymentMethod;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private TextField txtReference;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;

	private SimplePagedTable<Cashflow> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private Set<Long> selectedItemIds;
	private AddPaymentPanel addPaymentPanel;
	private Double totalAmount;
	private Button btnSearch;
	private Double installment;
	private Double installmentOther;

	/**
	 * @param addPaymentPanel
	 */
	public CashflowSearchPanel(AddPaymentPanel addPaymentPanel) {
		this.addPaymentPanel = addPaymentPanel;
		addComponent(createForm());
	}

	/**
	 * @returnO
	 */
	protected Component createForm() {	
		final GridLayout gridLayout = new GridLayout(14, 3);
		gridLayout.setSpacing(true);
		
		totalAmount = Double.valueOf(0);
		installment = Double.valueOf(0);
		installmentOther = Double.valueOf(0);
		selectedItemIds = new HashSet<Long>();
		
		cbxTreasuryType = new ERefDataComboBox<ETreasuryType>(ETreasuryType.class);
		cbxType = new ERefDataComboBox<ECashflowType>(ECashflowType.class);
		cbxPaymentMethod = new EntityRefComboBox<>();
		cbxPaymentMethod.setRestrictions(new BaseRestrictions<>(EPaymentMethod.class));
		cbxPaymentMethod.renderer();
		txtReference = ComponentFactory.getTextField(false, 60, 150);
		txtReference.setEnabled(false);
		
		dfStartDate = ComponentFactory.getAutoDateField("", false);
		dfEndDate = ComponentFactory.getAutoDateField("", false);       
        
		int iRow = 0;
		int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("treasury")), iCol++, iRow);
        gridLayout.addComponent(cbxTreasuryType, iCol++, iRow);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, iRow);
        gridLayout.addComponent(new Label(I18N.message("type")), iCol++, iRow);
        gridLayout.addComponent(cbxType, iCol++, iRow);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, iRow);
        gridLayout.addComponent(new Label(I18N.message("payment.method")), iCol++, iRow);
        gridLayout.addComponent(cbxPaymentMethod, iCol++, iRow);
        
        iRow++;
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("reference")), iCol++, iRow);
        gridLayout.addComponent(txtReference, iCol++, iRow);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, iRow);
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, iRow);
        gridLayout.addComponent(dfStartDate, iCol++, iRow);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, iRow);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, iRow);
        gridLayout.addComponent(dfEndDate, iCol++, iRow);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Cashflow>(this.columnDefinitions);
        pagedTable.addGeneratedColumn(CONTRACT, new ColumnGenerator() {
			private static final long serialVersionUID = 4294474058240344358L;
			public Component generateCell(Table source, Object itemId, Object columnId) {
                Item item = pagedTable.getItem(itemId);
                final String reference = (String) item.getItemProperty(CONTRACT).getValue();
                final Long contractId = (Long) item.getItemProperty("contract.id").getValue();
                Button btnContract = new Button(reference);
                btnContract.setStyleName(Runo.BUTTON_LINK);
                btnContract.addClickListener(new ClickListener() {
					private static final long serialVersionUID = -5025619822597590714L;
					@Override
					public void buttonClick(ClickEvent event) {
						Page.getCurrent().setUriFragment("!" + ContractsPanel.NAME + "/" + contractId);
					}
				});
                return btnContract;
            }
        });
        pagedTable.addGeneratedColumn(ID, new ColumnGenerator() {
			private static final long serialVersionUID = 6558818209312482753L;
			@Override
			public Object generateCell(Table source, final Object itemId, Object columnId) {
				
				CheckBox cb = new CheckBox();
				cb.addValueChangeListener(new ValueChangeListener() {
					private static final long serialVersionUID = -2120119835501936565L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Double tiInstallmentUsd = Double.valueOf(pagedTable.getItem(itemId).getItemProperty(TI_INSTALLMENT_USD).toString());
						String type = pagedTable.getItem(itemId).getItemProperty(CASHFLOW_TYPE).toString();
						if (selectedItemIds.contains(Long.valueOf(itemId.toString()))) {
							selectedItemIds.remove(Long.valueOf(itemId.toString()));
							totalAmount = Math.round((totalAmount - tiInstallmentUsd) * 100.0) / 100.0;
							addPaymentPanel.setTotalAmount(totalAmount);
							
							if (type.equals(ECashflowType.CAP.getDesc()) || type.equals(ECashflowType.IAP.getDesc())) {
								installment = Math.round((installment - tiInstallmentUsd) * 100.0) / 100.0;
							} else {
								installmentOther = Math.round((installmentOther - tiInstallmentUsd) * 100.0) / 100.0;
							}
							addPaymentPanel.setAmount(installment, installmentOther);
							
			            } else {
			            	selectedItemIds.add(Long.valueOf(itemId.toString()));
			            	totalAmount = Math.round((totalAmount + tiInstallmentUsd) * 100.0) / 100.0;
			            	addPaymentPanel.setTotalAmount(totalAmount);
			            
							if (type.equals(ECashflowType.CAP.getDesc()) || type.equals(ECashflowType.IAP.getDesc())) {
								installment = Math.round((installment + tiInstallmentUsd) * 100.0) / 100.0;
							} else {
								installmentOther = Math.round((installmentOther + tiInstallmentUsd) * 100.0) / 100.0;
							}
							addPaymentPanel.setAmount(installment, installmentOther);
			            }
						
				
						
					}
				});
				return cb;
			}
		});
        
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setMargin(true);
        buttonsLayout.setSpacing(true);
        
        btnSearch = new Button(I18N.message("search"));
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		buttonsLayout.addComponent(btnSearch);
		btnSearch.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6661162238224070578L;
			@Override
			public void buttonClick(ClickEvent event) {
				refreshCashflowTable();
			}
		});	
		
		Button resetButton = new Button(I18N.message("reset"));
		resetButton.setIcon(new ThemeResource("../nkr-default/icons/16/reset.png"));
		buttonsLayout.addComponent(resetButton);
		resetButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6661162238224070578L;
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		
		iRow++;
		iCol = 0;
		gridLayout.addComponent(buttonsLayout, iCol++, iRow);
		
        VerticalLayout contenLayout = new VerticalLayout();
        contenLayout.setMargin(true);
        contenLayout.addComponent(gridLayout);
        contenLayout.addComponent(pagedTable);
        contenLayout.addComponent(pagedTable.createControls());
   
		return contenLayout;
	}
	
	/**
	 * Reset components value 
	 */
	protected void reset() {
		txtReference.setValue("");
		cbxTreasuryType.setSelectedEntity(null);
		cbxType.setSelectedEntity(null);
		cbxPaymentMethod.setSelectedEntity(null);
		dfStartDate.setValue(null);
		dfEndDate.setValue(null);
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Cashflow> cashflows) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (Cashflow cashflow : cashflows) {
			Item item = indexedContainer.addItem(cashflow.getId());
			item.getItemProperty(ID).setValue(cashflow.getId());
			item.getItemProperty("contract.id").setValue(cashflow.getContract().getId());
			item.getItemProperty(CONTRACT).setValue(cashflow.getContract().getReference());
			item.getItemProperty(TREASURY_TYPE).setValue(cashflow.getTreasuryType().getDesc());
			item.getItemProperty(CASHFLOW_TYPE).setValue(cashflow.getCashflowType().getDesc());
			item.getItemProperty(PAYMENT_METHOD).setValue(cashflow.getPaymentMethod().getDescEn());
			item.getItemProperty(NUM_INSTALLMENT).setValue(cashflow.getNumInstallment());
			item.getItemProperty(INSTALLMENT_DATE).setValue(cashflow.getInstallmentDate());
			item.getItemProperty(TI_INSTALLMENT_USD).setValue(AmountUtils.format(cashflow.getTiInstallmentAmount()));
		}
		pagedTable.refreshContainerDataSource();
	}
	
	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("contract.id", I18N.message("contract.id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition(CONTRACT, I18N.message("contract"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(TREASURY_TYPE, I18N.message("treasury"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(CASHFLOW_TYPE, I18N.message("type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PAYMENT_METHOD, I18N.message("payment.method"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(NUM_INSTALLMENT, I18N.message("no"), Integer.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(INSTALLMENT_DATE, I18N.message("installment.date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(TI_INSTALLMENT_USD, I18N.message("amount"), String.class, Align.RIGHT, 70));
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("check"), Long.class, Align.LEFT, 50));
		return columnDefinitions;
	}

	/**
	 * @param cotraId
	 */
	public void setSearchCriteriaValues(String refContract) {
		txtReference.setValue(refContract);
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Cashflow> getRestrictions() {		
		BaseRestrictions<Cashflow> restrictions = new BaseRestrictions<Cashflow>(Cashflow.class);
		
		restrictions.addAssociation("contract", "cont", JoinType.INNER_JOIN);
		
		restrictions.addCriterion(Restrictions.eq(PAID, Boolean.FALSE));
		restrictions.addCriterion(Restrictions.eq(CANCEL, Boolean.FALSE));
		
		if (StringUtils.isNotEmpty(txtReference.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("cont."+ REFERENCE, txtReference.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxTreasuryType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(TREASURY_TYPE, cbxTreasuryType.getSelectedEntity()));
		}
		
		if (cbxType.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(CASHFLOW_TYPE, cbxType.getSelectedEntity()));
		}
		
		if (cbxPaymentMethod.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(PAYMENT_METHOD +"."+ ID, cbxPaymentMethod.getSelectedEntity().getId()));
		}
		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(INSTALLMENT_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(INSTALLMENT_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		restrictions.addOrder(Order.asc(INSTALLMENT_DATE));		
		return restrictions;
	}

	/**
	 * @return pagedTable
	 */
	public SimplePagedTable<Cashflow> getPageTable() {
		return pagedTable;
	}
	
	/**
	 * @return selectedItemIds
	 */
	public Set<Long> getSelectedItemIds() {
		return selectedItemIds;
	}

	/**
	 * Refresh Cash flow table
	 */
	public void refreshCashflowTable() {
		List<Cashflow> cashflows = cashflowService.getListCashflow(getRestrictions());
        setIndexedContainer(cashflows);
        //pagedTable.setVisibleColumns(new Object[] {CONTRACT, TREASURY_TYPE, CASHFLOW_TYPE, PAYMENT_METHOD, NUM_INSTALLMENT, INSTALLMENT_DATE, TI_INSTALLMENT_USD, ID});
        selectedItemIds.clear();
        totalAmount = Double.valueOf(0);
        installment = Double.valueOf(0);
        installmentOther = Double.valueOf(0);
	}
}
