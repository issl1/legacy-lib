package com.nokor.efinance.core.quotation.panel.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * History panel
 * @author ly.youhort
 */
public class HistoryPanel extends AbstractControlPanel implements ValueChangeListener, ClickListener{
	
	private static final long serialVersionUID = -788465592030480618L;

	private static String EVENT_HISTORY = I18N.message("event.history");
	private static String LOCK_SPLIT_HISTORY = I18N.message("lock.split.history");
	private static String REQUEST_HISTORY = I18N.message("request.history");
	private static String AP_HISTORY = I18N.message("ap.history");
	private static String COLLECTION_HISTORY = I18N.message("collection.history");
	private static String APPOINTMENT_HISTORY = I18N.message("appointment.history");
	private static String DOCUMENT_HISTORY = I18N.message("document.history");
	private static String ALL = I18N.message("all");
	
	private static String LOS = I18N.message("los");
	private static String CM = I18N.message("cm");
	private static String COLLECTION = I18N.message("collection");
	private static String REGISTRATION = I18N.message("registration");
	private static String INSURANCE = I18N.message("insurance");
	private static String AUCTION = I18N.message("auction");
	
	private ComboBox cbxHistory;
	private OptionGroup optionGroup;
	private HorizontalLayout optionGroupLayout;
	private VerticalLayout tableLayout;
	
	private List<ColumnDefinition> eventHistoryColumnDefinitions;
	private SimplePagedTable<Entity> eventHistoryPagedTable;
	
	private List<ColumnDefinition> lockSplitHistoryColumnDefinitions;
	private SimplePagedTable<Entity> lockSplitHistoryPagedTable;
	private Button btnOperateSuspense;
	private LockSplitHistoryPopupTablePanel lockSplitHistoryPopupTablePanel;
	
	/**
	 * 
	 * @param delegate
	 */
	public HistoryPanel(/*ContractFormPanel delegate*/) {
		setMargin(true);
		setSpacing(true);
		setSizeFull();
		optionGroupLayout = ComponentFactory.getHorizontalLayout();
		tableLayout = ComponentFactory.getVerticalLayout();
		tableLayout.setSpacing(true);
		addComponent(createComponent());
	}
	
