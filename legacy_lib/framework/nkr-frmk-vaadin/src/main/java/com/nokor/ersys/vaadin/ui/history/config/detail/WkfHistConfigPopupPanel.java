package com.nokor.ersys.vaadin.ui.history.config.detail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * WkfHistConfig Pop up Panel
 * @author bunlong.taing
 */
public class WkfHistConfigPopupPanel extends Window implements ClickListener {

	/** */
	private static final long serialVersionUID = -4623609339158642825L;
	
	private NativeButton btnSave;
	private NativeButton btnCancel;
	
	private VerticalLayout fieldPanel;
	private WkfHistConfigFormPanel formPanel;
	private List<CheckBox> cbFields;
	
	/**
	 * 
	 * @param caption
	 */
	public WkfHistConfigPopupPanel(String caption) {
		super(caption);
		setModal(true);
		setResizable(false);
		setWidth(300, Unit.PIXELS);
		setHeight(500, Unit.PIXELS);
		
		cbFields = new ArrayList<CheckBox>();
		btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		btnSave.addClickListener(this);
		btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.addClickListener(this);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		fieldPanel = new VerticalLayout();
		fieldPanel.setMargin(true);
		
		Panel panel = new Panel();
		panel.setSizeFull();
		panel.setContent(fieldPanel);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSizeFull();
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(panel);
		contentLayout.setExpandRatio(panel, 1);
		
		setContent(contentLayout);
	}
	
	/**
	 * Assign value to Pop up
	 * @param clazz
	 * @param strAuditProperties
	 */
	public void assignValues(Class<?> clazz, String strAuditProperties) {
		reset();
		List<String> fieldNames = getAllFields(clazz);
		List<String> auditProperties = new ArrayList<String>();
		if (strAuditProperties != null && StringUtils.isNotEmpty(strAuditProperties)) {
			auditProperties.addAll(Arrays.asList(strAuditProperties.split(";")));
		}
		for (String fieldName : fieldNames) {
			CheckBox checkBox = new CheckBox(fieldName);
			checkBox.setValue(isPropertyAudited(fieldName, auditProperties));
			cbFields.add(checkBox);
			fieldPanel.addComponent(checkBox);
		}
	}
	
	/**
	 * Get All Fields from Class
	 * @param clazz
	 * @return
	 */
	private List<String> getAllFields(Class<?> clazz) {
		List<String> fieldNames = new ArrayList<String>();
		ReflectionUtils.doWithFields(clazz, new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				fieldNames.add(field.getName());
			}
		}, new FieldFilter() {
			@Override
			public boolean matches(Field field) {
		        return !Modifier.isStatic(field.getModifiers());
			}
		});
		Collections.sort(fieldNames);
		return fieldNames;
	}
	
	/**
	 * Is Property Audited
	 * @param audited
	 * @param fieldName
	 * @return
	 */
	private boolean isPropertyAudited(String fieldName, List<String> audited) {
		boolean isAudited = false;
		if (audited != null && !audited.isEmpty()) {
			for (String string : audited) {
				if (fieldName.equals(string)) {
					isAudited = true;
				}
			}
		}
		return isAudited;
	}
	
	/**
	 * save
	 */
	private void save() {
		String value = "";
		int index = 0;
		for (CheckBox checkBox : cbFields) {
			if (checkBox.getValue()) {
				String fieldName = checkBox.getCaption();
				value += index == cbFields.size() - 1 ? fieldName : fieldName + ";";
			}
			index++;
		}
		formPanel.setAuditProperties(value);
		close();
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			save();
		} else if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		cbFields.clear();
		fieldPanel.removeAllComponents();
	}
	
	/**
	 * Set FormPanel
	 * @param formPanel
	 */
	public void setFormPanel(WkfHistConfigFormPanel formPanel) {
		this.formPanel = formPanel;
	}

}
