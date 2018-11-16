package com.nokor.efinance.ra.ui.panel.referential.paymentmethod;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.payment.model.EPaymentMethod;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.ui.Table.Align;
/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaymentMethodTablePanel extends AbstractTablePanel<EPaymentMethod> implements FMEntityField, 
			DeleteClickListener, SearchClickListener{


	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("payment.methods"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("payment.methods"));
		
		addDefaultNavigation();
	}	

	@Override
	protected PagedDataProvider<EPaymentMethod> createPagedDataProvider() {
		PagedDefinition<EPaymentMethod> pagedDefinition = new PagedDefinition<EPaymentMethod>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(CATEGORY_PAYMENT_METHOD + "." + DESC_EN, I18N.message("category.payment.method"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(SERVICE + "." + DESC_EN, I18N.message("service"), String.class, Align.LEFT, 200);
		
		EntityPagedDataProvider<EPaymentMethod> pagedDataProvider = new EntityPagedDataProvider<EPaymentMethod>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		
		return pagedDataProvider;
	}
	/**
	 * 
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(ID).getValue();
		}
		return null;
	}
	
	@Override
	protected EPaymentMethod getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(EPaymentMethod.class, id);
		}
		return null;
	}
	
	@Override
	protected PaymentMethodSearchPanel createSearchPanel() {
		return new PaymentMethodSearchPanel(this);		
	}
}
