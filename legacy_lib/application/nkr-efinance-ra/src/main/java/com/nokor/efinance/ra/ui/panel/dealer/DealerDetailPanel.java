package com.nokor.efinance.ra.ui.panel.dealer;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.dealer.model.DealerGroup;
import com.nokor.efinance.core.dealer.model.EDealerCategory;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.dealer.model.LadderType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.DealerGroupComboBox;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.ra.ui.panel.dealer.contact.DealerAddressContactTab;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author youhort.ly
 *
 */

public class DealerDetailPanel extends AbstractTabPanel implements SaveClickListener, FinServicesHelper {

	/** 
	 */
	private static final long serialVersionUID = -8471453747731682046L;

	private Dealer dealer;

	private ERefDataComboBox<EDealerType> cbxDealerType;
	private ERefDataComboBox<EDealerCategory> cbxDealerCategory;
	private CheckBox cbActive;
	// private TextField txtName;
	private TextField txtNameEn;
	private TextField txtIntCode;
    private TextField txtHomePage; 
    private DealerComboBox cbxDealer;
    private AutoDateField dfOpeningDate;
    private TextField txtLicenceNo;
    private TextField txtVatNo;
    private TextField txtDescription; 
    private DealerGroupComboBox cbxDealerGroup;
    private EntityComboBox<LadderType> cbxLadderType;    
    private EntityComboBox<Organization> cbxOrganization;
    private EntityComboBox<OrgStructure> cbxBranch;   
    private DealerFormPanel delegate;   	
    private DealerAddressContactTab tabSheet;
   
    private AutoDateField dfRegistrationDate;
    private TextField txtMonthlyTargetSales;
    private TextField txtRegistrationPlace;
    private TextField txtRegistrationCost;
    private TextField txtBranchNo;
    
