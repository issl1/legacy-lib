package com.nokor.efinance.gui.ui.panel.contract.notes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.collection.model.MNote;
import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractNote;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class HistoryTabPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper, MNote {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9139474744013449523L;
	
	private CheckBox cbType1;
	private CheckBox cbType2;
	private CheckBox cbType3;

	private TextField txtDisplayDay;
	private EntityComboBox<OrgStructure> cbxDepartment;
	
	private Button btnSearch;
	private Button btnReset;
	
	private SimplePagedTable<Entity> simplePagedTable;
	private List<ColumnDefinition> columnDefinitions;

	public HistoryTabPanel() {
		setMargin(true);
		setSpacing(true);
		init();
		
		addComponent(createSearchPanel());
		addComponent(simplePagedTable);
		addComponent(simplePagedTable.createControls());
	}
	
	/**
	 * init
	 */
	private void init() {
		cbType1 = new CheckBox(I18N.message("type.one"));
		cbType2 = new CheckBox(I18N.message("type.two"));
		cbType3 = new CheckBox(I18N.message("type.three"));
		
		txtDisplayDay = ComponentFactory.getTextField(10, 50);
		cbxDepartment = new EntityComboBox<>(OrgStructure.class, "nameEn");
		cbxDepartment.setImmediate(true);
		cbxDepartment.setEntities(getDepartment());
		
		btnSearch = ComponentLayoutFactory.getButtonSearch();
		btnSearch.addClickListener(this);
		
		btnReset = ComponentLayoutFactory.getButtonReset();
		btnReset.addClickListener(this);
		
		this.columnDefinitions = createColumnDifinitions();
		simplePagedTable = new SimplePagedTable<>(this.columnDefinitions);
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel createSearchPanel() {
		Label lblDisplayOnlyLast = ComponentLayoutFactory.getLabelCaption("display.only.last");
		Label lblDay = ComponentLayoutFactory.getLabelCaption("day");
		Label lblDepartment = ComponentLayoutFactory.getLabelCaption("department");
		
		HorizontalLayout searchLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		searchLayout.addComponent(cbType1);
		searchLayout.addComponent(cbType2);
		searchLayout.addComponent(cbType3);
		
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		searchLayout.addComponent(lblDisplayOnlyLast);
		searchLayout.addComponent(txtDisplayDay);
		searchLayout.addComponent(lblDay);
		searchLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		searchLayout.addComponent(lblDepartment);
		searchLayout.addComponent(cbxDepartment);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnSearch);
		buttonLayout.addComponent(btnReset);
		
		VerticalLayout mainSearchLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainSearchLayout.addComponent(searchLayout);
		mainSearchLayout.addComponent(buttonLayout);
		mainSearchLayout.setComponentAlignment(searchLayout, Alignment.TOP_CENTER);
		mainSearchLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		
		Panel searchPanel = ComponentLayoutFactory.getPanel(mainSearchLayout, false, false);
		
		return searchPanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(DATE_TIME, I18N.message("date.time"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(USER_DEPARTMENT, I18N.message("user.department"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition(TYPE, I18N.message("type"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(NOTE, I18N.message("note"), String.class, Align.LEFT, 140));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contracts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getIndexedContainer(Contract contract) {
		IndexedContainer indexedContainer = new IndexedContainer();
		
		List<ContractNote> contractNotes = NOTE_SRV.getNotesByContract(contract);
		List<Reminder> reminders = REMINDER_SRV.getReminderByContract(contract);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		
		int index = 0;
		if (contractNotes != null && !contractNotes.isEmpty()) {
			for (ContractNote contractNote : contractNotes) {
				Item item = indexedContainer.addItem(index);
				
				item.getItemProperty(DATE_TIME).setValue(dateFormat.format(contractNote.getCreateDate()));
				item.getItemProperty(USER_DEPARTMENT).setValue(NOTE_SRV.getUserDepartment(contractNote.getCreateUser()));
				item.getItemProperty(TYPE).setValue(I18N.message("note"));
				item.getItemProperty(NOTE).setValue(contractNote.getNote());
				index++;
			}
		}
		
		if (reminders != null && !reminders.isEmpty()) {
			for (Reminder reminder : reminders) {
				Item item = indexedContainer.addItem(index);
				item.getItemProperty(DATE_TIME).setValue(dateFormat.format(reminder.getDate()));
				item.getItemProperty(USER_DEPARTMENT).setValue(NOTE_SRV.getUserDepartment(reminder.getCreateUser()));
				item.getItemProperty(TYPE).setValue(I18N.message("reminder"));
				item.getItemProperty(NOTE).setValue(reminder.getComment());
				index++;
			}
			
		}
		return indexedContainer;
	}
	
	public void assignValues(Contract contract) {
		simplePagedTable.setContainerDataSource(getIndexedContainer(contract));
	}
	
	/**
	 * reset
	 */
	public void reset() {
		cbType1.setValue(false);
		cbType2.setValue(false);
		cbType3.setValue(false);
		txtDisplayDay.setValue("");
		cbxDepartment.setSelectedEntity(null);
	}
	

	/**
	 * 
	 * @return
	 */
	private List<OrgStructure> getDepartment() {
		BaseRestrictions<OrgStructure> restrictions = new BaseRestrictions<>(OrgStructure.class);
		restrictions.addCriterion(Restrictions.eq("level", EOrgLevel.DEPARTMENT));
		return ENTITY_SRV.list(restrictions);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSearch) {
			
		} else if (event.getButton() == btnReset) {
			reset();
		}
	}

}
