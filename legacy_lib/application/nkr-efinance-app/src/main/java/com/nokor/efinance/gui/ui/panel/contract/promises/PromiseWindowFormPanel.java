package com.nokor.efinance.gui.ui.panel.contract.promises;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.efinance.core.collection.model.EPromiseStatus;
import com.nokor.efinance.core.collection.model.EPromiseType;
import com.nokor.efinance.core.contract.model.ContractPromise;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.util.ValidateUtil;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class PromiseWindowFormPanel extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = -9114028094653655540L;

	private ERefDataComboBox<EPromiseType> cbxPromiseType;
	private AutoDateField dfPromiseDate;
	private TextField txtAmount;
	private TextArea txtRemark;
	private VerticalLayout messagePanel;
	
	/**
	 * 
	 * @param promiseTablePanel
	 * @param promise
	 */
	public PromiseWindowFormPanel(PromiseTablePanel promiseTablePanel, ContractPromise promise) {
		setModal(true);
		setResizable(false);
		setCaption(I18N.message("promise"));
		
		cbxPromiseType = new ERefDataComboBox<EPromiseType>(EPromiseType.values());
		cbxPromiseType.setWidth(150, Unit.PIXELS);
		
		dfPromiseDate = ComponentFactory.getAutoDateField();
		dfPromiseDate.setValue(DateUtils.today());
		
		txtAmount = ComponentFactory.getTextField(30, 150);
		
		txtRemark = ComponentFactory.getTextArea(false, 200, 60);
		
		if (promise.getId() != null) {
			cbxPromiseType.setSelectedEntity(promise.getPromiseType());
			txtAmount.setValue(AmountUtils.format(MyNumberUtils.getDouble(promise.getPromiseAmount())));
			dfPromiseDate.setValue(promise.getPromiseDate());
			txtRemark.setValue(promise.getRemark() == null ? StringUtils.EMPTY : promise.getRemark());
		}
		
		Button btnSave = new NativeButton(I18N.message("save"), new Button.ClickListener() {
			
			/** */
			private static final long serialVersionUID = 8249544310796151326L;

			/**
        	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
        	 */
        	@Override
			public void buttonClick(ClickEvent event) {
        		if (validate()) {
        			Date today = DateUtils.todayH00M00S00();
        			promise.setPromiseType(cbxPromiseType.getSelectedEntity());
        			if (today.before(dfPromiseDate.getValue())) {
        				promise.setPromiseStatus(EPromiseStatus.PENDING);
        			} else {
        				promise.setPromiseStatus(EPromiseStatus.BROKEN);
        			}
        			promise.setPromiseAmount(MyNumberUtils.getDouble(txtAmount.getValue(), 0d));
        			promise.setPromiseDate(dfPromiseDate.getValue());
        			promise.setRemark(txtRemark.getValue());
        			if (promise.getId() == null) {
        				SecUser userLogin = UserSessionManager.getCurrentUser();
        				promise.setCreatedBy(userLogin.getDesc());
        			}
            		try {
            			ENTITY_SRV.saveOrUpdate(promise);
                		ComponentLayoutFactory.displaySuccessfullyMsg();
                		close();
                		promiseTablePanel.assignValues(promise.getContract());
					} catch (Exception e) {
						e.printStackTrace();
						ValidateUtil.addError(I18N.message("msg.error.technical"));
						displayAllErrorsPanel();
					}
        		} else {
        			displayAllErrorsPanel();
        		}
        	}
        });
		btnSave.setIcon(FontAwesome.SAVE);
		
		Button btnCancel = new NativeButton(I18N.message("close"), new Button.ClickListener() {

			/** */
			private static final long serialVersionUID = -2273567024800197282L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
            	close();
            }
        });
		btnCancel.setIcon(FontAwesome.BAN);
		
		VerticalLayout contentLayout = new VerticalLayout(); 
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		GridLayout gridLayout = new GridLayout(2, 4);
		gridLayout.setSpacing(true);
		gridLayout.setMargin(true);
		
		Label lblPromiseType = ComponentLayoutFactory.getLabelCaptionRequired("promise.type");
		Label lblAmount = ComponentLayoutFactory.getLabelCaptionRequired("amount");
		Label lblPromiseDate = ComponentLayoutFactory.getLabelCaptionRequired("date");
		Label lblRemark = ComponentFactory.getHtmlLabel(I18N.message("reason") + StringUtils.SPACE + "<b style=\"color: red\">*</b>");
		gridLayout.addComponent(lblPromiseType);
		gridLayout.addComponent(cbxPromiseType);
		gridLayout.addComponent(lblAmount);
		gridLayout.addComponent(txtAmount);
		gridLayout.addComponent(lblPromiseDate);
		gridLayout.addComponent(dfPromiseDate);
		gridLayout.addComponent(lblRemark);
		gridLayout.addComponent(txtRemark);
		
		gridLayout.setComponentAlignment(lblRemark, Alignment.MIDDLE_LEFT);
		
		messagePanel = new VerticalLayout();
		messagePanel.setMargin(true);
		messagePanel.setVisible(false);
		messagePanel.addStyleName("message");
		
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(messagePanel);
		contentLayout.addComponent(gridLayout);
        
        setContent(contentLayout);
	}
	
	/**
	 * Display error message panel
	 */
	private void displayAllErrorsPanel() {
		messagePanel.removeAllComponents();
		Label messageLabel = ComponentFactory.getHtmlLabel(ValidateUtil.getErrorMessages());
		messageLabel.addStyleName("error");
		messagePanel.addComponent(messageLabel);
		messagePanel.setVisible(true);
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean validate() {
		ValidateUtil.clearErrors();
		ValidateUtil.checkMandatorySelectField(cbxPromiseType, "promise.type");
		ValidateUtil.checkMandatoryField(txtAmount, "amount");
		ValidateUtil.checkDoubleField(txtAmount, "amount");
		ValidateUtil.checkMandatoryDateField(dfPromiseDate, "date");
		ValidateUtil.checkMandatoryField(txtRemark, "remark");
		return StringUtils.isEmpty(ValidateUtil.getErrorMessages());
	}
	
}
