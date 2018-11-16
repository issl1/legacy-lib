package com.nokor.efinance.core.applicant.panel.applicant.individual;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.applicant.panel.contact.ContactInfoPanel;
import com.nokor.efinance.core.applicant.service.IndividualService;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Contact reference full detail for LESSEE
 * @author uhout.cheng
 */
public class ReferencePanel extends AbstractTabPanel implements ClickListener, FrmkServicesHelper {
	
	/** */
	private static final long serialVersionUID = -5625869440013638826L;
	
	private ReferenceInfoPanel referenceInfoPanel;
	private List<ReferenceInfoPanel> referenceInfoPanels;
	private List<List<ContactInfoPanel>> listContainListOfContactInfoPanel;
	
	private Button btnAddReferenceInfo;
	private Button btnCancelAddReferenceInfo;
	
	private VerticalLayout referenceLayout;
	private VerticalLayout newreferenceLayout;
	
	private Button btnDeleteReferenceInfo;
	private Applicant applicant;
	private List<IndividualReferenceInfo> individualReferenceInfos;
	
	private Button btnSave;
	private Button btnBack;
	
	/**
	 * @return the btnBack
	 */
	public Button getBtnBack() {
		return btnBack;
	}

	/**
	 * @param btnBack the btnBack to set
	 */
	public void setBtnBack(Button btnBack) {
		this.btnBack = btnBack;
	}
	
	/** */
	public ReferencePanel() {
		super.setMargin(false);
		setMargin(false);
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		
		btnBack = new NativeButton(I18N.message("back"));
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
		setBtnBack(btnBack);
	
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
		referenceInfoPanel = new ReferenceInfoPanel();
		
		referenceInfoPanels = new ArrayList<>();
		
		btnAddReferenceInfo = new Button(I18N.message("add.reference"));
		btnAddReferenceInfo.setWidth("450px");
		btnAddReferenceInfo.addClickListener(this);
		
		referenceLayout = new VerticalLayout();
		newreferenceLayout = new VerticalLayout();
		referenceLayout.addComponent(referenceInfoPanel);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(referenceLayout);
		verticalLayout.addComponent(btnAddReferenceInfo);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(verticalLayout);
		
		Panel mainPanel = new Panel();
		mainPanel.setContent(horizontalLayout);
		
		TabSheet tabReference = new TabSheet();
		tabReference.addTab(mainPanel, I18N.message("reference"));
		
        return tabReference;
    }
	
	/**
	 * Get applicant for SaveorUpdate
	 * @param applicant
	 * @return
	 */
	public Applicant getApplicant(Applicant applicant) {
		if (applicant.getQuotations() != null && !applicant.getQuotations().isEmpty()) {
			referenceInfoPanel.getReferenceInfo(referenceInfoPanels, applicant.getIndividual(), listContainListOfContactInfoPanel);
		}
		return applicant;
	}
	
	/**
	 * Set applicant
	 * @param applicant
	 */
	public void assignValues(Applicant applicant) {
		if (applicant != null) {
			this.applicant = applicant;
			List<Contract> contracts = applicant.getContracts();
			//It show only LESSEE tab
			if (contracts != null && !contracts.isEmpty()) {
				referenceLayout.setVisible(true);
				btnAddReferenceInfo.setVisible(true);
				this.assignReferenceInfoValue(applicant);
			} else {
				referenceLayout.setVisible(false);
				btnAddReferenceInfo.setVisible(false);
			}
		}
	}

	/**
	 * @return
	 */
	public boolean isValid() {
		removeErrorsPanel();
		for (ReferenceInfoPanel contactReferenceInfoPanel : referenceInfoPanels) {
			List<String> contactInfoErrors = contactReferenceInfoPanel.isValid();
			if (contactInfoErrors != null && !contactInfoErrors.isEmpty()) {
				errors.addAll(contactReferenceInfoPanel.isValid());
			}
		}
		if (!errors.isEmpty()) {
			displayErrorsPanel();
		}
		return errors.isEmpty();
	}

	/**
	 * Reset panel
	 */
	public void reset() {
		removeErrorsPanel();
		referenceInfoPanel.reset();
		referenceInfoPanels.clear();
	}
	
