package com.nokor.efinance.ra.ui.panel.blacklist;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.applicant.model.EApplicantCategory;
import com.nokor.efinance.core.common.reference.model.BlackListItem;
import com.nokor.efinance.core.common.reference.model.EBlackListReason;
import com.nokor.efinance.core.common.reference.model.EBlackListSource;
import com.nokor.efinance.core.widget.ERefDataListSelect;
import com.nokor.ersys.core.hr.model.eref.ECivility;
import com.nokor.ersys.core.hr.model.eref.EGender;
import com.nokor.ersys.core.hr.model.eref.EMaritalStatus;
import com.nokor.ersys.core.hr.model.eref.ENationality;
import com.nokor.ersys.core.hr.model.eref.ETypeIdNumber;
import com.nokor.frmk.helper.SeuksaServicesHelper;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


/**
 * 
 * @author uhout.cheng
 */
public class BlackListItemPopupWindow extends Window implements ClickListener, SeuksaServicesHelper {

	/** */
	private static final long serialVersionUID = -6907128167004952365L;
	
	private Logger logger = LoggerFactory.getLogger(BlackListItemPopupWindow.class);
	
	private final static String DATE_FORMAT = "dd.MM.yyyy hh:mm";
	
	private BlackListItem blackListItem;
	private Button btnSave;
	private Button btnReset;
	private Button btnCancel;
	protected List<String> errors;
	private VerticalLayout messagePanel;
	private ERefDataComboBox<EBlackListSource> cbxSource;
	private ERefDataListSelect<EBlackListReason> lstReason;
	private Upload btnUpload;
	private BlackListItemUploader blackListItemUploader;
	private Label lblUploadDesc;
	private BlackListItemTablePanel blackListItemTablePanel;

	/**
	 * 
	 * @param blackListItemTablePanel
	 */
	public BlackListItemPopupWindow(BlackListItemTablePanel blackListItemTablePanel) {
		this.blackListItemTablePanel = blackListItemTablePanel;
		setModal(true);
		setResizable(false);
	    setCaption(I18N.message("add.blacklist.item"));
	    setContent(createForm());
	}
	
	/**
	 * 
	 * @param caption
	 * @param resource
	 * @return
	 */
	private Button getNativeButton(String caption, Resource resource) {
		Button btn = new NativeButton(I18N.message(caption));
		btn.setIcon(resource);
		btn.addClickListener(this);
		return btn;
	}
	
