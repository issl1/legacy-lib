package com.nokor.efinance.gui.ui.panel.statisticconfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Restrictions;
import org.seuksa.frmk.dao.hql.BaseRestrictions;
import org.seuksa.frmk.i18n.I18N;
import org.seuksa.frmk.service.EntityService;
import org.seuksa.frmk.tools.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.nokor.efinance.core.dealer.model.Dealer;
import com.nokor.efinance.core.shared.asset.AssetEntityField;
import com.nokor.efinance.glf.statistic.model.StatisticConfig;
import com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel;
import com.nokor.frmk.vaadin.ui.widget.component.ButtonFactory;
import com.nokor.frmk.vaadin.ui.widget.component.ComponentFactory;
import com.nokor.frmk.vaadin.ui.widget.component.NumberField;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox;
import com.nokor.frmk.vaadin.ui.widget.dialog.MessageBox.ButtonType;
import com.nokor.frmk.vaadin.ui.widget.table.ColumnDefinition;
import com.nokor.frmk.vaadin.ui.widget.table.impl.SimpleTable;
import com.nokor.frmk.vaadin.ui.widget.toolbar.ToolbarButtonsPanel;
import com.nokor.frmk.vaadin.util.exporter.ExcelExporter;
import com.vaadin.data.Container;
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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * StatisticConfigs Panel
 * @author bunlong.taing
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@VaadinView(StatisticConfigsAllDealersPanel.NAME)
public class StatisticConfigsAllDealersPanel extends AbstractTabPanel implements View, AssetEntityField {

	private static final long serialVersionUID = 2553573409276658002L;

	public static final String NAME = "statistic.config";
	
	private SimpleTable<StatisticConfig> statisticTable;
	private List<StatisticConfig> listStatisticConfig;
	private List<String> errors;
	private DateField dfMonth;
	private Button btnSearch;
	private Button btnReset;
	private Date date;
	private Button btnSave;
	private Button btnExcel;
	
	@Autowired
	private EntityService entityService;
	
	@PostConstruct
	public void PostConstruct() {
		listStatisticConfig = new ArrayList<>();
		date = DateUtils.today();
		assignValue(date);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ColumnDefinition> createColumnDefinition() {
		List<ColumnDefinition> columnDefinitions = new ArrayList<>();
		
		columnDefinitions.add(new ColumnDefinition(ID, I18N.message("id"), Long.class, Align.LEFT, 40));
		columnDefinitions.add(new ColumnDefinition(DEALER + "." + NAME_EN, I18N.message("dealer"), String.class, Align.LEFT, 230));
		columnDefinitions.add(new ColumnDefinition("startDate", I18N.message("months"), String.class, Align.CENTER, 130));
		columnDefinitions.add(new ColumnDefinition("targetLow", I18N.message("target.low"), NumberField.class, Align.LEFT, 160));
		columnDefinitions.add(new ColumnDefinition("targetHigh", I18N.message("target.high"), NumberField.class, Align.LEFT, 160));
		
		return columnDefinitions;
	}
	
	@SuppressWarnings("unchecked")
	public void setContainerDataSource(List<StatisticConfig> statisticConfigs) {
		statisticTable.setPageLength(15);
		Container indexedContainer = statisticTable.getContainerDataSource();
		indexedContainer.removeAllItems();
		
		for(StatisticConfig statisticConfig : statisticConfigs) {
			Item item = indexedContainer.addItem(statisticConfig.getDealer().getId());
			SimpleDateFormat simpleDateFormate = new SimpleDateFormat("MM/yyyy");
			String strDate = simpleDateFormate.format(statisticConfig.getStartDate());
			
			item.getItemProperty(ID).setValue(statisticConfig.getDealer().getId());
			item.getItemProperty(DEALER + "." + NAME_EN).setValue(statisticConfig.getDealer().getNameEn());
			item.getItemProperty("startDate").setValue(strDate);
			
			NumberField txtTargetHigh = ComponentFactory.getNumberField();
			NumberField txtTargetLow = ComponentFactory.getNumberField();
			
			txtTargetHigh.setValue("" + statisticConfig.getTargetHigh());
			txtTargetLow.setValue("" + statisticConfig.getTargetLow());
			
			item.getItemProperty("targetHigh").setValue(txtTargetHigh);
			item.getItemProperty("targetLow").setValue(txtTargetLow);
		}
	}

	/**
	 * assignValue
	 * @param date
	 */
	public void assignValue(Date date) {
		this.listStatisticConfig = getListStatisticConfig(date);
		setContainerDataSource(this.listStatisticConfig);
	}
	
	/**
	 * Get statisticConfig base on dealer and date provided
	 * @param dealer
	 * @param date
	 * @return
	 */
	public StatisticConfig getStatisticConfig(Dealer dealer, Date date) {
		BaseRestrictions<StatisticConfig> restrictions = new BaseRestrictions<StatisticConfig>(StatisticConfig.class);
		restrictions.addCriterion(Restrictions.eq("dealer", dealer));
		restrictions.addCriterion(Restrictions.ge("startDate", DateUtils.getDateAtBeginningOfMonth(date)));
		restrictions.addCriterion(Restrictions.le("startDate", DateUtils.getDateAtEndOfMonth(date)));
		List<StatisticConfig> list = entityService.list(restrictions);
		return (list != null && !list.isEmpty())? list.get(0) : null;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public List<StatisticConfig> getListStatisticConfig(Date date) {
		List<StatisticConfig> list = new ArrayList<>();
		BaseRestrictions<Dealer> restrictions = new BaseRestrictions<Dealer>(Dealer.class);
		List<Dealer> dealers = entityService.list(restrictions);

		for (Dealer dealer : dealers) {
			StatisticConfig statisticConfig = getStatisticConfig(dealer, date);
			if (statisticConfig == null) {
				statisticConfig = new StatisticConfig();
				statisticConfig.setDealer(dealer);
				statisticConfig.setTargetHigh(0);
				statisticConfig.setTargetLow(0);
				statisticConfig.setStartDate(date);
			}
			list.add(statisticConfig);
		}
		return list;
	}

	/**
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	/**
	 * @see com.nokor.frmk.vaadin.ui.panel.AbstractTabPanel#createForm()
	 */
	@Override
	protected com.vaadin.ui.Component createForm() {
		TabSheet tabSheet = new TabSheet();
		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);

		statisticTable = new SimpleTable<StatisticConfig>(createColumnDefinition());

		ToolbarButtonsPanel tblButtonsPanel = new ToolbarButtonsPanel();
		btnSave = ButtonFactory.getSaveButton();
		
		btnExcel = new NativeButton();
		btnExcel.setCaption(I18N.message("excel"));
		btnExcel.setIcon(new ThemeResource("../nkr-default/icons/16/excel.png"));
		btnExcel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -345128294252780849L;
			public void buttonClick(ClickEvent event) {
	        	final ExcelExporter excelExp = new ExcelExporter();
	            excelExp.setContainerToBeExported(statisticTable.getContainerDataSource());
	            excelExp.sendConvertedFileToUser(getUI());
	        }
	    });

