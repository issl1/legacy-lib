package com.nokor.efinance.gui.ui.panel.report.qualityincentive;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * @author buntha.chea
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QualityIncentiveTablePanel extends AbstractTablePanel<Contract> implements FMEntityField {
	
	/** */
	private static final long serialVersionUID = 5204101495689053450L;
	private static final String QUALITY_INCENTIVE = "quality.incentive.data";
	
	/**
	 */
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message(QUALITY_INCENTIVE));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		super.init("");
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Contract> createPagedDataProvider() {
		PagedDefinition<Contract> pagedDefinition = new PagedDefinition<Contract>(searchPanel.getRestrictions());
		
		pagedDefinition.setRowRenderer(new QualityIncentiveRowRenderer());
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 230);
		pagedDefinition.addColumnDefinition(DEALER , I18N.message("dealer"), String.class, Align.LEFT, 230);
		pagedDefinition.addColumnDefinition("term", I18N.message("term"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CONTRACT_STATUS, I18N.message("contract.status"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("contract.start.date", I18N.message("contract.start.date"), Date.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("contract.amount", I18N.message("contract.amount"), Double.class, Align.LEFT, 150);
		
		pagedDefinition.addColumnDefinition("co.name", I18N.message("co.name"), String.class, Align.LEFT, 180);
		pagedDefinition.addColumnDefinition("po.name", I18N.message("po.name"), String.class, Align.LEFT, 180);
		pagedDefinition.addColumnDefinition("co.who.make.field.check", I18N.message("co.who.make.field.check"), String.class, Align.LEFT, 180);
		pagedDefinition.addColumnDefinition("overdue.day", I18N.message("overdue.day"), Integer.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("outstanding", I18N.message("outstanding"), Double.class, Align.LEFT, 180);

		EntityPagedDataProvider<Contract> pagedDataProvider = new EntityPagedDataProvider<Contract>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 *@author buntha.chea
	 */
	private class QualityIncentiveRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.Entity.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Contract contract = (Contract) entity;
			if(contract != null){
				Collection otherData = contract.getCollection();
				Integer nbOverdueDays = otherData != null ? otherData.getNbOverdueInDays() : Integer.valueOf(0);
				Double outStanding = otherData != null ? otherData.getTiBalanceCapital() : Double.valueOf(0.0);
				//Amount outStanding = contractService.getRealOutstanding(DateUtils.todayH00M00S00(), contract.getId());
				
				item.getItemProperty(REFERENCE).setValue(contract.getReference());
				item.getItemProperty(DEALER).setValue(contract.getDealer().getNameEn());
				item.getItemProperty("term").setValue(contract.getTerm());
				if(contract.getWkfStatus() != null){
					item.getItemProperty(CONTRACT_STATUS).setValue(contract.getWkfStatus().getDesc());
				}
							
				item.getItemProperty("contract.start.date").setValue(contract.getStartDate());
				item.getItemProperty("contract.amount").setValue(contract.getTiFinancedAmount());
				
				if(contract.getQuotation() != null){
					if(contract.getQuotation().getCreditOfficer() != null){
						item.getItemProperty("co.name").setValue(contract.getQuotation().getCreditOfficer().getDesc());
					}
					if(contract.getQuotation().getProductionOfficer() != null){
						item.getItemProperty("po.name").setValue(contract.getQuotation().getProductionOfficer().getDesc());
					}
					if(contract.getQuotation().getFieldCheck() != null){
						item.getItemProperty("co.who.make.field.check").setValue(contract.getQuotation().getFieldCheck().getDesc());
					}else if(contract.getQuotation().getCreditOfficer() != null){
						item.getItemProperty("co.who.make.field.check").setValue(contract.getQuotation().getCreditOfficer().getDesc());
					}															
				}
				item.getItemProperty("overdue.day").setValue(nbOverdueDays);
				item.getItemProperty("outstanding").setValue(outStanding);
			}
		
		}
	}
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Contract getEntity() {
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<Contract> createSearchPanel() {
		return new QualityIncentiveSearchPanel(this);
	}
}
