package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payoff;

import java.util.Date;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
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
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColPhonePayOffTabPanel extends Panel implements ClickListener {

	private static final long serialVersionUID = -6475351018332862992L;
	
	private AutoDateField dfDate;
	private Button btnSimulate;
	private Label lblOutstandingARValue;
	private Label lblSelectedDateValue;
	private Label lblAmountDiscountedSelectDateValue;
	private Label lblNextMonthOfDateValue;
	private Label lblAmountDiscountNextMonthValue;
	
	private Button btnPayOff;
	
	private AutoDateField dfDatePayOff;
	private Label lblExpiryDateValue;
	private ComboBox cbxChannel;
	private CheckBox cbUpdateBook;
	private Label lblAmountValue;
	private Button btnCancel;
	private Button btnSubmit;
	
//	private Contract contract;
	
	public ColPhonePayOffTabPanel() {
		init();
		
		FieldSet simulateFieldSet = new FieldSet();
		simulateFieldSet.setSizeFull();
		simulateFieldSet.setLegend(I18N.message("simulate"));
		simulateFieldSet.setContent(getSimulateLayout());
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addComponent(simulateFieldSet);
		mainLayout.addComponent(btnPayOff);
		mainLayout.addComponent(getPayoffLayout());
		
		mainLayout.setComponentAlignment(btnPayOff, Alignment.MIDDLE_LEFT);
		
		setContent(mainLayout);
	}
	
	/**
	 * init
	 */
	private void init() {
		
		dfDate = ComponentFactory.getAutoDateField();
		dfDate.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -882742199334161696L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Date selectedDate = dfDate.getValue();
				Date nextMonthSelectedDate = DateUtils.addMonthsDate(selectedDate, 1);
				lblSelectedDateValue.setValue(getDescription(getDateFormat(selectedDate)));
				lblNextMonthOfDateValue.setValue(getDescription(getDateFormat(nextMonthSelectedDate)));
			}
		});
		btnSimulate = new NativeButton(I18N.message("simulate"), this);
		btnSimulate.setStyleName("btn btn-success button-small");
			
		lblOutstandingARValue = getLabelBold();
		lblOutstandingARValue.setValue(getDescription(AmountUtils.format(12000d)));
		lblNextMonthOfDateValue = getLabelBold();
		lblSelectedDateValue = getLabelBold();
		lblAmountDiscountedSelectDateValue = getLabelBold();
		lblAmountDiscountedSelectDateValue.setValue(getDescription(AmountUtils.format(3200d)));
		lblAmountDiscountNextMonthValue = getLabelBold();
		lblAmountDiscountNextMonthValue.setValue(getDescription(AmountUtils.format(2200d)));
		btnPayOff = new NativeButton(I18N.message("pay.off"), this);
		btnPayOff.setStyleName("btn btn-success button-small");
		
		dfDatePayOff = ComponentFactory.getAutoDateField();
		lblExpiryDateValue = getLabelBold();
		lblExpiryDateValue.setValue(getDescription(getDateFormat(DateUtils.today())));
		
		cbxChannel = new ComboBox();
		cbUpdateBook = new CheckBox(I18N.message("update.book.by.gl"));
		lblAmountValue = getLabelBold();
		lblAmountValue.setValue(getDescription(AmountUtils.format(200d)));
		
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(FontAwesome.TIMES);
		btnCancel.setStyleName("btn btn-danger button-small");
		
		btnSubmit = new NativeButton(I18N.message("submit"), this);
		btnSubmit.setIcon(FontAwesome.ARROW_CIRCLE_O_RIGHT);
		btnSubmit.setStyleName("btn btn-success button-small");
	}
	
	/**
	 * Assign Value
	 * @param contract
	 */
	public void assignValue(Contract contract) {
//		this.contract = contract;
	}
	
	/**
	 * getSimulateLayout
	 * @return
	 */
	private GridLayout getSimulateLayout() {
		Label lblDateCaption = getLabel("date");
		
		GridLayout simulationLayout = new GridLayout(10, 2);
		simulationLayout.setSpacing(true);
		simulationLayout.setMargin(true);
		
		int iCol = 0;
		simulationLayout.addComponent(lblDateCaption, iCol++, 0);
		simulationLayout.addComponent(dfDate, iCol++, 0);
		simulationLayout.addComponent(btnSimulate, iCol++, 0);
		simulationLayout.addComponent(new Label(I18N.message("outstanding")), iCol++, 0);
		simulationLayout.addComponent(lblOutstandingARValue, iCol++, 0);
		simulationLayout.addComponent(lblSelectedDateValue, iCol++, 0);
		simulationLayout.addComponent(new Label(I18N.message("amount.discounted")), iCol++, 0);
		simulationLayout.addComponent(lblAmountDiscountedSelectDateValue, iCol++, 0);
		
		simulationLayout.addComponent(lblNextMonthOfDateValue, 5, 1);
		simulationLayout.addComponent(new Label(I18N.message("amount.discounted")), 6, 1);
		simulationLayout.addComponent(lblAmountDiscountNextMonthValue, 7, 1);
		
		return simulationLayout;
	}
	
	/**
	 * Payoff layout
	 * @return
	 */
	private HorizontalLayout getPayoffLayout() {
		Label lblPayOffDateCaption = getLabel("pay.off.date");
		Label lblChannelCaption = getLabel("channel");
		Label lblExpiryDateCaption = getLabel("expiry.date");
		Label lblAmountCaption = getLabel("amount");
		
		HorizontalLayout payOffLayout = new HorizontalLayout();
		payOffLayout.setMargin(true);
		payOffLayout.addComponent(lblPayOffDateCaption);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		payOffLayout.addComponent(dfDatePayOff);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		payOffLayout.addComponent(lblExpiryDateCaption);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		payOffLayout.addComponent(lblExpiryDateValue);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		payOffLayout.addComponent(lblChannelCaption);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		payOffLayout.addComponent(cbxChannel);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		payOffLayout.addComponent(cbUpdateBook);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		payOffLayout.addComponent(lblAmountCaption);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		payOffLayout.addComponent(lblAmountValue);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(15, Unit.PIXELS));
		payOffLayout.addComponent(btnCancel);
		payOffLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		payOffLayout.addComponent(btnSubmit);
		
		payOffLayout.setComponentAlignment(lblPayOffDateCaption, Alignment.MIDDLE_LEFT);
		payOffLayout.setComponentAlignment(lblChannelCaption, Alignment.MIDDLE_LEFT);
		payOffLayout.setComponentAlignment(lblExpiryDateCaption, Alignment.MIDDLE_LEFT);
		payOffLayout.setComponentAlignment(lblExpiryDateValue, Alignment.MIDDLE_LEFT);
		payOffLayout.setComponentAlignment(cbUpdateBook, Alignment.MIDDLE_LEFT);
		payOffLayout.setComponentAlignment(lblAmountCaption, Alignment.MIDDLE_LEFT);
		payOffLayout.setComponentAlignment(lblAmountValue, Alignment.MIDDLE_LEFT);
		
		return payOffLayout;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Label Template
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		Label label = new Label(I18N.message(caption));
		return label;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelBold() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		return DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH);
	}
	

}
