package com.nokor.efinance.gui.ui.panel.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class CommentReasonFormPanel extends Window implements FinServicesHelper, MContract {
	
	/** */
	private static final long serialVersionUID = 1674924725658858527L;
		
	private ERefDataComboBox<EHistoReason> cbxHistoryReason;
	private TextArea txtComment;
	
	/**
	 * 
	 * @param code
	 * @param contract
	 * @param wkfStatus
	 * @param onSaveListener
	 */
	public CommentReasonFormPanel(String code, Contract contract, EWkfStatus wkfStatus, ClickListener onSaveListener) {
		setModal(true);		
		setClosable(false);
		setResizable(false);			
		
		txtComment = ComponentFactory.getTextArea("remark", true, 250, 100);
		txtComment.setMaxLength(1000);
		
		cbxHistoryReason = new ERefDataComboBox<EHistoReason>(I18N.message("reason.type"), getHistoriesReason(code));
		cbxHistoryReason.setImmediate(true);
		cbxHistoryReason.setRequired(true);
		cbxHistoryReason.setWidth(250, Unit.PIXELS);
		
		Button btnconfirm = ComponentLayoutFactory.getButtonStyle("confirm", FontAwesome.CHECK, 80, "btn btn-success button-small");
		Button btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.BAN, 80, "btn btn-success button-small");
		
		btnconfirm.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 2378401897889934453L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (cbxHistoryReason.getSelectedEntity() == null || StringUtils.isEmpty(txtComment.getValue())) {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
							MessageBox.Icon.ERROR, I18N.message("please.select.a.reason.type.and.enter.remark"), 
							Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				} else {
					updateContractHistory(onSaveListener, event, contract);
				}
			}
		});
		
		btnCancel.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 1354826877381805919L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(true, true);
		buttonLayout.addComponent(btnconfirm);
		buttonLayout.addComponent(btnCancel);
		
		VerticalLayout commentLayout = new VerticalLayout();
		commentLayout.addComponent(getFormLayout(cbxHistoryReason));
		commentLayout.addComponent(getFormLayout(txtComment));
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSizeUndefined();
		contentLayout.addComponent(commentLayout);
		contentLayout.addComponent(buttonLayout);
		contentLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
        setContent(contentLayout);
	}
	
	/**
	 * Get Histories by code
	 * @param code
	 * @return
	 */
	private List<EHistoReason> getHistoriesReason(String code) {
		List<EHistoReason> histoReasons = new ArrayList<EHistoReason>();
		if (EHistoReason.values() != null && !EHistoReason.values().isEmpty()) {
			for (EHistoReason eHistoReason : EHistoReason.values()) {
				if (code.equals(eHistoReason.getCode())) {
					histoReasons.add(eHistoReason);
				}
			}
		}
		return histoReasons;
	}
	
	/**
	 * 
	 * @param onSaveListener
	 * @param event
	 * @param contract
	 */
	private void updateContractHistory(ClickListener onSaveListener, ClickEvent event, Contract contract) {
		contract.setWkfReason(cbxHistoryReason.getSelectedEntity());
		contract.setHistComment(txtComment.getValue());
		contract.setForcedHistory(true);
		CONT_SRV.saveOrUpdate(contract);
		if (onSaveListener != null) {
			onSaveListener.buttonClick(event);
		}
		close();
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	private FormLayout getFormLayout(Component component) {
		FormLayout frmLayout = new FormLayout();
		frmLayout.setMargin(true);
		frmLayout.setStyleName("myform-align-left");
		frmLayout.addComponent(component);
		return frmLayout;
	}
}
