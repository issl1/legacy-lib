package com.nokor.efinance.ra.ui.panel.collections.parameter.assignmentrule.debtlevel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.ColDebtLevel;
import com.nokor.efinance.core.shared.collection.CollectionEntityField;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;
/**
 * Debt level tab in assignment rule
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DebtLevelPanel extends AbstractTabPanel implements ClickListener, ValueChangeListener, CollectionEntityField, FrmkServicesHelper {

	/** */
	private static final long serialVersionUID = -6374264502609852283L;
	
	public static final String NAME = "debt.levels";
	
	private static String OPEN_TABLE = "<table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"5\" style=\"border:1px solid black; "
			+ "border-collapse:collapse;\" >";
	private static String OPEN_TR = "<tr>";
	private static String OPEN_TH = "<th class=\"align-center\" width=\"200px\" bgcolor=\"#e1e1e1\" "
			+ "style=\"border:1px solid black;\" >";
	private static String OPEN_TD = "<td class=\"align-left\" style=\"border:1px solid black;\" >";
	private static String CLOSE_TR = "</tr>";
	private static String CLOSE_TH = "</th>";
	private static String CLOSE_TD = "</td>";
	private static String CLOSE_TABLE = "</table>";
	
	private CustomLayout debtLevelCustomLayout;
	private String debtLevelTemplate;
	private VerticalLayout mainLayout;
	private Button btnAdd;
	
	private VerticalLayout messagePanel;
	private List<String> lstErrors;
	private TextField txtDebtLevel;
	private ComboBox cbxDueDate;
	private ComboBox cbxAssignmentDate;
	private ComboBox cbxAssignmentHour; 
	private CheckBox cbAllDueDate;
	private CheckBox cbAllAssignmentDate;
	private CheckBox cbAllAssignmentHour;
	
	Notification notification;
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel() {
		Label label = new Label();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @param themeResource
	 * @return
	 */
	private Button getButton(String caption, FontAwesome fontAwesome) {
		Button button = ComponentFactory.getButton(caption);
		button.setIcon(fontAwesome);
		button.setStyleName(Runo.BUTTON_SMALL);
		return button;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		btnAdd = getButton("add", FontAwesome.PLUS);
		btnAdd.addClickListener(this);
		
		notification = new Notification("");
		notification.setDelayMsec(1000);
		
		mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setHeight(750, Unit.PIXELS);
		mainLayout.addComponent(createDebtLevelTable());
		
		return mainLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColDebtLevel> getColDebtLevels() {
		BaseRestrictions<ColDebtLevel> restrictions = new BaseRestrictions<>(ColDebtLevel.class);
		restrictions.addOrder(Order.asc(DEBT_LEVEL));
		return ENTITY_SRV.list(restrictions);
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout createDebtLevelTable() {
		debtLevelCustomLayout = new CustomLayout("xxx");
		debtLevelTemplate = OPEN_TABLE;
		debtLevelTemplate += OPEN_TR;
		debtLevelTemplate += OPEN_TH;
		debtLevelTemplate += "<div location =\"lblDebtLevel\" />";
		debtLevelTemplate += CLOSE_TH;
		debtLevelTemplate += "<th class=\"align-center\" width=\"250px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		debtLevelTemplate += "<div location =\"lblDueDate\" />";
		debtLevelTemplate += CLOSE_TH;
		debtLevelTemplate += "<th class=\"align-center\" width=\"250px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		debtLevelTemplate += "<div location =\"lblAssignmentDate\" />";
		debtLevelTemplate += CLOSE_TH;
		debtLevelTemplate += "<th class=\"align-center\" width=\"300px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		debtLevelTemplate += "<div location =\"lblAssignmentHour\" />";
		debtLevelTemplate += CLOSE_TH;
		debtLevelTemplate += "<th class=\"align-center\" width=\"150px\" bgcolor=\"#e1e1e1\" "
				+ "style=\"border:1px solid black;\" >";
		debtLevelTemplate += "<div location =\"btnAdd\" />";
		debtLevelTemplate += CLOSE_TH;
		debtLevelTemplate += CLOSE_TR;
		debtLevelCustomLayout.addComponent(new Label(I18N.message("debt.level")), "lblDebtLevel");
		debtLevelCustomLayout.addComponent(new Label(I18N.message("due.date")), "lblDueDate");
		debtLevelCustomLayout.addComponent(new Label(I18N.message("assignment.date")), "lblAssignmentDate");
		debtLevelCustomLayout.addComponent(new Label(I18N.message("assignment.hour")), "lblAssignmentHour");
		debtLevelCustomLayout.addComponent(btnAdd, "btnAdd");
	
		assignValue(getColDebtLevels());
		
		return debtLevelCustomLayout;
	}

	/**
	 * 
	 * @param colDebtLevels
	 */
	private void assignValue(List<ColDebtLevel> colDebtLevels) {
		if (colDebtLevels != null && !colDebtLevels.isEmpty()) {
			int i = 1;
			for (final ColDebtLevel colDebtLevel : colDebtLevels) {
				i++;
				debtLevelTemplate += OPEN_TR;
				debtLevelTemplate += OPEN_TD;
				debtLevelTemplate += "<div location =\"lblDebtLevelValue" + i + "\" />";
				debtLevelTemplate += CLOSE_TD;
				debtLevelTemplate += OPEN_TD;
				debtLevelTemplate += "<div location =\"lblDueDateValue" + i + "\" />";
				debtLevelTemplate += CLOSE_TD;
				debtLevelTemplate += OPEN_TD;
				debtLevelTemplate += "<div location =\"lblAssignmentDateValue" + i + "\" />";
				debtLevelTemplate += CLOSE_TD;
				debtLevelTemplate += OPEN_TD;
				debtLevelTemplate += "<div location =\"lblAssignmentHourValue" + i + "\" />";
				debtLevelTemplate += CLOSE_TD;
				debtLevelTemplate += "<td class=\"align-center\" style=\"border:1px solid black;\" >";
				debtLevelTemplate += "<div location =\"btnLayout" + i + "\" />";
				debtLevelTemplate += CLOSE_TD;
				debtLevelTemplate += CLOSE_TR;
				
				Label lblDebtLevelValue = getLabel();
				Label lblDueDateValue = getLabel();
				Label lblAssignmentDateValue = getLabel();
				Label lblAssignmentHourValue = getLabel();
				Button btnEdit = getButton("", FontAwesome.EDIT);
				Button btnDelete = getButton("", FontAwesome.TIMES);
				btnEdit.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = 1761801926761282629L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						UI.getCurrent().addWindow(windowDebtLevel(colDebtLevel, false));
					}
				});
				btnDelete.addClickListener(new ClickListener() {
					
					/** */
					private static final long serialVersionUID = 7127204587528971199L;

					/**
					 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
					 */
					@Override
					public void buttonClick(ClickEvent event) {
						ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("delete.mgs.single",
								new String[] {colDebtLevel.getId().toString()}), new ConfirmDialog.Listener() {
									
								/** */
								private static final long serialVersionUID = 1761801926761282629L;
								
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										logger.debug("[>> deleteDebtLevel]");
										
										ENTITY_SRV.delete(colDebtLevel);
										
										logger.debug("This item " + colDebtLevel.getId() + "deleted successfully !");
										logger.debug("[<< deleteDebtLevel]");
										mainLayout.removeAllComponents();
										mainLayout.addComponent(createDebtLevelTable());
										dialog.close();
										MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
												MessageBox.Icon.INFO, I18N.message("item.deleted.successfully", 
												new String[]{colDebtLevel.getId().toString()}), Alignment.MIDDLE_RIGHT,
												new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
										mb.show();
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
				
				lblDebtLevelValue.setValue(getDefaultString(colDebtLevel.getDeptLevel()));
				lblDueDateValue.setValue(colDebtLevel.getDueDate());
				lblAssignmentDateValue.setValue(colDebtLevel.getAssignmentDate());
				lblAssignmentHourValue.setValue(colDebtLevel.getAssignmentHour());
				debtLevelCustomLayout.addComponent(lblDebtLevelValue, "lblDebtLevelValue" + i);
				debtLevelCustomLayout.addComponent(lblDueDateValue, "lblDueDateValue" + i);
				debtLevelCustomLayout.addComponent(lblAssignmentDateValue, "lblAssignmentDateValue" + i);
				debtLevelCustomLayout.addComponent(lblAssignmentHourValue, "lblAssignmentHourValue" + i);
				debtLevelCustomLayout.addComponent(buttonLayout, "btnLayout" + i);
			}
		}
		debtLevelTemplate += CLOSE_TABLE;
		debtLevelCustomLayout.setTemplateContents(debtLevelTemplate);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnAdd)) {
			ColDebtLevel debtLevel = new ColDebtLevel();
			UI.getCurrent().addWindow(windowDebtLevel(debtLevel, true));
		} 
	}
	
	/**
	 * 
	 * @param debtLevel
	 * @param isAddNew
	 * @return
	 */
	private Window windowDebtLevel(final ColDebtLevel debtLevel, final boolean isAddNew) {
		final Window windowDebtLevel = new Window();
		windowDebtLevel.setModal(true);
		windowDebtLevel.setResizable(false);
	    windowDebtLevel.setWidth(340, Unit.PIXELS);
	    windowDebtLevel.setHeight(280, Unit.PIXELS);
	    windowDebtLevel.setCaption(I18N.message("debt.level"));
	    
	    txtDebtLevel = ComponentFactory.getTextField(false, 60, 80);
		cbxDueDate = ComponentFactory.getComboBox();
		cbxDueDate.setNullSelectionAllowed(false);
		cbxAssignmentDate = ComponentFactory.getComboBox();
		cbxAssignmentDate.setNullSelectionAllowed(false);
		cbxAssignmentHour = ComponentFactory.getComboBox();
		cbxAssignmentHour.setNullSelectionAllowed(false);
		cbAllDueDate = new CheckBox(I18N.message("all"));
		cbAllAssignmentDate = new CheckBox(I18N.message("all"));
		cbAllAssignmentHour = new CheckBox(I18N.message("all"));
	
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		lstErrors = new ArrayList<String>();
		
	    Button btnCancel = new NativeButton(I18N.message("cancel"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = 2517613798105707236L;

			public void buttonClick(ClickEvent event) {
				windowDebtLevel.close();
            }
        });
        btnCancel.setIcon(FontAwesome.TIMES);
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -2182871630680823499L;

			@SuppressWarnings("static-access")
			public void buttonClick(ClickEvent event) {
				debtLevel.setDeptLevel(getInteger(txtDebtLevel) != null ? getInteger(txtDebtLevel) : 0);
				debtLevel.setDueDate(cbxDueDate.getValue() == null ? "All" : cbxDueDate.getValue().toString());
				debtLevel.setAssignmentDate(cbxAssignmentDate.getValue() == null ? "All" : String.valueOf(cbxAssignmentDate.getValue()));
				debtLevel.setAssignmentHour(cbxAssignmentHour.getValue() == null ? "All" : String.valueOf(cbxAssignmentHour.getValue()));
				lstErrors.clear();
				if (validateControls()) {
					logger.debug("[>> saveOrUpdateDebtLevel]");
					
					ENTITY_SRV.saveOrUpdate(debtLevel);
					
					logger.debug("[<< saveOrUpdateDebtLevel]");
					mainLayout.removeAllComponents();
					mainLayout.addComponent(createDebtLevelTable());
					resetControls();
					windowDebtLevel.close();
					notification.show("", I18N.message("save.successfully"), Notification.Type.HUMANIZED_MESSAGE);
				} else {
					displayAllErrorsPanel();
				}
			}
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		cbxDueDate.setWidth(80, Unit.PIXELS);
		cbxAssignmentDate.setWidth(80, Unit.PIXELS);
		cbxAssignmentHour.setWidth(80, Unit.PIXELS);
		
		for (int i = 1; i <= 30; i++) {
			if (i <= 20) {
				cbxDueDate.addItem(i + "");	
			}
			cbxAssignmentDate.addItem(i + "");
		}
		List<String> hours = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			String hh = "";
			String hh1 = "";
			String mm = "";
			String mm1 = "";
			if (i < 10) {
				hh = "0" + i;
				mm = "0" + i;
				hh1 = "0" + i;
				mm1 = "30";
			} else if (i >= 10 && i < 20) {
				hh = getDefaultString(i);
				mm = "00";
				hh1 = getDefaultString(i);
				mm1 = "30";
			} else {
				hh = getDefaultString(i);
				mm = "00";
				hh1 = getDefaultString(i);
				mm1 = "30";
			}
			
			String hour = hh + ":" + mm;
			String hour1 = hh1 + ":" + mm1;
			hours.add(hour);
			hours.add(hour1);
		}
		for (String string : hours) {
			cbxAssignmentHour.addItem(string);
		}
		
		if (!isAddNew) {
			removeAllErrorsPanel();
			txtDebtLevel.setValue(getDefaultString(debtLevel.getDeptLevel()));
			cbxDueDate.setValue(debtLevel.getDueDate().toString());
			cbxAssignmentDate.setValue(debtLevel.getAssignmentDate().toString());
			cbxAssignmentHour.setValue(debtLevel.getAssignmentHour().toString());
			cbAllDueDate.setValue("All".equals(debtLevel.getDueDate()));
			cbAllAssignmentDate.setValue("All".equals(debtLevel.getAssignmentDate()));
			cbAllAssignmentHour.setValue("All".equals(debtLevel.getAssignmentHour()));
			
		} else {
			resetControls();
		}
		String template = "debtLevel";
		InputStream layoutFile = getClass().getResourceAsStream("/VAADIN/themes/efinance/layouts/" + template + ".html");
		CustomLayout customLayout = null;
		try {
			customLayout = new CustomLayout(layoutFile);
		} catch (IOException e) {
			Notification.show("Could not locate template " + template, e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		customLayout.addComponent(new Label(I18N.message("debt.level")), "lblDebtLevel");
		customLayout.addComponent(txtDebtLevel, "txtDebtLevel");
		customLayout.addComponent(new Label(I18N.message("due.date")), "lblDueDate");
		customLayout.addComponent(cbxDueDate, "cbxDueDate");
		customLayout.addComponent(cbAllDueDate, "cbDueDate");
		customLayout.addComponent(new Label(I18N.message("assignment.date")), "lblAssignmentDate");
		customLayout.addComponent(cbxAssignmentDate, "cbxAssignmentDate");
		customLayout.addComponent(cbAllAssignmentDate, "cbAllAssignmentDate");
		customLayout.addComponent(new Label(I18N.message("assignment.hour")), "lblAssignmentHour");
		customLayout.addComponent(cbxAssignmentHour, "cbxAssignmentHour");
		customLayout.addComponent(cbAllAssignmentHour, "cbAllAssignmentHour");
		
	
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		verticalLayout.addComponent(navigationPanel);
		verticalLayout.addComponent(messagePanel);
		verticalLayout.addComponent(customLayout);
		windowDebtLevel.setContent(verticalLayout);
		return windowDebtLevel;
	}
	
	/**
	 * Display error message panel
	 */
	private void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		for (String error : lstErrors) {
			Label messageLabel = new Label(error);
			messageLabel.addStyleName("error");
			messagePanel.addComponent(messageLabel);
		}
		messagePanel.setVisible(true);
	}
	
	/**
	 * Remove error message panel
	 */
	private void removeAllErrorsPanel() {
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
		lstErrors.clear();
	}
	
	/**
	 * Reset controls
	 */
	private void resetControls() {
		removeAllErrorsPanel();
		txtDebtLevel.setValue("");
		cbxDueDate.setValue(null);
		cbxAssignmentDate.setValue(null);
		cbxAssignmentHour.setValue(null);
	}
	
	/**
	 * 
	 * @param field
	 * @param messageKey
	 * @return
	 */
	private List<String> checkIntegerTextField(AbstractTextField field, String messageKey) {
		if (StringUtils.isNotEmpty((String) field.getValue())) {
			try {
				Integer.parseInt((String) field.getValue());
				removeAllErrorsPanel();
			} catch (NumberFormatException e) {
				lstErrors.add(I18N.message("field.value.incorrect.1", new String[] { I18N.message(messageKey) }));
			}
		}
		if (cbxDueDate.getValue() == null && !cbAllDueDate.getValue()) {
			lstErrors.add(I18N.message("field.required.1", new String[] { I18N.message("due.date") }));
		} 
		if (cbxAssignmentDate.getValue() == null && !cbAllAssignmentDate.getValue()) {
			lstErrors.add(I18N.message("field.required.1", new String[] { I18N.message("assignment.date") }));
		}
		if (cbxAssignmentHour.getValue() == null && !cbAllAssignmentHour.getValue()) {
			lstErrors.add(I18N.message("field.required.1", new String[] { I18N.message("assignment.hour") }));
		}
		return lstErrors;
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validateControls() {
		lstErrors = checkIntegerTextField(txtDebtLevel, I18N.message("debt.level"));
		return lstErrors.isEmpty();
	}
	

	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty() == cbAllDueDate) {
			if (cbAllDueDate.getValue()) {
				cbxDueDate.setEnabled(false);
				cbxDueDate.setValue(null);
			} else {
				cbxDueDate.setEnabled(true);
			}
		} else if (event.getProperty() == cbAllAssignmentDate) {
			if (cbAllDueDate.getValue()) {
				cbxAssignmentDate.setEnabled(false);
				cbxAssignmentDate.setValue(null);
			} else {
				cbxAssignmentDate.setEnabled(true);
			}
		}else if (event.getProperty() == cbAllAssignmentHour) {
			if (cbAllDueDate.getValue()) {
				cbxAssignmentHour.setEnabled(false);
				cbxAssignmentDate.setValue(null);
			} else {
				cbxAssignmentDate.setEnabled(true);
			}
		}
		
	}
}
