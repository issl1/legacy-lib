package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class EditAdvancesPopupPanel extends Window implements ClickListener {

	/** */
	private static final long serialVersionUID = -2027229304050826519L;
	
	private Label lblAmountValue;
	private Button btnSave;
	private Button btnCancel;
	private CheckBox cbAllocate;
	private CheckBox cbDeduct;
	private CheckBox cbRefund;
	private ComboBox cbxAllocate;
	private ComboBox cbxDeduct;
	
	/**
	 * 
	 */
	public EditAdvancesPopupPanel() {
		setModal(true);
		setCaption(I18N.message("edit.advances"));
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		Label lblAmountTitle = getLabel("amount");
		lblAmountValue = getLabelValue();
		
		cbxAllocate = ComponentFactory.getComboBox();
		cbxDeduct = ComponentFactory.getComboBox();
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getButtonCancel();
		btnCancel.addClickListener(this);
		
		cbAllocate = new CheckBox(I18N.message("allocate"));
		cbDeduct = new CheckBox(I18N.message("deduct"));
		cbRefund = new CheckBox(I18N.message("refund"));
		
		HorizontalLayout topHorizontalLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		topHorizontalLayout.addComponent(lblAmountTitle);
		topHorizontalLayout.addComponent(lblAmountValue);
		
		GridLayout middleGridLayout = new GridLayout(3, 3);
		middleGridLayout.setMargin(true);
		middleGridLayout.setSpacing(true);
		
		middleGridLayout.addComponent(cbAllocate, 0, 0);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), 1, 0);
		middleGridLayout.addComponent(cbxAllocate, 2, 0);
		middleGridLayout.addComponent(cbDeduct, 0, 1);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), 1, 1);
		middleGridLayout.addComponent(cbxDeduct, 2, 1);
		middleGridLayout.addComponent(cbRefund, 0, 2);
		
		HorizontalLayout bellowHorizontalLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		bellowHorizontalLayout.addComponent(btnSave);
		bellowHorizontalLayout.addComponent(btnCancel);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainLayout.addComponent(topHorizontalLayout);
		mainLayout.addComponent(middleGridLayout);
		mainLayout.addComponent(bellowHorizontalLayout);
		
		mainLayout.setComponentAlignment(bellowHorizontalLayout, Alignment.BOTTOM_RIGHT);
		setContent(mainLayout);
		
		
	}
	
	/**
	 * 
	 * @return label
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		
		
	}
	
	/**
	 * reset
	 */
	private void reset() {
		lblAmountValue.setValue(StringUtils.EMPTY);
		cbxAllocate.setValue(null);
		cbxDeduct.setValue(null);
	}

}
