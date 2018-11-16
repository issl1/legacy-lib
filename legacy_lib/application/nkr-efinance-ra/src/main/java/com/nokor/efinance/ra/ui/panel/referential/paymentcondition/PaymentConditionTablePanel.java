package com.nokor.efinance.ra.ui.panel.referential.paymentcondition;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.payment.model.EPaymentCondition;
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
public class PaymentConditionTablePanel extends AbstractTablePanel<EPaymentCondition> implements FMEntityField, 
			DeleteClickListener, SearchClickListener{


	private static final long serialVersionUID = 1L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("payment.conditions"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("payment.conditions"));
		
		addDefaultNavigation();
	}	

	@Override
	protected PagedDataProvider<EPaymentCondition> createPagedDataProvider() {
		PagedDefinition<EPaymentCondition> pagedDefinition = new PagedDefinition<EPaymentCondition>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(PAYMENT_METHOD + "." + DESC_EN, I18N.message("payment.method"), String.class, Align.LEFT, 100);	
		pagedDefinition.addColumnDefinition("delay", I18N.message("delay"), Integer.class, Align.LEFT, 100);	
		EntityPagedDataProvider<EPaymentCondition> pagedDataProvider = new EntityPagedDataProvider<EPaymentCondition>();
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
	protected EPaymentCondition getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(EPaymentCondition.class, id);
		}
		return null;
	}
	
	@Override
	protected PaymentConditionSearchPanel createSearchPanel() {
		return new PaymentConditionSearchPanel(this);		
	}
}
