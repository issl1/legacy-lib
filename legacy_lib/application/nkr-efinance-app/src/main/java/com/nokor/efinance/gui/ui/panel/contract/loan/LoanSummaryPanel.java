package com.nokor.efinance.gui.ui.panel.contract.loan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.MContractNote;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.loan.summary.LoanApplicationContractPanel;
import com.nokor.efinance.gui.ui.panel.contract.loan.summary.asset.LoanAssetMainPanel;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * Loan summary panel
 * @author uhout.cheng
 */
public class LoanSummaryPanel extends AbstractTabPanel implements FinServicesHelper, ClickListener {

	/** */
	private static final long serialVersionUID = 7059826312223804004L;
	
	private static final String TYPE = "type";
	private static final String STAFF_IN_CHARGE = "staff.in.charge";
	private static final String DEPARTMENT = "department";
	private static final String PHONE = "phone";
	private static final String SINCE = "since";
	
	private LoanApplicationContractPanel loanApplicationContractPanel;
	private LoanAssetMainPanel loanAssetMainPanel;

	// Current Lock Split
	private SimpleTable<Entity> openTaskTable;
	private SimpleTable<Entity> latestNotesTable;	
		
	private Contract contract;
	
	/**
	 * @return the loanApplicationContractPanel
	 */
	public LoanApplicationContractPanel getLoanApplicationContractPanel() {
		return loanApplicationContractPanel;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		super.setMargin(false);
		loanApplicationContractPanel = new LoanApplicationContractPanel(this);
		loanAssetMainPanel = new LoanAssetMainPanel(this);
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(loanApplicationContractPanel);
		contentLayout.addComponent(loanAssetMainPanel);
		contentLayout.addComponent(getLatestNoteLayout());
		contentLayout.addComponent(getOpenTaskLayout());
		
		
		return contentLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @param icon
	 * @param width
	 * @return
	 */
	public Button getButton(String caption, Resource icon, float width) {
		Button button = ComponentLayoutFactory.getButtonStyle(caption, icon, width, "btn btn-success button-small");
		button.addClickListener(this);
		return button;
	} 
	
	/**
	 * 
	 * @param caption
	 * @param component
	 * @param button
	 * @return
	 */
	public VerticalLayout getVerticalLayout(String caption, Component component, boolean isDisplay) {
		Panel panelLayout = new Panel();
		panelLayout.setStyleName(Reindeer.PANEL_LIGHT);
		panelLayout.setContent(component);
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(panelLayout);
		contentLayout.setVisible(isDisplay);
		
		Label lblTitle = getLabel(caption);
		HorizontalLayout titleLayout = ComponentLayoutFactory.getHorizontalLayout(false, false);
		Button btnGroup = new Button();
		btnGroup.setStyleName(Reindeer.BUTTON_LINK);
		if (isDisplay) {
			btnGroup.setIcon(FontAwesome.CARET_DOWN);
		} else {
			btnGroup.setIcon(FontAwesome.CARET_RIGHT);
		}
		titleLayout.addComponent(btnGroup);
		titleLayout.addComponent(lblTitle);
		
		btnGroup.addClickListener(new ClickListener() {
		
			/** */
			private static final long serialVersionUID = -5177487921778049760L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				contentLayout.setVisible(!contentLayout.isVisible());	
				if (contentLayout.isVisible()) {
					btnGroup.setIcon(FontAwesome.CARET_DOWN);
				} else {
					btnGroup.setIcon(FontAwesome.CARET_RIGHT);
				}
			}
		});
	
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		if (caption != null) {
			verLayout.addComponent(titleLayout);
		}
		verLayout.addComponent(contentLayout);
		return verLayout;
	}
	
