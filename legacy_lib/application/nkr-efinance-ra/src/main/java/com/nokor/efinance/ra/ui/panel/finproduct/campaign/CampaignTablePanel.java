package com.nokor.efinance.ra.ui.panel.finproduct.campaign;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.Entity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.Campaign;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.RowRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.DeleteClickListener;
import com.nokor.frmk.vaadin.ui.widget.toolbar.event.SearchClickListener;
import com.vaadin.data.Item;
import com.vaadin.ui.Table.Align;

/**
 * 
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CampaignTablePanel extends AbstractTablePanel<Campaign> implements FMEntityField,
		DeleteClickListener, SearchClickListener {

	private static final long serialVersionUID = -2983055467054135680L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("campaigns"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		super.init(I18N.message("campaigns"));
		
		addDefaultNavigation();
	}
	
	/**
	 * Get item selected id
	 * @return
	 */
	public Long getItemSelectedId() {
		if (selectedItem != null) {
			return (Long) selectedItem.getItemProperty(ID).getValue();
		}
		return null;
	}	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Campaign> createPagedDataProvider() {
		PagedDefinition<Campaign> pagedDefinition = new PagedDefinition<Campaign>(searchPanel.getRestrictions());
		pagedDefinition.setRowRenderer(new CampaignRowRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("campaign.name"), String.class, Align.LEFT, 200);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("startdate"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(END_DATE, I18N.message("enddate"), Date.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CREATE_USER, I18N.message("created.by"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(UPDATE_USER, I18N.message("last.update.by"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition("zone.of.marketing", I18N.message("zone.of.marketing"), String.class, Align.LEFT, 100);
		
		EntityPagedDataProvider<Campaign> pagedDataProvider = new EntityPagedDataProvider<Campaign>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class CampaignRowRenderer implements RowRenderer {

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.RowRenderer#renderer(com.vaadin.data.Item, org.seuksa.frmk.model.entity.Entity)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void renderer(Item item, Entity entity) {
			Campaign campaign = (Campaign) entity;
			item.getItemProperty(ID).setValue(campaign.getId());
			item.getItemProperty(CODE).setValue(campaign.getCode());
			item.getItemProperty(DESC_EN).setValue(campaign.getDescEn());
			item.getItemProperty(START_DATE).setValue(campaign.getStartDate());
			item.getItemProperty(END_DATE).setValue(campaign.getEndDate());	
			item.getItemProperty(CREATE_USER).setValue(campaign.getCreateUser());
			item.getItemProperty(UPDATE_USER).setValue(campaign.getUpdateUser());
			item.getItemProperty("zone.of.marketing").setValue(campaign.getArea() == null ? "" : campaign.getArea().getDescLocale());
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Campaign getEntity() {
		if (selectedItem != null) {
			final Long id = (Long) selectedItem.getItemProperty(ID).getValue();
		    return ENTITY_SRV.getById(Campaign.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected CampaignSearchPanel createSearchPanel() {
		return new CampaignSearchPanel(this);		
	}
}
