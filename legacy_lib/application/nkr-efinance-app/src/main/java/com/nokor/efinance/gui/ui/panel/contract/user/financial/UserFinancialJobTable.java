package com.nokor.efinance.gui.ui.panel.contract.user.financial;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.model.entity.MEntityA;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.Employment;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.applicant.model.MEmployment;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.EEmploymentType;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * User reference table 
 * @author uhout.cheng
 */
public class UserFinancialJobTable extends Panel implements ItemClickListener, SelectedItem, ClickListener, FinServicesHelper, MEmployment {

	/** */
	private static final long serialVersionUID = -6028099930744228602L;
	
	private SimpleTable<Entity> simpleTable;
	private Button btnAdd;
	private Button btnEdit;
	private Button btnDelete;
	private Item selectedItem;
	private UserFinancialPanel userFinancialPanel;
	private UserFinancialJobForm userFinancialJobForm;
	private Individual individual;
	private Long selectedId;
	
	/**
	 * 
	 * @param userFinancialPanel
	 */
	public UserFinancialJobTable(UserFinancialPanel userFinancialPanel) {
		this.userFinancialPanel = userFinancialPanel;
		init();
	}
	
	/**
	 * 
	 * @param icon
	 * @return
	 */
	private Button getButton(String caption, Resource icon) {
		Button button = new NativeButton(I18N.message(caption));
		button.setIcon(icon);
		button.addClickListener(this);
		return button;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = new SimpleTable<Entity>(getColumnDefinitions());
		simpleTable.setSizeFull();
		simpleTable.setPageLength(3);
		simpleTable.addItemClickListener(this);
		btnAdd = getButton("add", FontAwesome.PLUS);
		btnEdit = getButton("edit", FontAwesome.PENCIL);
		btnDelete = getButton("delete", FontAwesome.TRASH_O);
		
		userFinancialJobForm = new UserFinancialJobForm();
		userFinancialJobForm.getBtnBack().addClickListener(new ClickListener() {
	
			/** */
			private static final long serialVersionUID = 3948855743898504357L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				individual = INDIVI_SRV.getById(Individual.class, individual.getId());
				setUserFinancialJobIndexedContainer(individual.getEmployments(EEmploymentType.SECO));
				userFinancialPanel.getContentLayout().removeComponent(userFinancialJobForm);
				userFinancialPanel.setVisibleLayouts(true);
			}
		});
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.setSizeFull();
		navigationPanel.addButton(btnAdd);
		navigationPanel.addButton(btnEdit);
		navigationPanel.addButton(btnDelete);
		
		Panel tablePanel = new Panel(simpleTable);
		tablePanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		VerticalLayout verLayout = new VerticalLayout();
		verLayout.setMargin(true);
		verLayout.setSpacing(true);
		verLayout.addComponent(navigationPanel);
		verLayout.addComponent(tablePanel);
		
