package com.nokor.efinance.gui.ui.panel.cashflow;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.cashflow.Cashflow;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;

/**
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CashflowFormPanel extends AbstractFormPanel {

	private static final long serialVersionUID = -9118726466667794288L;
	private CashflowFormContent cashFlowContent;
    
	@PostConstruct
	public void PostConstruct() {
        super.init();
        setCaption(I18N.message("cashflow"));
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}
    
	@Override
	protected Cashflow getEntity() {
		return cashFlowContent.setCashFlowValue();
	}

	@Override
	protected com.vaadin.ui.Component createForm() {
		cashFlowContent = new CashflowFormContent(ENTITY_SRV);
		return cashFlowContent.createForm();
	}


	/**
	 * @param id
	 */
	public void assignValues(Long id) {
		super.reset();
		cashFlowContent.assignValues(id);
	}
	
	/**
	 * Reset
	 */
	@Override
	public void reset() {
		super.reset();
		cashFlowContent.newCashFlow();
	
	}
	
	/**
	 * @return
	 */
	@Override
	protected boolean validate() {
		errors = cashFlowContent.getValidate();
		return errors.isEmpty();
	}

}
