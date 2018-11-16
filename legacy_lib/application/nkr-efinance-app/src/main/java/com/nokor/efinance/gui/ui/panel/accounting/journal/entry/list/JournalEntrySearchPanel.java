package com.nokor.efinance.gui.ui.panel.accounting.journal.entry.list;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.ersys.finance.accounting.model.Journal;
import com.nokor.ersys.finance.accounting.model.JournalEntry;
import com.nokor.ersys.finance.accounting.service.JournalEntryRestriction;
import com.nokor.ersys.finance.accounting.tools.helper.ErsysAccountingAppServicesHelper;
import com.nokor.ersys.finance.accounting.workflow.JournalEntryWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * JournalEntry Search Panel
 * @author bunlong.taing
 */
public class JournalEntrySearchPanel extends AbstractSearchPanel<JournalEntry> implements ErsysAccountingAppServicesHelper {
	/** */
	private static final long serialVersionUID = -3514363314722675994L;
	
	private TextField txtSearchText;
	
	private List<CheckBox> cbStatuses;
	private List<CheckBox> cbEvents;

	/**
	 * @param tablePanel
	 */
	public JournalEntrySearchPanel(JournalEntryTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		Label lblSearchText = ComponentFactory.getLabel("search.text");
		txtSearchText = ComponentFactory.getTextField(100, 200);
		
		GridLayout gridLayout = new GridLayout(2, 1);
		gridLayout.setSpacing(true);
		gridLayout.addComponent(lblSearchText);
		gridLayout.addComponent(txtSearchText);
		gridLayout.setComponentAlignment(lblSearchText, Alignment.MIDDLE_LEFT);
		
		VerticalLayout content = new VerticalLayout();
		content.setSpacing(true);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(createStatusLayout());
		horizontalLayout.addComponent(createJournalLayout());
		
		content.addComponent(gridLayout);
		content.addComponent(horizontalLayout);
		
		return content;
	}
	
	/**
	 * @return
	 */
	private Panel createStatusLayout() {
		cbStatuses = new ArrayList<CheckBox>();
		HorizontalLayout layout = ComponentFactory.getHorizontalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		
		for (EWkfStatus status : JournalEntryWkfStatus.values()) {
			CheckBox checkBox = new CheckBox(status.getDescLocale());
			checkBox.setData(status);
			layout.addComponent(checkBox);
			cbStatuses.add(checkBox);
		}
		Panel panel = new Panel(I18N.message("status"));
		panel.setContent(layout);
		return panel;
	}
	
	/**
	 * Create Journal Layout
	 * @return
	 */
	private Component createJournalLayout() {
		cbEvents = new ArrayList<CheckBox>();
		HorizontalLayout horizontalLayout = ComponentFactory.getHorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.setMargin(true);
		
		BaseRestrictions<Journal> restrictions = new BaseRestrictions<Journal>(Journal.class);
		restrictions.addOrder(Order.asc(Journal.SORTINDEX));
		for (Journal journal : ACCOUNTING_SRV.list(restrictions)) {
			CheckBox checkBox = new CheckBox();
			checkBox.setCaption(journal.getDescLocale());
			checkBox.setData(journal);
			cbEvents.add(checkBox);
			horizontalLayout.addComponent(checkBox);
		}
		Panel panel = new Panel(I18N.message("event"));
		panel.setContent(horizontalLayout);
		return panel;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<JournalEntry> getRestrictions() {
		JournalEntryRestriction restrictions = new JournalEntryRestriction();
		restrictions.setSearchText(txtSearchText.getValue());
		for (CheckBox cbStatus : cbStatuses) {
			if (cbStatus.getValue()) {
				restrictions.getWkfStatusList().add((EWkfStatus) cbStatus.getData());
			}
		}
		List<Journal> journals = getSelectedJournal();
		if (!journals.isEmpty()) {
			restrictions.setJournals(journals);
		}
		return restrictions;
	}
	
	/**
	 * @return
	 */
	private List<Journal> getSelectedJournal() {
		List<Journal> journals = new ArrayList<Journal>();
		for (CheckBox checkBox : cbEvents) {
			if (checkBox.getValue()) {
				journals.add((Journal) checkBox.getData());
			}
		}
		return journals;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtSearchText.setValue("");
		resetStatusCheckBox();
		resetJournalCheckBox();
	}
	
	/**
	 * Reset Status CheckBox
	 */
	private void resetStatusCheckBox() {
		for (CheckBox cb : cbStatuses) {
			cb.setValue(false);
		}
	}
	
	/**
	 * Reset Journal CheckBox
	 */
	private void resetJournalCheckBox() {
		for (CheckBox cb : cbEvents) {
			cb.setValue(false);
		}
	}
	
	/**
	 * Is Visible CheckBox Column
	 * @return
	 */
	public boolean isNewOrValidated() {
		CheckBox cbNew = getCheckBoxStatus(JournalEntryWkfStatus.NEW);
		CheckBox cbCanceled = getCheckBoxStatus(JournalEntryWkfStatus.CANCELLED);
		CheckBox cbPosted = getCheckBoxStatus(JournalEntryWkfStatus.POSTED);
		CheckBox cbValidated = getCheckBoxStatus(JournalEntryWkfStatus.VALIDATED);
		if (cbPosted.getValue() || cbCanceled.getValue()) {
			return false;
		}
		if (cbNew.getValue() ^ cbValidated.getValue()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Whether status New selected
	 * @return
	 */
	public boolean isStatusNew() {
		return getCheckBoxStatus(JournalEntryWkfStatus.NEW).getValue();
	}
	
	/**
	 * Whether status Validated selected
	 * @return
	 */
	public boolean isStatusValidated() {
		return getCheckBoxStatus(JournalEntryWkfStatus.VALIDATED).getValue();
	}
	
	/**
	 * @param status
	 * @return
	 */
	private CheckBox getCheckBoxStatus(EWkfStatus status) {
		for (CheckBox checkBox : cbStatuses) {
			((EWkfStatus) checkBox.getData()).equals(status);
			return checkBox;
		}
		return null;
	}

}
