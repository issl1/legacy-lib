package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class NewLockSplitPopupPanel extends Window implements ClickListener, MPayment{

	/** */
	private static final long serialVersionUID = 7459306650530717519L;
	
	private static final String SPACIFIC = I18N.message("specific");
	private static final String VARIABLE = I18N.message("variable");
	
	private OptionGroup optSpecificVariable;
	private Label lblAllocateToValue;
	private Label lblTotalAmountValue;
	private Label lblEmailValue;
	private Label lblPhoneValue;
	
	private Button btnRemoveInstallment;
	private Button btnRemovePenalty;
	private Button btnRemoveFee;
	
	private ComboBox cbxInstallment;
	private ComboBox cbxPenalty;
	private ComboBox cbxFee;
	
	private TextField txtInstallment;
	private TextField txtPenalty;
	private TextField txtFee;
	
	private Button btnAdd;
	private Button btnSubmit;
	private Button btnCancel;
	private Button btnCalculate;
	
	private CheckBox cbSendEmail;
	private CheckBox cbSendSMS;
	private CheckBox cbPromise;
	private CheckBox cbUse;
	
	private AutoDateField dfDate;
	private AutoDateField dfValidFromDate;
	private AutoDateField dfValidToDate;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public NewLockSplitPopupPanel() {
		setModal(true);
		setCaption(I18N.message("new.locksplit"));
		init();
	}
	
	/**
	 * init
	 */
	public void init() {
		lblAllocateToValue = getLabelValue();
		lblTotalAmountValue = getLabelValue();
		lblEmailValue = getLabelValue();
		lblPhoneValue = getLabelValue();
		Label lblAllocateToTitle = getLabel("allocate.to");
		Label lblTotalAmountTitle = getLabel("total.amount");
		Label lblEmailTitle = getLabel("email");
		Label lblPhoneTitle = getLabel("phone");
		Label lblValidFromTitle = getLabel("valid.from");
		Label lblValidToTitle = getLabel("to");
		
		List<String> options = Arrays.asList(new String[] {SPACIFIC, VARIABLE});
		
		optSpecificVariable = new OptionGroup();
		optSpecificVariable = ComponentLayoutFactory.getOptionGroup(options);
		optSpecificVariable.select(SPACIFIC);
		
		cbxInstallment = ComponentFactory.getComboBox();
		cbxPenalty = ComponentFactory.getComboBox();
		cbxFee = ComponentFactory.getComboBox();
		
		btnSubmit = new NativeButton(I18N.message("submit"), this);
		btnSubmit.setStyleName("btn btn-success button-small");
		btnSubmit.setWidth(60, Unit.PIXELS);
		
		btnRemoveInstallment = new NativeButton(I18N.message("remove"), this);
		btnRemoveInstallment.setStyleName("btn btn-success button-small");
		btnRemoveInstallment.setWidth(60, Unit.PIXELS);
		
		btnRemovePenalty = new NativeButton(I18N.message("remove"), this);
		btnRemovePenalty.setStyleName("btn btn-success button-small");
		btnRemovePenalty.setWidth(60, Unit.PIXELS);
		
		btnRemoveFee = new NativeButton(I18N.message("remove"), this);
		btnRemoveFee.setStyleName("btn btn-success button-small");
		btnRemoveFee.setWidth(60, Unit.PIXELS);
		
		btnCalculate = new NativeButton(I18N.message("calculate"), this);
		btnCalculate.setStyleName("btn btn-success button-small");
		btnCalculate.setWidth(60, Unit.PIXELS);
		
		btnAdd = ComponentLayoutFactory.getButtonAdd();
		btnAdd.addClickListener(this);
		
		btnCancel = ComponentLayoutFactory.getButtonCancel();
		btnCancel.addClickListener(this);
		
		txtInstallment = ComponentFactory.getTextField(false, 60, 100);
		txtPenalty = ComponentFactory.getTextField(false, 60, 100);
		txtFee = ComponentFactory.getTextField(false, 60, 100);
		
		dfDate = ComponentFactory.getAutoDateField();
		dfValidFromDate = ComponentFactory.getAutoDateField();
		dfValidToDate = ComponentFactory.getAutoDateField();
		
		cbPromise = new CheckBox(I18N.message("promise"));
		cbSendEmail = new CheckBox(I18N.message("send.email"));
		cbSendSMS = new CheckBox(I18N.message("send.sms"));
		cbUse = new CheckBox(I18N.message("use"));
		
		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.setMargin(true);
		topLayout.setSpacing(true);
		topLayout.addComponent(optSpecificVariable);
		
		GridLayout middleLayout = new GridLayout(5, 5);
		middleLayout.setMargin(true);
		middleLayout.setSpacing(true);
		
		int iCol = 0;
		middleLayout.addComponent(lblAllocateToTitle, iCol++, 0);
		middleLayout.addComponent(lblAllocateToValue, iCol++, 0);

		iCol = 0;
		middleLayout.addComponent(cbxInstallment, iCol++, 1);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		middleLayout.addComponent(txtInstallment, iCol++, 1);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		middleLayout.addComponent(btnRemoveInstallment, iCol++, 1);
		
		iCol = 0;
		middleLayout.addComponent(cbxPenalty, iCol++, 2);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		middleLayout.addComponent(txtPenalty, iCol++, 2);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 2);
		middleLayout.addComponent(btnRemovePenalty, iCol++, 2);
		
		iCol = 0;
		middleLayout.addComponent(cbxFee, iCol++, 3);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 3);
		middleLayout.addComponent(txtFee, iCol++, 3);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 3);
		middleLayout.addComponent(btnRemoveFee, iCol++, 3);
		
		iCol = 0;
		middleLayout.addComponent(btnAdd, iCol++, 4);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 4);
		middleLayout.addComponent(lblTotalAmountTitle, iCol++, 4);
		middleLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 4);
		middleLayout.addComponent(lblTotalAmountValue, iCol++, 4);
		
		middleLayout.setComponentAlignment(lblTotalAmountTitle, Alignment.MIDDLE_CENTER);
		
		GridLayout bellowLayout = new GridLayout(4, 3);
		bellowLayout.setMargin(true);
		bellowLayout.setSpacing(true);
		
		bellowLayout.addComponent(cbSendEmail, 0, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 0);
		bellowLayout.addComponent(lblEmailTitle, 2, 0);
		bellowLayout.addComponent(lblEmailValue, 3, 0);
		
		bellowLayout.addComponent(cbSendSMS, 0, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 1);
		bellowLayout.addComponent(lblPhoneTitle, 2, 1);
		bellowLayout.addComponent(lblPhoneValue, 3, 1);
		
		bellowLayout.addComponent(cbPromise, 0, 2);
		
		bellowLayout.setComponentAlignment(lblEmailTitle, Alignment.MIDDLE_CENTER);
		bellowLayout.setComponentAlignment(lblPhoneTitle, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout bellowHorLayout = new HorizontalLayout();
		bellowHorLayout.setMargin(true);
		bellowHorLayout.setSpacing(true);
		
		bellowHorLayout.addComponent(lblValidFromTitle);
		bellowHorLayout.addComponent(dfValidFromDate);
		bellowHorLayout.addComponent(ComponentFactory.getSpaceLayout(4, Unit.PIXELS));
		bellowHorLayout.addComponent(lblValidToTitle);
		bellowHorLayout.addComponent(dfValidToDate);
		
		bellowHorLayout.setComponentAlignment(lblValidFromTitle, Alignment.MIDDLE_CENTER);
		bellowHorLayout.setComponentAlignment(lblValidToTitle, Alignment.MIDDLE_CENTER);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		layout.addComponent(middleLayout);
		layout.addComponent(bellowLayout);
		layout.addComponent(bellowHorLayout);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(true);
		horizontalLayout.setSpacing(true);
		
		horizontalLayout.addComponent(layout);
		horizontalLayout.addComponent(getDueLayout());
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(topLayout);
		mainLayout.addComponent(horizontalLayout);
		
		setContent(mainLayout);
		
	}
	
	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition(ITEM, I18N.message("item"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DUE, I18N.message("due"), Double.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(DUEATDATE, I18N.message("due.at.date"), Date.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return mainLayout
	 */
	private Component getDueLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		
		layout.addComponent(dfDate);
		layout.addComponent(ComponentFactory.getSpaceLayout(4, Unit.PIXELS));
		layout.addComponent(btnCalculate);
		layout.addComponent(ComponentFactory.getSpaceLayout(4, Unit.PIXELS));
		layout.addComponent(cbUse);
		
		simpleTable = new SimpleTable<Entity>(getColumnDifinitions());
		simpleTable.setPageLength(5);
		setStyleName(Reindeer.PANEL_LIGHT);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true);
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(layout);
		verticalLayout.addComponent(simpleTable);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("due"));
		fieldSet.setContent(verticalLayout);
		
		Panel panel = new Panel();
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setContent(fieldSet);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setMargin(true);
		
		horizontalLayout.addComponent(btnSubmit);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		horizontalLayout.addComponent(btnCancel);
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(panel);
		mainLayout.addComponent(horizontalLayout);
		mainLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
		
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
	
	/**
	 * reset
	 */
	private void reset() {
		cbxInstallment.setValue(null);
		cbxPenalty.setValue(null);
		cbxFee.setValue(null);
		txtInstallment.setValue(StringUtils.EMPTY);
		txtPenalty.setValue(StringUtils.EMPTY);
		txtFee.setValue(StringUtils.EMPTY);
		lblTotalAmountValue.setValue(StringUtils.EMPTY);

	}

}
