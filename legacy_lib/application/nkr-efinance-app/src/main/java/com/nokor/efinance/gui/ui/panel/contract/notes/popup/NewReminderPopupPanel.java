package com.nokor.efinance.gui.ui.panel.contract.notes.popup;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

/**
 * 
 * @author buntha.chea
 *
 */
public class NewReminderPopupPanel extends Window implements ClickListener, FinServicesHelper, CloseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5052122701153932549L;
	
	private AutoDateField dfDate;
	private TextField txtInDay;
	private ComboBox cbxTime;
	private TextArea txtToDo;
	
	private Button btnConfirm;
	private Button btnCancel;
	
	private Contract contract;
	private Reminder reminder;
	
	private VerticalLayout messagePanel;
	private Listener listener;
	
	private List<String> errors;
	
	
	public interface Listener extends Serializable {
        void onClose(NewReminderPopupPanel dialog);
    }
	
	public NewReminderPopupPanel(Contract contract) {
		this.contract = contract;
		setModal(true);
		setCaption(I18N.message("new.reminder"));
		
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		dfDate = ComponentFactory.getAutoDateField();
		txtInDay = ComponentFactory.getTextField(10, 50);
		txtToDo = ComponentFactory.getTextArea(false, 300, 100);
		
		cbxTime = getTimeComboBox();
		cbxTime.setWidth(80, Unit.PIXELS);
		
		btnConfirm = ComponentLayoutFactory.getButtonStyle("confirm", FontAwesome.CHECK, 70, "btn btn-success");
		btnConfirm.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger");
		btnCancel.addClickListener(this);
		
		 // Create a listener for buttons
        Button.ClickListener cb = new Button.ClickListener() {
            private static final long serialVersionUID = 3525060915814334881L;
            public void buttonClick(ClickEvent event) {
              if (errors.isEmpty()) {
            	  if (listener != null) {
                      listener.onClose(NewReminderPopupPanel.this);
                  }
                  UI.getCurrent().removeWindow(NewReminderPopupPanel.this);
              }
            }
        };
		
        btnConfirm.addClickListener(cb);
		
		Label lblDate = ComponentLayoutFactory.getLabelCaptionRequired("date");
		Label lblIn = ComponentLayoutFactory.getLabelCaption("in");
		Label lblDay = ComponentLayoutFactory.getLabelCaption("day");
		Label lblTime = ComponentLayoutFactory.getLabelCaptionRequired("time");
		Label lblToDo = ComponentLayoutFactory.getLabelCaptionRequired("to.do");
		
		HorizontalLayout inDayLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		inDayLayout.addComponent(dfDate);
		inDayLayout.addComponent(lblIn);
		inDayLayout.addComponent(txtInDay);
		inDayLayout.addComponent(lblDay);
		
		GridLayout reminderGridLayout = ComponentLayoutFactory.getGridLayout(5, 3);
		reminderGridLayout.setSpacing(true);
		int iCol = 0;
		reminderGridLayout.addComponent(lblDate, iCol++, 0);
		reminderGridLayout.addComponent(inDayLayout, iCol++, 0);
		
		
		iCol = 0;
		reminderGridLayout.addComponent(lblTime, iCol++, 1);
		reminderGridLayout.addComponent(cbxTime, iCol++, 1);
		
		iCol = 0;
		reminderGridLayout.addComponent(lblToDo, iCol++, 2);
		reminderGridLayout.addComponent(txtToDo, iCol++, 2);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonLayout.addComponent(btnConfirm);
		buttonLayout.addComponent(btnCancel);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainLayout.addComponent(messagePanel);
		mainLayout.addComponent(reminderGridLayout);
		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		
		setContent(mainLayout);
	}
	
	/**
     * Show a modal ConfirmDialog in a window.
     * 
     * @param parentWindow
     * @param listener
     */
    public static NewReminderPopupPanel show(final Contract contract, final Listener listener) {    	
    	NewReminderPopupPanel reminderPopupPanel = new NewReminderPopupPanel(contract);
    	reminderPopupPanel.listener = listener;
        return reminderPopupPanel;
    }
	
	
	/**
	 * assignValues
	 * @param reminder
	 */
	public void assignValues(Reminder reminder) {
		this.reminder = reminder;
		dfDate.setValue(reminder.getDate());
		txtToDo.setValue(reminder.getComment() != null ? reminder.getComment() : "");
		if (reminder.getDate() != null) {
			cbxTime.setValue(reminder.getDate().getTime() - DateUtils.getDateAtBeginningOfDay(reminder.getDate()).getTime());
		}
	}
	
	/**
	 * getTimeComboBox
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
	 * Save
	 */
	private void save() {
		if (validate()) {
			reminder.setDismiss(false);
			reminder.setContract(contract);
			reminder.setComment(txtToDo.getValue());
			
			Date date = dfDate.getValue();
			if (date != null) {
				date = DateUtils.getDateAtBeginningOfDay(date);
				long time = (long) cbxTime.getValue();
				date.setTime(date.getTime() + time);
			}
			
			reminder.setDate(date);
			ENTITY_SRV.saveOrUpdate(reminder);
			ComponentLayoutFactory.displaySuccessfullyMsg();
		}
	}
	
	/**
	 * Validate 
	 * @return
	 */
	public boolean validate() {
		messagePanel.removeAllComponents();
		errors = new ArrayList<>();
		Label messageLabel;

		if (dfDate.getValue() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("date") }));
		}
		
		if (cbxTime.getValue() == null) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("time") }));
		}
		
		if (StringUtils.isEmpty(txtToDo.getValue())) {
			errors.add(I18N.message("field.required.1",new String[] { I18N.message("to.do") }));
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
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnConfirm) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}

	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}

}
