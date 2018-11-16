package com.nokor.efinance.gui.ui.panel.payment.blocked.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.collection.model.EPaymentChannel;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.workflow.PaymentFileWkfStatus;
import com.nokor.efinance.gui.ui.panel.payment.blocked.BlockedPaymentDetailTablePanel;
import com.nokor.efinance.gui.ui.panel.payment.blocked.filters.BlockedPaymentFilterPopUpPanel.StoreControlBlockPaymentFilter;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * 
 * @author uhout.cheng
 */
public abstract class AbstractBlockedPaymentFilterPanel extends VerticalLayout implements ClickListener {

	/** */
	private static final long serialVersionUID = 4875330491169794786L;

	private Button btnUpdateFilter;
	
	protected StoreControlBlockPaymentFilter storeControlFilter;
	
	private BlockedPaymentDetailTablePanel tablePanel;
	private EWkfStatus wkfStatus;

	private Label lblFromValue;
	private Label lblToValue;
	private Label lblChannelsValue;
	private Label lblAmountFromValue;
	private Label lblAmountToValue;
	private Label lblContractIdValue;
	
	/**
	 * 
	 * @param tablePanel
	 * @param wkfStatus
	 */
	public AbstractBlockedPaymentFilterPanel(BlockedPaymentDetailTablePanel tablePanel, EWkfStatus wkfStatus) {
		this.tablePanel = tablePanel;
		this.wkfStatus = wkfStatus;
		btnUpdateFilter = ComponentLayoutFactory.getDefaultButton("update", FontAwesome.EDIT, 70);
		btnUpdateFilter.addClickListener(this);
		
		Label lblUploadDateFrom = ComponentFactory.getLabel(I18N.message("upload.date.from") + " : ");
		Label lblUploadDateTo = ComponentFactory.getLabel(I18N.message("to") + " : ");
		Label lblChannel = ComponentFactory.getLabel(I18N.message("channels") + " : ");
		Label lblAmountFrom = ComponentFactory.getLabel(I18N.message("amounts.from") + " : ");
		Label lblAmountTo = ComponentFactory.getLabel(I18N.message("to") + " : ");
		Label lblContractId = ComponentFactory.getLabel(I18N.message("contract.id") + " : ");
	
		lblFromValue = ComponentFactory.getHtmlLabel(null);
		lblToValue = ComponentFactory.getHtmlLabel(null);
		lblChannelsValue = ComponentFactory.getHtmlLabel(null);
		lblAmountFromValue = ComponentFactory.getHtmlLabel(null);
		lblAmountToValue = ComponentFactory.getHtmlLabel(null);
		lblContractIdValue = ComponentFactory.getHtmlLabel(null);
		
		HorizontalLayout horLayout = ComponentLayoutFactory.getHorizontalLayout(false, true);
		horLayout.addComponent(btnUpdateFilter);
		horLayout.addComponent(lblUploadDateFrom);
		horLayout.addComponent(lblFromValue);
		horLayout.addComponent(lblUploadDateTo);
		horLayout.addComponent(lblToValue);
		horLayout.addComponent(lblChannel);
		horLayout.addComponent(lblChannelsValue);
		
		horLayout.setComponentAlignment(lblUploadDateFrom, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblFromValue, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblUploadDateTo, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblToValue, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblChannel, Alignment.MIDDLE_LEFT);
		horLayout.setComponentAlignment(lblChannelsValue, Alignment.MIDDLE_LEFT);
		
		if (!PaymentFileWkfStatus.UNIDENTIFIED.equals(wkfStatus)) {
			horLayout.addComponent(lblAmountFrom);
			horLayout.addComponent(lblAmountFromValue);
			horLayout.addComponent(lblAmountTo);
			horLayout.addComponent(lblAmountToValue);
			horLayout.addComponent(lblContractId);
			horLayout.addComponent(lblContractIdValue);
			
			horLayout.setComponentAlignment(lblAmountFrom, Alignment.MIDDLE_LEFT);
			horLayout.setComponentAlignment(lblAmountFromValue, Alignment.MIDDLE_LEFT);
			horLayout.setComponentAlignment(lblAmountTo, Alignment.MIDDLE_LEFT);
			horLayout.setComponentAlignment(lblAmountToValue, Alignment.MIDDLE_LEFT);
			horLayout.setComponentAlignment(lblContractId, Alignment.MIDDLE_LEFT);
			horLayout.setComponentAlignment(lblContractIdValue, Alignment.MIDDLE_LEFT);
		}
		
		Panel filterPanel = new Panel();
		filterPanel.setCaption("<b style=\"color:#3B5998\">" + I18N.message("current.filters") + "</b>");
		filterPanel.setContent(ComponentLayoutFactory.setMargin(horLayout));
		
		storeControlFilter = null;
		assignDefaultValue(wkfStatus);
		
		addComponent(filterPanel);
	}
	
	/**
	 * 
	 * @param wkfStatus
	 */
	private void assignDefaultValue(EWkfStatus wkfStatus) {
		PaymentFileItemRestriction restrictions = new PaymentFileItemRestriction();
		restrictions.setWkfStatuses(new EWkfStatus[] { wkfStatus });
		tablePanel.refresh(restrictions);
	}

	/**
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == btnUpdateFilter) {
			BlockedPaymentFilterPopUpPanel window = BlockedPaymentFilterPopUpPanel.show(storeControlFilter, wkfStatus, new BlockedPaymentFilterPopUpPanel.Listener() {
				
				/** */
				private static final long serialVersionUID = 7868677371822057725L;

				/**
				 * @see com.nokor.efinance.gui.ui.panel.payment.blocked.filters.BlockedPaymentFilterPopUpPanel.Listener#onClose(com.nokor.efinance.gui.ui.panel.payment.blocked.filters.BlockedPaymentFilterPopUpPanel, com.nokor.efinance.gui.ui.panel.payment.blocked.filters.BlockedPaymentFilterPopUpPanel.StoreControlBlockPaymentFilter)
				 */
				@Override
				public void onClose(BlockedPaymentFilterPopUpPanel dialog, StoreControlBlockPaymentFilter storeControl) {
					storeControlFilter = storeControl;
					reset();
					List<String> channelDescriptions = new ArrayList<String>();
					if (storeControl != null) {
						if (storeControl.getChannels() != null) {
							for (EPaymentChannel channel : storeControl.getChannels()) {
								channelDescriptions.add(channel.getCode());
							}
						}
						lblFromValue.setValue("<b>" + getDateFormat(storeControl.getFrom()) + "</b>");
						lblToValue.setValue("<b>" + getDateFormat(storeControl.getTo()) + "</b>");
						lblChannelsValue.setValue("<b>" + StringUtils.join(channelDescriptions, "/") + "</b>");
						lblAmountFromValue.setValue("<b>" + AmountUtils.format(storeControl.getAmountFrom()) + "</b>");
						lblAmountToValue.setValue("<b>" + AmountUtils.format(storeControl.getAmountTo()) + "</b>");
						lblContractIdValue.setValue("<b>" + storeControl.getContractId() + "</b>");
					}
					tablePanel.refresh(getRestrictions());
				}
			});
			UI.getCurrent().addWindow(window);
		}
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
	private void reset() {
		lblFromValue.setValue(StringUtils.EMPTY);
		lblToValue.setValue(StringUtils.EMPTY);
		lblChannelsValue.setValue(StringUtils.EMPTY);
		lblAmountFromValue.setValue(StringUtils.EMPTY);
		lblAmountToValue.setValue(StringUtils.EMPTY);
		lblContractIdValue.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract PaymentFileItemRestriction getRestrictions();

}
