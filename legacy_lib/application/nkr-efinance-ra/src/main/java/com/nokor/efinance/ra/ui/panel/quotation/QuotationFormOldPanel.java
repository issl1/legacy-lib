package com.nokor.efinance.ra.ui.panel.quotation;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.address.panel.AddressPanel;
import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.EAssetGender;
import com.nokor.efinance.core.asset.model.EEngine;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.service.ContractService;
import com.nokor.efinance.core.quotation.QuotationService;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.applicant.AddressUtils;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.ersys.core.hr.model.eref.EColor;
import com.nokor.ersys.core.hr.model.eref.EEmploymentStatus;
import com.nokor.ersys.core.hr.model.eref.ESeniorityLevel;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Quotation_2 form panel for administration profile
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QuotationFormOldPanel extends AbstractFormPanel implements FMEntityField {
	
	private static final long serialVersionUID = -197408723612709745L;
	
	@Autowired
	private QuotationService quotationService;
	@Autowired
	private ContractService contractService;
    
	private DealerComboBox cbxDealer;
	private SecUserComboBox cbxCreditOfficer;
	private SecUserComboBox cbxProductionOfficer;
	private ERefDataComboBox<ESeniorityLevel> cbxSeniorityLevel;
	private ERefDataComboBox<ESeniorityLevel> cbxSeniorityLevelGuarantor;
	private AutoDateField dfContractStartDate;
    private AutoDateField dfFirstPaymentDate;
	private TextField txtFirstNameEn;
	private TextField txtFirstName;
	private TextField txtLastNameEn;
	private TextField txtLastName;
	private AutoDateField dfDateOfBirth;
	private TextField txtChiefVillageName;
	private TextField txtChiefVillagePhoneNumber;
	private TextField txtDeputyChiefVillageName;
	private TextField txtDeputyChiefVillagePhoneNumber;
	private TextField txtTotalNumberFamilyMember;
	private TextField txtMobilePhone1;
	private TextField txtMobilePhone2;
	
	private AddressPanel addressFormPanel;
	
	private TextField txtGuarantorFirstNameEn;
	private TextField txtGuarantorFirstName;
	private TextField txtGuarantorLastNameEn;
	private TextField txtGuarantorLastName;
	private AutoDateField dfGuarantorDateOfBirth;
	private TextField txtGuarantorMobilePhone1;
	private TextField txtGuarantorMobilePhone2;
	private AddressPanel guarantorAddressFormPanel;
	
	private ERefDataComboBox<EColor> cbxColor;
	private ERefDataComboBox<EEngine> cbxEngine; 
	private ERefDataComboBox<EAssetGender> cbxAssetGender;
	private TextField txtChassisNumber;
	private TextField txtEngineNumber;
	private TextField txtAssetYear;
	
	private Quotation quotation;
    private TabSheet tabSheet;
    private VerticalLayout dealerVerticalLayout;   
    private VerticalLayout applicantLayout;
    private VerticalLayout applicantAddressLayout;
    private VerticalLayout guarantorLayout;
    private VerticalLayout guarantorAddressLayout;
    private VerticalLayout assetLayout;
    
    private QuotationsOldPanel quotationsPanel;
    
    /** */
	@PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("quotation"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	/**
	 * @return
	 */
    public QuotationsOldPanel getQuotationsPanel() {
		return quotationsPanel;
	}

    /**
     * @param quotationsPanel
     */
	public void setQuotationsPanel(QuotationsOldPanel quotationsPanel) {
		this.quotationsPanel = quotationsPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
    @Override
	protected Quotation getEntity() {
    	quotation.setDealer(cbxDealer.getSelectedEntity());
		quotation.setCreditOfficer(cbxCreditOfficer.getSelectedEntity());
		quotation.setProductionOfficer(cbxProductionOfficer.getSelectedEntity());
		quotation.setContractStartDate(dfContractStartDate.getValue());
		quotation.setFirstDueDate(dfFirstPaymentDate.getValue());
		return quotation;
	}
    
    /**
     * @return
     */
    private Applicant getApplicant() {
    	Applicant applicant = quotation.getApplicant();
    	Individual individual = applicant.getIndividual();
    	individual.setFirstNameEn(txtFirstNameEn.getValue());
    	individual.setFirstName(txtFirstName.getValue());
    	individual.setLastNameEn(txtLastNameEn.getValue());
    	individual.setLastName(txtLastName.getValue());
    	individual.setBirthDate(dfDateOfBirth.getValue());
    	individual.setTotalFamilyMember(getInteger(txtTotalNumberFamilyMember));
		return applicant;
 	}
    
    /**
     * @return
     */
    private Applicant getBOApplicant() {
		if (quotation.getContract().getId() != null) {
			Contract contract = ENTITY_SRV.getById(Contract.class, quotation.getContract().getId());
			Applicant boAapplicant = contract.getApplicant();
			Individual individual = boAapplicant.getIndividual();
			individual.setFirstNameEn(txtFirstNameEn.getValue());
			individual.setFirstName(txtFirstName.getValue());
			individual.setLastNameEn(txtLastNameEn.getValue());
			individual.setLastName(txtLastName.getValue());
			individual.setBirthDate(dfDateOfBirth.getValue());
			return boAapplicant;
		}
		return null;
	}

    /**
     * @return
     */
    private Employment getApplicantEmployment() {
    	Employment employment = quotation.getApplicant().getIndividual().getCurrentEmployment();
    	if (employment != null) {
    		employment.setSeniorityLevel(cbxSeniorityLevel.getSelectedEntity());
    	}
    	return employment;
    }
    
    /**
     * @return
     */
    private Employment getGuarantorEmployment() {
    	Employment employment = quotation.getGuarantor().getIndividual().getCurrentEmployment();
    	if (employment != null) {
    		employment.setSeniorityLevel(cbxSeniorityLevelGuarantor.getSelectedEntity());
    	}
    	return employment;
    }
    
    /**
     * @return
     */
    private Applicant getGuarantor() {
    	Applicant guarantor = quotation.getGuarantor();
    	if (guarantor != null) {
    		guarantor.getIndividual().setFirstNameEn(txtGuarantorFirstNameEn.getValue());
    		guarantor.getIndividual().setFirstName(txtGuarantorFirstName.getValue());
    		guarantor.getIndividual().setLastNameEn(txtGuarantorLastNameEn.getValue());
    		guarantor.getIndividual().setLastName(txtGuarantorLastName.getValue());
    		guarantor.getIndividual().setBirthDate(dfGuarantorDateOfBirth.getValue());
    	}
		return guarantor;
 	}
    
    /**
     * @return
     */
    private Applicant getBOGuarantor() {
    	if (quotation.getContract().getId() != null) {
			Contract contract = ENTITY_SRV.getById(Contract.class, quotation.getContract().getId());
			Applicant guarantor = contract.getGuarantor();
	    	if (guarantor != null) {
	    		guarantor.getIndividual().setFirstNameEn(txtGuarantorFirstNameEn.getValue());
	    		guarantor.getIndividual().setFirstName(txtGuarantorFirstName.getValue());
	    		guarantor.getIndividual().setLastNameEn(txtGuarantorLastNameEn.getValue());
	    		guarantor.getIndividual().setLastName(txtGuarantorLastName.getValue());
	    		guarantor.getIndividual().setBirthDate(dfGuarantorDateOfBirth.getValue());
	    		return guarantor;
	    	}
    	}
		return null;
 	}
    
    /**	
     * @return
     */
	private Asset getAsset() {
		Asset asset = quotation.getAsset();
		asset.setColor(cbxColor.getSelectedEntity());
		asset.setEngine(cbxEngine.getSelectedEntity());
		asset.setAssetGender(cbxAssetGender.getSelectedEntity());
		asset.setChassisNumber(txtChassisNumber.getValue());
		asset.setEngineNumber(txtEngineNumber.getValue());
		asset.setYear(getInteger(txtAssetYear));
		return asset;
	}

	/**
	 * @return
	 */
	private Asset getBoAsset() {
		if (quotation.getContract().getId() != null) {
			Contract contract = ENTITY_SRV.getById(Contract.class, quotation.getContract().getId());
			Asset boAsset = contract.getAsset();
			boAsset.setColor(cbxColor.getSelectedEntity());
			boAsset.setYear(getInteger(txtAssetYear));
			boAsset.setEngine(cbxEngine.getSelectedEntity());
			boAsset.setAssetGender(cbxAssetGender.getSelectedEntity());
			boAsset.setChassisNumber(txtChassisNumber.getValue());
			boAsset.setEngineNumber(txtEngineNumber.getValue());
			return boAsset;
		}
		return null;
	}
	
	/** */
	public void saveEntity() {

		if (tabSheet.getSelectedTab() == dealerVerticalLayout) {
			String startDate1 = DateUtils.getDateLabel(dfContractStartDate.getValue(), "ddMMyyyy");
			String startDate2 = DateUtils.getDateLabel(quotation.getContractStartDate(), "ddMMyyyy");
			if (quotation.getWkfStatus().equals(QuotationWkfStatus.ACT)
					&& !startDate1.equals(startDate2)) {	
				contractService.updateOfficialPaymentDate(quotation, dfContractStartDate.getValue());
			}
			
			String firstPaymentDate1 = DateUtils.getDateLabel(dfFirstPaymentDate.getValue(), "ddMMyyyy");
			String firstPaymentDate2 = DateUtils.getDateLabel(quotation.getFirstDueDate(), "ddMMyyyy");
			if (quotation.getWkfStatus().equals(QuotationWkfStatus.ACT)
					&& !firstPaymentDate1.equals(firstPaymentDate2)) {
				contractService.updateInstallmentDate(quotation.getContract().getId(), dfFirstPaymentDate.getValue());
			}
			ENTITY_SRV.saveOrUpdate(getEntity());
			
			if (quotation.getContract().getId() != null) {
				Contract contract = ENTITY_SRV.getById(Contract.class, quotation.getContract().getId());
				if (!contract.getDealer().getId().equals(quotation.getDealer().getId())) {
					contract.setDealer(cbxDealer.getSelectedEntity());
					ENTITY_SRV.saveOrUpdate(contract);
				}
			}
			
			
		} else if (tabSheet.getSelectedTab() == applicantLayout) {
			ENTITY_SRV.saveOrUpdate(getApplicant());
			ENTITY_SRV.saveOrUpdate(getApplicantEmployment());
			Applicant boApplicant = getBOApplicant();
			if (boApplicant != null) {
				ENTITY_SRV.saveOrUpdate(boApplicant);
			}
		} else if (tabSheet.getSelectedTab() == applicantAddressLayout) {
			ENTITY_SRV.saveOrUpdate(addressFormPanel.getAddress(quotation.getApplicant().getIndividual().getMainAddress()));
			Applicant boApplicant = getBOApplicant();
			if (boApplicant != null) {
				ENTITY_SRV.saveOrUpdate(AddressUtils.copy(quotation.getApplicant().getIndividual().getMainAddress(), boApplicant.getIndividual().getMainAddress()));
			}
		} else if (tabSheet.getSelectedTab() == guarantorLayout) {
			if (quotation.getGuarantor() != null) {
				ENTITY_SRV.saveOrUpdate(getGuarantor());
				ENTITY_SRV.saveOrUpdate(getGuarantorEmployment());
				Applicant boGuarantor = getBOGuarantor();
				if (boGuarantor != null) {
					ENTITY_SRV.saveOrUpdate(boGuarantor);
				}
			}
		} else if (tabSheet.getSelectedTab() == guarantorAddressLayout) {
			ENTITY_SRV.saveOrUpdate(guarantorAddressFormPanel.getAddress(quotation.getGuarantor().getIndividual().getMainAddress()));
			Applicant boGuarantor = getBOGuarantor();
			if (boGuarantor != null) {
				ENTITY_SRV.saveOrUpdate(AddressUtils.copy(quotation.getGuarantor().getIndividual().getMainAddress(), boGuarantor.getIndividual().getMainAddress()));
			}
		} else if (tabSheet.getSelectedTab() == assetLayout) {
			if (quotation.getAsset() != null) {
				ENTITY_SRV.saveOrUpdate(getAsset());
				if (getBoAsset() != null) {
					ENTITY_SRV.saveOrUpdate(getBoAsset());
				}
			}
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtFirstNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtFirstName = ComponentFactory.getTextField35(false, 60, 150); 
		txtLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtLastName = ComponentFactory.getTextField35(false, 60, 150);
		dfDateOfBirth = ComponentFactory.getAutoDateField();
		txtMobilePhone1 = ComponentFactory.getTextField(false, 30, 100);
		txtMobilePhone2 = ComponentFactory.getTextField(false, 30, 150);
		
		txtChiefVillageName = ComponentFactory.getTextField(false, 60, 150);
		txtChiefVillagePhoneNumber = ComponentFactory.getTextField(false, 60, 150);
		txtDeputyChiefVillageName = ComponentFactory.getTextField(false, 60, 150);
		txtDeputyChiefVillagePhoneNumber = ComponentFactory.getTextField(false, 60, 150);
		txtTotalNumberFamilyMember = ComponentFactory.getTextField(false, 50, 100);
		
		addressFormPanel = new AddressPanel(true, ETypeAddress.HOME, "addressHorizontal");
        addressFormPanel.setMargin(true);
		
		txtGuarantorFirstNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtGuarantorFirstName = ComponentFactory.getTextField35(false, 60, 150); 
		txtGuarantorLastNameEn = ComponentFactory.getTextField(false, 60, 150);
		txtGuarantorLastName = ComponentFactory.getTextField35(false, 60, 150);
		dfGuarantorDateOfBirth = ComponentFactory.getAutoDateField();
		txtGuarantorMobilePhone1 = ComponentFactory.getTextField(false, 30, 100);
		txtGuarantorMobilePhone2 = ComponentFactory.getTextField(false, 30, 150);
		
		guarantorAddressFormPanel = new AddressPanel(true, ETypeAddress.HOME, "addressHorizontal");
		guarantorAddressFormPanel.setMargin(true);
		
		cbxColor = new ERefDataComboBox<EColor>(EColor.class);
		txtAssetYear = ComponentFactory.getTextField(4, 50);
		cbxEngine = new ERefDataComboBox<EEngine>(EEngine.class);
		cbxAssetGender = new ERefDataComboBox<EAssetGender>(EAssetGender.class);
		
		txtChassisNumber = ComponentFactory.getTextField(false, 60, 150);
		txtEngineNumber = ComponentFactory.getTextField(false, 60, 150);
		
		final GridLayout gridLayout = new GridLayout(5, 3);
        gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		
		final GridLayout gridLayoutApplicant = new GridLayout(9, 9);
		gridLayoutApplicant.setMargin(true);
		
		final GridLayout gridLayoutGuarantor = new GridLayout(5, 9);
		
		final GridLayout gridLayoutAsset = new GridLayout(5, 4);
		
		cbxDealer = new DealerComboBox(DataReference.getInstance().getDealers());
		cbxDealer.setWidth("220px");
		cbxDealer.setImmediate(true);
		cbxDealer.setSelectedEntity(null);
		
		cbxCreditOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CO));
		cbxProductionOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.PO));
		cbxSeniorityLevel = new ERefDataComboBox<ESeniorityLevel>(ESeniorityLevel.class);
		
		cbxSeniorityLevelGuarantor = new ERefDataComboBox<ESeniorityLevel>(ESeniorityLevel.class);
		
		dfContractStartDate = ComponentFactory.getAutoDateField();
        dfFirstPaymentDate = ComponentFactory.getAutoDateField();
		
		//grid dealer
		int iCol = 0;
	    gridLayout.addComponent(new Label(I18N.message("credit.officer")), iCol++, 0);
	    gridLayout.addComponent(cbxCreditOfficer, iCol++, 0);
	    gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
	    gridLayout.addComponent(new Label(I18N.message("production.officer")), iCol++, 0);
	    gridLayout.addComponent(cbxProductionOfficer, iCol++, 0);
	    
	    iCol = 0;
	    gridLayout.addComponent(new Label(I18N.message("contract.start.date")), iCol++, 1);
	    gridLayout.addComponent(dfContractStartDate, iCol++, 1);
	    gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 1);
	    gridLayout.addComponent(new Label(I18N.message("first.payment.date")), iCol++, 1);
	    gridLayout.addComponent(dfFirstPaymentDate, iCol++, 1);
	    
	    iCol = 0;
	    gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 2);
	    gridLayout.addComponent(cbxDealer, iCol++, 2);
	    gridLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 2);
	    
	    dealerVerticalLayout = new VerticalLayout();
	    dealerVerticalLayout.addComponent(gridLayout);
	    dealerVerticalLayout.setSizeFull();
	    dealerVerticalLayout.setMargin(true);
	    dealerVerticalLayout.setCaption(I18N.message("dealer"));
	    
	    // grid applicant
	    iCol = 0;
	    gridLayoutApplicant.addComponent(new Label(I18N.message("seniorities.level")), iCol++, 0);
	    gridLayoutApplicant.addComponent(cbxSeniorityLevel, iCol++, 0);
	    gridLayoutApplicant.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
	    
	    iCol = 0;
	    gridLayoutApplicant.addComponent(ComponentFactory.getVerticalLayout(8), iCol++, 1);
	    
	    iCol = 0;
	    gridLayoutApplicant.addComponent(new Label(I18N.message("firstname.en")), iCol++, 2);
	    gridLayoutApplicant.addComponent(txtFirstNameEn, iCol++, 2);
	    gridLayoutApplicant.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 2);
	    gridLayoutApplicant.addComponent(new Label(I18N.message("firstname")), iCol++, 2);
	    gridLayoutApplicant.addComponent(txtFirstName, iCol++, 2);
	    
	    iCol = 0;
	    gridLayoutApplicant.addComponent(ComponentFactory.getVerticalLayout(8), iCol++, 3);
	    
	    iCol = 0;
	    gridLayoutApplicant.addComponent(new Label(I18N.message("lastname.en")), iCol++, 4);
	    gridLayoutApplicant.addComponent(txtLastNameEn, iCol++, 4);
	    gridLayoutApplicant.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 4);
	    gridLayoutApplicant.addComponent(new Label(I18N.message("lastname")), iCol++, 4);
	    gridLayoutApplicant.addComponent(txtLastName, iCol++, 4);
	    
	    iCol = 0;
	    gridLayoutApplicant.addComponent(new Label(I18N.message("dateofbirth")), iCol++, 5);
	    gridLayoutApplicant.addComponent(dfDateOfBirth, iCol++, 5);
	    gridLayoutApplicant.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 5);
	    gridLayoutApplicant.addComponent(new Label(I18N.message("total.family.member")), iCol++, 5);
	    gridLayoutApplicant.addComponent(txtTotalNumberFamilyMember, iCol++, 5);
	    
	    iCol = 0;
	    gridLayoutApplicant.addComponent(new Label(I18N.message("chief.village.name")), iCol++, 6);
	    gridLayoutApplicant.addComponent(txtChiefVillageName, iCol++, 6);
	    gridLayoutApplicant.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 6);
	    gridLayoutApplicant.addComponent(new Label(I18N.message("chief.village.phone.number")), iCol++, 6);
	    gridLayoutApplicant.addComponent(txtChiefVillagePhoneNumber, iCol++, 6);
	    
	    iCol = 0;
	    gridLayoutApplicant.addComponent(new Label(I18N.message("deputy.chief.village.name")), iCol++, 7);
	    gridLayoutApplicant.addComponent(txtDeputyChiefVillageName, iCol++, 7);
	    gridLayoutApplicant.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 7);
	    gridLayoutApplicant.addComponent(new Label(I18N.message("deputy.chief.village.phone.number")), iCol++, 7);
	    gridLayoutApplicant.addComponent(txtDeputyChiefVillagePhoneNumber, iCol++, 7);
	    
	    iCol = 0;
	    gridLayoutApplicant.addComponent(new Label(I18N.message("mobile.phone1")), iCol++, 8);
	    gridLayoutApplicant.addComponent(txtMobilePhone1, iCol++, 8);
	    gridLayoutApplicant.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 8);
	    gridLayoutApplicant.addComponent(new Label(I18N.message("mobile.phone2")), iCol++, 8);
	    gridLayoutApplicant.addComponent(txtMobilePhone2, iCol++, 8);
	    
	    //grid guarantor
	    iCol = 0;
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("seniorities.level")), iCol++, 0);
	    gridLayoutGuarantor.addComponent(cbxSeniorityLevelGuarantor, iCol++, 0);
	    gridLayoutGuarantor.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
	    
	    iCol = 0;
	    gridLayoutGuarantor.addComponent(ComponentFactory.getVerticalLayout(8), iCol++, 1);
	    
	    iCol = 0;
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("firstname.en")), iCol++, 2);
	    gridLayoutGuarantor.addComponent(txtGuarantorFirstNameEn, iCol++, 2);
	    gridLayoutGuarantor.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 2);
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("firstname")), iCol++, 2);
	    gridLayoutGuarantor.addComponent(txtGuarantorFirstName, iCol++, 2);
	    
	    iCol = 0;   
	    gridLayoutGuarantor.addComponent(ComponentFactory.getVerticalLayout(8), iCol++, 3);
	    
	    iCol = 0;
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("lastname.en")), iCol++, 4);
	    gridLayoutGuarantor.addComponent(txtGuarantorLastNameEn, iCol++, 4);
	    gridLayoutGuarantor.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 4);
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("lastname")), iCol++, 4);
	    gridLayoutGuarantor.addComponent(txtGuarantorLastName, iCol++, 4);
	    
	    iCol = 0;
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("dateofbirth")), iCol++, 5);
	    gridLayoutGuarantor.addComponent(dfGuarantorDateOfBirth, iCol++, 5);
	    gridLayoutGuarantor.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 5);
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("")), iCol++, 5);
	    
	    iCol = 0;
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("mobile.phone1")), iCol++, 6);
	    gridLayoutGuarantor.addComponent(txtGuarantorMobilePhone1, iCol++, 6);
	    gridLayoutGuarantor.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 6);
	    gridLayoutGuarantor.addComponent(new Label(I18N.message("mobile.phone2")), iCol++, 6);
	    gridLayoutGuarantor.addComponent(txtGuarantorMobilePhone2, iCol++, 6);
	    
	    // grid asset
		iCol = 0;
	    gridLayoutAsset.addComponent(new Label(I18N.message("color")), iCol++, 0);
	    gridLayoutAsset.addComponent(cbxColor, iCol++, 0);
	    gridLayoutAsset.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 0);
	    gridLayoutAsset.addComponent(new Label(I18N.message("engine")), iCol++, 0);
	    gridLayoutAsset.addComponent(cbxEngine, iCol++, 0);
	    
	    iCol = 0;
	    gridLayoutAsset.addComponent(new Label(I18N.message("year")), iCol++, 1);
	    gridLayoutAsset.addComponent(txtAssetYear, iCol++, 1);
	    gridLayoutAsset.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 1);
	    gridLayoutAsset.addComponent(new Label(I18N.message("type")), iCol++, 1);
	    gridLayoutAsset.addComponent(cbxAssetGender, iCol++, 1);
	    
	    iCol = 0;   
	    gridLayoutAsset.addComponent(ComponentFactory.getVerticalLayout(8), iCol++, 2);
	    
	    
	    iCol = 0;
	    gridLayoutAsset.addComponent(new Label(I18N.message("chassis.number")), iCol++, 3);
	    gridLayoutAsset.addComponent(txtChassisNumber, iCol++, 3);
	    gridLayoutAsset.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS), iCol++, 3);
	    gridLayoutAsset.addComponent(new Label(I18N.message("engine.number")), iCol++, 3);
	    gridLayoutAsset.addComponent(txtEngineNumber, iCol++, 3);
	    	    
	    applicantLayout = new VerticalLayout();
	    applicantLayout.setMargin(true);
	    applicantLayout.addComponent(gridLayoutApplicant);
	    
	    applicantAddressLayout = new VerticalLayout();
	    applicantAddressLayout.setMargin(true);
	    applicantAddressLayout.addComponent(addressFormPanel);
	    
	    guarantorLayout = new VerticalLayout();
	    guarantorLayout.setMargin(true);
	    guarantorLayout.addComponent(gridLayoutGuarantor);
	    
	    guarantorAddressLayout = new VerticalLayout();
	    guarantorAddressLayout.setMargin(true);
	    guarantorAddressLayout.addComponent(guarantorAddressFormPanel);
	    
	    assetLayout = new VerticalLayout();
	    assetLayout.setMargin(true);
	    assetLayout.addComponent(gridLayoutAsset);
	    
	    tabSheet = new TabSheet();
	    tabSheet.addTab(dealerVerticalLayout, I18N.message("dealer"));
	    tabSheet.addTab(applicantLayout, I18N.message("applicant"));
	    tabSheet.addTab(applicantAddressLayout, I18N.message("applicant.address"));
	    tabSheet.addTab(assetLayout, I18N.message("asset"));
	    
		return tabSheet;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void assignValues(Long id) {
		super.reset();
		if (id != null) {
			if (guarantorLayout != null) {
				tabSheet.removeComponent(guarantorLayout);
				tabSheet.removeComponent(guarantorAddressLayout);
			}
			quotation = ENTITY_SRV.getById(Quotation.class, id);	
			Asset asset = quotation.getAsset();
			cbxColor.setSelectedEntity(asset.getColor());
			txtAssetYear.setValue(getDefaultString(asset.getYear()));
			cbxEngine.setSelectedEntity(asset.getEngine());
			cbxAssetGender.setSelectedEntity(asset.getAssetGender());
			txtChassisNumber.setValue(asset.getChassisNumber());
			txtEngineNumber.setValue(asset.getEngineNumber());
			cbxCreditOfficer.setSelectedEntity(quotation.getCreditOfficer());
			cbxDealer.setSelectedEntity(quotation.getDealer());
			cbxProductionOfficer.setSelectedEntity(quotation.getProductionOfficer());
			if (quotation.getApplicant().getIndividual().getCurrentEmployment() != null) {
				EEmploymentStatus employmentStatus = quotation.getApplicant().getIndividual().getCurrentEmployment().getEmploymentStatus();				
				if (employmentStatus != null) {					
					cbxSeniorityLevel= new ERefDataComboBox<>(ESeniorityLevel.getByField(employmentStatus));
					cbxSeniorityLevel.setSelectedEntity(quotation.getApplicant().getIndividual().getCurrentEmployment().getSeniorityLevel());
				} else {
					cbxSeniorityLevel.setSelectedEntity(null);
				}
			}
			
			dfContractStartDate.setValue(quotation.getContractStartDate());			
			
			txtFirstNameEn.setValue(quotation.getApplicant().getIndividual().getFirstNameEn());
			txtFirstName.setValue(quotation.getApplicant().getIndividual().getFirstName());
			txtLastNameEn.setValue(quotation.getApplicant().getIndividual().getLastNameEn());
			txtLastName.setValue(quotation.getApplicant().getIndividual().getLastName());
			dfDateOfBirth.setValue(quotation.getApplicant().getIndividual().getBirthDate());
			
			txtTotalNumberFamilyMember.setValue(getDefaultString(quotation.getApplicant().getIndividual().getTotalFamilyMember()));		
			
			if (quotation.getApplicant().getIndividual().getMainAddress() != null) {
				addressFormPanel.assignValues(quotation.getApplicant().getIndividual().getMainAddress());
			} else {
				addressFormPanel.reset();
			}
			
			if (quotation.getGuarantor() != null) {
			    tabSheet.addTab(guarantorLayout, I18N.message("guarantor"));
			    tabSheet.addTab(guarantorAddressLayout, I18N.message("guarantor.address"));
				txtGuarantorFirstNameEn.setValue(quotation.getGuarantor().getIndividual().getFirstNameEn());
				txtGuarantorFirstName.setValue(quotation.getGuarantor().getIndividual().getFirstName());
				txtGuarantorLastNameEn.setValue(quotation.getGuarantor().getIndividual().getLastNameEn());
				txtGuarantorLastName.setValue(quotation.getGuarantor().getIndividual().getLastName());
				dfGuarantorDateOfBirth.setValue(quotation.getGuarantor().getIndividual().getBirthDate());
				
				if (quotation.getGuarantor().getIndividual().getCurrentEmployment() != null) {
					EEmploymentStatus employmentStatus = quotation.getGuarantor().getIndividual().getCurrentEmployment().getEmploymentStatus();				
					if (employmentStatus != null) {					
						cbxSeniorityLevel= new ERefDataComboBox<>(ESeniorityLevel.getByField(employmentStatus));
						cbxSeniorityLevelGuarantor.setSelectedEntity(quotation.getGuarantor().getIndividual().getCurrentEmployment().getSeniorityLevel());
					} else {
						cbxSeniorityLevelGuarantor.setSelectedEntity(null);
					}
				}
				
				if (quotation.getGuarantor().getIndividual().getMainAddress() != null) {
					guarantorAddressFormPanel.assignValues(quotation.getGuarantor().getIndividual().getMainAddress());
				} else {
					guarantorAddressFormPanel.reset();
				}
				
			} else {
				if (guarantorLayout != null) {
					tabSheet.removeComponent(guarantorLayout);
				}
				if (guarantorAddressLayout != null) {
					tabSheet.removeComponent(guarantorAddressLayout);
				}
			}
			
			dfFirstPaymentDate.setValue(quotation.getFirstDueDate());
			dfContractStartDate.setEnabled(quotation.getWkfStatus().equals(QuotationWkfStatus.ACT));
			dfFirstPaymentDate.setEnabled(quotation.getWkfStatus().equals(QuotationWkfStatus.ACT));
			
			SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (!"admin2".equals(secUser.getLogin()) && quotation.getWkfStatus().equals(QuotationWkfStatus.ACT) && quotation.getContract().getId() != null) {
				boolean changeContractDateAllowed = contractService.isOneInstallmentAlreadyPaid(quotation.getContract().getId());
				dfContractStartDate.setEnabled(!changeContractDateAllowed);
				dfFirstPaymentDate.setEnabled(!changeContractDateAllowed);
			}
		}
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		cbxDealer.setSelectedEntity(null);
		cbxCreditOfficer.setSelectedEntity(null);
		cbxProductionOfficer.setSelectedEntity(null);
		cbxColor.setSelectedEntity(null);
		txtAssetYear.setValue("");
		cbxEngine.setSelectedEntity(null);
		cbxAssetGender.setSelectedEntity(null);
		dfContractStartDate.setValue(null);
		dfFirstPaymentDate.setValue(null);
		txtFirstNameEn.setValue("");
		txtFirstName.setValue("");
		txtLastNameEn.setValue("");
		txtLastName.setValue("");
		dfDateOfBirth.setValue(null);
		txtChiefVillageName.setValue("");
		txtChiefVillagePhoneNumber.setValue("");
		txtDeputyChiefVillageName.setValue("");
		txtDeputyChiefVillagePhoneNumber.setValue("");
		txtTotalNumberFamilyMember.setValue("");
		txtMobilePhone1.setValue("");
		txtMobilePhone2.setValue("");
		txtGuarantorFirstNameEn.setValue("");
		txtGuarantorFirstName.setValue("");
		txtGuarantorLastNameEn.setValue("");
		txtGuarantorLastName.setValue("");
		dfGuarantorDateOfBirth.setValue(null);
		txtGuarantorMobilePhone1.setValue("");
		txtGuarantorMobilePhone2.setValue("");
		txtChassisNumber.setValue("");
		txtEngineNumber.setValue("");

	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatorySelectField(cbxDealer, "dealer");
		checkMandatorySelectField(cbxCreditOfficer, "credit.officer");
		checkMandatorySelectField(cbxProductionOfficer, "production.officer");
		checkMandatorySelectField(cbxSeniorityLevel, "seniorities.level");
		checkMandatorySelectField(cbxColor, "color");
		checkMandatorySelectField(cbxEngine, "engine");
		checkMandatorySelectField(cbxAssetGender, "type");
		checkMandatoryField(txtAssetYear, "year");
				
		checkMandatoryField(txtFirstNameEn, "firstname.en");
		checkMandatoryField(txtLastNameEn, "lastname.en");
		checkMandatoryField(txtFirstName, "firstname");
		checkMandatoryField(txtLastName, "lastname");
		checkMandatoryDateField(dfDateOfBirth, "dateofbirth");
		
		errors.addAll(addressFormPanel.fullValidate());
		
		if (quotation.getGuarantor() != null) {
			checkMandatoryField(txtGuarantorFirstNameEn, "firstname.en");
			checkMandatoryField(txtGuarantorLastNameEn, "lastname.en");
			checkMandatoryField(txtGuarantorFirstName, "firstname");
			checkMandatoryField(txtGuarantorLastName, "lastname");
			checkMandatoryDateField(dfGuarantorDateOfBirth, "dateofbirth");
			checkMandatorySelectField(cbxSeniorityLevelGuarantor, "seniorities.level");
			
			errors.addAll(guarantorAddressFormPanel.fullValidate());
		}
		
		return errors.isEmpty();
	}

}
