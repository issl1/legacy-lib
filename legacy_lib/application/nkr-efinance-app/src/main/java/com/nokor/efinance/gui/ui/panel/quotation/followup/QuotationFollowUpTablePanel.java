package com.nokor.efinance.gui.ui.panel.quotation.followup;

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
 * @author sok.vina
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QuotationFollowUpTablePanel extends AbstractTablePanel<Quotation> implements QuotationEntityField  {

	private static final long serialVersionUID = 8658619644904210673L;
	private static final String DEALER_TYPE = "dealerType";

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
		pagedDefinition.addColumnDefinition(DEALER + "." + DEALER_TYPE + "." + DESC, I18N.message("dealer.type"), String.class, Align.LEFT, 100);
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
	protected QuotationFollowUpSearchPanel createSearchPanel() {
		return new QuotationFollowUpSearchPanel(this);		
	}
	
	/**
	 * @author youhort.ly
	 */
	private class CustomerFullNameColumnRenderer extends EntityColumnRenderer {
		/**
		 */
		private static final long serialVersionUID = -7867467154841605648L;

		@Override
		public Object getValue() {
			Applicant customer = ((Quotation) getEntity()).getApplicant();
			return customer.getIndividual().getLastNameEn() + " " + customer.getIndividual().getFirstNameEn();
		}
	}
}
