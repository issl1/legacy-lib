package com.nokor.efinance.gui.ui.panel.report.creditofficer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.model.entity.EStatusRecord;
import org.seuksa.frmk.model.entity.Entity;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.seuksa.frmk.tools.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.system.FMProfile;
import com.nokor.efinance.core.workflow.QuotationWkfStatus;
import com.nokor.frmk.security.model.SecUser;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.AutoDateField;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimplePagedTable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;

/**
 * @author mao.heng
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(PerformanceCreditOfficerPanel.NAME)
public class PerformanceCreditOfficerPanel extends AbstractTabPanel implements View, QuotationEntityField {
	
	private static final long serialVersionUID = -4618786633559261506L;

	public static final String NAME = "performance.co";
	
	private final EntityService entityService = SpringUtils.getBean(EntityService.class);
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private TabSheet tabSheet;
	private SimplePagedTable<PerformanceCreditOfficer> pagedTable;
	private List<ColumnDefinition> columnDefinitions;
	private AutoDateField dfStartDate;
	private AutoDateField dfEndDate;
	
	public PerformanceCreditOfficerPanel() {
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
		
		final GridLayout gridLayout = new GridLayout(12, 1);
		gridLayout.setSpacing(true);
		
		dfStartDate = ComponentFactory.getAutoDateField("", false);
		dfEndDate = ComponentFactory.getAutoDateField("", false);    
		dfEndDate.setValue(DateUtils.today());

        int iCol = 0;
        
        iCol = 0;
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
        
        this.columnDefinitions = createColumnDefinitions();
        pagedTable = new SimplePagedTable<PerformanceCreditOfficer>(this.columnDefinitions);
        contentLayout.addComponent(searchPanel);
        contentLayout.addComponent(pagedTable);
        contentLayout.addComponent(pagedTable.createControls());
        
        tabSheet.addTab(contentLayout, I18N.message("Performance Credit Officer"));
        
        return tabSheet;
	}
	
	public void reset() {
		dfStartDate.setValue(null);
		dfEndDate.setValue(DateUtils.today());
	}
	
	/**
	 * Search
	 */
	public void search() {
		
		List<PerformanceCreditOfficer> performanceCreditOfficers = new ArrayList<PerformanceCreditOfficer>();
		
		BaseRestrictions<SecUser> secUserRestrictions = new BaseRestrictions<>(SecUser.class);
		secUserRestrictions.addCriterion(Restrictions.eq("defaultProfile.id", FMProfile.CO));
		secUserRestrictions.getStatusRecordList().add(EStatusRecord.ACTIV);
		
		List<SecUser> users = entityService.list(secUserRestrictions);
		
		for (SecUser user : users) {
			if (!"co".equals(user.getLogin())) {
				BaseRestrictions<Quotation> restrictions = new BaseRestrictions<>(Quotation.class);
				restrictions.addCriterion(Restrictions.eq(WKF_STATUS, QuotationWkfStatus.ACT));
				restrictions.addCriterion(Restrictions.eq("creditOfficer.id", user.getId()));
				if (dfStartDate.getValue() != null) {       
					restrictions.addCriterion(Restrictions.ge(CONTRACT_START_DATE, DateUtils.getDateAtBeginningOfDay(dfStartDate.getValue())));
				}
				if (dfEndDate.getValue() != null) {
					restrictions.addCriterion(Restrictions.le(CONTRACT_START_DATE, DateUtils.getDateAtEndOfDay(dfEndDate.getValue())));
				}
				long nbContract = entityService.count(restrictions);
				
				performanceCreditOfficers.add(new PerformanceCreditOfficer(user.getId(), user, nbContract));
			}
		}
		
		Collections.sort(performanceCreditOfficers, new ComparatorByNbContract());
		setIndexedContainer(performanceCreditOfficers);
	}
	
	/**
	 * Get indexed container
	 * @return IndexedContainer
	 */
	@SuppressWarnings("unchecked")
	private void setIndexedContainer(List<PerformanceCreditOfficer> performanceCreditOfficers) {
		Container indexedContainer = pagedTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		for (PerformanceCreditOfficer performanceCreditOfficer : performanceCreditOfficers) {
			Item item = indexedContainer.addItem(performanceCreditOfficer.getId());
			item.getItemProperty(ID).setValue(performanceCreditOfficer.getId());
			item.getItemProperty("co").setValue(performanceCreditOfficer.getCreditOfficer().getDesc());
			item.getItemProperty("nb.contract").setValue(performanceCreditOfficer.getNbContract());
		}
		pagedTable.refreshContainerDataSource();
	}

	/**
	 * @return List of ColumnDefinition
	 */
	protected List<ColumnDefinition> createColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 140, false));
		columnDefinitions.add(new ColumnDefinition("co", I18N.message("credit.officer"), String.class, Align.LEFT, 150));
		columnDefinitions.add(new ColumnDefinition("nb.contract", I18N.message("nb.contract"), Long.class, Align.LEFT, 140));
		return columnDefinitions;
	}
	
	/**
	 * Refresh
	 */
	public void refresh() {
		search();
	}
	/**
	 * @return the tabSheet
	 */
	public TabSheet getTabSheet() {
		return tabSheet;
	}
	
	/**
	 * @author ly.youhort
	 */
	private class ComparatorByNbContract implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			PerformanceCreditOfficer c1 = (PerformanceCreditOfficer) o1;
			PerformanceCreditOfficer c2 = (PerformanceCreditOfficer) o2;
			if (c1.getNbContract() == c2.getNbContract()) {
				return 0;
			} else if (c1.getNbContract() < c2.getNbContract()) {
				return 1;
			}
			return -1;
		}
	}
	
	/**
	 * @author ly.youhort
	 */
	private class PerformanceCreditOfficer implements Serializable, Entity {
		private static final long serialVersionUID = 3112339520304252300L;
		private Long id;
		private SecUser creditOfficer;
		private long nbContract;
		
		/**
		 * @param creditOfficer
		 * @param nbContract
		 */
		public PerformanceCreditOfficer(Long id, SecUser creditOfficer, long nbContract) {
			this.id = id;
			this.creditOfficer = creditOfficer;
			this.nbContract = nbContract;
		}
				
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}		
		
		/**
		 * @return the creditOfficer
		 */
		public SecUser getCreditOfficer() {
			return creditOfficer;
		}
		
		/**
		 * @return the nbContract
		 */
		public long getNbContract() {
			return nbContract;
		}
		

	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
