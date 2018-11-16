package com.nokor.efinance.gui.ui.panel.contract.notes.appointment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Appointment;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContractNote;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.Reindeer;

/**
 * Appointments table 
 * @author buntha.chea
 */
public class AppointmentsTablePanel extends Panel implements ItemClickListener, SelectedItem, FinServicesHelper, MContractNote {

	/** */
	private static final long serialVersionUID = 5804312889880447009L;

	private SimpleTable<Entity> simpleTable;
	private Item selectedItem;
	private Contract contract;
	
	private AppointmentFormPanel appointmentFormPanel;
	
	/**
	 * 
	 */
	public AppointmentsTablePanel(AppointmentFormPanel appointmentFormPanel) {
		this.appointmentFormPanel = appointmentFormPanel;
		init();
	}
	
	/**
	 * 
	 * @param columnDefinitions
	 * @return
	 */
	private SimpleTable<Entity> getSimpleTable(List<ColumnDefinition> columnDefinitions) {
		SimpleTable<Entity> simpleTable = new SimpleTable<Entity>(columnDefinitions);
		simpleTable.setSizeUndefined();
		simpleTable.setPageLength(4);
		simpleTable.addItemClickListener(this);
		return simpleTable;
	}
	
	/**
	 * 
	 */
	private void init() {
		simpleTable = getSimpleTable(getColumnDefinitions());
		simpleTable.setSizeFull();
		setContent(simpleTable);
		setStyleName(Reindeer.PANEL_LIGHT);
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> getColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50));
		columnDefinitions.add(new ColumnDefinition("location", I18N.message("location"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("between", I18N.message("between"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("and", I18N.message("and"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Date.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("time", I18N.message("time"), String.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition("remark", I18N.message("remark"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("action", I18N.message("actions"), Button.class, Align.CENTER, 55));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		refreshTable();
	}
	
	/**
	 * 
	 * @param individualContactInfos
	 */
	@SuppressWarnings("unchecked")
	public void setTableIndexedContainer(List<Appointment> appointments) {
		simpleTable.removeAllItems();
		selectedItem = null;
		Container indexedContainer = simpleTable.getContainerDataSource();
		if (appointments != null && !appointments.isEmpty()) {
			for (Appointment appointment : appointments) {
				if (appointment != null) {
					Item item = indexedContainer.addItem(appointment.getId());
					item.getItemProperty(ID).setValue(appointment.getId());
				    item.getItemProperty("location").setValue(appointment.getLocation().getDescEn());
					item.getItemProperty("between").setValue(appointment.getBetween1().getDescEn());
					if (appointment.getBetween2() != null) {
						item.getItemProperty("and").setValue(appointment.getBetween2().getNameEn());
					}
					if (appointment.getStartDate() != null) {
						long millis = appointment.getStartDate().getTime() - DateUtils.getDateAtBeginningOfDay(appointment.getStartDate()).getTime();										
					String time = convertMilisToHour(millis);
					item.getItemProperty("time").setValue(time);
					}
					item.getItemProperty("date").setValue(appointment.getStartDate());
					item.getItemProperty("remark").setValue(appointment.getRemark());
					Button btnDelete = ComponentLayoutFactory.getButtonIcon(FontAwesome.TRASH_O);
					item.getItemProperty("action").setValue(btnDelete);
					
					btnDelete.addClickListener(new ClickListener() {
						
						/** */
						private static final long serialVersionUID = -1430882535270828332L;

						/**
						 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
						 */
						@Override
						public void buttonClick(ClickEvent event) {
							ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"), new ConfirmDialog.Listener() {
							
								/** */
								private static final long serialVersionUID = -8329055025195023600L;

								/**
								 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
								 */
								@Override
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										try {
											ENTITY_SRV.delete(appointment);
											ComponentLayoutFactory.displaySuccessMsg("delete.successfully");
											refreshTable();
										} catch (Exception e) {
											if (e instanceof DataIntegrityViolationException) {
						    					ComponentLayoutFactory.displayErrorMsg("msg.warning.delete.selected.item.is.used");
						    				} else {
						    					ComponentLayoutFactory.displayErrorMsg("msg.error.technical");
						    				}
										}
									}
								}
							});
						}
					});
					
				}
			}
		}
	}
	
	/**
	 */
	public void refreshTable() {
		if (contract != null) {
			setTableIndexedContainer(NOTE_SRV.getAppointmentByContract(contract));
		}
	}

	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty("id").getValue());
		}
		return null;
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
		if (event.isDoubleClick()) {
			Appointment appointment = null;
			if (getItemSelectedId() != null) {
				appointment = CONT_SRV.getById(Appointment.class, getItemSelectedId());
			}
			appointmentFormPanel.assignValues(appointment);
			
		}
	}	
	
	/**
	 * 
	 * @param millis
	 * @return
	 */
	private String convertMilisToHour(Long millis) {
		String hm = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
		return hm;
	}
	
	/**
	 * display popup
	 */
	/*public void displayPopup(boolean isNew) {
		AppointmentPopupPanel appointmentPopupPanel = AppointmentPopupPanel.show(contract, new AppointmentPopupPanel.Listener() {
			private static final long serialVersionUID = -8930832327089009034L;
			@Override
			public void onClose(AppointmentPopupPanel dialog) {
				refreshTable();
			}
		});
		if (isNew) {
			appointmentPopupPanel.reset();
		} else {
			Appointment appointment = null;
			if (getItemSelectedId() != null) {
				appointment = ENTITY_SRV.getById(Appointment.class, getItemSelectedId());
			}
			appointmentPopupPanel.assignValues(appointment);
		}
	}*/

}
