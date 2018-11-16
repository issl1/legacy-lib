package com.nokor.efinance.core.quotation.panel.popup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Appointment;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.ersys.core.hr.service.OrgStructureRestriction;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Contract Appointment Pop up Panel
 * @author bunlong.taing
 */
public class ContractAppointmentPopupPanel extends Window implements ClickListener, CashflowEntityField, ValueChangeListener {

	/** */
	private static final long serialVersionUID = 4643747945929835146L;
	
	private EntityService entityService;
	
	private NativeButton btnSubmit;
	private NativeButton btnCancel;
	private NativeButton btnReset;
	private NativeButton btnDelete;
	
	private ERefDataComboBox<ELocation> cbxLocation;
	private ERefDataComboBox<EApplicantType> cbxBetween;
	private EntityComboBox<OrgStructure> cbxAnd;
	private ComboBox cbxTime;
	
	private AutoDateField dfDate;
	private TextArea txtRemark;
	
	private Appointment appointment;
	protected List<String> errors;
	private VerticalLayout messagePanel;
	
	private ListSelect listSelect;
	private List<Appointment> appointments;
	private Contract contract;
	
	/**
	 * Contract Appointment Pop up Panel
	 * @param caption
	 */
	public ContractAppointmentPopupPanel(String caption) {
		super(I18N.message(caption));
		setModal(true);
		setResizable(false);
		setWidth(480, Unit.PIXELS);
		setHeight(450, Unit.PIXELS);
		init();
	}

