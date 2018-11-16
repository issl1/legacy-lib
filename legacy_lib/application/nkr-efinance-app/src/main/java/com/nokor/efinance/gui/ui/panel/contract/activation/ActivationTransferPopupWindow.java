package com.nokor.efinance.gui.ui.panel.contract.activation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.history.model.EHistoReason;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.MContract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * For Activation Contract
 * @author buntha.chea
 */
public class ActivationTransferPopupWindow extends Window implements FinServicesHelper, MContract, ValueChangeListener {
	/**
	 */
	private static final long serialVersionUID = -5598413245737132957L;
	private ERefDataComboBox<EHistoReason> cbxForceReason;
	private TextArea txtForceComment;
	private CheckBox cbForce;
	private boolean isForceMandatory;
	
	/**
	 * @return the cbForce
	 */
	public CheckBox getCbForce() {
		return cbForce;
	}

	/**
	 * 
	 * @return
	 */
	private TextArea getTextArea() {
		TextArea textArea = ComponentFactory.getTextArea(false, 250, 100);
		textArea.setMaxLength(1000);
		return textArea;
	}
	
	/**
	 * 
	 * @param caption
	 */
	public ActivationTransferPopupWindow(Contract contract, ClickListener onSaveListener) {
		setModal(true);		
		setClosable(false);
		setResizable(false);
		
		txtForceComment = getTextArea();
		txtForceComment.setEnabled(false);
		
		cbForce = new CheckBox(I18N.message("force"));
		cbForce.addValueChangeListener(this);
		
		isForceMandatory = false;
		
		cbxForceReason = new ERefDataComboBox<>(getHistoriesReason("CONTRACT_0001"));
		cbxForceReason.setImmediate(true);
		cbxForceReason.setWidth(170, Unit.PIXELS);
		cbxForceReason.setEnabled(false);
				
		Button btnconfirm = ComponentLayoutFactory.getButtonStyle("confirm", FontAwesome.CHECK, 80, "btn btn-success button-small");
		Button btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.BAN, 80, "btn btn-success button-small");
		
		btnconfirm.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -6182677160749225701L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				if (isForceMandatory) {
					if (cbxForceReason.getSelectedEntity() == null || StringUtils.isEmpty(txtForceComment.getValue())) {
						MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "160px", I18N.message("information"),
								MessageBox.Icon.ERROR, I18N.message("please.select.a.reason.type.and.enter.remark.force"), 
								Alignment.MIDDLE_RIGHT, new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
						mb.show();
					}else {
						updateContractHistory(onSaveListener, event, contract);
					}
				} else {
					updateContractHistory(onSaveListener, event, contract);
				}
			}
		});
		
		btnCancel.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -421984394158414972L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(btnconfirm);
		buttonLayout.addComponent(btnCancel);
		
		HorizontalLayout forceInputLayout = new HorizontalLayout();
		forceInputLayout.setSpacing(true);
		forceInputLayout.addComponent(cbxForceReason);
		forceInputLayout.addComponent(ComponentFactory.getLabel("remark"));
		forceInputLayout.addComponent(txtForceComment);
		
		HorizontalLayout cbForceLayout = new HorizontalLayout();
		cbForceLayout.setWidth(100, Unit.PIXELS);
		cbForceLayout.addComponent(cbForce);
		
	
		HorizontalLayout forceLayout = new HorizontalLayout();
		forceLayout.setSpacing(true);
		forceLayout.addComponent(cbForceLayout);
		forceLayout.addComponent(forceInputLayout);
		
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		contentLayout.setSizeUndefined();
		contentLayout.addComponent(forceLayout);
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
		contract.setWkfReason(cbxForceReason.getSelectedEntity());
		contract.setHistComment(txtForceComment.getValue());
		contract.setForcedHistory(true);
		CONT_SRV.saveOrUpdate(contract);
		if (onSaveListener != null) {
			onSaveListener.buttonClick(event);
		}
		close();
	}
	
	/**
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty().equals(cbForce)) {
			cbxForceReason.setEnabled(cbForce.getValue());
			txtForceComment.setEnabled(cbForce.getValue());
			isForceMandatory = cbForce.getValue();
		}
	}

}
