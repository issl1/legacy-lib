package com.nokor.efinance.gui.ui.panel.dashboard;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.common.app.tools.UserSessionManager;
import com.nokor.common.app.workflow.model.EWkfStatus;
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ContractSimulationWkfStatus;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable;
import com.nokor.frmk.vaadin.ui.widget.table.listener.ItemPerPageChangeListener;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author youhort.ly
 *
 */
public class InboxTransferedPanel extends VerticalLayout implements FMEntityField, FinServicesHelper, ItemClickListener, SelectedItem, ItemPerPageChangeListener {
	
	private static final long serialVersionUID = -4618786633559261506L;
	
	
	private EntityPagedTable<Contract> pagedTable;
	private VerticalLayout contentLayout;
	private Item selectedItem;
	private TextField txtApplicationId;
	private Button btnSearch;
	
	public InboxTransferedPanel() {
		super();
		setSizeFull();
		setMargin(true);
		txtApplicationId = ComponentFactory.getTextField(false, 60, 180);
		btnSearch = new Button(I18N.message("search"));
		btnSearch.setIcon(FontAwesome.SEARCH);
		txtApplicationId.addFocusListener(new FocusListener() {
			
			/** */
			private static final long serialVersionUID = 4153133000943068964L;
			
			/**
			 * @see com.vaadin.event.FieldEvents.FocusListener#focus(com.vaadin.event.FieldEvents.FocusEvent)
			 */
			@Override
			public void focus(FocusEvent event) {
				btnSearch.setClickShortcut(KeyCode.ENTER, null);
			}
		});
		txtApplicationId.addBlurListener(new BlurListener() {
			
			/** */
			private static final long serialVersionUID = 2753277147623202412L;
			
			/**
			 * @see com.vaadin.event.FieldEvents.BlurListener#blur(com.vaadin.event.FieldEvents.BlurEvent)
			 */
			@Override
			public void blur(BlurEvent event) {
				btnSearch.removeClickShortcut();
			}
		});
		
		HorizontalLayout searchLayout = getsearchLayout();
		pagedTable = new EntityPagedTable<>(createPagedDataProvider());
		pagedTable.buildGrid();
		pagedTable.setCaption(null);
		pagedTable.addItemClickListener(this);
		pagedTable.setItemPerPageChangeListener(this);
		contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.addComponent(searchLayout);
		contentLayout.setComponentAlignment(searchLayout, Alignment.MIDDLE_RIGHT);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());        
        addComponent(contentLayout);
	}
		
	/**
	 * 
	 * @return
	 */
	private BaseRestrictions<Contract> getRestrictions() {
		
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
		restrictions.addAssociation("contractSimulations", "consim", JoinType.LEFT_OUTER_JOIN);
		
		restrictions.addCriterion(Restrictions.in(WKF_STATUS, new EWkfStatus[] {ContractWkfStatus.FIN}));
		restrictions.addCriterion(Restrictions.eq("consim." + WKF_STATUS, ContractSimulationWkfStatus.SIMULATED));
			
		SecUser secUser = UserSessionManager.getCurrentUser();

		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
		userContractSubCriteria.createAlias("usr.defaultProfile", "prf", JoinType.INNER_JOIN);
		
		if (ProfileUtil.isCMLeader()) {
			userContractSubCriteria.add(Restrictions.or(
					Restrictions.eq("prf.code", IProfileCode.CMLEADE),
					Restrictions.eq("usr.id", secUser.getId())));
		} else {
			userContractSubCriteria.add(Restrictions.eq("usr.id", secUser.getId()));
		}
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));		
		restrictions.addCriterion(Property.forName("id").in(userContractSubCriteria));
		
		if (StringUtils.isNotEmpty(txtApplicationId.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(EXTERNAL_REFERENCE, txtApplicationId.getValue(), MatchMode.ANYWHERE));
		}
				
		restrictions.addOrder(Order.desc(START_DATE));
		
		return restrictions;
	}
	
	/**
	 * @param dealer
	 * @return
	 */
	protected PagedDataProvider<Contract> createPagedDataProvider() {
		PagedDefinition<Contract> pagedDefinition = new PagedDefinition<>(getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(APPLICANT + "." + NAME_EN , I18N.message("name"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition(EXTERNAL_REFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition(QUOTATION_DATE, I18N.message("application.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(ASSET + "." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(FINANCIAL_PRODUCT + "." + DESC_EN, I18N.message("product"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(WKF_STATUS + "." + DESC_EN, I18N.message("status"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition("unbook", "", Button.class, Align.LEFT, 80, new UnbookButtonRenderer());
		EntityPagedDataProvider<Contract> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * Search Layout for search applicant id
	 * @return
	 */
	private HorizontalLayout getsearchLayout() {
		btnSearch.addClickListener(new ClickListener() {
			
			/** */
			private static final long serialVersionUID = -8362492762531823674L;

			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				pagedTable.getPagedDefinition().setRestrictions(getRestrictions());
				refresh();
			}
		});
		String OPEN_TABLE = "<table cellspacing=\"2\" cellpadding=\"2\" style=\"border:0\" >";
		String OPEN_TR = "<tr>";
		String OPEN_TD = "<td>";
		String CLOSE_TR = "</tr>";
		String CLOSE_TD = "</td>";
		String CLOSE_TABLE = "</table>";
		
		CustomLayout cusLayout = new CustomLayout("xxx");
		String template = OPEN_TABLE;
		template += OPEN_TR;
		template += "<td align=\"right\">";
		template += "<div location =\"lblApplicationId\"/>";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtApplicationId\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"btnSearch\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		
		cusLayout.addComponent(new Label(I18N.message("application.id")), "lblApplicationId");
		cusLayout.addComponent(txtApplicationId, "txtApplicationId");
		cusLayout.addComponent(btnSearch, "btnSearch");
		cusLayout.setTemplateContents(template);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.addComponent(cusLayout);
		return horLayout;
	}
	
	/**
	 * 
	 */
	public void refresh() {
		pagedTable.refresh();
	}
	
	/**
	 * @see com.vaadin.event.ItemClickEvent.ItemClickListener#itemClick(com.vaadin.event.ItemClickEvent)
	 */
	@Override
	public void itemClick(ItemClickEvent event) {
		this.selectedItem = event.getItem();
		if (event.isDoubleClick() || SecApplicationContextHolder.getContext().clientDeviceIsMobileOrTablet()) {
			Page.getCurrent().setUriFragment("!" + ContractsPanel.NAME + "/" + getItemSelectedId());
		}
	}
	
	
	/**
	 * @return
	 */
	public Long getItemSelectedId() {
		if (this.selectedItem != null) {
			return ((Long) this.selectedItem.getItemProperty("id").getValue());
		}
		return null;
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.SelectedItem#getSelectedItem()
	 */
	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	
	/**
	 * @author youhort.ly
	 */
	private class UnbookButtonRenderer extends EntityColumnRenderer {
		
		/** */
		private static final long serialVersionUID = -4503142422589840441L;

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			final Contract contract = (Contract) getEntity();
			Button btnUnbook = new Button(I18N.message("unbook"));
			// btnUnbook.setVisible(contract.getWkfStatus().equals(ContractWkfStatus.PEN));
			btnUnbook.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 4769888412014416921L;
				@Override
				public void buttonClick(ClickEvent event) {
					CONT_SRV.unbookContract(UserSessionManager.getCurrentUser(), contract.getId());
					refresh();
				}
			});
			return btnUnbook;
		}
	}
	
	/**
	 * @see com.nokor.frmk.vaadin.ui.widget.table.listener.ItemPerPageChangeListener#onItemPerPageChange(int)
	 */
	@Override
	public void onItemPerPageChange(int itemPerPage) {
		refresh();
	}
}
