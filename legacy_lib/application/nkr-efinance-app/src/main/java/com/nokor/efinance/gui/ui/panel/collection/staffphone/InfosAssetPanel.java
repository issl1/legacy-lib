package com.nokor.efinance.gui.ui.panel.collection.staffphone;

import java.util.Date;

import org.seuksa.frmk.tools.DateUtils;

import com.nokor.efinance.core.asset.model.Asset;
import com.nokor.efinance.core.asset.model.AssetMake;
import com.nokor.efinance.core.asset.model.AssetModel;
import com.nokor.efinance.core.asset.model.AssetRange;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * Contact information for applicant panel in collection phone staff
 * @author uhout.cheng
 */
public class InfosAssetPanel extends AbstractControlPanel {

	/** */
	private static final long serialVersionUID = 7383958395123969142L;

	private Label lblBrandName;
	private Label lblModelName;
	private Label lblSerieName;
	private Label lblCC;;
	private Label lblAOMTaxExpiryDate;
	private Label lblAOMTagExpiryDate;
	
	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = ComponentFactory.getHtmlLabel(null);
		label.setSizeUndefined();
		return label;
	}
	
	/**
	 * 
	 */
	public InfosAssetPanel() {
		setWidth(525, Unit.PIXELS);
		Panel mainPanel = new Panel(getApplicantLayout());
		addComponent(mainPanel);
	}
	
	/**
	 * 
	 * @return
	 */
	private Component getApplicantLayout() {
		lblBrandName = getLabelValue();
		lblModelName = getLabelValue();
		lblSerieName = getLabelValue();
		lblCC = getLabelValue();
		lblAOMTaxExpiryDate = getLabelValue();
		lblAOMTagExpiryDate = getLabelValue();
		
		GridLayout gridLayout = new GridLayout(15, 5);
		gridLayout.setMargin(new MarginInfo(true, false, false, true));
		gridLayout.setSpacing(true);
		
		Label lblBrandTitle = getLabelCaption("asset.make");
		Label lblModelTitle = getLabelCaption("asset.range");
		Label lblSerieTitle = getLabelCaption("asset.model");
		Label lblCCTitle = getLabelCaption("cc");
		Label lblAOMTaxExpiryDateTitle = getLabelCaption("aom.tax.expiry.date");
		Label lblAOMTagExpiryDateTitle = getLabelCaption("aom.tag.expiry.date");
		
		int iCol = 0;
		gridLayout.addComponent(lblBrandTitle, iCol++, 0);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 0);
		gridLayout.addComponent(lblBrandName, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblModelTitle, iCol++, 0);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 0);
		gridLayout.addComponent(lblModelName, iCol++, 0);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 0);
		gridLayout.addComponent(lblSerieTitle, iCol++, 0);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 0);
		gridLayout.addComponent(lblSerieName, iCol++, 0);
		iCol = 0;
		gridLayout.addComponent(lblCCTitle, iCol++, 1);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 1);
		gridLayout.addComponent(lblCC, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblAOMTaxExpiryDateTitle, iCol++, 1);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 1);
		gridLayout.addComponent(lblAOMTaxExpiryDate, iCol++, 1);
		gridLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS), iCol++, 1);
		gridLayout.addComponent(lblAOMTagExpiryDateTitle, iCol++, 1);
		gridLayout.addComponent(getLabelCaption(":"), iCol++, 1);
		gridLayout.addComponent(lblAOMTagExpiryDate, iCol++, 1);
		
		gridLayout.setComponentAlignment(lblBrandTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblModelTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblSerieTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblCCTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblAOMTaxExpiryDateTitle, Alignment.TOP_RIGHT);
		gridLayout.setComponentAlignment(lblAOMTagExpiryDateTitle, Alignment.TOP_RIGHT);
		
		return gridLayout;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private String getDescription(String value) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<b>");
		stringBuffer.append(value == null ? "" : value);
		stringBuffer.append("</b>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param contract
	 */
	public void assignValues(Contract contract) {
		resetControls();
		Asset asset = contract.getAsset();
		AssetModel assetModel = null;
		AssetRange assetRange = null;
		AssetMake assetMake = null;
		if (asset != null) {
			assetModel = asset.getModel();
			if (assetModel != null) {
				assetRange = assetModel.getAssetRange();
				if (assetRange != null) {
					assetMake = assetRange.getAssetMake();
				}
			}
		}
		lblBrandName.setValue(getDescription(assetMake == null ? "" : assetMake.getDescLocale()));
		lblModelName.setValue(getDescription(assetRange == null ? "" : assetRange.getDescLocale()));
		lblSerieName.setValue(getDescription(assetModel == null ? "" : assetModel.getDescLocale()));
		if (assetModel != null) {
			lblCC.setValue(getDescription(assetModel.getEngine() != null ? assetModel.getEngine().getDescLocale() : null));
		}
		// RegistrationBook regBook = ClientRegistration.getRegBookByContractReference(contract.getReference());
		// if (regBook != null) {
		//	lblAOMTaxExpiryDate.setValue(getDescription(getDateFormat(regBook.getTaxExpirationDate())));
		//	lblAOMTagExpiryDate.setValue(getDescription(getDateFormat(regBook.getTagExpirationDate())));
		// }
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String getDateFormat(Date date) {
		String dateFormat = DateUtils.getDateLabel(date, DateUtils.FORMAT_DDMMYYYY_SLASH); 
		return dateFormat != null ? dateFormat : "N/A";
	}
	
	/**
	 * 
	 */
	private void resetControls() {
		lblBrandName.setValue("");
		lblModelName.setValue("");
		lblSerieName.setValue("");
		lblCC.setValue("");
		lblAOMTaxExpiryDate.setValue("");
		lblAOMTagExpiryDate.setValue("");
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabelCaption(String caption) {
		Label label = ComponentFactory.getLabel(caption);
		label.setSizeUndefined();
		return label;
	}
}
