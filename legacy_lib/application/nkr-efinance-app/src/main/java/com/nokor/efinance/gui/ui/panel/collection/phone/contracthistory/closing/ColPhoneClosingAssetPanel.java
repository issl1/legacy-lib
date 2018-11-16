package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.closing;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.address.panel.AddressComboBox;
import com.nokor.efinance.core.collection.model.EContactPerson;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class ColPhoneClosingAssetPanel extends AbstractControlPanel implements ClickListener{

	/** */
	private static final long serialVersionUID = 1869079287756518541L;
	
	private static final String BYGL = I18N.message("by.gl");
	private static final String BYCUSTOMER = I18N.message("by.customer");
	
	private AutoDateField dfOperationDate;
	private AutoDateField dfFrom;
	private AutoDateField dfTo;
	
	private Label lblAOMTaxExpirationDateValue;
	private Label lblAOMTagExpirationDateValue;
	private Label lblTransferencePriceValue;
	private Label lblOperationPriceValue;
	private Label lblAOMTagPriceValue;
	private Label lblTopTotalToPayValue;
	private Label lblBottomTotalToPayValue;
	
	private OptionGroup optBy;
	
	private ERefDataComboBox<EContactPerson> cbxSendDocContactWith;
	private ERefDataComboBox<EContactPerson> cbxConfirmDocContactWith;
	private ComboBox cbxTransferences;
	private ComboBox cbxParty;
	private ComboBox cbxListOfOperation;
	private ComboBox cbxYear;
	private ComboBox cbxAOMTag;
	private ComboBox cbxAOMTax;
	private AddressComboBox cbxSendDocAddress;
	private AddressComboBox cbxConfirmDocAddress;
	
	private CheckBox cbSomeDay;
	private CheckBox cbSendDoc;
	private CheckBox cbConfirmDoc;
	private CheckBox cbSendDocPickup;
	private CheckBox cbConfirmDocPickup;
	
	private Button btnSave;
	private Button btnLockSplit;
	private Button btnCancel;
	private Button btnPay;
	private Button btnAdd;
	
	/**
	 * 
	 */
	public ColPhoneClosingAssetPanel() {
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		dfOperationDate = ComponentFactory.getAutoDateField();
		cbSomeDay = new CheckBox(I18N.message("some.day"));
		
		lblAOMTagExpirationDateValue = getLabelValue();
		lblAOMTagPriceValue = getLabelValue();
		lblAOMTaxExpirationDateValue = getLabelValue();
		lblBottomTotalToPayValue = getLabelValue();
		lblOperationPriceValue = getLabelValue();
		lblTopTotalToPayValue = getLabelValue();
		lblTransferencePriceValue = getLabelValue();
		lblBottomTotalToPayValue = getLabelValue();
		
		List<String> options = Arrays.asList(new String[] {BYGL, BYCUSTOMER});
		optBy = new OptionGroup();
		optBy = ComponentLayoutFactory.getOptionGroup(options);
		optBy.select(BYGL);
		
		cbxTransferences = ComponentFactory.getComboBox();
		cbxParty = ComponentFactory.getComboBox();
		cbxYear = ComponentFactory.getComboBox();
		cbxAOMTag = ComponentFactory.getComboBox();
		cbxAOMTax = ComponentFactory.getComboBox();
		cbxSendDocAddress = new AddressComboBox();
		cbxSendDocAddress.setImmediate(true);
		cbxConfirmDocAddress = new AddressComboBox();
		cbxConfirmDocAddress.setImmediate(true);
		cbxSendDocContactWith = new ERefDataComboBox<>(EContactPerson.values());
		cbxConfirmDocContactWith = new ERefDataComboBox<>(EContactPerson.values());
		cbxListOfOperation = ComponentFactory.getComboBox();
		
		dfFrom = ComponentFactory.getAutoDateField();
		dfTo = ComponentFactory.getAutoDateField();
		
		cbConfirmDocPickup = new CheckBox(I18N.message("pickup"));
		cbSendDocPickup = new CheckBox(I18N.message("pickup"));
		cbConfirmDoc = new CheckBox(I18N.message("send.to"));
		cbSendDoc = new CheckBox(I18N.message("send.to"));
		
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		
		btnCancel = ComponentLayoutFactory.getButtonCancel();
		btnCancel.addClickListener(this);
		
		btnPay = new NativeButton(I18N.message("pay"), this);
		btnPay.setStyleName("btn btn-success button-small");
		btnPay.setWidth(60, Unit.PIXELS);
		
		btnLockSplit = new NativeButton(I18N.message("lock.split"), this);
		btnLockSplit.setStyleName("btn btn-success button-small");
		btnLockSplit.setWidth(60, Unit.PIXELS);
		
		Label lblOperationDateTitle = ComponentFactory.getLabel(I18N.message("operation.date"));
		Label lblAOMTagExpirationDateTitle = getLabel(I18N.message("aom.tag.expiration.date"));
		Label lblAOMTaxExpirationDateTitle = getLabel(I18N.message("aom.tax.expiration.date"));
		
		GridLayout topLayout = new GridLayout(10, 1);
		topLayout.setMargin(true);
		topLayout.setSpacing(true);
		
		topLayout.addComponent(lblOperationDateTitle, 0, 0);
		topLayout.addComponent(dfOperationDate, 1, 0);
		topLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), 2, 0);
		topLayout.addComponent(cbSomeDay, 3, 0);
		topLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), 4, 0);
		topLayout.addComponent(lblAOMTaxExpirationDateTitle, 5, 0);
		topLayout.addComponent(lblAOMTaxExpirationDateValue, 6, 0);
		topLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), 7, 0);
		topLayout.addComponent(lblAOMTagExpirationDateTitle, 8, 0);
		topLayout.addComponent(lblAOMTagExpirationDateValue, 9, 0);
		
		topLayout.setComponentAlignment(lblOperationDateTitle, Alignment.MIDDLE_LEFT);
		topLayout.setComponentAlignment(lblAOMTaxExpirationDateTitle, Alignment.MIDDLE_LEFT);
		topLayout.setComponentAlignment(lblAOMTagExpirationDateTitle, Alignment.MIDDLE_LEFT);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(topLayout);
		mainLayout.addComponent(getMaintananceLayout());
		
		addComponent(mainLayout);
		
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getMaintananceLayout() {
		Label lblTransferencePriceTitle = getLabel(I18N.message("price"));
		Label lblOperationPriceTitle = getLabel(I18N.message("price"));
		Label lblAOMTaxPriceTitle = getLabel(I18N.message("price"));
		Label lblTopTotalToPayTitle = getLabel(I18N.message("total.to.pay"));
		Label lblBottomTotalToPayTitle = getLabel(I18N.message("total.to.pay.la"));
		Label lblFromTitle = ComponentFactory.getLabel(I18N.message("from"));
		Label lblToTitle = ComponentFactory.getLabel(I18N.message("to"));
		Label lblDocToPrepareTitle = ComponentFactory.getLabel(I18N.message("doc.to.prepare"));
		Label lblConfirmDocTitle = ComponentFactory.getLabel(I18N.message("confirmation.doc"));
		Label lblSendDocDisplayAddressTitle = ComponentFactory.getLabel(I18N.message("display.address"));
		Label lblConfirmDocDisplayAddressTitle = ComponentFactory.getLabel(I18N.message("display.address"));
		Label lblSendDocAtTitle = ComponentFactory.getLabel(I18N.message("at"));
		Label lblConfirmDocAtTitle = ComponentFactory.getLabel(I18N.message("at"));
		
		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setSpacing(true);
		topLayout.addComponent(optBy);
		
		GridLayout middleLayout = new GridLayout(7, 3);
		middleLayout.setMargin(true);
		middleLayout.setSpacing(true);
		
		int iCol = 0;
		middleLayout.addComponent(cbxTransferences, iCol++, 0);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		middleLayout.addComponent(cbxParty, iCol++, 0);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		middleLayout.addComponent(lblTransferencePriceTitle, iCol++, 0);
		middleLayout.addComponent(lblTransferencePriceValue, iCol++, 0);
		
		iCol = 0;
		middleLayout.addComponent(cbxListOfOperation, iCol++, 1);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		middleLayout.addComponent(btnAdd, iCol++, 1);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		middleLayout.addComponent(lblOperationPriceTitle, iCol++, 1);
		middleLayout.addComponent(lblOperationPriceValue, iCol++, 1);
		
		iCol = 0;
		middleLayout.addComponent(cbxAOMTax, iCol++, 2);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		middleLayout.addComponent(cbxYear, iCol++, 2);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		middleLayout.addComponent(lblAOMTaxPriceTitle, iCol++, 2);
		middleLayout.addComponent(lblAOMTagPriceValue, iCol++, 2);
		
		middleLayout.setComponentAlignment(lblTransferencePriceTitle, Alignment.MIDDLE_LEFT);
		middleLayout.setComponentAlignment(lblOperationPriceTitle, Alignment.MIDDLE_LEFT);
		middleLayout.setComponentAlignment(lblAOMTaxPriceTitle, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout hLayout = ComponentLayoutFactory.getHorizontalLayout(true, false);
		
		hLayout.addComponent(cbxAOMTag);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(17, Unit.PIXELS));
		hLayout.addComponent(lblFromTitle);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		hLayout.addComponent(dfFrom);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(32, Unit.PIXELS));
		hLayout.addComponent(lblToTitle);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		hLayout.addComponent(dfTo);
		
		hLayout.setComponentAlignment(lblFromTitle, Alignment.MIDDLE_LEFT);
		hLayout.setComponentAlignment(lblToTitle, Alignment.MIDDLE_LEFT);
		
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		verLayout.addComponent(middleLayout);
		verLayout.addComponent(hLayout);
		
		GridLayout bellowLayout = new GridLayout(13, 2);
		bellowLayout.setMargin(true);
		bellowLayout.setSpacing(true);
		
		bellowLayout.addComponent(lblDocToPrepareTitle, 0, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 0);
		bellowLayout.addComponent(cbSendDoc, 2, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 3, 0);
		bellowLayout.addComponent(cbxSendDocContactWith, 4, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 5, 0);
		bellowLayout.addComponent(lblSendDocAtTitle, 6, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 7, 0);
		bellowLayout.addComponent(cbxSendDocAddress, 8, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 9, 0);
		bellowLayout.addComponent(lblSendDocDisplayAddressTitle, 10, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 11, 0);
		bellowLayout.addComponent(cbSendDocPickup, 12, 0);
		
		bellowLayout.addComponent(lblConfirmDocTitle, 0, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 1);
		bellowLayout.addComponent(cbConfirmDoc, 2, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 3, 1);
		bellowLayout.addComponent(cbxConfirmDocContactWith, 4, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 5, 1);
		bellowLayout.addComponent(lblConfirmDocAtTitle, 6, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 7, 1);
		bellowLayout.addComponent(cbxConfirmDocAddress, 8, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 9, 1);
		bellowLayout.addComponent(lblConfirmDocDisplayAddressTitle, 10, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 11, 1);
		bellowLayout.addComponent(cbConfirmDocPickup, 12, 1);
		
		bellowLayout.setComponentAlignment(lblSendDocAtTitle, Alignment.MIDDLE_LEFT);
		bellowLayout.setComponentAlignment(lblSendDocDisplayAddressTitle, Alignment.MIDDLE_LEFT);
		bellowLayout.setComponentAlignment(lblDocToPrepareTitle, Alignment.MIDDLE_LEFT);
		bellowLayout.setComponentAlignment(lblConfirmDocAtTitle, Alignment.MIDDLE_LEFT);
		bellowLayout.setComponentAlignment(lblConfirmDocDisplayAddressTitle, Alignment.MIDDLE_LEFT);
		bellowLayout.setComponentAlignment(lblConfirmDocTitle, Alignment.MIDDLE_LEFT);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(topLayout);
		verticalLayout.addComponent(verLayout);
		verticalLayout.addComponent(bellowLayout);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("maintenance"));
		fieldSet.setContent(verticalLayout);
		
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setContent(fieldSet);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(lblTopTotalToPayTitle);
		horizontalLayout.addComponent(lblTopTotalToPayValue);
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		layout.addComponent(lblBottomTotalToPayTitle);
		layout.addComponent(lblBottomTotalToPayValue);
		layout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS));
		layout.addComponent(btnSave);
		layout.addComponent(btnLockSplit);
		layout.addComponent(btnPay);
		layout.addComponent(btnCancel);
		
		layout.setComponentAlignment(lblBottomTotalToPayTitle, Alignment.MIDDLE_LEFT);
		
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.setMargin(true);
		vLayout.setSpacing(true);
		
		vLayout.addComponent(horizontalLayout);
		vLayout.addComponent(layout);
		vLayout.setComponentAlignment(layout, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(panel);
		mainLayout.addComponent(vLayout);
		
		return mainLayout;
		
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

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}

}
