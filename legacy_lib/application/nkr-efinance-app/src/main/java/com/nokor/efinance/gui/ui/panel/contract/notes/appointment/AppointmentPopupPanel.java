package com.nokor.efinance.gui.ui.panel.contract.notes.appointment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.EntityFactory;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.applicant.model.EApplicantType;
import com.nokor.efinance.core.contract.model.Appointment;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.ersys.core.hr.model.eref.EOrgLevel;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * Appointments pop up panel
 * @author buntha.chea
 */
public class AppointmentPopupPanel extends Window implements ClickListener, FinServicesHelper, CloseListener {

	/** */
	private static final long serialVersionUID = 1032050276064909393L;

	private NativeButton btnSave;
	private NativeButton btnClose;
	
	private ERefDataComboBox<ELocation> cbxLocation;
	private ERefDataComboBox<EApplicantType> cbxBetween;
	private EntityComboBox<OrgStructure> cbxAnd;
	private ComboBox cbxTime;
	
	private AutoDateField dfDate;
	private TextArea txtRemark;
	
	private Appointment appointment;
	protected List<String> errors;
	private VerticalLayout messagePanel;
	
	private Contract contract;
	private Listener listener = null;
	
	public interface Listener extends Serializable {
        void onClose(AppointmentPopupPanel dialog);
    }
	
	/**
     * @param parentWindow
     * @param listener
     */
    public static AppointmentPopupPanel show(final Contract contract, final Listener listener) {    	
    	AppointmentPopupPanel appointmentPopupPanel = new AppointmentPopupPanel(contract);
    	appointmentPopupPanel.listener = listener;
    	UI.getCurrent().addWindow(appointmentPopupPanel);
        return appointmentPopupPanel;
    }
    
	/**
	 * 
	 * @param contract
	 * @param notesTablePanel
	 */
	private AppointmentPopupPanel(Contract contract) {
		this.contract = contract;
		setCaption(I18N.message("appointment"));
		setModal(true);
		setResizable(false);
		addCloseListener(this);
		init();
	}
	
	/**
	 * inti
	 */
	private void init() {
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.addClickListener(this);
		btnSave.setIcon(FontAwesome.SAVE);
	     
		btnClose = new NativeButton(I18N.message("close"));
		btnClose.addClickListener(this);
		btnClose.setIcon(FontAwesome.TIMES);
    
        messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
        
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
	 * 
	 * @param contractNote
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
	 * @return
	 */
	private VerticalLayout createForm() {
		cbxLocation = new ERefDataComboBox<>(I18N.message("location"), ELocation.values());
		cbxLocation.setRequired(true);
		cbxBetween = new ERefDataComboBox<>(I18N.message("between"), EApplicantType.values());
		cbxBetween.setRequired(true);
		
		cbxAnd = new EntityComboBox<>(OrgStructure.class, "nameEn");
		cbxAnd.setImmediate(true);
		cbxAnd.setEntities(getDepartment());
		cbxAnd.setCaption(I18N.message("and"));
		
		dfDate = ComponentFactory.getAutoDateField(I18N.message("date"), false);
		txtRemark = ComponentFactory.getTextArea("remark", false, 215, 80);
		cbxTime = getTimeComboBox();
		cbxTime.setCaption(I18N.message("time"));	
		
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft();
		frmLayout.addComponent(cbxLocation);
		frmLayout.addComponent(cbxBetween);
		frmLayout.addComponent(cbxAnd);
		frmLayout.addComponent(dfDate);
		frmLayout.addComponent(cbxTime);
		frmLayout.addComponent(txtRemark);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		
		contentLayout.addComponent(frmLayout);
		return contentLayout;
	}
	
	/**
	 * Validate 
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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			if (validate()) {
				save();
				if (listener != null) {
                    listener.onClose(AppointmentPopupPanel.this);
                }
                UI.getCurrent().removeWindow(AppointmentPopupPanel.this);
			}
		} else if (event.getButton() == btnClose) {
			close();
		}
	}
	
	/**
	 * Save
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
		NOTE_SRV.saveOrUpdate(appointment);
		close();
	}
	
	/**
	 * Reset the form
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
	 * 
	 * @return
	 */
	private List<OrgStructure> getDepartment() {
		BaseRestrictions<OrgStructure> restrictions = new BaseRestrictions<>(OrgStructure.class);
		restrictions.addCriterion(Restrictions.eq("level", EOrgLevel.DEPARTMENT));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}
	
}
