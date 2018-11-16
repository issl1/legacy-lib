package com.nokor.efinance.ra.ui.panel.collections.parameter.sms;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 * Set SMS tab panel in collection RA
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(SMSPanel.NAME)
public class SMSPanel extends AbstractTabPanel implements View, ClickListener {
	
	/** */
	private static final long serialVersionUID = -6221583050469228499L;

	public static final String NAME = "set.sms";
	
	private static String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; "
			+ "border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"150px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td class=\"align-left\" style=\"border:1px solid black;\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	private Button btnAdd;
	
	/**
	 * 
	 * @param caption
	 * @param themeResource
	 * @return
	 */
	private Button getButton(String caption, ThemeResource themeResource) {
		Button button = ComponentFactory.getButton(caption);
		button.setIcon(themeResource);
		button.setStyleName(Runo.BUTTON_SMALL);
		return button;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel() {
		Label label = ComponentFactory.getLabel();
		return label;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		btnAdd = getButton("add", new ThemeResource("../nkr-default/icons/16/add.png"));
		btnAdd.addClickListener(this);
		
        VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		
        contentLayout.addComponent(createSMSStatusTable());
        
        TabSheet mainTab = new TabSheet();
        mainTab.addTab(contentLayout, I18N.message("set.sms"));
		
		return mainTab;
	}

	/**
	 * 
	 * @return
	 */
	private CustomLayout createSMSStatusTable(){
		CustomLayout phoneCustomLayout = new CustomLayout("xxx");
		String phoneTemplate = OPEN_TABLE;
		phoneTemplate += OPEN_TR;
		phoneTemplate += OPEN_TH;
		phoneTemplate += "<div location =\"lblReference\" />";
		phoneTemplate += CLOSE_TH;
		phoneTemplate += OPEN_TH;
		phoneTemplate += "<div location =\"lblSMSTItle\" />";
		phoneTemplate += CLOSE_TH;
		phoneTemplate += OPEN_TH;
		phoneTemplate += "<div location =\"lblPassDueDate\" />";
		phoneTemplate += CLOSE_TH;
		phoneTemplate += "<th class=\"align-center\" width=\"300px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		phoneTemplate += "<div location =\"lblDetail\" />";
		phoneTemplate += CLOSE_TH;
		phoneTemplate += OPEN_TH;
		phoneTemplate += "<div location =\"btnAdd\" />";
		phoneTemplate += CLOSE_TH;
		phoneTemplate += CLOSE_TR;
		phoneCustomLayout.addComponent(new Label(I18N.message("reference")), "lblReference");
		phoneCustomLayout.addComponent(new Label(I18N.message("sms.title")), "lblSMSTItle");
		phoneCustomLayout.addComponent(new Label(I18N.message("pass.due.date")), "lblPassDueDate");
		phoneCustomLayout.addComponent(new Label(I18N.message("detail")), "lblDetail");
		phoneCustomLayout.addComponent(btnAdd, "btnAdd");
		
		for (int i = 0; i < 20; i++) {
			phoneTemplate += OPEN_TR;
			phoneTemplate += OPEN_TD;
			phoneTemplate += "<div location =\"lblReferenceValue" + i + "\" />";
			phoneTemplate += CLOSE_TD;
			phoneTemplate += OPEN_TD;
			phoneTemplate += "<div location =\"lblSMSTItleValue" + i + "\" />";
			phoneTemplate += CLOSE_TD;
			phoneTemplate += OPEN_TD;
			phoneTemplate += "<div location =\"lblPassDueDateValue" + i + "\" />";
			phoneTemplate += CLOSE_TD;
			phoneTemplate += OPEN_TD;
			phoneTemplate += "<div location =\"lblDetailValue" + i + "\" />";
			phoneTemplate += CLOSE_TD;
			phoneTemplate += OPEN_TD;
			phoneTemplate += "<div location =\"btnLayout" + i + "\" />";
			phoneTemplate += CLOSE_TD;
			phoneTemplate += CLOSE_TR;
			
			Label lblReferenceValue = getLabel();
			Label lblSMSTItleValue = getLabel();
			Label lblPassDueDateValue = getLabel();
			Label lblDetailValue = getLabel();
			Button btnEdit = getButton("edit", new ThemeResource("../nkr-default/icons/16/edit.png"));
			Button btnDelete = getButton("delete", new ThemeResource("../nkr-default/icons/16/close.png"));
			btnEdit.addClickListener(new ClickListener() {

				/** */
				private static final long serialVersionUID = -7560785620756196010L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().addWindow(windowSMSStatus());
				}
			});
			btnDelete.addClickListener(new ClickListener() {

				/** */
				private static final long serialVersionUID = 2561874478169343816L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
							new String[] {}), new ConfirmDialog.Listener() {
								
							/** */
							private static final long serialVersionUID = 8058640578859529246L;

							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									dialog.close();
					            }
							}
						});
						confirmDialog.setWidth("400px");
						confirmDialog.setHeight("150px");
				}
			});
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setSpacing(true);
			buttonLayout.addComponent(btnEdit);
			buttonLayout.addComponent(btnDelete);
			
			lblReferenceValue.setValue(getDefaultString("0000" + (i + 1)));
			lblSMSTItleValue.setValue(getDefaultString(""));
			lblPassDueDateValue.setValue(getDefaultString(15));
			lblDetailValue.setValue(getDefaultString("Warning! Pease make #AMT Baht payment installment before #DATE, #TIME"));
			
			phoneCustomLayout.addComponent(lblReferenceValue, "lblReferenceValue" + i);
			phoneCustomLayout.addComponent(lblSMSTItleValue, "lblSMSTItleValue" + i);
			phoneCustomLayout.addComponent(lblPassDueDateValue, "lblPassDueDateValue" + i);
			phoneCustomLayout.addComponent(lblDetailValue, "lblDetailValue" + i);
			phoneCustomLayout.addComponent(buttonLayout, "btnLayout" + i);
		}
		
		phoneTemplate += CLOSE_TABLE;
		phoneCustomLayout.setTemplateContents(phoneTemplate);
		
		return phoneCustomLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Window windowSMSStatus() {
		final Window windowSMSStatus = new Window();
		windowSMSStatus.setModal(true);
		windowSMSStatus.setResizable(false);
	    windowSMSStatus.setWidth(430, Unit.PIXELS);
	    windowSMSStatus.setHeight(350, Unit.PIXELS);
	    windowSMSStatus.setCaption(I18N.message("phone"));
	    Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 6247870703265744925L;

			public void buttonClick(ClickEvent event) {
				windowSMSStatus.close();
            }
        });
        btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -5947359851318579078L;

			public void buttonClick(ClickEvent event) {
				windowSMSStatus.close();
			}
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));	
		
		TextField txtSMSCode = ComponentFactory.getTextField("sms.code", false, 60, 150);
		TextField txtSMSTitle = ComponentFactory.getTextField("sms.title", false, 60, 150);
		TextField txtPassDueDate = ComponentFactory.getTextField("pass.due.date", false, 60, 150);
		TextArea txtDetail = ComponentFactory.getTextArea("detail", false, 230, 100);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.addComponent(txtSMSCode);
		formLayout.addComponent(txtSMSTitle);
		formLayout.addComponent(txtPassDueDate);
		formLayout.addComponent(txtDetail);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		windowSMSStatus.setContent(verticalLayout);
		return windowSMSStatus;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			UI.getCurrent().addWindow(windowSMSStatus());
		} 
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
