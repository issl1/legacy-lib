package com.nokor.efinance.ra.ui.panel.dealer.contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nokor.efinance.core.applicant.panel.contact.ContactInfoPanel;
import com.nokor.efinance.core.dealer.model.DealerEmployeeContactInfo;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
/**
 * 
 * @author buntha.chea
 *
 */
public class DealerEmployeeContactFormPanel extends VerticalLayout implements ClickListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3402273661694673936L;
	
	private BackContactFormListener backListener;
	private Button btnBack;
	private Button btnSave;
	private ContactInfoPanel contactinfoPanel;
	private DealerEmployeeContactInfo dealerEmployeeContactInfo;
	
	private VerticalLayout messagePanel;
	private List<String> errors;
	
	public DealerEmployeeContactFormPanel() {
		
		btnBack = new NativeButton(I18N.message("back"), this);
		btnBack.setIcon(FontAwesome.STEP_BACKWARD);
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		
		contactinfoPanel = new ContactInfoPanel();
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnBack);
		navigationPanel.addButton(btnSave);
		
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		errors = new ArrayList<>();
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(contactinfoPanel);
		
		addComponent(mainLayout);
	}
	
	/**
	 * assignValue
	 */
	public void assignValue(DealerEmployeeContactInfo dealerEmployeeContactInfo) {
		reset();
		this.dealerEmployeeContactInfo = dealerEmployeeContactInfo;
		contactinfoPanel.assignValueDealerEmployeeContactInfo(dealerEmployeeContactInfo);
	}
	
	/**
	 * @param backListener the backListener to set
	 */
	public void setBackListener(BackContactFormListener backListener) {
		this.backListener = backListener;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnBack) {
			if (backListener != null) {
				backListener.onBack();
			}
		} else if (event.getButton() == btnSave) {
			save();
		}
		
	}
	
	/**
	 * Validation
	 * @return
	 */
	private boolean validate() {
		reset();
		Label messageLabel;
	
		errors = contactinfoPanel.isValid();
		
		if (dealerEmployeeContactInfo.getDealerEmployee().getId() == null) {
			errors.add(I18N.message("save.dealer.employee.before.save.contact"));
		}
		
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
	 * Save
	 */
	private void save() {
		if (validate()) {
			try {
				dealerEmployeeContactInfo = contactinfoPanel.getDealerEmployeeContactInfo(dealerEmployeeContactInfo);
				ENTITY_SRV.saveOrUpdate(dealerEmployeeContactInfo);
				ENTITY_SRV.refresh(dealerEmployeeContactInfo.getDealerEmployee());
				ComponentLayoutFactory.displaySuccessfullyMsg();
			} catch (Exception e) {
				e.printStackTrace();
				Notification.show("", e.getMessage(), Type.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	public interface BackContactFormListener extends Serializable {
		/**
		 * onBack Listener
		 */
		void onBack();
	}
	/**
	 * Reset
	 */
	public void reset() {
		errors.clear();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
	}

}
