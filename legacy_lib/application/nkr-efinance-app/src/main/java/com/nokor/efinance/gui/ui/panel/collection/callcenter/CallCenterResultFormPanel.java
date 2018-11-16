package com.nokor.efinance.gui.ui.panel.collection.callcenter;

import java.util.Arrays;
import java.util.List;

import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.callcenter.model.CallCenterHistory;
import com.nokor.efinance.core.callcenter.model.ECallCenterResult;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * Call center history form panel latest
 * @author uhout.cheng
 */
public class CallCenterResultFormPanel extends AbstractControlPanel implements FinServicesHelper, ClickListener {

	/** */
	private static final long serialVersionUID = -3201618115832760236L;
	
	private static final String JOBCOMPLETE = I18N.message("job.complete");
	private static final String FOLLOWUP = I18N.message("follow.up");
	
	private OptionGroup optionGroupColAction;
	private TextArea txtRemark;
	private Button btnSave;
	
	private CallCenterHistory callCenterHistory;
	private Contract contract;
	
	/**
	 * 
	 */
	public CallCenterResultFormPanel() {
		init();
		
		Label lblRemarkTitle = ComponentFactory.getLabel("remark");
		
		GridLayout gridLayout = ComponentLayoutFactory.getGridLayoutDefaultMargin(3, 3);
		int iCol = 1;
		gridLayout.addComponent(optionGroupColAction, iCol++, 0);
		iCol = 0;
		gridLayout.addComponent(lblRemarkTitle, iCol++, 1);
		gridLayout.addComponent(txtRemark, iCol++, 1);
		iCol = 1;
		gridLayout.addComponent(btnSave, iCol++, 2);
		gridLayout.setComponentAlignment(lblRemarkTitle, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(btnSave, Alignment.MIDDLE_RIGHT);
	
		VerticalLayout verLayout = ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, false, true, false), false);
		verLayout.addComponent(gridLayout);
		
		Panel mainPanel = new Panel(verLayout);
		mainPanel.setCaption("<h3 style=\"color:#449D44; margin:0\">" + I18N.message("welcome.call") + "</h3>");
		mainPanel.setCaptionAsHtml(true);
		setMargin(true);
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 */
	private void init() {
		List<String> options = Arrays.asList(new String[] {JOBCOMPLETE, FOLLOWUP});
		optionGroupColAction = ComponentLayoutFactory.getOptionGroup(options);
		txtRemark = ComponentFactory.getTextArea(false, 170, 50);
		btnSave = ComponentLayoutFactory.getButtonSave();
		btnSave.addClickListener(this);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		this.contract = contract;
		this.callCenterHistory = CALL_CTR_SRV.getLastCallCenterHistory(contract.getId());
		if (this.callCenterHistory == null) {
			this.callCenterHistory = CallCenterHistory.createInstance();
		} else {
			ECallCenterResult ko = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.KO);
			ECallCenterResult ok = ENTITY_SRV.getByCode(ECallCenterResult.class, ECallCenterResult.OK);
			if (ko != null && ok != null) {
				if (ok.equals(this.callCenterHistory.getResult())) {
					optionGroupColAction.select(JOBCOMPLETE);
				} else if (ko.equals(this.callCenterHistory.getResult())) {
					optionGroupColAction.select(FOLLOWUP);
				}
			}
			txtRemark.setValue(this.callCenterHistory.getComment());
		}
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		txtRemark.setValue("");
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public ECallCenterResult getCode(String code) {
		return ENTITY_SRV.getByCode(ECallCenterResult.class, code);
	}
	
	/**
	 * 
	 */
	private void saveCallCenterHistory() {
		ECallCenterResult other = getCode(ECallCenterResult.OTHER);
		ECallCenterResult ko = getCode(ECallCenterResult.KO);
		ECallCenterResult ok = getCode(ECallCenterResult.OK);
		if (other != null) {
			if (FOLLOWUP.equals(optionGroupColAction.getValue())) {
				if (ko != null) {
					other = ko;
				}
			} else if (JOBCOMPLETE.equals(optionGroupColAction.getValue())) {
				if (ok != null) {
					other = ok;
				}
			}
		}
		
		this.callCenterHistory.setContract(contract);
		this.callCenterHistory.setResult(other);
		this.callCenterHistory.setComment(txtRemark.getValue());
		try {
			CALL_CTR_SRV.saveOrUpdateCallCenterHistory(UserSessionManager.getCurrentUser(), this.callCenterHistory);
			displaySuccessfullyMsg();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	public void displaySuccessfullyMsg() {
		ComponentLayoutFactory.displaySuccessfullyMsg();
		resetControls();
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton().equals(btnSave)) {
			saveCallCenterHistory();
		}
	}
	
}
