package com.nokor.efinance.gui.ui.panel.contract.user.financial;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * User financial panel
 * @author uhout.cheng
 */
public class UserFinancialPanel extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = 2405068721507477690L;

	private VerticalLayout contentLayout;
	private UserFinancialInfoFormPanel userFinancialInfoForm;
	
	private MainIncomePanel mainIncomePanel;
	private UserFinancialJobTable userFinancialJobTable;
	private Panel incomesPanel;
	
	/**
	 * @return the contentLayout
	 */
	public VerticalLayout getContentLayout() {
		return contentLayout;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		setHeight(500, Unit.PIXELS);
		userFinancialInfoForm = new UserFinancialInfoFormPanel();
		incomesPanel = getIncomesPanel();
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(new MarginInfo(false, false, true, false));
		contentLayout.addComponent(userFinancialInfoForm);
		contentLayout.addComponent(incomesPanel);
		return contentLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getIncomesPanel() {
		mainIncomePanel = new MainIncomePanel();
		userFinancialJobTable = new UserFinancialJobTable(this);
		
		VerticalLayout otherIncomeLayout = new VerticalLayout();
		otherIncomeLayout.setMargin(new MarginInfo(false, true, true, true));
		otherIncomeLayout.addComponent(userFinancialJobTable);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.addComponent(mainIncomePanel);
		verLayout.addComponent(otherIncomeLayout);
		
		Panel panel = new Panel();
		panel.setCaption(I18N.message("incomes"));
		panel.setContent(verLayout);
		return panel;
	}
	
	/**
	 * 
	 * @param isVisible
	 */
	public void setVisibleLayouts(boolean isVisible) {
		userFinancialInfoForm.setVisible(isVisible);
		incomesPanel.setVisible(isVisible);
	}
	
	/**
	 * 
	 * @param individual
	 */
	public void assignValues(Individual individual) {
		reset();
		if (individual != null) {
			individual = ENTITY_SRV.getById(Individual.class, individual.getId());
			userFinancialInfoForm.assignValues(individual.getId());
			userFinancialJobTable.assignValues(individual);
			mainIncomePanel.assignValues(individual, individual.getCurrentEmployment());
		}
	}

	/**
	 * 
	 */
	public void reset() {
		userFinancialInfoForm.reset();
		mainIncomePanel.reset();
	}
	
}
