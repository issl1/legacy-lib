package com.nokor.efinance.gui.ui.panel.contract.user.financial;

import org.seuksa.frmk.model.EntityFactory;

import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.panel.contact.FinancialIncomeAddressPanel;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class MainIncomePanel extends AbstractTabPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = 8233661362147176427L;

	private FinancialIncomeAddressPanel financialIncomeAddressPanel;
	private Individual individual;
	private Employment employment;
	
	private Button btnEdit;
	private Button btnSave;
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 80, "btn btn-success button-small");
		btnSave.setVisible(false);
		btnSave.addClickListener(this);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnEdit.setEnabled(true);
		btnEdit.addClickListener(this);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.addComponent(btnEdit);
		buttonLayout.addComponent(btnSave);
		
		financialIncomeAddressPanel = new FinancialIncomeAddressPanel();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(financialIncomeAddressPanel);
		verticalLayout.addComponent(buttonLayout);
		verticalLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		
		Panel panel = new Panel(verticalLayout);
		panel.setCaption(I18N.message("main.income"));
		return panel;
	}
	
	
	/**
	 * 
	 * @param individual
	 */
	public void assignValues(Individual individual, Employment employment) {
		financialIncomeAddressPanel.setEnabledControls(btnSave.isVisible());
		this.individual = individual;
		if (employment == null) {
			employment = EntityFactory.createInstance(Employment.class);
		}
		financialIncomeAddressPanel.assignValue(individual, employment);
		this.employment = employment;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.removeErrorsPanel();
		financialIncomeAddressPanel.reset();
	}

	/**
	 * @return
	 */
	public boolean isValid() {
		removeErrorsPanel();
		errors.addAll(financialIncomeAddressPanel.fullValidate());
		if (!errors.isEmpty()) {
			displayErrorsPanel();
		}
		return errors.isEmpty();
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnEdit) {
			btnEdit.setVisible(false);
			btnSave.setVisible(true);
			financialIncomeAddressPanel.setEnabledControls(true);
		} else if (event.getButton() == btnSave) {
			if (isValid()) {
				financialIncomeAddressPanel.setEmploymentInfoAddress(individual, employment, EEmploymentType.CURR);
				displaySuccess();
				
				btnSave.setVisible(false);
				btnEdit.setVisible(true);
				financialIncomeAddressPanel.setEnabledControls(false);
			}
		}
		
	}
}
