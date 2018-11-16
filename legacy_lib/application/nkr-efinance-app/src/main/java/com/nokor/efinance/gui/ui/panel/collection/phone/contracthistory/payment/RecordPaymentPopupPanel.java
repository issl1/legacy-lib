package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.payment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.payment.model.MPayment;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class RecordPaymentPopupPanel extends Window implements FinServicesHelper, ClickListener, MPayment {

	/** */
	private static final long serialVersionUID = 7123273193569725396L;
	
	private AutoDateField dfPaymentDate;
	private EntityRefComboBox<EPaymentMethod> cbxPaymentMethod;
	private ComboBox cbxFrom;
	private ComboBox cbxPendingLockSplit;
	private ComboBox cbxInstallment;
	private ComboBox cbxPenalty;
	private ComboBox cbxFee;
	private CheckBox cbAuto;
	private Label lblAllocateToValue;
	private Label lblTotalAmountValue;
	
	private Button btnTopAdd;
	private Button btnSee;
	private Button btnBellowAdd;
	private Button btnSubmit;
	private Button btnCancel;
	private Button btnRemoveInstallment;
	private Button btnRemovePenalty;
	private Button btnRemoveFee;
	
	private TextField txtAmount;
	private TextField txtInstallment;
	private TextField txtPenalty;
	private TextField txtFee;
	
	private SimpleTable<Entity> simpleTable;
	
	/**
	 * 
	 */
	public RecordPaymentPopupPanel() {
		setModal(true);
		setCaption(I18N.message("new.payment"));
		init();
	}
	
	/**
	 * init
	 */
	private void init() {
		List<EPaymentMethod> values = new ArrayList<>();
		values.add(ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.CASH.getCode()));
		values.add(ENTITY_SRV.getByCode(EPaymentMethod.class, EPaymentMethod.CHEQUE.getCode()));
		
		cbxPaymentMethod = new EntityRefComboBox<>(values);
		cbxPaymentMethod.setImmediate(true);
		cbxPaymentMethod.setWidth(180, Unit.PIXELS);
		
		cbxInstallment = ComponentFactory.getComboBox();
		cbxPenalty = ComponentFactory.getComboBox();
		cbxFee = ComponentFactory.getComboBox();
		
		dfPaymentDate = ComponentFactory.getAutoDateField();
		cbxFrom = ComponentFactory.getComboBox();
		cbxPendingLockSplit = ComponentFactory.getComboBox();
		cbAuto = new CheckBox(I18N.message("autos"));
		
		txtAmount = ComponentFactory.getTextField(false, 60, 150);
		txtInstallment = ComponentFactory.getTextField(false, 60, 100);
		txtPenalty = ComponentFactory.getTextField(false, 60, 100);
		txtFee = ComponentFactory.getTextField(false, 60, 100);
		
		btnBellowAdd = ComponentLayoutFactory.getButtonAdd();
		btnBellowAdd.addClickListener(this);
		
		btnTopAdd = ComponentLayoutFactory.getButtonAdd();
		btnTopAdd.addClickListener(this);
		
		btnCancel = ComponentLayoutFactory.getButtonCancel();
		btnCancel.addClickListener(this);
		
		btnSee = new NativeButton(I18N.message("see"), this);
		btnSee.setStyleName("btn btn-success button-small");
		btnSee.setWidth(50, Unit.PIXELS);
		
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
		
		Label lblPaymentDateTitle = ComponentFactory.getLabel(I18N.message("payment.date"));
		Label lblMethodTitle = ComponentFactory.getLabel(I18N.message("method"));
		Label lblFromTitle = ComponentFactory.getLabel(I18N.message("from"));
		Label lblAmountTitle = ComponentFactory.getLabel(I18N.message("amount"));
		Label lblPendingLockSplitTitle = ComponentFactory.getLabel(I18N.message("pending.locksplit"));
		Label lblAllocateToTitle = getLabel("allocate.to");
		Label lblTotalAmountTitle = getLabel("total.amount");
		lblAllocateToValue = getLabelValue();
		lblTotalAmountValue = getLabelValue();
		
		GridLayout topLayout = new GridLayout(8, 4);
		topLayout.setMargin(true);
		topLayout.setSpacing(true);
		
		int iCol = 0;
		topLayout.addComponent(lblPaymentDateTitle, iCol++, 0);
		topLayout.addComponent(dfPaymentDate, iCol++, 0);
		topLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		topLayout.addComponent(lblMethodTitle, iCol++, 0);
		topLayout.addComponent(cbxPaymentMethod, iCol++, 0);
		topLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		topLayout.addComponent(lblFromTitle, iCol++, 0);
		topLayout.addComponent(cbxFrom, iCol++, 0);
		
		iCol = 0;
		topLayout.addComponent(lblAmountTitle, iCol++, 1);
		topLayout.addComponent(txtAmount, iCol++, 1);
		topLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 1);
		topLayout.addComponent(cbAuto, iCol++, 1);
		
		iCol = 0;
		topLayout.addComponent(lblAllocateToTitle, iCol++, 2);
		topLayout.addComponent(lblAllocateToValue, iCol++, 2);
		
		iCol = 0;
		topLayout.addComponent(lblPendingLockSplitTitle, iCol++, 3);
		topLayout.addComponent(cbxPendingLockSplit, iCol++, 3);
		topLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 3);
		topLayout.addComponent(btnTopAdd, iCol++, 3);
		topLayout.addComponent(btnBellowAdd, iCol++, 3);
		
		topLayout.setComponentAlignment(lblPaymentDateTitle, Alignment.MIDDLE_CENTER);
		topLayout.setComponentAlignment(lblMethodTitle, Alignment.MIDDLE_CENTER);
		topLayout.setComponentAlignment(lblFromTitle, Alignment.MIDDLE_CENTER);
		topLayout.setComponentAlignment(lblAmountTitle, Alignment.MIDDLE_CENTER);
		topLayout.setComponentAlignment(lblPendingLockSplitTitle, Alignment.MIDDLE_CENTER);
		topLayout.setComponentAlignment(lblAllocateToTitle, Alignment.MIDDLE_CENTER);
		
		GridLayout bellowLayout = new GridLayout(5, 4);
		bellowLayout.setMargin(true);
		bellowLayout.setSpacing(true);
		
		bellowLayout.addComponent(cbxInstallment, 0, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 0);
		bellowLayout.addComponent(txtInstallment, 2, 0);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 3, 0);
		bellowLayout.addComponent(btnRemoveInstallment, 4, 0);
		bellowLayout.addComponent(cbxPenalty, 0, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 1);
		bellowLayout.addComponent(txtPenalty, 2, 1);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 3, 1);
		bellowLayout.addComponent(btnRemovePenalty, 4, 1);
		bellowLayout.addComponent(cbxFee, 0, 2);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 2);
		bellowLayout.addComponent(txtFee, 2, 2);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 3, 2);
		bellowLayout.addComponent(btnRemoveFee, 4, 2);
		bellowLayout.addComponent(btnBellowAdd, 0, 3);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 1, 3);
		bellowLayout.addComponent(lblTotalAmountTitle, 2, 3);
		bellowLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), 3, 3);
		bellowLayout.addComponent(lblTotalAmountValue, 4, 3);
		
		bellowLayout.setComponentAlignment(lblTotalAmountTitle, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(bellowLayout);
		layout.addComponent(ComponentFactory.getSpaceLayout(4, Unit.PIXELS));
		layout.addComponent(getDueLayout());
		
		
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(topLayout);
		mainLayout.addComponent(layout);
		
		setContent(mainLayout);
	}

	/**
	 * 
	 * @return columnDefinitions
	 */
	private List<ColumnDefinition> getColumnDifinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100, false));
		columnDefinitions.add(new ColumnDefinition(ITEM, I18N.message("item"), String.class, Align.CENTER, 150));
		columnDefinitions.add(new ColumnDefinition(DUE, I18N.message("due"), Double.class, Align.CENTER, 150));
		return columnDefinitions;
	}
	
	/**
	 * 
	 * @return mainLayout
	 */
	private Component getDueLayout() {
		simpleTable = new SimpleTable<Entity>(getColumnDifinitions());
		simpleTable.setPageLength(5);
		setStyleName(Reindeer.PANEL_LIGHT);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("due"));
		fieldSet.setContent(simpleTable);
		
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
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		
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
	 * reset
	 */
	private void reset() {
		dfPaymentDate.setValue(null);
		cbxPaymentMethod.setSelectedEntity(null);
		cbxFrom.setValue(null);
		txtAmount.setValue(StringUtils.EMPTY);
		cbAuto.setValue(false);
		lblAllocateToValue.setValue(StringUtils.EMPTY);
		cbxPendingLockSplit.setValue(null);
		cbxInstallment.setValue(null);
		cbxPenalty.setValue(null);
		cbxFee.setValue(null);
		txtInstallment.setValue(StringUtils.EMPTY);
		txtPenalty.setValue(StringUtils.EMPTY);
		txtFee.setValue(StringUtils.EMPTY);
		lblTotalAmountValue.setValue(StringUtils.EMPTY);

	}

}
