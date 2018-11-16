package com.nokor.efinance.core.quotation.panel;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.seuksa.frmk.i18n.I18N;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nokor.efinance.core.applicant.model.Applicant;
import com.nokor.efinance.core.quotation.model.Quotation;
import com.nokor.efinance.core.shared.quotation.QuotationEntityField;
import com.nokor.efinance.core.shared.quotation.QuotationProfileUtils;
import com.nokor.efinance.core.shared.util.ProfileUtil;
import com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel;
import com.nokor.frmk.vaadin.ui.widget.component.Timer;
import com.nokor.frmk.vaadin.ui.widget.component.Timer.TimerListener;
import com.nokor.frmk.vaadin.ui.widget.table.EntityColumnRenderer;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.table.PagedDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.EntityPagedDataProvider;
import com.nokor.frmk.vaadin.ui.widget.toolbar.NavigationPanel;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table.Align;

/**
 * @author ly.youhort
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QuotationTablePanel extends AbstractTablePanel<Quotation> implements QuotationEntityField {
	private static final long serialVersionUID = -2983055467054135680L;
			
	@PostConstruct
	public void PostConstruct() {
		
		setCaption(I18N.message("quotations"));
		setSizeFull();
		setHeight("100%");
		setMargin(true);
		setSpacing(true);
				
		super.init(I18N.message("quotations"));
		
		NavigationPanel navigationPanel = addNavigationPanel();
		if (QuotationProfileUtils.isAddQuotationAvailable()) {
			navigationPanel.addAddClickListener(this);
		}
		navigationPanel.addEditClickListener(this);
		navigationPanel.addRefreshClickListener(this);
		if (ProfileUtil.isPOS() || ProfileUtil.isUnderwriter() || ProfileUtil.isUnderwriterSupervisor() || ProfileUtil.isManager()){
			addAutoRefresh(navigationPanel);
		}
		/*
		getPagedTable().addStyleName("colortable");
		getPagedTable().setCellStyleGenerator(new Table.CellStyleGenerator() {
			private static final long serialVersionUID = 6242667432758981026L;
			@Override
			public String getStyle(Table source, Object itemId, Object propertyId) {
				if (propertyId == null) {
					Item item = source.getItem(itemId);
					String status = (String) item.getItemProperty(WKF_STATUS + "." + DESC).getValue();
					String underwriter = (String) item.getItemProperty(UNDERWRITER + "." + DESC).getValue();
					String underwriterSupervisor = (String) item.getItemProperty(UNDERWRITER_SUPERVISOR + "." + DESC).getValue();
					if (WkfQuotationStatus.PRO.getDesc().equals(status) && (StringUtils.isEmpty(underwriter) && StringUtils.isEmpty(underwriterSupervisor))) {
						return "highligh";
					}					
					if (ProfileUtil.isPOS() || ProfileUtil.isSeniorCO()) {
						if (WkfQuotationStatus.QUO.getDesc().equals(status)) {
							return "highligh-red";
						} else if (WkfQuotationStatus.RFC.getDesc().equals(status)) {
							return "highligh-orange";
						} else if (WkfQuotationStatus.AWT.getDesc().equals(status) || WkfQuotationStatus.APV.getDesc().equals(status)){
							return "highligh-yellow";
						} else if (WkfQuotationStatus.REJ.getDesc().equals(status)) {
							return "highligh-ocean";
						} else if (WkfQuotationStatus.LCG.getDesc().equals(status) || WkfQuotationStatus.ACG.getDesc().equals(status)){
							return "highligh-pink";
						} else if (WkfQuotationStatus.PPO.getDesc().equals(status)) {
							return "highligh-lightgreen";
						}
					}
				}
				return null;
			}
		});*/
	}	
	
	
	/**
	 * Get Paged definition
	 * @return
	 */
	@Override
	protected PagedDataProvider<Quotation> createPagedDataProvider() {
		PagedDefinition<Quotation> pagedDefinition = new PagedDefinition<>(searchPanel.getRestrictions());
		pagedDefinition.addColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 60);
		pagedDefinition.addColumnDefinition(APPLICANT + "." + REFERENCE, I18N.message("applicant.id"), String.class, Align.LEFT, 125);
		pagedDefinition.addColumnDefinition(REFERENCE, I18N.message("application.id"), String.class, Align.LEFT, 125);
		// pagedDefinition.addColumnDefinition(CUSTOMER, I18N.message("customer"), String.class, Align.LEFT, 150, new CustomerFullNameColumnRenderer());
		pagedDefinition.addColumnDefinition(QUOTATION_DATE, I18N.message("application.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition(ACCEPTATION_DATE, I18N.message("approval.date"), Date.class, Align.LEFT, 80);
		pagedDefinition.addColumnDefinition("elapse.time", I18N.message("time.elapsed.since.approval"), String.class, Align.LEFT, 80, new ElapsedTimeColumnRenderer());
		pagedDefinition.addColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(ASSET + "." + DESC_EN, I18N.message("asset"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(FINANCIAL_PRODUCT + "." + DESC_EN, I18N.message("product"), String.class, Align.LEFT, 150);
		pagedDefinition.addColumnDefinition(WKF_STATUS + "." + DESC_EN, I18N.message("status"), String.class, Align.LEFT, 140);
		EntityPagedDataProvider<Quotation> pagedDataProvider = new EntityPagedDataProvider<>();
		pagedDataProvider.setPagedDefinition(pagedDefinition);
		return pagedDataProvider;
	}
		
	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTablePanel#getEntity()
	 */	
	@Override
	protected Quotation getEntity() {
		final Long id = getItemSelectedId();
		if (id != null) {
		    return ENTITY_SRV.getById(Quotation.class, id);
		}
		return null;
	}
	
	@Override
	protected QuotationSearchPanel createSearchPanel() {
		return new QuotationSearchPanel(this);		
	}
	
	@SuppressWarnings("unused")
	private class CustomerFullNameColumnRenderer extends EntityColumnRenderer {
		/**
		 */
		private static final long serialVersionUID = 3378367404417575760L;

		@Override
		public Object getValue() {
			Applicant customer = ((Quotation) getEntity()).getApplicant();
			return customer.getIndividual().getLastNameEn() + " " + customer.getIndividual().getFirstNameEn();
		}
	}
	
	private class ElapsedTimeColumnRenderer extends EntityColumnRenderer {
		/** 
		 */
		private static final long serialVersionUID = 8048904270948719182L;

		@Override
		public Object getValue() {
			return "";
		}
	}
	
	/**
	 * 
	 * @author buntha.chea
	 * Auto Refresh
	 */
	public class AutoRefreshListener implements TimerListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = -892873062452046532L;
		public final static int DEFAULT_REFRESH_RATE = 30000;
		
		@Override
		public void onTimer() {
			refresh();
		}
	}
	
	/**
	 * @param navigationPanel
	 */
	private void addAutoRefresh(NavigationPanel panel) {
		final Timer timer = new Timer();
		timer.addListener(new AutoRefreshListener());
		timer.start(true, AutoRefreshListener.DEFAULT_REFRESH_RATE);
		
		final CheckBox cbAutoRefresh = new CheckBox(I18N.message("autorefresh"));
		cbAutoRefresh.setStyleName("checkbox_label_white_color");
		final ComboBox refreshRate = new ComboBox();
		cbAutoRefresh.setValue(true);
		cbAutoRefresh.addValueChangeListener(new ValueChangeListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5177860003563587324L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbAutoRefresh.getValue() == Boolean.TRUE) {
						timer.start(true, Integer.parseInt(refreshRate.getValue().toString()));			
				} else {
					timer.stop();
				}
			}
		});
		
		refreshRate.setNullSelectionAllowed(false);
		
		refreshRate.addItem(AutoRefreshListener.DEFAULT_REFRESH_RATE);
		refreshRate.setItemCaption(AutoRefreshListener.DEFAULT_REFRESH_RATE, I18N.message("second.30"));
		refreshRate.addItem(60000);
		refreshRate.setItemCaption(60000, I18N.message("minute.1"));
		refreshRate.addItem(120000);
		refreshRate.setItemCaption(120000, I18N.message("minute.2"));
		refreshRate.addItem(300000);
		refreshRate.setItemCaption(300000, I18N.message("minute.5"));
		refreshRate.addItem(600000);
		refreshRate.setItemCaption(600000, I18N.message("minute.10"));
		refreshRate.addItem(1800000);
		refreshRate.setItemCaption(1800000, I18N.message("minute.30"));
		refreshRate.select(AutoRefreshListener.DEFAULT_REFRESH_RATE);		
		refreshRate.addValueChangeListener(new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -7026910773539754604L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (cbAutoRefresh.getValue() == Boolean.TRUE) {
						timer.start(true, Integer.parseInt(refreshRate.getValue().toString()));
				}
			}
		});
		panel.addComponent(timer);
		panel.addComponent(cbAutoRefresh);
		panel.addComponent(refreshRate);
		panel.setComponentAlignment(cbAutoRefresh, Alignment.MIDDLE_RIGHT);
		panel.setComponentAlignment(refreshRate, Alignment.MIDDLE_RIGHT);
	}
}
