package com.nokor.efinance.gui.ui.panel.contract.asset.supplier;

import org.apache.commons.lang3.StringUtils;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.frmk.vaadin.ui.layout.LayoutHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


/**
 * Supplier tab panel in Asset
 * @author uhout.cheng
 */
public class AssetSupplierPanel extends AbstractTabPanel {

	/** */
	private static final long serialVersionUID = -8032156019536614041L;
	private static final String TEMPLATE = "asset/assetSupplierTopLayout";
	
	private Label lblID;
	private Label lblType;
	private Label lblDealerShopName;
	private Label lblCommercialNO;
	private SupplierContactDetailPanel supplierContactDetailPanel;
	private SupplierPaymentDetailPanel supplierPaymentDetailPanel;

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected Component createForm() {
		lblID = getLabelValue();
		lblType = getLabelValue();
		lblDealerShopName = getLabelValue();
		lblCommercialNO = getLabelValue();
		supplierContactDetailPanel = new SupplierContactDetailPanel();
		supplierPaymentDetailPanel = new SupplierPaymentDetailPanel();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(getTopLayout());
		contentLayout.addComponent(supplierContactDetailPanel);
		contentLayout.addComponent(supplierPaymentDetailPanel);
		
		return contentLayout;
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
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		return label;
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		Dealer dealer = contract.getDealer();
		if (dealer != null) {
			lblID.setValue(getDescription(dealer.getCode()));
			lblType.setValue(getDescription(dealer.getParent() == null ? EDealerType.BRANCH.getDescLocale() : EDealerType.HEAD.getDescLocale()));
			lblDealerShopName.setValue(getDescription(dealer.getNameLocale()));
			lblCommercialNO.setValue(getDescription(dealer.getLicenceNo()));
			supplierContactDetailPanel.assignValues(dealer);
			supplierPaymentDetailPanel.assignValues(dealer);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private Panel getTopLayout() {
		CustomLayout customLayout = LayoutHelper.createCustomLayout(TEMPLATE);
		if (customLayout == null) {
			Notification.show("Could not locate template " + LayoutHelper.getTemplateFullPath(TEMPLATE), Type.ERROR_MESSAGE);
		}
		customLayout.addComponent(ComponentFactory.getLabel("id"), "lblID");
		customLayout.addComponent(lblID, "lblIDValue");
		customLayout.addComponent(ComponentFactory.getLabel("type"), "lblType");
		customLayout.addComponent(lblType, "lblTypeValue");
		customLayout.addComponent(ComponentFactory.getLabel("dealer.shop.name"), "lblDealershopName");
		customLayout.addComponent(lblDealerShopName, "lblDealershopNameValue");
		customLayout.addComponent(ComponentFactory.getLabel("commercial.no"), "lblCommercialNO");
		customLayout.addComponent(lblCommercialNO, "lblCommercialNOValue");
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setMargin(true);
		horLayout.addComponent(customLayout);
		Panel panel = new Panel();
		panel.setContent(horLayout);
		return panel;
	}
	
	/**
	 * 
	 */
	public void reset() {
		lblID.setValue(StringUtils.EMPTY);
		lblType.setValue(StringUtils.EMPTY);
		lblDealerShopName.setValue(StringUtils.EMPTY);
		lblCommercialNO.setValue(StringUtils.EMPTY);
		supplierContactDetailPanel.reset();
	}

}
