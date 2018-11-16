package com.nokor.efinance.gui.ui.panel.contract.notes.appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Appointment;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.contract.notes.request.ColRequestFormPanel;
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * appointment form panel
 * @author buntha.chea
 */
public class AppointmentFormPanel extends VerticalLayout implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -4127939593851615582L;
	
	private AppointmentsTablePanel appointmentTablePanel;
	
	private ERefDataComboBox<ELocation> cbxLocation;
	private ERefDataComboBox<EApplicantType> cbxBetween;
	private EntityComboBox<OrgStructure> cbxAnd;
	private ComboBox cbxTime;
	
	private AutoDateField dfDate;
	private TextArea txtRemark;
	
	private VerticalLayout messagePanel;
	
	private Button btnSave;
	
	private Appointment appointment;
	
	private Contract contract;
	
	private final static Logger logger = LoggerFactory.getLogger(ColRequestFormPanel.class);
	
	/**
	 * 
	 * @param caption
	 */
	public AppointmentFormPanel(String caption) {
		if (!"null".equals(caption) && StringUtils.isNotEmpty(caption)) {
			setCaption("<b>" + I18N.message(caption) + "</b>");
		}
		setCaptionAsHtml(true);
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		appointmentTablePanel = new AppointmentsTablePanel(this);
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		setSpacing(true);
		addComponent(messagePanel);
		addComponent(createForm());
		addComponent(appointmentTablePanel);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		List<String> errors = new ArrayList<>();
		Label messageLabel;

		if (cbxLocation.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("location") }));
		}
		
		if (cbxBetween.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("between") }));
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
	 * create form
	 * @return
	 */
	private Component createForm() {
		cbxLocation = new ERefDataComboBox<>(ELocation.values());
		cbxLocation.setWidth(130, Unit.PIXELS);
		
		cbxBetween = new ERefDataComboBox<>(EApplicantType.values());
		cbxBetween.setWidth(110, Unit.PIXELS);
		
		cbxAnd = new EntityComboBox<>(OrgStructure.class, "nameEn");
		cbxAnd.setImmediate(true);
		cbxAnd.setEntities(getDepartment());
		cbxAnd.setWidth(110, Unit.PIXELS);
		
		dfDate = ComponentFactory.getAutoDateField();
		txtRemark = ComponentFactory.getTextArea(false, 250, 50);
		cbxTime = getTimeComboBox();
		cbxTime.setWidth(80, Unit.PIXELS);
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		
		Label lblLocation = ComponentLayoutFactory.getLabelCaptionRequired("location");
		Label lblBetween = ComponentLayoutFactory.getLabelCaptionRequired("between");
		Label lblAnd = ComponentFactory.getLabel(I18N.message("and"));
		Label lblDate = ComponentFactory.getLabel(I18N.message("date"));
		Label lblTime = ComponentFactory.getLabel(I18N.message("time"));
		Label lblRemark = ComponentFactory.getLabel(I18N.message("remark"));
		
		GridLayout gridLayout = new GridLayout(10, 1);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		gridLayout.addComponent(lblLocation, 0, 0);
		gridLayout.addComponent(cbxLocation, 1, 0);
		gridLayout.addComponent(lblBetween, 2, 0);
		gridLayout.addComponent(cbxBetween, 3, 0);
		gridLayout.addComponent(lblAnd, 4, 0);
		gridLayout.addComponent(cbxAnd, 5, 0);
		gridLayout.addComponent(lblDate, 6, 0);
		gridLayout.addComponent(dfDate, 7, 0);
		gridLayout.addComponent(lblTime, 8, 0);
		gridLayout.addComponent(cbxTime, 9, 0);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(new MarginInfo(false, true, true, true));
		horizontalLayout.addComponent(lblRemark);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(20, Unit.PIXELS));
		horizontalLayout.addComponent(txtRemark);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horizontalLayout.addComponent(btnSave);
		
		horizontalLayout.setComponentAlignment(lblRemark, Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_LEFT);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(gridLayout);
		verticalLayout.addComponent(horizontalLayout);
		
		gridLayout.setComponentAlignment(lblLocation, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblBetween, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblTime, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAnd, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblDate, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbxAnd, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbxBetween, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbxTime, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(dfDate, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(cbxLocation, Alignment.MIDDLE_LEFT);
		
		Panel mainPanel = new Panel(verticalLayout);
		return mainPanel;
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
	
	/**
	 * 
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
	 * Save Appointment
	 */
	private void save() {
		if (appointment == null) {
			appointment = EntityFactory.createInstance(Appointment.class);
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
		try {
			NOTE_SRV.saveOrUpdate(appointment);
			ComponentLayoutFactory.displaySuccessfullyMsg();
			appointment = null;
			appointmentTablePanel.assignValues(contract);
			reset();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		appointmentTablePanel.assignValues(contract);
	}
	
	/**
	 * 
	 * @param appointment
	 */
	public void assignValues(Appointment appointment) {
		this.appointment = appointment;
		if (appointment != null) {
			cbxLocation.setSelectedEntity(appointment.getLocation());
			cbxBetween.setSelectedEntity(appointment.getBetween1());
			cbxAnd.setSelectedEntity(appointment.getBetween2());
			dfDate.setValue(appointment.getStartDate());
			if (appointment.getStartDate() != null) {
				cbxTime.setValue(appointment.getStartDate().getTime() - DateUtils.getDateAtBeginningOfDay(appointment.getStartDate()).getTime());
			}
			txtRemark.setValue(appointment.getRemark());
		} else {
			reset();
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if(validate()) {
				save();
			}
		}
	}
	
	/**
	 * reset
	 */
	public void reset() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		cbxLocation.setSelectedEntity(null);
		cbxBetween.setSelectedEntity(null);
		cbxAnd.setSelectedEntity(null);
		cbxTime.setValue(0l);
		dfDate.setValue(null);
		txtRemark.setValue("");
	}

}
