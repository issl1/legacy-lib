package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.isr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.model.entity.RefDataId;
import org.seuksa.frmk.tools.DateUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.collection.model.EFlag;
import com.nokor.efinance.core.common.reference.model.PoliceStation;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.ISRWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.insurance.AssetInsurancePanel;
import com.nokor.efinance.third.finwiz.client.ins.ClientInsurance;
import com.nokor.ersys.core.hr.model.address.Commune;
import com.nokor.ersys.core.hr.model.address.District;
import com.nokor.ersys.core.hr.model.address.Province;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityComboBox;
import com.nokor.frmk.vaadin.ui.widget.combo.EntityRefComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.util.i18n.I18N;
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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class ISRTabPanel extends AbstractControlPanel implements FMEntityField, ClickListener {

	/** */
	private static final long serialVersionUID = -6844889681810755807L;
	
	private ERefDataComboBox<EFlag> cbxFlag;
	private Button btnOpenClaim;
	
	private AutoDateField dfDate;
	private ComboBox cbxTime;
	
	private EntityComboBox<Province> cbxProvince;
	private EntityComboBox<District> cbxDistrict;
	private EntityComboBox<Commune> cbxSubDistrict;
	
	private TextField txtLocation;
	private EntityComboBox<PoliceStation> cbxPoliceStation;
	private TextArea txtComment;
	
	private Button btnSubmit;
	private Button btnCancel;
	
	private GridLayout ISRGridLayout;
	
	private Panel mainPanel;
	private Contract contract;
	private ContractFlag contractFlag;
	
	private VerticalLayout messagePanel;
	
	private Label lblFlagValue;
	private Label lblCrateDateValue;
	private Label lblCreateByValue;
	private Label lblStatusValue;
	
	private Label lblDateValue;
	private Label lblTimeValue;
	private Label lblProvinceValue;
	private Label lblDistrictValue;
	private Label lblSubDistrictValue;
	
	private Label lblLocationValue;
	private Label lblPoliceStationValue;
	private Label lblCommentValue;
	
	private Button btnEdit;
	private Button btnWithdraw;
	
	private AssetInsurancePanel insurancePanel;
	
	public ISRTabPanel() {
		setMargin(true);
		setSpacing(true);
		init();
		
		insurancePanel = new AssetInsurancePanel();
		TabSheet insTab = new TabSheet();
		insTab.addTab(insurancePanel, I18N.message("insurance"));
		
		mainPanel = new Panel();
		mainPanel.setContent(getISRFormLayout());
		addComponent(mainPanel);
		addComponent(insTab);
	}
	
	/**
	 * init()
	 */
	@SuppressWarnings("unchecked")
	private void init() {
		messagePanel = new VerticalLayout();
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		btnOpenClaim = ComponentLayoutFactory.getButtonStyle("open.claim", null, 120, "btn btn-success button-small");
		btnOpenClaim.addClickListener(this);
		
	
		
		cbxFlag = new ERefDataComboBox<>(ISRValues());
		cbxFlag.addValueChangeListener(new ValueChangeListener() {
			
			private static final long serialVersionUID = -1112537066277977858L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxFlag.getSelectedEntity() == null) {
					btnOpenClaim.setEnabled(false);
				} else {
					btnOpenClaim.setEnabled(true);
				}
				
			}
		});
		
		dfDate = ComponentFactory.getAutoDateField();
		
		cbxTime = ComponentLayoutFactory.getTimeComboBox();
		cbxTime.setWidth(80, Unit.PIXELS);
		cbxProvince = new EntityComboBox<>(Province.class, "desc");
		cbxProvince.setEntities(ENTITY_SRV.list(new BaseRestrictions(Province.class)));
		cbxProvince.setWidth("150px");
		
		cbxDistrict = new EntityComboBox<>(District.class, "desc");
		cbxDistrict.setEntities(ENTITY_SRV.list(new BaseRestrictions(District.class)));
		cbxDistrict.setWidth("150px");
		
		cbxSubDistrict = new EntityComboBox<>(Commune.class, "desc");
		cbxSubDistrict.setEntities(ENTITY_SRV.list(new BaseRestrictions(Commune.class)));
		cbxSubDistrict.setWidth("150px");
		
		txtLocation = ComponentFactory.getTextField(150, 150);
		
		cbxPoliceStation = new EntityComboBox<>(PoliceStation.class, "desc");
		cbxPoliceStation.setWidth("150px");
		cbxPoliceStation.setImmediate(true);
		
		txtComment = ComponentFactory.getTextArea(false, 150, 50);
		txtComment.setMaxLength(250);
		
		btnSubmit = ComponentLayoutFactory.getButtonStyle("submit", FontAwesome.CHECK, 70, "btn btn-success button-small");
		btnSubmit.addClickListener(this);
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 70, "btn btn-danger button-small");
		btnCancel.addClickListener(this);
		
		cbxProvince.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -572281953646438700L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxProvince.getSelectedEntity() != null) {
					BaseRestrictions<District> restrictions = new BaseRestrictions<>(District.class);
					restrictions.addCriterion(Restrictions.eq(PROVINCE + "." + ID, cbxProvince.getSelectedEntity().getId()));
					cbxDistrict.setEntities(ENTITY_SRV.list(restrictions));
				
					cbxPoliceStation.renderer(getPoliceStationByProvince(cbxProvince.getSelectedEntity()));
				} else {
					cbxDistrict.clear();
					cbxPoliceStation.clear();
				}
				cbxSubDistrict.clear();
			}
		});
		
		cbxDistrict.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -470992549634354195L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDistrict.getSelectedEntity() != null) {
					BaseRestrictions<Commune> restrictions = new BaseRestrictions<>(Commune.class);
					restrictions.addCriterion(Restrictions.eq(DISTRICT + "." + ID, cbxDistrict.getSelectedEntity().getId()));
					cbxSubDistrict.setEntities(ENTITY_SRV.list(restrictions));
				} else {
					cbxSubDistrict.clear();
				}
			}
		});
		
		lblFlagValue = getLabelValue();
		lblFlagValue.setWidthUndefined();
		lblCrateDateValue = getLabelValue();
		lblCrateDateValue.setWidthUndefined();
		lblCreateByValue = getLabelValue();
		lblCreateByValue.setWidthUndefined();
		lblStatusValue = getLabelValue();
		lblStatusValue.setWidthUndefined();
		lblDateValue = getLabelValue();
		lblDateValue.setWidthUndefined();
		lblTimeValue = getLabelValue();
		lblTimeValue.setWidthUndefined();
		lblProvinceValue = getLabelValue();
		lblProvinceValue.setWidthUndefined();
		lblDistrictValue = getLabelValue();
		lblDistrictValue.setWidthUndefined();
		lblSubDistrictValue = getLabelValue();
		lblSubDistrictValue.setWidthUndefined();
		lblLocationValue = getLabelValue();
		lblLocationValue.setWidthUndefined();
		lblPoliceStationValue = getLabelValue();
		lblPoliceStationValue.setWidthUndefined();
		lblCommentValue = getLabelValue();
		lblCommentValue.setWidthUndefined();
		
		btnEdit = ComponentLayoutFactory.getButtonStyle("edit", FontAwesome.EDIT, 70, "btn btn-success button-small");
		btnEdit.addClickListener(this);
		btnWithdraw = ComponentLayoutFactory.getButtonStyle("withdraw", null, 70, "btn btn-success button-small");
		btnWithdraw.addClickListener(this);
	}
	
	/**
	 * Create ISR Form that not yet submitted
	 * @return
	 */
	private VerticalLayout getISRFormLayout() {
		
		HorizontalLayout openClaimLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		openClaimLayout.addComponent(cbxFlag);
		openClaimLayout.addComponent(btnOpenClaim);
		
		ISRGridLayout = new GridLayout(6, 3);
		ISRGridLayout.setMargin(true);
		ISRGridLayout.setSpacing(true);
		ISRGridLayout.setVisible(false);
		
		Label lblDate = ComponentLayoutFactory.getLabelCaption(I18N.message("date"));
		Label lblTime = ComponentLayoutFactory.getLabelCaption(I18N.message("time"));
		Label lblProvince = ComponentLayoutFactory.getLabelCaption(I18N.message("province"));
		Label lblDistrict = ComponentLayoutFactory.getLabelCaption(I18N.message("district"));
		Label lblSubDistrict = ComponentLayoutFactory.getLabelCaption(I18N.message("sub.district"));
		
		Label lblLocation = ComponentLayoutFactory.getLabelCaption(I18N.message("location"));
		Label lblPoliceStation = ComponentLayoutFactory.getLabelCaption(I18N.message("police.station"));
		
		
		int iCol = 0;
		ISRGridLayout.addComponent(lblDate, iCol++, 0);
		ISRGridLayout.addComponent(dfDate, iCol++, 0);
		ISRGridLayout.addComponent(lblTime, iCol++, 0);
		ISRGridLayout.addComponent(cbxTime, iCol++, 0);
		ISRGridLayout.addComponent(lblProvince, iCol++, 0);
		ISRGridLayout.addComponent(cbxProvince, iCol++, 0);
		
		iCol = 0;
		ISRGridLayout.addComponent(lblDistrict, iCol++, 1);
		ISRGridLayout.addComponent(cbxDistrict, iCol++, 1);
		ISRGridLayout.addComponent(lblSubDistrict, iCol++, 1);
		ISRGridLayout.addComponent(cbxSubDistrict, iCol++, 1);
		ISRGridLayout.addComponent(lblLocation, iCol++, 1);
		ISRGridLayout.addComponent(txtLocation, iCol++, 1);
		
		iCol = 0;
		ISRGridLayout.addComponent(lblPoliceStation, iCol++, 2);
		ISRGridLayout.addComponent(cbxPoliceStation, iCol++, 2);
		ISRGridLayout.addComponent(ComponentLayoutFactory.getLabelCaptionRequired("comment"), iCol++, 2);
		ISRGridLayout.addComponent(txtComment, iCol++, 2);
		ISRGridLayout.addComponent(btnSubmit, iCol++, 2);
		ISRGridLayout.addComponent(btnCancel, iCol++, 2);
		
		ISRGridLayout.setComponentAlignment(lblDate, Alignment.MIDDLE_LEFT);
		ISRGridLayout.setComponentAlignment(lblTime, Alignment.MIDDLE_LEFT);
		ISRGridLayout.setComponentAlignment(lblProvince, Alignment.MIDDLE_LEFT);
		ISRGridLayout.setComponentAlignment(lblDistrict, Alignment.MIDDLE_LEFT);
		ISRGridLayout.setComponentAlignment(lblSubDistrict, Alignment.MIDDLE_LEFT);
		ISRGridLayout.setComponentAlignment(lblLocation, Alignment.MIDDLE_LEFT);
		
		ISRGridLayout.setComponentAlignment(btnSubmit, Alignment.MIDDLE_LEFT);
		ISRGridLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
		
		VerticalLayout ISRLayout = new VerticalLayout();
		ISRLayout.addComponent(messagePanel);
		ISRLayout.addComponent(openClaimLayout);
		ISRLayout.addComponent(ISRGridLayout);
		
		return ISRLayout;
	}
	
	/**
	 * Layout ISR Submitted
	 * @return
	 */
	private GridLayout getISRSubmittedLayout() {
		
		GridLayout submittedGridLayout = new GridLayout(10, 5);
		submittedGridLayout.setSpacing(true);
		submittedGridLayout.setMargin(true);
		
		int iCol = 0;
		submittedGridLayout.addComponent(new Label(I18N.message("open.claim")), iCol++, 0);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 0);
		submittedGridLayout.addComponent(lblFlagValue, iCol++, 0);
		
		iCol = 0;
		submittedGridLayout.addComponent(new Label(I18N.message("create.date")), iCol++, 1);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 1);
		submittedGridLayout.addComponent(lblCrateDateValue, iCol++, 1);
		submittedGridLayout.addComponent(new Label(I18N.message("created.by")), iCol++, 1);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 1);
		submittedGridLayout.addComponent(lblCreateByValue, iCol++, 1);
		submittedGridLayout.addComponent(new Label(I18N.message("status")), iCol++, 1);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 1);
		submittedGridLayout.addComponent(lblStatusValue, iCol++, 1);
		
		iCol = 0;
		submittedGridLayout.addComponent(new Label(I18N.message("date")), iCol++, 2);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 2);
		submittedGridLayout.addComponent(lblDateValue, iCol++, 2);
		submittedGridLayout.addComponent(new Label(I18N.message("time")), iCol++, 2);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 2);
		submittedGridLayout.addComponent(lblTimeValue, iCol++, 2);
		submittedGridLayout.addComponent(new Label(I18N.message("province")), iCol++, 2);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 2);
		submittedGridLayout.addComponent(lblProvinceValue, iCol++, 2);
		
			
		iCol = 0;
		submittedGridLayout.addComponent(new Label(I18N.message("district")), iCol++, 3);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 3);
		submittedGridLayout.addComponent(lblDistrictValue, iCol++, 3);
		submittedGridLayout.addComponent(new Label(I18N.message("sub.district")), iCol++, 3);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 3);
		submittedGridLayout.addComponent(lblSubDistrictValue, iCol++, 3);
		submittedGridLayout.addComponent(new Label(I18N.message("location")), iCol++, 3);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 3);
		submittedGridLayout.addComponent(lblLocationValue, iCol++, 3);
		
		iCol = 0;
		submittedGridLayout.addComponent(new Label(I18N.message("police.station")), iCol++, 4);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 4);
		submittedGridLayout.addComponent(lblPoliceStationValue, iCol++, 4);
		submittedGridLayout.addComponent(new Label(I18N.message("comment")), iCol++, 4);
		submittedGridLayout.addComponent(new Label(":"), iCol++, 4);
		submittedGridLayout.addComponent(lblCommentValue, iCol++, 4);
		submittedGridLayout.addComponent(btnEdit, iCol++, 4);
		submittedGridLayout.addComponent(btnWithdraw, 8, 4);
		
		submittedGridLayout.setComponentAlignment(lblPoliceStationValue, Alignment.TOP_LEFT);
		
		return submittedGridLayout;
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		super.reset();
		
		cbxFlag.setSelectedEntity(null);
		dfDate.setValue(null);
		cbxTime.setValue(null);
		cbxProvince.setSelectedEntity(null);
		txtLocation.setValue("");
		cbxPoliceStation.setSelectedEntity(null);
		txtComment.setValue("");
		
		messagePanel.removeAllComponents();
		messagePanel.setVisible(false);
	}
	
	/**
	 * AssignValues
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		contractFlag = getContractFlag(contract, EFlag.LOST);
		if (contractFlag == null) {
			contractFlag = getContractFlag(contract, EFlag.DAMAGE);
		} 
		if (contractFlag != null) {
			mainPanel.setContent(getISRSubmittedLayout());
			lblFlagValue.setValue(getDescription(contractFlag.getFlag() != null ? contractFlag.getFlag().getDescLocale() : ""));
			lblCrateDateValue.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getCreateDate(), "dd/MM/yyyy")));
			lblCreateByValue.setValue(getDescription(contractFlag.getCreateUser()));
			lblStatusValue.setValue(getDescription(contractFlag.getWkfStatus().getDescLocale()));
			if (contractFlag.getDate() != null) {
				lblDateValue.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getDate(), "dd/MM/yyyy")));
				lblTimeValue.setValue(getDescription(getDefaultString(convertTime(contractFlag.getDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getDate()).getTime()))));
			}
			lblProvinceValue.setValue(getDescription(getDescription(contractFlag.getProvince() != null ? contractFlag.getProvince().getDescLocale() : StringUtils.EMPTY)));
			lblDistrictValue.setValue(getDescription(contractFlag.getDistrict() != null ? contractFlag.getDistrict().getDescLocale() : StringUtils.EMPTY));
			lblSubDistrictValue.setValue(getDescription(contractFlag.getCommune() != null ? contractFlag.getCommune().getDescLocale() : StringUtils.EMPTY));
			lblLocationValue.setValue(getDescription(contractFlag.getLocantion()));
			lblPoliceStationValue.setValue(getDescription(contractFlag.getPoliceStation() == null ? "" : contractFlag.getPoliceStation().getDesc()));
			lblCommentValue.setValue(getDescription(getDefaultString(contractFlag.getComment())));
		} else {
			mainPanel.setContent(getISRFormLayout());
		}
		insurancePanel.assignValues(contract);
	}
	
	/**
	 * FormLayout 
	 */
	private void assignValueForm() {
		reset();
		
		HorizontalLayout openClaimLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		openClaimLayout.addComponent(cbxFlag);
		openClaimLayout.addComponent(btnOpenClaim);
		
		ISRGridLayout.setVisible(true);
		ISRGridLayout.removeComponent(4, 2);
		ISRGridLayout.removeComponent(5, 2);
		ISRGridLayout.addComponent(btnEdit, 4, 2);
		ISRGridLayout.addComponent(btnWithdraw, 5, 2);
		
		VerticalLayout ISRLayout = new VerticalLayout();
		ISRLayout.addComponent(messagePanel);
		ISRLayout.addComponent(openClaimLayout);
		ISRLayout.addComponent(ISRGridLayout);
		
		mainPanel.setContent(ISRLayout);
		if (contractFlag != null) {
			cbxFlag.setSelectedEntity(contractFlag.getFlag());
			if (contractFlag.getDate() != null) {
				dfDate.setValue(contractFlag.getDate());
				cbxTime.setValue(contractFlag.getDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getDate()).getTime());
			}
			cbxProvince.setSelectedEntity(contractFlag.getProvince());
			cbxDistrict.setSelectedEntity(contractFlag.getDistrict());
			cbxSubDistrict.setSelectedEntity(contractFlag.getCommune());
			txtLocation.setValue(contractFlag.getLocantion());
			cbxPoliceStation.setSelectedEntity(contractFlag.getPoliceStation());
			txtComment.setValue(getDefaultString(contractFlag.getComment()));
		}
	}
	
	/**
	 * Get ISR Values
	 * @return
	 */
	private List<EFlag> ISRValues() {
		List<EFlag> flags  = new ArrayList<EFlag>();
		flags.add(EFlag.LOST);
		flags.add(EFlag.DAMAGE);
		return flags;
	}
	
	/**
	 * 
	 * @param restrictions
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T> getEntityRefComboBox(BaseRestrictions<T> restrictions) {
		EntityRefComboBox<T> comboBox = getEntityRefComboBox();
		comboBox.setRestrictions(restrictions);
		comboBox.setImmediate(true);
		comboBox.renderer();
		return comboBox;
	}
	
	/**
	 * 
	 * @return
	 */
	private <T extends RefDataId> EntityRefComboBox<T> getEntityRefComboBox() {
		EntityRefComboBox<T> comboBox = new EntityRefComboBox<>();
		comboBox.setWidth(190, Unit.PIXELS);
		comboBox.setImmediate(true);
		return comboBox;
	}
	
	/**
	 * Validate before Submit
	 */
	private boolean validate() {
		errors = new ArrayList<>();
		messagePanel.removeAllComponents();
		Label messageLabel;
		
		checkMandatoryDateField(dfDate, "date");
		checkMandatorySelectField(cbxProvince, "province");
		checkMandatorySelectField(cbxDistrict, "district");
		checkMandatorySelectField(cbxSubDistrict, "sub.district");
		checkMandatoryField(txtLocation, "location");
		checkMandatoryField(txtComment, "comment");
		
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
	
	/**
	 * Save
	 */
	private void save() {
		contractFlag = getContractFlag(contract, cbxFlag.getSelectedEntity());
		if (contractFlag == null) {
			contractFlag = ContractFlag.createInstance();
			contractFlag.setContract(contract);
			contractFlag.setFlag(cbxFlag.getSelectedEntity());
			contractFlag.setWkfStatus(ISRWkfStatus.ISRPEN);
		}
		
		Date date = dfDate.getValue();
		if (date != null) {
			date = DateUtils.getDateAtBeginningOfDay(date);
			if (cbxTime.getValue() != null) {
				long time = (long) cbxTime.getValue();
				date.setTime(date.getTime() + time);
			}
		}
		
		contractFlag.setDate(date);
		contractFlag.setProvince(cbxProvince.getSelectedEntity());
		contractFlag.setDistrict(cbxDistrict.getSelectedEntity());
		contractFlag.setCommune(cbxSubDistrict.getSelectedEntity());
		contractFlag.setLocantion(txtLocation.getValue());
		contractFlag.setPoliceStation(cbxPoliceStation.getSelectedEntity());
		contractFlag.setComment(txtComment.getValue());
		try {
			ENTITY_SRV.saveOrUpdate(contractFlag);
			ComponentLayoutFactory.displaySuccessfullyMsg();
			assignValues(contract);
		} catch (Exception e) {
			logger.error(e.getMessage());
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
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
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
	 * 
	 * @param contractFlag
	 * @param flag
	 */
	private void confirmWithdraw(ContractFlag contractFlag) {
		ConfirmDialog.show(UI.getCurrent(), I18N.message("withdraw"),
				new ConfirmDialog.Listener() {
			
			/** */
			private static final long serialVersionUID = 6224381821671532392L;

			public void onClose(ConfirmDialog dialog) {
				if (dialog.isConfirmed()) {
					ENTITY_SRV.delete(contractFlag);
					ComponentLayoutFactory.getNotificationDesc(contractFlag.getId().toString(), "withdraw.successfully");
					reset();
					assignValues(contract);
				}
			}
		});
	}
	
	/**
	 * get list of police station by province
	 * @param province
	 * @return
	 */
	private BaseRestrictions<PoliceStation> getPoliceStationByProvince(Province province) {
		BaseRestrictions<PoliceStation> restrictions = new BaseRestrictions<>(PoliceStation.class);
		restrictions.addCriterion(Restrictions.eq("province", province));
		return restrictions;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnOpenClaim) {
			ISRGridLayout.setVisible(true);
		} else if (event.getButton() == btnCancel) {
			reset();
			ISRGridLayout.setVisible(false);
		} else if (event.getButton() == btnSubmit) {
			if (validate()) {
				save();
				SecUser user = UserSessionManager.getCurrentUser();
				if (EFlag.LOST.equals(cbxFlag.getSelectedEntity())) {
					ClientInsurance.createLostMotobikeTask(contract.getReference(), user.getLogin(), txtComment.getValue());
				} else if (EFlag.DAMAGE.equals(cbxFlag.getSelectedEntity())) {
					ClientInsurance.createDamagedMotobikeTask(contract.getReference(), user.getLogin(), txtComment.getValue());
				}
			}
		} else if (event.getButton() == btnEdit) {
			if (btnEdit.getCaption().equals(I18N.message("edit"))) {
				if (ISRWkfStatus.ISRPEN.equals(contractFlag.getWkfStatus())) {
					assignValueForm();
					btnEdit.setCaption(I18N.message("save"));
					btnEdit.setIcon(FontAwesome.SAVE);
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
							MessageBox.Icon.WARN, I18N.message("edit.available.only.status.pending"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
			} else {
				if (validate()) {
					save();
					btnEdit.setCaption(I18N.message("edit"));
					btnEdit.setIcon(FontAwesome.EDIT);
				}
			}
		} else if (event.getButton() == btnWithdraw) {
			if (ISRWkfStatus.ISRPEN.equals(contractFlag.getWkfStatus())) {
				confirmWithdraw(contractFlag);
			} else {
				MessageBox mb = new MessageBox(UI.getCurrent(), "280px", "150px", I18N.message("information"),
						MessageBox.Icon.WARN, I18N.message("withdraw.can.only.status.pending"), Alignment.MIDDLE_RIGHT,
						new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
				mb.show();
			}
			
		}
	}

}
