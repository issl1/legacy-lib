package com.nokor.efinance.gui.ui.panel.contract.loan.summary.asset;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.amount.AmountUtils;

import com.gl.finwiz.share.domain.registration.RegistrationBook;
import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.gui.ui.panel.contract.loan.LoanSummaryPanel;
import com.nokor.efinance.third.finwiz.client.reg.ClientRegistration;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;


/**
 * Asset summary in Loan
 * @author uhout.cheng
 */
public class LoanAssetPanel extends AbstractControlPanel implements MAsset {

	/** */
	private static final long serialVersionUID = -4820309365361138669L;

	private static final String TEMPLATE = "loan/loanAssetLayout";
	
	private LoanSummaryPanel loanSummaryPanel;
	private Label lblAssetStatusValue;
	private Label lblManufacturingPriceValue;
	private Label lblMarketPriceValue;
	private Label lblMarketPricePercentageValue;
	private Label lblAssetValue;
	private Label lblAOMTaxExpiryDateValue;
	private Label lblAOMTagExpiryDateValue;
	private Label lblLostDamagedInsuranceExpiryDateValue;
	
	/**
	 * 
	 * @param loanSummaryPanel
	 */
	public LoanAssetPanel(LoanSummaryPanel loanSummaryPanel) {
		this.loanSummaryPanel = loanSummaryPanel;
		init();
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
	private Label getLabel() {
		Label label = ComponentFactory.getLabel(null, ContentMode.HTML);
		label.setSizeUndefined();
		return label;
		
	}
	
	/**
	 * 
	 */
	private void init() {
		lblAssetStatusValue = getLabel();
		lblManufacturingPriceValue = getLabel();
		lblMarketPriceValue = getLabel();
		lblMarketPricePercentageValue = getLabel();
		lblAssetValue = getLabel();
		lblAOMTaxExpiryDateValue = getLabel();
		lblAOMTagExpiryDateValue = getLabel();
		lblLostDamagedInsuranceExpiryDateValue = getLabel();
		
		Panel panel = loanSummaryPanel.getPanelCaptionColor("asset", getCustomLayout(), true);
		addComponent(panel);
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assigValues(Contract contract) {
		reset();
		Asset asset = contract.getAsset();
		if (asset != null) {
			AssetModel assetModel = asset.getModel();
			lblManufacturingPriceValue.setValue(getDescription(AmountUtils.format(asset.getTiAssetPrice())));
			if (assetModel != null) {
				lblAssetStatusValue.setValue(getDescription("N/A"));
				lblMarketPriceValue.setValue(getDescription(AmountUtils.format(0d)));
				lblMarketPricePercentageValue.setValue(getDescription("0%"));
			}
			String assetDetail = StringUtils.EMPTY;
			assetDetail = asset.getBrandDescLocale() + "-" + asset.getModelDescLocale();
			lblAssetValue.setValue(getDescription(assetDetail));
			lblLostDamagedInsuranceExpiryDateValue.setValue(getDescription("N/A"));
		}
		RegistrationBook regBook = ClientRegistration.getRegBookByContractReference(contract.getReference());
		if (regBook != null) {
			lblAOMTagExpiryDateValue.setValue(getDescription(getDateFormat(regBook.getTagExpirationDate())));
			lblAOMTaxExpiryDateValue.setValue(getDescription(getDateFormat(regBook.getTaxExpirationDate())));
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
	 * @return
	 */
	private CustomLayout getCustomLayout() {
		CustomLayout customLayout = loanSummaryPanel.getCustomLayout(TEMPLATE);
		customLayout.addComponent(ComponentFactory.getLabel("asset.status"), "lblAssetStatus");
		customLayout.addComponent(lblAssetStatusValue, "lblAssetStatusValue");
		customLayout.addComponent(ComponentFactory.getLabel("asset"), "lblAsset");
		customLayout.addComponent(lblAssetValue, "lblAssetValue");
		customLayout.addComponent(ComponentFactory.getLabel("manufactury.price"), "lblManufacturyPrice");
		customLayout.addComponent(lblManufacturingPriceValue, "lblManufacturingPriceValue");
		customLayout.addComponent(ComponentFactory.getLabel("market.price.auction"), "lblMarketPrice");
		customLayout.addComponent(lblMarketPriceValue, "lblMarketPriceValue");
		customLayout.addComponent(ComponentFactory.getLabel("market.price.percentage"), "lblMarketPricePercentage");
		customLayout.addComponent(lblMarketPricePercentageValue, "lblMarketPricePercentageValue");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tax.expiry.date"), "lblAOMTaxExpiryDate");
		customLayout.addComponent(lblAOMTaxExpiryDateValue, "lblAOMTaxExpiryDateValue");
		customLayout.addComponent(ComponentFactory.getLabel("aom.tag.expiry.date"), "lblAOMTagExpiryDate");
		customLayout.addComponent(lblAOMTagExpiryDateValue, "lblAOMTagExpiryDateValue");
		customLayout.addComponent(ComponentFactory.getLabel("lost.demaged.insurance.expiry.date"), "lblLostDamagedInsuranceExpiryDate");
		customLayout.addComponent(lblLostDamagedInsuranceExpiryDateValue, "lblLostDamagedInsuranceExpiryDateValue");

		return customLayout;
	}
	
	/**
	 * 
	 */
	protected void reset() {
		lblAssetStatusValue.setValue("");
		lblManufacturingPriceValue.setValue("");
		lblMarketPriceValue.setValue("");
		lblMarketPricePercentageValue.setValue("");
		lblAssetValue.setValue("");
		lblAOMTaxExpiryDateValue.setValue("");
		lblAOMTagExpiryDateValue.setValue("");
		lblLostDamagedInsuranceExpiryDateValue.setValue("");
	}
	
}
