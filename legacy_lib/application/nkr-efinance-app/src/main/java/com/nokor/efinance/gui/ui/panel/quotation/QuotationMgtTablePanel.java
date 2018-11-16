package com.nokor.efinance.gui.ui.panel.quotation;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.HeaderClickEvent;

/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QuotationMgtTablePanel extends AbstractTablePanel<Quotation> implements QuotationEntityField {
	private static final long serialVersionUID = -2983055467054135680L;
		
	private ArrayList<Long> selectInverts = new ArrayList<Long>();
	private boolean selectAll = false;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("quotations.to.stamp"));
		setSizeFull();
		setHeight("100%");
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("quotations.to.stamp"));
		
		NavigationPanel navigationPanel = addNavigationPanel();	
		navigationPanel.addRefreshClickListener(this);
		
		pagedTable.setColumnIcon("selectall", new ThemeResource("../nkr-default/icons/16/tick.png"));
		pagedTable.addHeaderClickListener(new com.vaadin.ui.Table.HeaderClickListener() {
			private static final long serialVersionUID = 7098577026875043963L;
			@Override
			public void headerClick(HeaderClickEvent event) {
				if (event.getPropertyId() == "selectall") {
					selectAll = !selectAll;
					selectInverts.clear();
					pagedTable.refresh();
				}
			}
		});
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Quotation> createPagedDataProvider() {
		PagedDefinition<Quotation> pagedDefinition = new PagedDefinition<Quotation>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new QuotationRowRenderer());
		pagedDefinition.addColumnDefinition("selectall", "", CheckBox.class, Align.LEFT, 30);
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition(CUSTOMER, I18N.message("customer"), String.class, Align.LEFT, 150, new CustomerFullNameColumnRenderer());
		pagedDefinition.addColumnDefinition("creditOfficer." + DESC, I18N.message("credit.officer"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(ASSET + "." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("dealer.type", I18N.message("dealer.type"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(QUOTATION_DATE, I18N.message("quotation.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(ACCEPTATION_DATE, I18N.message("acceptation.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(CONTRACT_START_DATE, I18N.message("contract.date"), Date.class, Align.LEFT, 80);
		EntityPagedDataProvider<Quotation> pagedDataProvider = new EntityPagedDataProvider<Quotation>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	private class QuotationRowRenderer implements RowRenderer, QuotationEntityField {
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Quotation quotation = (Quotation) entity;
			item.getItemProperty("selectall").setValue(getRenderSelected(quotation.getId()));
			item.getItemProperty(ID).setValue(quotation.getId());
			item.getItemProperty(REFERENCE).setValue(quotation.getReference());
			item.getItemProperty(CUSTOMER).setValue(quotation.getApplicant().getIndividual().getLastNameEn() + " " + quotation.getApplicant().getIndividual().getFirstNameEn());
			item.getItemProperty("creditOfficer." + DESC).setValue(quotation.getCreditOfficer().getDesc());
			item.getItemProperty(ASSET + "." + DESC_EN).setValue(quotation.getAsset().getDescEn());
			item.getItemProperty("dealer.type").setValue(quotation.getDealer().getDealerType().getDesc());
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(quotation.getDealer().getNameEn());
			item.getItemProperty(QUOTATION_DATE).setValue(quotation.getQuotationDate());
			item.getItemProperty(ACCEPTATION_DATE).setValue(quotation.getAcceptationDate());
			item.getItemProperty(CONTRACT_START_DATE).setValue(quotation.getContractStartDate());
		}
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
	
	@Override
	protected QuotationMgtSearchPanel createSearchPanel() {
		return new QuotationMgtSearchPanel(this);		
	}
	
	private class CustomerFullNameColumnRenderer extends EntityColumnRenderer {
		@Override
		public Object getValue() {
			Applicant customer = ((Quotation) getEntity()).getApplicant();
			return customer.getIndividual().getLastNameEn() + " " + customer.getIndividual().getFirstNameEn();
		}
	}
	
	/**
	 * @param paymentId
	 * @return
	 */
	private CheckBox getRenderSelected(Long quotaId) {
		final CheckBox checkBox = new CheckBox();
		boolean check = false;
		if (selectAll) {
			if (!selectInverts.contains(quotaId)) {
				check = true;
			}
		} else if (selectInverts.contains(quotaId)) {
			check = true;
		}
		checkBox.setValue(check);
		checkBox.setData(quotaId);
		checkBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 153504804651053033L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				Long id = (Long) checkBox.getData();
				if (selectAll) {
					if (!checkBox.getValue()) {
						selectInverts.add(id);
					} else {
						selectInverts.remove(id);
					}
				} else {
					if (checkBox.getValue()) {
						selectInverts.add(id);
					} else {
						selectInverts.remove(id);
					}
				}
			}
		});
		return checkBox;
	}
	
	/**
	 * @param pagedTable
	 * @return
	 */
	private ArrayList<Long> getSelectedQuotations(EntityPagedTable<Quotation> pagedTable) {
		ArrayList<Long> quotaIds = new ArrayList<Long>();
		int totalPage = pagedTable.getTotalAmountOfPages();
		while (totalPage > 0) {
			pagedTable.setCurrentPage(totalPage--);
			for (Iterator i = pagedTable.getItemIds().iterator(); i.hasNext();) {
			    Long iid = (Long) i.next();
			    Item item = pagedTable.getItem(iid);
			    Long quotaId = (Long) item.getItemProperty(ID).getValue();
			    if (selectAll) {
					if (!selectInverts.contains(quotaId)) {
						quotaIds.add(quotaId);
					}
				} else {
					if (selectInverts.contains(quotaId)) {
						quotaIds.add(quotaId);
					}
				}
			}
		}
		return quotaIds;
	}
	
	public void resetSelectStamp() {
		selectInverts.clear();
		selectAll = false;
	}
}
