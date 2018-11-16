package com.nokor.efinance.gui.ui.panel.report.overdue;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OverdueReportFormPanel extends AbstractFormPanel {

	private static final long serialVersionUID = -428044630697485756L;
	
	private EntityService entityService = SpringUtils.getBean(EntityService.class);

	private SecUserComboBox cbxCreditOfficer;
	private SecUserComboBox cbxCollectionOfficer;
	private Contract contract; 
    
	/** */
    @PostConstruct
	public void PostConstruct() {
        super.init();
        NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addSaveClickListener(this);
	}

    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#getEntity()
     */
    @Override
	protected Contract getEntity() {
    	// TODO PYI
//		contract.setCollectionCreditOfficer(cbxCreditOfficer.getSelectedEntity());
//		contract.setOfficer(cbxCollectionOfficer.getSelectedEntity());
		return contract;
	}

    /**
     * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#createForm()
     */
	@Override
	protected com.vaadin.ui.Component createForm() {
				
		cbxCreditOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CO));
		cbxCollectionOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CC, EStatusRecord.ACTIV));
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		final GridLayout gridLayoutCollectionProcess = new GridLayout(5, 3);
		gridLayoutCollectionProcess.setSpacing(true);
		
        int iCol = 0;
        gridLayoutCollectionProcess.addComponent(new Label(I18N.message("credit.officer")), iCol++, 0);
        gridLayoutCollectionProcess.addComponent(cbxCreditOfficer, iCol++, 0);
        
        iCol = 0;
        gridLayoutCollectionProcess.addComponent(new Label(I18N.message("collection.officer")), iCol++, 1);
        gridLayoutCollectionProcess.addComponent(cbxCollectionOfficer, iCol++, 1);
		contentLayout.addComponent(gridLayoutCollectionProcess);
        
		return contentLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		this.contract = contract;
		if (contract != null) {
			// TODO PYI
//			if (contract.getCollectionCreditOfficer() != null) {
//				cbxCreditOfficer.setSelectedEntity(contract.getCollectionCreditOfficer());
//			}
//			if (contract.getOfficer() != null) {
//				cbxCollectionOfficer.setSelectedEntity(contract.getOfficer());
//			}
		} 
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#saveEntity()
	 */
	@Override
	public void saveEntity() {
		entityService.saveOrUpdate(getEntity());
	}
	
	/**
	 * Reset
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractFormPanel#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		cbxCreditOfficer.setSelectedEntity(null);
		cbxCollectionOfficer.setSelectedEntity(null);
	}
}
