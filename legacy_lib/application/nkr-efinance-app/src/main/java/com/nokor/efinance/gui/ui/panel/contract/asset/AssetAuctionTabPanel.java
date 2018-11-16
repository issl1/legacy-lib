package com.nokor.efinance.gui.ui.panel.contract.asset;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.seuksa.frmk.model.entity.Entity;

import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.component.ComponentLayoutFactory;
import com.nokor.frmk.vaadin.ui.panel.AbstractControlPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.nokor.frmk.vaadin.util.i18n.I18N;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author buntha.chea
 *
 */
public class AssetAuctionTabPanel extends AbstractControlPanel implements MAsset {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5610977173506599799L;
	
	private Label lblCurrentEvaluationPriceValue;
	private Label lblGradeValue;
	private Label lblSellingPriceValue;
	private Label lblBuyerValue;
	private Label lblDateSaleValue;
	private Label lblSaleLocationValue;
	private Label lblPaymentStatusValue;
	private Label lblPaidReminingValue;
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<Entity> simplePagedTable;
	
	public AssetAuctionTabPanel() {
		setMargin(true);
		init();
		
		Label lblCurrentEvaluationPrice = getLabel("current.evaluation.price");
		Label lblGrade = getLabel("grade");
		Label lblSellingPrice = getLabel("selling.price");
		Label lblBuyer = getLabel("buyer");
		Label lblDateSale = getLabel("date.sale");
		Label lblSaleLocation = getLabel("sale.location");
		Label lblPaymentStatus = getLabel("payment.status");
		Label lblPaidRemining = getLabel("paid.remining");
		
		GridLayout auctionGridLayout = ComponentLayoutFactory.getGridLayout(5, 7);
		auctionGridLayout.setSpacing(true);
		
		int iCol = 0;
		auctionGridLayout.addComponent(lblCurrentEvaluationPrice, iCol++, 0);
		auctionGridLayout.addComponent(lblCurrentEvaluationPriceValue, iCol++, 0);
		auctionGridLayout.addComponent(lblGrade, iCol++, 0);
		auctionGridLayout.addComponent(lblGradeValue, iCol++, 0);
		auctionGridLayout.setComponentAlignment(lblGrade, Alignment.TOP_RIGHT);
		auctionGridLayout.setComponentAlignment(lblGradeValue, Alignment.TOP_RIGHT);
		
		iCol = 0;
		auctionGridLayout.addComponent(lblSellingPrice, iCol++, 1);
		auctionGridLayout.addComponent(lblSellingPriceValue, iCol++, 1);
		iCol = 0;
		auctionGridLayout.addComponent(lblBuyer, iCol++, 2);
		auctionGridLayout.addComponent(lblBuyerValue, iCol++, 2);
		iCol = 0;
		auctionGridLayout.addComponent(lblDateSale, iCol++, 3);
		auctionGridLayout.addComponent(lblDateSaleValue, iCol++, 3);
		iCol = 0;
		auctionGridLayout.addComponent(lblSaleLocation, iCol++, 4);
		auctionGridLayout.addComponent(lblSaleLocationValue, iCol++, 4);
		iCol = 0;
		auctionGridLayout.addComponent(lblPaymentStatus, iCol++, 5);
		auctionGridLayout.addComponent(lblPaymentStatusValue, iCol++, 5);
		iCol = 0;
		auctionGridLayout.addComponent(lblPaidRemining, iCol++, 6);
		auctionGridLayout.addComponent(lblPaidReminingValue, iCol++, 6);
		
		VerticalLayout mainLayout = ComponentLayoutFactory.getVerticalLayout(false, true);
		mainLayout.addComponent(auctionGridLayout);
		mainLayout.addComponent(simplePagedTable);
		mainLayout.addComponent(simplePagedTable.createControls());
		simplePagedTable.setPageLength(5);
		
		addComponent(mainLayout);
	}
	
	private void init() {
		lblCurrentEvaluationPriceValue = getLabelValue();
		lblGradeValue = getLabelValue();
		lblSellingPriceValue = getLabelValue();
		lblBuyerValue = getLabelValue();
		lblDateSaleValue = getLabelValue();
		lblSaleLocationValue = getLabelValue();
		lblPaymentStatusValue = getLabelValue();
		lblPaidReminingValue = getLabelValue();
		
		this.columnDefinitions = createColumnDefinition();
		simplePagedTable = new SimplePagedTable<>(this.columnDefinitions);
		simplePagedTable.setCaption(I18N.message("auction.history"));
	}
	

	/**
	 * 
	 * @return
	 */
	private Label getLabelValue() {
		Label label = new Label("", ContentMode.HTML);
		label.setWidthUndefined();
		return label;
	}
	
	/**
	 * 
	 * @param caption
	 * @return
	 */
	private Label getLabel(String caption) {
		return ComponentFactory.getLabel(I18N.message(caption) + StringUtils.SPACE + ":");
	}
	
	/**
	 * 
	 */
	public void assignValue(Contract contract) {
		
	}
	
	/**
	 * 
	 * @return
	 */
	private List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("ref"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(TOTAL_NUMBER_MOTORCYCLES, I18N.message("total.number.motocycles"), String.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition(DATE, I18N.message("date"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(TIME, I18N.message("time"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(LOCATION, I18N.message("location"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(FIXED_PRICE, I18N.message("fixed.price"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(NUMBER_BIDS, I18N.message("number.bids"), String.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition(RESULT, I18N.message("result"), String.class, Align.LEFT, 100));
		return columnDefinitions;
	}

}
