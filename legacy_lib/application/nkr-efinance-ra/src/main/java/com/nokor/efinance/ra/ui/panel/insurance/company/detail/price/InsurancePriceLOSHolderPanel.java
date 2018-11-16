package com.nokor.efinance.ra.ui.panel.insurance.company.detail.price;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import com.nokor.efinance.core.financial.model.EServiceType;
import com.nokor.efinance.ra.ui.panel.insurance.company.claims.InsuranceClaimDetailFormPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.tabsheet.TabSheet;

/**
 * Insurance Price Detail Holder Panel (LOS)
 * @author bunlong.taing
 */
@org.springframework.stereotype.Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InsurancePriceLOSHolderPanel extends AbstractControlPanel {
	
	/** */
	private static final long serialVersionUID = -6340243139156799731L;

	private TabSheet tabSheetLos;
	private TabSheet tabSheetClaim;
	private InsurancePriceDetailFormPanel lostPanel;
	private InsuranceClaimDetailFormPanel claimPanel;

	/**
	 */
	@PostConstruct
	public void PostContruct() {	
		setSizeFull();
		setMargin(true);
		setCaption(I18N.message("lost"));
		lostPanel = new InsurancePriceDetailFormPanel(EServiceType.INSLOS);
		lostPanel.setCaption(I18N.message("insurance"));
		lostPanel.setServiceType(EServiceType.INSLOS);
		
		claimPanel = new InsuranceClaimDetailFormPanel();
		claimPanel.setCaption(I18N.message("claims"));
		createForm();
	}

	/**
	 * Create Form
	 */
	private void createForm() {
		tabSheetLos = new TabSheet();
		tabSheetLos.addTab(lostPanel);
		
		tabSheetClaim = new TabSheet();
		tabSheetClaim.addTab(claimPanel);
		
		setSpacing(true);
		addComponent(tabSheetLos);
		addComponent(tabSheetClaim);
	}
	
	/**
	 * Assign Values
	 * @param insuranceCompanyId
	 */
	public void assignValues(Long insuranceCompanyId) {
		lostPanel.assignValues(insuranceCompanyId);
		claimPanel.assignValues(insuranceCompanyId);
	}

}
