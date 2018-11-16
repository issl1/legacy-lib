package com.nokor.efinance.gui.ui.panel.report.contract.overdue;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.widget.SecUserComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * Change the collection officer of the contract
 * @author bunlong.taing
 */
public class ChangeCollectionOfficerPanel extends AbstractTabPanel implements SaveClickListener, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = -397606368573263057L;
	
	private SecUserComboBox cbxCollectionOfficer;
	private NavigationPanel navigationPanel;
	private Contract contract;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		navigationPanel = new NavigationPanel();
		navigationPanel.addSaveClickListener(this);
		cbxCollectionOfficer = new SecUserComboBox(DataReference.getInstance().getUsers(FMProfile.CC, EStatusRecord.ACTIV));
		cbxCollectionOfficer.setCaption(I18N.message("collection.officer"));
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(cbxCollectionOfficer);
		
		return verticalLayout;
	}
	
	/**
	 * @param contract
	 */
	public void assignValues (Contract contract) {
		this.contract = contract;
		if (this.contract != null) {
			// TODO PYI
//			cbxCollectionOfficer.setSelectedEntity(this.contract.getOfficer());
		}
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.toolbar.event.SaveClickListener#saveButtonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void saveButtonClick(ClickEvent arg0) {
		if (this.contract != null) {
			// TODO PYI
//			this.contract.setOfficer(cbxCollectionOfficer.getSelectedEntity());
			ENTITY_SRV.saveOrUpdate(this.contract);
		}
	}

}
