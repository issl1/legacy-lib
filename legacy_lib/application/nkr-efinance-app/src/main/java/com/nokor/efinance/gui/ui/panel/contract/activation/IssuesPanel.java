package com.nokor.efinance.gui.ui.panel.contract.activation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.issue.model.ContractIssue;
import com.nokor.efinance.core.issue.model.EIssueAttribute;
import com.nokor.efinance.core.issue.model.EIssueDocument;
import com.nokor.efinance.core.issue.model.EIssueType;
import com.nokor.efinance.core.issue.model.MContractIssue;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
/**
 * 
 * @author buntha.chea
 *
 */
public class IssuesPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper, MContractIssue {

	/** */
	private static final long serialVersionUID = -1642627189943903832L;
	
	private Button btnAddIssue;
	
	private ERefDataComboBox<EIssueType> cbxIssueType;
	private ERefDataComboBox<EIssueAttribute> cbxIssueAttribute;
	private ERefDataComboBox<EIssueDocument> cbxIssueDocument1;
	private ERefDataComboBox<EIssueDocument> cbxIssueDocument2;
	private TextField txtComment;
	
	private SimplePagedTable<Entity> pageTable;
	private List<ColumnDefinition> columnDefinitions;	
	
	private Button btnSave;
	private Panel issueDetailPanel;
	private VerticalLayout issueTableLayout;
	private Panel issuePanel;
	
	private VerticalLayout messagePanel;
	private Contract contract;
	
	/**
	 * 
	 */
	public IssuesPanel() {
		
		btnAddIssue = new NativeButton(I18N.message("add.issue"));
		btnAddIssue.addClickListener(this);
		btnAddIssue.addStyleName("btn btn-success button-small");
		btnAddIssue.setIcon(FontAwesome.PLUS_CIRCLE);
		
		cbxIssueType = new ERefDataComboBox<>(EIssueType.values());
		cbxIssueType.setWidth(170, Unit.PIXELS);
		cbxIssueAttribute = new ERefDataComboBox<>(new ArrayList<>());
		cbxIssueAttribute.setWidth(170, Unit.PIXELS);
		
		cbxIssueType.addValueChangeListener(new ValueChangeListener() {
			
			/**  */
			private static final long serialVersionUID = 4610919026647329456L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				cbxIssueAttribute.assignValueMap(EIssueAttribute.values(cbxIssueType.getSelectedEntity()));
				
				if (EIssueType.MISTAKES.equals(cbxIssueType.getSelectedEntity())) {
					cbxIssueDocument1.assignValueMap(EIssueDocument.values());
					cbxIssueDocument2.setEnabled(false);
					cbxIssueDocument2.setSelectedEntity(null);
				} else if (EIssueType.DISCREPANCY.equals(cbxIssueType.getSelectedEntity())) {
					cbxIssueDocument1.assignValueMap(EIssueDocument.values());
					cbxIssueDocument2.setEnabled(true);
					cbxIssueDocument2.assignValueMap(EIssueDocument.values());
				}  else if (EIssueType.MISSING_OR_WRONG_DOCUMENT.equals(cbxIssueType.getSelectedEntity())
							|| EIssueType.UNCLEAR_DOCUMENT.equals(cbxIssueType.getSelectedEntity())) {
					cbxIssueDocument1.assignValueMap(EIssueDocument.values());
					cbxIssueDocument2.setEnabled(false);
					cbxIssueDocument2.setSelectedEntity(null);
				} else {
					cbxIssueDocument1.assignValueMap(EIssueDocument.values());
					cbxIssueDocument2.setEnabled(false);
					cbxIssueDocument2.setSelectedEntity(null);
				}
			}
		});
		cbxIssueDocument1 = new ERefDataComboBox<>(new ArrayList<>());
		cbxIssueDocument1.setWidth(170, Unit.PIXELS);
		cbxIssueDocument2 = new ERefDataComboBox<>(new ArrayList<>());
		cbxIssueDocument2.setWidth(170, Unit.PIXELS);
		
		txtComment = ComponentFactory.getTextField35("", false, 200, 170);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.addClickListener(this);
		btnSave.addStyleName("btn btn-success button-small");
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.setWidth("80px");
		
		this.columnDefinitions = createColumnDefinitions();
		pageTable = new SimplePagedTable<Entity>(this.columnDefinitions);
		pageTable.setPageLength(5);
		
