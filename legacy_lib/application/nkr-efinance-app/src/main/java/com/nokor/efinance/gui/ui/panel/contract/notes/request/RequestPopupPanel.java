package com.nokor.efinance.gui.ui.panel.contract.notes.request;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractRequest;
import com.nokor.efinance.core.contract.model.ERequestType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * Notes pop up panel
 * @author uhout.cheng
 */
public class RequestPopupPanel extends Window implements ClickListener, FinServicesHelper, CloseListener {

	/** */
	private static final long serialVersionUID = 5230887165545615705L;

	private ERefDataComboBox<ERequestType> cbxRequestType;
	private CheckBox cbProcessed;
	private TextField txtOthersValue;
	private TextField txtComment;
	
	private Button btnSave;
	private Button btnClose;
	
	private Contract contract;
	private ContractRequest contractRequest;
	
	private RequestTablePanel requestTablePanel;
	
	/**
	 * 
	 * @param contract
	 * @param requestsTablePanel
	 */
	public RequestPopupPanel(Contract contract, RequestTablePanel requestsTablePanel) {
		this.contract = contract;
		this.requestTablePanel = requestsTablePanel;
		setCaption(I18N.message("request"));
		setModal(true);
		setResizable(false);
		addCloseListener(this);
		init();
	}
	
	/**
	 */
	private void init() {
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.addClickListener(this);
		btnSave.setIcon(FontAwesome.SAVE);
	     
		btnClose = new NativeButton(I18N.message("close"));
		btnClose.addClickListener(this);
		btnClose.setIcon(FontAwesome.TIMES);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnClose);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		contentLayout.setSpacing(true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(createForm());
	    
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @param contractRequest
	 */
	public void assignValues(ContractRequest contractRequest) {
		reset();
		this.contractRequest = contractRequest;
		if (contractRequest != null) {
			cbxRequestType.setSelectedEntity(contractRequest.getRequestType());
			txtOthersValue.setValue(contractRequest.getOthersValue());
			txtComment.setValue(contractRequest.getComment());
			cbProcessed.setValue(contractRequest.isProcessed());
		}
	}
	
	/**
	 * @return
	 */
	private VerticalLayout createForm() {
		cbxRequestType = new ERefDataComboBox<>(I18N.message("request.type"), ERequestType.values());
		cbxRequestType.setWidth(150, Unit.PIXELS);
		txtOthersValue = ComponentFactory.getTextField("others.value", false, 100, 150);
		txtOthersValue.setVisible(false);
		txtComment = ComponentFactory.getTextField("comment", false, 100, 150);
		cbProcessed = new CheckBox(I18N.message("processed"));
		
		cbxRequestType.addValueChangeListener(new ValueChangeListener() {

			/** */
			private static final long serialVersionUID = 4670940686917598800L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				txtOthersValue.setVisible(ERequestType.OTH.equals(cbxRequestType.getSelectedEntity()));
			}
		});
		
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		frmLayout.addComponent(cbxRequestType);
		frmLayout.addComponent(txtOthersValue);
		frmLayout.addComponent(txtComment);
		frmLayout.addComponent(cbProcessed);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		contentLayout.addComponent(frmLayout);
		return contentLayout;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnClose) {
			close();
		}
	}
	
	/**
	 * Save
	 */
	private void save() {
		if (contractRequest == null) {
			contractRequest = EntityFactory.createInstance(ContractRequest.class);
		}
		contractRequest.setContract(contract);
		contractRequest.setRequestType(cbxRequestType.getSelectedEntity());
		contractRequest.setOthersValue(txtOthersValue.getValue());
		contractRequest.setComment(txtComment.getValue());
		contractRequest.setProcessed(cbProcessed.getValue());
		contractRequest.setUserLogin(UserSessionManager.getCurrentUser().getLogin());
		NOTE_SRV.saveOrUpdate(contractRequest);
		close();
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		cbxRequestType.setSelectedEntity(null);
		txtOthersValue.setValue("");
		txtComment.setValue("");
		cbProcessed.setValue(false);
	}
	
	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		requestTablePanel.refreshTable();
	}
	
}
