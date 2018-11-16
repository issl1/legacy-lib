package com.nokor.efinance.gui.ui.panel.contract.asset.supplier;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.DealerAddress;
import com.nokor.efinance.core.dealer.model.DealerEmployee;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.eref.ETypeContact;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * Contact detail in supplier tab
 * @author uhout.cheng
 */
public class SupplierContactDetailPanel extends Panel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -6086579594061812481L;
	
	private TextField txtManagerFullName;
	private TextField txtManagerPhoneNumber;
	private TextField txtOwnerFullName;
	private TextField txtOwnerPhoneNumber;
	private TextArea txtAddress;
	
	/**
	 * 
	 */
	public SupplierContactDetailPanel() {
		setCaption(I18N.message("contact.details"));
		
		txtManagerFullName = ComponentFactory.getTextField("manager.fullname", false, 60, 150);
		txtManagerPhoneNumber = ComponentFactory.getTextField("manager.phone.no", false, 20, 150);
		txtOwnerFullName = ComponentFactory.getTextField("owner.fullname", false, 60, 150);
		txtOwnerPhoneNumber = ComponentFactory.getTextField("owner.phone.no", false, 20, 150);
		txtAddress = ComponentFactory.getTextArea("address", false, 300, 50);
		setDisableControls(false);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(txtManagerFullName, false));
		horLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(txtManagerPhoneNumber, false));
		verLayout.addComponent(horLayout);
		
		horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(txtOwnerFullName, false));
		horLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(txtOwnerPhoneNumber, false));
		verLayout.addComponent(horLayout);
		
		verLayout.addComponent(ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(txtAddress, false));
		
		setContent(verLayout);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDefaultString(String value) {
		return value != null ? value : "";
	}
	
	/**
	 * 
	 * @param isEnabled
	 */
	private void setDisableControls(boolean isEnabled) {
		txtManagerFullName.setEnabled(isEnabled);
		txtManagerPhoneNumber.setEnabled(isEnabled);
		txtOwnerFullName.setEnabled(isEnabled);
		txtOwnerPhoneNumber.setEnabled(isEnabled);
		txtAddress.setEnabled(isEnabled);
	}
	
	/**
	 * 
	 * @param dealer
	 */
	public void assignValues(Dealer dealer) {
		this.reset();
		DealerEmployee manager = dealer.getDealerEmployee(ETypeContact.MANAGER);
		if (manager != null) {
			txtManagerFullName.setValue(getDefaultString(manager.getFullNameLocale()));
			txtManagerPhoneNumber.setValue(getDefaultString(manager.getMobilePro()));
		}
		DealerEmployee owner = dealer.getDealerEmployee(ETypeContact.OWNER);
		if (owner != null) {
			txtOwnerFullName.setValue(getDefaultString(owner.getFullNameLocale()));
			txtOwnerPhoneNumber.setValue(getDefaultString(owner.getMobilePro()));
		}
		DealerAddress dealerAddress = dealer.getDealerAddress(ETypeAddress.MAIN);
		if (dealerAddress != null) {
			Address address = dealerAddress.getAddress();
			if (address != null) {
				txtAddress.setValue(ADDRESS_SRV.getDetailAddress(address));
			}
		}
	}
	
	/**
	 * 
	 */
	protected void reset() {
		txtManagerFullName.setValue(StringUtils.EMPTY);
		txtManagerPhoneNumber.setValue(StringUtils.EMPTY);
		txtOwnerFullName.setValue(StringUtils.EMPTY);
		txtOwnerPhoneNumber.setValue(StringUtils.EMPTY);
		txtAddress.setValue(StringUtils.EMPTY);
	}
	
}
