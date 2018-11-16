package com.nokor.efinance.gui.ui.panel.contract.accounting;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class AccountingJournalPanel extends AbstractControlPanel implements ClickListener{

	/** */
	private static final long serialVersionUID = 7561378204497927984L;
	
	private SimpleTable<Entity> simpleTableTop;
	private SimpleTable<Entity> simpleTableBottom;
	
	private ComboBox cbxEvent;
	private ComboBox cbxJournal;
	private ComboBox cbxStatus;
	
	private Button btnReset;
	private AutoDateField dfDate;
	private TextField txtAccountingCode;
	
	/**
	 * 
	 */
	public AccountingJournalPanel() {
		simpleTableTop = new SimpleTable<Entity>(getColumnDefinitionsTop());
		simpleTableTop.setSizeFull();
		simpleTableTop.setPageLength(5);
		
		simpleTableBottom = new SimpleTable<Entity>(getColumnDefinitionsBottom());
		simpleTableBottom.setSizeFull();
		simpleTableBottom.setPageLength(5);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(getTopLayout());
		mainLayout.addComponent(simpleTableTop);
		mainLayout.addComponent(simpleTableBottom);
		
		addComponent(mainLayout);
		
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDefinitionsTop() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Long.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition("journal", I18N.message("journal"), Long.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("event", I18N.message("event"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("amount", I18N.message("amount"), Double.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("status", I18N.message("status"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("sunSystem", I18N.message("sun.system"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("action", I18N.message("option"), Button.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDefinitionsBottom() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("accountingCode", I18N.message("accounting.code"), String.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("name", I18N.message("name"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition("debitAmount", I18N.message("debit.amount"), Double.class, Align.LEFT, 110));
		columnDefinitions.add(new ColumnDefinition("creditAmount", I18N.message("credit.amount"), Double.class, Align.LEFT, 110));
		return columnDefinitions;
	}
	
	/**
	 * Top Layout
	 */
	private Component getTopLayout() {
		cbxEvent = ComponentFactory.getComboBox();
		cbxEvent.setWidth(120, Unit.PIXELS);
		cbxJournal = ComponentFactory.getComboBox();
		cbxJournal.setWidth(120, Unit.PIXELS);
		cbxStatus = ComponentFactory.getComboBox();
		cbxStatus.setWidth(120, Unit.PIXELS);
		
		dfDate = ComponentFactory.getAutoDateField();
		txtAccountingCode = ComponentFactory.getTextField(false, 100, 120);
		
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(this);
		
		Label lblEventTitle = ComponentFactory.getLabel(I18N.message("event"));
		Label lblJournalTitle = ComponentFactory.getLabel(I18N.message("journal"));
		Label lblStatusTitle = ComponentFactory.getLabel(I18N.message("status"));
		Label lblDateTitle = ComponentFactory.getLabel(I18N.message("date"));
		Label lblAccountingCodeTitle = ComponentFactory.getLabel("accounting.code");
		
		GridLayout gridLayout = new GridLayout(16, 1);
		gridLayout.setSpacing(true);
		
		gridLayout.addComponent(lblEventTitle, 0, 0);
		gridLayout.addComponent(cbxEvent, 1, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 2, 0);
		gridLayout.addComponent(lblDateTitle, 3, 0);
		gridLayout.addComponent(dfDate, 4, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 5, 0);
		gridLayout.addComponent(lblAccountingCodeTitle, 6, 0);
		gridLayout.addComponent(txtAccountingCode, 7, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 8, 0);
		gridLayout.addComponent(lblJournalTitle, 9, 0);
		gridLayout.addComponent(cbxJournal, 10, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 11, 0);
		gridLayout.addComponent(lblStatusTitle, 12, 0);
		gridLayout.addComponent(cbxStatus, 13, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 14, 0);
		gridLayout.addComponent(btnReset, 15, 0);
		
		gridLayout.setComponentAlignment(lblEventTitle, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblDateTitle, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAccountingCodeTitle, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblJournalTitle, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblStatusTitle, Alignment.MIDDLE_LEFT);
		
		return gridLayout;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		
	}

}