	/**
	 * 
	 * @return
	 */
	private Component createComponent() {
		btnOperateSuspense = ComponentFactory.getButton("operate.suspense");
		btnOperateSuspense.addClickListener(this);
		
		cbxHistory = ComponentFactory.getComboBox();
		cbxHistory.setWidth(170, Unit.PIXELS);
		cbxHistory.setNullSelectionAllowed(false);
		cbxHistory.setImmediate(true);
		cbxHistory.addValueChangeListener(this);
		final List<String> historyInfos = Arrays.asList(new String[] {EVENT_HISTORY, LOCK_SPLIT_HISTORY, REQUEST_HISTORY,
																	  AP_HISTORY, COLLECTION_HISTORY, APPOINTMENT_HISTORY,
																	  DOCUMENT_HISTORY, ALL});
		for (String string : historyInfos) {
			cbxHistory.addItem(string);
		}
		cbxHistory.setValue(EVENT_HISTORY);
		
		VerticalLayout mainLayout = ComponentFactory.getVerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		mainLayout.addComponent(cbxHistory);
		mainLayout.setComponentAlignment(cbxHistory, Alignment.TOP_RIGHT);
		mainLayout.addComponent(optionGroupLayout);
		mainLayout.setComponentAlignment(optionGroupLayout, Alignment.TOP_RIGHT);
		mainLayout.addComponent(tableLayout);
		
		return mainLayout;
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(optionGroup)) {
			tableLayout.removeAllComponents();
			if (LOS.equals(optionGroup.getValue())) {
				tableLayout.addComponent(createEventHistoryTable());
			} else if (CM.equals(optionGroup.getValue())) {
				tableLayout.addComponent(createEventHistoryTable());
			} else if (COLLECTION.equals(optionGroup.getValue())) {
				tableLayout.addComponent(createEventHistoryTable());
			} else if (REGISTRATION.equals(optionGroup.getValue())) {
				tableLayout.addComponent(createEventHistoryTable());
			} else if (INSURANCE.equals(optionGroup.getValue())) {
				tableLayout.addComponent(createEventHistoryTable());
			} else if (AUCTION.equals(optionGroup.getValue())) {
				tableLayout.addComponent(createEventHistoryTable());
			}
		} else if (event.getProperty().equals(cbxHistory)) {
			tableLayout.removeAllComponents();
			optionGroupLayout.removeAllComponents();
			if (EVENT_HISTORY.equals(cbxHistory.getValue())) {
				optionGroupLayout.addComponent(createOptionGroupPanel());
			} else if (LOCK_SPLIT_HISTORY.equals(cbxHistory.getValue())) {
				tableLayout.addComponent(createLockSplitHistoryTable());
			} else if (REQUEST_HISTORY.equals(cbxHistory.getValue())) {
				tableLayout.addComponent(createHistoryTable("request.history", createRequestHistoryColumnDefinitions()));
			} else if (AP_HISTORY.equals(cbxHistory.getValue())) {
				tableLayout.addComponent(createHistoryTable("ap.history", createAPHistoryColumnDefinitions()));
			} else if (COLLECTION_HISTORY.equals(cbxHistory.getValue())) {
				tableLayout.addComponent(createHistoryTable("collection.history", createCollectionHistoryColumnDefinitions()));
			} else if (APPOINTMENT_HISTORY.equals(cbxHistory.getValue())) {
				tableLayout.addComponent(createHistoryTable("appointment.history", createAppointmentHistoryColumnDefinitions()));
			} else if (DOCUMENT_HISTORY.equals(cbxHistory.getValue())) {
				tableLayout.addComponent(createHistoryTable("document.history", createDocumentHistoryColumnDefinitions()));
			} else if (ALL.equals(cbxHistory.getValue())) {
				optionGroupLayout.addComponent(createOptionGroupPanel());
				tableLayout.addComponent(createHistoryTable("ap.history", createAPHistoryColumnDefinitions()));
				tableLayout.addComponent(createLockSplitHistoryTable());
				tableLayout.addComponent(createHistoryTable("appointment.history", createAppointmentHistoryColumnDefinitions()));
				tableLayout.addComponent(createHistoryTable("document.history", createDocumentHistoryColumnDefinitions()));
			}
		}
	}
	
	/** */
	private Panel createOptionGroupPanel() {
		optionGroup = new OptionGroup();
		final List<String> profileInfos = Arrays.asList(new String[] {LOS, CM, COLLECTION, REGISTRATION, INSURANCE, AUCTION});
		optionGroup = new OptionGroup(null, profileInfos);
		optionGroup.addStyleName("horizontal");
		optionGroup.setImmediate(true);
		optionGroup.setNullSelectionAllowed(false);
		optionGroup.addValueChangeListener(this);
		if (ProfileUtil.isCMProfile()) {
			optionGroup.select(CM);
		} else {
			optionGroup.select(COLLECTION);
		}
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(optionGroup);
		verticalLayout.setComponentAlignment(optionGroup, Alignment.MIDDLE_RIGHT);
		
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setContent(verticalLayout);
		return panel;
	}
	
	/**
	 * event history and LOS table
	 * @return
	 */
	private VerticalLayout createEventHistoryTable() {
		eventHistoryColumnDefinitions = createEventHistoryColumnDefinitions();
		eventHistoryPagedTable = new SimplePagedTable<Entity>(eventHistoryColumnDefinitions);
		eventHistoryPagedTable.setCaption(I18N.message("event.history"));
		TextField txtAddInfo = ComponentFactory.getTextField(60, 180);
		Button btnAdd = ComponentFactory.getButton("add");
		
		HorizontalLayout buttonLayout = ComponentFactory.getHorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(txtAddInfo);
		buttonLayout.addComponent(btnAdd);
		
		VerticalLayout verticalLayout = ComponentFactory.getVerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(eventHistoryPagedTable);
		verticalLayout.addComponent(eventHistoryPagedTable.createControls());
		verticalLayout.addComponent(buttonLayout);
		
		return verticalLayout;
	}
	
	/**
	 * Lock split history table
	 * @return
	 */
	private VerticalLayout createLockSplitHistoryTable() {
		lockSplitHistoryColumnDefinitions = createLockSplitHistoryColumnDefinitions();
		lockSplitHistoryPagedTable = new SimplePagedTable<Entity>(lockSplitHistoryColumnDefinitions);
		lockSplitHistoryPagedTable.setCaption(I18N.message("lock.split.history"));
		
		VerticalLayout verticalLayout = ComponentFactory.getVerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(lockSplitHistoryPagedTable);
		verticalLayout.addComponent(lockSplitHistoryPagedTable.createControls());
		verticalLayout.addComponent(btnOperateSuspense);
		verticalLayout.setComponentAlignment(btnOperateSuspense, Alignment.BOTTOM_RIGHT);
		
		return verticalLayout;
	}
	
	/**
	 * Request, AP, Collection, Appointment,
	 * Document history table
	 * @param caption
	 * @param columnDefinitions
	 * @return
	 */
	private VerticalLayout createHistoryTable(String caption, List<ColumnDefinition> columnDefinitions) {
		List<ColumnDefinition> list = columnDefinitions;
		SimplePagedTable<Entity> simplePagedTable = new SimplePagedTable<Entity>(list);
		simplePagedTable.setCaption(I18N.message(caption));
		VerticalLayout verticalLayout = ComponentFactory.getVerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(simplePagedTable);
		verticalLayout.addComponent(simplePagedTable.createControls());
		return verticalLayout;
	}
	
	/**
	 * Event history 
	 * @return
	 */
	protected List<ColumnDefinition> createEventHistoryColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("updateTime", I18N.message("time"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("create.user", I18N.message("user.id"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("action", I18N.message("action"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("attributes", I18N.message("attributes"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("remark", I18N.message("remark"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("comment", I18N.message("comment"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * Lock split history
	 * @return
	 */
	protected List<ColumnDefinition> createLockSplitHistoryColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("lock.split.status", I18N.message("lock.split.status"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("updateTime", I18N.message("time"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("create.user", I18N.message("user.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("lock.split.number", I18N.message("lock.split.number"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("lock.split.detail", I18N.message("lock.split.detail"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("lock.split.amount", I18N.message("lock.split.amount"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("due.date", I18N.message("due.date"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("lock.split.expiry.date", I18N.message("lock.split.expiry.date"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("payment.result", I18N.message("payment.result"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("payment.amount", I18N.message("payment.amount"), String.class, Align.LEFT, 90));
		columnDefinitions.add(new ColumnDefinition("tr.date", I18N.message("tr.date"), String.class, Align.LEFT, 80));
		return columnDefinitions;
	}
	
	/**
	 * Request history
	 * @return
	 */
	protected List<ColumnDefinition> createRequestHistoryColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("updateTime", I18N.message("time"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("create.user", I18N.message("user.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("caller", I18N.message("caller"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("person.phone", I18N.message("person.phone"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("request", I18N.message("request"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("remark", I18N.message("remark"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * AP history
	 * @return
	 */
	protected List<ColumnDefinition> createAPHistoryColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("payment.code", I18N.message("payment.code"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("payment.code.receiver", I18N.message("payment.code.receiver"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("receiver.name", I18N.message("receiver.name"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("payment.voucher.no", I18N.message("payment.voucher.no"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("submit.date", I18N.message("submit.date"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("tr.date", I18N.message("tr.date"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("payment.mode", I18N.message("payment.mode"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("amount", I18N.message("amount"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("acct.no", I18N.message("acct.no"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("status", I18N.message("status"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("remark", I18N.message("remark"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("division", I18N.message("division"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("create.user", I18N.message("user.id"), String.class, Align.LEFT,120));
		return columnDefinitions;
	}
	
	/**
	 * Collection history
	 * @return
	 */
	protected List<ColumnDefinition> createCollectionHistoryColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("updateTime", I18N.message("time"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("create.user", I18N.message("user.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("origin", I18N.message("origin"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("person.contact", I18N.message("person.contact"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("contact.no", I18N.message("contact.no"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("result", I18N.message("result"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("action1", I18N.message("action1"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("action2", I18N.message("action2"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("remark", I18N.message("remark"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Appointment history
	 * @return
	 */
	protected List<ColumnDefinition> createAppointmentHistoryColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("updateTime", I18N.message("time"), String.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition("create.user", I18N.message("user.id"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("appointment.type", I18N.message("appointment.type"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("appointment.date", I18N.message("appointment.date"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("appointment.time", I18N.message("appointment.time"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("appointment.width", I18N.message("appointment.width"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("location", I18N.message("location"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Document history
	 * @return
	 */
	protected List<ColumnDefinition> createDocumentHistoryColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("updateDate", I18N.message("date"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("updateTime", I18N.message("time"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("create.user", I18N.message("user.id"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("action", I18N.message("action"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("document.name", I18N.message("document.name"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * @param applicant
	 */
	public void assignValues(Applicant applicant) {			
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		assignValues(new Applicant());
	}
	
	/**
	 * @return
	 */
	public boolean isValid() {		
		return errors.isEmpty();
	}

	/**
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnOperateSuspense) {
			final Window window = new Window();
			window.setModal(true);
			lockSplitHistoryPopupTablePanel = new LockSplitHistoryPopupTablePanel();
			Button btnSave = new NativeButton(I18N.message("save"), new ClickListener() {
				
				/** */
				private static final long serialVersionUID = 2469296272151550057L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					window.close();
				}
			});
			Button btnCancel = new NativeButton(I18N.message("cancel"), new ClickListener() {
				
				/** */
				private static final long serialVersionUID = 6025301090255270156L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					window.close();
				}
			});
			btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));
			btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
			NavigationPanel navigationPanel = new NavigationPanel();
			navigationPanel.addButton(btnSave);
			navigationPanel.addButton(btnCancel);
			
			VerticalLayout contentLayout = new VerticalLayout(); 
			contentLayout.addComponent(navigationPanel);
			contentLayout.addComponent(lockSplitHistoryPopupTablePanel);
			window.setContent(contentLayout);
			window.setWidth(1150, Unit.PIXELS);
			window.setHeight(650, Unit.PIXELS);
	        UI.getCurrent().addWindow(window);
		}
	}
}
