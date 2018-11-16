package com.nokor.efinance.core.shared.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class ComponentLayoutFactory {
	
	/**
	 * 
	 * @param maxLength
	 * @param width
	 * @return
	 */
	public static TextField getTextFieldAlignRight(int maxLength, float width) {
		TextField txt = ComponentFactory.getTextField(maxLength, width);
		txt.setStyleName("align-right");
		return txt;
	}
	
	/**
	 * 
	 * @return
	 */
	public static ComboBox getTimeComboBox() {
		ComboBox comboBox = ComponentFactory.getComboBox();
		Date date = DateUtils.getDateAtBeginningOfDay(DateUtils.today());
		long startTime = date.getTime();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < 96; i++) {
			long time = 900000 * i;
			date.setTime(startTime + time);
			comboBox.addItem(time);
			comboBox.setItemCaption(time, df.format(date));
		}
		comboBox.setNullSelectionAllowed(false);
		return comboBox;
	}

	/**
	 * 
	 * @param component
	 * @return
	 */
	public static FormLayout getFormLayoutCaptionAlignLeft() {
		FormLayout formLayout = ComponentFactory.getFormLayout();
		formLayout.setStyleName("myform-align-left");
		return formLayout;
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	public static FormLayout getFormLayoutCaptionAlignLeft(Component component, boolean isMargin) {
		FormLayout formLayout = getFormLayoutCaptionAlignLeft(isMargin);
		if (component != null) {
			formLayout.addComponent(component);
		}
		return formLayout;
	}
	
	/**
	 * 
	 * @param component
	 * @param isMargin
	 * @return
	 */
	public static FormLayout getFormLayoutCaptionAlignLeft(boolean isMargin) {
		FormLayout formLayout = getFormLayoutCaptionAlignLeft();
		if (isMargin) {
			formLayout.setMargin(new MarginInfo(false, true, false, true));
		}
		return formLayout;
	}
	
	/**
	 * 
	 * @param caption
	 * @param icon
	 * @param width
	 * @param styleName
	 * @return
	 */
	public static Button getButtonStyle(String caption, Resource icon, float width, String styleName) {
		Button button = new NativeButton(I18N.message(caption));
		button.addStyleName(styleName);
		button.setIcon(icon);
		button.setWidth(width, Unit.PIXELS);
		button.setImmediate(true);
		return button;
	} 
	
	/**
	 * 
	 * @return
	 */
	public static Button getButtonSave() {
		return getDefaultButton("save", FontAwesome.SAVE, 60);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Button getButtonAdd() {
		return getDefaultButton("add", FontAwesome.PLUS, 60);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Button getButtonSearch() {
		return getDefaultButton("search", FontAwesome.SEARCH, 70);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Button getButtonReset() {
		return getDefaultButton("reset", FontAwesome.ERASER, 70);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Button getButtonUpdate() {
		return getDefaultButton("update", FontAwesome.EDIT, 60);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Button getButtonDelete() {
		return getDefaultButton("delete", FontAwesome.TRASH_O, 60);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Button getButtonCancel() {
		return getDefaultButton("cancel", FontAwesome.BAN, 60);
	}
	
	/**
	 * 
	 * @param caption
	 * @param resource
	 * @param width
	 * @return
	 */
	public static Button getDefaultButton(String caption, Resource resource, float width) {
		return getButtonStyle(I18N.message(caption), resource, width, "btn btn-success button-small");
	}
	
	/**
	 * 
	 * @param icon
	 * @return
	 */
	public static Button getButtonIcon(Resource icon) {
		Button btn = new Button();
		btn.setIcon(icon);
		btn.setStyleName(Reindeer.BUTTON_LINK);
		return btn;
	}
	
	/**
	 * 
	 * @param margin
	 * @param spacing
	 * @return
	 */
	public static VerticalLayout getVerticalLayout(boolean margin, boolean spacing) {
		VerticalLayout verLayout = ComponentFactory.getVerticalLayout();
		verLayout.setMargin(margin);
		verLayout.setSpacing(spacing);
		return verLayout;
	}
	
	/**
	 * 
	 * @param margin
	 * @param spacing
	 * @return
	 */
	public static VerticalLayout getVerticalLayout(MarginInfo margin, boolean spacing) {
		VerticalLayout verLayout = ComponentFactory.getVerticalLayout();
		verLayout.setMargin(margin);
		verLayout.setSpacing(spacing);
		return verLayout;
	}
	
	/**
	 * 
	 * @param margin
	 * @param spacing
	 * @param component
	 * @return
	 */
	public static VerticalLayout getVerticalLayout(MarginInfo margin, boolean spacing, Component component) {
		VerticalLayout verLayout = ComponentFactory.getVerticalLayout();
		verLayout.setMargin(margin);
		verLayout.setSpacing(spacing);
		verLayout.addComponent(component);
		return verLayout;
	}
	
	/**
	 * 
	 * @param margin
	 * @param spacing
	 * @return
	 */
	public static HorizontalLayout getHorizontalLayout(boolean margin, boolean spacing) {
		HorizontalLayout horLayout = ComponentFactory.getHorizontalLayout();
		horLayout.setMargin(margin);
		horLayout.setSpacing(spacing);
		return horLayout;
	}
	
	/**
	 * 
	 * @param margin
	 * @param spacing
	 * @return
	 */
	public static HorizontalLayout getHorizontalLayout(MarginInfo margin, boolean spacing) {
		HorizontalLayout horLayout = ComponentFactory.getHorizontalLayout();
		horLayout.setMargin(margin);
		horLayout.setSpacing(spacing);
		return horLayout;
	}
	
	/**
	 * 
	 * @param selectedId
	 * @param description
	 * @return
	 */
	public static Notification getNotificationDesc(String selectedId, String description) {
		Notification notification = getNotification(Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message(description, new String[]{ selectedId }));
		return notification;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static Notification getNotification(Type type) {
		Notification notification = new Notification(StringUtils.EMPTY, type);
		notification.setDelayMsec(3000);
		notification.show(Page.getCurrent());
		return notification;
	}
	
	/**
	 * 
	 */
	public static Notification displaySuccessfullyMsg() {
		Notification notification = getNotification(Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message("msg.info.save.successfully"));
		return notification;
	}
	
	/**
	 * 
	 * @param desc
	 * @return
	 */
	public static Notification displayErrorMsg(String desc) {
		Notification notification = getNotification(Type.ERROR_MESSAGE);
		notification.setDescription(I18N.message(desc));
		return notification;
	}
	
	/**
	 * 
	 * @param desc
	 * @return
	 */
	public static Notification displaySuccessMsg(String desc) {
		Notification notification = getNotification(Type.HUMANIZED_MESSAGE);
		notification.setDescription(I18N.message(desc));
		return notification;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	public static Label getLabelCaption(String caption) {
		Label label = ComponentFactory.getLabel(caption);
		label.setStyleName("label-padding");
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	public static Label getLabelCaptionRequired(String caption) {
		Label label = ComponentFactory.getHtmlLabel(I18N.message(caption) + StringUtils.SPACE + "<b style=\"color: red\">*</b>");
		label.addStyleName("label-padding");
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param options
	 * @return
	 */
	public static OptionGroup getOptionGroup(List<String> options) {
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.addItems(options);
		optionGroup.addStyleName("horizontal");
		optionGroup.setImmediate(true);
		optionGroup.setNullSelectionAllowed(false);
		return optionGroup;
	}
	
	/**
	 * 
	 * @param columns
	 * @param rows
	 * @return
	 */
	public static GridLayout getGridLayout(int columns, int rows) {
		GridLayout gridLayout = new GridLayout(columns, rows);
		gridLayout.setSpacing(true);
		return gridLayout;
	}
	
	/**
	 * 
	 * @param columns
	 * @param rows
	 * @return
	 */
	public static GridLayout getGridLayout(MarginInfo marginInfo, int columns, int rows) {
		GridLayout gridLayout = getGridLayout(columns, rows);
		gridLayout.setMargin(marginInfo);
		return gridLayout;
	}
	
	/**
	 * 
	 * @param columns
	 * @param rows
	 * @return
	 */
	public static GridLayout getGridLayoutDefaultMargin(int columns, int rows) {
		GridLayout gridLayout = getGridLayout(new MarginInfo(true, false, false, true), columns, rows);
		gridLayout.setSpacing(true);
		return gridLayout;
	}
	
	/**
	 * 
	 * @param component
	 * @param isMargin
	 * @param isLight
	 * @return
	 */
	public static Panel getPanel(Component component, boolean isMargin, boolean isLight) {
		VerticalLayout layout = getVerticalLayout(isMargin, false);
		layout.addComponent(component);
		Panel panel = new Panel(layout);
		if (isLight) {
			panel.setStyleName(Reindeer.PANEL_LIGHT);
		}
		return panel;
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	public static Component setMargin(Component component) {
		VerticalLayout layout = ComponentFactory.getVerticalLayout();
		layout.setMargin(true);
		layout.addComponent(component);
		return layout;
	}
	
}
