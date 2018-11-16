package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.applicant.model.Company;
import com.nokor.efinance.core.applicant.model.CompanyContactInfo;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceContactInfo;
import com.nokor.efinance.core.applicant.model.IndividualReferenceInfo;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.applicant.ApplicantsPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.other.ColContactOtherPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.phones.ColContactPhonePanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class ContactInfoTitleLayout extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 9215527143803123483L;
	
	private ColContactPhonePanel contactPhonePanel;
	private ColContactOtherPanel contactOtherPanel;
	private boolean phone = false;
	private Long applicantId;
	
	private ColContractHistoryFormPanel contractHistoriesFormPanel;
	
	/**
	 * @param contractHistoriesFormPanel the contractHistoriesFormPanel to set
	 */
	public void setContractHistoriesFormPanel(ColContractHistoryFormPanel contractHistoriesFormPanel) {
		this.contractHistoriesFormPanel = contractHistoriesFormPanel;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(boolean phone) {
		this.phone = phone;
	}

	/**
	 * @param contactPhonePanel the contactPhonePanel to set
	 */
	public void setContactPhonePanel(ColContactPhonePanel contactPhonePanel) {
		this.contactPhonePanel = contactPhonePanel;
	}

	/**
	 * @param contactOtherPanel the contactOtherPanel to set
	 */
	public void setContactOtherPanel(ColContactOtherPanel contactOtherPanel) {
		this.contactOtherPanel = contactOtherPanel;
	}
	
	/**
	 * @param applicantId the applicantId to set
	 */
	public void setApplicantId(Long applicantId) {
		this.applicantId = applicantId;
	}

	/**
	 * LESSEE & GUARANTOR layout
	 * @param nbRows
	 * @param iCol
	 * @param iRow
	 * @param caption
	 * @param value
	 * @param ind
	 * @param indInfo
	 * @param com
	 * @param comConInfo
	 */
	public ContactInfoTitleLayout(int nbRows, int iCol, int iRow, String caption, String value, Individual ind, 
			IndividualContactInfo indInfo, Company com, CompanyContactInfo comConInfo) {
		addComponent(getGridLayout(nbRows, iCol, iRow, caption, value, ind, indInfo, com, comConInfo));
	}
	
	/**
	 * Reference layout
	 * @param nbRows
	 * @param iCol
	 * @param iRow
	 * @param caption
	 * @param value
	 * @param indRefInfo
	 * @param indRefContInfo
	 */
	public ContactInfoTitleLayout(int nbRows, int iCol, int iRow, String caption, String value, 
			IndividualReferenceInfo indRefInfo, IndividualReferenceContactInfo indRefContInfo) {
		addComponent(getGridLayout(nbRows, iCol, iRow, caption, value, indRefInfo, indRefContInfo));
	}
	
	/**
	 * LESSEE & GUARANTOR
	 * @param nbRows
	 * @param iCol
	 * @param iRow
	 * @param caption
	 * @param value
	 * @param ind
	 * @param indConInfo
	 * @param com
	 * @param comConInfo
	 * @return
	 */
	private GridLayout getGridLayout(int nbRows, int iCol, int iRow, String caption, String value, Individual ind, 
			IndividualContactInfo indConInfo, Company com, CompanyContactInfo comConInfo) {
		GridLayout gridLayout = new GridLayout(4, nbRows);
		Label lblCaption = null;
		if (nbRows > 1) {
			lblCaption = ComponentFactory.getHtmlLabel("<b>" + I18N.message(caption) + StringUtils.SPACE + (iRow + 1) + "</b>");
		} else {
			lblCaption = ComponentFactory.getHtmlLabel("<b>" + I18N.message(caption) + "</b>");
		}
		lblCaption.setWidth(100, Unit.PIXELS);
		Button btnApplicant = new Button();
		btnApplicant.setWidth(140, Unit.PIXELS);
		btnApplicant.setCaption(value);
		btnApplicant.setStyleName(Reindeer.BUTTON_LINK);
		btnApplicant.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -4366966675481342230L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("!" + ApplicantsPanel.NAME + "/" + applicantId);
			}
		});
				
		Button btnAction = new Button();
		btnAction.setIcon(FontAwesome.PLUS);
		btnAction.setStyleName(Reindeer.BUTTON_LINK);
		
		gridLayout.addComponent(lblCaption, iCol++, iRow);
		gridLayout.addComponent(btnApplicant, iCol++, iRow);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, iRow);
		gridLayout.addComponent(btnAction, iCol++, iRow);
		gridLayout.setComponentAlignment(lblCaption, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(btnAction, Alignment.MIDDLE_CENTER);
		
		btnAction.addClickListener(new ClickListener() {
		
			/** */
			private static final long serialVersionUID = 1527672271405694313L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				ContactInfoDetailPopup popup = new ContactInfoDetailPopup(contactPhonePanel, contactOtherPanel, phone);
				popup.assignValues(ind, indConInfo, null, null, com, comConInfo);
				popup.setContractHistoriesFormPanel(contractHistoriesFormPanel);
				UI.getCurrent().addWindow(popup);
			}
		});
		return gridLayout;
	}
	
	/**
	 * Reference
	 * @param nbRows
	 * @param iCol
	 * @param iRow
	 * @param caption
	 * @param value
	 * @param indRefInfo
	 * @param indRefContInfo
	 * @return
	 */
	private GridLayout getGridLayout(int nbRows, int iCol, int iRow, String caption, String value, 
			IndividualReferenceInfo indRefInfo, IndividualReferenceContactInfo indRefContInfo) {
		GridLayout gridLayout = new GridLayout(4, nbRows);
		Label lblCaption = null;
		if (nbRows > 1) {
			lblCaption = ComponentFactory.getHtmlLabel("<b>" + I18N.message(caption) + StringUtils.SPACE + (iRow + 1) + "</b>");
		} else {
			lblCaption = ComponentFactory.getHtmlLabel("<b>" + I18N.message(caption) + "</b>");
		}
		lblCaption.setWidth(100, Unit.PIXELS);
		TextField txtValue = ComponentFactory.getTextField(100, 140);
		txtValue.setValue(value);
		txtValue.setEnabled(false);
		Button btnAction = new Button();
		btnAction.setIcon(FontAwesome.PLUS);
		btnAction.setStyleName(Reindeer.BUTTON_LINK);
		
		gridLayout.addComponent(lblCaption, iCol++, iRow);
		gridLayout.addComponent(txtValue, iCol++, iRow);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, iRow);
		gridLayout.addComponent(btnAction, iCol++, iRow);
		gridLayout.setComponentAlignment(lblCaption, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(btnAction, Alignment.MIDDLE_CENTER);
		
		btnAction.addClickListener(new ClickListener() {
	
			/** */
			private static final long serialVersionUID = -709806710133351380L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				ContactInfoDetailPopup popup = new ContactInfoDetailPopup(contactPhonePanel, contactOtherPanel, phone);
				popup.assignValues(null, null, indRefInfo, indRefContInfo, null, null);
				popup.setContractHistoriesFormPanel(contractHistoriesFormPanel);
				UI.getCurrent().addWindow(popup);
			}
		});
		return gridLayout;
	}
	
}
