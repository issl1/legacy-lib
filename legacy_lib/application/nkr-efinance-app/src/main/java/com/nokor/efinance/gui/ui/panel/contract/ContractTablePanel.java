package com.nokor.efinance.gui.ui.panel.contract;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.MyNumberUtils;

import com.nokor.efinance.core.asset.model.MAsset;
import com.nokor.efinance.core.collection.model.Collection;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.contract.service.ContractUserInboxRestriction;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
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
public class ContractTablePanel extends AbstractTablePanel<Contract> implements FMEntityField {

	/** */
	private static final long serialVersionUID = 4644194602140870212L;

	public ContractTablePanel() {
		setCaption(I18N.message("contracts"));
		setSizeFull();
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("contracts"));
		
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
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 50);
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("reference"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("applicant" + "." + NAME_EN, I18N.message("name"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("collector", I18N.message("collector"), String.class, Align.LEFT, 100, new CollectorIDColumnRenderer());
		pagedDefinition.addColumnDefinition("odm", I18N.message("odm"), Integer.class, Align.LEFT, 25, new ODMColumnRenderer());
		pagedDefinition.addColumnDefinition("odi", I18N.message("odi"), Integer.class, Align.LEFT, 25, new ODIColumnRenderer());
		pagedDefinition.addColumnDefinition("dpd", I18N.message("dpd"), Integer.class, Align.LEFT, 50, new DPDColumnRenderer());
		pagedDefinition.addColumnDefinition("asset." + SERIE, I18N.message("asset"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition("financialProduct." + DESC_EN, I18N.message("financial.product"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition("dealer." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(START_DATE, I18N.message("start.date"), Date.class, Align.LEFT, 100);	
		pagedDefinition.addColumnDefinition(WKF_STATUS + "." + DESC_EN, I18N.message("contract.status"), String.class, Align.LEFT, 120);
		pagedDefinition.addColumnDefinition(ASSET + "." + MAsset.PLATE_NO, I18N.message("registration.number"), String.class, Align.LEFT, 100);
		
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
	 * 
	 * @author uhout.cheng
	 */
	private class CollectorIDColumnRenderer extends EntityColumnRenderer {
		
		/** */
		private static final long serialVersionUID = 5558928122887793644L;

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			Contract contra = ((Contract) getEntity());
			if (contra != null) {
				ContractUserInboxRestriction restrictions = new ContractUserInboxRestriction();
				restrictions.setConId(contra.getId());
				restrictions.setUserIsNull(false);
				restrictions.setProfileCodes(new String[] { IProfileCode.COL_PHO_STAFF, IProfileCode.COL_FIE_STAFF, 
						IProfileCode.COL_INS_STAFF, IProfileCode.COL_OA_STAFF });
				restrictions.addOrder(Order.desc(ContractUserInbox.ID));
				List<ContractUserInbox> inboxs = ENTITY_SRV.list(restrictions);
				if (inboxs != null && !inboxs.isEmpty()) {
					for (ContractUserInbox inbox : inboxs) {
						if (StringUtils.isEmpty(((ContractSearchPanel) searchPanel).getCollector())) {
							return inbox.getSecUser().getDesc();
						} else {
							if ((inbox.getSecUser().getDesc().equals(((ContractSearchPanel) searchPanel).getCollector()))) {
								return inbox.getSecUser().getDesc();
							}
						}
					}
				}
			}
			return StringUtils.EMPTY;
		}
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class ODMColumnRenderer extends EntityColumnRenderer {
		
		/** */
		private static final long serialVersionUID = 7830827176661222857L;

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			Collection col = ((Contract) getEntity()).getCollection();
			if (col != null) {
				return MyNumberUtils.getInteger(col.getDebtLevel());
			}
			return null;
		}
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class ODIColumnRenderer extends EntityColumnRenderer {
		
		/** */
		private static final long serialVersionUID = -6168683420742375679L;

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			Collection col = ((Contract) getEntity()).getCollection();
			if (col != null) {
				return MyNumberUtils.getInteger(col.getNbInstallmentsInOverdue());
			}
			return null;
		}
	}
	
	/**
	 * 
	 * @author uhout.cheng
	 */
	private class DPDColumnRenderer extends EntityColumnRenderer {
		
		/** */
		private static final long serialVersionUID = -7161472801563524692L;

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			Collection col = ((Contract) getEntity()).getCollection();
			if (col != null) {
				return MyNumberUtils.getInteger(col.getNbOverdueInDays());
			}
			return null;
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#createSearchPanel()
	 */
	@Override
	protected ContractSearchPanel createSearchPanel() {
		return new ContractSearchPanel(this);		
	}
	
	/**
	 * reload list of contracts
	 */
	public void reloadContract() {
		createSearchPanel();
	}
}
