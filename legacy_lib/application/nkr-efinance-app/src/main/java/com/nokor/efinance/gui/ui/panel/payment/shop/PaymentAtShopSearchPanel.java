package com.nokor.efinance.gui.ui.panel.payment.shop;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.dao.hql.BaseRestrictions;

import com.nokor.efinance.core.payment.model.PaymentFileItem;
import com.nokor.efinance.core.payment.service.PaymentFileItemRestriction;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

/**
 * 
 * @author uhout.cheng
 */
public class PaymentAtShopSearchPanel extends AbstractSearchPanel<PaymentFileItem> {
	
	/** */
	private static final long serialVersionUID = -2361463435394450945L;
	
	private TextField txtDealerID;
	private TextField txtContractID;
	private AutoDateField dfPaymentDateFrom;
	private AutoDateField dfPaymentDateTo;

	/**
	 * 
	 * @param tablePanel
	 */
	public PaymentAtShopSearchPanel(PaymentAtShopTablePanel tablePanel) {
		super(I18N.message("search"), tablePanel);
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		txtDealerID = ComponentFactory.getTextField(100, 170);
		txtContractID = ComponentFactory.getTextField(100, 170);
		dfPaymentDateFrom = ComponentFactory.getAutoDateField();
		dfPaymentDateTo = ComponentFactory.getAutoDateField();
		
		GridLayout content = new GridLayout(8, 1);
		content.setSpacing(true);
		content.addComponent(ComponentLayoutFactory.getLabelCaption("dealer.id"));
		content.addComponent(txtDealerID);
		content.addComponent(ComponentLayoutFactory.getLabelCaption("contract.id"));
		content.addComponent(txtContractID);
		content.addComponent(ComponentLayoutFactory.getLabelCaption("payment.date.from"));
		content.addComponent(dfPaymentDateFrom);
		content.addComponent(ComponentLayoutFactory.getLabelCaption("to"));
		content.addComponent(dfPaymentDateTo);
		
		return content;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#getRestrictions()
	 */
	@Override
	public BaseRestrictions<PaymentFileItem> getRestrictions() {
		PaymentFileItemRestriction restrictions = new PaymentFileItemRestriction();
		restrictions.setDealerCode(StringUtils.trim(txtDealerID.getValue()));
		restrictions.setContractReference(StringUtils.trim(txtContractID.getValue()));
		restrictions.setPaymentDateFrom(dfPaymentDateFrom.getValue());
		restrictions.setPaymentDateFrom(dfPaymentDateTo.getValue());
		return restrictions;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel#reset()
	 */
	@Override
	protected void reset() {
		txtDealerID.setValue(StringUtils.EMPTY);
		txtContractID.setValue(StringUtils.EMPTY);
		dfPaymentDateFrom.setValue(null);
		dfPaymentDateTo.setValue(null);
	}

}
