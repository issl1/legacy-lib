package com.nokor.efinance.gui.ui.panel.contract.queue;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.widget.FieldSet;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * 
 * @author seanglay.chhoeurn
 *
 */
public class QueueDetailPanel extends AbstractControlPanel implements ClickListener {

	/** */
	private static final long serialVersionUID = -7264963998317704893L;
	
	private Label lblType;
	private Label lblCreateDate;
	private Label lblCloseDate;
	private Label lblStaffInCharge;
	private Label lblDepartment;
	private Label lblCollected;
	private Label lblFeePenalty;
	private Label lblNextActionDate;
	private Label lblNumberOfCall;
	private Label lblNumberOfPromises;
	private Label lblNumberOfField;
	private Label lblNumberofEmail;
	private Label lblNumberOfSMS;
	private Label lblInstallment;
	private Label lblBroken;
	private Label lblKept;
	private Label lblPendding;
	
	private Button btnFlagRequest;
	private Button btnFlagCancel;
	private Button btnAssistRequest;
	private Button btnAssistCancel;
	
	private QueueTableHistoryPanel queueTableHistoryPanel;
	
	/** */
	public QueueDetailPanel() {
		createForm();
	}

	/**
	 * create form
	 */
	private void createForm() {
		lblType = getLabelValue();
		lblCreateDate = getLabelValue();
		lblCloseDate = getLabelValue();
		lblStaffInCharge = getLabelValue();
		lblDepartment = getLabelValue();
		lblCollected = getLabelValue();
		lblFeePenalty = getLabelValue();
		lblNextActionDate = getLabelValue();
		lblNumberOfCall = getLabelValue();
		lblNumberOfSMS = getLabelValue();
		lblNumberOfPromises = getLabelValue();
		lblNumberofEmail = getLabelValue();
		lblNumberOfField = getLabelValue();
		lblInstallment = getLabelValue();
		lblBroken = getLabelValue();
		lblKept = getLabelValue();
		lblPendding = getLabelValue();
		
		Label lblTypeTitle = getLabel("type");
		Label lblCreateDateTitle = getLabel("create.date");
		Label lblCloseDateTitle = getLabel("close.date");
		Label lblStaffInChargeTitle = getLabel("staff.in.charge");
		Label lblDepartmentTitle = getLabel("department");
		Label lblCollectedTitle = getLabel("collected");
		Label lblFeePenaltyTitle = getLabel("fee.penalty");
		Label lblNextActionDateTitle = getLabel("next.action.date");
		Label lblNumberOfSMSTitle = getLabel("nb.sms");
		Label lblNumberOfCallTitle = getLabel("nb.call");
		Label lblNumberOfPromisesTitle = getLabel("nb.of.promised");
		Label lblNumberOfEmailTitle = getLabel("nb.email");
		Label lblNumberOfFieldTitle = getLabel("nb.field");
		Label lblInstallmentTitle = getLabel("installment");
		Label lblBrokenTitle = getLabel("broken");
		Label lblKeptTitle = getLabel("kept");
		Label lblPenddingTitle = getLabel("pending");
		Label lblFlage = ComponentFactory.getLabel("flag");
		Label lblAssist = ComponentFactory.getLabel("assist");
		
		btnFlagRequest = new NativeButton(I18N.message("request"), this);
		btnFlagRequest.setStyleName("btn btn-success button-small");
		btnFlagRequest.setWidth(70, Unit.PIXELS);
		
		btnFlagCancel = new NativeButton(I18N.message("cancel"), this);
		btnFlagCancel.setStyleName("btn btn-success button-small");
		btnFlagCancel.setWidth(70, Unit.PIXELS);
		
		btnAssistRequest = new NativeButton(I18N.message("request"), this);
		btnAssistRequest.setStyleName("btn btn-success button-small");
		btnAssistRequest.setWidth(70, Unit.PIXELS);
		
		btnAssistCancel = new NativeButton(I18N.message("cancel"), this);
		btnAssistCancel.setStyleName("btn btn-success button-small");
		btnAssistCancel.setWidth(70, Unit.PIXELS);
		
		queueTableHistoryPanel = new QueueTableHistoryPanel();

		GridLayout topGridLayout = new GridLayout(8, 1);
		topGridLayout.setMargin(true);
		topGridLayout.setSpacing(true);
		
		int iCol = 0;
		topGridLayout.addComponent(lblTypeTitle, iCol++, 0);
		topGridLayout.addComponent(lblType, iCol++, 0);
		topGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		topGridLayout.addComponent(lblCreateDateTitle, iCol++, 0);
		topGridLayout.addComponent(lblCreateDate, iCol++, 0);
		topGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCol++, 0);
		topGridLayout.addComponent(lblCloseDateTitle, iCol++, 0);
		topGridLayout.addComponent(lblCloseDate, iCol++, 0);
		
