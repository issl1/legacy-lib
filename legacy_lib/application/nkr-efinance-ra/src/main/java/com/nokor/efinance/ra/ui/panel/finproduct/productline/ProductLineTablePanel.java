package com.nokor.efinance.ra.ui.panel.finproduct.productline;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.ProductLine;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Table.Align;

/**
 * Payment Condition TablePanel
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProductLineTablePanel extends AbstractTablePanel<ProductLine> implements FMEntityField {

	private static final long serialVersionUID = 2213658160564758296L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("product.lines"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("product.lines"));
		
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addAddClickListener(this);
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
	}	
		
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<ProductLine> createPagedDataProvider() {
		PagedDefinition<ProductLine> pagedDefinition = new PagedDefinition<ProductLine>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(PRODUCT_LINE_TYPE + "." + DESC, I18N.message("product.line.type"), String.class, Align.LEFT, 200);
//		pagedDefinition.addColumnDefinition("thirdPartyCompany" + "." + DESC_EN, I18N.message("insurance.company"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(PAYMENT_CONDITION_FIN + "." + DESC_EN, I18N.message("payment.condition.funding"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(PAYMENT_CONDITION_CAP + "." + DESC_EN, I18N.message("payment.condition.capital"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(PAYMENT_CONDITION_IAP + "." + DESC_EN, I18N.message("payment.condition.interest.applicant"), String.class, Align.LEFT, 300);
		pagedDefinition.addColumnDefinition(PAYMENT_CONDITION_IMA + "." + DESC_EN, I18N.message("payment.condition.interest"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(PAYMENT_CONDITION_FEE + "." + DESC_EN, I18N.message("payment.condition.fee"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<ProductLine> pagedDataProvider = new EntityPagedDataProvider<ProductLine>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected ProductLine getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(ProductLine.class, id);
		}
		return null;
	}
	
	@Override
	protected ProductLineSearchPanel createSearchPanel() {
		return new ProductLineSearchPanel(this);		
	}
}