		String template = "issueLayout";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout issueLayout = null;
		try {
			issueLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		issueLayout.addComponent(new Label(I18N.message("issue.type")), "lblIssueType");
		issueLayout.addComponent(cbxIssueType, "cbxIssueType");
		
		issueLayout.addComponent(new Label(I18N.message("document.1")), "lblDocument1");
		issueLayout.addComponent(cbxIssueDocument1, "cbxIssueDocument1");
		issueLayout.addComponent(new Label(I18N.message("attribute")), "lblAdttribute");
		issueLayout.addComponent(cbxIssueAttribute, "cbxIssueAttribute");
		issueLayout.addComponent(new Label(I18N.message("document.2")), "lblDocument2");
		issueLayout.addComponent(cbxIssueDocument2, "cbxIssueDocument2");
		issueLayout.addComponent(new Label(I18N.message("comment")), "lblcomment");
		issueLayout.addComponent(txtComment, "txtComment");
		
		VerticalLayout issueFormLayout = new VerticalLayout();
		issueFormLayout.setSpacing(true);
		issueFormLayout.setMargin(true);
		issueFormLayout.addComponent(issueLayout);
		issueFormLayout.addComponent(btnSave);
		issueFormLayout.setComponentAlignment(btnSave, Alignment.BOTTOM_RIGHT);
		
		issueDetailPanel = new Panel();
		issueDetailPanel.setContent(issueFormLayout);
		issueDetailPanel.setVisible(false);
		
		issueTableLayout = new VerticalLayout();
		issueTableLayout.setSpacing(true);
		issueTableLayout.addComponent(pageTable);
		issueTableLayout.addComponent(pageTable.createControls());
		pageTable.setPageLength(5);
		issueTableLayout.setVisible(false);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(issueDetailPanel);
		contentLayout.addComponent(issueTableLayout);
				
		issuePanel = new Panel();
		issuePanel.setCaption(I18N.message("issue"));
		issuePanel.setContent(contentLayout);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(btnAddIssue);
		mainLayout.setComponentAlignment(btnAddIssue, Alignment.TOP_RIGHT);
		mainLayout.addComponent(issuePanel);
		
		addComponent(mainLayout);
	}
	
	/**
	 * Validate 
	 * @return
	 */
	private boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		Label messageLabel;