	/**
	 * 
	 * @return
	 */
	private Component createForm() {
		btnSave = getNativeButton("save", FontAwesome.SAVE);
        btnCancel = getNativeButton("cancel", FontAwesome.TIMES);
        btnReset = getNativeButton("reset", FontAwesome.ERASER);
        
        errors = new ArrayList<String>();
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		cbxSource = new ERefDataComboBox<>(I18N.message("source"), EBlackListSource.values());
		cbxSource.setImmediate(true);
		cbxSource.setWidth(180, Unit.PIXELS);
		cbxSource.setRequired(true);
		
		lstReason = new ERefDataListSelect<>(I18N.message("reason"), EBlackListReason.values()); 
		lstReason.setRows(10);
		lstReason.setNullSelectionAllowed(false);
		lstReason.setMultiSelect(true);
		lstReason.setImmediate(true);
		lstReason.setWidth(180, Unit.PIXELS);
		lstReason.setRequired(true);
		
		cbxSource.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = -6041013472393135704L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				lstReason.clear();
				if (cbxSource.getSelectedEntity() != null) {
					List<EBlackListReason> blackListReasons = EBlackListReason.values(cbxSource.getSelectedEntity());
					lstReason.setEntities(blackListReasons);
				} else {
					lstReason.setEntities(EBlackListReason.values());
				}
			}
		});
		
		lblUploadDesc = new Label();
		
		blackListItemUploader = new BlackListItemUploader(this);
		btnUpload = new Upload();
		btnUpload.setButtonCaption(I18N.message("browse"));
		btnUpload.setImmediate(true);
		btnUpload.setReceiver(blackListItemUploader);
		
		final FormLayout frmLayout = new FormLayout();
		frmLayout.setMargin(true);
		frmLayout.setStyleName("myform-align-left");
		frmLayout.addComponent(cbxSource);
		frmLayout.addComponent(lstReason);
		
        NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnReset);
		navigationPanel.addButton(btnCancel);
		VerticalLayout mainVerLayout = new VerticalLayout();
		mainVerLayout.addComponent(navigationPanel);
		mainVerLayout.addComponent(messagePanel);
		mainVerLayout.addComponent(frmLayout);
		mainVerLayout.addComponent(getUploadLayout());
        return mainVerLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private CustomLayout getUploadLayout() {
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = "<table cellspacing=\"2\" cellpadding=\"2\" border=\"0\" style=\"margin-left:15px;\">";
		template += "<tr>";
		template += "<td align=\"left\" width=\"135\"><div location=\"lblFile\" class=\"inline-block\"></div>";
		template += "<div class=\"inline-block requiredfield\">&nbsp;*</div></td>";
		template += "<td><div location =\"btnUpload\"/></td>";
		template += "<td align=\"left\" width=\"120\"><div location=\"lblUploadDesc\"></td>";
		template += "</tr>";
		template += "<tr>";
		template += "<td></td>";
		template += "<td><div location =\"cbActive\"/></td>";
		template += "<td></td>";
		template += "</tr>";
		template += "</table>";
		
		cusLayout.addComponent(new Label(I18N.message("file")), "lblFile");
		cusLayout.addComponent(btnUpload, "btnUpload");
		cusLayout.addComponent(lblUploadDesc, "lblUploadDesc");
		cusLayout.setTemplateContents(template);
		return cusLayout;
	}
	
	/**
	 * 
	 * @param blst
	 * @return
	 */
	private BlackListItem getEntity(BlackListItem blst) {
		blackListItem = BlackListItem.createInstance();
		blackListItem.setTypeIdNumber(blst.getTypeIdNumber());
		blackListItem.setIdNumber(blst.getIdNumber());
		blackListItem.setStatusRecord(blst.getStatusRecord());
		blackListItem.setCivility(blst.getCivility());
		blackListItem.setFirstNameEn(blst.getFirstNameEn());
		blackListItem.setLastNameEn(blst.getLastNameEn());
		blackListItem.setMobilePerso(blst.getMobilePerso());
		blackListItem.setSource(blst.getSource());
		blackListItem.setReason(blst.getReason());
		blackListItem.setBirthDate(blst.getBirthDate());
		blackListItem.setIssuingIdNumberDate(blst.getIssuingIdNumberDate());
		blackListItem.setExpiringIdNumberDate(blst.getExpiringIdNumberDate());
		blackListItem.setNationality(blst.getNationality());
		blackListItem.setGender(blst.getGender());
		blackListItem.setMaritalStatus(blst.getMaritalStatus());
		blackListItem.setApplicantCategory(blst.getApplicantCategory());
		blackListItem.setDetails(blst.getDetails());
		blackListItem.setRemarks(blst.getRemarks());
		return blackListItem;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	private List<BlackListItem> getBlackListItems(File file) {
		String cvsSplitBy = "\\|";
		String header = "ID Type|ID Number|Status|Prefix|First Name|Last Name|Phone Number|Source|Reason|DOB|Issuing Date|"
				+ "Expiring Date|Nationality|Gender|Marital Status|Applicant Category|Details|Remarks";
		int length = header.split(cvsSplitBy).length;
		List<BlackListItem> blackListItems = new ArrayList<BlackListItem>();
		if (file != null) {
			try {
				List<String> lines = FileUtils.readLines(file);
				for (int i = 0; i < lines.size(); i++) {
					if (i > 0) {
						String[] blackListItem = lines.get(i).split(cvsSplitBy, -1);
						if (length == blackListItem.length) {
							BlackListItem blst = BlackListItem.createInstance();
							blst.setTypeIdNumber(StringUtils.isEmpty(blackListItem[0]) ? null : ETypeIdNumber.getById(Long.parseLong(blackListItem[0])));
							blst.setIdNumber(blackListItem[1]);
							blst.setStatusRecord(StringUtils.isEmpty(blackListItem[2]) ? null : EStatusRecord.getById(Long.parseLong(blackListItem[2])));
							blst.setCivility(StringUtils.isEmpty(blackListItem[3]) ? null : ECivility.getById(Long.parseLong(blackListItem[3])));
							blst.setFirstNameEn(blackListItem[4]);
							blst.setLastNameEn(blackListItem[5]);
							blst.setMobilePerso(blackListItem[6]);
							blst.setSource(StringUtils.isEmpty(blackListItem[7]) ? null : EBlackListSource.getById(Long.parseLong(blackListItem[7])));
							blst.setReason(StringUtils.isEmpty(blackListItem[8]) ? null : EBlackListReason.getById(Long.parseLong(blackListItem[8])));
							blst.setBirthDate(checkDate(blackListItem[9]) == null ? checkDate("10.10.2015 00:00") : checkDate(blackListItem[9]));
							blst.setIssuingIdNumberDate(checkDate(blackListItem[10]) == null ? checkDate("10.10.2015 00:00") : checkDate(blackListItem[10]));
							blst.setExpiringIdNumberDate(checkDate(blackListItem[11]) == null ? checkDate("10.10.2015 00:00") : checkDate(blackListItem[11]));
							blst.setNationality(StringUtils.isEmpty(blackListItem[12]) ? null : ENationality.getById(Long.parseLong(blackListItem[12])));
							blst.setGender(StringUtils.isEmpty(blackListItem[13]) ? null : EGender.getById(Long.parseLong(blackListItem[13])));
							blst.setMaritalStatus(StringUtils.isEmpty(blackListItem[14]) ? null : EMaritalStatus.getById(Long.parseLong(blackListItem[14])));
							blst.setApplicantCategory(StringUtils.isEmpty(blackListItem[15]) ? null : EApplicantCategory.getById(Long.parseLong(blackListItem[15])));
							blst.setDetails(blackListItem[16]);
							blst.setRemarks(blackListItem[17]);
							blackListItems.add(blst);
						} 
					}
				}
			} catch (Exception e) {
				String errMsg = "Exception";
				logger.error(errMsg, e);
				Notification.show(errMsg, e.getMessage(), Notification.Type.ERROR_MESSAGE);
			}
		}
		return blackListItems;
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSave)) {
			if (validate()) {
				List<BlackListItem> blackListItems = getBlackListItems(blackListItemUploader.getFile());
				List<Integer> lstInts = new ArrayList<Integer>();
				if (blackListItems != null && !blackListItems.isEmpty()) {
					int i = 1;
					for (BlackListItem blackList : blackListItems) {
						i++;
						if (cbxSource.getSelectedEntity().equals(blackList.getSource())) {
							for (EBlackListReason blackListReason : lstReason.getSelectedEntities()) {
								if (blackListReason.equals(blackList.getReason())) {
									logger.debug(">> save blacklist");
									ENTITY_SRV.create(getEntity(blackList));
									logger.debug("<< save blacklist");
								} else {
									lstInts.add(i);
								}
							}
						} else {
							lstInts.add(i);
						}
					}
				}
				blackListItemTablePanel.refresh();
				if (!lstInts.isEmpty()) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "400px", "160px", I18N.message("information"),
							MessageBox.Icon.INFO, getErrorIndex(lstInts), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
				close();
			} else {
				displayErrors();
			}
		} else if (event.getButton().equals(btnReset)) {
			reset();
		} else if (event.getButton().equals(btnCancel)) {
			close();
		} 
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	private String getErrorIndex(List<Integer> integers) {
		StringBuffer stringBuffer = new StringBuffer(); 
		if (!integers.isEmpty()) {
			stringBuffer.append("Row [");
			for (Integer integer : integers) {
				stringBuffer.append(integer);
				stringBuffer.append(",");
			}
			int lastIndex = stringBuffer.lastIndexOf(",");
			stringBuffer.replace(lastIndex, lastIndex + 1, "]");
			stringBuffer.append(" ");
			stringBuffer.append(I18N.message("source.reason.incorrect"));
		}
		return stringBuffer.toString();
	}
	
	/**
	 * Validate the black list form
	 * @return
	 */
	private boolean validate() {
		removeAllMessage();
		if (cbxSource.getSelectedEntity() == null) {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("source") }));
		}
		if (lstReason.getSelectedEntities().isEmpty()) {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("reason") }));
		}
		if (StringUtils.isNotEmpty(lblUploadDesc.getValue())) {
			if (!FileExtensionValidation.validateFileExtn(lblUploadDesc.getValue())) {
				errors.add(I18N.message("file.extension.incorrect"));
			}
		} else {
			errors.add(I18N.message("field.required.1", new String[] { I18N.message("file") }));
		}
		return errors.isEmpty();
	}
	
	/**
	 * 
	 */
	private void removeAllMessage() {
		messagePanel.setVisible(false);
		messagePanel.removeAllComponents();
		errors.clear();
	}
	
	/**
	 * Display Errors
	 */
	private void displayErrors() {
		messagePanel.removeAllComponents();
		if (!(errors.isEmpty())) {
			for (String error : errors) {
				Label messageLabel = new Label(error);
				messageLabel.addStyleName("error");
				messagePanel.addComponent(messageLabel);
			}
			messagePanel.setVisible(true);
		}
	}
	
	/**
	 * Reset controls
	 */
	private void reset() {
		removeAllMessage();
		cbxSource.setSelectedEntity(null);
	}
	
	/**
	 * 
	 * @param birthDate
	 * @return
	 */
	private Date checkDate(String birthDate) {
		return StringUtils.isEmpty(birthDate) ? null : DateUtils.getDate(birthDate, DATE_FORMAT);
	}
	
	/**
	 * @return the lblUploadDesc
	 */
	public Label getLblUploadDesc() {
		return lblUploadDesc;
	}
}
