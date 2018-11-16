package com.nokor.efinance.ra.ui.panel.organization.branch.detail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.ra.ui.panel.organization.branch.list.BranchTablePanel;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Branch or Department Popup Panel
 * @author bunlong.taing
 */
public class BranchPopupPanel extends Window implements ClickListener, FinServicesHelper {
	/** */
	private static final long serialVersionUID = 3165445004407272343L;
	
	private TextField txtCode;
	private TextField txtName;
	private TextField txtNameEn;
	private TextField txtTel;
	private TextField txtMobile;
	private TextField txtEMail;
	private TextField txtFax;
	
	private ERefDataComboBox<EOrgLevel> cbxLevel;
	
	private Button btnSave;
	private Button btnCancel;
	
	private VerticalLayout messagePanel;
	private OrgStructure branch;
	private BranchTablePanel tablePanel;
	
	/**
	 * Branch Popup Panel
	 * @param caption
	 */
	public BranchPopupPanel(String caption, BranchTablePanel tablePanel) {
		super(I18N.message(caption));
		this.tablePanel = tablePanel;
		initSize();
		setModal(true);
		setResizable(false);
		createForm();
	}

	/**
	 * Create Branch Popup Panel form
	 */
	private void createForm() {
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		
		txtCode = ComponentFactory.getTextField("code", true, 100, 200);
		txtName = ComponentFactory.getTextField("name", true, 100, 200);
		txtNameEn = ComponentFactory.getTextField("name.en", true, 100, 200);
		txtTel = ComponentFactory.getTextField("tel", false, 10, 200);
		txtMobile = ComponentFactory.getTextField("mobile", false, 10, 200);
		txtEMail = ComponentFactory.getTextField("email", false, 100, 200);
		txtFax = ComponentFactory.getTextField("fax", false, 100, 200);
		
		List<EOrgLevel> levels = new ArrayList<EOrgLevel>();
		levels.add(EOrgLevel.BRANCH);
		levels.add(EOrgLevel.HEAD_OFFICE);
		cbxLevel = new ERefDataComboBox<EOrgLevel>(I18N.message("level"), levels);
		cbxLevel.setRequired(true);
		cbxLevel.setWidth(200, Unit.PIXELS);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(txtCode);
		if (EOrgLevel.BRANCH.equals(tablePanel.getLavel())) {
			formLayout.addComponent(cbxLevel);
		}
		formLayout.addComponent(txtName);
		formLayout.addComponent(txtNameEn);
		formLayout.addComponent(txtTel);
		formLayout.addComponent(txtMobile);
		if (EOrgLevel.BRANCH.equals(tablePanel.getLavel())) {
			formLayout.addComponent(txtFax);
		}
		formLayout.addComponent(txtEMail);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(formLayout);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		content.addComponent(navigationPanel);
		content.addComponent(verticalLayout);
		
		setContent(content);
	}
	
	/**
	 * Init Popup Size
	 */
	private void initSize() {
		if (EOrgLevel.BRANCH.equals(tablePanel.getLavel())) {
			setWidth(400, Unit.PIXELS);
			setHeight(350, Unit.PIXELS);
		} else if (EOrgLevel.DEPARTMENT.equals(tablePanel.getLavel())) {
			setWidth(400, Unit.PIXELS);
			setHeight(300, Unit.PIXELS);
		}
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		branch = OrgStructure.createInstance();
		branch.setOrganization(ORG_STRUC_SRV.getById(Organization.class, tablePanel.getOrganizationId()));
		
		if (EOrgLevel.BRANCH.equals(tablePanel.getLavel())) {
			branch.setLevel(EOrgLevel.BRANCH);
		} else if (EOrgLevel.DEPARTMENT.equals(tablePanel.getLavel())) {
			branch.setLevel(EOrgLevel.DEPARTMENT);
		}
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		txtCode.setValue("");
		txtName.setValue("");
		txtNameEn.setValue("");
		txtTel.setValue("");
		txtMobile.setValue("");
		txtFax.setValue("");
		txtEMail.setValue("");
		cbxLevel.setSelectedEntity(null);
	}
	
	/**
	 * Assign Values to form
	 * @param branchId
	 */
	public void assignValues(Long branchId) {
		reset();
		if (branchId != null) {
			branch = ORG_STRUC_SRV.getById(OrgStructure.class, branchId);
			txtCode.setValue(branch.getCode() != null ? branch.getCode() : "");
			cbxLevel.setSelectedEntity(branch.getLevel() != null ? branch.getLevel() : null);
			txtName.setValue(branch.getName() != null ? branch.getName() : "");
			txtNameEn.setValue(branch.getNameEn() != null ? branch.getNameEn() : "");
			txtTel.setValue(branch.getTel() != null ? branch.getTel() : "");
			txtMobile.setValue(branch.getMobile() != null ? branch.getMobile() : "");
			txtFax.setValue(branch.getFax() != null ? branch.getFax() : "");
			txtEMail.setValue(branch.getEmail() != null ? branch.getEmail() : "");
		}
	}
	
	/**
	 * Show the window
	 */
	public void show() {
		UI.getCurrent().addWindow(this);
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
	 * Save
	 */
	private void save() {
		if (validate()) {
			buildBranchDetailsFromControls(branch);
			ORG_STRUC_SRV.saveOrUpdate(branch);
			tablePanel.refresh();
			close();
		} else {
			displayErrorsPanel();
		}
	}
	
	/**
	 * Build Branch Details From Controls
	 * @param branch
	 */
	private void buildBranchDetailsFromControls(OrgStructure branch) {
		branch.setCode(txtCode.getValue());
		if (!EOrgLevel.DEPARTMENT.equals(tablePanel.getLavel())) {
			branch.setLevel(cbxLevel.getSelectedEntity());
		}
		branch.setName(txtName.getValue());
		branch.setNameEn(txtNameEn.getValue());
		branch.setTel(txtTel.getValue());
		branch.setMobile(txtMobile.getValue());
		branch.setFax(txtFax.getValue());
		branch.setEmail(txtEMail.getValue());
	}
	
	/**
	 * Validate
	 * @return
	 */
	private boolean validate() {
		boolean isValid = true;
		ValidateUtil.clearErrors();
		
		ValidateUtil.checkMandatoryField(txtCode, "code");
		ValidateUtil.checkMandatoryField(txtName, "name");
		ValidateUtil.checkMandatoryField(txtNameEn, "name.en");
		
		if (EOrgLevel.BRANCH.equals(tablePanel.getLavel())) {
			ValidateUtil.checkMandatorySelectField(cbxLevel, "level");
		}
		if (!StringUtils.isEmpty(ValidateUtil.getErrorMessages())) {
			isValid = false;
		} 
		return isValid;
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
