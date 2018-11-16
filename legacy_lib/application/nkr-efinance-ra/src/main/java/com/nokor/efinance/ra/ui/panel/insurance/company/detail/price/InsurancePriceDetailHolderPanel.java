package com.nokor.efinance.ra.ui.panel.insurance.company.detail.price;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 * Insurance Price Detail Holder Panel
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InsurancePriceDetailHolderPanel extends VerticalLayout {
	/** */
	private static final long serialVersionUID = 3905005808937834930L;
	
	private TabSheet tabSheet;
	
	private InsurancePriceDetailFormPanel lostPanel;
	private InsurancePriceDetailFormPanel aomPanel;

	/**
	 */
	@PostConstruct
	public void PostContruct() {	
		setSizeFull();
		setMargin(true);
		setCaption(I18N.message("price.details"));
		lostPanel = new InsurancePriceDetailFormPanel(EServiceType.INSLOS);
		lostPanel.setCaption(I18N.message("lost"));
		lostPanel.setServiceType(EServiceType.INSLOS);
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
		tabSheet.addTab(lostPanel);
		tabSheet.addTab(aomPanel);
		
		addComponent(tabSheet);
	}
	
	/**
	 * Assign Values
	 * @param insuranceCompanyId
	 */
	public void assignValues(Long insuranceCompanyId) {
		tabSheet.setSelectedTab(lostPanel);
		lostPanel.assignValues(insuranceCompanyId);
		aomPanel.assignValues(insuranceCompanyId);
	}

}