		setCaption(I18N.message("other.income"));
		setContent(verLayout);
		
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70));
		columnDefinitions.add(new ColumnDefinition(EMPLOYMENTSTATUS, I18N.message("employment.type"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(EMPLOYMENTCATEGORY, I18N.message("employment.category"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(POSITION, I18N.message("position"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(COMPANYNAME, I18N.message("company.name"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(EMPLOYMENTINDUSTRY, I18N.message("company.sector"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(PHONE, I18N.message("phone"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(OCCUPATIONGROUP, I18N.message("occupation.group"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(ADDRESS, I18N.message("address"), String.class, Align.LEFT, 450));
		columnDefinitions.add(new ColumnDefinition(WORKPHONE, I18N.message("department.phone"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(MANAGERNAME, I18N.message("boss.name"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(MANAGERPHONE, I18N.message("boss.phone"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition(TIMEWITHEMPLOYERINYEAR + TIMEWITHEMPLOYERINMONTH, I18N.message("working.period"), String.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param employments
	 */
	@SuppressWarnings("unchecked")
	public void setUserFinancialJobIndexedContainer(List<Employment> employments) {
		simpleTable.removeAllItems();
		selectedItem = null;
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (employments != null && !employments.isEmpty()) {
			for (Employment emp : employments) {
				if (emp != null && emp.getId() != null) {
					Item item = indexedContainer.addItem(emp.getId());
					item.getItemProperty(ID).setValue(emp.getId());
					item.getItemProperty(EMPLOYMENTSTATUS).setValue(emp.getEmploymentStatus() != null ? emp.getEmploymentStatus().getDescLocale() : "");
					item.getItemProperty(EMPLOYMENTCATEGORY).setValue(emp.getEmploymentCategory() != null ? emp.getEmploymentCategory().getDescLocale() : "");
					item.getItemProperty(POSITION).setValue(emp.getPosition());
					item.getItemProperty(COMPANYNAME).setValue(emp.getEmployerName());
					item.getItemProperty(EMPLOYMENTINDUSTRY).setValue(emp.getEmploymentIndustryCategory() != null ? emp.getEmploymentIndustryCategory().getDescLocale() : "");
					item.getItemProperty(PHONE).setValue(emp.getIndividual() != null ? emp.getIndividual().getMobilePerso() : "");
					item.getItemProperty(OCCUPATIONGROUP).setValue(emp.getEmploymentIndustry() != null ? emp.getEmploymentIndustry().getDescLocale() : "");
					Address address = emp.getAddress();
					if (address != null) {
						item.getItemProperty(ADDRESS).setValue(ADDRESS_SRV.getDetailAddress(address));
					}
					item.getItemProperty(WORKPHONE).setValue(emp.getWorkPhone());
					item.getItemProperty(MANAGERNAME).setValue(emp.getManagerName());
					item.getItemProperty(MANAGERPHONE).setValue(emp.getManagerPhone());
					item.getItemProperty(TIMEWITHEMPLOYERINYEAR + TIMEWITHEMPLOYERINMONTH).setValue(getWorkingPeriod(emp));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param employment
	 * @return
	 */
	private String getWorkingPeriod(Employment employment) {
		String SPACE = " ";
		StringBuffer stringBuffer = new StringBuffer(); 
		stringBuffer.append(String.valueOf(MyNumberUtils.getInteger(employment.getTimeWithEmployerInYear())));
		stringBuffer.append(SPACE);
		stringBuffer.append(I18N.message("year"));
		stringBuffer.append(SPACE);
		stringBuffer.append(String.valueOf(MyNumberUtils.getInteger(employment.getTimeWithEmployerInMonth())));
		stringBuffer.append(SPACE);
		stringBuffer.append(I18N.message("month"));
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param individual
	 */
	public void assignValues(Individual individual) {
		this.individual = individual;
		setUserFinancialJobIndexedContainer(individual.getEmployments(EEmploymentType.SECO));
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			userFinancialPanel.setVisibleLayouts(false);
			userFinancialPanel.getContentLayout().addComponent(userFinancialJobForm);
			userFinancialJobForm.assignValues(individual, null);
			userFinancialJobForm.reset();
		} else if (event.getButton().equals(btnEdit)) {
			if (selectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("edit.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				userFinancialPanel.setVisibleLayouts(false);
				userFinancialPanel.getContentLayout().addComponent(userFinancialJobForm);
				userFinancialJobForm.removedMessagePanel();
				userFinancialJobForm.assignValues(individual, INDIVI_SRV.getById(Employment.class, selectedId));
			}
		} else if (event.getButton().equals(btnDelete)) {
			if (selectedItem == null) {
				MessageBox mb = new MessageBox(UI.getCurrent(), "300px", "160px", I18N.message("information"),
						MessageBox.Icon.INFO, I18N.message("delete.item.not.selected"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			} else {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
						new String[] {selectedId.toString()}),
						new ConfirmDialog.Listener() {

					/** */
					private static final long serialVersionUID = 6782654497474787095L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							INDIVI_SRV.deleteEmployment(INDIVI_SRV.getById(Employment.class, selectedId));
							getNotificationDesc("item.deleted.successfully");
							INDIVI_SRV.refresh(individual);
							setUserFinancialJobIndexedContainer(individual.getEmployments(EEmploymentType.SECO));
						}
					}
				});
			}
		}
	}
	
	/**
	 * 
	 * @param description
	 * @return
	 */
	private Notification getNotificationDesc(String description) {
		Notification notification = new Notification("", Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message(description, new String[]{ selectedId.toString() }));
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		return notification;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}

	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (this.selectedItem != null) {
			selectedId = (Long) selectedItem.getItemProperty(MEntityA.ID).getValue();
		}	
		if (event.isDoubleClick()) {
			userFinancialPanel.setVisibleLayouts(false);
			userFinancialPanel.getContentLayout().addComponent(userFinancialJobForm);
			userFinancialJobForm.assignValues(individual, INDIVI_SRV.getById(Employment.class, selectedId));
			userFinancialJobForm.removedMessagePanel();
		}
	}	

}
