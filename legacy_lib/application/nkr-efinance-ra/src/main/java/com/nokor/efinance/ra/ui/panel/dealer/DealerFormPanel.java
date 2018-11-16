package com.nokor.efinance.ra.ui.panel.dealer;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.ra.ui.panel.dealer.attribute.DealerAttributeFormPanel;
import com.nokor.efinance.ra.ui.panel.dealer.contact.DelaerEmployeeContactInfoPanel;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * @author youhort.ly
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DealerFormPanel extends AbstractFormPanel implements FinServicesHelper, SelectedTabChangeListener {

	private static final long serialVersionUID = -428044630697485756L;
	
	private Dealer dealer;
	
	private TabSheet tabDealer;
	
	private DealerDetailPanel detailPanel;
	private DelaerEmployeeContactInfoPanel managerContactInfoPanel;
	private DelaerEmployeeContactInfoPanel ownerContactInfoPanel;
	private DealerBankAccountsPanel bankAccountsPanel;
	private DealerBranchesPanel branchesPanel;
//	private DealerPaymentMethodsPanel paymentMethodsPanel;
	private DealerAttributeFormPanel attributePanel;
	
	/**
	 */
	public DealerFormPanel() {
		super.init();
	}
    
    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
	@Override
	protected Dealer getEntity() {
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		
		tabDealer = new TabSheet();		
		tabDealer.addSelectedTabChangeListener(this);
		detailPanel = new DealerDetailPanel(this);
		
		tabDealer.addTab(detailPanel, I18N.message("detail"));
		
	    VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(tabDealer);
		
		return contentLayout;
	}
	
	/**
	 * @param delerId
	 */
	public void assignValues(Long deaId) {
		super.reset();
		if (deaId != null) {
			dealer = ENTITY_SRV.getById(Dealer.class, deaId);
			detailPanel.assignValues(dealer);
			if (tabDealer.getComponentCount() <= 1) {
				managerContactInfoPanel = new DelaerEmployeeContactInfoPanel();
				ownerContactInfoPanel = new DelaerEmployeeContactInfoPanel();
				bankAccountsPanel = new DealerBankAccountsPanel();
				branchesPanel = new DealerBranchesPanel();
				attributePanel = new DealerAttributeFormPanel();
//				paymentMethodsPanel = new DealerPaymentMethodsPanel();
				
				tabDealer.addTab(managerContactInfoPanel, I18N.message("manager"));
				tabDealer.addTab(ownerContactInfoPanel, I18N.message("owner"));
				tabDealer.addTab(bankAccountsPanel, I18N.message("payment"));
				tabDealer.addTab(branchesPanel, I18N.message("branches"));
//				tabDealer.addTab(paymentMethodsPanel, I18N.message("payment.methods"));
				tabDealer.addTab(attributePanel, I18N.message("attributes"));
			}
			if (managerContactInfoPanel != null) {
				managerContactInfoPanel.assignValue(dealer, ETypeContact.MANAGER);
			}
			if (ownerContactInfoPanel != null) {
				ownerContactInfoPanel.assignValue(dealer, ETypeContact.OWNER);
			}
			if (bankAccountsPanel != null) {
				bankAccountsPanel.assignValues(dealer);			
			}
			if (branchesPanel != null) {
				branchesPanel.assignValues(dealer);			
			}
			/*if (paymentMethodsPanel != null) {
				paymentMethodsPanel.assignValues(dealer);			
			}*/
			if (attributePanel != null) {
				attributePanel.assignValues(dealer);
			}
		} else {
			reset();
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		detailPanel.reset();
		
		if (managerContactInfoPanel != null) {
			tabDealer.removeComponent(managerContactInfoPanel);
		}
		if (ownerContactInfoPanel != null) {
			tabDealer.removeComponent(ownerContactInfoPanel);
		} 
		if (bankAccountsPanel != null) {
			tabDealer.removeComponent(bankAccountsPanel);
		}
		if (branchesPanel != null) {
			tabDealer.removeComponent(branchesPanel);
		}
		/*if (paymentMethodsPanel != null) {
			tabDealer.removeComponent(paymentMethodsPanel);
		}*/
		if (attributePanel != null) {
			tabDealer.removeComponent(attributePanel);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#validate()
	 */
	@Override
	protected boolean validate() {
		return errors.isEmpty();
	}

	/**
	 * @see com.vaadin.ui.TabSheet.SelectedTabChangeListener#selectedTabChange(com.vaadin.ui.TabSheet.SelectedTabChangeEvent)
	 */
	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		/*if (tabDealer.getSelectedTab().equals(paymentMethodsPanel)) {
			paymentMethodsPanel.assignValues(dealer);
		}*/
	}
}
