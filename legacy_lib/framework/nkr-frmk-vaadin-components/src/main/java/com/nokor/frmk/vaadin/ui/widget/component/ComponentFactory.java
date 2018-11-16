package com.nokor.frmk.vaadin.ui.widget.component;

import org.apache.commons.lang.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Container;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ComponentFactory {
    
    private static String getMessage(String key) {
        return I18N.message(key);
    }

    public static Button getButton() {
        return new Button();
    }

    public static Button getButton(String key) {
        Button button = getButton();
        button.setCaption(getMessage(key));
        return button;
    }
    
    public static Button getButton(String key, String event) {
        final Button button = getButton(key);
        button.setData(event);
        return button;
    }

    
    public static Button getButton(String key, String descriptionKey, String event) {
        final Button button = getButton(key, event);
        button.setDescription(getMessage(descriptionKey));
        return button;
    }

    
    public static Label getLabel() {
        return new Label();
    }

    
    public static Label getLabel(String key, ContentMode contentMode) {
        return new Label(getMessage(key), contentMode);
    }

    
    public static Label getLabel(String key) {
        final Label label = new Label(getMessage(key));
        return label;
    }

    public static Label getLabel(String key, int width) {
        final Label label = getLabel(key);        
        label.setWidth(width, Unit.PIXELS);
        return label;
    }
    
    public static Label getLabel(String key, String event) {
        final Label label = getLabel(key);
        label.setData(event);
        return label;
    }

    public static HorizontalLayout getHorizontalLayout() {
        return new HorizontalLayout();
    }

    
    public static HorizontalLayout getHorizontalLayout(String key) {
        final HorizontalLayout layout = getHorizontalLayout();
        layout.setCaption(getMessage(key));
        return layout;
    }

    public static VerticalLayout getVerticalLayout() {
        return new VerticalLayout();
    }

    public static VerticalLayout getVerticalLayout(int height) {
    	VerticalLayout verticalLayout = new VerticalLayout();
    	verticalLayout.setHeight(height, Unit.PIXELS);
        return verticalLayout;
    }
    
    public static VerticalLayout getVerticalLayout(String key) {
        final VerticalLayout layout = getVerticalLayout();
        layout.setCaption(getMessage(key));
        return layout;
    }

    
    public static HorizontalSplitPanel getHorizontalSplitPanel() {
        return new HorizontalSplitPanel();
    }

    public static  CssLayout getCssLayout() {
        return new CssLayout();
    }

    
    public static FormLayout getFormLayout() {
        return new FormLayout();
    }

    public static Panel getPanel() {
        final Panel panel = new Panel();
        panel.setContent(getVerticalLayout());
        return panel;
    }

    
    public static Panel getPanel(String key) {
        final Panel panel = getPanel();
        panel.setCaption(getMessage(key));
        return panel;
    }
   
    public static Accordion getAccordion() {
        return new Accordion();
    }

    
    public static Window getWindow() {
        return new Window();
    }
    
    public static Window getWindow(String key) {
        Window window = getWindow();
        window.setCaption(getMessage(key));
        return window;
    }
    
    public static InlineDateField getInlineDateField() {
        return new InlineDateField();
    }
    
    public static ProgressIndicator getProgressIndicator() {
        return new ProgressIndicator();
    }

    
    public static ProgressIndicator getProgressIndicator(String key) {
        final ProgressIndicator progressIndicator = getProgressIndicator();
        progressIndicator.setCaption(getMessage(key));
        return progressIndicator;
    }

    
    public static TabSheet getTabSheet() {
        return new TabSheet();
    }

    
    public static Upload getUpload(String key, Upload.Receiver uploadReceiver) {
        return new Upload(getMessage(key), uploadReceiver);
    }

    
    public static Upload getUpload() {
        return new Upload();
    }

    
    public static ComboBox getComboBox() {
        return new ComboBox();
    }

    
    public static ComboBox getComboBox(String key, Container container) {
        return new ComboBox(getMessage(key), container);
    }

   /**
    *  
    * @return
    */
    public static PasswordField getPasswordField() {
        return new PasswordField();
    }

    /**
     * 
     * @param key
     * @return
     */
    public static PasswordField getPasswordField(String key) {
        PasswordField passwordField = getPasswordField();
        passwordField.setCaption(getMessage(key));
        return passwordField;
    }
   
    /**
     * 
     * @return
     */
    public static TextField getTextField() {
    	final TextField textField = new TextField();
        return textField;
    }
    
    /**
     * 
     * @param maxLength
     * @param width
     * @return
     */
    public static TextField getTextField(int maxLength, float width) {
        return getTextField(null, false, maxLength, width);
    }
    
    /**
     * 
     * @param required
     * @param maxLength
     * @param width
     * @return
     */
    public static TextField getTextField(boolean required, int maxLength, float width) {
        return getTextField(null, required, maxLength, width);
    }
    
    /**
     * 
     * @param required
     * @param maxLength
     * @param width
     * @return
     */
    public static TextField getTextField35(boolean required, int maxLength, float width) {
    	final TextField textField = getTextField(required, maxLength, width);
    	textField.setHeight(35, Unit.PIXELS);
        return textField;
    }

    /**
     * 
     * @param key
     * @param required
     * @param maxLength
     * @param width
     * @return
     */
    public static TextField getTextField35(String key, boolean required, int maxLength, float width) {
    	final TextField textField = getTextField(key, required, maxLength, width);
    	textField.setHeight(35, Unit.PIXELS);
        return textField;
    }
    
    /**
     * 
     * @param key
     * @param required
     * @param maxLength
     * @param width
     * @return
     */
    public static TextField getTextField(String key, boolean required, int maxLength, float width) {
    	return getTextField(key, required, null, maxLength, width);
    }
    
    /**
     * 
     * @param key
     * @param required
     * @param nullRepresentation
     * @param maxLength
     * @param width
     * @return
     */
    public static TextField getTextField(String key, boolean required, String nullRepresentation, int maxLength, float width) {
        final TextField textField = getTextField();
        if (StringUtils.isNotEmpty(key)) {
        	textField.setCaption(I18N.message(key));
        }
//        textField.setNullSettingAllowed(false);
        textField.setNullRepresentation(nullRepresentation);
        textField.setRequired(required);
        textField.setMaxLength(maxLength);
        textField.setWidth(width, Unit.PIXELS);
        return textField;
    }
    
    /**
     * 
     * @return
     */
    public static TextArea getTextArea() {
        return new TextArea();
    }

    /**
     * 
     * @param required
     * @param width
     * @param height
     * @return
     */
    public static TextArea getTextArea(boolean required, float width, float height) {
        return getTextArea(null, required, width, height);
    }
    
    /**
     * 
     * @param key
     * @param required
     * @param width
     * @param height
     * @return
     */
    public static TextArea getTextArea(String key, boolean required, float width, float height) {
        TextArea textArea = new TextArea();
        if (StringUtils.isNotEmpty(key)) {
        	textArea.setCaption(getMessage(key));
        }
        textArea.setRequired(required);
        textArea.setWidth(width, Unit.PIXELS);
        textArea.setHeight(height, Unit.PIXELS);
        return textArea;
    }

        
    public static TwinColSelect getTwinColSelect(String key, Container container) {
        return new TwinColSelect(getMessage(key), container);
    }

    
    public static Button getButton(String key, String event, Object payload) {
        final Button button = getButton(key);
        button.setData(event);
        return button;
    }

    
    public static GridLayout getGridLayout() {
        final GridLayout gridLayout = new GridLayout(3, 3);
        return gridLayout;
    }
    
    public static Label getHtmlLabel(String content) {
        return new Label(content, ContentMode.HTML);
    }
    
    /**
     * Get PopupDateField
     * @return
     */
    public static PopupDateField getPopupDateField() {
    	PopupDateField popupDateField = new PopupDateField();
    	popupDateField.setDateFormat("dd/MM/yyyy");
    	return popupDateField;
    }
    
    /**
     * Get PopupDateField
     * @param key
     * @param required
     * @return
     */
    public static PopupDateField getPopupDateField(String key, boolean required) {
    	PopupDateField popupDateField = getPopupDateField();
    	if (StringUtils.isNotEmpty(key)) {
    		popupDateField.setCaption(getMessage(key));
    	}
    	popupDateField.setRequired(required);
    	return popupDateField;
    }
    
    /**
     * Get PopupDateField
     * @return
     */
    public static AutoDateField getAutoDateField() {
    	AutoDateField autoDateField = new AutoDateField();
    	autoDateField.setDateFormat("dd/MM/yyyy");
    	autoDateField.setWidth(110, Unit.PIXELS);
    	return autoDateField;
    }
    
    /**
     * Get PopupDateField
     * @param key
     * @param required
     * @return
     */
    public static AutoDateField getAutoDateField(String key, boolean required) {
    	AutoDateField autoDateField = getAutoDateField();
    	if (StringUtils.isNotEmpty(key)) {
    		autoDateField.setCaption(getMessage(key));
    	}
    	autoDateField.setRequired(required);
    	return autoDateField;
    }
    
    public static NumberField getNumberField() {
    	final NumberField textField = new NumberField();
        return textField;
    }
    
    public static NumberField getNumberField(int maxLength, float width) {
        return getNumberField(null, false, maxLength, width);
    }
    
    public static NumberField getNumberField(boolean required, int maxLength, float width) {
        return getNumberField(null, required, maxLength, width);
    }
        
    public static NumberField getNumberField(String key, boolean required, int maxLength, float width) {
        final NumberField textField = getNumberField();
        if (StringUtils.isNotEmpty(key)) {
        	textField.setCaption(I18N.message(key));
        }
        textField.setRequired(required);
        textField.setMaxLength(maxLength);
        textField.setWidth(width, Unit.PIXELS);
        return textField;
    }
    
    /**
     * Get space layout
     * @param width
     * @param unit
     * @return
     */
    public static HorizontalLayout getSpaceLayout(int width, Unit unit) {
        final HorizontalLayout layout = getHorizontalLayout();
        layout.setWidth(width, unit);
        return layout;
    }
    
    /**
     * @param height
     * @param unit
     * @return
     */
    public static VerticalLayout getSpaceHeight(int height, Unit unit) {
        VerticalLayout spaceHeight = new VerticalLayout();
        spaceHeight.setHeight(height, unit);
        return spaceHeight;
    }
    
    /**
     * 
     * @return
     */
    public static ComboBox getYearComboBox() {
    	return getYearComboBox(5);
    }
    
    /**
     * 
     * @param nbYearAfter
     * @return
     */
    public static ComboBox getYearComboBox(int nbYearAfter) {
		ComboBox combobox = ComponentFactory.getComboBox();
		int startYear = 2010;
		int currentYear = DateUtils.getYear(DateUtils.today());
		int endYear = currentYear + nbYearAfter;
		for (int i = startYear; i <= endYear ; i++) {
			combobox.addItem(String.valueOf(i));
		}
		return combobox;
	}
}
