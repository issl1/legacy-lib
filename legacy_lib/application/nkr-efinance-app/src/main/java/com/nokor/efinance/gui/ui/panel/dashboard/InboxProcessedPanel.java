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
import com.nokor.efinance.core.common.IProfileCode;
import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.contract.model.ContractUserInbox;
import com.nokor.efinance.core.shared.FMEntityField;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.ApplicationDateValueRenderer;
import com.nokor.efinance.gui.ui.panel.contract.ApplicationIDValueRenderer;
import com.nokor.efinance.gui.ui.panel.contract.ApplicationNameValueRenderer;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.efinance.gui.ui.panel.contract.ReceivedDateRenderer;
import com.nokor.efinance.gui.ui.panel.contract.TransferValueRenderer;
import com.nokor.efinance.gui.ui.panel.contract.barcode.ContractBarcodePopupPanel;
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
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * @author youhort.ly
 *
 */
public class InboxProcessedPanel extends VerticalLayout implements FMEntityField, ItemClickListener, SelectedItem, ItemPerPageChangeListener {
	
	private static final long serialVersionUID = -4618786633559261506L;
	
	private static final String PRINT = "print";
		
	private EntityPagedTable<Contract> pagedTable;
	private VerticalLayout contentLayout;
	private Item selectedItem;
	private TextField txtContractId;
	private Button btnSearch;
	private ContractBarcodePopupPanel barcodePopup;
	
	public InboxProcessedPanel() {
		super();
		setSizeFull();
		setMargin(true);
		barcodePopup = new ContractBarcodePopupPanel();
		txtContractId = ComponentFactory.getTextField(false, 60, 180);
		btnSearch = new Button(I18N.message("search"));
		btnSearch.setIcon(FontAwesome.SEARCH);
		txtContractId.addFocusListener(new FocusListener() {
			
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
		txtContractId.addBlurListener(new BlurListener() {
			
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
		SecUser secUser = UserSessionManager.getCurrentUser();
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);		
		DetachedCriteria userContractSubCriteria = DetachedCriteria.forClass(ContractUserInbox.class, "cousr");
		userContractSubCriteria.createAlias("cousr.secUser", "usr", JoinType.INNER_JOIN);
		userContractSubCriteria.createAlias("usr.defaultProfile", "prf", JoinType.INNER_JOIN);
		
		if (ProfileUtil.isCMLeader()) {
			userContractSubCriteria.add(Restrictions.in("prf.code", new String[] { IProfileCode.CMSTAFF, IProfileCode.CMLEADE }));
		} else {
			userContractSubCriteria.add(Restrictions.eq("usr.id", secUser.getId()));
		}
		userContractSubCriteria.setProjection(Projections.projectionList().add(Projections.property("cousr.contract.id")));
		restrictions.addCriterion(Property.forName("id").in(userContractSubCriteria));
		
		if (StringUtils.isNotEmpty(txtContractId.getValue())) {
			restrictions.addCriterion(Restrictions.ilike(REFERENCE, txtContractId.getValue(), MatchMode.ANYWHERE));
		}
		
		restrictions.addCriterion(Restrictions.eq(WKF_STATUS, ContractWkfStatus.FIN));
		restrictions.addCriterion(Restrictions.isNull("nbPrints"));
		
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
		pagedDefinition.addColumnDefinition(EXTERNAL_REFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 125, new ApplicationIDValueRenderer());
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("contract.id"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition(APPLICANT + "." + NAME_EN , I18N.message("name"), String.class, Align.LEFT, 125, new ApplicationNameValueRenderer());
		pagedDefinition.addColumnDefinition("transfered", I18N.message("type"), String.class, Align.LEFT, 70, new TransferValueRenderer());
		pagedDefinition.addColumnDefinition(ContractUserInbox.CREATEDATE, I18N.message("received.date"), Date.class, Align.LEFT, 80, new ReceivedDateRenderer());
		pagedDefinition.addColumnDefinition(QUOTATION_DATE, I18N.message("application.date"), Date.class, Align.LEFT, 80, new ApplicationDateValueRenderer());
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(ASSET + "." + SERIE, I18N.message("asset"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(FINANCIAL_PRODUCT + "." + DESC_EN, I18N.message("product"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(WKF_STATUS + "." + DESC_EN, I18N.message("status"), String.class, Align.LEFT, 140);
		pagedDefinition.addColumnDefinition(PRINT, I18N.message("print"), Button.class, Align.CENTER, 70, new PrintColumnRenderer());
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
		template += "<div location =\"lblContractId\"/>";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"txtContractId\" />";
		template += CLOSE_TD;
		template += OPEN_TD;
		template += "<div location =\"btnSearch\" />";
		template += CLOSE_TD;
		template += CLOSE_TR;
		template += CLOSE_TABLE;
		
		cusLayout.addComponent(new Label(I18N.message("contract.id")), "lblContractId");
		cusLayout.addComponent(txtContractId, "txtContractId");
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
	 * @see com.nokor.frmk.vaadin.ui.widget.table.listener.ItemPerPageChangeListener#onItemPerPageChange(int)
	 */
	@Override
	public void onItemPerPageChange(int itemPerPage) {
		refresh();
	}
	
	/**
	 * @author bunlong.taing
	 */
	private class PrintColumnRenderer extends EntityColumnRenderer {
		/** */
		private static final long serialVersionUID = 664632996462652447L;
		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			NativeButton btnPrint = new NativeButton();
			Contract contract = (Contract) getEntity();
			btnPrint.addStyleName("btn btn-success");
			btnPrint.setIcon(FontAwesome.BARCODE);
			btnPrint.addClickListener(new ClickListener() {
				/** */
				private static final long serialVersionUID = -26299517082510451L;
				/**
				 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					barcodePopup.assignValues(contract);
					UI.getCurrent().addWindow(barcodePopup);
				}
			});
			return btnPrint;
		}
		
	}
	
}
