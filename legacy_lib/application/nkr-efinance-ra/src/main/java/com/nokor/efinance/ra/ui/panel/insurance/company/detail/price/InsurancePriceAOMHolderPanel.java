package com.nokor.efinance.ra.ui.panel.insurance.company.detail.price;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Insurance Price Detail Holder Panel (AOM)
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InsurancePriceAOMHolderPanel extends VerticalLayout {
	
	/** */
	private static final long serialVersionUID = 1037854226702158546L;

	private TabSheet tabSheet;
	
	private InsurancePriceDetailFormPanel aomPanel;

	/**
	 */
	@PostConstruct
	public void PostContruct() {	
		setSizeFull();
		setMargin(true);
		setCaption(I18N.message("aom"));
		aomPanel = new InsurancePriceDetailFormPanel(EServiceType.INSAOM);
		aomPanel.setCaption(I18N.message("aom"));
		aomPanel.setServiceType(EServiceType.INSAOM);
		createForm();
	}

	/**
	 * Create Form
	 */
	private void createForm() {
		tabSheet = new TabSheet();
		tabSheet.addTab(aomPanel);
		
		addComponent(tabSheet);
	}
	
	/**
	 * Assign Values
	 * @param insuranceCompanyId
	 */
	public void assignValues(Long insuranceCompanyId) {
		aomPanel.assignValues(insuranceCompanyId);
	}

}