	/**
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		Label label = new Label();
		label.setValue("<h2 style=\"margin: 0; color: #398439\" align=\"right\" >" + I18N.message(caption) + "</h2>");
		label.setContentMode(ContentMode.HTML);
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @param component
	 * @param isBorderPanel
	 * @return
	 */
	public Panel getPanelCaptionColor(String caption, Component component, boolean isBorderPanel) {
		Panel panel = new Panel(component);
		panel.setCaptionAsHtml(true);
		panel.setCaption("<h2 style=\"margin: 0; color: #398439\" >" + I18N.message(caption) + "</h2>");
		if (!isBorderPanel) {
			panel.setStyleName(Reindeer.PANEL_LIGHT);
		}
		return panel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getOpenTaskLayout() {
		openTaskTable = getSimpleTable(getOpenTaskColumnDefinitions());
		Panel panel = getPanelCaptionColor("open.task", openTaskTable, false);
		return panel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getLatestNoteLayout() {
		latestNotesTable = getSimpleTable(getLatestNotesColumnDefinitions());
		latestNotesTable.setPageLength(5);
		Panel panel = getPanelCaptionColor("latest.activity", latestNotesTable, false);
		return panel;
	}
		
	/**
	 * 
	 * @param contract
	 * @param contractFormPanel
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;		
		loanApplicationContractPanel.assigValues(contract);
		loanAssetMainPanel.assigValues(contract);		
		// setLockSplitIndexedContainer(LCK_SPL_SRV.getLockSplitByContract(contract.getId()));		
		// Load Latest notes table data
		getLatestNotesTableData(contract);
	}
	
	/**
	 * Load Latest Notes Table Data
	 * @param contract
	 */
	@SuppressWarnings("unchecked")
	public void getLatestNotesTableData(Contract contract) {
		latestNotesTable.removeAllItems();
		Container indexedContainer = latestNotesTable.getContainerDataSource();
		for (ContractNote note : NOTE_SRV.getLatestNotes(contract)) {
			Item item = indexedContainer.addItem(note.getId());
			item.getItemProperty(MContractNote.CREATEDATE).setValue(note.getCreateDate());
			item.getItemProperty(MContractNote.USER).setValue(note.getUserLogin());
			item.getItemProperty(MContractNote.SUBJECT).setValue(note.getSubject());
			item.getItemProperty(MContractNote.COMMENT).setValue(note.getComment());
		}
		
	}
	
	/**
	 * 
	 * @param asset
	 */
	public void assignValuesToAsset(Asset asset) {
		
	}
	
	
	/**
	 * Validate for activated
	 * @return
	 */
	public List<String> validateForActivated() {
		if (contract != null) {
			if (contract.getFirstDueDate() == null) {
				errors.add(I18N.message("field.required.1", I18N.message("first.due.date")));
			}
		}
		return errors;
	}
			
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {		
	}
	
	/**
	 * 
	 * @param template
	 * @return
	 */
	public CustomLayout getCustomLayout(String template) {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(template);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(template), Type.ERROR_MESSAGE);
		}
		return customLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getOpenTaskColumnDefinitions() {
		List<ColumnDefinition> openTaskColumnDefinitions = new ArrayList<ColumnDefinition>();
		openTaskColumnDefinitions.add(new ColumnDefinition(TYPE, I18N.message("type"), Date.class, Align.LEFT, 130));
		openTaskColumnDefinitions.add(new ColumnDefinition(STAFF_IN_CHARGE, I18N.message("staff.in.charge"), String.class, Align.LEFT, 170));
		openTaskColumnDefinitions.add(new ColumnDefinition(DEPARTMENT, I18N.message("department"), Date.class, Align.LEFT, 130));
		openTaskColumnDefinitions.add(new ColumnDefinition(PHONE, I18N.message("phone"), Date.class, Align.LEFT, 130));
		openTaskColumnDefinitions.add(new ColumnDefinition(SINCE, I18N.message("since"), String.class, Align.LEFT, 150));
		return openTaskColumnDefinitions;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getLatestNotesColumnDefinitions() {
		List<ColumnDefinition> latestNotesColumnDefinitions = new ArrayList<ColumnDefinition>();
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.CREATEDATE, I18N.message("date.time"), Date.class, Align.LEFT, 170));
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.USER, I18N.message("user.departments"), String.class, Align.LEFT, 200));
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.SUBJECT, I18N.message("type"), String.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.COMMENT, I18N.message("note"), String.class, Align.LEFT, 150));
		return latestNotesColumnDefinitions;
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		return simpleTable;
	}

	/**
	 * 
	 */
	public void reset() {
		removeErrorsPanel();
		contract = Contract.createInstance();
	}
	
}
