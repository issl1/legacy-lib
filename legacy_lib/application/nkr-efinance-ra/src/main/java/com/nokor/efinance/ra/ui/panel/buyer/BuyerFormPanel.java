package com.nokor.efinance.ra.ui.panel.buyer;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.auction.model.Buyer;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

/**
 * Buyer Form Panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BuyerFormPanel extends AbstractFormPanel implements FMEntityField {

	/** */
	private static final long serialVersionUID = 7895718348761625327L;
	
	private TextField txtFirstNameEn;
	private TextField txtLastNameEn;
	private TextField txtTelephone;
	
	private Buyer buyer;
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
        super.init();
        
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		txtFirstNameEn = ComponentFactory.getTextField("firstname.en", true, 50, 200);
		txtLastNameEn = ComponentFactory.getTextField("lastname.en", true, 50, 200);
		txtTelephone = ComponentFactory.getTextField("telephone", false, 15, 200);
		
		FormLayout formLayout = new FormLayout();
		
		formLayout.addComponent(txtFirstNameEn);
		formLayout.addComponent(txtLastNameEn);
		formLayout.addComponent(txtTelephone);
		
		return formLayout;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		this.buyer = new Buyer();
		txtFirstNameEn.setValue("");
		txtLastNameEn.setValue("");
		txtTelephone.setValue("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
	 */
	@Override
	protected Entity getEntity() {
		if (this.buyer != null) {
			buyer.setFirstNameEn(txtFirstNameEn.getValue());
			buyer.setLastNameEn(txtLastNameEn.getValue());
			buyer.setTelephone(txtTelephone.getValue());
		}
		return buyer;
	}
	
	/**
	 * Assign value to form
	 */
	public void assignValues(Long id) {
		if (id != null) {
			this.buyer = ENTITY_SRV.getById(Buyer.class, id);
			txtFirstNameEn.setValue(buyer.getFirstNameEn());
			txtLastNameEn.setValue(buyer.getLastNameEn());
			txtTelephone.setValue(buyer.getTelephone());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		checkMandatoryField(txtFirstNameEn, FIRST_NAME_EN);
		checkMandatoryField(txtLastNameEn, LAST_NAME_EN);
		
		return this.errors.isEmpty();
	}

}
