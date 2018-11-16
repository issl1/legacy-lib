package com.nokor.efinance.gui.ui.panel.statisticvisitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.payment.model.Payment;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.glf.statistic.model.Statistic3HoursVisitor;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

import ru.xpoft.vaadin.VaadinView;

/**
 * @author sok.vina
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(StatisticVisitorReportsPanel.NAME)
public class StatisticVisitorReportsPanel extends AbstractTabPanel implements View, FrmkServicesHelper {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "statistic.visitors.report";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	
	private SimplePagedTable<Payment> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private DealerComboBox cbxDealer;
	private SecUserDetail secUserDetail;
	private DateField dfStartDate;
	private DateField dfEndDate;
	public StatisticVisitorReportsPanel() {
		super();
		setSizeFull();
	}
	
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
		
		final GridLayout gridLayout = new GridLayout(10, 2);
		gridLayout.setSpacing(true);
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setSelectedEntity(null);
		cbxDealer.setWidth("220px");
		
		secUserDetail = getSecUserDetail(); 
		cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		
		dfStartDate = new DateField();
		dfStartDate.setValue(new Date());
		dfStartDate.setResolution(Resolution.DAY);
		dfStartDate.setResolution(Resolution.HOUR);
		dfStartDate.setResolution(Resolution.MINUTE);
		dfEndDate = new DateField();  
		dfEndDate.setValue(new Date());
		dfEndDate.setResolution(Resolution.DAY);
		dfEndDate.setResolution(Resolution.HOUR);
		dfEndDate.setResolution(Resolution.MINUTE);

        int iCol = 0;
        gridLayout.addComponent(new Label(I18N.message("dealer")), iCol++, 0);
        gridLayout.addComponent(cbxDealer, iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("start.date")), iCol++, 0);
        gridLayout.addComponent(dfStartDate, iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new Label(I18N.message("end.date")), iCol++, 0);
        gridLayout.addComponent(dfEndDate, iCol++, 0);
        
        gridLayoutPanel.addComponent(gridLayout);
        searchLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.addComponent(gridLayoutPanel);
        searchLayout.addComponent(buttonsLayout);
        
        Panel searchPanel = new Panel();
        searchPanel.setCaption(I18N.message("search"));
        searchPanel.setContent(searchLayout);
        
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<Payment>(this.columnDefinitions);
        pagedTable.setFooterVisible(true);
        pagedTable.setColumnFooter("dealer.name", I18N.message("total.visitors"));
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(horizontalLayout);
        contentLayout.setComponentAlignment(horizontalLayout, Alignment.BOTTOM_RIGHT);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        tabSheet.addTab(contentLayout, I18N.message("statistic.visitors"));
        return tabSheet;
	}
	
	/**
	 * @return BaseRestrictions
	 */
	public BaseRestrictions<Statistic3HoursVisitor> getRestrictions() {
		BaseRestrictions<Statistic3HoursVisitor> restrictions = new BaseRestrictions<Statistic3HoursVisitor>(Statistic3HoursVisitor.class);
		
		if (dfStartDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.ge("createDate", dfStartDate.getValue()));	
		}
		if (dfEndDate.getValue() != null) {
			restrictions.addCriterion(Restrictions.le("createDate", dfEndDate.getValue()));	
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealer.id", cbxDealer.getSelectedEntity().getId()));
		}
		restrictions.addOrder(Order.desc("dealer.id"));
		return restrictions;
	
	}

	public void reset() {
		cbxDealer.setSelectedEntity(secUserDetail.getDealer());
		dfStartDate.setValue(new Date());
		dfEndDate.setValue(new Date());
	}
	
	/**
	 * Search
	 */
	private void search() {
		setIndexedContainer(ENTITY_SRV.list(getRestrictions()));
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<Statistic3HoursVisitor> statisticVisitors) {
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		int totalVisitorDealer = 0;
		int totalVisitorCompany = 0;
		int totalVisitorApply = 0;
		int visitorDealer = 0;
		int visitorCompany = 0;
		int visitorApply = 0;
		int id = 1;
		if (statisticVisitors != null & !statisticVisitors.isEmpty()) {
			for (int i = 0; i < statisticVisitors.size() ; i++) {
				visitorDealer += statisticVisitors.get(i).getNumberVisitorDealer11();
				visitorDealer += statisticVisitors.get(i).getNumberVisitorDealer14();
				visitorDealer += statisticVisitors.get(i).getNumberVisitorDealer17();
				visitorCompany += statisticVisitors.get(i).getNumberVisitorCompany11();
				visitorCompany += statisticVisitors.get(i).getNumberVisitorCompany14();
				visitorCompany += statisticVisitors.get(i).getNumberVisitorCompany17();
				//visitorApply += statisticVisitors.get(i).getNumberVisitorApply();
				if (i == statisticVisitors.size() - 1
						|| statisticVisitors.get(i).getDealer() != statisticVisitors.get(i+1).getDealer()
						|| !DateUtils.getDateAtBeginningOfDay(statisticVisitors.get(i).getCreateDate())
								.equals(DateUtils.getDateAtBeginningOfDay(statisticVisitors.get(i + 1).getCreateDate()))) {
					
					final Item item = indexedContainer.addItem(statisticVisitors.get(i).getId());
					item.getItemProperty("id").setValue(id);	
					item.getItemProperty("date").setValue(statisticVisitors.get(i).getCreateDate());
					item.getItemProperty("dealer.name").setValue(statisticVisitors.get(i).getDealer().getNameEn());	
					item.getItemProperty("visitor.dealer").setValue(visitorDealer);	
					item.getItemProperty("visitor.company").setValue(visitorCompany);	
					item.getItemProperty("visitor.apply").setValue(visitorApply);		
					visitorDealer = 0;
					visitorCompany = 0;
					visitorApply = 0;
					id++;
				}
				totalVisitorDealer += statisticVisitors.get(i).getNumberVisitorDealer11();
				totalVisitorDealer += statisticVisitors.get(i).getNumberVisitorDealer14();
				totalVisitorDealer += statisticVisitors.get(i).getNumberVisitorDealer17();
				totalVisitorCompany += statisticVisitors.get(i).getNumberVisitorCompany11();
				totalVisitorCompany += statisticVisitors.get(i).getNumberVisitorCompany14();
				totalVisitorCompany += statisticVisitors.get(i).getNumberVisitorCompany17();
				//totalVisitorApply += statisticVisitors.get(i).getNumberVisitorApply();
			} 	
		}
		pagedTable.setColumnFooter("visitor.dealer", ""+totalVisitorDealer);
		pagedTable.setColumnFooter("visitor.company", ""+totalVisitorCompany);
		pagedTable.setColumnFooter("visitor.apply", ""+totalVisitorApply);
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("id", I18N.message("id"), Integer.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Date.class, Align.CENTER, 120));
		columnDefinitions.add(new ColumnDefinition("dealer.name", I18N.message("dealer"), String.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("visitor.dealer", I18N.message("visitor.dealer"), Integer.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("visitor.company", I18N.message("visitor.company"), Integer.class, Align.LEFT, 100));
		columnDefinitions.add(new ColumnDefinition("visitor.apply", I18N.message("visitor.apply"), Integer.class, Align.LEFT, 100));
		return columnDefinitions;
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		search();
	}	
	/**
	 * @return
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		EntityService entityService = SpringUtils.getBean(EntityService.class);
		return entityService.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}
}
