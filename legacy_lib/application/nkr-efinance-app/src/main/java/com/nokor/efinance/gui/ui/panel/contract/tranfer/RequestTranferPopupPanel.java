package com.nokor.efinance.gui.ui.panel.contract.tranfer;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.vaadin.dialogs.ConfirmDialog;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.LockSplit;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.gui.ui.panel.payment.locksplit.LockSplitFormPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;

public class RequestTranferPopupPanel extends Window implements ClickListener, FinServicesHelper, CloseListener {

	private static final long serialVersionUID = -4926574925916579812L;
	
	private LockSplitFormPanel formPanel;
	private Button btnRequest;
	private Button btnCancel;
	
	private Listener listener = null;
	
	public interface Listener extends Serializable {
        void onClose(RequestTranferPopupPanel dialog);
    }
	
	/**
	 * 
	 */
	public RequestTranferPopupPanel() {
		setCaption(I18N.message("request.tranfer"));
		setModal(true);
		super.center();
		
		Button.ClickListener cb = new Button.ClickListener() {
	           private static final long serialVersionUID = 3525060915814334881L;
	           public void buttonClick(ClickEvent event) {
	        	   
	        	   ConfirmDialog.show(UI.getCurrent(), I18N.message("are.you.sure.to.request.tranfer"),
	   					new ConfirmDialog.Listener() {
	        		   		
	        		   		/** */
							private static final long serialVersionUID = -1105685787984679391L;

							/**
	        		   		 * @see org.vaadin.dialogs.ConfirmDialog.Listener#onClose(org.vaadin.dialogs.ConfirmDialog)
	        		   		 */
	        		   		@Override
	   						public void onClose(ConfirmDialog dialog) {
	   							if (dialog.isConfirmed()) {
	   								if (listener != null) {
	   			                     listener.onClose(RequestTranferPopupPanel.this);
	   							}
	   						UI.getCurrent().removeWindow(RequestTranferPopupPanel.this);
	   					} 
	   				}
	   			});
	        }
	    };
		
		formPanel = new LockSplitFormPanel();
		
		btnRequest = ComponentLayoutFactory.getButtonStyle("request", null, 120, "btn btn-success");
		btnRequest.addClickListener(this);
		btnRequest.addClickListener(cb);
		
		btnCancel = ComponentLayoutFactory.getButtonStyle("cancel", FontAwesome.TIMES, 120, "btn btn-success");
		btnCancel.addClickListener(this);
		
		HorizontalLayout buttonLayout = ComponentLayoutFactory.getHorizontalLayout(new MarginInfo(false, true, true, true), true);
		buttonLayout.addComponent(btnRequest);
		buttonLayout.addComponent(btnCancel);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, false);
		mainLayout.addComponent(formPanel);
		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
		setContent(mainLayout);
	}
	
	/**
     * Show a modal ConfirmDialog in a window.
     * 
     * @param parentWindow
     * @param listener
     */
    public static RequestTranferPopupPanel show(final Contract contract, final Listener listener) {    	
    	RequestTranferPopupPanel requestTranferPopupPanel = new RequestTranferPopupPanel();
    	requestTranferPopupPanel.assignValues(contract);
    	requestTranferPopupPanel.listener = listener;
    	UI.getCurrent().addWindow(requestTranferPopupPanel);
        return requestTranferPopupPanel;
    }
	

	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		formPanel.reset();
		BaseRestrictions<LockSplit> restrictions = new BaseRestrictions<>(LockSplit.class);
		restrictions.addCriterion(Restrictions.eq("contract", contract));
		restrictions.addOrder(Order.desc("createDate"));
		List<LockSplit> lockSplits = ENTITY_SRV.list(restrictions);
		if (lockSplits != null && !lockSplits.isEmpty()) {
			formPanel.assignValues(lockSplits.get(0).getId());
		} else {
			formPanel.setContract(contract);
			formPanel.assignValues(null);
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnCancel) {
			close();
		}
	}
	
	/**
	 * @see com.vaadin.ui.Window.CloseListener#windowClose(com.vaadin.ui.Window.CloseEvent)
	 */
	@Override
	public void windowClose(CloseEvent e) {
		UI.getCurrent().removeWindow(this);
	}

}
