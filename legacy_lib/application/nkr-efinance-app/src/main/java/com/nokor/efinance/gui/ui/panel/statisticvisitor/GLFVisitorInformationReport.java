package com.nokor.efinance.gui.ui.panel.statisticvisitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.common.security.model.SecUserDetail;
import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.dealer.model.EDealerType;
import com.nokor.efinance.core.shared.cashflow.CashflowEntityField;
import com.nokor.efinance.core.shared.referencial.DataReference;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.efinance.core.widget.DealerComboBox;
import com.nokor.efinance.glf.statistic.model.StatisticVisitor;
import com.nokor.frmk.helper.FrmkServicesHelper;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.combo.ERefDataComboBox;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
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
 * GLF visitor information report
 * @author uhout.cheng
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(GLFVisitorInformationReport.NAME)
public class GLFVisitorInformationReport extends AbstractTabPanel implements View, CashflowEntityField, FrmkServicesHelper {
	private static final long serialVersionUID = 148711582191868605L;

	public static final String NAME = "GLF.visitor.information.report";
	
	private TabSheet tabSheet;
	
	private SimplePagedTable<Dealer> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private ERefDataComboBox<EDealerType> cbxDealerType;
	
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	private DealerComboBox cbxDealer;
	
	/**
	 * 
	 */
	public GLFVisitorInformationReport() {
		super();
		setSizeFull();
	}
	
	/**
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
		
		final GridLayout gridLayout = new GridLayout(8, 1);
		gridLayout.setSpacing(true);
		
		dfStartDate = ComponentFactory.getAutoDateField("start.date", false);
		dfStartDate.setValue(DateUtils.today());
		dfEndDate = ComponentFactory.getAutoDateField("end.date", false);
		dfEndDate.setValue(DateUtils.today());
		cbxDealer = new DealerComboBox(null, DataReference.getInstance().getDealers(), I18N.message("all"));
		cbxDealer.setCaption(I18N.message("dealer"));
		
		List<EDealerType> dealerTypes = EDealerType.values();
		cbxDealerType = new ERefDataComboBox<EDealerType>(dealerTypes);
		cbxDealerType.setImmediate(true);
		cbxDealerType.setWidth("220px");
		cbxDealerType.setCaption(I18N.message("dealer.type"));
		
		SecUserDetail secUserDetail = getSecUserDetail(); 
		if (ProfileUtil.isPOS() && secUserDetail != null && secUserDetail.getDealer() != null) {
			cbxDealer.setSelectedEntity(secUserDetail.getDealer());
			cbxDealerType.setSelectedEntity(secUserDetail.getDealer().getDealerType() != null ? 
					secUserDetail.getDealer().getDealerType() : null);
			cbxDealer.setEnabled(false);
			cbxDealerType.setEnabled(false);
		} else {
			cbxDealer.setEnabled(true);
			cbxDealerType.setEnabled(true);
		}
		
		if(ProfileUtil.isCreditOfficerMovable()){
			cbxDealer.setEnabled(true);
			cbxDealerType.setEnabled(true);
		}

        int iCol = 0;
        gridLayout.addComponent(new FormLayout(dfStartDate), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(dfEndDate), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(cbxDealerType), iCol++, 0);
        gridLayout.addComponent(ComponentFactory.getSpaceLayout(10, Unit.PIXELS), iCol++, 0);
        gridLayout.addComponent(new FormLayout(cbxDealer), iCol++, 0);
        
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
        
        tabSheet.addTab(contentLayout, I18N.message("glf.visitor.info"));
        
        setIndexedContainer();
        
        return tabSheet;
	}
	
	/**
	 * @return 
	 */
	private SecUserDetail getSecUserDetail() {
		SecUser secUser = (SecUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ENTITY_SRV.getByField(SecUserDetail.class, "secUser.id", secUser.getId());
	}

	/**
	 * Reset
	 */
	public void reset() {
		dfStartDate.setValue(DateUtils.today());
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	private void search() {	
		
		setIndexedContainer();
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer() {
		BaseRestrictions<StatisticVisitor> restrictions = new BaseRestrictions<StatisticVisitor>(StatisticVisitor.class);
		restrictions.addAssociation("dealer", "dea", JoinType.INNER_JOIN);
		if(dfStartDate.getValue() !=null){
			restrictions.addCriterion(Restrictions.ge("createDate", DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
		}
		if(dfEndDate.getValue() !=null){
			restrictions.addCriterion(Restrictions.le("createDate", DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
		}
		if (cbxDealerType.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dea.dealerType", cbxDealerType.getSelectedEntity()));
		}
		if (cbxDealer.getSelectedEntity() != null) {
			restrictions.addCriterion(Restrictions.eq("dealer", cbxDealer.getSelectedEntity()));
		}
		List<StatisticVisitor> statisticVisitors = ENTITY_SRV.list(restrictions);
		Indexed indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		if (statisticVisitors != null && !statisticVisitors.isEmpty()) {
			for (StatisticVisitor statisticVisitor : statisticVisitors) {
				Item item = indexedContainer.addItem(statisticVisitor.getId());
				item.getItemProperty("date").setValue(statisticVisitor.getCreateDate());
				item.getItemProperty("family").setValue(statisticVisitor.getFamilyName());
				item.getItemProperty("firstname").setValue(statisticVisitor.getFirstName());
				item.getItemProperty("phonenumber").setValue(statisticVisitor.getPhoneNumber());
				if(statisticVisitor.getWayOfKnowing() != null){
					item.getItemProperty("how.applicant.know.GLf").setValue(statisticVisitor.getWayOfKnowing().getDescEn());
				}
				item.getItemProperty("product.feedback").setValue(statisticVisitor.getProductFeedback());
				if(statisticVisitor.getComplaint() != null){
					item.getItemProperty("complaints").setValue(statisticVisitor.getComplaint().getDescEn());
				}
			}
		}
		
		pagedTable.refreshContainerDataSource();
		
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition("date", I18N.message("date"), Date.class, Align.LEFT, 130));
		columnDefinitions.add(new ColumnDefinition("family", I18N.message("familyname"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("firstname", I18N.message("firstname.en"), String.class, Align.LEFT,120));
		columnDefinitions.add(new ColumnDefinition("phonenumber", I18N.message("phonenumber"), String.class, Align.LEFT, 120));
		columnDefinitions.add(new ColumnDefinition("how.applicant.know.GLf", I18N.message("how.applicant.know.glf"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("product.feedback", I18N.message("product.feedback"), String.class, Align.LEFT, 180));
		columnDefinitions.add(new ColumnDefinition("complaints", I18N.message("complaints"), String.class, Align.LEFT, 180));
		
		return columnDefinitions;

	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
}

