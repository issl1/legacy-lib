package com.nokor.efinance.gui.ui.panel.collection;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.CollectionAssist;
import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.gui.ui.panel.collection.phone.filter.CollectionContractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;


/**
 * 
 * @author uhout.cheng
 */
public class MultiApproveAssistFlagLayout extends HorizontalLayout implements ClickListener, FinServicesHelper {

	/** */
	private static final long serialVersionUID = 3396897839880163445L;
	
	private ERefDataComboBox<EColType> cbxColTypeAssist;
	private ERefDataComboBox<EColType> cbxColTypeFlag;
	private Button btnApproveAssist;
	private Button btnApproveFlag;
	
	private CollectionContractTablePanel deleget;
	
	/**
	 * 
	 * @param deleget
	 * @param isFlagm
	 * @param btnPrint
	 */
	public MultiApproveAssistFlagLayout(CollectionContractTablePanel deleget, Boolean isFlag, Button btnPrint) {
		this.deleget = deleget;
		btnApproveAssist = ComponentLayoutFactory.getDefaultButton("approve", null, 60);
		btnApproveAssist.addClickListener(this);
		btnApproveFlag = ComponentLayoutFactory.getDefaultButton("approve", null, 60);
		btnApproveFlag.addClickListener(this);
		
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
		
		Label lblAssistance = ComponentFactory.getHtmlLabel("<h4 style=\"margin: 0\">" + I18N.message("assistance") + " : " + "</h4>");
		Label lblFlag = ComponentFactory.getHtmlLabel("<h4 style=\"margin: 0\">" + I18N.message("flag") + " : " + "</h4>");
		
		setMargin(new MarginInfo(false, false, true, false));
		setSpacing(true);
		setVisible(isFlag != null && (ProfileUtil.isColLeader() 
				|| ProfileUtil.isColFieldSupervisor()
				|| ProfileUtil.isColInsideRepoSupervisor()
				|| ProfileUtil.isColOASupervisor()));
		
		if (isFlag != null) {
			if (isFlag) {
				addComponent(lblFlag);
				addComponent(cbxColTypeFlag);
				addComponent(btnApproveFlag);
				setComponentAlignment(lblFlag, Alignment.MIDDLE_LEFT);
			} else {
				addComponent(lblAssistance);
				addComponent(cbxColTypeAssist);
				addComponent(btnApproveAssist);
				setComponentAlignment(lblAssistance, Alignment.MIDDLE_LEFT);
			}
			addComponent(btnPrint);
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		List<Long> selectedIds = deleget.getSelectedIds();
		if (event.getButton().equals(btnApproveAssist) || event.getButton().equals(btnApproveFlag)) {
			List<Contract> contracts = new ArrayList<Contract>();
			if (!selectedIds.isEmpty()) {
				for (int i = 0; i < selectedIds.size(); i++) {
					contracts.add(CONT_SRV.getById(Contract.class, selectedIds.get(i)));
				}	
			}
			if (event.getButton().equals(btnApproveFlag)) {
				if (!contracts.isEmpty()) {	
					List<Long> contFlagIds = new ArrayList<Long>();
					for (Contract cont : contracts) {
						CollectionFlag flag = deleget.getLastColFlag(cont, new ERequestStatus[] { ERequestStatus.PENDING });
						if (flag != null) {
							contFlagIds.add(cont.getId());
						}
					}
					if (!contFlagIds.isEmpty() && contFlagIds.size() == contracts.size()) {
						ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.validate.flag"), 
								new ConfirmDialog.Listener() {
							
							/** */
							private static final long serialVersionUID = -3384523273845307934L;

							/**
							 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
							 */
							@Override
							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									if (ProfileUtil.isColLeader()) {
										COL_SRV.approveFlagRequest(contFlagIds, cbxColTypeFlag.getSelectedEntity());
									} else if (ProfileUtil.isColFieldSupervisor() 
											|| ProfileUtil.isColInsideRepoSupervisor()
											|| ProfileUtil.isColOASupervisor()) {
										for (int i = 0; i < contFlagIds.size(); i++) {
											COL_SRV.validateAssistFlagContract(CONT_SRV.getById(Contract.class, contFlagIds.get(i)));
										}
									}
									ComponentLayoutFactory.displaySuccessMsg("validate.flag.success");
									deleget.refresh(deleget.getRestriction());
									selectedIds.clear();
								} 
							}
						});
					} else {
						Notification notification = ComponentLayoutFactory.getNotification(Type.WARNING_MESSAGE);
						notification.setDescription(I18N.message("msg.can.approve.flag"));
					}
				}
			} else if (event.getButton().equals(btnApproveAssist)) {
				if (!contracts.isEmpty()) {
					List<Long> contAssistIds = new ArrayList<Long>();
					for (Contract cont : contracts) {
						CollectionAssist assist = deleget.getLastColAssist(cont, new ERequestStatus[] { ERequestStatus.PENDING });
						if (assist != null) {
							contAssistIds.add(cont.getId());
						}
					}
					if (!contAssistIds.isEmpty() && contAssistIds.size() == contracts.size()) {
						ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.validate.assist"), 
								new ConfirmDialog.Listener() {
							
							/** */
							private static final long serialVersionUID = -1033883067473692329L;

							/**
							 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
							 */
							@Override
							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									if (ProfileUtil.isColLeader()) {
										COL_SRV.approveAssistRequest(contAssistIds, cbxColTypeFlag.getSelectedEntity());
									} else if (ProfileUtil.isColFieldSupervisor() 
												|| ProfileUtil.isColInsideRepoSupervisor()
												|| ProfileUtil.isColOASupervisor()) {
										for (int i = 0; i < contAssistIds.size(); i++) {
											COL_SRV.validateAssistFlagContract(CONT_SRV.getById(Contract.class, contAssistIds.get(i)));
										}
									}
									ComponentLayoutFactory.displaySuccessMsg("validate.assist.success");
									deleget.refresh(deleget.getRestriction());
									selectedIds.clear();
								} 
							}
						});
					} else {
						Notification notification = ComponentLayoutFactory.getNotification(Type.WARNING_MESSAGE);
						notification.setDescription(I18N.message("msg.can.approve.assist"));
					}
				}
			}
		}
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
	
}
