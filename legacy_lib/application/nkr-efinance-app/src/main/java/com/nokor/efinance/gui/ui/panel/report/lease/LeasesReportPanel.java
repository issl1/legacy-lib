package com.nokor.efinance.gui.ui.panel.report.lease;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.accounting.LeasesReport;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.glf.accounting.service.GLFLeasingAccountingService;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author nora.ky
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(LeasesReportPanel.NAME)
public class LeasesReportPanel extends AbstractTabPanel implements View, ContractEntityField, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4754689714473699494L;

	public static final String NAME = "leases.information";
	
	@Autowired
    private GLFLeasingAccountingService accountingService;
			
	private TabSheet tabSheet;
	
	private SimplePagedTable<LeasesReport> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private DealerComboBox cbxDealer;
	private TextField txtReference;
	private TextField txtFamilyName;
	private TextField txtFirstName;
	private AutoDateField dfCalculDate;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public LeasesReportPanel() {
		super();
		setSizeFull();
	}
	
	@SuppressWarnings("serial")
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout gridLayoutPanel = new VerticalLayout();
		VerticalLayout searchLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null); // null it means we don't modify key of shortcut Enter(default = 13)
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
		
		final GridLayout gridLayout = new GridLayout(11, 3);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		cbxDealerType = new ERefDataComboBox<EDealerType>(EDealerType.class);
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -6774641791917653706L;
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
		});
		
		txtFamilyName = ComponentFactory.getTextField(false, 60, 200);	
		txtFirstName = ComponentFactory.getTextField(false, 60, 200);
		
		txtReference = ComponentFactory.getTextField(false, 20, 150);
		dfCalculDate = ComponentFactory.getAutoDateField("",false);
		dfCalculDate.setValue(DateUtils.today());
		dfStartDate = ComponentFactory.getAutoDateField("",false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("",false);
		dfEndDate.setValue(DateUtils.today());
	
        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("contract.reference")), iCol++, 0);
        gridLayout.addComponent(txtReference, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer.type")), iCol++, 0);
        gridLayout.addComponent(cbxDealerType, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("lastname.en")), iCol++, 1);
        gridLayout.addComponent(txtFamilyName, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("firstname.en")), iCol++, 1);
        gridLayout.addComponent(txtFirstName, iCol++, 1);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
        gridLayout.addComponent(new Label(I18N.message("calcul.date")), iCol++, 1);
        gridLayout.addComponent(dfCalculDate, iCol++, 1);
        
        iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 2);
        gridLayout.addComponent(dfStartDate, iCol++, 2);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 2);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 2);
        gridLayout.addComponent(dfEndDate, iCol++, 2);
        
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<LeasesReport>(this.columnDefinitions);
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("leases.information"));
        
        return tabSheet;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	private BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("contractApplicants", "contractapp", JoinType.INNER_JOIN);
		restrictions.addAssociation("contractapp.applicant", "app", JoinType.INNER_JOIN);
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		
		restrictions.addCriterion("contractapp.applicantType", EApplicantType.C);
		restrictions.addCriterion(Restrictions.ne(CONTRACT_STATUS, ContractWkfStatus.PEN));
		
		if (StringUtils.isNotEmpty(txtReference.getValue())) { 
			restrictions.addCriterion(Restrictions.like(REFERENCE, txtReference.getValue(), MatchMode.ANYWHERE));
		}
		
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (cbxDealer.getSelectedEntity() != null) { 
			restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, cbxDealer.getSelectedEntity().getId()));
		}
		
		if (StringUtils.isNotEmpty(txtFamilyName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + LAST_NAME_EN, txtFamilyName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotEmpty(txtFirstName.getValue())) {
			restrictions.addCriterion(Restrictions.ilike("app." + FIRST_NAME_EN, txtFirstName.getValue(), MatchMode.ANYWHERE));
		}
		
		if (dfStartDate.getValue() != null) {       
			restrictions.addCriterion(Restrictions.ge(START_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le(START_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
				
		restrictions.addOrder(Order.desc(START_DATE));
		return restrictions;
	}

	public void reset() {
		cbxDealerType.setSelectedEntity(null);
		cbxDealer.setSelectedEntity(null);
		txtReference.setValue("");
		txtFamilyName.setValue("");
		txtFirstName.setValue("");
		dfCalculDate.setValue(DateUtils.today());
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	private void search() {
		List<LeasesReport> leasesReports = accountingService.getLeaseReports(dfCalculDate.getValue(), getRestrictions());
		setIndexedContainer(leasesReports);

	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<LeasesReport> leases) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (LeasesReport lease : leases) {
			Item item = indexedContainer.addItem(lease.getId());
			item.getItemProperty("production.officer").setValue(lease.getPoNo());
			item.getItemProperty(REFERENCE).setValue(lease.getLidNo());
			item.getItemProperty(CONTRACT_STATUS).setValue(lease.getWkfStatus().getDesc());
			item.getItemProperty("po.number").setValue(lease.getPoNumber());
			item.getItemProperty(FULL_NAME).setValue(lease.getFullName());
			item.getItemProperty("business.type").setValue(lease.getBusinessType());
			item.getItemProperty("businessIndustry").setValue(lease.getBusinessIndustry());
			item.getItemProperty(MOBILEPHONE).setValue(lease.getTel());
			item.getItemProperty(HOUSE_NO).setValue(lease.getHouseNo());
			item.getItemProperty(STREET).setValue(lease.getStreet());
			item.getItemProperty(VILLAGE).setValue(lease.getVillage());
			item.getItemProperty(VILLAGE_KH).setValue(lease.getVillageKh());
			item.getItemProperty(COMMUNE).setValue(lease.getCommune());
			item.getItemProperty(COMMUNE_KH).setValue(lease.getCommuneKh());
			item.getItemProperty(DISTRICT).setValue(lease.getDistrict());
			item.getItemProperty(DISTRICT_KH).setValue(lease.getDistrictKh());
			item.getItemProperty(PROVINCE).setValue(lease.getProvince());
			item.getItemProperty(PROVINCE_KH).setValue(lease.getProvinceKh());
			item.getItemProperty("sex").setValue(lease.getSex());
			item.getItemProperty("dealer" + NAME_EN).setValue(lease.getDealerName());
			item.getItemProperty(ASSET_MODEL).setValue(lease.getAssetModel());
			item.getItemProperty("credit.officer").setValue(lease.getCoName());
			item.getItemProperty(START_DATE).setValue(lease.getDateOfContract());
			item.getItemProperty("asset.price").setValue(AmountUtils.convertToAmount(lease.getAssetPrice()));
			item.getItemProperty("term").setValue(lease.getTerm());
			item.getItemProperty("rate").setValue(AmountUtils.convertToAmount(lease.getRate()));
			item.getItemProperty("first.installment.date").setValue(lease.getFirstInstallmentDate());
			item.getItemProperty("irr.year").setValue(AmountUtils.convertToAmount(lease.getIrrYear()));
			item.getItemProperty("irr.month").setValue(AmountUtils.convertToAmount(lease.getIrrMonth()));
			item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.convertToAmount(lease.getAdvPaymentPer()));
			item.getItemProperty("installment.amount").setValue(AmountUtils.convertToAmount(lease.getInstallmentAmount()));
			item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(lease.getInsurance()));
			item.getItemProperty("registration.fee").setValue(AmountUtils.convertToAmount(lease.getRegistration()));
			item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(lease.getServiceFee()));
			item.getItemProperty("advance.payment").setValue(AmountUtils.convertToAmount(lease.getAdvPayment()));
			item.getItemProperty("second.payment").setValue(AmountUtils.convertToAmount(lease.getSecondPay()));
			item.getItemProperty("down.payment").setValue(AmountUtils.convertToAmount(lease.getDownPay()));
			item.getItemProperty("loan.amount").setValue(AmountUtils.convertToAmount(lease.getLoanAmount()));
			item.getItemProperty("nbOverdueInDays").setValue(lease.getNbOverdueInDays());
			item.getItemProperty("total.interest").setValue(AmountUtils.convertToAmount(lease.getTotalInt()));
			item.getItemProperty("interest.balance").setValue(AmountUtils.convertToAmount(lease.getIntBalance()));
			item.getItemProperty("realIntBalance").setValue(AmountUtils.convertToAmount(lease.getRealIntBalance()));
			item.getItemProperty("principal.balance").setValue(AmountUtils.convertToAmount(lease.getPrinBalance()));
			item.getItemProperty("realPrinBalance").setValue(AmountUtils.convertToAmount(lease.getRealPrinBalance()));
			item.getItemProperty("total.receivable").setValue(AmountUtils.convertToAmount(lease.getTotalReceive()));
			item.getItemProperty("realTotalReceive").setValue(AmountUtils.convertToAmount(lease.getRealTotalReceive()));
		}
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("production.officer", I18N.message("production.officer"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("contract.reference"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(CONTRACT_STATUS, I18N.message("contract.status"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("po.number", I18N.message("po.number"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FULL_NAME, I18N.message("customer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("business.type", I18N.message("business.type"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("businessIndustry", I18N.message("business.industry"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(MOBILEPHONE, I18N.message("telephone"), String.class, Align.LEFT, 80));
		
		columnDefinitions.add(new ColumnDefinition(HOUSE_NO, I18N.message("house.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(STREET, I18N.message("street"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(VILLAGE, I18N.message("village"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(VILLAGE_KH, I18N.message("village.kh"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(COMMUNE, I18N.message("commune"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(COMMUNE_KH, I18N.message("commune.kh"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DISTRICT, I18N.message("district"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DISTRICT_KH, I18N.message("district.kh"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PROVINCE, I18N.message("province"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(PROVINCE_KH, I18N.message("province.kh"), String.class, Align.LEFT, 100));
		
		columnDefinitions.add(new ColumnDefinition("sex", I18N.message("gender"), String.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition("dealer" + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(ASSET_MODEL, I18N.message("asset.model"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition("credit.officer", I18N.message("credit.officer"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("asset.price", I18N.message("asset.price"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("term", I18N.message("term"), Integer.class, Align.LEFT, 60));
		columnDefinitions.add(new ColumnDefinition("rate", I18N.message("rate"), Amount.class, Align.RIGHT, 60));
		columnDefinitions.add(new ColumnDefinition("first.installment.date", I18N.message("first.installment.date"), Date.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("irr.year", I18N.message("irryear"), Amount.class, Align.RIGHT, 60));
		columnDefinitions.add(new ColumnDefinition("irr.month", I18N.message("irrmonth"), Amount.class, Align.RIGHT, 60));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("installment.amount", I18N.message("installment.amount"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("registration.fee", I18N.message("registration.fee"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("advance.payment", I18N.message("advance.payment"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("second.payment", I18N.message("second.payment"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("down.payment", I18N.message("down.payment"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("loan.amount", I18N.message("loan.amount"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("nbOverdueInDays", I18N.message("nb.overdue.in.days"), Integer.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("total.interest", I18N.message("interest.total"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("interest.balance", I18N.message("interest.balance"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("realIntBalance", I18N.message("real.interest.balance"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("principal.balance", I18N.message("principal.balance"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("realPrinBalance", I18N.message("real.principal.balance"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("total.receivable", I18N.message("total.receivable"), Amount.class, Align.RIGHT, 80));
		columnDefinitions.add(new ColumnDefinition("realTotalReceive", I18N.message("real.total.receivable"), Amount.class, Align.RIGHT, 80));
		
		return columnDefinitions;
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
