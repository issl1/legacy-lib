package com.nokor.efinance.gui.ui.panel.collection.phone.history.reminder;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.Reminder;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.quotation.model.Comment;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.MainUI;
import com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.ColContractHistoryFormPanel;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ReminderFormPanel extends AbstractTabPanel implements ValueChangeListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -6471629448114082911L;

	private static final String COMMENT = I18N.message("comment");
	private static final String REMINDER = I18N.message("reminder");
	
	private OptionGroup optionGroup;
	private TextArea txtReminder;
	private Button btnSave;
	private AutoDateField dfReminder;
	
	private Reminder reminder;
	private Comment comment;
	private Contract contract;
	
	private Label lblDateRequired;
	
	private ColContractHistoryFormPanel deleget;
	
	/**
	 * @param deleget the deleget to set
	 */
	public void setDeleget(ColContractHistoryFormPanel deleget) {
		this.deleget = deleget;
	}

	@PostConstruct
	public void PostConstract() {
		super.init();
		super.setMargin(false);
		setSizeFull();
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		List<String> options = Arrays.asList(new String[] {COMMENT, REMINDER});
		
		optionGroup = new OptionGroup();
		optionGroup = ComponentLayoutFactory.getOptionGroup(options);
		optionGroup.select(REMINDER);
		optionGroup.addValueChangeListener(this);
		
		txtReminder = ComponentFactory.getTextArea(false, 300, 50);
		
		Label lblReminder = ComponentLayoutFactory.getLabelCaptionRequired("remark");
		
		dfReminder = ComponentFactory.getAutoDateField();
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -6279647903542319966L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
					if (optionGroup.getValue().equals(COMMENT)) {
						saveComment();
					} else if (optionGroup.getValue().equals(REMINDER)) {
						saveReminder();
						
					}
				
			}
		});
		
		lblDateRequired = ComponentLayoutFactory.getLabelCaptionRequired(StringUtils.EMPTY);
		lblDateRequired.setVisible(true);
		
		GridLayout gridLayout = new GridLayout(6, 1);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(optionGroup, iCol++, 0);
		gridLayout.addComponent(lblDateRequired, iCol++, 0);
		gridLayout.addComponent(dfReminder, iCol++, 0);
		gridLayout.addComponent(lblReminder, iCol++, 0);
		gridLayout.addComponent(txtReminder, iCol++, 0);
		gridLayout.addComponent(btnSave, iCol++, 0);
		gridLayout.setComponentAlignment(optionGroup, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblDateRequired, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblReminder, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(dfReminder, Alignment.MIDDLE_LEFT);
		
		return gridLayout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		reset();
	}
	
	/**
	 * 
	 */
	public void reset() {
		reminder = null;
		comment = null;
		optionGroup.select(REMINDER);
		txtReminder.setValue(StringUtils.EMPTY);
		dfReminder.setValue(null);
	}
	
	/**
	 * 
	 * @param isEnabled
	 */
	private void setEnabledControls(boolean isEnabled) {
		dfReminder.setEnabled(isEnabled);
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(optionGroup)) {
			if (optionGroup.getValue().equals(COMMENT)) {
				setEnabledControls(false);
				dfReminder.setValue(null);
				txtReminder.setValue("");
				lblDateRequired.setVisible(false);
			} else if (optionGroup.getValue().equals(REMINDER)) {
				setEnabledControls(true);
				txtReminder.setValue("");
				lblDateRequired.setVisible(true);
			}
		}
	}
	
	/** */
	public void saveReminder() {
		SecUser user = UserSessionManager.getCurrentUser();
		if (reminder == null) {
			reminder = Reminder.createInstance();
			reminder.setContract(contract);
		}
		reminder.setDate(dfReminder.getValue());
		reminder.setComment(txtReminder.getValue());
		reminder.setSecUser(user);
		
		try {
			if (StringUtils.isNotEmpty(txtReminder.getValue()) && dfReminder.getValue() != null) {
				REMINDER_SRV.saveOrUpdateReminder(reminder);
				ComponentLayoutFactory.displaySuccessfullyMsg();
				reset();
				deleget.assignReminders();
				MainUI.mainUI.createReminederButton();
			} else {
				if (dfReminder.getValue() == null) {
					ComponentLayoutFactory.displayErrorMsg(I18N.message("field.required.1", new String[] { I18N.message("date") }));
				} else {
					ComponentLayoutFactory.displayErrorMsg(I18N.message("field.required.1", new String[] { I18N.message("remark") }));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/** */
	public void saveComment() {
		if (comment == null) {
			comment = new Comment();
			comment.setContract(contract);
		}
		comment.setDesc(txtReminder.getValue());
		
		try {
			if (StringUtils.isNotEmpty(txtReminder.getValue())) {
				REMINDER_SRV.saveOrUpdateComment(comment);
				ComponentLayoutFactory.displaySuccessfullyMsg();
				reset();
			} else {
				ComponentLayoutFactory.displayErrorMsg(I18N.message("field.required.1", new String[] { I18N.message("remark") }));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
}