    /**
     * @param delegate
     */
	public DealerDetailPanel(DealerFormPanel delegate) {
        super();
        this.delegate = delegate;
        NavigationPanel navigationPanel = new NavigationPanel();
   		navigationPanel.addSaveClickListener(this);
   		addComponent(navigationPanel, 0);
	}
    
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	private Dealer getEntity() {
		dealer.setDealerType(cbxDealerType.getSelectedEntity());
		dealer.setTypeOrganization(ETypeOrganization.MAIN);
		dealer.setName(txtNameEn.getValue());
		dealer.setNameEn(txtNameEn.getValue());
		dealer.setCode(txtIntCode.getValue());
		dealer.setWebsite(txtHomePage.getValue());
		dealer.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		dealer.setForcedHistory(true);
		dealer.setParent(cbxDealer.getSelectedEntity());
		dealer.setStartDate(dfOpeningDate.getValue());
		dealer.setDescEn(txtDescription.getValue());
		dealer.setDealerGroup(cbxDealerGroup.getSelectedEntity());
		dealer.setLadderType(cbxLadderType.getSelectedEntity());
		dealer.setFinancialCompanyBranch(cbxBranch.getSelectedEntity());
		dealer.setDealerCategory(cbxDealerCategory.getSelectedEntity());
		dealer.setVatRegistrationNo(txtVatNo.getValue());
		dealer.setMonthlyTargetSales(getInteger(txtMonthlyTargetSales));
		dealer.setRegistrationDate(dfRegistrationDate.getValue());
		dealer.setRegistrationPlace(txtRegistrationPlace.getValue());
		dealer.setRegistrationCost(getDouble(txtRegistrationCost));
		dealer.setBranchNo(txtBranchNo.getValue());
		dealer.setLicenceNo(txtLicenceNo.getValue());
		
//		List<DealerAddress> dealerAddresses = dealer.getDealerAddresses();
//		if (dealerAddresses == null) {
//			dealerAddresses = new ArrayList<>();
//		}
//
//		dealer.setDealerAddresses(dealerAddresses);
		return dealer;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		cbxDealerCategory = new ERefDataComboBox<EDealerCategory>(EDealerCategory.values());
		cbxDealerCategory.setWidth(200, Unit.PIXELS);
		
		cbxDealerType = new ERefDataComboBox<>(EDealerType.values());
		cbxDealerType.setSelectedEntity(EDealerType.HEAD);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth(200, Unit.PIXELS);
		
		txtNameEn = ComponentFactory.getTextField(false, 60, 200);		
		txtIntCode = ComponentFactory.getTextField(false, 60, 200);		
		txtIntCode.setEnabled(false);
		txtHomePage = ComponentFactory.getTextField(false, 60, 200);
		
		cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
          
        cbxDealer = new DealerComboBox(getListDealer());
        cbxDealer.setWidth(200, Unit.PIXELS);
        cbxDealer.setSelectedEntity(null);
        cbxDealer.setEnabled(false);
        
        dfOpeningDate = ComponentFactory.getAutoDateField();
        txtLicenceNo = ComponentFactory.getTextField(false, 60, 200);
        txtDescription = ComponentFactory.getTextField(false, 200, 200);
        txtVatNo = ComponentFactory.getTextField(100, 200);
        
        cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/**
			 */
			private static final long serialVersionUID = -1761199571379718456L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDealerType.getSelectedEntity() != null) {
					if (cbxDealerType.getSelectedEntity().equals(EDealerType.BRANCH)) {
						cbxDealer.setEnabled(true);
					} else {
						cbxDealer.setEnabled(false);
						cbxDealer.setSelectedEntity(null);
					}
				}
			}
		});
        
        cbxDealerGroup = new DealerGroupComboBox(null, ENTITY_SRV.list(new BaseRestrictions<>(DealerGroup.class)), I18N.message("none"));
        cbxDealerGroup.setWidth(200, Unit.PIXELS);
        
        cbxLadderType = new EntityComboBox<>(LadderType.class, LadderType.DESCEN);
        cbxLadderType.renderer();
        cbxLadderType.setWidth(200, Unit.PIXELS);
        
        cbxOrganization = new EntityComboBox<>(Organization.class, "nameEn");
        cbxOrganization.setImmediate(true);
        cbxOrganization.renderer();
        cbxOrganization.setWidth(200, Unit.PIXELS);
        cbxOrganization.setSelectedEntity(Organization.getMainOrganization());
        
        cbxOrganization.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -419912123440388808L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Organization organization = cbxOrganization.getSelectedEntity();
				if (organization != null) {
					cbxBranch.setEntities(organization.getBranches());
					cbxBranch.setEnabled(true);
				} else {
					cbxBranch.setEnabled(false);
					cbxBranch.setSelectedEntity(null);
				}
				
			}
		});
        
        cbxBranch = new EntityComboBox<>(OrgStructure.class, "nameEn");
        cbxBranch.setImmediate(true);
        cbxBranch.renderer();
        cbxBranch.setEnabled(true);
        cbxBranch.setWidth(200, Unit.PIXELS);
        
        dfRegistrationDate = ComponentFactory.getAutoDateField();
        txtRegistrationPlace = ComponentFactory.getTextField(false, 60, 200);
        txtRegistrationCost = ComponentFactory.getTextField(false, 60, 200);
        txtMonthlyTargetSales = ComponentFactory.getTextField(false, 60, 200);
        txtBranchNo = ComponentFactory.getTextField(false, 4, 200);
        
		CustomLayout customLayout = initCustomLayout("/VAADIN/themes/efinance/layouts/", "dealer.html");
		customLayout.addComponent(new Label(I18N.message("dealer.type")), "lblDealerType");
        customLayout.addComponent(cbxDealerType, "cbxDealerType");
        customLayout.addComponent(new Label(I18N.message("main.dealer")), "lblDealer");
        customLayout.addComponent(cbxDealer, "cbxDealer");
        customLayout.addComponent(new Label(I18N.message("name")), "lblName");
        customLayout.addComponent(txtNameEn, "txtNameEn");
        customLayout.addComponent(new Label(I18N.message("dealershop.id")), "lblDealershopID");
        customLayout.addComponent(txtIntCode, "txtIntCode");
        customLayout.addComponent(new Label(I18N.message("homepage")), "lblHomepage");
        customLayout.addComponent(txtHomePage, "txtHomePage");
        customLayout.addComponent(new Label(I18N.message("opening.date")), "lblOpeningDate");
        customLayout.addComponent(dfOpeningDate, "dfOpeningDate");
        customLayout.addComponent(new Label(I18N.message("description")), "lblDescription");
        customLayout.addComponent(txtDescription, "txtDescription");
        customLayout.addComponent(new Label(I18N.message("dealer.group")), "lblDealerGroup");
        customLayout.addComponent(cbxDealerGroup, "cbxDealerGroup");
        customLayout.addComponent(new Label(I18N.message("ladder")), "lblLadderType");
        customLayout.addComponent(cbxLadderType, "cbxLadderType");
        customLayout.addComponent(new Label(I18N.message("dealer.category")), "lblDealerCategory");
        customLayout.addComponent(cbxDealerCategory, "cbxDealerCategory");
        
        customLayout.addComponent(new Label(I18N.message("organization")), "lblOrganization");
        customLayout.addComponent(cbxOrganization, "cbxOrganization");
        customLayout.addComponent(new Label(I18N.message("branch")), "lblBranch");
        customLayout.addComponent(cbxBranch, "cbxBranch");
        customLayout.addComponent(cbActive, "cbActive");
        
        customLayout.addComponent(ComponentFactory.getLabel("monthly.target.sales"), "lbleMonthlyTargetSales");
        customLayout.addComponent(txtMonthlyTargetSales, "txtMonthlyTargetSales");
        customLayout.addComponent(ComponentFactory.getLabel(I18N.message("branch.no")), "lblBranchNo");
        customLayout.addComponent(txtBranchNo, "txtBranchNo");
        
        tabSheet = new DealerAddressContactTab();
        
        FieldSet infoFieldSet = new FieldSet();
        infoFieldSet.setLegend(I18N.message("info"));
        infoFieldSet.setContent(customLayout);
        
        Panel infoPanel = new Panel(infoFieldSet);
        infoPanel.setStyleName(Reindeer.PANEL_LIGHT);
        
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.addComponent(infoPanel);
        content.addComponent(registrationDetailPanel());
        content.addComponent(tabSheet);

		return content;
	}

	/**
	 * 
	 * @return
	 */
	private Panel registrationDetailPanel() {
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		
		Label lblRegDate = ComponentFactory.getLabel("registration.date");
		Label lblRegCost = ComponentFactory.getLabel(I18N.message("registration.cost"));
		Label lblRegPlace = ComponentFactory.getLabel("registration.place");
		Label lblLicenceNo = ComponentFactory.getLabel("licence.no");
		Label lblVatNo = ComponentFactory.getLabel("vat.no");
		
		GridLayout gridLayout = new GridLayout(6, 2);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblRegDate);
		gridLayout.addComponent(dfRegistrationDate);
		gridLayout.addComponent(lblRegCost);
		gridLayout.addComponent(txtRegistrationCost);
		gridLayout.addComponent(lblRegPlace);
		gridLayout.addComponent(txtRegistrationPlace);
		gridLayout.addComponent(lblLicenceNo);
		gridLayout.addComponent(txtLicenceNo);
		gridLayout.addComponent(lblVatNo);
		gridLayout.addComponent(txtVatNo);
		
		gridLayout.setComponentAlignment(lblRegDate, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblRegCost, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblRegPlace, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblLicenceNo, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblVatNo, Alignment.MIDDLE_LEFT);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("registration.details"));
		fieldSet.setContent(gridLayout);
		
		panel.setContent(fieldSet);
		
		return panel;
	}
	
	/**
	 * 
	 * @param delerId
	 */
	public void assignValues(Dealer dealer) {
		super.removeMessagePanel();
		reset();
		tabSheet.assignValues(dealer);
		if (dealer != null) {
			this.dealer = dealer;
			cbxDealer.setSelectedEntity(dealer.getParent());
			txtNameEn.setValue(dealer.getNameEn());
//			txtIntCode.setEnabled(false);
			txtIntCode.setValue(dealer.getCode());
			txtHomePage.setValue(dealer.getWebsite());
			cbActive.setValue(dealer.getStatusRecord().equals(EStatusRecord.ACTIV));
			cbxDealer.setSelectedEntity(dealer.getParent());
			dfOpeningDate.setValue(dealer.getStartDate());
			txtLicenceNo.setValue(getDefaultString(dealer.getLicenceNo()));
			txtDescription.setValue(getDefaultString(dealer.getDescEn()));
			cbxDealerGroup.setSelectedEntity(dealer.getDealerGroup());
			cbxLadderType.setSelectedEntity(dealer.getLadderType());
			cbxDealerType.setSelectedEntity(dealer.getDealerType());
			cbxDealerCategory.setSelectedEntity(dealer.getDealerCategory());
			txtVatNo.setValue(getDefaultString(dealer.getVatRegistrationNo()));
			txtMonthlyTargetSales.setValue(getDefaultString(dealer.getMonthlyTargetSales()));
			dfRegistrationDate.setValue(dealer.getRegistrationDate());
			txtRegistrationPlace.setValue(dealer.getRegistrationPlace());
			txtRegistrationCost.setValue(getDefaultString(dealer.getRegistrationCost()));
			
			if (dealer.getFinancialCompanyBranch() != null) {
				cbxOrganization.setSelectedEntity(dealer.getFinancialCompanyBranch().getOrganization());
			}
			
			cbxBranch.setEnabled(dealer.getFinancialCompanyBranch() != null);
			cbxBranch.setSelectedEntity(dealer.getFinancialCompanyBranch());
			txtBranchNo.setValue(dealer.getBranchNo());
		} 
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		removeErrorsPanel();
		dealer = Dealer.createInstance();
		List<DealerEmployee> dealerEmployees = dealer.getDealerEmployees();
		dealerEmployees = new ArrayList<>();
		dealer.setDealerEmployees(dealerEmployees);
		
		txtNameEn.setValue("");
		txtIntCode.setValue("");
		txtHomePage.setValue("");
		cbActive.setValue(true);
//		txtIntCode.setEnabled(true);
		cbxDealerType.setSelectedEntity(EDealerType.HEAD);
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setEnabled(false);
		dfOpeningDate.setValue(null);
		txtLicenceNo.setValue("");
		txtDescription.setValue("");
		cbxOrganization.setSelectedEntity(Organization.getMainOrganization());
		cbxBranch.setSelectedEntity(null);
		cbxDealerGroup.setSelectedEntity(null);
		cbxLadderType.setSelectedEntity(null);
		cbxDealerCategory.setSelectedEntity(null);
		txtVatNo.setValue("");
		txtRegistrationPlace.setValue("");
		txtMonthlyTargetSales.setValue("");
		dfRegistrationDate.setValue(null);
		txtRegistrationCost.setValue("");
		txtBranchNo.setValue("");
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	private boolean validate() {
		errors.clear();
		checkMandatorySelectField(cbxDealerGroup, "dealer.group");
		checkMandatorySelectField(cbxOrganization, "organization");
		checkMandatorySelectField(cbxDealerType, "dealer.type");
		if (EDealerType.BRANCH.equals(cbxDealerType.getSelectedEntity())) {
			checkMandatorySelectField(cbxDealer, "dealer");
		}
		checkMandatoryField(txtNameEn, "name");
		return errors.isEmpty();
	}

	/**
	 * get dealer to combo box
	 * @return
	 */
	public List<Dealer> getListDealer() {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		restrictions.setMaxResults(100);
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent event) {
		if (validate()) {
			dealer = DEA_SRV.createDealer(getEntity());
			delegate.assignValues(dealer.getId());
			displaySuccess();
			tabSheet.assignValues(dealer);
		} else {
			displayErrorsPanel();
		}
	}
}
