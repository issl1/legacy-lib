package com.nokor.efinance.gui.ui.panel.dashboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.contract.model.Contract;
import com.nokor.efinance.core.helper.FinServicesHelper;
import com.nokor.efinance.core.shared.contract.ContractEntityField;
import com.nokor.efinance.core.workflow.ContractWkfStatus;
import com.nokor.efinance.gui.ui.panel.contract.ContractsPanel;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
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
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author buntha.chea
 */
public class CentralQueuePanel extends VerticalLayout implements ContractEntityField, ItemClickListener, SelectedItem, ItemPerPageChangeListener, FinServicesHelper {
	
	private static final long serialVersionUID = -2162299312022479255L;
	
	private Button btnBook;
	private EntityPagedTable<Contract> pagedTable;
	private VerticalLayout contentLayout;
	private Item selectedItem;
	private TextField txtApplicationId;
	private Button btnSearch;
	
	public CentralQueuePanel() {
		setCaption(I18N.message("central.queue"));
		setSizeFull();
		setMargin(true);
		txtApplicationId = ComponentFactory.getTextField(false, 60, 180);
		btnSearch = new Button(I18N.message("search"));
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnBook = new Button(I18N.message("book"));
		
		txtApplicationId.addFocusListener(new FocusListener() {
			/** */
			private static final long serialVersionUID = 6333845283205966781L;
			
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
			private static final long serialVersionUID = -1337241393559797264L;
			
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
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());        
        addComponent(contentLayout);
	}
	
	/**
	 * Search Layout for search applicant id
	 * @return
	 */
	private HorizontalLayout getsearchLayout() {
		btnBook.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = -5347171223537338570L;
			
			/**
			 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				List<Long> ids = getSelectedIds(pagedTable);
				if (ids != null && !ids.isEmpty()) {
					for (Long id : ids) {
						CONT_SRV.bookContract(id);
					}
				}
				refresh();
			}
		});
		
		btnSearch.addClickListener(new ClickListener() {
			/** */
			private static final long serialVersionUID = 5957274994260833712L;
			
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
		
		CustomLayout cusBtnBookLayout = new CustomLayout("xxx");
		String templateBtnBook = OPEN_TABLE;
		templateBtnBook += OPEN_TR;
		templateBtnBook += OPEN_TD;
		templateBtnBook += "<div location =\"btnBook\"/>";
		templateBtnBook += CLOSE_TD;
		templateBtnBook += CLOSE_TR;
		templateBtnBook += CLOSE_TABLE;
		
		cusBtnBookLayout.addComponent(btnBook, "btnBook");
		cusBtnBookLayout.setTemplateContents(templateBtnBook);
		
		CustomLayout cusSearchLayout = new CustomLayout("xxx");
		String templateSearchLayout = "<table cellspacing=\"2\" cellpadding=\"2\" style=\"border:0\" align=\"right\">";
		templateSearchLayout += OPEN_TR;
		templateSearchLayout += "<td align=\"right\">";
		templateSearchLayout += "<div location =\"lblApplicationId\"/>";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += OPEN_TD;
		templateSearchLayout += "<div location =\"txtApplicationId\" />";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += OPEN_TD;
		templateSearchLayout += "<div location =\"btnSearch\" />";
		templateSearchLayout += CLOSE_TD;
		templateSearchLayout += CLOSE_TR;
		templateSearchLayout += CLOSE_TABLE;
		
		cusSearchLayout.addComponent(new Label(I18N.message("application.id")), "lblApplicationId");
		cusSearchLayout.addComponent(txtApplicationId, "txtApplicationId");
		cusSearchLayout.addComponent(btnSearch, "btnSearch");
		cusSearchLayout.setTemplateContents(templateSearchLayout);
		
		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.setWidth(100, Unit.PERCENTAGE);
		horLayout.addComponent(cusBtnBookLayout);
		horLayout.addComponent(cusSearchLayout);
		horLayout.setComponentAlignment(cusSearchLayout, Alignment.TOP_RIGHT);
		return horLayout;
	}
	
	/**
	 * 
	 */
	public void refresh() {
		pagedTable.refresh();
	}
	
	/**
	 * @param dealer
	 * @return
	 */
	private BaseRestrictions<Contract> getRestrictions() {
		BaseRestrictions<Contract> restrictions = new BaseRestrictions<>(Contract.class);
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
		pagedDefinition.addColumnDefinition("select", "", CheckBox.class, Align.LEFT, 30, new BookCheckboxRenderer());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(APPLICANT + "." + INDIVIDUAL + "." + REFERENCE, I18N.message("applicant.id"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition(EXTERNAL_REFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition(QUOTATION_DATE, I18N.message("application.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(APPROVAL_DATE, I18N.message("approval.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(ASSET + "." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(FINANCIAL_PRODUCT + "." + DESC_EN, I18N.message("product"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(WKF_STATUS + "." + DESC_EN, I18N.message("status"), String.class, Align.LEFT, 140);
		EntityPagedDataProvider<Contract> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}

	
	/**
	 * @param pagedTable
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Long> getSelectedIds(EntityPagedTable<Contract> pagedTable) {
		ArrayList<Long> ids = new ArrayList<>();
		// int totalPage = pagedTable.getTotalAmountOfPages();
		// while (totalPage > 0) {
		//	pagedTable.setCurrentPage(totalPage--);
			for (Iterator i = pagedTable.getItemIds().iterator(); i.hasNext();) {
			    Long iid = (Long) i.next();
			    Item item = pagedTable.getItem(iid);
			    CheckBox cbSelect = (CheckBox) item.getItemProperty("select").getValue();
			    if (cbSelect.getValue()) {
			    	ids.add((Long) cbSelect.getData());
			    }
			}
		// }
		return ids;
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
	private class BookCheckboxRenderer extends EntityColumnRenderer {
		
		/** */
		private static final long serialVersionUID = -8970026866762252886L;

		/**
		 * @see com.nokor.frmk.vaadin.ui.widget.table.ColumnRenderer#getValue()
		 */
		@Override
		public Object getValue() {
			Contract contract = (Contract) getEntity();
			CheckBox cbSelect = new CheckBox();
			if (!(contract.getWkfStatus().equals(ContractWkfStatus.PEN))) {
				cbSelect.setEnabled(false);
			}
			cbSelect.setData(contract.getId());
			return cbSelect;
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
