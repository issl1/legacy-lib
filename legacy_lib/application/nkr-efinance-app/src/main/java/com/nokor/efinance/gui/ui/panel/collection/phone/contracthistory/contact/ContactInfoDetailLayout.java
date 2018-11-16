package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.other.ColContactOtherPanel;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.phones.ColContactPhonePanel;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.ersys.core.hr.model.organization.ContactInfo;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class ContactInfoDetailLayout extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -8263230990435569471L;

	private ColContactPhonePanel conPhonePanel;
	private ColContactOtherPanel conOtherPanel;
	
	/**
	 * @param conOtherPanel the conOtherPanel to set
	 */
	public void setConOtherPanel(ColContactOtherPanel conOtherPanel) {
		this.conOtherPanel = conOtherPanel;
	}

	/**
	 * @param conPhonePanel the conPhonePanel to set
	 */
	public void setConPhonePanel(ColContactPhonePanel conPhonePanel) {
		this.conPhonePanel = conPhonePanel;
	}

	/**
	 * 
	 * @param nbRows
	 * @param iCol
	 * @param iRow
	 * @param caption
	 * @param contactInfo
	 * @param contHisFrmPanel
	 */
	public ContactInfoDetailLayout(int nbRows, int iCol, int iRow, String caption, ContactInfo contactInfo, ColContractHistoryFormPanel contHisFrmPanel) {
		GridLayout gridLayout = new GridLayout(5, nbRows);
		Label lblCaption = null;
		if (nbRows > 1) {
			lblCaption = ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + (iRow + 1));
		} else {
			lblCaption = ComponentFactory.getLabel(caption);
		}
		lblCaption.setWidth(100, Unit.PIXELS);
		TextField txtValue = ComponentFactory.getTextField();
		CheckBox cbActive = new CheckBox();
		txtValue.setWidth(140, Unit.PIXELS);
		if (ETypeContactInfo.LANDLINE.equals(contactInfo.getTypeInfo()) || ETypeContactInfo.MOBILE.equals(contactInfo.getTypeInfo())) {
			txtValue.setMaxLength(10);
		} else {
			txtValue.setMaxLength(60);
		}
		txtValue.setEnabled(false);
		txtValue.setValue(getDefaultString(contactInfo.getValue()));
		if (contactInfo.isActive()) {
			cbActive.setValue(true);
		} else {
			cbActive.setValue(false);
		}
		cbActive.setEnabled(false);
		Button btnAction = new Button();
		btnAction.setIcon(FontAwesome.EDIT);
		btnAction.setStyleName(Reindeer.BUTTON_LINK);
		
		gridLayout.addComponent(lblCaption, iCol++, iRow);
		gridLayout.addComponent(txtValue, iCol++, iRow);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, iRow);
		gridLayout.addComponent(cbActive, iCol++, iRow);
		gridLayout.addComponent(btnAction, iCol++, iRow);
		gridLayout.setComponentAlignment(lblCaption, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbActive, Alignment.MIDDLE_CENTER);
		gridLayout.setComponentAlignment(btnAction, Alignment.MIDDLE_CENTER);
		
		btnAction.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -7182243758925708816L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (!txtValue.isEnabled() && !cbActive.isEnabled()) {
					txtValue.setEnabled(true);
					cbActive.setEnabled(true);
					btnAction.setIcon(FontAwesome.SAVE);
				} else {
					contactInfo.setValue(txtValue.getValue());
					contactInfo.setActive(cbActive.getValue());
					try {
						if (StringUtils.isNotEmpty(txtValue.getValue())) {
							CONT_SRV.update(contactInfo);
							ComponentLayoutFactory.displaySuccessfullyMsg();
							txtValue.setEnabled(false);
							cbActive.setEnabled(false);
							btnAction.setIcon(FontAwesome.EDIT);
							txtValue.setRequired(false);
							contHisFrmPanel.refereshAfterSaved();
							if (conPhonePanel != null) {
								conPhonePanel.refresh();
							} else if (conOtherPanel != null) {
								conOtherPanel.refresh();
							}
						} else {
							txtValue.setRequired(true);
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
		});
		
		addComponent(gridLayout);
	}
	
}
