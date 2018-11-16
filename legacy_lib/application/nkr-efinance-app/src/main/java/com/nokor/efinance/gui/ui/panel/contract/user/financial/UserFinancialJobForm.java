package com.nokor.efinance.gui.ui.panel.contract.user.financial;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.panel.contact.FinancialIncomeAddressPanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;

/**
 * Contact reference form panel
 * @author uhout.cheng
 */
public class UserFinancialJobForm extends AbstractTabPanel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 5403712630231838540L;
	
	private Individual individual;
	private Employment employment;
	private FinancialIncomeAddressPanel financialIncomeAddressPanel;
 	
	private Button btnSave;
	private Button btnBack;
	
	/**
	 * @return the btnBack
	 */
	public Button getBtnBack() {
		return btnBack;
	}
	
	/** */
	public UserFinancialJobForm() {
		super.setMargin(false);
		setSpacing(true);
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnBack = new NativeButton(I18N.message("back"));
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
	
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnBack);
		navigationPanel.addButton(btnSave);
		addComponent(navigationPanel, 0);
	}	
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {	
		financialIncomeAddressPanel = new FinancialIncomeAddressPanel();
		Panel panel = new Panel();
		panel.setContent(financialIncomeAddressPanel);
		return panel;
    }
	
	/**
	 * 
	 * @param individual
	 * @param employment
	 */
	public void assignValues(Individual individual, Employment employment) {
		this.individual = individual;
		if (employment == null) {
			employment = EntityFactory.createInstance(Employment.class);
		}
		financialIncomeAddressPanel.assignValue(individual, employment);
		this.employment = employment;
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

	/**
	 * 
	 */
	public void removedMessagePanel() {
		super.removeErrorsPanel();
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		super.removeErrorsPanel();
		financialIncomeAddressPanel.reset();
	}
	
	/**
	 * Event to Click Add button Address,Contact,Reference
	 */
	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (isValid()) {
				financialIncomeAddressPanel.setEmploymentInfoAddress(individual, employment, EEmploymentType.SECO);
				displaySuccess();
			} 
		}
	}
	
}
