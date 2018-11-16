package com.nokor.efinance.gui.ui.panel.report.adjustment;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaidOffAdjustmentReportTablePanel extends AbstractTablePanel<Contract> implements ContractEntityField {
		
	private static final long serialVersionUID = 4888877705364229344L;
	
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("paid.off.adjustments"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("paid.off.adjustments"));

	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Contract> createPagedDataProvider() {
		
		PagedDefinition<Contract> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new ContractRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition("contract.reference", I18N.message("contract"), String.class, Align.LEFT, 160);
		pagedDefinition.addColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 180);
		pagedDefinition.addColumnDefinition("loan.amount", I18N.message("loan.amount"), Amount.class, Align.RIGHT, 120);
		pagedDefinition.addColumnDefinition("adjustment.amount", I18N.message("adjustment.amount"), Amount.class, Align.RIGHT, 120);
		
		EntityPagedDataProvider<Contract> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	
	@Override
	protected PaidOffAdjustmentReportSearchPanel createSearchPanel() {
		return new PaidOffAdjustmentReportSearchPanel(this);		
	}
		
	/**
	 * @author ly.youhort
	 */
	private class ContractRowRenderer implements RowRenderer, ContractEntityField {

		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Contract contract = (Contract) entity;
			item.getItemProperty(ID).setValue(contract.getId());
			item.getItemProperty("contract.reference").setValue(contract.getReference());
			item.getItemProperty(LAST_NAME_EN).setValue(contract.getApplicant().getIndividual().getLastNameEn());
			item.getItemProperty(FIRST_NAME_EN).setValue(contract.getApplicant().getIndividual().getFirstNameEn());
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(contract.getDealer().getNameEn());
			item.getItemProperty("loan.amount").setValue(AmountUtils.convertToAmount(contract.getTiFinancedAmount()));
			item.getItemProperty("adjustment.amount").setValue(AmountUtils.convertToAmount(contract.getContractAdjustment().getTiAdjustmentInterest()));
		}
	}

	@Override
	protected Contract getEntity() {
		return null;
	}
}