	/**
	 * Init component in window
	 */
	private void init() {
		errors = new ArrayList<String>();
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		entityService = SpringUtils.getBean(EntityService.class);
		btnSubmit = new NativeButton(I18N.message("submit"));
		btnSubmit.setIcon(FontAwesome.SAVE);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnReset = new NativeButton(I18N.message("reset"));
		btnReset.setIcon(FontAwesome.ERASER);
		btnDelete = new NativeButton(I18N.message("delete"));
		btnDelete.setIcon(FontAwesome.TRASH_O);
		btnSubmit.addClickListener(this);
		btnCancel.addClickListener(this);
		btnReset.addClickListener(this);
		btnDelete.addClickListener(this);
		
		
		cbxLocation = new ERefDataComboBox<ELocation>(ELocation.values());
		cbxLocation.setCaption(I18N.message("location"));
		cbxLocation.setRequired(true);
		cbxBetween =  new ERefDataComboBox<EApplicantType>(EApplicantType.values());
		cbxBetween.setCaption(I18N.message("between"));
		cbxBetween.setRequired(true);
		
		cbxAnd = new EntityComboBox(OrgStructure.class, OrgStructure.NAME);
		cbxAnd.setCaption(I18N.message("and"));
		OrgStructureRestriction restrictions = new OrgStructureRestriction();
		// ? which company
//		restrictions.setOrganizationId(organizationId);
		cbxAnd.renderer(restrictions);
		cbxTime = getTimeComboBox();
		cbxTime.setCaption(I18N.message("time"));
		
		dfDate = ComponentFactory.getAutoDateField("date", false);
		dfDate.setValue(DateUtils.today());
		txtRemark = ComponentFactory.getTextArea("remark", false, 300, 100);
		
		listSelect = new ListSelect();
		listSelect.setNullSelectionAllowed(false);
		listSelect.setRows(5);
		listSelect.setWidth("460px");
		listSelect.addValueChangeListener(this);
		listSelect.setImmediate(true);
		
		VerticalLayout listSelectLayout = new VerticalLayout();
		listSelectLayout.setMargin(true);
		listSelectLayout.addComponent(listSelect);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSubmit);
		navigationPanel.addButton(btnReset);
		navigationPanel.addButton(btnCancel);
		navigationPanel.addButton(btnDelete);
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxLocation);
		formLayout.addComponent(cbxBetween);
		formLayout.addComponent(cbxAnd);
		formLayout.addComponent(dfDate);
		formLayout.addComponent(cbxTime);
		formLayout.addComponent(txtRemark);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(formLayout);
		verticalLayout.addComponent(listSelectLayout);
		
		setContent(verticalLayout);
	}
	
	/**
	 * Assign values to controls
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		if (contract != null) {
			this.contract = contract;
			appointments = this.getAppointmentByContract(contract);
			listSelect.removeAllItems();
			for (Appointment appointment : appointments) {
				String combineEachAppointment = this.combineEachAppointment(appointment);
				listSelect.addItem(combineEachAppointment);
			}
		} else {
			MessageBox mb = new MessageBox(UI.getCurrent(), I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("appointment.no.contract"),
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.setWidth("300px");
			mb.setHeight("150px");
			mb.show();
			close();
		}
	}
	
	/**
	 * Get Time ComboBox
	 * @return
	 */
	private ComboBox getTimeComboBox() {
		ComboBox comboBox = ComponentFactory.getComboBox();
		Date date = DateUtils.getDateAtBeginningOfDay(DateUtils.today());
		long startTime = date.getTime();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < 48; i++) {
			long time = 1800000 * i;		// 30 minute interval = 1800000
			date.setTime(startTime + time);
			comboBox.addItem(time);
			comboBox.setItemCaption(time, df.format(date));
		}
		comboBox.setValue(0l);
		comboBox.setNullSelectionAllowed(false);
		return comboBox;
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnCancel) {
			close();
		} else if (event.getButton() == btnReset) {
			clear();
		} else if (event.getButton() == btnSubmit) {
			save();
		} else if (event.getButton() == btnDelete) {
			deleteAppointment();
		}
	}
	
	/**
	 * Save the appointment
	 */
	private void save() {
		if (validate()) {		
			if (appointment == null) {
				appointment = new Appointment();
				appointment.setContract(contract);
			}
			appointment.setLocation(cbxLocation.getSelectedEntity());
			appointment.setBetween1(cbxBetween.getSelectedEntity());
			appointment.setBetween2(cbxAnd.getSelectedEntity());
			Date date = dfDate.getValue();
			if (date != null) {
				date = DateUtils.getDateAtBeginningOfDay(date);
				long time = (long) cbxTime.getValue();
				date.setTime(date.getTime() + time);
			}
			appointment.setStartDate(date);
			appointment.setRemark(txtRemark.getValue());
			entityService.saveOrUpdate(appointment);
			assignValues(contract);
			clear();
			displaySuccessMsg();
		} else {
			displayErrors();
		}
	}
	
	/**
	 * Validate the appointment form
	 * @return
	 */
	private boolean validate() {
		messagePanel.setVisible(false);
		messagePanel.removeAllComponents();
		errors.clear();
		if (cbxLocation.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("location") }));
		}
		if (cbxBetween.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("between") }));
		}
		return errors.isEmpty();
	}
	/**
	 * Clear all components
	 */
	public void clear() {
		appointment = null;
		cbxLocation.setSelectedEntity(null);
		cbxBetween.setSelectedEntity(null);
		cbxAnd.setSelectedEntity(null);
		cbxTime.setValue(0l);
		dfDate.setValue(null);
		txtRemark.setValue("");
		errors.clear();
		messagePanel.setVisible(false);
		listSelect.clear();
	}
	
	/**
	 * Display Errors
	 */
	public void displayErrors() {
		this.messagePanel.removeAllComponents();
		if (!(this.errors.isEmpty())) {
			for (String error : this.errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				this.messagePanel.addComponent(messageLabel);
			}
			this.messagePanel.setVisible(true);
		}
	}
	/**
	 * 
	 * @param contract
	 * @return
	 */
	private List<Appointment> getAppointmentByContract(Contract contract) {
		BaseRestrictions<Appointment> restrictions = new BaseRestrictions<>(Appointment.class);
		restrictions.addCriterion(Restrictions.eq("contract.id", contract.getId()));
		restrictions.addOrder(Order.asc("startDate"));
		List<Appointment> appointments = entityService.list(restrictions);
		return appointments;
	}
	/**
	 * 
	 * @param appointment
	 * @return
	 */
	private String combineEachAppointment(Appointment appointment) {
		String id = String.valueOf(appointment.getId()) + "/";
		String date = DateUtils.getDateLabel(appointment.getStartDate()) + "/";
		String time = "/";
		if (appointment.getStartDate() != null) {
			long millis = appointment.getStartDate().getTime() - DateUtils.getDateAtBeginningOfDay(appointment.getStartDate()).getTime();										
			time = convertMilisToHour(millis) + "/";
		}
		String between = appointment.getBetween1() != null ? appointment.getBetween1().getDescEn() + "/" : "/";
		String and = appointment.getBetween2() != null ? appointment.getBetween2().getNameEn() + "/" : "/";
		String location = appointment.getLocation() != null ? appointment.getLocation().getDescEn() + "/" : "/";
		String remark = appointment.getRemark();
		return id + date + time + between + and + location + remark;
	}
	/**
	 * 
	 */
	private String convertMilisToHour(Long millis) {
		String hm = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
		return hm;
	}
	/**
	 * style display successmessage save 
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
	public void valueChange(ValueChangeEvent event) {
		String itemSelected = String.valueOf(event.getProperty());
		if (itemSelected != null && !itemSelected.isEmpty()) {
			String[] splitItemSelected = itemSelected.split("/", -1);
			appointment = entityService.getById(Appointment.class, Long.valueOf(splitItemSelected[0]));
			cbxLocation.setSelectedEntity(appointment.getLocation());
			cbxBetween.setSelectedEntity(appointment.getBetween1());
			cbxAnd.setSelectedEntity(appointment.getBetween2());
			if (appointment.getStartDate() != null) {
				cbxTime.setValue(appointment.getStartDate().getTime() - DateUtils.getDateAtBeginningOfDay(appointment.getStartDate()).getTime());
			}
			dfDate.setValue(appointment.getStartDate());
			txtRemark.setValue(appointment.getRemark());
		}
	}
	
	public void deleteAppointment() {
		if (appointment != null) {
			ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("msg.ask.delete"), new ConfirmDialog.Listener() {
				
				/** */
				private static final long serialVersionUID = 1761801926761282629L;
					
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						entityService.delete(appointment);
						assignValues(contract);
						clear();
		            }
				}
			});
			confirmDialog.setWidth("400px");
			confirmDialog.setHeight("150px");
		} else {
			MessageBox mb = new MessageBox(UI.getCurrent(),"280px","150px",I18N.message("information"),
					MessageBox.Icon.WARN,I18N.message("delete.item.not.selected"),Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig[] { new MessageBox.ButtonConfig(MessageBox.ButtonType.OK, I18N.message("ok")) });
			mb.show();
		}
	}

}
