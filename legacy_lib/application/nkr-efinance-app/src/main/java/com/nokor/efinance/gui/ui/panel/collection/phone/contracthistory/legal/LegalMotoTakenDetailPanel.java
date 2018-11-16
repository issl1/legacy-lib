package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class LegalMotoTakenDetailPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = -7853380261236835320L;
	private static final String MOTO_TAKEN = I18N.message("moto.taken");
	private static final String FOLLOWUP = I18N.message("follow.up");
	
	private Label lblCreateDate;
	private Label lblCreateBy;
	private Label lblDate;
	private Label lblTime;
	private Label lblCurrentLocation;
	private Label lblProvince;
	private Label lblSubDistrict;
	private Label lblDistrict;
	private Label lblRemark;
	private Label lblResult;
	private Label lblDateMotoTaken;
	private Label lblTimeFollowUp;
	private Label lblTimeMotoTaken;
	
	
	private OptionGroup optMotoTaken;
	private OptionGroup optFollowUp;
	
	private TextField txtRemark;
	
	public LegalMotoTakenDetailPanel() {
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
		Label lblDateMotoTakenTitle = getLabel("date");
		Label lblTimeMotoTakenTitle = getLabel("time");
		Label lblTimeFollowupTitle = getLabel("time");
		Label lblRemarkFollowupTitle = getLabel("remark");
		
		lblCreateDate = getLabelValue();
		lblCreateBy = getLabelValue();
		lblDate = getLabelValue();
		lblTime = getLabelValue();
		lblCurrentLocation = getLabelValue();
		lblProvince = getLabelValue();
		lblDistrict = getLabelValue();
		lblSubDistrict = getLabelValue();
		lblRemark = getLabelValue();
		lblResult = getLabelValue();
		lblDateMotoTaken = getLabelValue();
		lblTimeFollowUp = getLabelValue();
		lblTimeMotoTaken = getLabelValue();
		
		optFollowUp = new OptionGroup();
		optFollowUp.addItem(FOLLOWUP);
		
		optMotoTaken = new OptionGroup();
		optMotoTaken.addItem(MOTO_TAKEN);
		
		txtRemark = ComponentFactory.getTextField();
		
		GridLayout gridLayout = new GridLayout(5, 1);
		
		int iCol = 0;
		gridLayout.addComponent(lblCreateDateTitle, iCol++, 0);
		gridLayout.addComponent(lblCreateDate, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblCreateByTitle, iCol++, 0);
		gridLayout.addComponent(lblCreateBy, iCol++, 0);
		
		GridLayout gLayout = new GridLayout(18, 1);
		
		int iCols = 0;
		gLayout.addComponent(lblDateTitle, iCols++, 0);
		gLayout.addComponent(lblDate, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblTimeTitle, iCols++, 0);
		gLayout.addComponent(lblTime, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblCurrentLocationTitle, iCols++, 0);
		gLayout.addComponent(lblCurrentLocation, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblProvinceTitle, iCols++, 0);
		gLayout.addComponent(lblProvince, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblDistrictTitle, iCols++, 0);
		gLayout.addComponent(lblDistrict, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		gLayout.addComponent(lblSubDistrictTitle, iCols++, 0);
		gLayout.addComponent(lblSubDistrict, iCols++, 0);
		gLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		
		HorizontalLayout hLayout = new HorizontalLayout();

		hLayout.addComponent(lblRemarkTitle);
		hLayout.addComponent(lblRemark);
		
		hLayout.setComponentAlignment(lblRemarkTitle, Alignment.MIDDLE_CENTER);
		
		GridLayout gLayouts = new GridLayout(21, 1);
		
		int iColumn = 0;
		gLayouts.addComponent(lblResultTitle, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(optMotoTaken, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(lblDateMotoTakenTitle, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(lblDateMotoTaken, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(lblTimeMotoTakenTitle, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(lblTimeMotoTaken, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(lblTimeFollowupTitle, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(lblTimeFollowUp, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(optFollowUp, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(lblRemarkFollowupTitle, iColumn++, 0);
		gLayouts.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iColumn++, 0);
		gLayouts.addComponent(txtRemark, iColumn++, 0);
		
		gLayouts.setComponentAlignment(lblResultTitle, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblDateMotoTakenTitle, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblTimeMotoTakenTitle, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblTimeFollowupTitle, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblRemarkFollowupTitle, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblDateMotoTaken, Alignment.MIDDLE_CENTER);
		gLayouts.setComponentAlignment(lblTimeFollowUp, Alignment.MIDDLE_CENTER);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		
		layout.addComponent(horizontalLayout);
		layout.addComponent(gridLayout);
		layout.addComponent(gLayout);
		layout.addComponent(hLayout);
		layout.addComponent(gLayouts);
		
		addComponent(layout);
		
	}
	
	/**
	 * 
	 * @param contractFlag
	 */
	public void assignMotoTakenValue(ContractFlag contractFlag) {
		reset();
		if (contractFlag.getDate() != null) {
			lblDate.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getDate(), "dd/MM/yyyy")));
			lblTime.setValue(getDescription(getDefaultString(convertTime(contractFlag.getDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getDate()).getTime()))));
		}
		if (contractFlag.getDate() != null && contractFlag.getActionDate() != null) {
			lblDateMotoTaken.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getActionDate(), "dd/MM/yyyy")));
			lblTimeFollowUp.setValue(getDescription(getDefaultString(convertTime(contractFlag.getActionDate().getTime() - DateUtils.getDateAtBeginningOfDay(contractFlag.getDate()).getTime()))));
		}
		lblCreateDate.setValue(getDescription(DateUtils.getDateLabel(contractFlag.getCreateDate(), "dd/MM/yyyy")));
		lblCreateBy.setValue(getDescription(contractFlag.getCreateUser()));
		lblRemark.setValue(getDescription(contractFlag.getComment() != null ? contractFlag.getComment() : ""));
		lblProvince.setValue(getDescription(getDescription(contractFlag.getProvince() != null ? contractFlag.getProvince().getDescLocale() : StringUtils.EMPTY)));
		lblDistrict.setValue(getDescription(contractFlag.getDistrict() != null ? contractFlag.getDistrict().getDescLocale() : StringUtils.EMPTY));
		lblSubDistrict.setValue(getDescription(contractFlag.getCommune() != null ? contractFlag.getCommune().getDescLocale() : StringUtils.EMPTY));
		lblCurrentLocation.setValue(getDescription(contractFlag.getLocantion()));
		setEnabledControl(false);
		if (contractFlag.isCompleted()) {
			optMotoTaken.select(MOTO_TAKEN);
			lblDateMotoTaken.setValue(getDescription(getDateFormat(contractFlag.getActionDate())));
		} else {
			optFollowUp.select(FOLLOWUP);
			lblRemark.setValue(getDescription(contractFlag.getResultRemark()));
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : "";
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		optFollowUp.select(null);
		optMotoTaken.select(null);
		lblDate.setValue(StringUtils.EMPTY);
		lblTime.setValue(StringUtils.EMPTY);
		lblDateMotoTaken.setValue(StringUtils.EMPTY);
		lblTimeFollowUp.setValue(StringUtils.EMPTY);
		lblCreateDate.setValue(StringUtils.EMPTY);
		lblCreateBy.setValue(StringUtils.EMPTY);
		lblRemark.setValue(StringUtils.EMPTY);
		lblProvince.setValue(StringUtils.EMPTY);
		lblDistrict.setValue(StringUtils.EMPTY);
		lblSubDistrict.setValue(StringUtils.EMPTY);
		lblCurrentLocation.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @param enabled
	 */
	private void setEnabledControl(boolean enabled) {
		optFollowUp.setEnabled(enabled);
		optMotoTaken.setEnabled(enabled);
		lblTimeFollowUp.setEnabled(enabled);
		lblTimeMotoTaken.setEnabled(enabled);
		lblRemark.setEnabled(enabled);
		txtRemark.setEnabled(enabled);
		lblResult.setEnabled(enabled);
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

}
