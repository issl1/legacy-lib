package com.nokor.efinance.gui.ui.panel.collection.phone.contracthistory.legal.court;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.ContractFlag;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

/**
 * 
 * @author uhout.cheng
 */
public class LegalCourtResultPanel extends AbstractControlPanel implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = -8395301817231841560L;

	private Label lblCreatedDate;
	private Label lblCreatedUser;
	
	private Label lblDate;
	private Label lblTime;
	private Label lblCourtInCharge;
	private Label lblProvince;
	private Label lblDistrict;
	private Label lblSubDistrict;
	private Label lblStatus;
	private Label lblRemark;
	
	private Button btnEdit;
	private Button btnWithdraw;
	
	private LegalCourtDetailPanel caseDetailPanel;
	
	private ContractFlag contractFlag;
	
	/**
	 * 
	 * @param caseDetailPanel
	 */
	public LegalCourtResultPanel(LegalCourtDetailPanel caseDetailPanel) {
		this.caseDetailPanel = caseDetailPanel;
		lblCreatedDate = ComponentFactory.getHtmlLabel(null);
		lblCreatedUser = ComponentFactory.getHtmlLabel(null);
		
		lblDate = ComponentFactory.getHtmlLabel(null);
		lblTime = ComponentFactory.getHtmlLabel(null);
		
		lblProvince = ComponentFactory.getHtmlLabel(null);
		lblDistrict = ComponentFactory.getHtmlLabel(null);
		lblSubDistrict = ComponentFactory.getHtmlLabel(null);
		lblSubDistrict.setWidth(150, Unit.PIXELS);
		
		lblCourtInCharge = ComponentFactory.getHtmlLabel(null);
		lblRemark = ComponentFactory.getHtmlLabel(null);
		lblRemark.setWidth(200, Unit.PIXELS);
		
		lblStatus = ComponentFactory.getHtmlLabel(null);
		lblStatus.setWidth(150, Unit.PIXELS);
		
		btnEdit = ComponentLayoutFactory.getDefaultButton("edit", FontAwesome.EDIT, 70);
		btnEdit.addClickListener(this);
		btnWithdraw = ComponentLayoutFactory.getDefaultButton("withdraw", null, 70);
		btnWithdraw.addClickListener(this);
		
		Label lblDateCreatedTitle = getLabelCaption("date.created");
		Label lblUserCreatedTitle = getLabelCaption("user.created");
		
		Label lblDateTitle = getLabelCaption("date");
		Label lblTimeTitle = getLabelCaption("time");
		Label lblCourtInChargeTitle = getLabelCaption("court.in.charge");
		
		Label lblProvinceTitle = getLabelCaption("province");
		Label lblDistrictTitle = getLabelCaption("district");
		Label lblSubDistrictTitle = getLabelCaption("subdistrict");
		
		Label lblStatusTitle = getLabelCaption("status");
		Label lblRemarkTitle = getLabelCaption("remark");
		
		GridLayout gridLayout = new GridLayout(6, 4);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(lblDateCreatedTitle, iCol++, 0);
		gridLayout.addComponent(lblCreatedDate, iCol++, 0);
		gridLayout.addComponent(lblUserCreatedTitle, iCol++, 0);
		gridLayout.addComponent(lblCreatedUser, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblDateTitle, iCol++, 1);
		gridLayout.addComponent(lblDate, iCol++, 1);
		gridLayout.addComponent(lblTimeTitle, iCol++, 1);
		gridLayout.addComponent(lblTime, iCol++, 1);
		gridLayout.addComponent(lblCourtInChargeTitle, iCol++, 1);
		gridLayout.addComponent(lblCourtInCharge, iCol++, 1);
		
		iCol = 0;
		gridLayout.addComponent(lblProvinceTitle, iCol++, 2);
		gridLayout.addComponent(lblProvince, iCol++, 2);
		gridLayout.addComponent(lblDistrictTitle, iCol++, 2);
		gridLayout.addComponent(lblDistrict, iCol++, 2);
		gridLayout.addComponent(lblSubDistrictTitle, iCol++, 2);
		gridLayout.addComponent(lblSubDistrict, iCol++, 2);
		
		iCol = 0;
		gridLayout.addComponent(lblStatusTitle, iCol++, 3);
		gridLayout.addComponent(lblStatus, iCol++, 3);
		gridLayout.addComponent(lblRemarkTitle, iCol++, 3);
		gridLayout.addComponent(lblRemark, iCol++, 3);
		
		gridLayout.setComponentAlignment(lblDateCreatedTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblUserCreatedTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblDateTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblTimeTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblCourtInChargeTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblProvinceTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblDistrictTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblSubDistrictTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblStatusTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblRemarkTitle, Alignment.TOP_RIGHT);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		buttonLayout.addComponent(btnEdit);
		buttonLayout.addComponent(btnWithdraw);
		
		gridLayout.addComponent(buttonLayout, 5, 3);
		gridLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
		
		addComponent(gridLayout);
	}
	
	/**
	 * 
	 * @param contractFlag
	 */
	public void assignValues(ContractFlag contractFlag) {
		this.reset();
		this.contractFlag = contractFlag;
		if (contractFlag != null && contractFlag.getId() != null) {
			lblCreatedDate.setValue(getDescription(getDateFormat(contractFlag.getCreateDate())));
			lblCreatedUser.setValue(getDescription(contractFlag.getCreateUser()));
			lblDate.setValue(getDescription(getDateFormat(contractFlag.getDate())));
			if (contractFlag.getDate() != null) {
				lblTime.setValue(getDescription(getDefaultString(convertTime(contractFlag.getDate().getTime() - 
						DateUtils.getDateAtBeginningOfDay(contractFlag.getDate()).getTime()))));
			}
			lblCourtInCharge.setValue(getDescription(contractFlag.getCourtInCharge()));
			lblProvince.setValue(getDescription(contractFlag.getProvince() != null ? contractFlag.getProvince().getDescLocale() : StringUtils.EMPTY));
			lblDistrict.setValue(getDescription(contractFlag.getDistrict() != null ? contractFlag.getDistrict().getDescLocale() : StringUtils.EMPTY));
			lblSubDistrict.setValue(getDescription(contractFlag.getCommune() != null ? contractFlag.getCommune().getDescLocale() : StringUtils.EMPTY));
			Contract con = contractFlag.getContract();
			String status = StringUtils.EMPTY;
			if (con != null && con.getWkfSubStatus() != null) {
				status = con.getWkfSubStatus().getDescLocale();
			} 
			lblStatus.setValue(getDescription(status));
			lblRemark.setValue(getDescription(contractFlag.getComment()));
			caseDetailPanel.hideDetailLayout();
		} else {
			caseDetailPanel.displayDetailLayout(contractFlag);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		this.contractFlag = null;
		lblCreatedDate.setValue(StringUtils.EMPTY);
		lblCreatedUser.setValue(StringUtils.EMPTY);
		lblDate.setValue(StringUtils.EMPTY);
		lblTime.setValue(StringUtils.EMPTY);
		lblCourtInCharge.setValue(StringUtils.EMPTY);
		lblProvince.setValue(StringUtils.EMPTY);
		lblDistrict.setValue(StringUtils.EMPTY);
		lblSubDistrict.setValue(StringUtils.EMPTY);
		lblStatus.setValue(StringUtils.EMPTY);
		lblRemark.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnEdit)) {
			caseDetailPanel.assignValueToControls();
			caseDetailPanel.displayDetailLayout(this.contractFlag);
		} else if (event.getButton().equals(btnWithdraw)) {
			if (contractFlag != null && contractFlag.getId() != null) {
				ConfirmDialog.show(UI.getCurrent(), I18N.message("withdraw.msg.single"), new ConfirmDialog.Listener() {	
						
					/** */
					private static final long serialVersionUID = 6383730478577178464L;

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							try {
								logger.debug("[>> withdrawContractFlag]");
								
								CON_FLAG_SRV.withdrawLegalCase(contractFlag);
									
								logger.debug("[<< withdrawContractFlag]");
								dialog.close();
								ComponentLayoutFactory.displaySuccessMsg("withdraw.successfully");
								assignValues(null);
							} catch (Exception e) {
								ComponentLayoutFactory.displayErrorMsg(e.getMessage());
							}
				        }
					}
				});
			}
		}
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
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : StringUtils.EMPTY;
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
	 * @param caption
	 * @return
	 */
	private Label getLabelCaption(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + " : ");
	}
	
}