		if (cbxIssueType.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("issue.type") }));
		}
		if (cbxIssueDocument1.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("document.1") }));
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
	 * Create Column Definitions
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(CREATEDATE, I18N.message("date"), Date.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(CREATEUSER, I18N.message("user"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(ISSUETYPE, I18N.message("issue"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(COMMENT, I18N.message("comment"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(DATEFIXED, I18N.message("date.fixed"), Date.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(REMARK, I18N.message("remark"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(ACTION, I18N.message("action"), Button.class, Align.CENTER, 60));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param schedules
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(List<ContractIssue> contractIssues) {
		IndexedContainer indexedContainer = new IndexedContainer();
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		for (ContractIssue contractIssue : contractIssues) {
			String issueType = "";
			String issueAttribute = "";
			String issueDocument1 = "";
			String issueDocument2 = "";
			String issue = "";
			
			Item item = indexedContainer.addItem(contractIssue.getId());
			item.getItemProperty(CREATEDATE).setValue(contractIssue.getCreateDate());
			item.getItemProperty(CREATEUSER).setValue(contractIssue.getCreateUser());
			if (contractIssue.getIssueType() != null) {
				issueType = contractIssue.getIssueType().getDescEn();
			} 
			if (contractIssue.getIssueAttribute() != null) {
				issueAttribute = " -> " + contractIssue.getIssueAttribute().getDescEn();
			}
			if (contractIssue.getIssueDocument1() != null) {
				issueDocument1 = " -> " + contractIssue.getIssueDocument1().getDescEn();
			}
			if (contractIssue.getIssueDocument2() != null) {
				issueDocument2 = " -> " + contractIssue.getIssueDocument2().getDescEn();
			}
			issue = issueType + issueAttribute + issueDocument1 + issueDocument2;
			item.getItemProperty(ISSUETYPE).setValue(issue);
			item.getItemProperty(COMMENT).setValue(contractIssue.getComment());
			item.getItemProperty(DATEFIXED).setValue(contractIssue.getDateFixed());
			item.getItemProperty(REMARK).setValue(contractIssue.getRemark());
			item.getItemProperty(ACTION).setValue(new IssueReCapButton(contractIssue.getId()));
		}
		return indexedContainer;
	}
		
	/**
	 * 
	 * @param conIsuId
	 */
	private void createWindowPopup(Long conIsuId) {
		Window window = new Window(I18N.message("issue.recap"));
		window.setModal(true);
		window.setResizable(false);
		
		ContractIssue contractIssue = ENTITY_SRV.getById(ContractIssue.class, conIsuId);
		
		TextArea txtComment = ComponentFactory.getTextArea("comment", false, 250, 100);
		TextArea txtRemark = ComponentFactory.getTextArea("remark", false, 250, 100);
		CheckBox cbFixed = new CheckBox(I18N.message("fixed"));
		CheckBox cbForced = new CheckBox(I18N.message("forced"));
		
		txtComment.setValue(getDefaultString(contractIssue.getComment()));
		txtRemark.setValue(getDefaultString(contractIssue.getRemark()));
		cbFixed.setValue(contractIssue.isFixed());
		cbForced.setValue(contractIssue.isForced());
		
		Button btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 8556944318552563331L;

			@Override
			public void buttonClick(ClickEvent event) {
				contractIssue.setComment(txtComment.getValue());
				contractIssue.setRemark(txtRemark.getValue());
				contractIssue.setDateFixed(cbFixed.getValue() ? DateUtils.today() : null);
				contractIssue.setFixed(cbFixed.getValue());
				contractIssue.setForced(cbForced.getValue());
				ENTITY_SRV.saveOrUpdate(contractIssue);
				ENTITY_SRV.refresh(contract);
				assignValue(contract);
				window.close();
			}
		});
		
		Button btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(new ClickListener() {
	
			private static final long serialVersionUID = -717873095422590217L;

			@Override
			public void buttonClick(ClickEvent event) {
				window.close();
			}
		});
		
		FormLayout formLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		formLayout.addComponent(txtComment);
		formLayout.addComponent(txtRemark);
		
		HorizontalLayout checkBoxLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		checkBoxLayout.addComponent(cbFixed);
		checkBoxLayout.addComponent(cbForced);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeUndefined();
		verticalLayout.setMargin(true);
		verticalLayout.addComponent(formLayout);
		verticalLayout.addComponent(checkBoxLayout);
		verticalLayout.setComponentAlignment(checkBoxLayout, Alignment.BOTTOM_CENTER);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(verticalLayout);
		
		window.setContent(mainLayout);
		UI.getCurrent().addWindow(window);	
	}
	
	/**
	 * Assign Value
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		this.contract = contract;
		List<ContractIssue> contractIssues = contract.getContractIssues();
		pageTable.setContainerDataSource(getIndexedContainer(contractIssues));
		issueTableLayout.setVisible(!contractIssues.isEmpty());
		issuePanel.setVisible(!contractIssues.isEmpty());
	}
	
	
	/**
	 * save issue to contractIssue
	 */
	private void doAddIssue() {
		if (validate()) {
			if (contract != null) {
				ContractIssue contractIssue = new ContractIssue();
				contractIssue.setContract(contract);
				contractIssue.setIssueType(cbxIssueType.getSelectedEntity());
				contractIssue.setIssueAttribute(cbxIssueAttribute.getSelectedEntity());
				contractIssue.setIssueDocument1(cbxIssueDocument1.getSelectedEntity());
				contractIssue.setIssueDocument2(cbxIssueDocument2.getSelectedEntity());
				contractIssue.setComment(txtComment.getValue());
				ENTITY_SRV.saveOrUpdate(contractIssue);
				ENTITY_SRV.refresh(contract);
				assignValue(contract);
				displaySuccessMsg();
				reset();
			}
			issueTableLayout.setVisible(true);
			issueDetailPanel.setVisible(false);
			assignValue(contract);
		}
	}
	
	/**
	 * reset
	 */
	protected void reset() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		cbxIssueType.setSelectedEntity(null);
		cbxIssueAttribute.setSelectedEntity(null);
		cbxIssueDocument1.setSelectedEntity(null);
		cbxIssueDocument2.setSelectedEntity(null);
		txtComment.setValue("");
	}
	
	/**
	 * display success 
	 */
	private void displaySuccessMsg() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		iconLabel.addStyleName("success-icon");
		messagePanel.removeAllComponents();
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.addComponent(iconLabel);
		layout.addComponent(messageLabel);
		messagePanel.addComponent(layout);
		messagePanel.setVisible(true);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			doAddIssue();
		} else if (event.getButton() == btnAddIssue) {
			issuePanel.setVisible(true);
			issueDetailPanel.setVisible(true);
		}
	}
	
	/**
	 * @author buntha.chea
	 */
	private class IssueReCapButton extends NativeButton {
		
		private static final long serialVersionUID = 5331682229749631545L;

		public IssueReCapButton(Long conIsuId) {
			super("");
			setIcon(FontAwesome.COG);
			setStyleName(Reindeer.BUTTON_LINK);
			addClickListener(new ClickListener() {
				private static final long serialVersionUID = 4769888412014416921L;
				@Override
				public void buttonClick(ClickEvent event) {
					createWindowPopup(conIsuId);
				}
			});
		}
	}

}
