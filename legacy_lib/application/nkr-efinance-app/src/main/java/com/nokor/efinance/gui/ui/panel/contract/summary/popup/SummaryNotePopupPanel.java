package com.nokor.efinance.gui.ui.panel.contract.summary.popup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.contract.model.MContractNote;
import com.nokor.efinance.core.contract.service.ContractNoteRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanSummaryPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Summary note pop up panel
 * @author uhout.cheng
 */
public class SummaryNotePopupPanel extends Window implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -4941044691098892778L;
	
	private TextField txtSubject;
	private TextField txtNote;
	private TextField txtComment;
	private SimpleTable<Entity> simpleTable;
	
	private Button btnSave;
	private Button btnClose;
	private VerticalLayout messagePanel;
	
	private Contract contract;
	private LoanSummaryPanel loanSummaryPanel;
	
	/**
	 */
	public SummaryNotePopupPanel(Contract contract, LoanSummaryPanel loanSummaryPanel) {
		this.contract = contract;
		this.loanSummaryPanel = loanSummaryPanel;
		setCaption(I18N.message("note"));
		setModal(true);
		setResizable(false);
		init();
	}
	
	/**
	 */
	private void init() {
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
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
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(createForm());
	    
		setContent(contentLayout);
	}
	
	/**
	 * @return
	 */
	private VerticalLayout createForm() {
		txtSubject = ComponentFactory.getTextField("subject", false, 100, 150);
		txtNote = ComponentFactory.getTextField("note", false, 100, 150);
		txtComment = ComponentFactory.getTextField("detail", false, 100, 150);
		
		simpleTable = new SimpleTable<Entity>(I18N.message("latest.notes"), getLatestNotesColumnDefinitions());
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(3);
		refreshTable();
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setSpacing(true);
		horLayout.addComponent(new FormLayout(txtSubject));
		horLayout.addComponent(new FormLayout(txtNote));
		horLayout.addComponent(new FormLayout(txtComment));
		
		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		content.setSpacing(true);
		
		content.addComponent(horLayout);
		content.addComponent(simpleTable);
		return content;
	}
	
	/**
	 * @return
	 */
	private List<ColumnDefinition> getLatestNotesColumnDefinitions() {
		List<ColumnDefinition> latestNotesColumnDefinitions = new ArrayList<ColumnDefinition>();
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.CREATEDATE, I18N.message("date"), Date.class, Align.LEFT, 130));
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.USER, I18N.message("user"), String.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.NOTE, I18N.message("note"), String.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.SUBJECT, I18N.message("subject"), String.class, Align.LEFT, 150));
		latestNotesColumnDefinitions.add(new ColumnDefinition(ContractNote.COMMENT, I18N.message("comment"), String.class, Align.LEFT, 270));
		return latestNotesColumnDefinitions;
	}
	
	/**
	 * @param notes
	 */
	@SuppressWarnings("unchecked")
	private void setTableIndexedContainer(List<ContractNote> notes) {
		simpleTable.removeAllItems();
		Container indexedContainer = simpleTable.getContainerDataSource();
		for (ContractNote note : notes) {
			Item item = indexedContainer.addItem(note.getId());
			item.getItemProperty(MContractNote.CREATEDATE).setValue(note.getCreateDate());
			item.getItemProperty(MContractNote.USER).setValue(note.getUserLogin());
			item.getItemProperty(MContractNote.NOTE).setValue(note.getNote());
			item.getItemProperty(MContractNote.SUBJECT).setValue(note.getSubject());
			item.getItemProperty(MContractNote.COMMENT).setValue(note.getComment());
		}
	}
	
	/**
	 */
	public void refreshTable() {
		if (contract != null) {
			ContractNoteRestriction restrictions = new ContractNoteRestriction();
			restrictions.setConId(contract.getId());
			restrictions.setOrder(Order.desc(ContractNote.CREATEDATE));
			setTableIndexedContainer(CONT_SRV.list(restrictions));
		}
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
		ContractNote note = new ContractNote();
		note.setContract(contract);
		note.setSubject(txtSubject.getValue());
		note.setNote(txtNote.getValue());
		note.setComment(txtComment.getValue());
		note.setUserLogin(UserSessionManager.getCurrentUser().getLogin());
		CONT_SRV.saveOrUpdate(note);
		reset();
		displaySuccess();
		refreshTable();
		if (loanSummaryPanel != null) {
			loanSummaryPanel.getLatestNotesTableData(contract);
		}
	}
	
	/**
	 * Reset the form
	 */
	public void reset() {
		txtSubject.setValue("");
		txtNote.setValue("");
		txtComment.setValue("");
	}

	/**
	 */
	protected void displaySuccess() {
		Label messageLabel = new Label(I18N.message("msg.info.save.successfully"));
		messageLabel.addStyleName("success");
		Label iconLabel = new Label();
		iconLabel.setIcon(new ThemeResource("../nkr-default/icons/16/twitter.png"));
		messagePanel.removeAllComponents();
		messagePanel.addComponent(new HorizontalLayout(iconLabel, messageLabel));
		messagePanel.setVisible(true);
	}
	
}
