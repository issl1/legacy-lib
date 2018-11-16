package com.nokor.efinance.gui.ui.panel.collection.searchcontract;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.tools.amount.Amount;
import org.seuksa.frmk.tools.amount.AmountUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.applicant.model.Individual;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.quotation.model.Quotation;
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
 * Search contract table panel in collection
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollectionSearchContractTablePanel extends AbstractTablePanel<Quotation> implements QuotationEntityField {
	
	/** */
	private static final long serialVersionUID = -2983055467054135680L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("search.contract"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		init(I18N.message("search"));	
		NavigationPanel navigationPanel = addNavigationPanel();
    	navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Quotation> createPagedDataProvider() {
		PagedDefinition<Quotation> pagedDefinition = new PagedDefinition<Quotation>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new SearchContractRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60, false);
		pagedDefinition.addColumnDefinition("customer.id", I18N.message("customer.id"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("contract.id", I18N.message("contract.id"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("applicant.id", I18N.message("applicant.id"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("contract.status", I18N.message("contract.status"), String.class, Align.LEFT, 145);
		pagedDefinition.addColumnDefinition("firstname", I18N.message("firstname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("lastname", I18N.message("lastname.en"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("nickname", I18N.message("nickname"), String.class, Align.LEFT, 110);
		pagedDefinition.addColumnDefinition("dob", I18N.message("dob"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("date.approval", I18N.message("date.approval"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("dealer.name", I18N.message("dealer.name"), String.class, Align.LEFT, 90);
		pagedDefinition.addColumnDefinition("installment", I18N.message("installment"), Amount.class, Align.LEFT, 90);
		EntityPagedDataProvider<Quotation> pagedDataProvider = new EntityPagedDataProvider<Quotation>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}		
	
	/**
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
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected CollectionContractSearchPanel createSearchPanel() {
		return new CollectionContractSearchPanel(this);
	}

	/**
	 * 
	 * @author uhout.cheng
	 */
	private class SearchContractRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Quotation quotation = (Quotation) entity;
			Contract contract = quotation.getContract();
			Applicant applicant = quotation.getApplicant();
			Individual individual = applicant.getIndividual();
			
			item.getItemProperty(ID).setValue(quotation.getId());
			if (contract != null) {
				item.getItemProperty("contract.id").setValue(contract.getReference());
				item.getItemProperty("contract.status").setValue(contract.getWkfStatus().getDescEn());
				
				item.getItemProperty("first.installment.date").setValue(contract.getFirstDueDate());
				item.getItemProperty("contact.no").setValue(contract.getReference());
			}
			if (applicant != null) {
				item.getItemProperty("customer.id").setValue(individual.getReference());
				item.getItemProperty("firstname").setValue(individual.getFirstNameEn());
				item.getItemProperty("lastname").setValue(individual.getLastNameEn());
				item.getItemProperty("nickname").setValue(individual.getNickName());
				item.getItemProperty("dob").setValue(individual.getBirthDate());
			}
			item.getItemProperty("applicant.id").setValue(quotation.getReference());	
			item.getItemProperty("date.approval").setValue(quotation.getAcceptationDate());
			item.getItemProperty("dealer.name").setValue(quotation.getDealer().getNameEn());	
			item.getItemProperty("installment").setValue(AmountUtils.convertToAmount(quotation.getTiInstallmentAmount(), 2));	
		}
	}
}
