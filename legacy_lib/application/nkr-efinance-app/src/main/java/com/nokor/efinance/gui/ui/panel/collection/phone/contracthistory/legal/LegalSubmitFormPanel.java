package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class LegalSubmitFormPanel extends AbstractControlPanel implements ClickListener, ValueChangeListener {

	/** */
	private static final long serialVersionUID = 6421460858058980931L;
	
	private static final String MOTO_TAKEN = I18N.message("moto.taken");
	private static final String FOLLOWUP = I18N.message("follow.up");

	private Button btnEdit;
	private Button btnWithdraw;
	
	private Label lblCreateDate;
	private Label lblCreateBy;
	private Label lblDate;
	private Label lblTime;
	private Label lblCurrentLocation;
	private Label lblProvince;
	private Label lblDistrict;
	private Label lblSubDistrict;
	private Label lblRemark;
	
	private AutoDateField dfMotoTakenDate;
	private AutoDateField dfFolloupDate;
	
	private OptionGroup optMotoTaken;
	private OptionGroup optFollowup;
	
	private TextField txtRemark;
	
	private Button btnSave;
	private ComboBox cbxTime;
	
	private LegalFormPanel legalFormPanel;
	
	private ContractFlag contractFlag;
	
	/**
	 * 
	 * @param legalFormPanel
	 */
	public LegalSubmitFormPanel(LegalFormPanel legalFormPanel) {
		this.legalFormPanel = legalFormPanel;
		setSizeFull();
		createForm();
	}
	
	/**
	 * create form
	 */
	public void createForm() {
		Label lblSeizedMotobike = ComponentFactory.getHtmlLabel("<b>" + I18N.message("seized.motorbike") + "</b>");
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(lblSeizedMotobike);
		
		Label lblCreateDateTitle = getLabel("create.date");
		Label lblCreateByTitle = getLabel("create.by");
		Label lblDateTitle = getLabel("date");
		Label lblTimeTitle = getLabel("time");
		Label lblCurrentLocationTitle = getLabel("current.location.of.motobike");
		Label lblProvinceTitle = getLabel("province");
		Label lblDistrictTitle = getLabel("district");
		Label lblSubDistrictTitle = getLabel("sub.district");
		Label lblRemarkTitle = getLabel("remark");
		Label lblResultTitle = getLabel("result");
		Label lblRemarksTitle = getLabel("remark");
		
		lblCreateDate = getLabelValue();
		lblCreateBy = getLabelValue();
		lblDate = getLabelValue();
		lblTime = getLabelValue();
		lblCurrentLocation = getLabelValue();
		lblProvince = getLabelValue();
		lblDistrict = getLabelValue();
		lblSubDistrict = getLabelValue();
		lblRemark = getLabelValue();
		
		btnWithdraw = new NativeButton(I18N.message("withdraw"), this);
		btnWithdraw.setStyleName("btn btn-success button-small");
		btnWithdraw.setWidth("70px");
		
		btnEdit = new NativeButton(I18N.message("edit"), this);
		btnEdit.setStyleName("btn btn-success button-small");
		btnEdit.setWidth("70px");
		
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
		
		optMotoTaken = new OptionGroup();
		optMotoTaken.addItems(MOTO_TAKEN);
		optMotoTaken.addValueChangeListener(this);
		
		optFollowup = new OptionGroup();
		optFollowup.addItem(FOLLOWUP);
		optFollowup.addValueChangeListener(this);
		
		dfMotoTakenDate = ComponentFactory.getAutoDateField();
		dfFolloupDate = ComponentFactory.getAutoDateField();
		
		cbxTime = ComponentLayoutFactory.getTimeComboBox();
		cbxTime.setWidth(65, Unit.PIXELS);
		txtRemark = ComponentFactory.getTextField();
		
		GridLayout gridLayout = new GridLayout(7, 1);
		
		int iCol = 0;
		gridLayout.addComponent(lblCreateDateTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblCreateDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblCreateByTitle, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblCreateBy, iCol++, 0);
		
		GridLayout gLayout = new GridLayout(23, 1);
		
		int iCols = 0;
		gLayout.addComponent(lblDateTitle, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblDate, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblTimeTitle, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblTime, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblCurrentLocationTitle, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblCurrentLocation, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblProvinceTitle, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblProvince, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblDistrictTitle, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblDistrict, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblSubDistrictTitle, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblSubDistrict, iCols++, 0);
		
		
		HorizontalLayout hLayout = new HorizontalLayout();

		hLayout.addComponent(lblRemarkTitle);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		hLayout.addComponent(lblRemark);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS));
		hLayout.addComponent(btnEdit);
		hLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS));
		hLayout.addComponent(btnWithdraw);
		
		hLayout.setComponentAlignment(lblRemark, Alignment.MIDDLE_CENTER);
		hLayout.setComponentAlignment(lblRemarkTitle, Alignment.MIDDLE_CENTER);
		
		GridLayout gLayouts = new GridLayout(16, 1);
		
		int iColumn = 0;
		gLayouts.addComponent(lblResultTitle, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(optMotoTaken, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(dfMotoTakenDate, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(cbxTime, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(6, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(optFollowup, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(dfFolloupDate, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(lblRemarksTitle, iColumn++, 0);
		gLayouts.addComponent(txtRemark, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(btnSave, iColumn++, 0);
		
		gLayouts.setComponentAlignment(lblResultTitle, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblRemarksTitle, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblRemarksTitle, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblRemarksTitle, Alignment.MIDDLE_CENTER);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSpacing(true);
		
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.addComponent(gridLayout);
		verticalLayout.addComponent(gLayout);
		verticalLayout.addComponent(hLayout);
		verticalLayout.addComponent(gLayouts);
		addComponent(verticalLayout);
	}
	
	/**
	 * reset
	 */
	public void reset() {
		dfFolloupDate.setValue(null);
		dfMotoTakenDate.setValue(null);
		cbxTime.setValue(null);
		optFollowup.setValue(null);
		optMotoTaken.setValue(null);
		txtRemark.setValue("");
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void showControl(boolean enabled) {
		cbxTime.setEnabled(enabled);
		txtRemark.setEnabled(enabled);
		dfFolloupDate.setEnabled(enabled);
		dfMotoTakenDate.setEnabled(enabled);
	}
	
	/**
	 * check moto taken and follow up value
	 */
	public void checkMotoTakenValues() {
		if (MOTO_TAKEN.equals(optMotoTaken.getValue())) {
			dfMotoTakenDate.setEnabled(true);
			cbxTime.setEnabled(true);
			dfFolloupDate.setEnabled(false);
			txtRemark.setEnabled(false);
		} else if (FOLLOWUP.equals(optFollowup.getValue())) {
			dfFolloupDate.setEnabled(true);
			txtRemark.setEnabled(true);
			dfMotoTakenDate.setEnabled(false);
			cbxTime.setEnabled(false);
			dfMotoTakenDate.setValue(null);
			cbxTime.setValue(null);
		}
	}
	
	/**
	 * 
	 */
	protected void saveResult() {
		if (MOTO_TAKEN.equals(optMotoTaken.getValue())) {
			this.contractFlag.setCompleted(true);
			Date date = dfMotoTakenDate.getValue();
			if (date != null) {
				date = DateUtils.getDateAtBeginningOfDay(date);
				if (cbxTime.getValue() != null) {
					long time = (long) cbxTime.getValue();
					date.setTime(date.getTime() + time);
				}
			}
			this.contractFlag.setActionDate(date);
		} else {
			this.contractFlag.setCompleted(false);
			this.contractFlag.setActionDate(dfFolloupDate.getValue());
			this.contractFlag.setResultRemark(txtRemark.getValue());
		}
		ENTITY_SRV.saveOrUpdate(contractFlag);
	}
	
	/**
	 * 
	 * @param contractFlag
	 */
	public void assignValues(ContractFlag contractFlag) {
		reset();
		legalFormPanel.reset();
		this.contractFlag = contractFlag;
		if (contractFlag != null) {
			if (contractFlag.getDate() != null) {
				lblDate.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getDate(), "dd/MM/yyyy")));
				lblTime.setValue(getDescription(getDefaultString(convertTime(contractFlag.getDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getDate()).getTime()))));
			}
			lblCreateDate.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getCreateDate(), "dd/MM/yyyy")));
			lblCreateBy.setValue(getDescription(contractFlag.getCreateUser()));
			lblRemark.setValue(getDescription(contractFlag.getComment() != null ? contractFlag.getComment() : ""));
			lblProvince.setValue(getDescription(getDescription(contractFlag.getProvince() != null ? contractFlag.getProvince().getDescLocale() : StringUtils.EMPTY)));
			lblDistrict.setValue(getDescription(contractFlag.getDistrict() != null ? contractFlag.getDistrict().getDescLocale() : StringUtils.EMPTY));
			lblSubDistrict.setValue(getDescription(contractFlag.getCommune() != null ? contractFlag.getCommune().getDescLocale() : StringUtils.EMPTY));
			lblCurrentLocation.setValue(getDescription(contractFlag.getLocantion()));
			if (contractFlag.isCompleted()) {
				optFollowup.select(null);
				optMotoTaken.select(MOTO_TAKEN);
				dfMotoTakenDate.setValue(contractFlag.getActionDate());
				if (contractFlag.getActionDate() != null) {
					cbxTime.setValue(contractFlag.getActionDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getActionDate()).getTime());
				}
			} else {
				optMotoTaken.select(null);
				optFollowup.select(FOLLOWUP);
				dfFolloupDate.setValue(contractFlag.getActionDate());
				txtRemark.setValue(contractFlag.getResultRemark());
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
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(getDefaultString(value));
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setSizeUndefined();
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
		if (event.getButton() == btnSave) {
			saveResult();
			ComponentLayoutFactory.displaySuccessfullyMsg();
			legalFormPanel.assignMotoTakenValues(contractFlag);
		} else if (event.getButton() == btnEdit) {
			legalFormPanel.assignContractFlagValues(contractFlag);
		} else if (event.getButton() == btnWithdraw) {
			legalFormPanel.deleteContractFlag(contractFlag);
		}
		
	}

	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(optMotoTaken)) {
			if (MOTO_TAKEN.equals(optMotoTaken.getValue())) {
				optFollowup.select(null);
			}
		} else if (event.getProperty().equals(optFollowup)) {
			if (FOLLOWUP.equals(optFollowup.getValue())) {
				optMotoTaken.select(null);
			}
		}
		checkMotoTakenValues();
	}

}
