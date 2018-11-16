package com.nokor.efinance.ra.ui.panel.subdealer;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.address.panel.AddressPanel;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.dealer.service.DealerService;
import com.nokor.efinance.core.shared.dealer.DealerEntityField;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SubDealerFormPanel extends AbstractFormPanel implements DealerEntityField {

	private static final long serialVersionUID = -428044630697485756L;

	private Dealer dealer;
	
	private CheckBox cbActive;
	private TextField txtName;
	private TextField txtNameEn;
	private TextField txtIntCode;
    private TextField txtExtCode;
    private TextField txtVatRegistrationNo;
    private TextField txtMobilePhone;
    private TextField txtWorkPhone;
    private TextField txtEmail;
    private TextField txtHomePage;
    private TextField txtRegPlateNumberFee;
    private CheckBox cbIncludeInDailyReport;
    
    private AddressPanel addressFormPanel;
    
    @Autowired
    private DealerService dealerService;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("delear.create"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
    
    /*
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Dealer getEntity() {
		dealer.setDealerType(EDealerType.BRANCH);
		dealer.setName(txtName.getValue());
		dealer.setNameEn(txtNameEn.getValue());
		dealer.setCode(txtIntCode.getValue());
		//dealer.setExternalCode(txtExtCode.getValue());
		//dealer.setVatRegistrationNo(txtVatRegistrationNo.getValue());
		dealer.setTel(txtWorkPhone.getValue());
		dealer.setMobile(txtMobilePhone.getValue());
		dealer.setEmail(txtEmail.getValue());
		dealer.setWebsite(txtHomePage.getValue());
		dealer.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		if (dealer.getMainAddress() == null) {
			Address address = new Address();
			dealer.addAddress(addressFormPanel.getAddress(address), ETypeAddress.MAIN);
		} else {
			Address mainAddress = dealer.getMainAddress();
			mainAddress = addressFormPanel.getAddress(mainAddress);
		}
		return dealer;
	}

	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
			
		txtNameEn = ComponentFactory.getTextField("name.en", true, 60, 400);		
		txtName = ComponentFactory.getTextField35("name", false, 60, 400);		
		txtIntCode = ComponentFactory.getTextField("internal.code", true, 60, 200);		
		txtExtCode = ComponentFactory.getTextField("external.code", false, 60, 200);        
		txtVatRegistrationNo = ComponentFactory.getTextField("vat.registration.no", false, 60, 200);
		txtWorkPhone = ComponentFactory.getTextField("work.phone", false, 60, 200);
		txtMobilePhone = ComponentFactory.getTextField("mobile.phone", false, 60, 200);
		txtEmail = ComponentFactory.getTextField("email", false, 60, 200);
		txtHomePage = ComponentFactory.getTextField("homepage", false, 60, 200);
		txtRegPlateNumberFee = ComponentFactory.getTextField("registration.fee", false, 60, 200);
		cbIncludeInDailyReport = new CheckBox(I18N.message("include.in.daily.report"));
		cbIncludeInDailyReport.setValue(true);
		
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);
        
        final Panel detailPanel = new Panel(I18N.message("detail"));
        detailPanel.setSizeFull();
        final FormLayout formDetailPanel = new FormLayout();
        formDetailPanel.setMargin(true);
        formDetailPanel.setSizeFull();
        formDetailPanel.addComponent(txtNameEn);
        formDetailPanel.addComponent(txtName);
        formDetailPanel.addComponent(txtIntCode);
        formDetailPanel.addComponent(txtExtCode);
        formDetailPanel.addComponent(txtVatRegistrationNo);
        formDetailPanel.addComponent(txtRegPlateNumberFee);
        formDetailPanel.addComponent(cbIncludeInDailyReport);
        formDetailPanel.addComponent(cbActive);
        formDetailPanel.addComponent(txtWorkPhone);
        formDetailPanel.addComponent(txtMobilePhone);
        formDetailPanel.addComponent(txtEmail);
        formDetailPanel.addComponent(txtHomePage);
        detailPanel.setContent(formDetailPanel);
        
        final VerticalLayout leftVerticalLayout = new VerticalLayout();
        leftVerticalLayout.setSizeFull();
        leftVerticalLayout.setSpacing(true);
        leftVerticalLayout.addComponent(detailPanel);
                
        final VerticalLayout rightVerticalLayout = new VerticalLayout();
        rightVerticalLayout.setSizeFull();
        final Panel addressPanel = new Panel(I18N.message("address"));
        addressFormPanel = new AddressPanel(true, ETypeAddress.WORK, "dealerAddress");
        addressFormPanel.setMargin(true);
        addressPanel.setContent(addressFormPanel);
        rightVerticalLayout.addComponent(addressPanel);
        
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.addComponent(leftVerticalLayout);
        horizontalLayout.addComponent(rightVerticalLayout);
        horizontalLayout.setExpandRatio(leftVerticalLayout, 1);
        horizontalLayout.setExpandRatio(rightVerticalLayout, 1);
        
		return horizontalLayout;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long delerId) {
		super.reset();
		if (delerId != null) {
			dealer = ENTITY_SRV.getById(Dealer.class, delerId);
			txtName.setValue(dealer.getName());
			txtNameEn.setValue(dealer.getNameEn());
			txtIntCode.setEnabled(false);
			txtIntCode.setValue(dealer.getCode());
			//txtExtCode.setValue(dealer.getExternalCode());
			//txtVatRegistrationNo.setValue(dealer.getVatRegistrationNo());
			txtWorkPhone.setValue(dealer.getTel());
			txtMobilePhone.setValue(dealer.getMobile());
			txtEmail.setValue(dealer.getEmail());
			txtHomePage.setValue(dealer.getWebsite());
			cbActive.setValue(dealer.getStatusRecord().equals(EStatusRecord.ACTIV));
			
			addressFormPanel.assignValues(dealer.getMainAddress());
		} else {
			reset();
		}
	}
	
	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		dealerService.createDealer(getEntity());
	}
	
	/*
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		dealer = new Dealer();
		txtName.setValue("");
		txtNameEn.setValue("");
		txtIntCode.setValue("");
		txtExtCode.setValue("");
		txtVatRegistrationNo.setValue("");
		txtWorkPhone.setValue("");
		txtMobilePhone.setValue("");
		txtEmail.setValue("");
		txtHomePage.setValue("");
		txtRegPlateNumberFee.setValue("");
		cbIncludeInDailyReport.setValue(true);
		cbActive.setValue(true);
		txtIntCode.setEnabled(true);
		addressFormPanel.reset();
		markAsDirty();
	}
	
	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		boolean isValid = true;
		if (StringUtils.isEmpty(txtNameEn.getValue())) {
			errors.add(I18N.message("field.required.1", I18N.message("name.en")));
			isValid = false;
		}
		if (StringUtils.isEmpty(txtIntCode.getValue())) {
			errors.add(I18N.message("field.required.1", I18N.message("internal.code")));
			isValid = false;
		}
		List<String> addressErrors = addressFormPanel.partialValidate();
		if (addressErrors != null && !addressErrors.isEmpty()) {
			errors.addAll(addressErrors);
			isValid = false;
		}
		return isValid;
	}

}