	/**
	 * Event to Click Add button Address,Contact,Reference
	 */
	@Override
	public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
		if (event.getButton() == btnAddReferenceInfo) {
			referenceInfoPanel = new ReferenceInfoPanel();
			newreferenceLayout = new VerticalLayout();
			
			btnCancelAddReferenceInfo = new Button("X");
			btnCancelAddReferenceInfo.setStyleName(Reindeer.BUTTON_LINK);
			btnCancelAddReferenceInfo.addClickListener(new CancelAddReferenceInfo(newreferenceLayout, referenceInfoPanel));
			
			newreferenceLayout.addComponent(btnCancelAddReferenceInfo);
			newreferenceLayout.setComponentAlignment(btnCancelAddReferenceInfo, Alignment.MIDDLE_RIGHT);
			newreferenceLayout.addComponent(referenceInfoPanel);
			referenceLayout.addComponent(newreferenceLayout);
			referenceInfoPanels.add(referenceInfoPanel);
		} else if (event.getButton() == btnSave) {
			if (applicant != null && applicant.getId() != null) {
				if (isValid()) {
					IndividualService individualService = SpringUtils.getBean(IndividualService.class);
					individualService.saveOrUpdateIndividualReference(getApplicant(applicant).getIndividual());
					displaySuccess();
				} 
			}
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 */
	private class CancelAddReferenceInfo implements ClickListener {
		
		/** */
		private static final long serialVersionUID = -5458670875472738592L;
		
		private VerticalLayout referenceLayouts;
		private ReferenceInfoPanel contactReferenceInfoLayout;
		
		public CancelAddReferenceInfo(VerticalLayout referenceInfoLayout, ReferenceInfoPanel contactReferenceInfoLayout) {
			this.referenceLayouts = referenceInfoLayout;
			this.contactReferenceInfoLayout = contactReferenceInfoLayout;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			this.contactReferenceInfoLayout.removeErrorsPanel();
			referenceLayout.removeComponent(this.referenceLayouts);
			referenceInfoPanels.remove(this.contactReferenceInfoLayout);
		}
	}
	
	/**
	 * assign value to Reference Panel
	 * @param applicant
	 */
	public void assignReferenceInfoValue(Applicant applicant) {
		listContainListOfContactInfoPanel = new ArrayList<>();
		individualReferenceInfos = applicant.getIndividual().getIndividualReferenceInfos();
		referenceLayout.removeAllComponents();
		newreferenceLayout.removeAllComponents();
		referenceInfoPanels.clear();
		if (!individualReferenceInfos.isEmpty()) {
			for (IndividualReferenceInfo individualReferenceInfo : individualReferenceInfos) {
				btnDeleteReferenceInfo = new Button("X");
				btnDeleteReferenceInfo.setStyleName(Reindeer.BUTTON_LINK);
				btnDeleteReferenceInfo.addClickListener(new ButtonDeleteReferenceInfoPanelListener(individualReferenceInfo));
				
				referenceInfoPanel = new ReferenceInfoPanel();
				List<ContactInfoPanel> contactInfoPanels = referenceInfoPanel.assignValue(individualReferenceInfo);
				listContainListOfContactInfoPanel.add(contactInfoPanels);
				referenceLayout.addComponent(btnDeleteReferenceInfo);
				referenceLayout.addComponent(referenceInfoPanel);
				referenceLayout.setComponentAlignment(btnDeleteReferenceInfo, Alignment.MIDDLE_RIGHT);
				referenceInfoPanels.add(referenceInfoPanel);
				
			}
		} else {
			referenceInfoPanel = new ReferenceInfoPanel();
			referenceLayout.addComponent(referenceInfoPanel);
			referenceInfoPanels.add(referenceInfoPanel);
		}
	}
	
	/**
	 * set listener to button delete reference info panel
	 * @author buntha.chea
	 *
	 */
	private class ButtonDeleteReferenceInfoPanelListener implements ClickListener{
		

		private static final long serialVersionUID = -2366823219939383496L;
		private IndividualReferenceInfo individualReferenceInfo;
		
		public ButtonDeleteReferenceInfoPanelListener(IndividualReferenceInfo individualReferenceInfo) {
			this.individualReferenceInfo = individualReferenceInfo;
		}

		/**
		 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
		 */
		@Override
		public void buttonClick(ClickEvent event) {
			confirmDeleteReferenceInfo(individualReferenceInfo);
		}
	}
	
	/**
	 * 
	 * @param individualReferenceInfo
	 */
	private void confirmDeleteReferenceInfo(IndividualReferenceInfo individualReferenceInfo) {
		final DeleteInformationInApplicant deleteInformationInApplicant = new DeleteInformationInApplicant();
		deleteInformationInApplicant.setIndividualReferenceInfo(individualReferenceInfo);
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.delete.referenceinfo"),
		        new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 2380193173874927880L;
					public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	IndividualReferenceInfo individualReferenceInfo = deleteInformationInApplicant.getIndividualReferenceInfo();
		                	List<IndividualReferenceContactInfo> individualReferenceContactInfos = individualReferenceInfo.getIndividualReferenceContactInfos();
		                	for (IndividualReferenceContactInfo individualReferenceContactInfo : individualReferenceContactInfos) {
								ContactInfo contactInfo = individualReferenceContactInfo.getContactInfo();
								ENTITY_SRV.delete(IndividualReferenceContactInfo.class, individualReferenceContactInfo.getId());
								ENTITY_SRV.delete(ContactInfo.class, contactInfo.getId());
							}
		                	ENTITY_SRV.delete(IndividualReferenceInfo.class, individualReferenceInfo.getId());
		                	individualReferenceInfos.remove(individualReferenceInfo);
		                	assignReferenceInfoValue(applicant);
		                	Notification.show("",I18N.message("delete.success"),Notification.Type.HUMANIZED_MESSAGE);
		                }
		            }
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
	}
	
	/**
	 * 
	 * @author buntha.chea
	 */
	private class DeleteInformationInApplicant {
		private IndividualReferenceInfo individualReferenceInfo;

		/**
		 * @return the IndividualReferenceInfo
		 */
		public IndividualReferenceInfo getIndividualReferenceInfo() {
			return individualReferenceInfo;
		}

		/**
		 * @param individualReferenceInfo the individualReferenceInfo to set
		 */
		public void setIndividualReferenceInfo(IndividualReferenceInfo individualReferenceInfo) {
			this.individualReferenceInfo = individualReferenceInfo;
		}
	}
}
