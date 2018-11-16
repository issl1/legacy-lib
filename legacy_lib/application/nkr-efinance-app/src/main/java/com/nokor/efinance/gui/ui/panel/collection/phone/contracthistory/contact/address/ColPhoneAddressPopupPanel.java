package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.contact.address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.IndividualAddress;
import com.nokor.efinance.core.applicant.panel.AddressInfoPanel;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;
/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhoneAddressPopupPanel extends Window implements ClickListener, CloseListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5663474780542385010L;
	
	private AddressInfoPanel addressInfoPanel;
	private Address address;
	private Button btnSave;
	private Button btnCancel;
	
	private VerticalLayout messagePanel;
	private Listener listener = null;
	
	private Individual individual;
	
	/**
	 * 
	 */
	public ColPhoneAddressPopupPanel() {
		setCaption(I18N.message("address"));
		setModal(true);
		setWidth(570, Unit.PIXELS);
		setHeight(515, Unit.PIXELS);
		
		 // Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
            	if (validate()) {
            		if (listener != null) {
                        listener.onClose(ColPhoneAddressPopupPanel.this);
                     }
            		UI.getCurrent().removeWindow(ColPhoneAddressPopupPanel.this);
            	}
            }
        };
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		btnSave.addClickListener(cb);
		
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(this);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		addressInfoPanel = new AddressInfoPanel();
		
		VerticalLayout addressLayout = new VerticalLayout();
		addressLayout.setMargin(true);
		addressLayout.addComponent(addressInfoPanel);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(addressLayout);
		mainLayout.setComponentAlignment(addressLayout, Alignment.TOP_CENTER);
		
		setContent(mainLayout);
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		List<String> errors = new ArrayList<String>();
		messagePanel.removeAllComponents();
		Label messageLabel;
		
		errors.addAll(addressInfoPanel.fullValidate());
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		
		return errors.isEmpty();
	}
	
	/**
	 * 
	 * @param address
	 * @param individual
	 */
	private void assignValue(Address address, Individual individual) {
		this.address = address;
		this.individual = individual;
		reset();
		Applicant app = APP_SRV.getApplicantCategory(EApplicantCategory.INDIVIDUAL, individual.getId());
		addressInfoPanel.assignValue(this.address, app);
		if (this.address == null) {
			this.address = Address.createInstance();
		} 
	}
	
	/**
	 * Save or Update Address
	 */
	private void save() {
		if (validate()) {
			try {
				this.address = addressInfoPanel.getContactAddress(address);
				if (this.address.getId() != null) {
					INDIVI_SRV.update(this.address);
				} else {
					IndividualAddress indAddress = IndividualAddress.createInstance();
					indAddress.setIndividual(this.individual);
					indAddress.setAddress(this.address);
					INDIVI_SRV.saveOrUpdateIndividualAddress(indAddress);
				}
				ComponentLayoutFactory.displaySuccessfullyMsg();
			} catch (Exception ex) {
				ex.printStackTrace();
				Notification.show("", ex.getMessage(), Type.ERROR_MESSAGE);
			}
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
		
	}

	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}
	
	/**
	 * Reset
	 */
	private void reset() {
		addressInfoPanel.reset();
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	public interface Listener extends Serializable {
        void onClose(ColPhoneAddressPopupPanel dialog);
    }
	
	/**
	 * Show a modal ConfirmDialog in a window.
	 * @param address
	 * @param individual
	 * @param listener
	 * @return
	 */
    public static ColPhoneAddressPopupPanel show(final Address address, Individual individual, final Listener listener) {    	
    	ColPhoneAddressPopupPanel colPhoneAddressPopupPanel = new ColPhoneAddressPopupPanel();
    	colPhoneAddressPopupPanel.listener = listener;
    	colPhoneAddressPopupPanel.assignValue(address, individual);
    	UI.getCurrent().addWindow(colPhoneAddressPopupPanel);
        return colPhoneAddressPopupPanel;
    }
}
