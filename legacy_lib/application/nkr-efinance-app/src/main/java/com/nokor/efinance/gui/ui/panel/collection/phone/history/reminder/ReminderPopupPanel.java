package com.nokor.efinance.gui.ui.panel.collection.phone.history.reminder;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ReminderPopupPanel extends Window{

	/** */
	private static final long serialVersionUID = 1047461659799685732L;
	
	private AutoDateField dfReminder;
	private TextArea txtRemark;
	private Button btnAdd;
	
	/** */
	public ReminderPopupPanel() {
		super(I18N.message(I18N.message("reminder")));
		setModal(true);
		setResizable(false);
		
		createForm();
	}
	
	/** */
	public void createForm() {
		Label lblReminder = new Label(I18N.message("reminder"));
		Label lblRemark = new Label(I18N.message("remark"));
		dfReminder = ComponentFactory.getAutoDateField();
		txtRemark = ComponentFactory.getTextArea(false, 200, 50);
		btnAdd = ComponentLayoutFactory.getButtonIcon(FontAwesome.PLUS);
		
		GridLayout gridLayout = new GridLayout(5, 1);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		gridLayout.addComponent(lblReminder, 0, 0);
		gridLayout.addComponent(dfReminder, 1, 0);
		gridLayout.addComponent(lblRemark, 2, 0);
		gridLayout.addComponent(txtRemark, 3, 0);
		gridLayout.addComponent(btnAdd, 4, 0);
		
		gridLayout.setComponentAlignment(lblReminder, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(dfReminder, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblRemark, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
		
		setContent(gridLayout);
	}
	
	/** */
	public void show() {
		UI.getCurrent().addWindow(this);
	}
}
