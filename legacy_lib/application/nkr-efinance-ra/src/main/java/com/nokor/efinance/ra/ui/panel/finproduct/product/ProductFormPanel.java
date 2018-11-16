package com.nokor.efinance.ra.ui.panel.finproduct.product;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.ERoundingFormat;
import com.nokor.efinance.core.stock.model.Product;
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
public class ProductFormPanel extends AbstractFormPanel {
	
	private static final long serialVersionUID = 7921384403817084517L;

	private Product product;
	
	private CheckBox cbActive;
	private TextField txtCode;
	private TextField txtDesc;
    private TextField txtDescEn;
    private ERefDataComboBox<ERoundingFormat> cbxInstallmentRounding;
    
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
	
	@Override
	protected Entity getEntity() {
		product.setCode(txtCode.getValue());
		product.setDesc(txtDesc.getValue());
		product.setDescEn(txtDescEn.getValue());
		product.setStatusRecord(cbActive.getValue() ? EStatusRecord.ACTIV : EStatusRecord.INACT);
		return product;
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		final FormLayout formPanel = new FormLayout();		
		txtCode = ComponentFactory.getTextField("code", true, 60, 200);		
		txtDescEn = ComponentFactory.getTextField("desc.en", true, 60, 200);        
		txtDesc = ComponentFactory.getTextField35("desc", false, 60, 200);
		cbxInstallmentRounding = new ERefDataComboBox<>(ERoundingFormat.class);
		cbxInstallmentRounding.setCaption(I18N.message("installment.rounding"));
        cbActive = new CheckBox(I18N.message("active"));
        cbActive.setValue(true);        
        formPanel.addComponent(txtCode);
        formPanel.addComponent(txtDescEn);
        formPanel.addComponent(txtDesc);
        formPanel.addComponent(cbxInstallmentRounding);
        formPanel.addComponent(cbActive);
		return formPanel;
	}
	
	/**
	 * @param produId
	 */
	public void assignValues(Long produId) {
		super.reset();
		if (produId != null) {
			product = ENTITY_SRV.getById(Product.class, produId);
			txtCode.setValue(product.getCode());
			txtDescEn.setValue(product.getDescEn());
			txtDesc.setValue(product.getDesc());
			cbActive.setValue(product.getStatusRecord().equals(EStatusRecord.ACTIV));
		}
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		product = new Product();
		txtCode.setValue("");
		txtDescEn.setValue("");
		txtDesc.setValue("");
		cbxInstallmentRounding.setSelectedEntity(null);
		cbActive.setValue(true);
		markAsDirty();
	}
	
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtCode, "code");
		checkMandatoryField(txtDescEn, "desc.en");		
		return errors.isEmpty();
	}
}
