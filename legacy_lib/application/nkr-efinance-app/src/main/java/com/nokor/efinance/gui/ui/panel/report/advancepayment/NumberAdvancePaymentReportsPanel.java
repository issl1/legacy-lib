package com.nokor.efinance.gui.ui.panel.report.advancepayment;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.util.DateFilterUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author uhout.cheng
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(NumberAdvancePaymentReportsPanel.NAME)
public class NumberAdvancePaymentReportsPanel extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	private static final long serialVersionUID = 148711582191868605L;

	public static final String NAME = "number.advance.payment.report";
		
	private TabSheet tabSheet;
	
	private SimplePagedTable<Dealer> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private DealerComboBox cbxDealer;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	public NumberAdvancePaymentReportsPanel() {
		super();
		setSizeFull();
	}
	
	/*
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		tabSheet = new TabSheet();
		
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setSpacing(true);
		contentLayout.setMargin(true);
		
		VerticalLayout gridLayoutPanel = new VerticalLayout();
		VerticalLayout searchLayout = new VerticalLayout();
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		Button btnSearch = new Button(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null); // null it means we don't modify key of shortcut Enter(default = 13)
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		btnSearch.addClickListener(new ClickListener() {		
			private static final long serialVersionUID = -3403059921454308342L;
			@Override
			public void buttonClick(ClickEvent event) {
				search();
			}
		});
		
		Button btnReset = new Button(I18N.message("reset"));
		btnReset.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -7165734546798826698L;
			@Override
			public void buttonClick(ClickEvent event) {
				reset();
			}
		});
		buttonsLayout.setSpacing(true);
		buttonsLayout.addComponent(btnSearch);
		buttonsLayout.addComponent(btnReset);
		
		final GridLayout gridLayout = new GridLayout(9, 1);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(I18N.message("dealer"), ENTITY_SRV.list(getDealerRestriction()), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		cbxDealerType.setImmediate(true);
		cbxDealerType.addValueChangeListener(new ValueChangeListener() {
			/** */
			private static final long serialVersionUID = -4569345302444826020L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				BaseRestrictions<Dealer> restrictions = getDealerRestriction();
				if (cbxDealerType.getSelectedEntity() != null) {
					restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
				}
				cbxDealer.setDealers(ENTITY_SRV.list(restrictions));
				cbxDealer.setSelectedEntity(null);
			}
		});
		
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
        gridLayout.addComponent(new FormLayout(dfStartDate), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(25, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(dfEndDate), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(25, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(cbxDealerType), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(25, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(cbxDealer), iCol, 0);
        
        gridLayoutPanel.addComponent(gridLayout);
        
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Dealer>(this.columnDefinitions);
        
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("advance.payment"));
        
        return tabSheet;
	}
	
	/**
	 * @return
	 */
	private BaseRestrictions<Dealer> getDealerRestriction () {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		return restrictions;
	}

	/**
	 * Reset
	 */
	public void reset() {
		cbxDealer.setSelectedEntity(null);
		cbxDealerType.setSelectedEntity(null);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	private void search() {
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		restrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("id", cbxDealer.getSelectedEntity().getId()));
		}
		setIndexedContainer(ENTITY_SRV.list(restrictions));
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Dealer> dealers) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (dealers != null && !dealers.isEmpty()) {
			for (Dealer dealer : dealers) {
				AdvancePaymentByDealer advancePaymentByDealer = getTotalNumberAdvancePayemntByDealer(dealer);
				Item item = indexedContainer.addItem(dealer.getId());
				item.getItemProperty(DEALER + "." + NAME_EN).setValue(dealer.getNameEn());
				item.getItemProperty("advance.payment.percentage10").setValue(advancePaymentByDealer.getNumTotalAdvancePaymentPercentage10());
				item.getItemProperty("advance.payment.percentage20").setValue(advancePaymentByDealer.getNumTotalAdvancePaymentPercentage20());
				item.getItemProperty("advance.payment.percentage30").setValue(advancePaymentByDealer.getNumTotalAdvancePaymentPercentage30());
				item.getItemProperty("advance.payment.percentage40").setValue(advancePaymentByDealer.getNumTotalAdvancePaymentPercentage40());
				item.getItemProperty("advance.payment.percentage50").setValue(advancePaymentByDealer.getNumTotalAdvancePaymentPercentage50());
				item.getItemProperty("advance.payment.percentage60").setValue(advancePaymentByDealer.getNumTotalAdvancePaymentPercentage60());
				item.getItemProperty("advance.payment.percentage70").setValue(advancePaymentByDealer.getNumTotalAdvancePaymentPercentage70());
			}
		}
		
		pagedTable.refreshContainerDataSource();
		
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage10", I18N.message("10%"), Integer.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage20", I18N.message("20%"), Integer.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage30", I18N.message("30%"), Integer.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage40", I18N.message("40%"), Integer.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage50", I18N.message("50%"), Integer.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage60", I18N.message("60%"), Integer.class, Align.CENTER, 70));
		columnDefinitions.add(new ColumnDefinition("advance.payment.percentage70", I18N.message("70%"), Integer.class, Align.CENTER, 70));
		
		return columnDefinitions;

	}
	
	/**
	 * 
	 * @param dealer
	 * @return list of advancePaymentByDealer
	 */
	private AdvancePaymentByDealer getTotalNumberAdvancePayemntByDealer(Dealer dealer) {
		BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
		restrictions.addCriterion(Restrictions.eq(DEALER + "." + ID, dealer.getId()));
		if (dfStartDate.getValue() != null) {  
			restrictions.addCriterion(Restrictions.ge("firstSubmissionDate", DateFilterUtil.getStartDate(dfStartDate.getValue())));
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("firstSubmissionDate", DateFilterUtil.getEndDate(dfEndDate.getValue())));
		}
		List<Quotation> quotations = ENTITY_SRV.list(restrictions);
		int numTotalAdvancePayment10 = 0;
		int numTotalAdvancePayment20 = 0;
		int numTotalAdvancePayment30 = 0;
		int numTotalAdvancePayment40 = 0;
		int numTotalAdvancePayment50 = 0;
		int numTotalAdvancePayment60 = 0;
		int numTotalAdvancePayment70 = 0;
		
		if (quotations != null && !quotations.isEmpty()) {
			for (Quotation quotation : quotations) {
				if (quotation.getTerm() != null) {
					if (quotation.getAdvancePaymentPercentage() <= 10) {
						numTotalAdvancePayment10++;
					} else if (quotation.getAdvancePaymentPercentage() <= 20) {
						numTotalAdvancePayment20++;
					} else if (quotation.getAdvancePaymentPercentage() <= 30) {
						numTotalAdvancePayment30++;
					} else if (quotation.getAdvancePaymentPercentage() <= 40) {
						numTotalAdvancePayment40++;
					} else if (quotation.getAdvancePaymentPercentage() <= 50) {
						numTotalAdvancePayment50++;
					} else if (quotation.getAdvancePaymentPercentage() <= 60) {
						numTotalAdvancePayment60++;
					} else if (quotation.getAdvancePaymentPercentage() > 60) {
						numTotalAdvancePayment70++;
					}
				}
				
			}
		}
		AdvancePaymentByDealer advancePaymentByDealer = new AdvancePaymentByDealer();
		advancePaymentByDealer.setNumTotalAdvancePaymentPercentage10(numTotalAdvancePayment10);
		advancePaymentByDealer.setNumTotalAdvancePaymentPercentage20(numTotalAdvancePayment20);
		advancePaymentByDealer.setNumTotalAdvancePaymentPercentage30(numTotalAdvancePayment30);
		advancePaymentByDealer.setNumTotalAdvancePaymentPercentage40(numTotalAdvancePayment40);
		advancePaymentByDealer.setNumTotalAdvancePaymentPercentage50(numTotalAdvancePayment50);
		advancePaymentByDealer.setNumTotalAdvancePaymentPercentage60(numTotalAdvancePayment60);
		advancePaymentByDealer.setNumTotalAdvancePaymentPercentage70(numTotalAdvancePayment70);
		return advancePaymentByDealer;
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	/**
	 * 
	 * @author buntha.chea
	 *
	 */
	private class AdvancePaymentByDealer {
		
		private Integer numTotalAdvancePaymentPercentage10;
		private Integer numTotalAdvancePaymentPercentage20;
		private Integer numTotalAdvancePaymentPercentage30;
		private Integer numTotalAdvancePaymentPercentage40;
		private Integer numTotalAdvancePaymentPercentage50;
		private Integer numTotalAdvancePaymentPercentage60;
		private Integer numTotalAdvancePaymentPercentage70;
		/**
		 * @return the numTotalAdvancePaymentPercentage10
		 */
		public Integer getNumTotalAdvancePaymentPercentage10() {
			return numTotalAdvancePaymentPercentage10;
		}
		/**
		 * @param numTotalAdvancePaymentPercentage10 the numTotalAdvancePaymentPercentage10 to set
		 */
		public void setNumTotalAdvancePaymentPercentage10(
				Integer numTotalAdvancePaymentPercentage10) {
			this.numTotalAdvancePaymentPercentage10 = numTotalAdvancePaymentPercentage10;
		}
		/**
		 * @return the numTotalAdvancePaymentPercentage20
		 */
		public Integer getNumTotalAdvancePaymentPercentage20() {
			return numTotalAdvancePaymentPercentage20;
		}
		/**
		 * @param numTotalAdvancePaymentPercentage20 the numTotalAdvancePaymentPercentage20 to set
		 */
		public void setNumTotalAdvancePaymentPercentage20(
				Integer numTotalAdvancePaymentPercentage20) {
			this.numTotalAdvancePaymentPercentage20 = numTotalAdvancePaymentPercentage20;
		}
		/**
		 * @return the numTotalAdvancePaymentPercentage30
		 */
		public Integer getNumTotalAdvancePaymentPercentage30() {
			return numTotalAdvancePaymentPercentage30;
		}
		/**
		 * @param numTotalAdvancePaymentPercentage30 the numTotalAdvancePaymentPercentage30 to set
		 */
		public void setNumTotalAdvancePaymentPercentage30(
				Integer numTotalAdvancePaymentPercentage30) {
			this.numTotalAdvancePaymentPercentage30 = numTotalAdvancePaymentPercentage30;
		}
		/**
		 * @return the numTotalAdvancePaymentPercentage40
		 */
		public Integer getNumTotalAdvancePaymentPercentage40() {
			return numTotalAdvancePaymentPercentage40;
		}
		/**
		 * @param numTotalAdvancePaymentPercentage40 the numTotalAdvancePaymentPercentage40 to set
		 */
		public void setNumTotalAdvancePaymentPercentage40(
				Integer numTotalAdvancePaymentPercentage40) {
			this.numTotalAdvancePaymentPercentage40 = numTotalAdvancePaymentPercentage40;
		}
		/**
		 * @return the numTotalAdvancePaymentPercentage50
		 */
		public Integer getNumTotalAdvancePaymentPercentage50() {
			return numTotalAdvancePaymentPercentage50;
		}
		/**
		 * @param numTotalAdvancePaymentPercentage50 the numTotalAdvancePaymentPercentage50 to set
		 */
		public void setNumTotalAdvancePaymentPercentage50(
				Integer numTotalAdvancePaymentPercentage50) {
			this.numTotalAdvancePaymentPercentage50 = numTotalAdvancePaymentPercentage50;
		}
		/**
		 * @return the numTotalAdvancePaymentPercentage60
		 */
		public Integer getNumTotalAdvancePaymentPercentage60() {
			return numTotalAdvancePaymentPercentage60;
		}
		/**
		 * @param numTotalAdvancePaymentPercentage60 the numTotalAdvancePaymentPercentage60 to set
		 */
		public void setNumTotalAdvancePaymentPercentage60(
				Integer numTotalAdvancePaymentPercentage60) {
			this.numTotalAdvancePaymentPercentage60 = numTotalAdvancePaymentPercentage60;
		}
		/**
		 * @return the numTotalAdvancePaymentPercentage70
		 */
		public Integer getNumTotalAdvancePaymentPercentage70() {
			return numTotalAdvancePaymentPercentage70;
		}
		/**
		 * @param numTotalAdvancePaymentPercentage70 the numTotalAdvancePaymentPercentage70 to set
		 */
		public void setNumTotalAdvancePaymentPercentage70(
				Integer numTotalAdvancePaymentPercentage70) {
			this.numTotalAdvancePaymentPercentage70 = numTotalAdvancePaymentPercentage70;
		}
	}
}

