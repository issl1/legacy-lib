package com.nokor.efinance.gui.ui.panel.payment.blocked.filters;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.ERefDataListSelect;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseListener;


/**
 * 
 * @author uhout.cheng
 */
public class BlockedPaymentFilterPopUpPanel extends Window implements ClickListener, CloseListener {

	/** */
	private static final long serialVersionUID = -1780927551688418907L;
	
	private Button btnSave;
	private Button btnReset;
	private Button btnCancel;
	
	private AutoDateField dfUploadDateFrom;
	private AutoDateField dfUploadDateTo;
	private ERefDataListSelect<EPaymentChannel> lstChannel;
	private NumberField txtAmountFrom;
	private NumberField txtAmountTo;
	private TextField txtContractId;
	
	private Listener listener = null;
	
	private EWkfStatus status;
	
	/**
	 * 
	 * @author uhout.cheng
	 *
	 */
	public interface Listener extends Serializable {
        void onClose(BlockedPaymentFilterPopUpPanel dialog, StoreControlBlockPaymentFilter storeControl);
    }
	
	/**
	 * 
	 * @param wkfStatus
	 */
	public BlockedPaymentFilterPopUpPanel(EWkfStatus wkfStatus) {
		status = wkfStatus;
		init(wkfStatus);
	}

	/**
	 * 
	 * @param wkfStatus
	 */
	private void init(EWkfStatus wkfStatus) {
		dfUploadDateFrom = ComponentFactory.getAutoDateField();
		dfUploadDateTo = ComponentFactory.getAutoDateField();
		
		lstChannel = new ERefDataListSelect<EPaymentChannel>(I18N.message("channel"), EPaymentChannel.values());
		lstChannel.setRows(5);
		lstChannel.setMultiSelect(true);
		lstChannel.setImmediate(true);
		lstChannel.setWidth(200, Unit.PIXELS);
		
		txtAmountFrom = ComponentFactory.getNumberField(10, 110);
		txtAmountTo = ComponentFactory.getNumberField(10, 110);
		txtContractId = ComponentFactory.getTextField(20, 170);
		
		btnSave = new NativeButton(I18N.message("save"), this);
		btnSave.setIcon(FontAwesome.SAVE);
		btnReset = new NativeButton(I18N.message("reset"), this);
		btnReset.setIcon(FontAwesome.ERASER);
		btnCancel = new NativeButton(I18N.message("cancel"), this);
		btnCancel.setIcon(FontAwesome.TIMES);
		
		Button.ClickListener cb = new Button.ClickListener() {
	            
	        /** */
			private static final long serialVersionUID = 672284297423816605L;

			public void buttonClick(ClickEvent event) {
				if (listener != null) {
					StoreControlBlockPaymentFilter storeControl = new StoreControlBlockPaymentFilter();
					listener.onClose(BlockedPaymentFilterPopUpPanel.this, storeControl);
				}
				UI.getCurrent().removeWindow(BlockedPaymentFilterPopUpPanel.this);
			}
		};
			
	    btnSave.addClickListener(cb);
			
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnReset);
		navigationPanel.addButton(btnCancel);
		
		Label lblFrom = ComponentFactory.getLabel("upload.date.from");
		Label lblTo = ComponentFactory.getLabel("to");
		Label lblAmtFrom = ComponentFactory.getLabel("amounts.from");
		Label lblAmtTo = ComponentFactory.getLabel("to");
		Label lblContractId = ComponentFactory.getLabel("contract.id");
		
		GridLayout gridLayout = new GridLayout(12, 2);
		gridLayout.setSpacing(true);
		int iCol = 0;
		gridLayout.addComponent(lblFrom, iCol++, 0);
		gridLayout.addComponent(dfUploadDateFrom, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblTo, iCol++, 0);
		gridLayout.addComponent(dfUploadDateTo, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblContractId, iCol++, 0);
		gridLayout.addComponent(txtContractId, iCol++, 0);
		
		iCol = 0;
		gridLayout.addComponent(lblAmtFrom, iCol++, 1);
		gridLayout.addComponent(txtAmountFrom, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblAmtTo, iCol++, 1);
		gridLayout.addComponent(txtAmountTo, iCol++, 1);
		
		gridLayout.setComponentAlignment(lblFrom, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblTo, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAmtFrom, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblAmtTo, Alignment.MIDDLE_LEFT);
		gridLayout.setComponentAlignment(lblContractId, Alignment.MIDDLE_LEFT);
		
		boolean isUnIdentified = !PaymentFileWkfStatus.UNIDENTIFIED.equals(wkfStatus);
		lblAmtFrom.setVisible(isUnIdentified);
		txtAmountFrom.setVisible(isUnIdentified);
		lblAmtTo.setVisible(isUnIdentified);
		txtAmountTo.setVisible(isUnIdentified);
		lblContractId.setVisible(isUnIdentified);
		txtContractId.setVisible(isUnIdentified);
		