		tblButtonsPanel.addButton(btnSave);
		tblButtonsPanel.addButton(btnExcel);

		btnSave.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1717161084451001316L;
			@Override
			public void buttonClick(ClickEvent event) {
				onSave();
			}
		});

		contentLayout.addComponent(tblButtonsPanel, 0);
		contentLayout.addComponent(createSearchLayout());
		contentLayout.addComponent(statisticTable);

		tabSheet.addTab(contentLayout, I18N.message("statistic.config"));

		return tabSheet;
	}
	
	/**
	 * Save or update
	 */
	public void onSave() {
		errors = new ArrayList<>();
		if (listStatisticConfig != null & !listStatisticConfig.isEmpty()) {
			for (StatisticConfig statisticConfig : listStatisticConfig) {
				Item item = statisticTable.getItem(statisticConfig.getDealer().getId());
				Integer targetLow = getInteger(((NumberField) item.getItemProperty("targetLow").getValue()));
				Integer targetHigh = getInteger((NumberField) item.getItemProperty("targetHigh").getValue());

				if (targetLow > targetHigh) {
					errors.add(I18N.message("target.of.dealer.is.invalid", statisticConfig.getDealer().getNameEn()));
				} else {
					statisticConfig.setStartDate(DateUtils.getDateAtBeginningOfMonth(date));
					statisticConfig.setTargetLow(targetLow);
					statisticConfig.setTargetHigh(targetHigh);
					entityService.saveOrUpdate(statisticConfig);
				}
			}
		}
		if (errors.isEmpty()) {
			MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("information"),
					MessageBox.Icon.INFO, I18N.message("save.successfully"), Alignment.MIDDLE_RIGHT,
					new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
			mb.show();
		} else {
			String errorsMessage = "";
			for (String error : errors) {
				errorsMessage += error + "\n";
			}
			Notification.show("", errorsMessage, Type.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Search layout
	 * @return panel
	 */
	private Panel createSearchLayout() {
		Panel panel = new Panel();

		FormLayout formLayout = new FormLayout();
		dfMonth = new DateField(I18N.message("months"));
		dfMonth.setImmediate(true);
		dfMonth.setResolution(Resolution.MONTH);
		dfMonth.setValue(new Date());
		dfMonth.setDateFormat("MM/yyyy");

		formLayout.addComponent(dfMonth);
		
		btnSearch = ComponentFactory.getButton(I18N.message("search"));
		btnSearch.setClickShortcut(KeyCode.ENTER, null);
		btnSearch.setIcon(new ThemeResource("../nkr-default/icons/16/search.png"));
		
		btnReset = ComponentFactory.getButton(I18N.message("reset"));
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		horizontalLayout.addComponent(btnSearch);
		horizontalLayout.addComponent(btnReset);

		btnSearch.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = -4228138908371860297L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (dfMonth.getValue() != null) {
					date = dfMonth.getValue();
					assignValue(date);
				} else {
					MessageBox mb = new MessageBox(UI.getCurrent(), "320px", "140px", I18N.message("error"),
							MessageBox.Icon.ERROR, I18N.message("the.field.require.can't.null.or.empty"), Alignment.MIDDLE_RIGHT,
							new MessageBox.ButtonConfig(ButtonType.OK, I18N.message("ok")));
					mb.show();
				}
			}
		});
		btnReset.addClickListener(new ClickListener() {

			/** */
			private static final long serialVersionUID = 957403754682560705L;

			@Override
			public void buttonClick(ClickEvent event) {
				dfMonth.setValue(new Date());
				assignValue(new Date());
			}
		});

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.addComponent(formLayout);
		layout.addComponent(horizontalLayout);

		panel.setContent(layout);

		return panel;
	}

}
