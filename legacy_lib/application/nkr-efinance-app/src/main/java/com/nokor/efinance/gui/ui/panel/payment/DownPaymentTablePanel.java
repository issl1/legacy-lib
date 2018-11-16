package com.nokor.efinance.gui.ui.panel.payment;

import java.util.List;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.MyNumberUtils;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationService;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DownPaymentTablePanel extends AbstractTablePanel<Quotation> implements QuotationEntityField {

	private static final long serialVersionUID = -3673659939697073593L;
	private static final String DEALER_TYPE = "dealer.type";

	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("down.payments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("down.payments"));
		
		NavigationPanel navigationPanel = addNavigationPanel();	
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
	}	
	
	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Quotation> createPagedDataProvider() {
		
		PagedDefinition<Quotation> pagedDefinition = new PagedDefinition<Quotation>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new QuotationRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER_TYPE, I18N.message("dealer.type"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("motor.model", I18N.message("motor.model"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("year", I18N.message("year"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("color", I18N.message("color"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("motor.price", I18N.message("motor.price"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("advance.payment.percentage", I18N.message("advance.payment.percentage"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("advance.payment", I18N.message("advance.payment"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("insurance.fee", I18N.message("insurance.fee"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("servicing.fee", I18N.message("servicing.fee"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("registration.fee", I18N.message("registration.fee"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("total.payment", I18N.message("total.payment"), Amount.class, Align.RIGHT, 90);
		pagedDefinition.addColumnDefinition("second.payment", I18N.message("second.payment"), Amount.class, Align.RIGHT, 120);
		
		EntityPagedDataProvider<Quotation> pagedDataProvider = new EntityPagedDataProvider<Quotation>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Quotation getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Quotation.class, id);
		}
		return null;
	}
	
	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#deleteEntity(org.seuksa.frmk.model.entity.Entity)
	 */
	@Override
	protected void deleteEntity(Entity entity) {
		ENTITY_SRV.changeStatusRecord((Payment) entity, EStatusRecord.RECYC);
	}
	
	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected DownPaymentSearchPanel createSearchPanel() {
		return new DownPaymentSearchPanel(this);		
	}
	
	private class QuotationRowRenderer implements RowRenderer, QuotationEntityField {

		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Quotation quotation = (Quotation) entity;
			Double tiRegistrationFeeUsd = getService("REGFEE", quotation.getQuotationServices());
			Double tiInsuranceFeeUsd = getService("INSFEE", quotation.getQuotationServices());
			Double tiServicingFeeUsd = getService("SERFEE", quotation.getQuotationServices());
			double tiTotalPaymentUsd = MyNumberUtils.getDouble(quotation.getTiAdvancePaymentAmount()) 
					+ MyNumberUtils.getDouble(tiInsuranceFeeUsd)
					+ MyNumberUtils.getDouble(tiServicingFeeUsd) 
					+ MyNumberUtils.getDouble(tiRegistrationFeeUsd);
			item.getItemProperty(ID).setValue(quotation.getId());
			item.getItemProperty(LAST_NAME_EN).setValue(quotation.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(quotation.getApplicant().getIndividual().getFirstNameEn());
			item.getItemProperty(DEALER_TYPE).setValue(quotation.getDealer() != null ? quotation.getDealer().getDealerType().getDesc() : "");
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(quotation.getDealer().getNameEn());
			item.getItemProperty("motor.model").setValue(quotation.getAsset().getDescEn());
			item.getItemProperty("year").setValue(quotation.getAsset().getYear().toString());
			item.getItemProperty("color").setValue(quotation.getAsset().getColor().getDescEn());
			item.getItemProperty("motor.price").setValue(AmountUtils.convertToAmount(quotation.getAsset().getTiAssetPrice()));
			item.getItemProperty("advance.payment.percentage").setValue(AmountUtils.convertToAmount(quotation.getAdvancePaymentPercentage()));
			item.getItemProperty("advance.payment").setValue(AmountUtils.convertToAmount(quotation.getTiAdvancePaymentAmount()));
			item.getItemProperty("insurance.fee").setValue(AmountUtils.convertToAmount(tiInsuranceFeeUsd));
			item.getItemProperty("servicing.fee").setValue(AmountUtils.convertToAmount(tiServicingFeeUsd));
			item.getItemProperty("registration.fee").setValue(AmountUtils.convertToAmount(tiRegistrationFeeUsd));
			item.getItemProperty("total.payment").setValue(AmountUtils.convertToAmount(tiTotalPaymentUsd));
			item.getItemProperty("second.payment").setValue(AmountUtils.convertToAmount(quotation.getTiFinanceAmount()));
		}
	}
	
	/**
	 * @param code
	 * @param quotationServices
	 * @return
	 */
	private Double getService(String code, List<QuotationService> quotationServices) {
		for (QuotationService quotationService : quotationServices) {
			if (code.equals(quotationService.getService().getCode()) && !quotationService.isSplitWithInstallment()) {
				return quotationService.getTiPrice();
			}
		}
		return null;
	}
	
}
