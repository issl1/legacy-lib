package com.nokor.efinance.ra.ui.panel.buyer;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.auction.model.Buyer;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractSearchPanel;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;

/**
 * Buyer table panel
 * @author bunlong.taing
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BuyerTablePanel extends AbstractTablePanel<Buyer> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -2806992304487269395L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("buyers"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("buyers"));
		addDefaultNavigation();
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createPagedDataProvider()
	 */
	@Override
	protected PagedDataProvider<Buyer> createPagedDataProvider() {
		PagedDefinition<Buyer> pagedDefinition = new PagedDefinition<Buyer>(searchPanel.getRestrictions());
		
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(FIRST_NAME_EN, I18N.message("firstname.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(TELEPHONE, I18N.message("telephone"), String.class, Align.LEFT, 150);
		
		EntityPagedDataProvider<Buyer> pagedDataProvider = new EntityPagedDataProvider<Buyer>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Buyer getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Buyer.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected AbstractSearchPanel<Buyer> createSearchPanel() {
		return new BuyerSearchPanel(this);
	}

}
