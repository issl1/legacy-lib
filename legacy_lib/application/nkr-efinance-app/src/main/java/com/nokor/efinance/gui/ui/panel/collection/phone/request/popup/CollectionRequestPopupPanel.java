package com.nokor.efinance.gui.ui.panel.collection.phone.request.popup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;
/**
 * 
 * @author buntha.chea
 *
 */
public class CollectionRequestPopupPanel extends Window implements ClickListener, CloseListener, FinServicesHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5062923405677383340L;
	
	private Listener listener;
	private List<Contract> contracts;
	
	private TextArea txtReason;
	private Button btnRequest;
	private Button btnCancel;
	
	private List<String> errors;
	private VerticalLayout messagePanel;
	private String requestType;
	
	public interface Listener extends Serializable {
        void onClose(CollectionRequestPopupPanel dialog);
    }
	
	/**
	 * Show a modal ConfirmDialog in a window.
	 * @param contracts
	 * @param requestType
	 * @param listener
	 * @return
	 */
    public static CollectionRequestPopupPanel show(final List<Contract> contracts, String requestType, final Listener listener) {    
    	CollectionRequestPopupPanel requestPopupPanel = new CollectionRequestPopupPanel(contracts, requestType);
    	requestPopupPanel.listener = listener;
    	UI.getCurrent().addWindow(requestPopupPanel);
        return requestPopupPanel;
    }
	
    /**
     * 
     * @param contracts
     * @param requestType
     */
	public CollectionRequestPopupPanel(List<Contract> contracts, String requestType) {
		this.contracts = contracts;
		this.requestType = requestType;
		setCaption(I18N.message("sms"));
		setModal(true);
		setResizable(false);
		addCloseListener(this);
		setCaption(I18N.message(requestType));
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		txtReason = ComponentFactory.getTextArea(false, 250, 60);
		btnRequest = new NativeButton(I18N.message(requestType), this);
		btnRequest.setIcon(FontAwesome.CHECK);
		btnCancel = new NativeButton(I18N.message("cancel"),this);
		btnCancel.setIcon(FontAwesome.TIMES);
		
		Label lblReason = ComponentLayoutFactory.getLabelCaptionRequired("reason");
		HorizontalLayout formLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		formLayout.addComponent(lblReason);
		formLayout.addComponent(txtReason);
		formLayout.setComponentAlignment(lblReason, Alignment.MIDDLE_LEFT);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnRequest);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout requestFlagLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		requestFlagLayout.addComponent(navigationPanel);
		requestFlagLayout.addComponent(messagePanel);
		requestFlagLayout.addComponent(formLayout);
		
		 // Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
              if (errors.isEmpty()) {
            	  if (listener != null) {
                      listener.onClose(CollectionRequestPopupPanel.this);
                  }
                  UI.getCurrent().removeWindow(CollectionRequestPopupPanel.this);
              }
            }
        };
        btnRequest.addClickListener(cb);
		
		setContent(requestFlagLayout);
	}
	
	/**
	 * Validate Add BankAccount 
	 * @return
	 */
	private boolean validate() {
		messagePanel.removeAllComponents();
		errors = new ArrayList<>();
		Label messageLabel;

		if (StringUtils.isEmpty(txtReason.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("reason") }));
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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnRequest) {
			if (validate()) {
				if (this.contracts != null && !this.contracts.isEmpty()) {
					for (Contract contract : contracts) {
						if ("flag".equals(requestType)) {
							COL_SRV.requestFlagRequest(contract.getId(), txtReason.getValue());
							Notification.show("",I18N.message("request.flag.success"),Notification.Type.HUMANIZED_MESSAGE);
						} else if ("assist".equals(requestType)) {
							COL_SRV.requestAssistRequest(contract.getId(), txtReason.getValue());
							Notification.show("",I18N.message("request.assist.success"), Notification.Type.HUMANIZED_MESSAGE);
						}	
					}
				}
				close();
			}
		} else if (event.getButton() == btnCancel) {
			close();
		}
		
	}

	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}

}
