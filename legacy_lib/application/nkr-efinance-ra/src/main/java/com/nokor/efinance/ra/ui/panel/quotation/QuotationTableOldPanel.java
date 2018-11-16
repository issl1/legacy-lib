package com.nokor.efinance.ra.ui.panel.quotation;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.DateColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QuotationTableOldPanel extends AbstractTablePanel<Quotation> implements QuotationEntityField  {
	
	private static final long serialVersionUID = 7079747579949643586L;
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("quotations"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("quotations"));
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
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition(CUSTOMER, I18N.message("customer"), String.class, Align.LEFT, 150, new CustomerFullNameColumnRenderer());
		pagedDefinition.addColumnDefinition(ASSET + "." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(WKF_STATUS + "." + DESC, I18N.message("status"), String.class, Align.LEFT, 190);
		pagedDefinition.addColumnDefinition(QUOTATION_DATE, I18N.message("quotation.date"), Date.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(SUBMISSION_DATE, I18N.message("submission.date"), String.class, Align.LEFT, 120, new DateColumnRenderer(DateUtils.FORMAT_YYYYMMDD_HHMMSS_SLASH));
		
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
	
	@Override
	protected QuotationSearchOldPanel createSearchPanel() {
		return new QuotationSearchOldPanel(this);		
	}
	
	/**
	 * @author youhort.ly
	 */
	private class CustomerFullNameColumnRenderer extends EntityColumnRenderer {
		/**
		 */
		private static final long serialVersionUID = -500947935013500608L;

		@Override
		public Object getValue() {
			Applicant customer = ((Quotation) getEntity()).getApplicant();
			return customer.getIndividual().getLastNameEn() + " " + customer.getIndividual().getFirstNameEn();
		}
	}
}
