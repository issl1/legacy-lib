package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.ArrayList;
import java.util.List;

import org.seuksa.frmk.tools.DateUtils;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.collection.model.CollectionFlag;
import com.nokor.efinance.core.collection.model.EColType;
import com.nokor.efinance.core.collection.model.ERequestStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author uhout.cheng
 */
public class InfoFlagPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 7383958395123969142L;

	private Label lblRequestedBy;
	private Label lblRequestedOn;
	private Label lblResult;
	
	private ERefDataComboBox<EColType> cbxColType;
	private Button btnValidate;
	private Button btnReject;
	
	private Contract contract; 
	
	private HorizontalLayout validationLayout;
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = ComponentFactory.getHtmlLabel(null);
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 */
	public InfoFlagPanel() {
		setWidth(525, Unit.PIXELS);
		Panel mainPanel = new Panel(getContentLayout());
		mainPanel.setCaption(I18N.message("flag.request"));
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getContentLayout() {
		lblRequestedBy = getLabelValue();
		lblRequestedOn = getLabelValue();
		lblResult = getLabelValue();
				
		GridLayout gridLayout = new GridLayout(10, 1);
		gridLayout.setMargin(true);
		gridLayout.setSpacing(true);
				
		int iCol = 0;
		gridLayout.addComponent(getLabelCaption(I18N.message("requested.by") + " : "), iCol++, 0);
		gridLayout.addComponent(lblRequestedBy, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(getLabelCaption(I18N.message("requested.on") + " : "), iCol++, 0);
		gridLayout.addComponent(lblRequestedOn, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(getLabelCaption(I18N.message("result") + " : "), iCol++, 0);
		gridLayout.addComponent(lblResult, iCol++, 0);
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.addComponent(gridLayout);		
		validationLayout = getValidationLayout();
		contentLayout.addComponent(validationLayout);
		contentLayout.setComponentAlignment(validationLayout, Alignment.MIDDLE_RIGHT);		
		return contentLayout;
	}
	
	/**
	 * @return
	 */
	private HorizontalLayout getValidationLayout() {
		HorizontalLayout validationLayout = new HorizontalLayout();
		validationLayout.setSizeUndefined();
		validationLayout.setSpacing(true);
		validationLayout.setMargin(new MarginInfo(false, true, false, false));
		
		cbxColType = new ERefDataComboBox<>(I18N.message("flag.team"), EColType.class);
		cbxColType.setRequired(true);
		
		btnValidate = ComponentLayoutFactory.getButtonStyle(I18N.message("validate"), FontAwesome.SAVE, 80, "btn btn-success button-small");		
		btnValidate.addClickListener(new ClickListener() {
			/**
			 */
			private static final long serialVersionUID = -1838784233861407810L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (cbxColType.getSelectedEntity() != null) {
					ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.approve.flag"),
						new ConfirmDialog.Listener() {
							private static final long serialVersionUID = 2831516023203612933L;
							public void onClose(ConfirmDialog dialog) {
								if (dialog.isConfirmed()) {
									List<Long> conIds = new ArrayList<Long>();
									conIds.add(contract.getId());
									COL_SRV.approveFlagRequest(conIds, cbxColType.getSelectedEntity());									
									Notification notification = new Notification("", I18N.message("msg.info.save.successfully"), 
																Notification.Type.HUMANIZED_MESSAGE, true);
									notification.show(Page.getCurrent());
									contract = CONT_SRV.getById(Contract.class, contract.getId());
									assignValues(contract);
								} 
							}
					});
					confirmDialog.setWidth("400px");
					confirmDialog.setHeight("150px");
				}
			}
		});
		
		btnReject = ComponentLayoutFactory.getButtonStyle(I18N.message("reject"), FontAwesome.BAN, 80, "btn btn-danger button-small");
		btnReject.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -4338323330548075975L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), I18N.message("confirm.approve.flag"),
					new ConfirmDialog.Listener() {
						private static final long serialVersionUID = 2831516023203612933L;
						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								COL_SRV.rejectFlagRequest(contract.getId()); 
								Notification notification = new Notification("", I18N.message("msg.info.save.successfully"), 
										Notification.Type.HUMANIZED_MESSAGE, true);
								notification.show(Page.getCurrent());
								contract = CONT_SRV.getById(Contract.class, contract.getId());
								assignValues(contract);
							} 
						}
				});
				confirmDialog.setWidth("400px");
				confirmDialog.setHeight("150px");
			}
		});
		
		FormLayout formLayout = new FormLayout();
		formLayout.addComponent(cbxColType);
		validationLayout.addComponent(formLayout);
		validationLayout.addComponent(btnValidate);
		validationLayout.addComponent(btnReject);
		validationLayout.setComponentAlignment(btnValidate, Alignment.MIDDLE_LEFT);
		validationLayout.setComponentAlignment(btnReject, Alignment.MIDDLE_LEFT);
		
		return validationLayout;
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
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		resetControls();
		this.contract = contract;
		CollectionFlag collectionFlag = contract.getCollection().getLastCollectionFlag();	
		setVisible(collectionFlag != null);
		lblRequestedBy.setValue(getDescription(collectionFlag == null ? "" : collectionFlag.getRequestedUserLogin()));
		lblRequestedOn.setValue(getDescription(collectionFlag == null ? "" : DateUtils.getDateLabel(collectionFlag.getRequestedDate(), "dd/MM/yyyy")));
		lblResult.setValue(getDescription(collectionFlag == null ? "" : collectionFlag.getRequestStatus().getDescEn()));
		
		if (collectionFlag != null && ProfileUtil.isColPhoneLeader() 
				&& collectionFlag.getRequestStatus().equals(ERequestStatus.PENDING) ) {
			validationLayout.setVisible(true);
		} else {
			validationLayout.setVisible(false);
		}
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		lblRequestedBy.setValue("");
		lblRequestedOn.setValue("");
		lblResult.setValue("");
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabelCaption(String caption) {
		Label label = ComponentFactory.getLabel(caption);
		label.setSizeUndefined();
		return label;
	}
}
