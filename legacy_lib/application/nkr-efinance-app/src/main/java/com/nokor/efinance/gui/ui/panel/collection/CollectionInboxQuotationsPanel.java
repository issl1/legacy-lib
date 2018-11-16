package com.nokor.efinance.gui.ui.panel.collection;

import java.util.Date;

import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;

import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.quotation.panel.QuotationsPanel;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.ersys.core.hr.model.eref.ETypeContactInfo;
import com.nokor.frmk.security.context.SecApplicationContextHolder;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.SelectedItem;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedTable;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * @author buntha.chea
 *
 */
public class CollectionInboxQuotationsPanel extends VerticalLayout implements QuotationEntityField, ItemClickListener, SelectedItem {
	
	private static final long serialVersionUID = -4618786633559261506L;
	
	private EntityPagedTable<Quotation> pagedTable;
	private VerticalLayout contentLayout;
	private Item selectedItem;
	private Button btnSearch;
	
	public ERefDataComboBox<ETypeContactInfo> cbxStatus;
	public ERefDataComboBox<ETypeContactInfo> cbxLastResult;
	
	public CollectionInboxQuotationsPanel() {
		super();
		setSizeFull();
		setMargin(true);
		btnSearch = new Button(I18N.message("search"));
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		cbxStatus = new ERefDataComboBox<>(ETypeContactInfo.class);
		cbxLastResult = new ERefDataComboBox<>(ETypeContactInfo.class);
		
		
		HorizontalLayout searchLayout = searchPanel();
		pagedTable = new EntityPagedTable<>(createPagedDataProvider());
		pagedTable.buildGrid();
		pagedTable.addItemClickListener(this);
		contentLayout = new VerticalLayout();
		contentLayout.addComponent(searchLayout);
		contentLayout.setComponentAlignment(searchLayout, Alignment.MIDDLE_RIGHT);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());        
        addComponent(contentLayout);
	}
		
	/**
	 * @param dealer
	 * @return
	 */
	private BaseRestrictions<Quotation> getRestrictions() {
		//SecUser secUser = SecApplicationContext.getSecUser();
		
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		
		/*if (secUser != null && IProfileCode.CMSTAFF.equals(secUser.getDefaultProfile().getCode())) {
			DetachedCriteria userQuotationSubCriteria = DetachedCriteria.forClass(QuotationSecUserQueue.class, "qusr");
			userQuotationSubCriteria.add(Restrictions.eq("secUser.id", secUser.getId()));
			userQuotationSubCriteria.setProjection(Projections.projectionList().add(Projections.property("qusr.quotation.id")));
			restrictions.addCriterion(Property.forName("id").in(userQuotationSubCriteria));
			restrictions.addCriterion(Restrictions.in("wkfStatus", new EWkfStatus[] {QuotationWkfStatus.INPRO}));
		} else {
			restrictions.addCriterion(Restrictions.in("wkfStatus", new EWkfStatus[] {QuotationWkfStatus.ASS_LEV1}));
		}
		
		if (StringUtils.isNotEmpty(txtApplicantId.getValue())) {
			restrictions.addAssociation("applicant", "app", JoinType.INNER_JOIN);
			restrictions.addCriterion(Restrictions.ilike("app.reference", txtApplicantId.getValue(), MatchMode.ANYWHERE));
		}*/
		return restrictions;
	}
	
	/**
	 * @param dealer
	 * @return
	 */
	protected PagedDataProvider<Quotation> createPagedDataProvider() {
		PagedDefinition<Quotation> pagedDefinition = new PagedDefinition<>(getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition("branch", I18N.message("branch"), String.class, Align.LEFT, 100, new TestColumnRender());
		pagedDefinition.addColumnDefinition("contract.no", I18N.message("contract.no"), String.class, Align.LEFT, 100, new TestColumnRender());
		pagedDefinition.addColumnDefinition("firstname", I18N.message("firstname.en"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("lastname", I18N.message("lastname.en"), String.class, Align.LEFT, 145, new TestColumnRender());
		pagedDefinition.addColumnDefinition("due.date", I18N.message("due.date"), Date.class, Align.LEFT, 100, new TestColumnRender());
		pagedDefinition.addColumnDefinition("installment.vat", I18N.message("installment.vat"), String.class, Align.LEFT, 100, new TestColumnRender());
		pagedDefinition.addColumnDefinition("installment.overdue", I18N.message("installment.overdue"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("overdue.amount.a", I18N.message("overdue.amount.a"), String.class, Align.LEFT, 100, new TestColumnRender());
		pagedDefinition.addColumnDefinition("current.penalty.b", I18N.message("current.penalty.b"), String.class, Align.LEFT, 100, new TestColumnRender());
		pagedDefinition.addColumnDefinition("pressing.fee.c", I18N.message("pressing.fee.c"), String.class, Align.LEFT, 90, new TestColumnRender());
		pagedDefinition.addColumnDefinition("following.fee.d", I18N.message("following.fee.d"), String.class, Align.LEFT, 90, new TestColumnRender());
		pagedDefinition.addColumnDefinition("total.a+b+c+d", I18N.message("total.a+b+c+d"), String.class, Align.LEFT, 110, new TestColumnRender());
		
		pagedDefinition.addColumnDefinition("end.month.penalty", I18N.message("end.month.penalty"), Date.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("outstanding.balance", I18N.message("outstanding.balance"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("number.of.overdue", I18N.message("number.of.overdue"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("full.block", I18N.message("full.block"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("latest.payment.date", I18N.message("latest.payment.date"), Date.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("latest.lock.split.deadline", I18N.message("latest.lock.split.deadline"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("first.due.1.to.3", I18N.message("first.due.1.to.3"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("last.result.date", I18N.message("last.result.date"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("last.result", I18N.message("last.result"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("latest.staff", I18N.message("latest.staff"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("current.staff", I18N.message("current.staff"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("debt.level", I18N.message("debt.level"), String.class, Align.LEFT, 110, new TestColumnRender());
		pagedDefinition.addColumnDefinition("status", I18N.message("status"), String.class, Align.LEFT, 110, new TestColumnRender());
		EntityPagedDataProvider<Quotation> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
	
	/**
	 * Search Panel for search applicant id
	 * @return
	 */
	public HorizontalLayout searchPanel() {
		btnSearch.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8362492762531823674L;

			@Override
			public void buttonClick(ClickEvent event) {
				pagedTable.getPagedDefinition().setRestrictions(getRestrictions());
				refresh();
			}
		});
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(new Label(I18N.message("status")));
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horizontalLayout.addComponent(cbxStatus);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		horizontalLayout.addComponent(new Label(I18N.message("last.resutl")));
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(5, Unit.PIXELS));
		horizontalLayout.addComponent(cbxLastResult);
		horizontalLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS));
		horizontalLayout.addComponent(btnSearch);
		return horizontalLayout;
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
			Page.getCurrent().setUriFragment("!" + QuotationsPanel.NAME + "/" + getItemSelectedId());
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

	@Override
	public Item getSelectedItem() {
		return this.selectedItem;
	}
	/**
	 * 
	 * @author buntha.chea
	 *test column in table because not yet to have data 
	 */
	private class TestColumnRender extends EntityColumnRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Object getValue() {
			final Quotation quotation = (Quotation) getEntity();
			
			return quotation.getTerm();
		}
		
	}
}
