package com.nokor.efinance.gui.ui.panel.contract.activation;

import java.util.Date;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractWkfHistoryItem;
import com.nokor.efinance.core.contract.service.ContractUtils;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * Confirmation panel for activation
 * @author uhout.cheng
 */
public class ContractStatusDetailPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 7002476378087353235L;

	private Label lblContractStatus;
	private Label lblActionStatus;
	private Label lblDate;
	private TextArea txtReason;
	private TextArea txtReasonForce;
	private TextArea txtReasonHoldPayment;
	
	private Label lblDisbursedStatus;
	private Label lblDisbursedDate;
	
	private Label lblForced;
	private Label lblHolded;
	private Button btnCheckedForce;
	private Button btnCheckedHold;
	
	private Label lblHoldPayStaus;
	private Label lblHoldPayDate; 
	
	private EWkfStatus wkfStatus;
	private HorizontalLayout userDetailLayoutActivated;
	private HorizontalLayout userDetailLayoutDisbursed;
	private HorizontalLayout contractStatusLayout;
	
	/**
	 * 
	 */
	public ContractStatusDetailPanel() {
		setCaption(I18N.message("status"));
		init();
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		Label label = ComponentFactory.getLabel(caption);
		label.setSizeUndefined();
		return label;
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
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : "";
	}
	
	/**
	 * 
	 */
	public void init() {
		lblContractStatus = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblContractStatus.setWidth(100, Unit.PIXELS);
		lblActionStatus = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblActionStatus.setWidth(100, Unit.PIXELS);
		lblDate = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblDate.setWidth(100, Unit.PIXELS);
		txtReason = ComponentFactory.getTextArea(false, 250, 80);
		txtReasonForce = ComponentFactory.getTextArea(false, 250, 80);
		txtReasonHoldPayment = ComponentFactory.getTextArea(false, 250, 80);
		txtReasonForce.setVisible(false);
		txtReasonHoldPayment.setVisible(false);
		
		btnCheckedForce = getCheckedIcon();
		btnCheckedHold = getCheckedIcon();
		btnCheckedForce.setVisible(false);
		btnCheckedHold.setVisible(false);
		
		lblForced = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblForced.setSizeUndefined();
		lblForced.setVisible(false);
		
		lblHolded = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblHolded.setSizeUndefined();
		lblHolded.setVisible(false);
		
		lblDisbursedStatus = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblDisbursedStatus.setWidth(100, Unit.PIXELS);
		lblDisbursedDate = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblDisbursedDate.setWidth(100, Unit.PIXELS);
		
		lblHoldPayStaus = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblHoldPayStaus.setWidth(100, Unit.PIXELS);
		lblHoldPayDate = ComponentFactory.getLabel(null, ContentMode.HTML);
		lblHoldPayDate.setWidth(100, Unit.PIXELS);
		
		contractStatusLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		userDetailLayoutActivated = ComponentLayoutFactory.getHorizontalLayout(false, true);
		userDetailLayoutDisbursed = ComponentLayoutFactory.getHorizontalLayout(false, true);
	
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(true, true);
		verLayout.addComponent(contractStatusLayout);
		verLayout.addComponent(userDetailLayoutActivated);
		verLayout.addComponent(userDetailLayoutDisbursed);
		
		Panel mainPanel = new Panel(verLayout);
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		reset();
		wkfStatus = contract.getWkfStatus();
		String caption = "created.by";
		if (wkfStatus.equals(ContractWkfStatus.CAN)) {
			caption = "cancelled.by";
		} else if (wkfStatus.equals(ContractWkfStatus.WRI)) {
			caption = "terminated.by";
		} else if (wkfStatus.equals(ContractWkfStatus.BLOCKED)) {
			caption = "blocked.by";
		} else if (ContractUtils.isActivated(contract) || ContractUtils.isHoldPayment(contract)) {
			caption = "activated.by";
		} else if (wkfStatus.equals(ContractWkfStatus.CLO)) {
			caption = "closed.by";
		} 
		
		contractStatusLayout.addComponent(getLabel("contract.status"));
		contractStatusLayout.addComponent(getLabel(":"));
		contractStatusLayout.addComponent(lblContractStatus);
	
		lblContractStatus.setValue(getDescription(wkfStatus.getDescLocale()));
		List<ContractWkfHistoryItem> items = CONT_SRV.getListContractHistories(contract);
		if (items != null && !items.isEmpty()) {
			SecUser secUser = null;
			if (ContractUtils.isActivated(contract) || ContractUtils.isHoldPayment(contract)) {
				for (ContractWkfHistoryItem item : items) {
					secUser = item.getModifiedBy();
					if (item.getNewValue().equals(ContractWkfStatus.FIN.getCode())) {
						assignValuesToActivatedLayout(secUser, contract, item);
						if (item.getOldValue().equals(ContractWkfStatus.HOLD_PAY.getCode())) {
							lblDisbursedStatus.setValue(getDescription(secUser.getLogin()));
							lblDisbursedDate.setValue(getDescription(getDateFormat(item.getChangeDate())));
							
							userDetailLayoutDisbursed.addComponent(getLabel("disbursed.by"));
							userDetailLayoutDisbursed.addComponent(getLabel(":"));
							userDetailLayoutDisbursed.addComponent(lblDisbursedStatus);
							userDetailLayoutDisbursed.addComponent(getLabel("date"));
							userDetailLayoutDisbursed.addComponent(getLabel(":"));
							userDetailLayoutDisbursed.addComponent(lblDisbursedDate);
						}
					} else if (item.getNewValue().equals(ContractWkfStatus.HOLD_PAY.getCode())) {
						assignValuesToActivatedLayout(secUser, contract, item);
						lblHolded.setValue(getDescription(I18N.message("hold")));
						txtReasonHoldPayment.setValue(item.getComment2());
						btnCheckedHold.setVisible(true);
						lblHolded.setVisible(true);
						txtReasonHoldPayment.setVisible(true);
						lblHoldPayStaus.setValue(getDescription(secUser.getLogin()));
						lblHoldPayDate.setValue(getDescription(getDateFormat(item.getChangeDate())));
						
						userDetailLayoutDisbursed.addComponent(getCheckedIcon());
						userDetailLayoutDisbursed.addComponent(lblHolded);
						userDetailLayoutDisbursed.addComponent(getLabel("holded.by"));
						userDetailLayoutDisbursed.addComponent(getLabel(":"));
						userDetailLayoutDisbursed.addComponent(lblHoldPayStaus);
						userDetailLayoutDisbursed.addComponent(getLabel("date"));
						userDetailLayoutDisbursed.addComponent(getLabel(":"));
						userDetailLayoutDisbursed.addComponent(lblHoldPayDate);
						userDetailLayoutDisbursed.addComponent(getLabel("reason"));
						userDetailLayoutDisbursed.addComponent(getLabel(":"));
						userDetailLayoutDisbursed.addComponent(txtReasonHoldPayment);
					}
				}
			} else {
				secUser = items.get(0).getModifiedBy();
				if (secUser != null) {
					lblActionStatus.setValue(getDescription(secUser.getLogin()));
					lblDate.setValue(getDescription(getDateFormat(items.get(0).getChangeDate())));
					txtReason.setValue(items.get(0).getComment());
				}
			}
			
		}
		
		if (ContractUtils.isActivated(contract) || ContractUtils.isHoldPayment(contract) || wkfStatus.equals(ContractWkfStatus.CLO) 
				|| wkfStatus.equals(ContractWkfStatus.BLOCKED) || wkfStatus.equals(ContractWkfStatus.WRI) 
				|| wkfStatus.equals(ContractWkfStatus.CAN)) {
			userDetailLayoutActivated.addComponent(getLabel(caption));
			userDetailLayoutActivated.addComponent(getLabel(":"));
			userDetailLayoutActivated.addComponent(lblActionStatus);
			userDetailLayoutActivated.addComponent(getLabel("date"));
			userDetailLayoutActivated.addComponent(getLabel(":"));
			userDetailLayoutActivated.addComponent(lblDate);
		}
		
		if ((ContractUtils.isActivated(contract) || ContractUtils.isHoldPayment(contract)) && contract.isForceActivated()) {
			userDetailLayoutActivated.addComponent(getCheckedIcon());
			userDetailLayoutActivated.addComponent(lblForced);
			userDetailLayoutActivated.addComponent(getLabel("reason"));
			userDetailLayoutActivated.addComponent(getLabel(":"));
			userDetailLayoutActivated.addComponent(txtReasonForce);
		} else if(ContractUtils.isTerminated(contract) || ContractUtils.isCancelled(contract)) {
			userDetailLayoutActivated.addComponent(getLabel("reason"));
			userDetailLayoutActivated.addComponent(getLabel(":"));
			userDetailLayoutActivated.addComponent(txtReason);
		}
	}
	
	/**
	 * 
	 * @param secUser
	 * @param contract
	 * @param item
	 */
	private void assignValuesToActivatedLayout(SecUser secUser, Contract contract, ContractWkfHistoryItem item) {
		lblActionStatus.setValue(getDescription(secUser.getLogin()));
		lblDate.setValue(getDescription(getDateFormat(item.getChangeDate())));
		if (contract.isForceActivated()) {
			lblForced.setValue(getDescription(I18N.message("force")));
			txtReasonForce.setValue(item.getComment());
			btnCheckedForce.setVisible(true);
			lblForced.setVisible(true);
			txtReasonForce.setVisible(true);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Button getCheckedIcon() {
		Button button = new Button(FontAwesome.CHECK);
		button.setStyleName(Reindeer.BUTTON_LINK);
		return button;
	}
	
	/**
	 * 
	 */
	protected void reset() {
		contractStatusLayout.removeAllComponents();
		userDetailLayoutActivated.removeAllComponents();
		userDetailLayoutDisbursed.removeAllComponents();
		lblContractStatus.setValue("");
		lblActionStatus.setValue("");
		lblDate.setValue("");
		txtReason.setValue("");
		txtReasonForce.setValue("");
		txtReasonHoldPayment.setValue("");
		lblDisbursedStatus.setValue("");
		lblDisbursedDate.setValue("");
		lblHoldPayStaus.setValue("");
		lblHoldPayDate.setValue(""); 
		lblForced.setValue("");
		lblHolded.setValue("");
		lblForced.setVisible(false);
		lblHolded.setVisible(false);
		txtReasonForce.setVisible(false);
		txtReasonHoldPayment.setVisible(false);
		btnCheckedForce.setVisible(false);
		btnCheckedHold.setVisible(false);
	}
	
}