		GridLayout middleGridLayout = new GridLayout(17, 5);
		middleGridLayout.setMargin(true);
		middleGridLayout.setSpacing(true);
		
		int iCols = 0;
		middleGridLayout.addComponent(lblStaffInChargeTitle, iCols++, 0);
		middleGridLayout.addComponent(lblStaffInCharge, iCols++, 0);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 0);
		middleGridLayout.addComponent(lblDepartmentTitle, iCols++, 0);
		middleGridLayout.addComponent(lblDepartment, iCols++, 0);
		
		iCols = 0;
		middleGridLayout.addComponent(lblCollectedTitle, iCols++, 1);
		middleGridLayout.addComponent(lblCollected, iCols++, 1);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 1);
		middleGridLayout.addComponent(lblInstallmentTitle, iCols++, 1);
		middleGridLayout.addComponent(lblInstallment, iCols++, 1);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 1);
		middleGridLayout.addComponent(lblFeePenaltyTitle, iCols++, 1);
		middleGridLayout.addComponent(lblFeePenalty, iCols++, 1);
		
		iCols = 0;
		middleGridLayout.addComponent(lblNextActionDateTitle, iCols++, 2);
		middleGridLayout.addComponent(lblNextActionDate, iCols++, 2);
		
		iCols = 0;
		middleGridLayout.addComponent(lblNumberOfCallTitle, iCols++, 3);
		middleGridLayout.addComponent(lblNumberOfCall, iCols++, 3);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 3);
		middleGridLayout.addComponent(lblNumberOfSMSTitle, iCols++, 3);
		middleGridLayout.addComponent(lblNumberOfSMS, iCols++, 3);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 3);
		middleGridLayout.addComponent(lblNumberOfPromisesTitle, iCols++, 3);
		middleGridLayout.addComponent(lblNumberOfPromises, iCols++, 3);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), iCols++, 3);
		middleGridLayout.addComponent(lblBrokenTitle, iCols++, 3);
		middleGridLayout.addComponent(lblBroken, iCols++, 3);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), iCols++, 3);
		middleGridLayout.addComponent(lblKeptTitle, iCols++, 3);
		middleGridLayout.addComponent(lblKept, iCols++, 3);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), iCols++, 3);
		middleGridLayout.addComponent(lblPenddingTitle, iCols++, 3);
		middleGridLayout.addComponent(lblPendding, iCols++, 3);
		
		iCols = 0;
		middleGridLayout.addComponent(lblNumberOfEmailTitle, iCols++, 4);
		middleGridLayout.addComponent(lblNumberofEmail, iCols++, 4);
		middleGridLayout.addComponent(ComponentFactory.getSpaceLayout(3, Unit.PIXELS), iCols++, 4);
		middleGridLayout.addComponent(lblNumberOfFieldTitle, iCols++, 4);
		middleGridLayout.addComponent(lblNumberOfField, iCols++, 4);
		
		GridLayout bellowGridLayout = new GridLayout(5, 2);
		bellowGridLayout.setSpacing(true);
		bellowGridLayout.setMargin(true);
		
		bellowGridLayout.addComponent(lblFlage, 0, 0);
		bellowGridLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), 1, 0);
		bellowGridLayout.addComponent(btnFlagRequest, 2, 0);
		bellowGridLayout.addComponent(ComponentFactory.getSpaceLayout(1, Unit.PIXELS), 3, 0);
		bellowGridLayout.addComponent(btnFlagCancel, 4, 0);
		
		bellowGridLayout.addComponent(lblAssist, 0, 1);
		bellowGridLayout.addComponent(ComponentFactory.getSpaceLayout(2, Unit.PIXELS), 1, 1);
		bellowGridLayout.addComponent(btnAssistRequest, 2, 1);
		bellowGridLayout.addComponent(ComponentFactory.getSpaceLayout(1, Unit.PIXELS), 3, 1);
		bellowGridLayout.addComponent(btnAssistCancel, 4, 1);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(topGridLayout);
		verticalLayout.addComponent(middleGridLayout);
		verticalLayout.addComponent(bellowGridLayout);
		verticalLayout.addComponent(queueTableHistoryPanel);
		
		FieldSet fieldSet = new FieldSet();
		fieldSet.setLegend(I18N.message("queue.detail"));
		fieldSet.setContent(verticalLayout);
		
		Panel mainPanel = new Panel();
		mainPanel.setStyleName(Reindeer.PANEL_LIGHT);
		mainPanel.setContent(fieldSet);
		
		addComponent(mainPanel);
		
	}
	
	/**
	 * 
	 * @return label
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		
	}
}
