package com.nokor.efinance.ra.ui.panel.finproduct.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.asset.model.ECalculMethod;
import com.nokor.efinance.core.contract.model.cashflow.ETreasuryType;
import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.core.financial.model.FinService;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.widget.JournalEventComboBox;
import com.nokor.ersys.finance.accounting.model.JournalEvent;
import com.nokor.finance.services.shared.system.EFrequency;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author youhort.ly
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceFormPanel extends AbstractFormPanel implements FinServicesHelper {

	private static final long serialVersionUID = -8371274781712659783L;

	private FinService service;
	
	private CheckBox cbActive;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;

    private CheckBox cbPaidBeginContract;
    private ERefDataComboBox<EFrequency> cbxFrequency;
    private ERefDataComboBox<ETreasuryType> cbxTreasuryType;
    private ERefDataComboBox<ECalculMethod> cbxCalculMethod;
    private CheckBox cbAllowchangePrice;
    private CheckBox cbSplitWithInstallment;

    private JournalEventComboBox cbxJournalEvent;
    
    private TextField txtPrice;
    
    private ERefDataComboBox<EServiceType> cbxServiceType;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("create"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		service.setCode(txtCode.getValue());
		service.setDesc(txtDesc.getValue());
		service.setDescEn(txtDescEn.getValue());
		service.setServiceType(cbxServiceType.getSelectedEntity());
		service.setCalculMethod(cbxCalculMethod.getSelectedEntity() == null ? ECalculMethod.FIX : cbxCalculMethod.getSelectedEntity());
		service.setAllowChangePrice(cbAllowchangePrice.getValue());
		service.setTePrice(Double.parseDouble((txtPrice.getValue().equals("") ? "0" : txtPrice.getValue())));
		service.setVatPrice(0d);
		service.setTiPrice(service.getTePrice());
		service.setPercentage(0d);
		service.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		service.setTreasuryType(cbxTreasuryType.getSelectedEntity() == null ? ETreasuryType.APP : cbxTreasuryType.getSelectedEntity());
		service.setFrequency(cbxFrequency.getSelectedEntity());
		service.setPaidBeginContract(cbPaidBeginContract.getValue());
		service.setSplitWithInstallment(cbSplitWithInstallment.getValue());
		service.setJournalEvent(cbxJournalEvent.getSelectedEntity());
		return service;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();
		
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);      
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
        
		cbxServiceType = new ERefDataComboBox<EServiceType>(I18N.message("service.type"), EServiceType.values());
        cbxServiceType.setRequired(true);
     
    	cbxCalculMethod = new ERefDataComboBox<ECalculMethod>(I18N.message("calculate.method"), ECalculMethod.class);
    	cbxCalculMethod.setRequired(true);
    	
        cbAllowchangePrice = new CheckBox(I18N.message("allow.change.price"));
        txtPrice = ComponentFactory.getTextField("service.amount", false, 60, 200);
        cbxFrequency = new ERefDataComboBox<EFrequency>(I18N.message("frequency"), EFrequency.class);
        cbxTreasuryType = new ERefDataComboBox<ETreasuryType>(I18N.message("payer"), ETreasuryType.class);
        cbxTreasuryType.setRequired(true);
        
        cbPaidBeginContract = new CheckBox(I18N.message("paid.begin.contract"));
        cbPaidBeginContract.setValue(true);
        
        cbSplitWithInstallment = new CheckBox(I18N.message("split.with.installment"));
        
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);     
        
        List<JournalEvent> journalEvents = ENTITY_SRV.list(new BaseRestrictions<>(JournalEvent.class));
        cbxJournalEvent = new JournalEventComboBox(journalEvents);
        cbxJournalEvent.setWidth(200, Unit.PIXELS);
        cbxJournalEvent.setImmediate(true);
        cbxJournalEvent.setCaption(I18N.message("event"));
        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(cbxServiceType);
//        formPanel.addComponent(cbxTreasuryType);
//        formPanel.addComponent(cbxCalculMethod);
        formPanel.addComponent(txtPrice);
//        formPanel.addComponent(cbxFrequency);
        formPanel.addComponent(cbxJournalEvent);
//        formPanel.addComponent(cbPaidBeginContract);
//        formPanel.addComponent(cbAllowchangePrice);
//        formPanel.addComponent(cbSplitWithInstallment);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * @param asmakId
	 */
	public void assignValues(Long serviId) {
		super.reset();
		if (serviId != null) {
			service = ENTITY_SRV.getById(FinService.class, serviId);
			txtCode.setValue(service.getCode());
			txtDescEn.setValue(service.getDescEn());
			txtDesc.setValue(service.getDesc());
			cbxServiceType.setSelectedEntity(service.getServiceType());
			cbxCalculMethod.setSelectedEntity(service.getCalculMethod());
			cbAllowchangePrice.setValue(service.isAllowChangePrice());
			cbActive.setValue(service.getStatusRecord().equals(EStatusRecord.ACTIV));
			txtPrice.setValue(AmountUtils.format(service.getTiPrice()));
			cbxFrequency.setSelectedEntity(service.getFrequency());
			cbxTreasuryType.setSelectedEntity(service.getTreasuryType());
			cbPaidBeginContract.setValue(service.isPaidBeginContract());
			cbSplitWithInstallment.setValue(service.isSplitWithInstallment());
			cbxJournalEvent.setSelectedEntity(service.getJournalEvent());
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		service = new FinService();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbActive.setValue(true);
		txtPrice.setValue("");
		cbAllowchangePrice.setValue(false);
		cbxCalculMethod.setSelectedEntity(ECalculMethod.FIX);
		cbxServiceType.setSelectedEntity(null);
		cbxFrequency.setSelectedEntity(null);
		cbxTreasuryType.setSelectedEntity(ETreasuryType.APP);
		cbPaidBeginContract.setValue(false);
		cbSplitWithInstallment.setValue(false);
		cbxJournalEvent.setSelectedEntity(null);
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkDuplicatedCode(FinService.class, txtCode, service.getId(), "code");
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxServiceType, "service.type");
//		checkMandatorySelectField(cbxCalculMethod, "calculate.method");
		checkDoubleField(txtPrice, "service.amount");
//		checkMandatorySelectField(cbxTreasuryType, "payer");
		return errors.isEmpty();
	}
}
