package com.nokor.efinance.ra.ui.panel.insurance.campaign;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.financial.model.InsuranceCampaign;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.vaadin.ui.Table.Align;
/**
 * 
 * @author buntha.chea
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InsuranceCampaignsTablePanel extends AbstractTablePanel<InsuranceCampaign> implements FMEntityField {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7915490319973998220L;

	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("insurance.campaigns"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("insurance.campaigns"));
		
		addDefaultNavigation();
	}	
	
	/**
	 * 
	 */
	@Override
	protected PagedDataProvider<InsuranceCampaign> createPagedDataProvider() {
		PagedDefinition<InsuranceCampaign> pagedDefinition = new PagedDefinition<InsuranceCampaign>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(CODE, I18N.message("code"), String.class, Align.LEFT, 100);
		pagedDefinition.addColumnDefinition(DESC_EN, I18N.message("desc.en"), String.class, Align.LEFT, 160);
		pagedDefinition.addColumnDefinition(DESC, I18N.message("desc"), String.class, Align.LEFT, 160);
		
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition(END_DATE, I18N.message("end.date"), Date.class, Align.LEFT, 130);
		pagedDefinition.addColumnDefinition("insuranceCompany.nameEn", I18N.message("company"), String.class, Align.LEFT, 160);
		
		EntityPagedDataProvider<InsuranceCampaign> pagedDataProvider = new EntityPagedDataProvider<InsuranceCampaign>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	
	/**
	 * 
	 */
	@Override
	protected InsuranceCampaign getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(InsuranceCampaign.class, id);
		}
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	protected InsuranceCampaignsSearchPanel createSearchPanel() {
		return new InsuranceCampaignsSearchPanel(this);		
	}
	
	

}
