package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.redumption;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractRedemption;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.ersys.core.hr.model.eref.ETypeOrganization;
import com.nokor.ersys.core.hr.model.organization.Organization;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author buntha.cheaentities
 *
 */
public class RedemptionPanel extends AbstractControlPanel implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7566139296395017948L;
	
	private AutoDateField dfRedumptionDate;
	private Button btnOk;
	
	private Label lblCurrentLocationMotobikeValue;
	private EntityComboBox<Organization> cbxPickupLocation;
	private AutoDateField dfDate;
	private ComboBox cbxTime;
	
	private TextField txtRedemptionFee;
	private TextField txtDiscountFee;
	private Label lblTotalToPayValue;
	
	private RedemptionOptionPanel redemptionOptionPanel;
	private Button btnSave;
	private Button btnLockSplit;
	private Button btnPay;
	private Button btnCancel;
	
	private Label lblCollectTotalToPayValue;
	private VerticalLayout contentLayout;
	
	private Contract contract;
	private ContractRedemption contractRedemption;
	
	private double totalFeeToPay;
	
	public RedemptionPanel() {
		
		init();
		
		HorizontalLayout redumptionDateLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		redumptionDateLayout.addComponent(ComponentLayoutFactory.getLabelCaption("redumption.date"));
		redumptionDateLayout.addComponent(dfRedumptionDate);
		redumptionDateLayout.addComponent(btnOk);
		
		GridLayout assetGridLayout = ComponentLayoutFactory.getGridLayout(6, 2);
		assetGridLayout.setSpacing(true);
		assetGridLayout.setMargin(true);
		
		Label lblCurrentLocationMotobike = new Label(I18N.message("current.location.motobike") + " : ");
		lblCurrentLocationMotobike.setWidthUndefined();
		
		int iCol = 0;
		assetGridLayout.addComponent(lblCurrentLocationMotobike, iCol++, 0);
		assetGridLayout.addComponent(lblCurrentLocationMotobikeValue, iCol++, 0);
		
		iCol = 0;
		assetGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("pickup.location"), iCol++, 1);
		assetGridLayout.addComponent(cbxPickupLocation, iCol++, 1);
		assetGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("date"), iCol++, 1);
		assetGridLayout.addComponent(dfDate, iCol++, 1);
		assetGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("time"), iCol++, 1);
		assetGridLayout.addComponent(cbxTime, iCol++, 1);
		
		FieldSet assetFieldSet = new FieldSet();
		assetFieldSet.setLegend(I18N.message("asset"));
		assetFieldSet.setContent(assetGridLayout);
		
		Panel assetPanel = new Panel();
		assetPanel.setStyleName(Reindeer.PANEL_LIGHT);
		assetPanel.setContent(assetFieldSet);
		
		Label lblCollectionTotalToPay = getLabelValue();
		lblCollectionTotalToPay.setValue(getDescription(I18N.message("total.to.pay") + " : "));
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonLayout.addComponent(lblCollectionTotalToPay);
		buttonLayout.addComponent(lblCollectTotalToPayValue);
		buttonLayout.setComponentAlignment(lblCollectionTotalToPay, Alignment.MIDDLE_RIGHT);
		buttonLayout.setComponentAlignment(lblCollectTotalToPayValue, Alignment.MIDDLE_RIGHT);
		buttonLayout.addComponent(btnSave);
		buttonLayout.addComponent(btnLockSplit);
		buttonLayout.addComponent(btnPay);
		buttonLayout.addComponent(btnCancel);
		
		contentLayout.addComponent(assetPanel);
		contentLayout.addComponent(createFeePanel());
		contentLayout.addComponent(redemptionOptionPanel);
		contentLayout.addComponent(buttonLayout);
		contentLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		mainLayout.addComponent(redumptionDateLayout);
		mainLayout.addComponent(contentLayout);
		
		Panel mainPanel = new Panel(mainLayout);
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		addComponent(mainPanel);
	}
	
	/**
	 * init
	 */
	private void init() {
		
		contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.setVisible(false);
		
		dfRedumptionDate = ComponentFactory.getAutoDateField();
		btnOk = ComponentLayoutFactory.getButtonStyle("ok", FontAwesome.CHECK, 70, "btn btn-success button-small");
		btnOk.setEnabled(false);
		btnOk.addClickListener(this);
		
		dfRedumptionDate.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 119737554592001828L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (dfRedumptionDate.getValue() != null) {
					btnOk.setEnabled(true);
					txtRedemptionFee.setValue(getDefaultString(calculateRedemptionFee()));
				} else {
					contentLayout.setVisible(false);
					btnOk.setEnabled(false);
				}
			}
		});
		
		lblCurrentLocationMotobikeValue = getLabelValue();
		
		cbxPickupLocation = new EntityComboBox<>(Organization.class,  "nameEn");
		cbxPickupLocation.setEntities(getWarehouseLocations());
		cbxPickupLocation.setWidth("180px");
		
		dfDate = ComponentFactory.getAutoDateField();
		cbxTime = getTimeComboBox();
		cbxTime.setWidth("60px");
		
		lblTotalToPayValue = getLabelValue();
		lblTotalToPayValue.setValue(getDescription(AmountUtils.format(0d)));
		txtRedemptionFee = ComponentFactory.getTextField();
		txtRedemptionFee.setEnabled(false);
		txtDiscountFee = ComponentFactory.getTextField();
		txtDiscountFee.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = 8734488712361369517L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				errors.clear();
				checkDoubleField(txtDiscountFee, "discount.on.fee");
				if (errors.isEmpty()) {
					totalFeeToPay = calculateTotalPayFee();
					lblTotalToPayValue.setValue(getDescription(AmountUtils.format(totalFeeToPay)));
				} else {
					ComponentLayoutFactory.displayErrorMsg(errors.get(0));
				}
			}
		});
		
		redemptionOptionPanel = new RedemptionOptionPanel();
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 70, "btn btn-success");
		btnSave.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger");
		btnCancel.addClickListener(this);
		btnLockSplit = ComponentLayoutFactory.getButtonStyle("locksplit", null, 90, "btn btn-success");
		btnLockSplit.addClickListener(this);
		btnPay = ComponentLayoutFactory.getButtonStyle("pay", FontAwesome.MONEY, 70, "btn btn-success");
		btnPay.addClickListener(this);
		lblCollectTotalToPayValue = getLabelValue();
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		this.contract = contract;
		contractRedemption = getContractRedemption();
		if (contractRedemption != null) {
			contentLayout.setVisible(true);
			if (contractRedemption.getPickupLocationId() != null) {
				Organization organization = ENTITY_SRV.getById(Organization.class, contractRedemption.getPickupLocationId());
				cbxPickupLocation.setSelectedEntity(organization);
			}
			
			if (contractRedemption.getRedemptionDate() == null) {
				dfRedumptionDate.setValue(DateUtils.todayH00M00S00());
			} else {
				dfRedumptionDate.setValue(contractRedemption.getRedemptionDate());
			}
			
			dfDate.setValue(contractRedemption.getPickupDate());
			if (contractRedemption.getPickupDate() != null) {
				cbxTime.setValue(contractRedemption.getPickupDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractRedemption.getPickupDate()).getTime());
			}
			txtRedemptionFee.setValue(AmountUtils.format(calculateRedemptionFee()));
			txtDiscountFee.setValue(AmountUtils.format(contractRedemption.getDiscountFees()));
			
			lblCollectTotalToPayValue.setValue(getDescription(AmountUtils.format(totalFeeToPay + redemptionOptionPanel.getTotalLoan())));
			
		} else {
			contractRedemption = ContractRedemption.createInstance();
			contractRedemption.setContract(contract);
		}
		redemptionOptionPanel.assignValue(contractRedemption);
	}
	
	/**
	 * save contract redemption
	 */
	private void doSave() {
		contractRedemption.setRedemptionDate(dfRedumptionDate.getValue());
		
		Date pickupDate = dfDate.getValue();
		if (pickupDate != null) {
			pickupDate = DateUtils.getDateAtBeginningOfDay(pickupDate);
			long time = (long) cbxTime.getValue();
			pickupDate.setTime(pickupDate.getTime() + time);
		}
		
		if (cbxPickupLocation.getSelectedEntity() != null) {
			Organization organization = cbxPickupLocation.getSelectedEntity();
			contractRedemption.setPickupLocationId(organization.getId());
		}
		
		contractRedemption.setPickupDate(pickupDate);
		contractRedemption.setRedemptionFee(getDouble(txtRedemptionFee, 0d));
		contractRedemption.setDiscountFees(getDouble(txtDiscountFee, 0d));
		redemptionOptionPanel.save(contractRedemption);
		try {
			ENTITY_SRV.saveOrUpdate(contractRedemption);
			ComponentLayoutFactory.displaySuccessfullyMsg();
		} catch (Exception e) {
			e.printStackTrace();
			ComponentLayoutFactory.displayErrorMsg(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private double calculateRedemptionFee() {
		Date foreclosureDate = contract.getForeclosureDate();
		Date redemptionDate = dfRedumptionDate.getValue();
		if (foreclosureDate == null || redemptionDate == null) {
			return 0d;
		}
		long nbRedemptionDate = DateUtils.getDiffInDaysPlusOneDay(redemptionDate, foreclosureDate);
		return nbRedemptionDate * 20; // Fix code 20bath per day
	}
	
	/**
	 * 
	 * @return
	 */
	private double calculateTotalPayFee() {
		double redemptionFee = getDouble(txtRedemptionFee, 0d);
		double discountFee = getDouble(txtDiscountFee, 0d);
		double totalPayFee = redemptionFee - discountFee;
		return totalPayFee;
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel createFeePanel() {
		Panel feePanel = new Panel();
		feePanel.setStyleName(Reindeer.PANEL_LIGHT);
		
		GridLayout feeGridLayout = ComponentLayoutFactory.getGridLayout(2, 3);
		feeGridLayout.setSpacing(true);
		feeGridLayout.setMargin(true);
		
		feeGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("redemption.fee"), 0, 0);
		feeGridLayout.addComponent(txtRedemptionFee, 1, 0);
		feeGridLayout.addComponent(ComponentLayoutFactory.getLabelCaption("discount.on.fee"), 0, 1);
		feeGridLayout.addComponent(txtDiscountFee, 1, 1);
		feeGridLayout.addComponent(new Label(I18N.message("total.to.pay") + " : "), 0, 2);
		feeGridLayout.addComponent(lblTotalToPayValue, 1, 2);
		
		FieldSet feeFieldSet = new FieldSet();
		feeFieldSet.setLegend(I18N.message("fee"));
		feeFieldSet.setContent(feeGridLayout);
		feePanel.setContent(feeFieldSet);
		return feePanel;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
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
		stringBuffer.append(value != null ? value : "");
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	
	/**
	 * Get Time ComboBox
	 * @return
	 */
	private ComboBox getTimeComboBox() {
		ComboBox comboBox = ComponentFactory.getComboBox();
		Date date = DateUtils.getDateAtBeginningOfDay(DateUtils.today());
		long startTime = date.getTime();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < 48; i++) {
			long time = 1800000 * i;		// 30 minute interval = 1800000
			date.setTime(startTime + time);
			comboBox.addItem(time);
			comboBox.setItemCaption(time, df.format(date));
		}
		comboBox.setValue(0l);
		comboBox.setNullSelectionAllowed(false);
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	private ContractRedemption getContractRedemption() {
		BaseRestrictions<ContractRedemption> restrictions = new BaseRestrictions<>(ContractRedemption.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addOrder(Order.desc(ContractRedemption.ID));
		List<ContractRedemption> contractRedemptions = ENTITY_SRV.list(restrictions);
		if (!contractRedemptions.isEmpty()) {
			return contractRedemptions.get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Organization> getWarehouseLocations() {
		BaseRestrictions<Organization> restrictions = new BaseRestrictions<>(Organization.class);
		restrictions.addCriterion(Restrictions.eq("typeOrganization", ETypeOrganization.LOCATION));
		return ENTITY_SRV.list(restrictions);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			doSave();
		} else if (event.getButton() == btnLockSplit) {
			RedemptionLockSplitPopupPanel redemptionLockSplitPopupPanel = new RedemptionLockSplitPopupPanel();
			redemptionLockSplitPopupPanel.assignValues(contract, getDouble(txtRedemptionFee, 0d), getDouble(txtDiscountFee, 0d));
			UI.getCurrent().addWindow(redemptionLockSplitPopupPanel);
		} else if (event.getButton() == btnPay) {
			
		} else if (event.getButton() == btnCancel) {
			dfRedumptionDate.setValue(null);
			contentLayout.setVisible(false);
		} else if (event.getButton() == btnOk) {
			contentLayout.setVisible(true);
		}
	}
}
