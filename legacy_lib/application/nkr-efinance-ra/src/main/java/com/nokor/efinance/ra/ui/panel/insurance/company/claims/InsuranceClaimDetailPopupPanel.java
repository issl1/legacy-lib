package com.nokor.efinance.ra.ui.panel.insurance.company.claims;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.financial.model.InsuranceClaims;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class InsuranceClaimDetailPopupPanel extends Window implements SeuksaServicesHelper, ClickListener {
	
	/** */
	private static final long serialVersionUID = 3348265746704891357L;
	
	private InsuranceClaimDetailFormPanel formPanel;
	private VerticalLayout messagePanel;
	private Button btnSave;
	private Button btnCancel;
	
	private ComboBox cbxRangeOfYear;
	private TextField txtFrom;
	private TextField txtTo;
	private TextField txtPremiumRefundedPercentage;
	
	private InsuranceClaims insuranceClaims;
	private Organization insurance;
	
	/**
	 * @param formPanel
	 */
	public InsuranceClaimDetailPopupPanel(InsuranceClaimDetailFormPanel formPanel) {
		setModal(true);
		setWidth(400, Unit.PIXELS);
		this.formPanel = formPanel;
		setCaption(I18N.message("claims"));
		createForm();
	}
	
	/**
	 * Create Form
	 */
	private void createForm() {
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(FontAwesome.BAN);
		
		cbxRangeOfYear = ComponentFactory.getComboBox();
		cbxRangeOfYear.addItem(1);
		cbxRangeOfYear.addItem(2);
		cbxRangeOfYear.setWidth(170, Unit.PIXELS);
		cbxRangeOfYear.setNullSelectionAllowed(false);
		cbxRangeOfYear.select(1);
		txtFrom = ComponentFactory.getTextField(20, 170);
		txtTo = ComponentFactory.getTextField(20, 170);
		txtPremiumRefundedPercentage = ComponentFactory.getTextField(20, 170);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		Label lblRangeOfYear = ComponentFactory.getLabel("range.of.year");
		Label lblFrom = ComponentFactory.getLabel("from");
		Label lblTo = ComponentFactory.getLabel("to");
		Label lblPremiumnRefundedPercentage = ComponentFactory.getLabel("permiumn.refunded.percentage");
		
		GridLayout gridLayout = new GridLayout(2, 4);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(new MarginInfo(false, true, true, true));
		gridLayout.addComponent(lblRangeOfYear);
		gridLayout.addComponent(cbxRangeOfYear);
		gridLayout.addComponent(lblFrom);
		gridLayout.addComponent(txtFrom);
		gridLayout.addComponent(lblTo);
		gridLayout.addComponent(txtTo);
		gridLayout.addComponent(lblPremiumnRefundedPercentage);
		gridLayout.addComponent(txtPremiumRefundedPercentage);
		
		gridLayout.setComponentAlignment(lblRangeOfYear, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblFrom, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblTo, Alignment.MIDDLE_RIGHT);
		gridLayout.setComponentAlignment(lblPremiumnRefundedPercentage, Alignment.MIDDLE_RIGHT);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(gridLayout);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(verticalLayout);
		
		setContent(content);
	}
	
	/**
	 * Assign Values
	 * @param isrClaimId
	 */
	public void assignValues(Long isrClaimId) {
		reset();
		if (isrClaimId != null) {
			insuranceClaims = ENTITY_SRV.getById(InsuranceClaims.class, isrClaimId);
			cbxRangeOfYear.select(insuranceClaims.getRangeOfYear());
			txtFrom.setValue(String.valueOf(MyNumberUtils.getInteger(insuranceClaims.getFrom())));
			txtTo.setValue(String.valueOf(MyNumberUtils.getInteger(insuranceClaims.getTo())));
			txtPremiumRefundedPercentage.setValue(String.valueOf(MyNumberUtils.getDouble(insuranceClaims.getPremiumnRefundedPercentage())));
		}
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		insuranceClaims = InsuranceClaims.createInstance();
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		txtFrom.setValue(StringUtils.EMPTY);
		txtTo.setValue(StringUtils.EMPTY);
		txtPremiumRefundedPercentage.setValue(StringUtils.EMPTY);
		cbxRangeOfYear.select(1);
	}
	
	/**
	 * Save
	 */
	private void save() {
		if (validate()) {
			if (insuranceClaims.getId() == null) {
				insuranceClaims.setInsurance(insurance);
			}
			insuranceClaims.setRangeOfYear(Integer.parseInt(cbxRangeOfYear.getValue().toString()));
			insuranceClaims.setFrom(Integer.parseInt(txtFrom.getValue()));
			insuranceClaims.setTo(Integer.parseInt(txtTo.getValue()));
			insuranceClaims.setPremiumnRefundedPercentage(Double.parseDouble(txtPremiumRefundedPercentage.getValue()));
			ENTITY_SRV.saveOrUpdate(insuranceClaims);
			formPanel.searchButtonClick(null);
			close();
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		boolean valid = true;
		ValidateUtil.clearErrors();
		if (!ValidateUtil.checkIntegerField(txtFrom, "from")) {
			valid = false;
		}
		if (!ValidateUtil.checkIntegerField(txtTo, "to")) {
			valid = false;
		}																  
		if (!ValidateUtil.checkDoubleField(txtPremiumRefundedPercentage, "permiumn.refunded.percentage")) {
			valid = false;
		}
		return valid;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}

	/**
	 * @param insurance the insurance to set
	 */
	public void setInsurance(Organization insurance) {
		this.insurance = insurance;
	}
	
	/**
	 * Display Errors Panel
	 */
	private void displayErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = new Label(ValidateUtil.getErrorMessages(), ContentMode.HTML);
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}

}