		HorizontalLayout searchLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		searchLayout.addComponent(gridLayout);
		searchLayout.addComponent(lstChannel);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainLayout.addComponent(navigationPanel);
		mainLayout.addComponent(ComponentLayoutFactory.setMargin(searchLayout));
		
		setModal(true);
		setCaptionAsHtml(true);
		setCaption(I18N.message("filters"));
		setContent(mainLayout);
	}
	
	/**
	 * 
	 * @param storeControl
	 * @param listener
	 * @return
	 */
	public static BlockedPaymentFilterPopUpPanel show(StoreControlBlockPaymentFilter storeControl, EWkfStatus wkfStatus, final Listener listener) {   	
		BlockedPaymentFilterPopUpPanel paymentFilterPopUpPanel = new BlockedPaymentFilterPopUpPanel(wkfStatus);
	    paymentFilterPopUpPanel.listener = listener;
	    paymentFilterPopUpPanel.assignValue(storeControl);
	    return paymentFilterPopUpPanel;
	}
	
	/**
	 * 
	 */
	public void reset() {
		dfUploadDateFrom.setValue(null);
		dfUploadDateTo.setValue(null);
		txtAmountFrom.setValue(StringUtils.EMPTY);
		txtAmountTo.setValue(StringUtils.EMPTY);
		txtContractId.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * Assign Value to all control in filter
	 * @param storeControl
	 */
	public void assignValue(StoreControlBlockPaymentFilter storeControl) {
		reset();
		if (storeControl != null) {
			lstChannel.setSelectedEntities(storeControl.getChannels());
			dfUploadDateFrom.setValue(storeControl.getFrom());
			dfUploadDateTo.setValue(storeControl.getTo());
			txtAmountFrom.setValue(String.valueOf(storeControl.getAmountFrom()));
			txtAmountTo.setValue(String.valueOf(storeControl.getAmountTo()));
			txtContractId.setValue(storeControl.getContractId());
		}
	}
	
	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnSave) {
			new StoreControlBlockPaymentFilter();
		} else if (event.getButton() == btnReset) {
			reset();
		} else if (event.getButton() == btnCancel) {
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
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	public class StoreControlBlockPaymentFilter implements Serializable {
		
		/** */
		private static final long serialVersionUID = -7643029629177103070L;
		
		private EWkfStatus wkfStatus;
		private List<EPaymentChannel> channels;
		private Date from;
		private Date to;
		private Double amountFrom;
		private Double amountTo;
		private String contractId;
		
		/**
		 * 
		 */
		public StoreControlBlockPaymentFilter() {
			this.wkfStatus = status;
			this.channels = lstChannel.getSelectedEntities();
			this.from = dfUploadDateFrom.getValue();
			this.to = dfUploadDateTo.getValue();
			this.amountFrom = Double.parseDouble(StringUtils.isEmpty(txtAmountFrom.getValue()) ? "0" : txtAmountFrom.getValue());
			this.amountTo = Double.parseDouble(StringUtils.isEmpty(txtAmountTo.getValue()) ? "0" : txtAmountTo.getValue());
			this.contractId = txtContractId.getValue();
		}
		
		/**
		 * @return the wkfStatus
		 */
		public EWkfStatus getWkfStatus() {
			return wkfStatus;
		}
		
		/**
		 * @param wkfStatus the wkfStatus to set
		 */
		public void setWkfStatus(EWkfStatus wkfStatus) {
			this.wkfStatus = wkfStatus;
		}
		
		/**
		 * @return the channels
		 */
		public List<EPaymentChannel> getChannels() {
			return channels;
		}
		
		/**
		 * @param channels the channels to set
		 */
		public void setChannels(List<EPaymentChannel> channels) {
			this.channels = channels;
		}
		
		/**
		 * @return the from
		 */
		public Date getFrom() {
			return from;
		}
		
		/**
		 * @param from the from to set
		 */
		public void setFrom(Date from) {
			this.from = from;
		}
		
		/**
		 * @return the to
		 */
		public Date getTo() {
			return to;
		}
		
		/**
		 * @param to the to to set
		 */
		public void setTo(Date to) {
			this.to = to;
		}
		
		/**
		 * @return the amountFrom
		 */
		public Double getAmountFrom() {
			return amountFrom;
		}
		
		/**
		 * @param amountFrom the amountFrom to set
		 */
		public void setAmountFrom(Double amountFrom) {
			this.amountFrom = amountFrom;
		}
		
		/**
		 * @return the amountTo
		 */
		public Double getAmountTo() {
			return amountTo;
		}
		
		/**
		 * @param amountTo the amountTo to set
		 */
		public void setAmountTo(Double amountTo) {
			this.amountTo = amountTo;
		}
		
		/**
		 * @return the contractId
		 */
		public String getContractId() {
			return contractId;
		}
		
		/**
		 * @param contractId the contractId to set
		 */
		public void setContractId(String contractId) {
			this.contractId = contractId;
		}
	}
}
