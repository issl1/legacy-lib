package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.repo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.core.workflow.ReturnWkfStatus;
import com.nokor.ersys.core.hr.model.address.Address;
import com.nokor.ersys.core.hr.model.eref.ELocation;
import com.nokor.ersys.core.hr.model.eref.ETypeAddress;
import com.nokor.ersys.core.hr.model.organization.OrgStructure;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ColReturnRepoTabPanel extends AbstractControlPanel implements ClickListener, ValueChangeListener, FinServicesHelper {

	private static final long serialVersionUID = 6068874487833291182L;
	private Contract contract;
	private ContractFlag contractFlag;
	private ERefDataComboBox<EFlag> cbxFlag;
	private Button btnOpen;
	private AutoDateField dfDate;
	private ComboBox cbxTime;
	private ERefDataComboBox<ELocation> cbxLocation1;
	private ComboBox cbxLocation2;
	private TextField txtRemark;
	private Button btnSubmit;
	private Button btnCancel;
	private GridLayout returnGridLayout;
	private GridLayout submittedGridLayout;
	private Panel mainPanel;
	private Label lblFlagValue;
	private Label lblCreateDateValue;
	private Label lblCreateByValue;
	private Label lblStatusValue;
	private Label lblDateValue;
	private Label lblTimeValue;
	private Label lblLocation1Value;
	private Label lblLocation2Value;
	private Label lblRemarkValue;
	private Button btnEdit;
	private Button btnWithdraw;
	private CheckBox cbMotoFound;
	private AutoDateField dfMotoFoundDate;
	private ComboBox cbxMotoFoundTime;
	private CheckBox cbFollowUp;
	private AutoDateField dfFollowUpDate;
	private TextField txtResultRemark;
	private Button btnSave;
	private Label lblMotoFoundDateValue;
	private Label lblMotoFoundTimeValue;
	private GridLayout resultLayout;
	private VerticalLayout messagePanel;
	private Component location2;
	private Label lblAddress;
	private EntityComboBox<OrgStructure> cbxBranch ;
	private DealerComboBox cbxDealer;
	//private AddressComboBox cbxAddress;
	private EntityRefComboBox<ETypeAddress> cbxTypeAddress;
	private Address addressSelected;
	private TextField txtOtherLocation;
	private boolean isEdit = false;
	
	private Label lblLocation2Caption;
	
	public ColReturnRepoTabPanel() {
		setMargin(true);
		setSpacing(true);
		init();
		
		mainPanel = new Panel();
		mainPanel.setContent(createReturnFormLayout(false));
		addComponent(mainPanel);
	}
	
	/**
	 * init
	 */
	private void init() {
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		cbxFlag = new ERefDataComboBox<>(getReturnRepoValues());
		btnOpen = ComponentLayoutFactory.getButtonStyle("open", null, 70, "btn btn-success button-small");
		btnOpen.addClickListener(this);
		
		dfDate = ComponentFactory.getAutoDateField();
		cbxTime = getTimeComboBox();
		cbxTime.setWidth(65, Unit.PIXELS);
		cbxLocation1 = new ERefDataComboBox<>(ELocation.getReturnLocationValues());
		cbxLocation1.addValueChangeListener(this);
		cbxLocation2 = new ComboBox();
		location2 = cbxLocation2;
		txtRemark = ComponentFactory.getTextField();
		btnSubmit = ComponentLayoutFactory.getButtonStyle("submit", FontAwesome.CHECK, 70, "btn btn-success button-small");
		btnSubmit.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger button-small");
		btnCancel.addClickListener(this);
		
		lblFlagValue = getLabelValue();
		lblFlagValue.setWidthUndefined();
		lblCreateDateValue = getLabelValue();
		lblCreateDateValue.setWidthUndefined();
		lblCreateByValue = getLabelValue();
		lblCreateByValue.setWidthUndefined();
		lblStatusValue = getLabelValue();
		lblStatusValue.setWidthUndefined();
		lblDateValue = getLabelValue();
		lblDateValue.setWidthUndefined();
		lblTimeValue = getLabelValue();
		lblTimeValue.setWidthUndefined();
		lblLocation1Value = getLabelValue();
		lblLocation1Value.setWidthUndefined();
		lblLocation2Value = getLabelValue();
		lblLocation2Value.setWidthUndefined();
		lblRemarkValue = getLabelValue();
		lblRemarkValue.setWidthUndefined();
		lblAddress = getLabelValue();
		lblAddress.setWidthUndefined();
		
		lblLocation2Caption = getLabelValue();
		lblLocation2Caption.setVisible(false);
		location2.setVisible(false);
		
		lblMotoFoundDateValue = getLabelValue();
		lblMotoFoundDateValue.setEnabled(false);
		lblMotoFoundTimeValue = getLabelValue();
		lblMotoFoundTimeValue.setEnabled(false);
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 80, "btn btn-success button-small");
		btnEdit.addClickListener(this);
		
		btnWithdraw = ComponentLayoutFactory.getButtonStyle("withdraw", null, 80, "btn btn-success button-small");
		btnWithdraw.addClickListener(this);
		
		dfMotoFoundDate = ComponentFactory.getAutoDateField();
		dfMotoFoundDate.setWidth("95px");
		cbxMotoFoundTime = getTimeComboBox();
		cbxMotoFoundTime.setWidth(80, Unit.PIXELS);
		
		cbMotoFound = new CheckBox(I18N.message("moto.found"));
		cbFollowUp = new CheckBox(I18N.message("follow.up"));
		cbFollowUp.addValueChangeListener(new ValueChangeListener() {
	
			private static final long serialVersionUID = 2589189768650090798L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbFollowUp.getValue()) {
					cbMotoFound.setValue(false);
					dfMotoFoundDate.setValue(null);
					dfMotoFoundDate.setEnabled(false);
					cbxMotoFoundTime.setValue(null);
					cbxMotoFoundTime.setEnabled(false);
					dfFollowUpDate.setEnabled(true);
				}
				
			}
		});
		dfFollowUpDate = ComponentFactory.getAutoDateField();
		dfFollowUpDate.setWidth("95px");
				
		cbMotoFound.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 2158074782268019665L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbMotoFound.getValue()) {
					cbFollowUp.setValue(false);
					dfFollowUpDate.setValue(null);
					dfFollowUpDate.setEnabled(false);
					dfMotoFoundDate.setEnabled(true);
					cbxMotoFoundTime.setEnabled(true);
				}
			}
		});
		
		txtResultRemark = ComponentFactory.getTextField();
		btnSave = ComponentLayoutFactory.getButtonStyle("save", FontAwesome.SAVE, 70, "btn btn-success button-small");
		btnSave.addClickListener(this);
	}
	
	/**
	 * Assign Value
	 * @param contract
	 */
	public void assignValue(Contract contract) {
		this.contract = contract;
		contractFlag = getContractFlag(contract, EFlag.RETURN);
		if (contractFlag == null) {
			contractFlag = getContractFlag(contract, EFlag.REPO);
		}
		btnEdit.setCaption(I18N.message("edit"));
		if (contractFlag != null) {
			lblFlagValue.setValue(getDescription(contractFlag.getFlag() != null ? contractFlag.getFlag().getDescLocale() : ""));
			lblCreateDateValue.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getCreateDate())));
			lblCreateByValue.setValue(getDescription(contractFlag.getCreateUser()));
			lblStatusValue.setValue(getDescription(contractFlag.getWkfStatus().getDescLocale()));
			if (contractFlag.getDate() != null) {
				lblDateValue.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getDate())));
				lblTimeValue.setValue(getDescription(getDefaultString(convertTime(contractFlag.getDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getDate()).getTime()))));
			}
			lblRemarkValue.setValue(getDescription(contractFlag.getComment()));
			mainPanel.setContent(createReturnSubmittedLayout());
			if (contractFlag.isCompleted()) {
				if (contractFlag.getActionDate() != null) {
					lblMotoFoundDateValue.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getActionDate())));
					lblMotoFoundTimeValue.setValue(getDescription(getDefaultString(convertTime(contractFlag.getActionDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getActionDate()).getTime()))));
				}				
				cbMotoFound.setValue(contractFlag.isCompleted());
				motoFoundLayout();
			} else {
				cbFollowUp.setValue(true);
				dfFollowUpDate.setValue(contractFlag.getActionDate());
			}
			txtResultRemark.setValue(getDefaultString(contractFlag.getResultRemark()));
			// Location 
			lblLocation1Value.setValue(getDescription(contractFlag.getLocation() != null ? contractFlag.getLocation().getDescLocale() : "" ));
			if (ELocation.BRANCH.equals(contractFlag.getLocation())) {
				lblLocation2Caption.setValue(ELocation.BRANCH.getDescLocale());
				lblLocation2Value.setValue(contractFlag.getBranch() != null ? contractFlag.getBranch().getNameLocale() : "");
			} else if (ELocation.DEALERSHOP.equals(contractFlag.getLocation())) {
				lblLocation2Caption.setValue(ELocation.DEALERSHOP.getDescLocale());
				lblLocation2Value.setValue(getDescription(contractFlag.getDealer() != null ? contractFlag.getDealer().getNameLocale() : ""));
			} else if (ELocation.CUSTOMERADDRESSS.equals(contractFlag.getLocation())) {
				lblLocation2Caption.setValue(ELocation.CUSTOMERADDRESSS.getDescLocale());
				lblLocation2Value.setValue(getDescription(ADDRESS_SRV.getDetailAddress(contractFlag.getAddress())));
				lblLocation1Value.setValue(getDescription(displayGeneralAddress(contractFlag)));
			} else if (ELocation.OTHER.equals(contractFlag.getLocation())) {
				lblLocation2Caption.setValue(ELocation.OTHER.getDescLocale());
				lblLocation2Value.setValue(getDescription(contractFlag.getOtherLocationValue()));
			}	
			lblLocation2Caption.setVisible(true);
		} else {
			isEdit = false;
			lblLocation2Caption.setVisible(false);
			location2.setVisible(false);
			mainPanel.setContent(createReturnFormLayout(false));
		}
	}
	
	/**
	 * get list of ContractFlage
	 * @param contract
	 * @return
	 */
	private ContractFlag getContractFlag(Contract contract, EFlag flag) {
		BaseRestrictions<ContractFlag> restrictions	= new BaseRestrictions<>(ContractFlag.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addCriterion(Restrictions.eq("flag", flag));
		restrictions.addOrder(Order.asc("createDate"));
		List<ContractFlag> contractFlags = ENTITY_SRV.list(restrictions);
		if (!contractFlags.isEmpty()) {
			return contractFlags.get(0);
		}
		return null;
	}
	
	/**
	 * createReturnFormLayout
	 */
	private VerticalLayout createReturnFormLayout(boolean isShowForm) {
		
		HorizontalLayout openLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		openLayout.addComponent(cbxFlag);
		openLayout.addComponent(btnOpen);
		
		Label lblDateCaption = ComponentFactory.getLabel(I18N.message("date"));
		Label lblTimeCaption = ComponentFactory.getLabel(I18N.message("time"));
		Label lblLocation1 = ComponentFactory.getLabel(I18N.message("location"));
		lblLocation1.setWidthUndefined();
		lblLocation1.setWidthUndefined();
		
		Label lblRemark = ComponentFactory.getLabel(I18N.message("remark"));
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnSubmit);
		buttonLayout.addComponent(btnCancel);
		
		HorizontalLayout buttonEditLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonEditLayout.addComponent(btnEdit);
		buttonEditLayout.addComponent(btnWithdraw);
		
		HorizontalLayout timeLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		timeLayout.addComponent(lblTimeCaption);
		timeLayout.addComponent(cbxTime);
		timeLayout.addComponent(lblLocation1);
		timeLayout.addComponent(cbxLocation1);
		
		timeLayout.setComponentAlignment(lblTimeCaption, Alignment.MIDDLE_LEFT);
		timeLayout.setComponentAlignment(lblLocation1, Alignment.MIDDLE_LEFT);
		
		returnGridLayout = ComponentLayoutFactory.getGridLayout(7, 3);
		returnGridLayout.setVisible(isShowForm);
		int iCol = 0;
		returnGridLayout.addComponent(lblDateCaption, iCol++, 0);
		returnGridLayout.addComponent(dfDate, iCol++, 0);
		returnGridLayout.addComponent(timeLayout, iCol++, 0);
		
		iCol = 0;
		returnGridLayout.addComponent(lblLocation2Caption, iCol++, 1);
		returnGridLayout.addComponent(location2, iCol++, 1);
		returnGridLayout.addComponent(lblAddress, iCol++, 1);
		
		iCol = 0;
		returnGridLayout.addComponent(lblRemark, iCol++, 2);
		returnGridLayout.addComponent(txtRemark, iCol++, 2);
		if (isEdit) {
			returnGridLayout.addComponent(buttonEditLayout, iCol++, 2);
		} else {
			returnGridLayout.addComponent(buttonLayout, iCol++, 2);
		}
		
		returnGridLayout.setComponentAlignment(lblDateCaption, Alignment.MIDDLE_LEFT);
		returnGridLayout.setComponentAlignment(lblLocation2Caption, Alignment.MIDDLE_LEFT);
		returnGridLayout.setComponentAlignment(lblRemark, Alignment.MIDDLE_LEFT);
		returnGridLayout.setComponentAlignment(lblAddress, Alignment.MIDDLE_LEFT);
		
		VerticalLayout returnLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		returnLayout.addComponent(messagePanel);
		returnLayout.addComponent(openLayout);
		returnLayout.addComponent(returnGridLayout);
		
		return returnLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout createReturnSubmittedLayout() {
		
		submittedGridLayout = new GridLayout(10, 5);
		submittedGridLayout.setSpacing(true);
		submittedGridLayout.setMargin(new MarginInfo(true, true, false, true));
		
		int iCol = 0;
		submittedGridLayout.addComponent(new Label(I18N.message("open.claim")), iCol++, 0);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 0);
		submittedGridLayout.addComponent(lblFlagValue, iCol++, 0);
		
		iCol = 0;
		submittedGridLayout.addComponent(new Label(I18N.message("create.date")), iCol++, 1);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 1);
		submittedGridLayout.addComponent(lblCreateDateValue, iCol++, 1);
		submittedGridLayout.addComponent(new Label(I18N.message("created.by")), iCol++, 1);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 1);
		submittedGridLayout.addComponent(lblCreateByValue, iCol++, 1);
		
		if (!EFlag.REPO.equals(contractFlag.getFlag())) {
			submittedGridLayout.addComponent(new Label(I18N.message("status")), iCol++, 1);
			submittedGridLayout.addComponent(new Label(":"), iCol++, 1);
			submittedGridLayout.addComponent(lblStatusValue, iCol++, 1);
		}
		
		iCol = 0;
		submittedGridLayout.addComponent(new Label(I18N.message("date")), iCol++, 2);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 2);
		submittedGridLayout.addComponent(lblDateValue, iCol++, 2);
		submittedGridLayout.addComponent(new Label(I18N.message("time")), iCol++, 2);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 2);
		submittedGridLayout.addComponent(lblTimeValue, iCol++, 2);
		submittedGridLayout.addComponent(new Label(I18N.message("location")), iCol++, 2);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 2);
		submittedGridLayout.addComponent(lblLocation1Value, iCol++, 2);
		
		iCol = 0;
		submittedGridLayout.addComponent(lblLocation2Caption, iCol++, 3);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 3);
		submittedGridLayout.addComponent(lblLocation2Value, iCol++, 3);
		submittedGridLayout.addComponent(new Label(I18N.message("remark")), iCol++, 3);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 3);
		submittedGridLayout.addComponent(lblRemarkValue, iCol++, 3);
		submittedGridLayout.addComponent(btnEdit, iCol++, 3);
		submittedGridLayout.addComponent(btnWithdraw, iCol + 1, 3);
		
		HorizontalLayout generalAddressLayout = new HorizontalLayout();
		if (ELocation.CUSTOMERADDRESSS.equals(contractFlag.getLocation())) {
			submittedGridLayout.removeComponent(6, 2);
			submittedGridLayout.removeComponent(7, 2);
			submittedGridLayout.removeComponent(8, 2);
			
			submittedGridLayout.removeComponent(0, 3);
			submittedGridLayout.removeComponent(1, 3);
			submittedGridLayout.removeComponent(2, 3);
			
			submittedGridLayout.removeComponent(3, 3);
			submittedGridLayout.removeComponent(4, 3);
			submittedGridLayout.removeComponent(5, 3);
			submittedGridLayout.removeComponent(6, 3);
			submittedGridLayout.removeComponent(8, 3);
			
			iCol = 0;
			submittedGridLayout.addComponent(new Label(I18N.message("remark")), iCol++, 3);
			submittedGridLayout.addComponent(new Label(":"), iCol++, 3);
			submittedGridLayout.addComponent(lblRemarkValue, iCol++, 3);
			submittedGridLayout.addComponent(btnEdit, iCol++, 3);
			submittedGridLayout.addComponent(btnWithdraw, iCol + 1, 3);
			
			Label locationCaption = new Label(I18N.message("location") + " : ");
			generalAddressLayout.setSpacing(true);
			generalAddressLayout.setMargin(new MarginInfo(false, true, true, true));
			generalAddressLayout.addComponent(locationCaption);
			generalAddressLayout.addComponent(lblLocation1Value);
			generalAddressLayout.setComponentAlignment(locationCaption, Alignment.MIDDLE_LEFT);
		}
		
		Label lblResult = ComponentFactory.getLabel(I18N.message("result"));
		Label label = ComponentFactory.getLabel(":");
		Label lblRemark = ComponentFactory.getLabel(I18N.message("remark"));
		lblRemark.setWidthUndefined();
		resultLayout = ComponentLayoutFactory.getGridLayout(7, 2);
		resultLayout.setSpacing(true);
		resultLayout.setMargin(true);
		
		iCol = 0;
		resultLayout.addComponent(lblResult, iCol++, 0);
		resultLayout.addComponent(label, iCol++, 0);
		resultLayout.addComponent(cbMotoFound, iCol++, 0);
		resultLayout.addComponent(dfMotoFoundDate, iCol++, 0);
		resultLayout.addComponent(cbxMotoFoundTime, iCol++, 0);
		
		iCol = 2;
		resultLayout.addComponent(cbFollowUp, iCol++, 1);
		resultLayout.addComponent(dfFollowUpDate, iCol++, 1);
		resultLayout.addComponent(lblRemark, iCol++, 1);
		resultLayout.addComponent(txtResultRemark, iCol++, 1);
		resultLayout.addComponent(btnSave, iCol++, 1);
		
		resultLayout.setComponentAlignment(lblResult, Alignment.MIDDLE_LEFT);
		resultLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		resultLayout.setComponentAlignment(lblRemark, Alignment.MIDDLE_LEFT);
		
		lblLocation2Caption.setVisible(true);
		location2.setVisible(true);
		
		Panel resultPanel = new Panel(resultLayout);
		
		VerticalLayout submitedLayout = new VerticalLayout();
		submitedLayout.setSpacing(true);
		submitedLayout.setMargin(true);
		submitedLayout.addComponent(submittedGridLayout);
		if (ELocation.CUSTOMERADDRESSS.equals(contractFlag.getLocation())) {
			submitedLayout.addComponent(generalAddressLayout);
		}
		if (!EFlag.REPO.equals(contractFlag.getFlag())) {
			submitedLayout.addComponent(resultPanel);
		}
		
		return submitedLayout;
	}
	
	/**
	 * motoFoundLayout
	 */
	private void motoFoundLayout() {
		submittedGridLayout.removeComponent(btnEdit);
		submittedGridLayout.removeComponent(btnWithdraw);
		resultLayout.removeComponent(btnSave);
		resultLayout.removeComponent(dfMotoFoundDate);
		resultLayout.removeComponent(cbxMotoFoundTime);
		cbMotoFound.setEnabled(false);
		cbFollowUp.setEnabled(false);
		dfFollowUpDate.setEnabled(false);
		txtResultRemark.setEnabled(false);
		
		Label lblDate = ComponentFactory.getLabel(I18N.message("date"));
		Label lblTime = ComponentFactory.getLabel(I18N.message("time"));
		
		HorizontalLayout motoFoundDateLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		HorizontalLayout motoFoundTimeLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		
		motoFoundDateLayout.addComponent(lblDate);
		motoFoundDateLayout.addComponent(lblMotoFoundDateValue);
		
		motoFoundTimeLayout.addComponent(lblTime);
		motoFoundTimeLayout.addComponent(lblMotoFoundTimeValue);
	
		resultLayout.addComponent(motoFoundDateLayout, 3, 0);
		resultLayout.addComponent(motoFoundTimeLayout, 5, 0);

		resultLayout.setComponentAlignment(motoFoundDateLayout, Alignment.MIDDLE_LEFT);
		resultLayout.setComponentAlignment(motoFoundTimeLayout, Alignment.MIDDLE_LEFT);
	}
	
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		
		cbxFlag.setSelectedEntity(null);
		dfDate.setValue(null);
		cbxTime.setValue(null);
		cbxLocation1.setSelectedEntity(null);
		txtRemark.setValue("");
		lblAddress.setValue("");
		
		cbMotoFound.setEnabled(true);
		cbMotoFound.setValue(false);
		dfMotoFoundDate.setEnabled(true);
		dfMotoFoundDate.setValue(null);
		cbxMotoFoundTime.setEnabled(true);
		cbxMotoFoundTime.setValue(null);
		
		cbFollowUp.setEnabled(true);
		cbFollowUp.setValue(false);
		dfFollowUpDate.setEnabled(true);
		dfFollowUpDate.setValue(null);
		txtResultRemark.setEnabled(true);
		txtResultRemark.setValue("");
		
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
	
		returnGridLayout.setVisible(false);
	}
	
	/**
	 * get return / repo values to combox
	 * @return
	 */
	private List<EFlag> getReturnRepoValues() {
		List<EFlag> flags  = new ArrayList<EFlag>();
		flags.add(EFlag.RETURN);
		flags.add(EFlag.REPO);
		return flags;
	}
	
	/**
	 * 
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
	 * 
	 * @param contractFlag
	 * @return
	 */
	private String displayGeneralAddress(ContractFlag contractFlag) {
		Address address = contractFlag.getAddress();
		String generalAddress = contractFlag.getLocation().getDescLocale() + ", " + address.getType().getDescLocale() + ", " + ADDRESS_SRV.getDetailAddress(address);
		return generalAddress;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}

	/**
	 * AssignValue for Edit
	 */
	private void assignValueEditForm() {
		reset();
		
		if (contractFlag != null) {
			cbxFlag.setSelectedEntity(contractFlag.getFlag());
			if (contractFlag.getDate() != null) {
				dfDate.setValue(contractFlag.getDate());
				cbxTime.setValue(contractFlag.getDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getDate()).getTime());
			}
			txtRemark.setValue(contractFlag.getComment());
			cbxLocation1.setSelectedEntity(contractFlag.getLocation());
			if (ELocation.BRANCH.equals(contractFlag.getLocation())) {
				cbxBranch.setSelectedEntity(contractFlag.getBranch());
			} else if (ELocation.DEALERSHOP.equals(contractFlag.getLocation())) {
				cbxDealer.setSelectedEntity(contractFlag.getDealer());
			} else if (ELocation.CUSTOMERADDRESSS.equals(contractFlag.getLocation())) {
				cbxTypeAddress.setSelectedEntity(contractFlag.getAddress().getType());
			} else if (ELocation.OTHER.equals(contractFlag.getLocation())) {
				txtOtherLocation.setValue(contractFlag.getOtherLocationValue());
			}	
		}
	}

	/**
	 * Convert Time
	 * @param time
	 * @return
	 */
	private String convertTime(long time) {
		long timeDifference = time/1000;
		int h = (int) (timeDifference / (3600));
		int m = (int) ((timeDifference - (h * 3600)) / 60);
		return String.format("%02d:%02d", h,m);
	}
	
	/**
	 * Save 
	 */
	private void save() {
			contractFlag = getContractFlag(contract, cbxFlag.getSelectedEntity());
			if (contractFlag == null) {
				contractFlag = ContractFlag.createInstance();
				contractFlag.setContract(contract);
				contractFlag.setFlag(cbxFlag.getSelectedEntity());
				contractFlag.setWkfStatus(ReturnWkfStatus.REPEN);
			}
			Date date = dfDate.getValue();
			if (date != null) {
				date = DateUtils.getDateAtBeginningOfDay(date);
				long time = (long) cbxTime.getValue();
				date.setTime(date.getTime() + time);
			}
			contractFlag.setDate(date);
			contractFlag.setComment(txtRemark.getValue());
			contractFlag.setLocation(cbxLocation1.getSelectedEntity());
			if (ELocation.BRANCH.equals(cbxLocation1.getSelectedEntity())) {
				contractFlag.setBranch(cbxBranch.getSelectedEntity());
			} else if (ELocation.DEALERSHOP.equals(cbxLocation1.getSelectedEntity())) {
				contractFlag.setDealer(cbxDealer.getSelectedEntity());
			} else if (ELocation.CUSTOMERADDRESSS.equals(cbxLocation1.getSelectedEntity())) {
				contractFlag.setAddress(addressSelected);
			} else if (ELocation.OTHER.equals(cbxLocation1.getSelectedEntity())) {
				contractFlag.setOtherLocationValue(txtOtherLocation.getValue());
			}
			try {
				ENTITY_SRV.saveOrUpdate(contractFlag);
				ComponentLayoutFactory.displaySuccessfullyMsg();
				assignValue(contract);
				mainPanel.setContent(createReturnSubmittedLayout());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	
	/**
	 * Save Return Result
	 */
	private void saveReturnResult() {
		if (cbMotoFound.getValue()) {
			motoFoundLayout();
			contractFlag.setWkfStatus(ReturnWkfStatus.RECLO);
			Date motoFoundDate = dfMotoFoundDate.getValue();
			if (motoFoundDate != null) {
				motoFoundDate = DateUtils.getDateAtBeginningOfDay(motoFoundDate);
				long time = (long) cbxMotoFoundTime.getValue();
				motoFoundDate.setTime(motoFoundDate.getTime() + time);
			}
			contractFlag.setActionDate(motoFoundDate);
			contractFlag.setCompleted(cbMotoFound.getValue());
		} else if (cbFollowUp.getValue()) {
			contractFlag.setActionDate(dfFollowUpDate.getValue());
			contractFlag.setResultRemark(txtResultRemark.getValue());
			contractFlag.setCompleted(false);
		}
		ENTITY_SRV.saveOrUpdate(contractFlag);
		ComponentLayoutFactory.displaySuccessfullyMsg();
		assignValue(contract);
	}
	
	/**
	 * 
	 * @param contractFlag
	 * @param flag
	 */
	private void confirmWithdraw(ContractFlag contractFlag) {
		ConfirmDialog.show(UI.getCurrent(), I18N.message("withdraw"), new ConfirmDialog.Listener() {

			private static final long serialVersionUID = 6224381821671532392L;

			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					ENTITY_SRV.delete(contractFlag);
					ComponentLayoutFactory.getNotificationDesc(contractFlag.getId().toString(), "withdraw.successfully");
					reset();
					isEdit = false;
					assignValue(contract);
				}
			}
		});
	}
	
	/**
	 * Validate before Submit
	 */
	private boolean validate() {
		errors = new ArrayList<>();
		messagePanel.removeAllComponents();
		Label messageLabel;
		
		checkMandatoryDateField(dfDate, "date");
		checkMandatorySelectField(cbxLocation1, "location");
		
		if (!errors.isEmpty()) {
			for (String error : errors) {
				messageLabel = new Label();
				messageLabel.addStyleName("error");
				messageLabel.setValue(error);
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
		return errors.isEmpty();
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnOpen) {
			returnGridLayout.setVisible(true);
		} else if (event.getButton() == btnCancel) {
			reset();
		} else if (event.getButton() == btnSubmit) {
			if (validate()) {
				save();
			}
		} else if (event.getButton() == btnSave) {
			saveReturnResult();
		} else if (event.getButton() == btnEdit) {
			if (I18N.message("edit").equals(btnEdit.getCaption())) {
				btnEdit.setCaption(I18N.message("save"));
				btnEdit.setIcon(FontAwesome.SAVE);
				isEdit = true;
				assignValueEditForm();
			} else {
				if (validate()) {
					btnEdit.setCaption(I18N.message("edit"));
					btnEdit.setIcon(FontAwesome.EDIT);
					isEdit = false;
					save();
				}
			}
		} else if (event.getButton() == btnWithdraw) {
			confirmWithdraw(contractFlag);
		}
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		lblAddress.setValue("");
		lblLocation2Caption.setVisible(true);
		location2.setVisible(true);
		if (ELocation.BRANCH.equals(cbxLocation1.getSelectedEntity())) {
			lblLocation2Caption.setValue(ELocation.BRANCH.getDescLocale());
			cbxBranch = new EntityComboBox<>(OrgStructure.class, "nameEn");
			cbxBranch.setImmediate(true);
			cbxBranch.renderer();
			cbxBranch.addValueChangeListener(new ValueChangeListener() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					lblAddress.setValue(getDescription(ADDRESS_SRV.getBranchAddress(cbxBranch.getSelectedEntity())));
				}
			});
			location2 = cbxBranch;
		} else if (ELocation.DEALERSHOP.equals(cbxLocation1.getSelectedEntity())) {
			lblLocation2Caption.setValue(ELocation.DEALERSHOP.getDescLocale());
			BaseRestrictions<Dealer> restrictions = new BaseRestrictions<>(Dealer.class);
			List<Dealer> dealers = ENTITY_SRV.list(restrictions);
			cbxDealer = new DealerComboBox(dealers);
			cbxDealer.addValueChangeListener(new ValueChangeListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					lblAddress.setValue(getDescription(ADDRESS_SRV.getDealerAddress(cbxDealer.getSelectedEntity())));	
				}
			});
			location2 = cbxDealer;
		} else if (ELocation.CUSTOMERADDRESSS.equals(cbxLocation1.getSelectedEntity())){
			lblLocation2Caption.setValue(I18N.message("address.type"));
		
			cbxTypeAddress = new EntityRefComboBox<>(ETypeAddress.values());
			cbxTypeAddress.setWidth("200px");
			cbxTypeAddress.addValueChangeListener(new ValueChangeListener() {

				private static final long serialVersionUID = 8768500532610318545L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					addressSelected = ADDRESS_SRV.getAddress(contract, cbxTypeAddress.getSelectedEntity()); 
					lblAddress.setValue(addressSelected != null ? ADDRESS_SRV.getDetailAddress(addressSelected) : "");
				}
			});
			location2 = cbxTypeAddress;
		} else if (ELocation.OTHER.equals(cbxLocation1.getSelectedEntity())) {
			lblLocation2Caption.setValue(ELocation.OTHER.getDescLocale());
			txtOtherLocation = ComponentFactory.getTextField();
			location2 = txtOtherLocation;
		} else {
			lblLocation2Caption.setVisible(false);
			location2.setVisible(false);
		}
		mainPanel.setContent(createReturnFormLayout(true));
	}

}
