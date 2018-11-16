package com.nokor.efinance.gui.ui.panel.contract.notes.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractRequest;
import com.nokor.efinance.core.contract.model.ERequestType;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Request form panel
 * @author uhout.cheng
 */
public class ColRequestFormPanel extends VerticalLayout implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 2507541812520319226L;

	private final static Logger logger = LoggerFactory.getLogger(ColRequestFormPanel.class);
	
	private ColRequestTablePanel requestTablePanel;
	private Button btnSave;
	
	private CheckBox cbProcessed;
	private ERefDataComboBox<ERequestType> cbxRequestType;
	private TextArea txtComment;
	private TextField txtOthersValue;
	
	private Contract contract;
	
	private VerticalLayout messagePanel;
	
	private ContractRequest contractRequest;
	
	/**
	 * 
	 * @param caption
	 */
	public ColRequestFormPanel(String caption) {
		if (!"null".equals(caption) && StringUtils.isNotEmpty(caption)) {
			setCaption("<b>" + I18N.message(caption) + "</b>");
		}
		setCaptionAsHtml(true);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		requestTablePanel = new ColRequestTablePanel(this);
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		setSpacing(true);
		addComponent(messagePanel);
		addComponent(createForm());
		addComponent(requestTablePanel);
	}

	/** */
	public Component createForm() {
		cbxRequestType = new ERefDataComboBox<>(ERequestType.values());
		cbxRequestType.setImmediate(true);
		cbxRequestType.setWidth(150, Unit.PIXELS);
		Label lblOthersValue = ComponentLayoutFactory.getLabelCaptionRequired("others");;
		lblOthersValue.setVisible(false);
		cbxRequestType.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5988964619470082403L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				txtOthersValue.setVisible(ERequestType.OTH.equals(cbxRequestType.getSelectedEntity()));
				lblOthersValue.setVisible(ERequestType.OTH.equals(cbxRequestType.getSelectedEntity()));
			}
		});
		
		txtOthersValue = ComponentFactory.getTextField(false, 100, 220);
		txtOthersValue.setVisible(false);
		
		Label lblComment = new Label(I18N.message("comment"));
		Label lblRequest = ComponentLayoutFactory.getLabelCaptionRequired("request");;
		
		txtComment = ComponentFactory.getTextArea(false, 250, 50);
		cbProcessed = new CheckBox(I18N.message("processed"));
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		
		GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblRequest, 0, 0);
		gridLayout.addComponent(cbxRequestType, 1, 0);
		gridLayout.addComponent(lblOthersValue, 2, 0);
		gridLayout.addComponent(txtOthersValue, 3, 0);
		gridLayout.addComponent(lblComment , 4, 0);
		gridLayout.addComponent(txtComment, 5, 0);
		gridLayout.addComponent(cbProcessed, 6, 0);
		gridLayout.addComponent(btnSave, 7, 0);
		
		gridLayout.setComponentAlignment(lblRequest, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblComment, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbProcessed, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbxRequestType, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblOthersValue, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(txtOthersValue, Alignment.MIDDLE_LEFT);
		
		Panel maninPanel = new Panel(gridLayout);
		return maninPanel;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		requestTablePanel.assignValues(contract);
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
	
	/** */
	public void reset() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		contractRequest = new ContractRequest();
		cbxRequestType.setSelectedEntity(null);
		txtOthersValue.setValue("");
		txtComment.setValue("");
		cbProcessed.setValue(false);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		 if (event.getButton() == btnSave) {
			if (validate()) {
				save();
			}
		}
	} 

	/** */
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
		try {
			NOTE_SRV.saveOrUpdateContractRequest(contractRequest);
			ComponentLayoutFactory.displaySuccessfullyMsg();
			requestTablePanel.assignValues(contract);
			reset();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		Label messageLabel;

		if (cbxRequestType.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("request") }));
		} else if (cbxRequestType.getSelectedEntity().equals(ERequestType.OTH)) {
			if (StringUtils.isEmpty(txtOthersValue.getValue())) {
				errors.add(I18N.message("field.required.1",new String[] { I18N.message("others") }));
			}
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
	
}
