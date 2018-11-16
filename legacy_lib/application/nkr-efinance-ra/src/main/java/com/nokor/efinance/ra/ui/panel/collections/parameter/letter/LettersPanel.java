package com.nokor.efinance.ra.ui.panel.collections.parameter.letter;

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
import com.vaadin.ui.ComboBox;
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
 * Set Letter tab panel in collection RA
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(LettersPanel.NAME)
public class LettersPanel extends AbstractTabPanel implements View, ClickListener {
	
	/** */
	private static final long serialVersionUID = 9169883953049206474L;

	public static final String NAME = "set.letters";
	
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
		
        contentLayout.addComponent(createLetterStatusTable());
        
        TabSheet mainTab = new TabSheet();
        mainTab.addTab(contentLayout, I18N.message("set.letters"));
		
		return mainTab;
	}

	/**
	 * 
	 * @return
	 */
	private CustomLayout createLetterStatusTable(){
		CustomLayout letterCustomLayout = new CustomLayout("xxx");
		String letterTemplate = OPEN_TABLE;
		letterTemplate += OPEN_TR;
		letterTemplate += OPEN_TH;
		letterTemplate += "<div location =\"lblLetterCode\" />";
		letterTemplate += CLOSE_TH;
		letterTemplate += OPEN_TH;
		letterTemplate += "<div location =\"lblPassDueDate\" />";
		letterTemplate += CLOSE_TH;
		letterTemplate += OPEN_TH;
		letterTemplate += "<div location =\"lblResult\" />";
		letterTemplate += CLOSE_TH;
		letterTemplate += "<th class=\"align-center\" width=\"300px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		letterTemplate += "<div location =\"lblDetail\" />";
		letterTemplate += CLOSE_TH;
		letterTemplate += OPEN_TH;
		letterTemplate += "<div location =\"btnAdd\" />";
		letterTemplate += CLOSE_TH;
		letterTemplate += CLOSE_TR;
		letterCustomLayout.addComponent(new Label(I18N.message("letter.code")), "lblLetterCode");
		letterCustomLayout.addComponent(new Label(I18N.message("pass.due.date")), "lblPassDueDate");
		letterCustomLayout.addComponent(new Label(I18N.message("result")), "lblResult");
		letterCustomLayout.addComponent(new Label(I18N.message("detail")), "lblDetail");
		letterCustomLayout.addComponent(btnAdd, "btnAdd");
		
		for (int i = 0; i < 20; i++) {
			letterTemplate += OPEN_TR;
			letterTemplate += OPEN_TD;
			letterTemplate += "<div location =\"lblLetterCodeValue" + i + "\" />";
			letterTemplate += CLOSE_TD;
			letterTemplate += OPEN_TD;
			letterTemplate += "<div location =\"lblPassDueDateValue" + i + "\" />";
			letterTemplate += CLOSE_TD;
			letterTemplate += OPEN_TD;
			letterTemplate += "<div location =\"lblResultValue" + i + "\" />";
			letterTemplate += CLOSE_TD;
			letterTemplate += OPEN_TD;
			letterTemplate += "<div location =\"lblDetailValue" + i + "\" />";
			letterTemplate += CLOSE_TD;
			letterTemplate += OPEN_TD;
			letterTemplate += "<div location =\"btnLayout" + i + "\" />";
			letterTemplate += CLOSE_TD;
			letterTemplate += CLOSE_TR;
			
			Label lblLetterCodeValue = getLabel();
			Label lblPassDueDateValue = getLabel();
			Label lblResultValue = getLabel();
			Label lblDetailValue = getLabel();
			Button btnEdit = getButton("edit", new ThemeResource("../nkr-default/icons/16/edit.png"));
			Button btnDelete = getButton("delete", new ThemeResource("../nkr-default/icons/16/close.png"));
			btnEdit.addClickListener(new ClickListener() {

				/** */
				private static final long serialVersionUID = -8933162214293305260L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					UI.getCurrent().addWindow(windowLetterStatus());
				}
			});
			btnDelete.addClickListener(new ClickListener() {

				/** */
				private static final long serialVersionUID = -4955292427305053055L;

				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
							new String[] {}), new ConfirmDialog.Listener() {
								
							/** */
							private static final long serialVersionUID = 3099186833581152931L;

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
			
			lblLetterCodeValue.setValue(getDefaultString("0000" + (i + 1)));
			lblResultValue.setValue(getDefaultString("P11 no answer"));
			lblPassDueDateValue.setValue(getDefaultString(15));
			lblDetailValue.setValue(getDefaultString("Warning! Pease make #AMT Baht payment installment before #DATE, #TIME"));
			
			letterCustomLayout.addComponent(lblLetterCodeValue, "lblLetterCodeValue" + i);
			letterCustomLayout.addComponent(lblPassDueDateValue, "lblPassDueDateValue" + i);
			letterCustomLayout.addComponent(lblResultValue, "lblResultValue" + i);
			letterCustomLayout.addComponent(lblDetailValue, "lblDetailValue" + i);
			letterCustomLayout.addComponent(buttonLayout, "btnLayout" + i);
		}
		
		letterTemplate += CLOSE_TABLE;
		letterCustomLayout.setTemplateContents(letterTemplate);
		
		return letterCustomLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private Window windowLetterStatus() {
		final Window windowLetterStatus = new Window();
		windowLetterStatus.setModal(true);
		windowLetterStatus.setResizable(false);
	    windowLetterStatus.setWidth(430, Unit.PIXELS);
	    windowLetterStatus.setHeight(300, Unit.PIXELS);
	    windowLetterStatus.setCaption(I18N.message("phone"));
	    Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 6757984496633888003L;

			public void buttonClick(ClickEvent event) {
				windowLetterStatus.close();
            }
        });
        btnCancel.setIcon(new ThemeResource("../nkr-default/icons/16/delete.png"));
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -349051871881186730L;

			public void buttonClick(ClickEvent event) {
				windowLetterStatus.close();
			}
        });
		btnSave.setIcon(new ThemeResource("../nkr-default/icons/16/save.png"));	
		
		TextField txtLetterCode = ComponentFactory.getTextField("letter.code", false, 60, 150);
		ComboBox cbxPassDueDate = ComponentFactory.getComboBox("pass.due.date", null);
		TextArea txtDetail = ComponentFactory.getTextArea("detail", false, 230, 100);
		
		FormLayout formLayout = new FormLayout();
		formLayout.setMargin(true);
		formLayout.addComponent(txtLetterCode);
		formLayout.addComponent(cbxPassDueDate);
		formLayout.addComponent(txtDetail);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(formLayout);
		windowLetterStatus.setContent(verticalLayout);
		return windowLetterStatus;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			UI.getCurrent().addWindow(windowLetterStatus());
		} 
	}
	
	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
}
