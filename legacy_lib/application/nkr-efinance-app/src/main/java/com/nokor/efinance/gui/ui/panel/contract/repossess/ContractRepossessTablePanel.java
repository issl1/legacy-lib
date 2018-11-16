package com.nokor.efinance.gui.ui.panel.contract.repossess;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.ui.Table.Align;

/**
 * Contract re possess table panel
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContractRepossessTablePanel extends AbstractTablePanel<Contract> implements FMEntityField {

	/** */
	private static final long serialVersionUID = -4923844187558010673L;
	
	@PostConstruct
	public void PostConstruct() {
		setCaption(I18N.message("contract.repossesses"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("contract.repossesses"));
		
		NavigationPanel navigationPanel = addNavigationPanel();
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
	}
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Contract> createPagedDataProvider() {
		PagedDefinition<Contract> pagedDefinition = new PagedDefinition<Contract>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 70);
		pagedDefinition.addColumnDefinition("contract.auctiondata.repossess.date", I18N.message("date.repossessed"), Date.class, Align.LEFT, 100, new RepossessedDateColumnRenderer());
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("applicant." + LAST_NAME_EN, I18N.message("lastname.en"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("applicant." + FIRST_NAME_EN , I18N.message("firstname.en"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("asset." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("financialProduct." + DESC_EN, I18N.message("financial.product"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("dealer." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 100);	
		pagedDefinition.addColumnDefinition("contractStatus." + DESC, I18N.message("contract.status"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("insuranceCompany." + DESC_EN, I18N.message("insurance.company"), String.class, Align.LEFT, 140);
		
		EntityPagedDataProvider<Contract> pagedDataProvider = new EntityPagedDataProvider<Contract>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}	
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */
	@Override
	protected Contract getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Contract.class, id);
		}
		return null;
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected ContractRepossessSearchPanel createSearchPanel() {
		return new ContractRepossessSearchPanel(this);		
	}
	
	private class RepossessedDateColumnRenderer extends EntityColumnRenderer{

		@Override
		public Object getValue() {
			Contract cotra = (Contract)getEntity();
//			if( cotra.getContractAuctionData() != null){		// TODO PYI

//				ContractAuctionData auctionData = cotra.getContractAuctionData();
//				return auctionData.getRequestRepossessedDate();
//			}
			return null;
		}
		
	}
}


