package com.nokor.efinance.ra.ui.panel.referential.exchangerate;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.reference.model.ExchangeRate;
import com.nokor.ersys.core.finance.model.eref.ECurrency;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AmountField;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * ExchangeRate form panel
 * @author nora.ky
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ExchangeRateFormPanel extends AbstractFormPanel {
	
	private static final long serialVersionUID = 2899487004883122824L;
	private ExchangeRate exchangeRate;
    private TextField txtDesc;
    private TextField txtDescEn;
    private ERefDataComboBox<ECurrency> cbxCurrencyFrom;
    private ERefDataComboBox<ECurrency> cbxCurrencyTo;
	private AmountField rate;
	private AutoDateField startDate;
	private AutoDateField endDate;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		exchangeRate.setDesc(txtDesc.getValue());
		exchangeRate.setDescEn(txtDescEn.getValue());
		exchangeRate.setFrom(cbxCurrencyFrom.getSelectedEntity());
		exchangeRate.setTo(cbxCurrencyTo.getSelectedEntity());
		exchangeRate.setRate(rate.getDouble());
		exchangeRate.setStart(startDate.getValue());
		exchangeRate.setEnd(endDate.getValue());
		return exchangeRate;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();
		txtDescEn = ComponentFactory.getTextField("desc.en", false, 60, 200);	
		txtDescEn.setRequired(true);
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);	
		cbxCurrencyFrom = new ERefDataComboBox<ECurrency>(I18N.message("currency.from"), ECurrency.class);
		cbxCurrencyTo = new ERefDataComboBox<ECurrency>(I18N.message("currency.to"), ECurrency.class);
		cbxCurrencyFrom.setRequired(true);
		cbxCurrencyTo.setRequired(true);
		rate = new AmountField(I18N.message("rate"));
		rate.setMask("##0.0000");
		startDate = ComponentFactory.getAutoDateField("startdate", true);
		endDate = ComponentFactory.getAutoDateField("enddate", false);
		formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(cbxCurrencyFrom);
        formPanel.addComponent(cbxCurrencyTo);
        formPanel.addComponent(rate);
        formPanel.addComponent(startDate);
        formPanel.addComponent(endDate);
		return formPanel;
	}
	
	/**
	 * @param exchangeRateId
	 */
	public void assignValues(Long exchangeRateId) {
		super.reset();
		if (exchangeRateId != null) {
			exchangeRate = ENTITY_SRV.getById(ExchangeRate.class, exchangeRateId);
			txtDesc.setValue(exchangeRate.getDesc());
			txtDescEn.setValue(exchangeRate.getDescEn());
			cbxCurrencyFrom.setSelectedEntity(exchangeRate.getFrom());
			cbxCurrencyTo.setSelectedEntity(exchangeRate.getTo());
			rate.setValue(Double.toString(exchangeRate.getRate()));
			startDate.setValue(exchangeRate.getStart());
			endDate.setValue(exchangeRate.getEnd());
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		exchangeRate = new ExchangeRate();
		txtDesc.setValue("");
		txtDescEn.setValue("");
		cbxCurrencyFrom.setSelectedEntity(null);
		cbxCurrencyTo.setSelectedEntity(null);
		rate.setValue("");
		startDate.setValue(null);
		endDate.setValue(null);
		markAsDirty();
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtDescEn, "desc.en");
		checkMandatorySelectField(cbxCurrencyFrom, "currency.from");
		checkMandatorySelectField(cbxCurrencyTo, "currency.to");
		checkConvertToSelfCurrency(cbxCurrencyFrom.getSelectedEntity(), cbxCurrencyTo.getSelectedEntity(), "currency.check.selfconvert");
		checkMandatoryField(rate, "rate");
		checkMandatoryDateField(startDate, "startdate");
		checkRangeDateField(startDate, endDate);
		return errors.isEmpty();
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param messageKey
	 * @return
	 */
	protected boolean checkConvertToSelfCurrency(ECurrency from, ECurrency to, String messageKey) {
		boolean isValid = true;
		if (from != null && to != null && from.getCode().equals(to.getCode())) {
			errors.add(I18N.message(messageKey));
			isValid = false;
		}
		return isValid;
	}
}
