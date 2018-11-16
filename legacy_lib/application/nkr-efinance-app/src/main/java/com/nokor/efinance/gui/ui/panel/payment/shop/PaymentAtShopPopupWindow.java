package com.nokor.efinance.gui.ui.panel.payment.shop;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.service.DealerRestriction;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author uhout.cheng
 */
public class PaymentAtShopPopupWindow extends Window implements FinServicesHelper {

	/** */
	private static final long serialVersionUID = 2891421290200863422L;

	private final static Logger logger = LoggerFactory.getLogger(PaymentAtShopPopupWindow.class);

	private PaymentFileItem paymentFileItem;
	
	private DealerComboBox cbxDealer;
	private TextField txtDealerID;
	private TextField txtContractID;
	private AutoDateField dfPaymentDate;
	private TextField txtAmount;
	
	private PaymentAtShopTablePanel tablePanel;
	
	/**
	 * 
	 * @param tablePanel
	 */
	public PaymentAtShopPopupWindow(PaymentAtShopTablePanel tablePanel) {
		this.tablePanel = tablePanel;
		setModal(true);		
		setCaption(I18N.message("payment.at.shop"));
		
		cbxDealer = new DealerComboBox(I18N.message("dealer"), DEA_SRV.list(new DealerRestriction()));
		cbxDealer.setWidth(180, Unit.PIXELS);
		cbxDealer.setImmediate(true);
		
		txtDealerID = ComponentFactory.getTextField("dealer.id", false, 150, 180);
		txtContractID = ComponentFactory.getTextField("contract.id", false, 150, 180);
		txtAmount = ComponentFactory.getTextField("amount", false, 50, 180);
		
		dfPaymentDate = ComponentFactory.getAutoDateField("payment.date", false);
		
		Button btnSave = new NativeButton(I18N.message("save"));
		btnSave.setIcon(FontAwesome.SAVE);
		Button btnCancel = new NativeButton(I18N.message("cancel"));
		btnCancel.setIcon(FontAwesome.BAN);
		
		cbxDealer.addValueChangeListener(new ValueChangeListener() {
			
			/** */
			private static final long serialVersionUID = 6240365509931863206L;

			/**
			 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbxDealer.getSelectedEntity() != null) {
					txtDealerID.setValue(cbxDealer.getSelectedEntity().getCode());
				} else {
					txtDealerID.setValue(StringUtils.EMPTY);
				}
			}
		});
		
		btnSave.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = 4131525901685614595L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if (StringUtils.isEmpty(checkDoubleField())) {
						// update payment file item information
						ENTITY_SRV.saveOrUpdate(getEntity());
						ComponentLayoutFactory.displaySuccessfullyMsg();
						refresh();
					} else {
						ComponentLayoutFactory.displayErrorMsg(checkDoubleField());
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
		
		btnCancel.addClickListener(new ClickListener() {
		
			/** */
			private static final long serialVersionUID = -6882253476186663374L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		FormLayout frmLayout = ComponentLayoutFactory.getFormLayoutCaptionAlignLeft(true);
		frmLayout.addComponent(cbxDealer);
		frmLayout.addComponent(txtDealerID);
		frmLayout.addComponent(txtContractID);
		frmLayout.addComponent(dfPaymentDate);
		frmLayout.addComponent(txtAmount);
		
		NavigationPanel navigationPanel = new NavigationPanel();
		navigationPanel.addButton(btnSave);
		navigationPanel.addButton(btnCancel);
		
		VerticalLayout contentLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		contentLayout.addComponent(navigationPanel);
		contentLayout.addComponent(ComponentLayoutFactory.getVerticalLayout(new MarginInfo(false, false, true, false), false, frmLayout));
		setContent(contentLayout);
	}
	
	/**
	 * 
	 * @return
	 */
	private PaymentFileItem getEntity() {
		this.paymentFileItem.setDealerNo(txtDealerID.getValue());
		this.paymentFileItem.setCustomerRef1(txtContractID.getValue());
		this.paymentFileItem.setPaymentDate(dfPaymentDate.getValue());
		this.paymentFileItem.setAmount(Double.parseDouble(StringUtils.isEmpty(txtAmount.getValue()) ? "0.0" : txtAmount.getValue()));
		return this.paymentFileItem;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void assignValues(Long id) {
		reset();
		if (id != null) {
			this.paymentFileItem = ENTITY_SRV.getById(PaymentFileItem.class, id);
			Dealer dealer = null;
			txtDealerID.setValue(this.paymentFileItem.getDealerNo());
			if (StringUtils.isNotEmpty(txtDealerID.getValue())) {
				DealerRestriction restrictions = new DealerRestriction();
				restrictions.setInternalCode(txtDealerID.getValue());
				dealer = ENTITY_SRV.list(restrictions) != null ? ENTITY_SRV.list(restrictions).get(0) : null;
			}
			cbxDealer.setSelectedEntity(dealer);
			txtContractID.setValue(this.paymentFileItem.getCustomerRef1());
			dfPaymentDate.setValue(this.paymentFileItem.getPaymentDate());
			txtAmount.setValue(MyNumberUtils.formatDoubleToString(MyNumberUtils.getDouble(this.paymentFileItem.getAmount())));
		}
	}
	
	/**
	 * 
	 */
	public void reset() {
		this.paymentFileItem = PaymentFileItem.createInstance();
		cbxDealer.setSelectedEntity(null);
		txtDealerID.setValue(StringUtils.EMPTY);
		txtContractID.setValue(StringUtils.EMPTY);
		dfPaymentDate.setValue(null);
		txtAmount.setValue(StringUtils.EMPTY);
	}
	
	/**
	 * 
	 * @return
	 */
	private String checkDoubleField() {
		if (StringUtils.isNotEmpty(txtAmount.getValue())) {
			try {
				Double.parseDouble(txtAmount.getValue());
			} catch (Exception e) {
				return I18N.message("field.value.incorrect.1", I18N.message("amount"));
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * 
	 */
	public void refresh() {
		tablePanel.refresh();
		close();
	}

}
