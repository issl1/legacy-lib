package com.nokor.efinance.gui.ui.panel.contract.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.collection.model.CollectionAction;
import com.nokor.efinance.core.collection.model.ECallType;
import com.nokor.efinance.core.collection.service.CollectionHistoryRestriction;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.model.LockSplitRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.efinance.core.widget.LabelFormCustomLayout;
import com.nokor.efinance.core.workflow.LockSplitWkfStatus;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author uhout.cheng
 */
public class ContractTaskPanel extends AbstractControlPanel implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -512183791076766535L;
	
	private final static String USD_FORMART = "###,##0.00";

	private HorizontalLayout topLayout;
	
	private Label lblAssignmentDate;
	private Label lblDayElapsed;
	private Label lblProgress;
	private Label lblNbOfCall;
	private Label lblNbOfSMS;
	private Label lblNbOfEmail;
	private Label lblNbOfField;
	private Label lblCollectedDue;
	
	private Label lblLastAction;
	private Label lblNextActionDate;
	private Label lblNbOfPromises;
	
	private HorizontalLayout middleLayout;
	private AssistFlagRequestLayout assistFlagRequestLayout;
	
	/**
	 * 
	 * @param deleget
	 */
	public ContractTaskPanel() {
		Label lblDebtPressing = ComponentFactory.getHtmlLabel("<h2 style=\"margin:0\">" + I18N.message("debt.pressing") + "</h2>");
		
		topLayout = new HorizontalLayout();
		topLayout.setWidth(100, Unit.PERCENTAGE);
		topLayout.addComponent(lblDebtPressing);
		topLayout.setComponentAlignment(lblDebtPressing, Alignment.MIDDLE_LEFT);
		
		lblAssignmentDate = getLabelValue();
		lblDayElapsed = getLabelValue();
		lblProgress = getLabelValue();
		lblNbOfCall = getLabelValue();
		lblNbOfSMS = getLabelValue();
		lblNbOfEmail = getLabelValue();
		lblNbOfField = getLabelValue();
		lblCollectedDue = getLabelValue();
		lblLastAction = getLabelValue();
		lblNextActionDate = getLabelValue();
		lblNbOfPromises = getLabelValue();
		
		assistFlagRequestLayout = new AssistFlagRequestLayout();
		assistFlagRequestLayout.setVisible(!ProfileUtil.isCMProfile());
		
		middleLayout = new HorizontalLayout();
		
		VerticalLayout vLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		vLayout.addComponent(topLayout);
		vLayout.addComponent(middleLayout);
       
		FieldSet taskFieldSet = new FieldSet();
		taskFieldSet.setLegend(I18N.message("task"));
		taskFieldSet.setContent(vLayout);
	
		Panel taskFieldSetPanel = new Panel(taskFieldSet);
		taskFieldSetPanel.setStyleName(Reindeer.PANEL_LIGHT);
	  
		addComponent(taskFieldSetPanel);
	}
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getMiddleLeftLayout() {
		VerticalLayout vLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, true), false);
		vLayout.addComponent(new LabelFormCustomLayout("assignment.date", lblAssignmentDate.getValue(), 90, 70));
		
		HorizontalLayout leftHorLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		leftHorLayout.addComponent(new LabelFormCustomLayout("day.elapsed", lblDayElapsed.getValue(), 90, 35));
		leftHorLayout.addComponent(new LabelFormCustomLayout("progress", lblProgress.getValue(), 90, 35));
		vLayout.addComponent(leftHorLayout);
		
		leftHorLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		leftHorLayout.addComponent(new LabelFormCustomLayout("nb.call", lblNbOfCall.getValue(), 90, 35));
		leftHorLayout.addComponent(new LabelFormCustomLayout("nb.sms", lblNbOfSMS.getValue(), 90, 35));
		vLayout.addComponent(leftHorLayout);
		
		leftHorLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		leftHorLayout.addComponent(new LabelFormCustomLayout("nb.email", lblNbOfEmail.getValue(), 90, 35));
		leftHorLayout.addComponent(new LabelFormCustomLayout("nb.field", lblNbOfField.getValue(), 90, 35));
		vLayout.addComponent(leftHorLayout);
		
		vLayout.addComponent(new LabelFormCustomLayout("collected.due", lblCollectedDue.getValue(), 90, 100));
		
		return vLayout;
	}
	
	/**
	 * 
	 * @return
	 */
	private VerticalLayout getMiddleRightLayout() {
		VerticalLayout vLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, true, false, true), false);
		vLayout.addComponent(new LabelFormCustomLayout("last.action", lblLastAction.getValue(), 120, 350));
		vLayout.addComponent(new LabelFormCustomLayout("next.action.date", lblNextActionDate.getValue(), 120, 150));
		vLayout.addComponent(new LabelFormCustomLayout("nb.of.promised", lblNbOfPromises.getValue(), 120, 350));
		
		vLayout.addComponent(assistFlagRequestLayout);
		
		return vLayout;
	}
	
	/**
	 * 
	 * @param conId
	 */
	public void assignValues(Long conId) {
		this.reset();
		if (conId != null) {
			Contract contract = ENTITY_SRV.getById(Contract.class, conId);
			assistFlagRequestLayout.assignValues(contract);
			
			List<ContractUserInbox> userInboxs = INBOX_SRV.getContractUserInboxByContract(conId);
			ContractUserInbox userInbox = null;
			if (userInboxs != null && !userInboxs.isEmpty()) {
				userInbox = userInboxs.get(userInboxs.size() - 1);
			}
			
			if (userInbox != null) {
				lblAssignmentDate.setValue(getDescription(getDateFormat(userInbox.getCreateDate())));
			}
			
			lblNbOfCall.setValue(getDescription(getDefaultString(getNbOfContacted(conId, ECallType.CALL))));
			lblNbOfSMS.setValue(getDescription(getDefaultString(getNbOfContacted(conId, ECallType.SMS))));
			lblNbOfEmail.setValue(getDescription(getDefaultString(getNbOfContacted(conId, ECallType.MAIL))));
			lblNbOfField.setValue(getDescription(getDefaultString(getNbOfContacted(conId, ECallType.FIELD))));
			
			Collection col = contract.getCollection();
			String lastAction = StringUtils.EMPTY;
			Date nextActionDate = null;
			
			if (col != null) {
				double collected = 0d;
				List<Payment> payments = PAYMENT_SRV.getListPaymentPaidInCurrentMonth(contract.getId());
				if (payments != null && !payments.isEmpty()) {
					for (Payment payment : payments) {
						collected += MyNumberUtils.getDouble(payment.getTiPaidAmount());
					}
				}
				lblCollectedDue.setValue(getDescription(MyNumberUtils.formatDoubleToString(collected, USD_FORMART) + "/" +
						MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(col.getTiTotalAmountInOverdue()), USD_FORMART)));
				
				CollectionAction colAction = col.getLastAction();
				if (colAction != null) {
					String createdDate = "(" + DateUtils.getDateLabel(colAction.getCreateDate(), DateUtils.FORMAT_DDMMYYYY_SLASH) + ")";
					String comment = colAction.getComment();
					lastAction = createdDate + " - " + comment;
					nextActionDate = col.getLastAction().getNextActionDate();
				}
			}
			lblLastAction.setValue(getDefaultString(lastAction));
			lblNextActionDate.setValue(getDescription(getDateFormat(nextActionDate)));
			
			String broken = getDefaultString(getNbOfPromises(contract.getReference(), LockSplitWkfStatus.LEXP));
			String kept = getDefaultString(getNbOfPromises(contract.getReference(), LockSplitWkfStatus.LPAI));
			String pending = getDefaultString(getNbOfPromises(contract.getReference(), LockSplitWkfStatus.LNEW));
			
			lblNbOfPromises.setValue(getDescription(I18N.message("broken") + " : " + broken + StringUtils.SPACE + 
					I18N.message("kept") + " : " + kept + StringUtils.SPACE +
					I18N.message("pending") + " : " + pending));
		} 
		
		middleLayout.removeAllComponents();
		middleLayout.addComponent(getMiddleLeftLayout());
		middleLayout.addComponent(getMiddleRightLayout());
	}
	
	/**
	 * 
	 * @param conRef
	 * @param status
	 * @return
	 */
	private int getNbOfPromises(String conRef, EWkfStatus status) {
		LockSplitRestriction restrictions = new LockSplitRestriction();
		restrictions.setContractID(conRef);
		restrictions.getWkfStatusList().add(status);
		return (int) ENTITY_SRV.count(restrictions);
	}
	
	/***
	 * 
	 * @param conId
	 * @param callType
	 * @return
	 */
	private int getNbOfContacted(Long conId, ECallType callType) {
		CollectionHistoryRestriction restrictions = new CollectionHistoryRestriction();
		restrictions.setContractId(conId);
		restrictions.setCallTypes(new ECallType[] { callType });
		return (int) ENTITY_SRV.count(restrictions);
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel#reset()
	 */
	@Override
	protected void reset() {
		super.reset();
		lblAssignmentDate.setValue(StringUtils.EMPTY);
		lblDayElapsed.setValue(StringUtils.EMPTY);
		lblProgress.setValue(StringUtils.EMPTY);
		lblNbOfCall.setValue(StringUtils.EMPTY);
		lblNbOfSMS.setValue(StringUtils.EMPTY);
		lblNbOfEmail.setValue(StringUtils.EMPTY);
		lblNbOfField.setValue(StringUtils.EMPTY);
		lblCollectedDue.setValue(StringUtils.EMPTY);
		lblLastAction.setValue(StringUtils.EMPTY);
		lblNextActionDate.setValue(StringUtils.EMPTY);
		lblNbOfPromises.setValue(StringUtils.EMPTY);
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
	 * @return
	 */
	private Label getLabelValue() {
		Label label = ComponentFactory.getHtmlLabel(StringUtils.EMPTY);
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
		stringBuffer.append(value == null ? StringUtils.EMPTY : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
}
