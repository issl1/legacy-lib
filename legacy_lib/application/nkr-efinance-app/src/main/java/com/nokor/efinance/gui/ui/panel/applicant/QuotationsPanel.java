package com.nokor.efinance.gui.ui.panel.applicant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.spring.SpringUtils;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.model.QuotationApplicant;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * Quotations panel
 * @author ly.youhort
 */
public class QuotationsPanel extends AbstractTabPanel implements QuotationEntityField {
	
	private static final long serialVersionUID = 2202264472024719484L;
		
	private EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	private List<ColumnDefinition> columnDefinitions;
	private SimplePagedTable<QuotationApplicant> pagedTable;
		
	public QuotationsPanel() {
		super();
		setSizeFull();
	}
	
	@Override
	protected Component createForm() {
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSizeFull();
		this.columnDefinitions = createColumnDefinitions();
		pagedTable = new SimplePagedTable<QuotationApplicant>(this.columnDefinitions);
		contentLayout.addComponent(pagedTable);
        return contentLayout;
				
	}
	/**
	 * Get indexed container
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IndexedContainer getIndexedContainer(Applicant applicant) {
		IndexedContainer indexedContainer = new IndexedContainer();
					
		for (ColumnDefinition column : this.columnDefinitions) {
			indexedContainer.addContainerProperty(column.getPropertyId(), column.getPropertyType(), null);
		}
		indexedContainer.addContainerProperty(REFERENCE, String.class, null);
		indexedContainer.addContainerProperty(ASSET, String.class, null);
		indexedContainer.addContainerProperty(DEALER, String.class, null);
		indexedContainer.addContainerProperty(WKF_STATUS, String.class, null);
		indexedContainer.addContainerProperty(APPLICANT_TYPE, String.class, null);
		indexedContainer.addContainerProperty(QUOTATION_DATE, String.class, null);
		
		BaseRestrictions<QuotationApplicant> restrictions = new BaseRestrictions<QuotationApplicant>(QuotationApplicant.class);	
		restrictions.addCriterion(Restrictions.eq("applicant.id", applicant.getId()));
		
		List<QuotationApplicant> quotationApplicants = entityService.list(restrictions);
		
		for (QuotationApplicant quotationApplicant : quotationApplicants) {
			Quotation quotation = quotationApplicant.getQuotation();
			Item item = indexedContainer.addItem(quotation.getId());
			item.getItemProperty(ID).setValue(quotation.getId());
			item.getItemProperty(REFERENCE).setValue(quotation.getReference());
			item.getItemProperty(ASSET).setValue(quotation.getAsset().getDescEn());
			item.getItemProperty(DEALER).setValue(quotation.getDealer().getNameEn());
			item.getItemProperty(WKF_STATUS).setValue(quotation.getWkfStatus().getDesc());
			item.getItemProperty(APPLICANT_TYPE).setValue(quotationApplicant.getApplicantType().getDesc());
			item.getItemProperty(QUOTATION_DATE).setValue(quotation.getQuotationDate());
		}
		
		return indexedContainer;
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 80));
		columnDefinitions.add(new ColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 140));
		columnDefinitions.add(new ColumnDefinition(ASSET, I18N.message("asset"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(DEALER, I18N.message("dealer"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(WKF_STATUS, I18N.message("status"), String.class, Align.LEFT, 200));
		columnDefinitions.add(new ColumnDefinition(QUOTATION_DATE, I18N.message("quotation.date"), Date.class, Align.LEFT, 120));
		return columnDefinitions;
	}
	
	/**
	 * Set quotation
	 * @param applicant
	 */
	public void assignValues(Applicant applicant) {
		if (applicant != null) {
			pagedTable.setContainerDataSource(getIndexedContainer(applicant));
		} else {
			pagedTable.removeAllItems();
		}
	}
	
	/**
	 * Reset panel
	 */
	public void reset() {
		pagedTable.removeAllItems();
	}

	/**
	 * @return
	 */
	public boolean isValid() {
		super.removeErrorsPanel();
		if (!errors.isEmpty()) {
			super.displayErrorsPanel();
		}
		return errors.isEmpty();
	}

}
