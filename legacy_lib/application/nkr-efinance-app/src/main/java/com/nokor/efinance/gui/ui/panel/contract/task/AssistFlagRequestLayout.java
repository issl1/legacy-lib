package com.nokor.efinance.gui.ui.panel.contract.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAssist;
import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public class AssistFlagRequestLayout extends AbstractControlPanel implements ClickListener, FinServicesHelper {
	
	/** */
	private static final long serialVersionUID = 253373843449471802L;
	
	private GridLayout gridLayout;
	
	private ERefDataComboBox<EColType> cbxColTypeAssist;
	private Button btnAssistRequest;
	private Button btnAssistValidate;
	private Button btnAssistReject;
	
	private ERefDataComboBox<EColType> cbxColTypeFlag;
	private Button btnFlagRequest;
	private Button btnFlagValidate;
	private Button btnFlagReject;
	
	private Label lblDateCaption;
	private Label lblStatusCaption;
	
	private Label lblAssistDate;
	private Label lblAssistValue;
	
	private Label lblFlagDate;
	private Label lblFlagValue;
	
	private Button btnWithdrawAssist;
	private Button btnWithdrawFlag;
	
	private Label lblAssistanceTitle;
	private Label lblFlagTitle;
	
	private HorizontalLayout assistReqReasonLayout;
	private Label lblAssistReqReason;
	private TextArea txtAssistReqReason;
	private Button btnEditAssistReason;
	
	private HorizontalLayout flagReqReasonLayout;
	private Label lblFlagReqReason;
	private TextArea txtFlagReqReason;
	private Button btnEditFlagReason;
	
	private Contract contract;
	private CollectionFlag colFlag;
	private CollectionAssist colAssist;
	
	/**
	 * 
	 */
	public AssistFlagRequestLayout() {
		init();
		lblAssistanceTitle = ComponentFactory.getHtmlLabel("<h4 style=\"margin: 0\">" + I18N.message("assistance") + " : " + "</h4>");
		lblAssistanceTitle.setWidth(80, Unit.PIXELS);
		lblFlagTitle = ComponentFactory.getHtmlLabel("<h4 style=\"margin: 0\">" + I18N.message("flag") + " : " + "</h4>");
		lblFlagTitle.setWidth(80, Unit.PIXELS);
		
		lblAssistReqReason = ComponentFactory.getLabel();
		lblFlagReqReason = ComponentFactory.getLabel();
		
		txtAssistReqReason = ComponentFactory.getTextArea("reason", true, 250, 60);
		txtFlagReqReason = ComponentFactory.getTextArea("reason", true, 250, 60);
		
		btnEditAssistReason = ComponentLayoutFactory.getButtonIcon(FontAwesome.EDIT);
		btnEditAssistReason.addClickListener(this);
		btnEditFlagReason = ComponentLayoutFactory.getButtonIcon(FontAwesome.EDIT);
		btnEditFlagReason.addClickListener(this);
		
		assistReqReasonLayout = getReasonLayout(lblAssistReqReason, txtAssistReqReason, btnEditAssistReason);
		
		flagReqReasonLayout = getReasonLayout(lblFlagReqReason, txtFlagReqReason, btnEditFlagReason);
		
		gridLayout = ComponentLayoutFactory.getGridLayout(new MarginInfo(false, false, false, false), 4, 4);
		gridLayout.addComponent(lblAssistanceTitle, 0, 0);
		gridLayout.addComponent(lblFlagTitle, 0, 2);
		
		addComponent(gridLayout);
	}
	
	private void init() {
		cbxColTypeAssist = new ERefDataComboBox<>(getColTypeValues());
		cbxColTypeFlag = new ERefDataComboBox<>(getColTypeValues());
		cbxColTypeAssist.setEnabled(!ProfileUtil.isColFieldSupervisor());
		cbxColTypeFlag.setEnabled(!ProfileUtil.isColFieldSupervisor());
		if (ProfileUtil.isColPhoneLeader() || ProfileUtil.isColFieldLeader() || ProfileUtil.isColInsideRepoLeader()) {
			cbxColTypeAssist.setNullSelectionAllowed(false);
			cbxColTypeFlag.setNullSelectionAllowed(false);
		}
		if (ProfileUtil.isColPhoneLeader()) {
			cbxColTypeAssist.setSelectedEntity(EColType.FIELD);
			cbxColTypeFlag.setSelectedEntity(EColType.FIELD);
		} else if (ProfileUtil.isColFieldLeader()) {
			cbxColTypeAssist.setSelectedEntity(EColType.INSIDE_REPO);
			cbxColTypeFlag.setSelectedEntity(EColType.INSIDE_REPO);
		} else if (ProfileUtil.isColInsideRepoLeader()) {
			cbxColTypeAssist.setSelectedEntity(EColType.OA);
			cbxColTypeFlag.setSelectedEntity(EColType.OA);
		}
		
		btnAssistRequest = ComponentLayoutFactory.getDefaultButton("request", null, 60);		
		btnAssistRequest.addClickListener(this);
		btnAssistRequest.setVisible(ProfileUtil.isCollectionStaff());
		
		btnAssistValidate = ComponentLayoutFactory.getDefaultButton("validate", null, 60);		
		btnAssistValidate.addClickListener(this);
		
		btnAssistReject = ComponentLayoutFactory.getDefaultButton("reject", null, 60);		
		btnAssistReject.addClickListener(this);
		
		btnFlagRequest = ComponentLayoutFactory.getDefaultButton("request", null, 60);
		btnFlagRequest.addClickListener(this);
		btnFlagRequest.setVisible(ProfileUtil.isCollectionStaff());
		
		btnFlagValidate = ComponentLayoutFactory.getDefaultButton("validate", null, 60);
		btnFlagValidate.addClickListener(this);
		
		btnFlagReject = ComponentLayoutFactory.getDefaultButton("reject", null, 60);
		btnFlagReject.addClickListener(this);
		
		btnWithdrawAssist = ComponentLayoutFactory.getDefaultButton("withdraw", null, 70);
		btnWithdrawAssist.addClickListener(this);
		
		btnWithdrawFlag = ComponentLayoutFactory.getDefaultButton("withdraw", null, 70);
		btnWithdrawFlag.addClickListener(this);
		
		lblAssistDate = getLabelValue();
		lblAssistValue = getLabelValue();
		
		lblFlagDate = getLabelValue();
		lblFlagValue = getLabelValue();
	}
	
	/**
	 * 
	 * @param lblReason
	 * @param txtReason
	 * @param btnEdit
	 * @return
	 */
	private HorizontalLayout getReasonLayout(Label lblReason, TextArea txtReason, Button btnEdit) {
		lblReason.setWidth(300, Unit.PIXELS);
		lblReason.setCaption("<b>" + I18N.message("reason") + " : " + "</b>");
		lblReason.setCaptionAsHtml(true);
		
		txtReason.setVisible(false);
		
		HorizontalLayout layout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		layout.addComponent(btnEdit);
		layout.addComponent(lblReason);
		layout.addComponent(txtReason);
		layout.setComponentAlignment(btnEdit, Alignment.MIDDLE_LEFT);
		return layout;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		this.contract = contract;
		Collection collection = null;
		if (contract != null) {
			collection = COL_SRV.getCollection(contract);
		}
		if (collection != null) {
			colFlag = collection.getLastCollectionFlag();
			colAssist = collection.getLastCollectionAssist();
			
			HorizontalLayout assistComponent = new HorizontalLayout();	
			assistComponent.setSpacing(true);
			
			VerticalLayout assistVerLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, true), true);
			assistVerLayout.addComponent(assistComponent);
			
			HorizontalLayout flagComponent = new HorizontalLayout();
			flagComponent.setSpacing(true);
			
			VerticalLayout flagVerLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, true), true);
			flagVerLayout.addComponent(flagComponent);
			
			if (colAssist != null && colAssist.getRequestStatus() != null) {
				
				if (ERequestStatus.PENDING.equals(colAssist.getRequestStatus())) {
					lblAssistDate.setValue(getDescription(getDateFormat(colAssist.getRequestedDate())));
				} else if (ERequestStatus.REJECT.equals(colAssist.getRequestStatus())) {
					lblAssistDate.setValue(getDescription(getDateFormat(colAssist.getRejectedDate())));
				} else if (ERequestStatus.APPROVE.equals(colAssist.getRequestStatus())) {
					lblAssistDate.setValue(getDescription(getDateFormat(colAssist.getApprovedDate())));
				}
				
				lblAssistValue.setValue(getDescription(colAssist.getRequestStatus().getDescLocale()));
				
				lblDateCaption = ComponentFactory.getLabel(I18N.message("date") + " : ");
				lblStatusCaption = ComponentFactory.getLabel(I18N.message("status") + " : ");
				
				lblAssistReqReason.setValue(getDefaultString(colAssist.getReason()));
				assistReqReasonLayout.setVisible(true);
				
				assistComponent.addComponent(lblDateCaption);
				assistComponent.addComponent(lblAssistDate);
				assistComponent.addComponent(lblStatusCaption);
				assistComponent.addComponent(lblAssistValue);
				if (ProfileUtil.isCollectionStaff() && ERequestStatus.REJECT.equals(colAssist.getRequestStatus())) {
					assistComponent.addComponent(btnAssistRequest);
				}
				assistComponent.addComponent(btnWithdrawAssist);
				
				assistComponent.setComponentAlignment(lblDateCaption, Alignment.MIDDLE_LEFT);
				assistComponent.setComponentAlignment(lblStatusCaption, Alignment.MIDDLE_LEFT);
				assistComponent.setComponentAlignment(lblAssistDate, Alignment.MIDDLE_LEFT);
				assistComponent.setComponentAlignment(lblAssistValue, Alignment.MIDDLE_LEFT);
				
				lblAssistReqReason.setValue(getDefaultString(colAssist.getReason()));
				assistReqReasonLayout.setVisible(true);
				assistVerLayout.addComponent(assistReqReasonLayout);
				
				if (colAssist.getRequestStatus().equals(ERequestStatus.PENDING)) {
					btnWithdrawAssist.setVisible(true);
				} else {
					btnWithdrawAssist.setVisible(false);
				}
				if (colAssist.getRequestStatus().equals(ERequestStatus.PENDING) ||
						(colAssist.getRequestStatus().equals(ERequestStatus.APPROVE) && ProfileUtil.isColFieldSupervisor())) {
					if (!ProfileUtil.isColPhoneStaff() && !ProfileUtil.isColFieldStaff()) {
						assistComponent.addComponent(btnAssistReject);
						assistComponent.addComponent(cbxColTypeAssist);
						assistComponent.addComponent(btnAssistValidate);
					}
				}				
			} else {
				assistReqReasonLayout.setVisible(false);
				assistComponent.addComponent(btnAssistRequest);
			}
			
			if (colFlag != null && colFlag.getRequestStatus() != null) {
				
				if (ERequestStatus.PENDING.equals(colFlag.getRequestStatus())) {
					lblFlagDate.setValue(getDescription(getDateFormat(colFlag.getRequestedDate())));
				} else if (ERequestStatus.REJECT.equals(colFlag.getRequestStatus())) {
					lblFlagDate.setValue(getDescription(getDateFormat(colFlag.getRejectedDate())));
				} else if (ERequestStatus.APPROVE.equals(colFlag.getRequestStatus())) {
					lblFlagDate.setValue(getDescription(getDateFormat(colFlag.getApprovedDate())));
				}
				
				lblDateCaption = ComponentFactory.getLabel(I18N.message("date") + " : ");
				lblStatusCaption = ComponentFactory.getLabel(I18N.message("status") + " : ");
				
				lblFlagValue.setValue(getDescription(colFlag.getRequestStatus().getDescLocale()));
				flagComponent.addComponent(lblDateCaption);
				flagComponent.addComponent(lblFlagDate);
				flagComponent.addComponent(lblStatusCaption);
				flagComponent.addComponent(lblFlagValue);
				if (ProfileUtil.isCollectionStaff() && ERequestStatus.REJECT.equals(colFlag.getRequestStatus())) {
					flagComponent.addComponent(btnFlagRequest);
				}
				flagComponent.addComponent(btnWithdrawFlag);
				
				flagComponent.setComponentAlignment(lblDateCaption, Alignment.MIDDLE_LEFT);
				flagComponent.setComponentAlignment(lblStatusCaption, Alignment.MIDDLE_LEFT);
				flagComponent.setComponentAlignment(lblFlagDate, Alignment.MIDDLE_LEFT);
				flagComponent.setComponentAlignment(lblFlagValue, Alignment.MIDDLE_LEFT);
				
				lblFlagReqReason.setValue(getDefaultString(colFlag.getReason()));
				flagReqReasonLayout.setVisible(true);
				flagVerLayout.addComponent(flagReqReasonLayout);
				
				if (colFlag.getRequestStatus().equals(ERequestStatus.PENDING)) {
					btnWithdrawFlag.setVisible(true);
				} else {
					btnWithdrawFlag.setVisible(false);
				}
				if (colFlag.getRequestStatus().equals(ERequestStatus.PENDING) ||
						(colFlag.getRequestStatus().equals(ERequestStatus.APPROVE) && ProfileUtil.isColFieldSupervisor())) {
					if (!ProfileUtil.isColPhoneStaff() && !ProfileUtil.isColFieldStaff()) {
						flagComponent.addComponent(btnFlagReject);
						flagComponent.addComponent(cbxColTypeFlag);
						flagComponent.addComponent(btnFlagValidate);
					}
				}	
			} else {
				flagReqReasonLayout.setVisible(false);
				flagComponent.addComponent(btnFlagRequest);
			}
			
			gridLayout.removeComponent(0, 1);
			gridLayout.addComponent(assistVerLayout, 0, 1);
			gridLayout.removeComponent(0, 3);
			gridLayout.addComponent(flagVerLayout, 0, 3);
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		this.colFlag = null;
		this.colAssist = null;
		lblAssistReqReason.setValue(StringUtils.EMPTY);
		lblFlagReqReason.setValue(StringUtils.EMPTY);
		lblFlagDate.setValue(StringUtils.EMPTY);
		lblFlagValue.setValue(StringUtils.EMPTY);
		lblAssistDate.setValue(StringUtils.EMPTY);
		lblAssistValue.setValue(StringUtils.EMPTY);
		txtAssistReqReason.setValue(StringUtils.EMPTY);
		txtFlagReqReason.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		List<Contract> contracts = null;
		if (event.getButton() == btnAssistRequest) {
			contracts = new ArrayList<Contract>();
			contracts.add(contract);
			CollectionRequestPopupPanel.show(contracts, "assist", new CollectionRequestPopupPanel.Listener() {
				
				/** */
				private static final long serialVersionUID = -6795110859545614898L;

				/**
				 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
				 */
				@Override
				public void onClose(CollectionRequestPopupPanel dialog) {
					assignValues(contract);
				}
			});
		} else if (event.getButton() == btnAssistReject) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.reject.assist"),
					new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = 8265180198439095718L;

				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						COL_SRV.rejectAssistRequest(contract.getId()); 
						COL_SRV.refresh(contract.getCollection());
						assignValues(contract);
					} 
				}
			});
		} else if (event.getButton() == btnAssistValidate) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.validate.assist"),
					new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -278180626559281008L;

				/**
				 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						if (ProfileUtil.isColLeader()) {
							List<Long> conIds = new ArrayList<Long>();
							conIds.add(contract.getId());
							COL_SRV.approveAssistRequest(conIds, cbxColTypeAssist.getSelectedEntity());	
						} else if (ProfileUtil.isColFieldSupervisor() 
								|| ProfileUtil.isColInsideRepoSupervisor()
								|| ProfileUtil.isColOASupervisor()) {
							COL_SRV.validateAssistFlagContract(contract);
						}
						COL_SRV.refresh(contract.getCollection());
						ComponentLayoutFactory.displaySuccessMsg("validate.assist.success");
						assignValues(contract);
					} 
				}
			});
		} else if (event.getButton() == btnFlagRequest) {
			contracts = new ArrayList<Contract>();
			contracts.add(contract);
			CollectionRequestPopupPanel.show(contracts, "flag", new CollectionRequestPopupPanel.Listener() {
			
				/** */
				private static final long serialVersionUID = 1437908640663560075L;

				/**
				 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
				 */
				@Override
				public void onClose(CollectionRequestPopupPanel dialog) {
					assignValues(contract);
				}
			});
		} else if (event.getButton() == btnFlagReject) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.reject.flag"),
					new ConfirmDialog.Listener() {
						
				/** */
				private static final long serialVersionUID = -5801877731870628204L;
				
				/**
				 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						COL_SRV.rejectFlagRequest(contract.getId()); 
						assignValues(contract);
					} 
				}
			});
		} else if (event.getButton() == btnFlagValidate) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.validate.flag"),
					new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = -354135741151783758L;

				/**
				 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						if (ProfileUtil.isColPhoneLeader()) {
							List<Long> conIds = new ArrayList<Long>();
							conIds.add(contract.getId());
							COL_SRV.approveFlagRequest(conIds, cbxColTypeFlag.getSelectedEntity());
						} else if (ProfileUtil.isColFieldSupervisor() 
								|| ProfileUtil.isColInsideRepoSupervisor()
								|| ProfileUtil.isColOASupervisor()) {
							COL_SRV.validateAssistFlagContract(contract);
						}
						ComponentLayoutFactory.displaySuccessMsg("validate.flag.success");
						assignValues(contract);
					} 
				}
			});
		} else if (event.getButton() == btnWithdrawAssist) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.cancel.assist"),
					new ConfirmDialog.Listener() {
			
				/** */
				private static final long serialVersionUID = -5284432411196689689L;
				
				/**
				 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						Collection collection = contract.getCollection();
						CollectionAssist collectionAssist = collection.getLastCollectionAssist();
						if (collectionAssist != null) {
							collection.setLastCollectionAssist(null);
							ENTITY_SRV.saveOrUpdate(collection);
							ENTITY_SRV.delete(collectionAssist);
							displaySuccessfullyMsg();
							assignValues(contract);
						}
					} 
				}
			});
		} else if (event.getButton() == btnWithdrawFlag) {
			ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.cancel.flag"),
					new ConfirmDialog.Listener() {
				/** */
				private static final long serialVersionUID = 1451247348515592021L;
				/**
				 * @see com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.collection.phone.request.popup.CollectionRequestPopupPanel)
				 */
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						Collection collection = contract.getCollection();
						CollectionFlag collectionFlag = collection.getLastCollectionFlag();
						if (collectionFlag != null) {
							collection.setLastCollectionFlag(null);
							ENTITY_SRV.saveOrUpdate(collection);
							ENTITY_SRV.delete(collectionFlag);
							displaySuccessfullyMsg();
							assignValues(contract);
						}
					} 
				}
			});
		} else if (event.getButton() == btnEditAssistReason) {
			if (!txtAssistReqReason.isVisible()) {
				lblAssistReqReason.setVisible(false);
				txtAssistReqReason.setVisible(true);
				txtAssistReqReason.setValue(lblAssistReqReason.getValue());
				btnEditAssistReason.setIcon(FontAwesome.SAVE);
			} else {
				if (colAssist != null) {
					colAssist.setReason(txtAssistReqReason.getValue());
					try {
						if (StringUtils.isNotEmpty(txtAssistReqReason.getValue())) {
							COL_SRV.update(colAssist);
							ComponentLayoutFactory.displaySuccessfullyMsg();
							lblAssistReqReason.setVisible(true);
							txtAssistReqReason.setVisible(false);
							lblAssistReqReason.setValue(getDefaultString(colAssist.getReason()));
							btnEditAssistReason.setIcon(FontAwesome.EDIT);
						} else {
							txtAssistReqReason.setRequiredError(I18N.message("field.required.1", new String[] { I18N.message("reason") }));
							txtAssistReqReason.focus();
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
						ComponentLayoutFactory.displayErrorMsg("msg.error.technical");
					}
				}
			}
		} else if (event.getButton() == btnEditFlagReason) {
			if (!txtFlagReqReason.isVisible()) {
				lblFlagReqReason.setVisible(false);
				txtFlagReqReason.setVisible(true);
				txtFlagReqReason.setValue(lblFlagReqReason.getValue());
				btnEditFlagReason.setIcon(FontAwesome.SAVE);
			} else {
				if (colFlag != null) {
					colFlag.setReason(txtFlagReqReason.getValue());
					try {
						if (StringUtils.isNotEmpty(txtFlagReqReason.getValue())) {
							COL_SRV.update(colFlag);
							ComponentLayoutFactory.displaySuccessfullyMsg();
							lblFlagReqReason.setVisible(true);
							txtFlagReqReason.setVisible(false);
							lblFlagReqReason.setValue(getDefaultString(colFlag.getReason()));
							btnEditFlagReason.setIcon(FontAwesome.EDIT);
						} else {
							txtFlagReqReason.setRequiredError(I18N.message("field.required.1", new String[] { I18N.message("reason") }));
							txtFlagReqReason.focus();
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
						ComponentLayoutFactory.displayErrorMsg("msg.error.technical");
					}
				}
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
		stringBuffer.append(value == null ? "" : value);
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
	 * 
	 * @return
	 */
	private List<EColType> getColTypeValues() {
		List<EColType> values = new ArrayList<>();
		if (ProfileUtil.isColPhoneLeader()) {
			values.add(EColType.FIELD);
			values.add(EColType.INSIDE_REPO);
			values.add(EColType.OA);
		} else if (ProfileUtil.isColFieldLeader()) {
			values.add(EColType.INSIDE_REPO);
			values.add(EColType.OA);
		} else if (ProfileUtil.isColInsideRepoLeader()) {
			values.add(EColType.OA);
		}
		return values;
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
	 * 
	 */
	public void displaySuccessfullyMsg() {
		ComponentLayoutFactory.displaySuccessfullyMsg();
	}
}
