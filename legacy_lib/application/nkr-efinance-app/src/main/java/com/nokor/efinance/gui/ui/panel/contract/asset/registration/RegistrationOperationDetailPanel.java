package com.nokor.efinance.gui.ui.panel.contract.asset.registration;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * Registration operation detail panel
 * @author uhout.cheng
 */
public class RegistrationOperationDetailPanel extends Panel {

	/** */
	private static final long serialVersionUID = -1954828217453094564L;
	
	private static final String MAIN_TEMPLATE = "asset/assetRegistrationDetailLayout";
	private static final String DOCUMENT_PENDING_TEMPLATE = "asset/assetDocumentPendingLayout";

	private TextField txtOperation;
	private TextField txtCreationUser;
	private TextField txtStatus;
	private TextField txtComments;
	private AutoDateField dfCreationDate;
	private AutoDateField dfDeadLine;
	private Label lblCostValue;
	private Label lblTransferToValue;
	private Label lblPriceValue;
	private Label lblAssignedValueTo;
	private Button btnCostDetail;
	private Button btnPriceDetail;
	private Button btnPOAPrint;
	private Button btnDLTFormPrint;
	private Button btnWaive;
	private Button btnAdd;
	private Button btnUpdateInfo;
	private Button btnEdit;
	private CheckBox cbPOA;
	private CheckBox cbDLTForm;
	private CheckBox cbIDCopy;
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public RegistrationOperationDetailPanel() {
		init();
		setCaption(I18N.message("operation.detail"));
		
		btnPriceDetail.setVisible(false);
		btnCostDetail.setVisible(false);
		btnWaive.setVisible(false);
		btnAdd.setVisible(false);
		btnEdit.setVisible(false);
		btnUpdateInfo.setVisible(false);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		horLayout.addComponent(getCustomLayout());
		horLayout.addComponent(getDocumentPendingPanel());
		horLayout.addComponent(getCommentLayout());
		setContent(new VerticalLayout(horLayout));
	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	private Button getButtonLicon(Resource resource) {
		Button button = ComponentFactory.getButton();
		button.setStyleName(Reindeer.BUTTON_LINK);
		button.setIcon(resource);
		return button;
	}
	
	/**
	 * 
	 */
	private void init() {
		txtOperation = ComponentFactory.getTextField(60, 120);
		txtCreationUser = ComponentFactory.getTextField(60, 120);
		txtStatus = ComponentFactory.getTextField(60, 120);
		txtComments = ComponentFactory.getTextField(150, 300);
		dfCreationDate = ComponentFactory.getAutoDateField();
		dfDeadLine = ComponentFactory.getAutoDateField();
		btnWaive = ComponentLayoutFactory.getButtonStyle("waive", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnAdd = ComponentLayoutFactory.getButtonStyle("add", FontAwesome.PLUS, 80, "btn btn-success button-small");
		btnUpdateInfo = ComponentLayoutFactory.getButtonStyle("update.info", FontAwesome.EDIT, 120, "btn btn-success button-small");
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnCostDetail = getButtonLicon(FontAwesome.SEARCH);
		btnPriceDetail = getButtonLicon(FontAwesome.SEARCH);
		btnPOAPrint = getButtonLicon(FontAwesome.PRINT);
		btnDLTFormPrint = getButtonLicon(FontAwesome.PRINT);
		lblCostValue = getLabelValue();
		lblTransferToValue = getLabelValue();
		lblPriceValue = getLabelValue();
		lblAssignedValueTo = getLabelValue();
		cbPOA = new CheckBox(I18N.message("received"));
		cbDLTForm = new CheckBox(I18N.message("received"));
		cbIDCopy = new CheckBox(I18N.message("received"));
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setPageLength(3);
		simpleTable.setSizeUndefined();
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}
	
	/**
	 * 
	 */
	public void assignValues() {
		lblCostValue.setValue("N/A");
		lblTransferToValue.setValue("N/A");
		lblPriceValue.setValue("N/A");
		lblAssignedValueTo.setValue("N/A");
		txtOperation.setValue("");
		txtCreationUser.setValue("");
		txtStatus.setValue("");
		dfCreationDate.setValue(null);
		dfDeadLine.setValue(null);
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getCustomLayout() {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(MAIN_TEMPLATE);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(MAIN_TEMPLATE), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("operation")), "lblOperation");
		customLayout.addComponent(txtOperation, "txtOperation");
		customLayout.addComponent(ComponentFactory.getLabel("cost"), "lblCost");
		customLayout.addComponent(lblCostValue, "lblCostValue");
		customLayout.addComponent(btnCostDetail, "btnCostDetail");
		customLayout.addComponent(ComponentFactory.getLabel("transfer.to"), "lblTransferTo");
		customLayout.addComponent(lblTransferToValue, "lblTransferToValue");
		customLayout.addComponent(ComponentFactory.getLabel("creation.date"), "lblCreationDate");
		customLayout.addComponent(dfCreationDate, "dfCreationDate");
		customLayout.addComponent(ComponentFactory.getLabel("price"), "lblPrice");
		customLayout.addComponent(lblPriceValue, "lblPriceValue");
		customLayout.addComponent(btnPriceDetail, "btnPriceDetail");
		customLayout.addComponent(btnWaive, "btnWaive");
		customLayout.addComponent(ComponentFactory.getLabel("creation.user"), "lblCreationUser");
		customLayout.addComponent(txtCreationUser, "txtCreationUser");
		customLayout.addComponent(ComponentFactory.getLabel("assigned.to"), "lblAssignedTo");
		customLayout.addComponent(lblAssignedValueTo, "lblAssignedToValue");
		customLayout.addComponent(ComponentFactory.getLabel("deadline"), "lblDeadLine");
		customLayout.addComponent(dfDeadLine, "dfDeadLine");
		customLayout.addComponent(ComponentFactory.getLabel("status"), "lblStatus");
		customLayout.addComponent(txtStatus, "txtStatus");
		return customLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getDocumentPendingPanel() {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(DOCUMENT_PENDING_TEMPLATE);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(DOCUMENT_PENDING_TEMPLATE), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(new Label(I18N.message("poa")), "lblPOA");
		customLayout.addComponent(cbPOA, "cbPOA");
		customLayout.addComponent(btnPOAPrint, "btnPOAPrint");
		customLayout.addComponent(ComponentFactory.getLabel("dlt.form"), "lblDLTForm");
		customLayout.addComponent(cbDLTForm, "cbDLTForm");
		customLayout.addComponent(btnDLTFormPrint, "btnDLTFormPrint");
		customLayout.addComponent(ComponentFactory.getLabel("id.copy"), "lblIDCopy");
		customLayout.addComponent(cbIDCopy, "cbIDCopy");
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, false);
		verLayout.addComponent(customLayout);
		Panel panel = new Panel(I18N.message("document.pending"), verLayout);
		return panel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getCommentsPanel() {
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(txtComments);
		horLayout.addComponent(btnAdd);
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(horLayout);
		verLayout.addComponent(simpleTable);
		
		Panel panel = new Panel(I18N.message("comments"), verLayout);
		return panel;
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getCommentLayout() {
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnUpdateInfo);
		buttonLayout.addComponent(btnEdit);
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, false), true);
		verLayout.addComponent(getCommentsPanel());
		verLayout.addComponent(buttonLayout);
		verLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		return verLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("user.id", I18N.message("user.id"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("fullname", I18N.message("fullname"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("comment", I18N.message("comment"), String.class, Align.LEFT, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 */
	public void reset() {
		lblCostValue.setValue("");
		lblTransferToValue.setValue("");
		lblPriceValue.setValue("");
		lblAssignedValueTo.setValue("");
		txtOperation.setValue("");
		txtCreationUser.setValue("");
		txtStatus.setValue("");
		dfCreationDate.setValue(null);
		dfDeadLine.setValue(null);
	}
	
}
